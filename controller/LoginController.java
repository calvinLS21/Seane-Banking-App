package controller;

import dao.CustomerDAO;
import model.Customer;
import view.CustomerView;

import javax.swing.JOptionPane;

public class LoginController {
    private CustomerDAO customerDAO = new CustomerDAO();
    private CustomerController customerController = new CustomerController();
    private AccountController accountController = new AccountController();

    public void loginCustomer(String customerID, String password) {
        Customer customer = customerDAO.getCustomerByID(customerID);
        if (customer != null && customer.getCustomerPassword().equals(password)) {
            CustomerView customerView = new CustomerView(customer, customerController, accountController);
            customerView.show();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
