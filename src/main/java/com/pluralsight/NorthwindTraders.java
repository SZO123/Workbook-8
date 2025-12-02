package com.pluralsight;

import java.sql.*;

public class NorthwindTraders {
    public static void main(String[] args) {
        try {
            // Step 1: open the connection
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind", "root", "yearup24");

            // Step 2: create a statement
            Statement statement = connection.createStatement();

            // Step 3a: execute the query
            String query = "SELECT ProductName FROM products;";
            ResultSet resultSet = statement.executeQuery(query);

            // Step 3b: process the results
            while (resultSet.next()) {
                System.out.println(resultSet.getString("ProductName"));
            }

            // Step 4: close the connection
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}