package controller;

import dao.CustomerDAO;
import model.Customer;
import model.Person;

import javax.swing.JOptionPane;

public class SignUpController {
    private CustomerDAO customerDAO = new CustomerDAO();

    public void signUpCustomer(int nationalID, String firstName, String lastName, String dateOfBirth, String address, String emailAddress, int contact, String customerID, String customerPassword) {
        try {
            Person.signUp(nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact); // Validation only, result not used
            Customer customer = new Customer(nationalID, firstName, lastName, dateOfBirth, address, emailAddress, contact, customerID, customerPassword);
            customerDAO.addCustomer(customer);
            JOptionPane.showMessageDialog(null, "Sign up successful", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Sign Up Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}