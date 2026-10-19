package com.example.library.dao.implementations;

import java.sql.Connection;
import java.sql.SQLException;

import com.example.library.dao.interfaces.ConnectionManager;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.example.library.config.DAOConfigurationProperties;
import org.springframework.stereotype.Repository;

public class ConnectionManagerImpl implements ConnectionManager {

    final DAOConfigurationProperties config;
    private MysqlConnectionPoolDataSource dataSource;

    public ConnectionManagerImpl(DAOConfigurationProperties config) {
        super();
        this.config = config;
        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUrl(config.getUrl());
        dataSource.setUser(config.getUser());
        dataSource.setPassword(config.getPassword());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(true);
    }

    @Override
    public Connection getConnection(boolean autoCommit) throws SQLException {
        Connection con = dataSource.getConnection();
        con.setAutoCommit(autoCommit);
        return con;
    }

    @Override
    public Connection getConnection(boolean autoCommit, int transactionIsolation) throws SQLException {
        Connection con = getConnection(autoCommit);
        con.setTransactionIsolation(transactionIsolation);
        return con;
    }

}
