package com.pluralsight.NorthwindShippers;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Scanner;

public class NorthwindShippers {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Provide MySQL username and password as arguments.");
            return;
        }

        String dbUser = args[0];
        String dbPass = args[1];

        DataSource dataSource = createDataSource(dbUser, dbPass);
        ShipperDataManager dataManager = new ShipperDataManager(dataSource);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Step 1: Add a new shipper");
        System.out.print("Enter new shipper name: ");
        String newName = scanner.nextLine().trim();

        System.out.print("Enter new shipper phone: ");
        String newPhone = scanner.nextLine().trim();

        int newId = dataManager.insertShipper(newName, newPhone);
        System.out.println("New shipper id: " + newId);

        System.out.println();
        System.out.println("Step 2: List all shippers");
        displayAllShippers(dataManager);

        System.out.println();
        System.out.println("Step 3: Update shipper phone");
        System.out.print("Enter shipper id to update: ");
        int updateId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter new phone number: ");
        String updatePhone = scanner.nextLine().trim();

        dataManager.updateShipperPhone(updateId, updatePhone);

        System.out.println();
        System.out.println("Step 4: List all shippers");
        displayAllShippers(dataManager);

        System.out.println();
        System.out.println("Step 5: Delete a shipper (not 1, 2, or 3)");
        System.out.print("Enter shipper id to delete: ");
        int deleteId = Integer.parseInt(scanner.nextLine().trim());

        if (deleteId >= 1 && deleteId <= 3) {
            System.out.println("Do not delete shippers 1, 2, or 3.");
        } else {
            dataManager.deleteShipper(deleteId);
        }

        System.out.println();
        System.out.println("Step 6: List all shippers");
        displayAllShippers(dataManager);
    }

    private static DataSource createDataSource(String username, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/northwind?useSSL=false&serverTimezone=UTC");
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    private static void displayAllShippers(ShipperDataManager dataManager) {
        List<Shipper> shippers = dataManager.getAllShippers();

        System.out.println("Id   Name                    Phone");
        System.out.println("-------------------------------------------");

        for (Shipper shipper : shippers) {
            System.out.printf("%-4d%-24s%s%n",
                    shipper.getShipperId(),
                    shipper.getName(),
                    shipper.getPhone());
        }
    }
}