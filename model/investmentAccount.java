package model;

import dao.AccountDAO;

public class investmentAccount extends Account {

    public investmentAccount(int accountID, double initialBalance, String accountName) {
        super(accountID, initialBalance, accountName);
        if (initialBalance < 500) {
            throw new IllegalArgumentException("Initial deposit must be at least BWP500.00");
        }
    }

    @Override
    public String withdraw(double amount) {
        if (amount > 0 && amount <= getAccountBalance()) {
            setAccountBalance(getAccountBalance() - amount);
            return "Withdrew " + amount + ". New balance: " + getAccountBalance();
        }
        return "Invalid withdrawal";
    }

    @Override
    public double calculateInterest() {
        double interest = getAccountBalance() * 0.05;
        deposit(interest);
        return interest;
    }

    @Override
    public String closeAccount() {
        if (getAccountBalance() != 0) {
            return "Error: Cannot close account with non-zero balance";
        }
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.closeAccount(getAccountID());
        return "Account closed.";
    }
}
