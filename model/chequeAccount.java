package model;

import dao.AccountDAO;

public class chequeAccount extends Account {
    private String companyName;
    private String companyAddress;

    public chequeAccount(int accountID, double accountBalance, String accountName, String companyName, String companyAddress) {
        super(accountID, accountBalance, accountName);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @Override
    public String withdraw(double amount) {
        if (amount > 0 && amount <= getAccountBalance()) {
            setAccountBalance(getAccountBalance() - amount);
            return "Withdrew " + amount + ". New balance: " + getAccountBalance();
        }
        return "Invalid withdrawal";
    }

    public String receiveSalary(double amount) {
        if (amount <= 0) {
            return "Invalid salary amount";
        }
        return deposit(amount);
    }

    @Override
    public double calculateInterest() {
        // Cheque accounts do not earn interest
        return 0;
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
