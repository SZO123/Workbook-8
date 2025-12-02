package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class NorthwindTraders {
    public static void main(String[] args) {

        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind?useSSL=false&serverTimezone=UTC",
                    "root",
                    "yearup24"
            );

            System.out.println("What do you want to do?");
            System.out.println("1) Display all products");
            System.out.println("2) Display all customers");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayAllProducts(connection);
                    break;
                case 2:
                    displayAllCustomers(connection);
                    break;
                case 0:
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }

        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection:");
                    e.printStackTrace();
                }
            }
        }
    }

    private static void displayAllProducts(Connection connection) throws SQLException {
        String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products;";

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            System.out.println("Id  Name                 Price   Stock");
            System.out.println("---------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

                System.out.printf("%-4d%-20s%-8.2f%-6d%n", id, name, price, stock);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    private static void displayAllCustomers(Connection connection) throws SQLException {
        String query =
                "SELECT ContactName, CompanyName, City, Country, Phone " +
                        "FROM customers " +
                        "ORDER BY Country, CompanyName;";

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            System.out.println("Contact Name         Company Name               City             Country        Phone");
            System.out.println("----------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                String contactName = resultSet.getString("ContactName");
                String companyName = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");

                System.out.printf("%-20s%-25s%-17s%-15s%-20s%n",
                        contactName, companyName, city, country, phone);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }
}