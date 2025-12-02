package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class NorthwindTraders {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("What do you want to do?");
        System.out.println("1) Display all products");
        System.out.println("2) Display all customers");
        System.out.println("3) Display all categories and products in a category");
        System.out.println("0) Exit");
        System.out.print("Select an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0) {
            System.out.println("Goodbye.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/northwind?useSSL=false&serverTimezone=UTC",
                "root",
                "yearup24")) {

            switch (choice) {
                case 1:
                    displayAllProducts(connection);
                    break;
                case 2:
                    displayAllCustomers(connection);
                    break;
                case 3:
                    displayCategoriesAndProducts(connection, scanner);
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        } catch (SQLException e) {
            System.out.println("Database error:");
            e.printStackTrace();
        }
    }

    private static void displayAllProducts(Connection connection) throws SQLException {
        String sql = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products;";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Id  Name                 Price   Stock");
            System.out.println("---------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

                System.out.printf("%-4d%-20s%-8.2f%-6d%n", id, name, price, stock);
            }
        }
    }

    private static void displayAllCustomers(Connection connection) throws SQLException {
        String sql = "SELECT ContactName, CompanyName, City, Country, Phone " +
                "FROM customers ORDER BY Country, CompanyName;";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Contact Name         Company Name               City             Country        Phone");
            System.out.println("----------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                String contact = resultSet.getString("ContactName");
                String company = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");

                System.out.printf("%-20s%-25s%-17s%-15s%-20s%n",
                        contact, company, city, country, phone);
            }
        }
    }

    private static void displayCategoriesAndProducts(Connection connection, Scanner scanner) throws SQLException {

        String categorySql = "SELECT CategoryID, CategoryName FROM categories ORDER BY CategoryID;";

        try (PreparedStatement stmt = connection.prepareStatement(categorySql);
             ResultSet results = stmt.executeQuery()) {

            System.out.println("CategoryID   Name");
            System.out.println("-------------------------");

            while (results.next()) {
                int id = results.getInt("CategoryID");
                String name = results.getString("CategoryName");

                System.out.printf("%-12d%s%n", id, name);
            }
        }

        System.out.print("\nEnter a category id: ");
        int picked = scanner.nextInt();
        scanner.nextLine();

        String productSql =
                "SELECT ProductID, ProductName, UnitPrice, UnitsInStock " +
                        "FROM products WHERE CategoryID = ? ORDER BY ProductID;";

        try (PreparedStatement stmt = connection.prepareStatement(productSql)) {
            stmt.setInt(1, picked);

            try (ResultSet products = stmt.executeQuery()) {

                System.out.println("\nProducts in category " + picked + ":");
                System.out.println("Id  Name                 Price   Stock");
                System.out.println("---------------------------------------");

                while (products.next()) {
                    int id = products.getInt("ProductID");
                    String name = products.getString("ProductName");
                    double price = products.getDouble("UnitPrice");
                    int stock = products.getInt("UnitsInStock");

                    System.out.printf("%-4d%-20s%-8.2f%-6d%n", id, name, price, stock);
                }
            }
        }
    }
}