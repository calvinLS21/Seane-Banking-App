package model;

import java.util.ArrayList;
import java.util.List;

public class systemAdmin extends Person {
    private int adminID;
    private String adminPassword;
    private static List<bankTeller> bankTellers = new ArrayList<>();

    public systemAdmin(int nationalID, String firstName, String lastName, String dateOfBirth, String address, String emailAddress, int contact, int adminID, String adminPassword) {
        super(nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact);
        this.adminID = adminID;
        this.adminPassword = adminPassword;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String resetCustomerPassword(Customer customer, String newPassword) {
        if (newPassword.length() >= 8) {
            customer.setCustomerPassword(newPassword);
            return "Password reset successful";
        }
        return "Password too short";
    }

    public String resetBankTellerPassword(bankTeller teller, String newPassword) {
        if (newPassword.length() >= 8) {
            teller.setBankTellerPassword(newPassword);
            return "Password reset successful";
        }
        return "Password too short";
    }

    public void addBankTeller(bankTeller teller) {
        bankTellers.add(teller);
    }

    public void removeBankTeller(int tellerID) {
        bankTellers.removeIf(t -> t.getBankTellerID() == tellerID);
    }

    public String enforcePolicies() {
        return "Policies enforced.";
    }

    public void viewUsers() {
        for (bankTeller teller : bankTellers) {
            System.out.println(teller.toString());
        }
    }

    public String updateBankingSystem() {
        return "System updated.";
    }

    public void viewAdminProfile() {
        System.out.println(toString());
    }
}