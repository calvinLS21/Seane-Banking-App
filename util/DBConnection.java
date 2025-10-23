package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String USER = "admin";
    private static final String PASS = "Branch of sin";

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
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS Transactions (" +
                            "transactionID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "accountID INT, " +
                            "transactionType VARCHAR(255), " +
                            "amount DOUBLE, " +
                            "transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "toAccountID INT, " +
                            "FOREIGN KEY (accountID) REFERENCES Account(accountID)" +
                            ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
