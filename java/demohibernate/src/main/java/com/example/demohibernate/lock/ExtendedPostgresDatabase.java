package com.example.demohibernate.lock;

import liquibase.database.core.PostgresDatabase;

import javax.sql.DataSource;

public class ExtendedPostgresDatabase extends PostgresDatabase {

    private DataSource dataSource;

    public String getLockFunctionArgs() {
        return "0,0";
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }
}
