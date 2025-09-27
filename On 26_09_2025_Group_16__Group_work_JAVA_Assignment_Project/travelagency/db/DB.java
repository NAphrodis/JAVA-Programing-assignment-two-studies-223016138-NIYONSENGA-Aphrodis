package com.travelagency.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/travelagencydb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "traveluser";
    private static final String PASSWORD = "secret123";

    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            
            throw new SQLException("MySQL JDBC driver not found.", ex);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
