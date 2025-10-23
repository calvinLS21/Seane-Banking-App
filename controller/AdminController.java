package controller;

import dao.BankTellerDAO;
import dao.CustomerDAO;
import dao.SystemAdminDAO;
import model.bankTeller;
import model.Customer;
import model.systemAdmin;

import java.util.List;

public class AdminController {
    private BankTellerDAO tellerDAO = new BankTellerDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private SystemAdminDAO adminDAO = new SystemAdminDAO();

    public void addBankTeller(bankTeller teller) {
        tellerDAO.addBankTeller(teller);
    }

    public void removeBankTeller(int tellerID) {
        tellerDAO.deleteBankTeller(tellerID);
    }

    public void updateAdminProfile(systemAdmin admin) {
        adminDAO.updateSystemAdmin(admin);
    }

    public void deleteAdmin(int adminID) {
        adminDAO.deleteSystemAdmin(adminID);
    }

    public String enforcePolicies() {
        return "Policies enforced.";
    }

    public String updateSystem() {
        return "System updated.";
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public List<bankTeller> getAllBankTellers() {
        return tellerDAO.getAllBankTellers();
    }

    public void resetCustomerPassword(String customerID, String newPassword) {
        Customer customer = customerDAO.getCustomerByID(customerID);
        if (customer != null) {
            customer.setCustomerPassword(newPassword);
            customerDAO.updateCustomer(customer);
        }
    }

    public void resetTellerPassword(int tellerID, String newPassword) {
        bankTeller teller = tellerDAO.getBankTellerByID(tellerID);
        if (teller != null) {
            teller.setBankTellerPassword(newPassword);
            tellerDAO.updateBankTeller(teller);
        }
    }
}

