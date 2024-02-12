package com.asgarov.daodemoapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String CONNECTION_URL = PropertiesReader.getProperty("datasource.url");
    private static final String USERNAME = PropertiesReader.getProperty("datasource.username");
    private static final String PASSWORD = PropertiesReader.getProperty("datasource.password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
    }
}
