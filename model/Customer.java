package model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private String customerID;
    private String customerPassword;
    private List<Account> accounts = new ArrayList<>();

    public Customer(int nationalID, String firstName, String lastName, String dateOfBirth, String address, String emailAddress, int contact, String customerID, String customerPassword) {
        super(nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact);
        this.customerID = customerID;
        this.customerPassword = customerPassword;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts); // Return copy to prevent direct modification
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Cannot add null account");
        }
        accounts.add(account);
    }

    public void viewPersonalInformation() {
        System.out.println(toString());
    }

    public boolean updatePersonalInformation(String newFirstName, String newLastName, String newDateOfBirth, String newAddress, String newEmail, int newContact) {
        if (newFirstName == null || newFirstName.isEmpty() || newLastName == null || newLastName.isEmpty() || newEmail == null || newEmail.isEmpty()) {
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
        return accounts.isEmpty(); // Only allow deletion if no accounts exist
    }
}
