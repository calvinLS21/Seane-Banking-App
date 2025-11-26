package model;

import dao.AccountDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBConnection;
import javax.swing.JOptionPane;


public abstract class Account {
    private int accountID;
    private double accountBalance;
    private String accountName;

    public Account(int accountID, double accountBalance, String accountName) {
        if (accountBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountID = accountID;
        this.accountBalance = accountBalance;
        this.accountName = accountName;
    }

    public int getAccountID() {
        return accountID;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String deposit(double amount) {
        if (amount <= 0) {
            return "Invalid deposit amount";
        }
        accountBalance += amount;
        return "Deposited " + amount + ". New balance: " + accountBalance;
    }

    public abstract String withdraw(double amount);

    public abstract double calculateInterest();

    public abstract String closeAccount();

    public boolean transferCurrency(double amount, Account toAccount) {
        if (amount <= 0) {
            return false;
        }
        if (amount <= accountBalance && toAccount != null) {
            accountBalance -= amount;
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }

    public String[][] transactionHistory() {
        List<String[]> history = new ArrayList<>();
        String query = "SELECT transactionID, transactionType, amount, transactionDate, toAccountID " +
                "FROM Transactions WHERE accountID = ? ORDER BY transactionDate DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, this.accountID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] row = new String[5];
                row[0] = String.valueOf(rs.getInt("transactionID"));
                row[1] = rs.getString("transactionType");
                row[2] = String.format("BWP %.2f", rs.getDouble("amount"));
                row[3] = rs.getTimestamp("transactionDate").toString();
                int toAcc = rs.getInt("toAccountID");
                row[4] = (rs.wasNull()) ? "-" : String.valueOf(toAcc);
                history.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading transaction history");
        }

        // Convert to 2D array
        String[][] result = new String[history.size()][5];
        for (int i = 0; i < history.size(); i++) {
            result[i] = history.get(i);
        }
        return result;
    }

}
