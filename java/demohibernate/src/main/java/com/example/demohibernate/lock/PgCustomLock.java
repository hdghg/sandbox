package com.example.demohibernate.lock;

import liquibase.GlobalConfiguration;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.exception.LockException;
import liquibase.lockservice.DatabaseChangeLogLock;
import liquibase.lockservice.LockService;
import liquibase.servicelocator.PrioritizedService;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PgCustomLock implements LockService {

    private static final Logger log = LoggerFactory.getLogger(PgCustomLock.class);

    public static final String LOCK_TABLE_NAME = "customlock";

    private ScheduledExecutorService executor;

    private ExtendedPostgresDatabase database;
    private Long changeLogLockWaitTime;
    private Long changeLogLocRecheckTime;

    /**
     * @implSpec The lock table designed in such way shat it can contain 1 row maximum
     * <dl>
     *     <dt>Section 0</dt>
     *     <dd>When it contains 0 rows it means that previous execution of locking service finished normally. This
     *      case is handled by section 0 of the code (See comments). To avoid race conditions, the service verifies
     *      value of the 'updated' property.
     *     </dd>
     *     <dt>Section 1</dt>
     *     <dd>When it contains 1 row it means that a parallel service may hold a lock. Section 1 verifies that
     *      and if detected that lock is held, returns false
     *     </dd>
     *     <dt>Section 2</dt>
     *     <dd>When it contains 1 row and it's locked_until value is in the past. This may happen in following
     *     cases: 1) previous migration completed abnormally; 2) section 0 scenario was unable to acquire lock.
     *     Section 2 tries to acquire a lock by executing SQL with `where locked_until = ?`, this ensures the lock
     *     is acquired by only one service.</dd>
     * </dl>
     */
    private boolean acquireCustomLock() throws SQLException {
        try (Connection c = database.getDataSource().getConnection()) {
            executeStatement(c, s -> s.executeUpdate("""
                        create table if not exists %s(
                            id varchar(255) unique not null DEFAULT 'application' CHECK (id in ('application')),
                            locked_until timestamp)
                    """.formatted(LOCK_TABLE_NAME)));
            // section: 0
            Long count = executeQuery(c, "select count(1) from " + LOCK_TABLE_NAME, Long.class);
            if (Long.valueOf(0).equals(count)) {
                Integer updated = executeStatement(c, s -> s.executeUpdate("""
                        insert into %s(locked_until)
                        values (clock_timestamp() + interval '1 minute')
                        on conflict do nothing""".formatted(LOCK_TABLE_NAME)));
                log.debug("count = 0; updated = {}", updated);
                return updated > 0;
            }
            // section: 1
            Timestamp lockedUntil = executeQuery(c, """
                    select locked_until from %s
                    where locked_until < clock_timestamp()""".formatted(LOCK_TABLE_NAME), Timestamp.class);
            if (lockedUntil == null) {
                log.debug("count <> 0; lockedUntil = null");
                return false;
            }
            try {
                log.debug("sleeping 20 sec, debugging RC");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // section: 2
            try (PreparedStatement ps = c.prepareStatement("""
                    update %s set locked_until = clock_timestamp() + interval '1 minute'
                    where locked_until = ?
                    """.formatted(LOCK_TABLE_NAME))) {
                ps.setTimestamp(1, lockedUntil);
                int preparedStatementUpdated = ps.executeUpdate();
                log.debug("count <> 0; lockedUntil = {}, updated = {}", lockedUntil, preparedStatementUpdated);
                return preparedStatementUpdated > 0;
            }
        }
    }

    private void scheduleAutoPostponeLockRelease() {
        BasicThreadFactory tf = new BasicThreadFactory.Builder()
                .daemon(true)
                .namingPattern("liq-bg-lock-%d")
                .build();
        executor = new ScheduledThreadPoolExecutor(1, tf);
        executor.scheduleAtFixedRate(() -> {
            try (Connection c = database.getDataSource().getConnection();
                 Statement s = c.createStatement()) {
                log.info("Postponing locked_until...");
                s.executeUpdate("update %s set locked_until = clock_timestamp() + interval '1 minute'".formatted(LOCK_TABLE_NAME));
            } catch (Exception ex) {
                log.error("Unexpected error", ex);
                throw new RuntimeException(ex);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * @implNote Calls database.rollback() at the beginning, so that CREATE INDEX CONCURRENT would not block
     * if executed in parallel service. This is considered a safe operation because standard lock implementation
     * does the same when it acquires a lock {@link liquibase.lockservice.StandardLockService#acquireLock}
     */
    @Override
    public void waitForLock() throws LockException {
        try {
            database.rollback();
            log.info("Trying to acquire the changelog lock...");
            Duration duration = Duration.ofMinutes(getChangeLogLockWaitTime());
            Instant end = Instant.now().plus(duration);
            boolean locked = false;
            while (!locked && Instant.now().isBefore(end)) {
                locked = acquireCustomLock();
                if (!locked) {
                    log.info("Waiting for changelog lock....");
                    TimeUnit.SECONDS.sleep(getChangeLogLockRecheckTime());
                } else {
                    scheduleAutoPostponeLockRelease();
                }
            }
            if (!locked) {
                throw new LockException("Could not acquire change log lock after " + duration);
            }
        } catch (SQLException | DatabaseException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockException(e);
        }
    }

    @Override
    public void releaseLock() throws LockException {
        log.info("Releasing changelog lock.");
        if (executor != null) {
            executor.shutdownNow();
        }
        try (Connection c = database.getDataSource().getConnection()) {
            executeStatement(c, s -> s.executeUpdate("delete from " + LOCK_TABLE_NAME));
        } catch (SQLException e) {
            throw new LockException(e);
        }
    }

    private <T> T executeStatement(Connection c, SqlFunction<Statement, T> fun) throws SQLException {
        try (Statement statement = c.createStatement()) {
            return fun.apply(statement);
        }
    }

    private <T> T executeQuery(Connection c, String sql, Class<T> tClass) throws SQLException {
        try (Statement statement = c.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getObject(1, tClass);
            }
            return null;
        }
    }

    @Override
    public boolean supports(Database database) {
        return database instanceof ExtendedPostgresDatabase;
    }

    // --------------- Config setters and getters ------------------------
    @Override
    public int getPriority() {
        return PrioritizedService.PRIORITY_DEFAULT + 1;
    }

    @Override
    public void setDatabase(Database database) {
        this.database = ((ExtendedPostgresDatabase) database);
    }

    @Override
    public void setChangeLogLockWaitTime(long changeLogLockWaitTime) {
        this.changeLogLockWaitTime = changeLogLockWaitTime;
    }

    @Override
    public void setChangeLogLockRecheckTime(long changeLogLocRecheckTime) {
        this.changeLogLocRecheckTime = changeLogLocRecheckTime;
    }

    public Long getChangeLogLockWaitTime() {
        if (changeLogLockWaitTime != null) {
            return changeLogLockWaitTime;
        }
        return GlobalConfiguration.CHANGELOGLOCK_WAIT_TIME.getCurrentValue();
    }

    public Long getChangeLogLockRecheckTime() {
        if (changeLogLocRecheckTime != null) {
            return changeLogLocRecheckTime;
        }
        return GlobalConfiguration.CHANGELOGLOCK_POLL_RATE.getCurrentValue();
    }

    // ------------------ no action -------------
    @Override
    public void reset() {
    }

    @Override
    public void init() {
    }

    // ------------- not used ---------------------
    @Override
    public boolean hasChangeLogLock() {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public DatabaseChangeLogLock[] listLocks() {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public void forceReleaseLock() throws LockException {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not used");
    }

    @Override
    public boolean acquireLock() throws LockException {
        throw new UnsupportedOperationException("Not used");
    }

}
