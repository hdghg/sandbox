package com.example.demohibernate.lock;

import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ResourceAccessor;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class MySpringLiquibaseConfiguration {

    private final LiquibaseProperties properties;

    public MySpringLiquibaseConfiguration(LiquibaseProperties properties) {
        this.properties = properties;
    }


    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase() {
            @Override
            protected Database createDatabase(Connection c, ResourceAccessor resourceAccessor) throws DatabaseException {
                Database database = super.createDatabase(c, resourceAccessor);
                if (database instanceof ExtendedPostgresDatabase myPostgresDatabase) {
                    myPostgresDatabase.setDataSource(dataSource);
                }
                return database;
            }
        };
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(this.properties.getChangeLog());
        liquibase.setClearCheckSums(this.properties.isClearChecksums());
        liquibase.setContexts(this.properties.getContexts());
        liquibase.setDefaultSchema(this.properties.getDefaultSchema());
        liquibase.setLiquibaseSchema(this.properties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(this.properties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogTable(this.properties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(this.properties.getDatabaseChangeLogLockTable());
        liquibase.setDropFirst(this.properties.isDropFirst());
        liquibase.setShouldRun(this.properties.isEnabled());
        liquibase.setLabels(this.properties.getLabels());
        liquibase.setChangeLogParameters(this.properties.getParameters());
        liquibase.setRollbackFile(this.properties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(this.properties.isTestRollbackOnUpdate());
        liquibase.setTag(this.properties.getTag());
        return liquibase;
    }
}
