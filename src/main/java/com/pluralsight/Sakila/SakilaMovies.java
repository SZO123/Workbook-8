package com.pluralsight.Sakila;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Scanner;

public class SakilaMovies {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Provide MySQL username and password as arguments.");
            return;
        }

        String dbUser = args[0];
        String dbPass = args[1];

        DataSource dataSource = createDataSource(dbUser, dbPass);
        DataManager dataManager = new DataManager(dataSource);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Search for actor name: ");
        String search = scanner.nextLine().trim();

        List<Actor> actors = dataManager.searchActorsByName(search);

        if (actors.isEmpty()) {
            System.out.println("No actors found.");
            return;
        }

        System.out.println();
        System.out.println("Actors found:");
        for (Actor actor : actors) {
            System.out.printf("%-4d%s %s%n",
                    actor.getActorId(),
                    actor.getFirstName(),
                    actor.getLastName());
        }

        System.out.println();
        System.out.print("Enter actor id to view films: ");
        int actorId = scanner.nextInt();
        scanner.nextLine();

        List<Film> films = dataManager.getFilmsByActorId(actorId);

        System.out.println();
        if (films.isEmpty()) {
            System.out.println("No films found for that actor.");
            return;
        }

        System.out.println("Films for actor id " + actorId + ":");
        for (Film film : films) {
            System.out.printf("%-4d%s (%d) %d min%n",
                    film.getFilmId(),
                    film.getTitle(),
                    film.getReleaseYear(),
                    film.getLength());
        }
    }

    private static DataSource createDataSource(String username, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/sakila?useSSL=false&serverTimezone=UTC");
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }
}