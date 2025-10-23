package model;

import dao.AccountDAO;
import java.util.List;

public class bankTeller extends Person {
    private int bankTellerID;
    private String bankTellerPassword;

    public bankTeller(int nationalID, String firstName, String lastName, String dateOfBirth, String address,
                      String emailAddress, int contact, int bankTellerID, String bankTellerPassword) {
        super(nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact);
        this.bankTellerID = bankTellerID;
        this.bankTellerPassword = bankTellerPassword;
    }

    public int getBankTellerID() {
        return bankTellerID;
    }

    public void setBankTellerID(int bankTellerID) {
        this.bankTellerID = bankTellerID;
    }

    public String getBankTellerPassword() {
        return bankTellerPassword;
    }

    public void setBankTellerPassword(String bankTellerPassword) {
        this.bankTellerPassword = bankTellerPassword;
    }

    public Customer createCustomerProfile(int nationalID, String firstName, String lastName, String dateOfBirth,
                                          String address, String emailAddress, int contact, String customerID,
                                          String password) {
        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() ||
                emailAddress == null || emailAddress.isEmpty() || customerID == null || customerID.isEmpty() ||
                password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Invalid customer details");
        }
        return new Customer(nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact,
                customerID, password);
    }

    public Account openCustomerAccount(Customer customer, String accountType, double initialBalance,
                                       String accountName, String... extraParams) {
        if (customer == null || accountType == null || accountName == null || accountName.isEmpty()) {
            throw new IllegalArgumentException("Invalid account details");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (accountType.toLowerCase().equals("cheque")) {
            if (extraParams.length < 2 || extraParams[0] == null || extraParams[0].isEmpty() ||
                    extraParams[1] == null || extraParams[1].isEmpty()) {
                throw new IllegalArgumentException("Customer must be employed and provide valid company name and address to open a Cheque account");
            }
        }
        try {
            Account account;
            AccountDAO accountDAO = new AccountDAO();
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
                    throw new IllegalArgumentException("Invalid account type: " + accountType);
            }
            customer.addAccount(account);
            accountDAO.addAccount(account, customer.getCustomerID());
            return account;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Failed to open account: " + e.getMessage());
        }
    }

    public void viewPersonalInformation() {
        System.out.println(toString());
    }

    public boolean updatePersonalInformation(String newFirstName, String newLastName, String newDateOfBirth,
                                             String newAddress, String newEmail, int newContact) {
        if (newFirstName == null || newFirstName.isEmpty() || newLastName == null || newLastName.isEmpty() ||
                newEmail == null || newEmail.isEmpty()) {
            return false;
        }
        setFirstName(newFirstName);
        setLastName(newLastName);
        setDateOfBirth(newDateOfBirth);
        setAddress(newAddress);
        setEmailAddress(newEmail);
        setContact(newContact);
        return true;
    }

    public boolean deletePersonalInformation() {
        return true;
    }

    public String closeCustomerAccount(Customer customer, int accountID) {
        if (customer == null) {
            return "Error: Invalid customer";
        }
        List<Account> accounts = customer.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getAccountID() == accountID) {
                String result = acc.closeAccount();
                if (result.equals("Account closed.")) {
                    accounts.remove(i);
                }
                return result;
            }
        }
        return "Error: Account not found";
    }

    @Override
    public String toString() {
        return "BankTeller{" +
                "nationalID=" + getNationalID() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", dateOfBirth='" + getDateOfBirth() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", emailAddress='" + getEmailAddress() + '\'' +
                ", contact=" + getContact() +
                ", bankTellerID=" + bankTellerID +
                '}';
    }
}
