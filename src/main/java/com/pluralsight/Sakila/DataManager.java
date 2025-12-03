package com.pluralsight.Sakila;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final DataSource dataSource;

    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> searchActorsByName(String namePart) {
        List<Actor> actors = new ArrayList<>();

        String sql =
                "SELECT actor_id, first_name, last_name " +
                        "FROM actor " +
                        "WHERE first_name LIKE ? OR last_name LIKE ? " +
                        "ORDER BY last_name, first_name";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            String pattern = "%" + namePart + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("actor_id");
                    String first = rs.getString("first_name");
                    String last = rs.getString("last_name");

                    actors.add(new Actor(id, first, last));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching for actors.");
            e.printStackTrace();
        }

        return actors;
    }

    public List<Film> getFilmsByActorId(int actorId) {
        List<Film> films = new ArrayList<>();

        String sql =
                "SELECT f.film_id, f.title, f.description, f.release_year, f.length " +
                        "FROM film f " +
                        "JOIN film_actor fa ON f.film_id = fa.film_id " +
                        "WHERE fa.actor_id = ? " +
                        "ORDER BY f.title";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, actorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int filmId = rs.getInt("film_id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    int releaseYear = rs.getInt("release_year");
                    int length = rs.getInt("length");

                    films.add(new Film(filmId, title, description, releaseYear, length));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error loading films for actor.");
            e.printStackTrace();
        }

        return films;
    }
}