package dao;

import model.Account;
import model.chequeAccount;
import model.savingsAccount;
import model.investmentAccount;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public void addAccount(Account account, String customerID) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            PreparedStatement psAccount = conn.prepareStatement(
                    "INSERT INTO Account (accountID, accountBalance, accountName, accountType, customerID) VALUES (?, ?, ?, ?, ?)"
            );
            psAccount.setInt(1, account.getAccountID());
            psAccount.setDouble(2, account.getAccountBalance());
            psAccount.setString(3, account.getAccountName());
            psAccount.setString(4, account.getClass().getSimpleName());
            psAccount.setString(5, customerID);
            psAccount.executeUpdate();

            if (account instanceof chequeAccount) {
                chequeAccount ca = (chequeAccount) account;
                PreparedStatement psCheque = conn.prepareStatement(
                        "INSERT INTO ChequeAccount (chequeAccountID, companyName, companyAddress) VALUES (?, ?, ?)"
                );
                psCheque.setInt(1, ca.getAccountID());
                psCheque.setString(2, ca.getCompanyName());
                psCheque.setString(3, ca.getCompanyAddress());
                psCheque.executeUpdate();
            }
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to add account: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    public void closeAccount(int accountID) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            // Delete from ChequeAccount if applicable
            PreparedStatement psCheque = conn.prepareStatement(
                    "DELETE FROM ChequeAccount WHERE chequeAccountID = ?"
            );
            psCheque.setInt(1, accountID);
            psCheque.executeUpdate();

            // Delete from Account
            PreparedStatement psAccount = conn.prepareStatement(
                    "DELETE FROM Account WHERE accountID = ?"
            );
            psAccount.setInt(1, accountID);
            int rowsAffected = psAccount.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No account found with ID: " + accountID);
            }
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to close account: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    public int generateAccountID() {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT MAX(accountID) + 1 AS newID FROM Account"
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int newID = rs.getInt("newID");
                return newID > 0 ? newID : 1; // Start at 1 if no accounts exist
            }
            return 1; // Default if table is empty
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate account ID: " + e.getMessage());
        }
    }

    public List<Account> getAccountsByCustomerID(String customerID) {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Account WHERE customerID = ?"
            );
            ps.setString(1, customerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("accountType");
                Account account = null;
                if ("chequeAccount".equals(type)) {
                    PreparedStatement psCheque = conn.prepareStatement(
                            "SELECT * FROM ChequeAccount WHERE chequeAccountID = ?"
                    );
                    psCheque.setInt(1, rs.getInt("accountID"));
                    ResultSet rsCheque = psCheque.executeQuery();
                    if (rsCheque.next()) {
                        account = new chequeAccount(
                                rs.getInt("accountID"),
                                rs.getDouble("accountBalance"),
                                rs.getString("accountName"),
                                rsCheque.getString("companyName"),
                                rsCheque.getString("companyAddress")
                        );
                    }
                } else if ("savingsAccount".equals(type)) {
                    account = new savingsAccount(
                            rs.getInt("accountID"),
                            rs.getDouble("accountBalance"),
                            rs.getString("accountName")
                    );
                } else if ("investmentAccount".equals(type)) {
                    account = new investmentAccount(
                            rs.getInt("accountID"),
                            rs.getDouble("accountBalance"),
                            rs.getString("accountName")
                    );
                }
                if (account != null) {
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve accounts: " + e.getMessage());
        }
        return accounts;
    }

    public void updateAccountBalance(int accountID, double newBalance) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Account SET accountBalance = ? WHERE accountID = ?"
            );
            ps.setDouble(1, newBalance);
            ps.setInt(2, accountID);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No account found with ID: " + accountID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update account balance: " + e.getMessage());
        }
    }

    public Account getAccountByID(int accountID) {
        Account account = null;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Account WHERE accountID = ?"
            );
            ps.setInt(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("accountType");
                if ("chequeAccount".equals(type)) {
                    PreparedStatement psCheque = conn.prepareStatement(
                            "SELECT * FROM ChequeAccount WHERE chequeAccountID = ?"
                    );
                    psCheque.setInt(1, accountID);
                    ResultSet rsCheque = psCheque.executeQuery();
                    if (rsCheque.next()) {
                        account = new chequeAccount(
                                rs.getInt("accountID"),
                                rs.getDouble("accountBalance"),
                                rs.getString("accountName"),
                                rsCheque.getString("companyName"),
                                rsCheque.getString("companyAddress")
                        );
                    }
                } else if ("savingsAccount".equals(type)) {
                    account = new savingsAccount(
                            rs.getInt("accountID"),
                            rs.getDouble("accountBalance"),
                            rs.getString("accountName")
                    );
                } else if ("investmentAccount".equals(type)) {
                    account = new investmentAccount(
                            rs.getInt("accountID"),
                            rs.getDouble("accountBalance"),
                            rs.getString("accountName")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve account: " + e.getMessage());
        }
        return account;
    }

    public List<String[]> getTransactionHistory(int accountID) {
        List<String[]> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Transactions WHERE accountID = ? ORDER BY transactionDate DESC"
            );
            ps.setInt(1, accountID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] transaction = new String[]{
                        String.valueOf(rs.getInt("transactionID")),
                        rs.getString("transactionType"),
                        String.valueOf(rs.getDouble("amount")),
                        rs.getString("transactionDate"),
                        rs.getInt("toAccountID") == 0 ? "" : String.valueOf(rs.getInt("toAccountID"))
                };
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve transaction history: " + e.getMessage());
        }
        return transactions;
    }
}