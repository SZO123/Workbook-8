package com.pluralsight.Northwind;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class NorthwindTraders {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Provide MySQL username and password as arguments.");
            return;
        }

        String dbUser = args[0];
        String dbPass = args[1];

        DataSource dataSource = createDataSource(dbUser, dbPass);
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

        switch (choice) {
            case 1:
                displayAllProducts(dataSource);
                break;
            case 2:
                displayAllCustomers(dataSource);
                break;
            case 3:
                displayCategoriesAndProducts(dataSource, scanner);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static DataSource createDataSource(String username, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/northwind?useSSL=false&serverTimezone=UTC");
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    private static void displayAllProducts(DataSource dataSource) {
        String sql = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            System.out.println("Id  Name                 Price   Stock");
            System.out.println("---------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("ProductID");
                String name = rs.getString("ProductName");
                double price = rs.getDouble("UnitPrice");
                int stock = rs.getInt("UnitsInStock");

                System.out.printf("%-4d%-20s%-8.2f%-6d%n", id, name, price, stock);
            }
        } catch (SQLException e) {
            System.out.println("Error displaying products.");
        }
    }

    private static void displayAllCustomers(DataSource dataSource) {
        String sql = "SELECT ContactName, CompanyName, City, Country, Phone FROM customers ORDER BY Country, CompanyName";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            System.out.println("Contact Name         Company Name               City             Country        Phone");
            System.out.println("----------------------------------------------------------------------------------------");

            while (rs.next()) {
                String contact = rs.getString("ContactName");
                String company = rs.getString("CompanyName");
                String city = rs.getString("City");
                String country = rs.getString("Country");
                String phone = rs.getString("Phone");

                System.out.printf("%-20s%-25s%-17s%-15s%-20s%n",
                        contact, company, city, country, phone);
            }
        } catch (SQLException e) {
            System.out.println("Error displaying customers.");
        }
    }

    private static void displayCategoriesAndProducts(DataSource dataSource, Scanner scanner) {

        String categorySql = "SELECT CategoryID, CategoryName FROM categories ORDER BY CategoryID";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(categorySql);
                ResultSet rs = stmt.executeQuery()
        ) {
            System.out.println("CategoryID   Name");
            System.out.println("-------------------------");

            while (rs.next()) {
                int id = rs.getInt("CategoryID");
                String name = rs.getString("CategoryName");

                System.out.printf("%-12d%s%n", id, name);
            }

        } catch (SQLException e) {
            System.out.println("Error displaying categories.");
            return;
        }

        System.out.print("Enter category id: ");
        int picked = scanner.nextInt();
        scanner.nextLine();

        String productSql = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products WHERE CategoryID = ? ORDER BY ProductID";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(productSql)
        ) {
            stmt.setInt(1, picked);

            try (ResultSet rs = stmt.executeQuery()) {

                System.out.println("\nProducts in category " + picked + ":");
                System.out.println("Id  Name                 Price   Stock");
                System.out.println("---------------------------------------");

                while (rs.next()) {
                    int id = rs.getInt("ProductID");
                    String name = rs.getString("ProductName");
                    double price = rs.getDouble("UnitPrice");
                    int stock = rs.getInt("UnitsInStock");

                    System.out.printf("%-4d%-20s%-8.2f%-6d%n", id, name, price, stock);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error displaying products.");
        }
    }
}