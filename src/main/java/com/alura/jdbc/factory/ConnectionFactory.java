package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

    private DataSource datasource;

    public ConnectionFactory() {
        var pooledDataSource = new ComboPooledDataSource();
        pooledDataSource
                .setJdbcUrl("jdbc:postgresql://localhost:5432/control_stock?useTimezone=true&serverTimezone=UTC");
        pooledDataSource.setUser("postgres");
        pooledDataSource.setPassword("postgres");
        pooledDataSource.setMaxPoolSize(20);

        this.datasource = pooledDataSource;
    }

    public Connection getConnection() {
        try {
            return this.datasource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
