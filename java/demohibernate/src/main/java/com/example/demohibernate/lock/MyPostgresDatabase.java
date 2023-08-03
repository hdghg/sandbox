package com.example.demohibernate.lock;

import liquibase.database.core.PostgresDatabase;

import javax.sql.DataSource;

public class MyPostgresDatabase extends PostgresDatabase {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public MyPostgresDatabase setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }
}
