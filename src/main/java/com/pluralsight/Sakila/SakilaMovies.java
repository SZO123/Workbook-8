package com.pluralsight.Sakila;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class SakilaMovies {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Provide MySQL username and password as arguments.");
            System.out.println("Example: java com.pluralsight.SakilaMovies root yearup24");
            return;
        }

        String dbUser = args[0];
        String dbPass = args[1];

        DataSource dataSource = createDataSource(dbUser, dbPass);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter actor last name: ");
        String lastNameSearch = scanner.nextLine().trim();

        listActorsByLastName(dataSource, lastNameSearch);

        System.out.println();
        System.out.println("Now select an actor to view films.");

        System.out.print("Enter actor first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter actor last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.println();
        listFilmsForActor(dataSource, firstName, lastName);
    }

    private static DataSource createDataSource(String username, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/sakila?useSSL=false&serverTimezone=UTC");
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    private static void listActorsByLastName(DataSource dataSource, String lastName) {
        String sql =
                "SELECT actor_id, first_name, last_name " +
                        "FROM actor " +
                        "WHERE last_name = ? " +
                        "ORDER BY first_name, last_name";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, lastName);

            try (ResultSet rs = stmt.executeQuery()) {

                boolean any = false;

                System.out.println();
                System.out.println("Actors with last name '" + lastName + "':");

                while (rs.next()) {
                    any = true;
                    int id = rs.getInt("actor_id");
                    String first = rs.getString("first_name");
                    String last = rs.getString("last_name");

                    System.out.printf("%-4d%s %s%n", id, first, last);
                }

                if (!any) {
                    System.out.println("No actors found with that last name.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading actors.");
            e.printStackTrace();
        }
    }

    private static void listFilmsForActor(DataSource dataSource, String firstName, String lastName) {
        String sql =
                "SELECT f.title " +
                        "FROM actor a " +
                        "JOIN film_actor fa ON a.actor_id = fa.actor_id " +
                        "JOIN film f ON fa.film_id = f.film_id " +
                        "WHERE a.first_name = ? AND a.last_name = ? " +
                        "ORDER BY f.title";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            try (ResultSet rs = stmt.executeQuery()) {

                boolean any = false;

                System.out.println("Films for " + firstName + " " + lastName + ":");

                while (rs.next()) {
                    any = true;
                    String title = rs.getString("title");
                    System.out.println(title);
                }

                if (!any) {
                    System.out.println("No films found for that actor.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading films.");
            e.printStackTrace();
        }
    }
}