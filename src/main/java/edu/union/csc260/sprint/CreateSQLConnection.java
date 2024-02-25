package edu.union.csc260.sprint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateSQLConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3333/group2_db";
        String username = "group2user";
        String password = "group2_user-Un1on1795";

        Connection connection;
        System.out.println("======================");
        System.out.println("Connecting database...");
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot close the database.", e);
        }
        System.out.println("Database connection closed!");
        System.out.println("===========================");
    }
}

