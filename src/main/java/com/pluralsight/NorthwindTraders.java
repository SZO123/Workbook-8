package com.pluralsight;

import java.sql.*;

public class NorthwindTraders {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind?useSSL=false&serverTimezone=UTC",
                    "root",
                    "yearup24"
            );

            Statement statement = connection.createStatement();

            String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products;";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Id  Name                 Price   Stock");
            System.out.println("---------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

                System.out.printf("%-4d%-20s%-8.2f%-6d%n", id, name, price, stock);
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}