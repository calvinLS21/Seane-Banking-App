package model;

import dao.AccountDAO;

import java.util.List;

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

    public List<String[]> transactionHistory() {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.getTransactionHistory(accountID);
    }
}
