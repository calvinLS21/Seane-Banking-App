package controller;

import dao.BankTellerDAO;
import dao.CustomerDAO;
import model.bankTeller;
import model.Customer;

public class AdminController {
    private BankTellerDAO tellerDAO = new BankTellerDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public void addBankTeller(bankTeller teller) {
        tellerDAO.addBankTeller(teller);
    }

    public void removeBankTeller(int tellerID) {
        tellerDAO.deleteBankTeller(tellerID);
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