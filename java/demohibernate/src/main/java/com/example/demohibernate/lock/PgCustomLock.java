package com.example.demohibernate.lock;

import liquibase.GlobalConfiguration;
import liquibase.database.Database;
import liquibase.exception.LockException;
import liquibase.lockservice.DatabaseChangeLogLock;
import liquibase.lockservice.LockService;
import liquibase.servicelocator.PrioritizedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class PgCustomLock implements LockService {

    private static final Logger log = LoggerFactory.getLogger(PgCustomLock.class);

    private ExtendedPostgresDatabase database;
    private Connection connection;
    private Long changeLogLockWaitTime;
    private Long changeLogLocRecheckTime;

    private boolean executeFunction(Connection con, String funcName) throws SQLException {
        var sql = "select %s(%s)".formatted(funcName, database.getLockFunctionArgs());
        log.info("trying '%s' ...".formatted(sql));
        try (
                var stmt = con.createStatement();
                var rs = stmt.executeQuery(sql);
        ) {
            var res = rs.next() && rs.getBoolean(1);
            log.info("... '%s' completed with result: %s".formatted(sql, res));
            return res;
        }
    }

    @Override
    public void waitForLock() throws LockException {
        DataSource dataSource = this.database.getDataSource();
        boolean locked = false;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Duration duration = Duration.ofMinutes(getChangeLogLockWaitTime());
            Instant end = Instant.now().plus(duration);
            while (!locked && Instant.now().isBefore(end)) {
                locked = executeFunction(connection, "pg_try_advisory_xact_lock");
                if (!locked) {
                    log.info("Waiting for changelog lock....");
                    TimeUnit.SECONDS.sleep(getChangeLogLockRecheckTime());
                }
            }
            if (!locked) {
                throw new LockException("Could not acquire change log lock after " + duration);
            }
        } catch (SQLException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockException(e);
        } finally {
            if (!locked) {
                releaseConnection();
            }
        }
    }

    @Override
    public void releaseLock() throws LockException {
        releaseConnection();
    }

    private void releaseConnection() throws LockException {
        if (this.connection != null) {
            log.info("Releasing connection and its lock");
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new LockException(e);
            }
        }
        this.connection = null;
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
