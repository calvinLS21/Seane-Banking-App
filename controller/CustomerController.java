package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import model.Account;
import model.Customer;

public class CustomerController {
    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();

    public void createCustomer(Customer customer) {
        customerDAO.addCustomer(customer);
    }

    public void addAccountToCustomer(String customerID, Account account) {
        accountDAO.addAccount(account, customerID);
    }

    public void updatePersonalInformation(String customerID, String newFirstName, String newLastName, String newDateOfBirth, String newAddress, String newEmail, int newContact) {
        Customer customer = customerDAO.getCustomerByID(customerID);
        if (customer != null) {
            customer.updatePersonalInformation(newFirstName, newLastName, newDateOfBirth, newAddress, newEmail, newContact);
            customerDAO.updateCustomer(customer);
        }
    }

    public boolean deleteCustomer(String customerID) {
        return customerDAO.deleteCustomer(customerID);
    }

    public Customer getCustomer(String customerID) {
        return customerDAO.getCustomerByID(customerID);
    }
}
