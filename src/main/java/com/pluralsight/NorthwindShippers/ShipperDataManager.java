package com.pluralsight.NorthwindShippers;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipperDataManager {

    private final DataSource dataSource;

    public ShipperDataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertShipper(String name, String phone) {
        String sql = "INSERT INTO shippers (CompanyName, Phone) VALUES (?, ?)";
        int newId = -1;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, name);
            stmt.setString(2, phone);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        newId = keys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting shipper.");
        }

        return newId;
    }

    public List<Shipper> getAllShippers() {
        List<Shipper> shippers = new ArrayList<>();

        String sql = "SELECT ShipperID, CompanyName, Phone FROM shippers ORDER BY ShipperID";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("ShipperID");
                String name = rs.getString("CompanyName");
                String phone = rs.getString("Phone");

                shippers.add(new Shipper(id, name, phone));
            }
        } catch (SQLException e) {
            System.out.println("Error loading shippers.");
        }

        return shippers;
    }

    public int updateShipperPhone(int id, String newPhone) {
        String sql = "UPDATE shippers SET Phone = ? WHERE ShipperID = ?";
        int rows = 0;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setString(1, newPhone);
            stmt.setInt(2, id);

            rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating shipper phone.");
        }

        return rows;
    }

    public int deleteShipper(int id) {
        String sql = "DELETE FROM shippers WHERE ShipperID = ?";
        int rows = 0;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);

            rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting shipper.");
        }

        return rows;
    }
}