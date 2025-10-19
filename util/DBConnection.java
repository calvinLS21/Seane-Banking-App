package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS Person (" +
                            "nationalID INT PRIMARY KEY, " +
                            "firstName VARCHAR(255), " +
                            "lastName VARCHAR(255), " +
                            "dateOfBirth VARCHAR(255), " +
                            "address VARCHAR(255), " +
                            "emailAddress VARCHAR(255), " +
                            "contact INT" +
                            ")"
            );
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS Customer (" +
                            "customerID VARCHAR(255) PRIMARY KEY, " +
                            "customerPassword VARCHAR(255), " +
                            "nationalID INT, " +
                            "FOREIGN KEY (nationalID) REFERENCES Person(nationalID)" +
                            ")"
            );
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS BankTeller (" +
                            "bankTellerID INT PRIMARY KEY, " +
                            "bankTellerPassword VARCHAR(255), " +
                            "nationalID INT, " +
                            "FOREIGN KEY (nationalID) REFERENCES Person(nationalID)" +
                            ")"
            );
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS SystemAdmin (" +
                            "adminID INT PRIMARY KEY, " +
                            "adminPassword VARCHAR(255), " +
                            "nationalID INT, " +
                            "FOREIGN KEY (nationalID) REFERENCES Person(nationalID)" +
                            ")"
            );
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS Account (" +
                            "accountID INT PRIMARY KEY, " +
                            "accountBalance DOUBLE, " +
                            "accountName VARCHAR(255), " +
                            "accountType VARCHAR(255), " +
                            "customerID VARCHAR(255), " +
                            "FOREIGN KEY (customerID) REFERENCES Customer(customerID)" +
                            ")"
            );
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS ChequeAccount (" +
                            "chequeAccountID INT PRIMARY KEY, " +
                            "companyName VARCHAR(255), " +
                            "companyAddress VARCHAR(255), " +
                            "FOREIGN KEY (chequeAccountID) REFERENCES Account(accountID)" +
                            ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}