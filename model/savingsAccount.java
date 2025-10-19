package model;

import dao.AccountDAO;

public class savingsAccount extends Account {

    public savingsAccount(int accountID, double accountBalance, String accountName) {
        super(accountID, accountBalance, accountName);
    }

    @Override
    public String withdraw(double amount) {
        return "Withdrawals not allowed for Savings Account";
    }

    @Override
    public double calculateInterest() {
        double interest = getAccountBalance() * 0.0005;
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