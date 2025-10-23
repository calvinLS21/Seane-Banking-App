package controller;

import dao.AccountDAO;
import model.Account;
import model.Customer;
import model.savingsAccount;
import model.investmentAccount;
import model.chequeAccount;

public class AccountController {
    private AccountDAO accountDAO = new AccountDAO();

    public String deposit(int accountID, double amount) {
        if (amount <= 0) {
            return "Error: Invalid deposit amount";
        }
        Account account = accountDAO.getAccountByID(accountID);
        if (account == null) {
            return "Error: Account not found";
        }
        String result = account.deposit(amount);
        if (result.toLowerCase().startsWith("deposited")) {
            accountDAO.updateAccountBalance(accountID, account.getAccountBalance());
        }
        return result;
    }

    public String withdraw(int accountID, double amount) {
        if (amount <= 0) {
            return "Error: Invalid withdrawal amount";
        }
        Account account = accountDAO.getAccountByID(accountID);
        if (account == null) {
            return "Error: Account not found";
        }
        if (account instanceof savingsAccount) {
            System.out.println("Withdrawal blocked for savings account ID " + accountID);
            return "Error: Withdrawals not allowed for Savings Account";
        }
        String result = account.withdraw(amount);
        if (result.toLowerCase().startsWith("withdrew")) {
            accountDAO.updateAccountBalance(accountID, account.getAccountBalance());
            return "Success: " + result;
        }
        return "Error: " + result;
    }

    public String transfer(int fromAccountID, int toAccountID, double amount) {
        if (amount <= 0) {
            return "Error: Invalid transfer amount";
        }
        Account fromAccount = accountDAO.getAccountByID(fromAccountID);
        Account toAccount = accountDAO.getAccountByID(toAccountID);
        if (fromAccount == null || toAccount == null) {
            return "Error: One or both accounts not found";
        }
        try {
            if (fromAccount.transferCurrency(amount, toAccount)) {
                accountDAO.updateAccountBalance(fromAccountID, fromAccount.getAccountBalance());
                accountDAO.updateAccountBalance(toAccountID, toAccount.getAccountBalance());
                return "Success: Transferred " + amount + " successfully";
            }
            return "Error: Transfer failed";
        } catch (Exception e) {
            return "Error: Transfer failed: " + e.getMessage();
        }
    }

    public String closeAccount(int accountID) {
        Account account = accountDAO.getAccountByID(accountID);
        if (account == null) {
            return "Error: Account not found";
        }
        String result = account.closeAccount();
        if (result.equals("Account closed.")) {
            accountDAO.closeAccount(accountID);
            return "Success: " + result;
        }
        return "Error: " + result;
    }

    public String openAccount(Customer customer, String accountType, double initialBalance, String accountName, String... extraParams) {
        if (customer == null || accountType == null || accountName == null || accountName.isEmpty()) {
            return "Error: Invalid account details";
        }
        if (initialBalance < 0) {
            return "Error: Initial balance cannot be negative";
        }
        if (accountType.toLowerCase().equals("cheque")) {
            if (extraParams.length < 2 || extraParams[0] == null || extraParams[0].isEmpty() || extraParams[1] == null || extraParams[1].isEmpty()) {
                return "Error: Valid company name and address required to open Cheque account for salary";
            }
        }
        try {
            Account account;
            int accountID = accountDAO.generateAccountID();
            switch (accountType.toLowerCase()) {
                case "savings":
                    account = new savingsAccount(accountID, initialBalance, accountName);
                    break;
                case "investment":
                    account = new investmentAccount(accountID, initialBalance, accountName);
                    break;
                case "cheque":
                    account = new chequeAccount(accountID, initialBalance, accountName, extraParams[0], extraParams[1]);
                    break;
                default:
                    return "Error: Invalid account type";
            }
            customer.addAccount(account);
            accountDAO.addAccount(account, customer.getCustomerID());
            return "Success: Account " + accountName + " opened with ID " + accountID;
        } catch (RuntimeException e) {
            return "Error: " + e.getMessage();
        }
    }
}
