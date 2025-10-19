package view;

import controller.AccountController;
import controller.AdminController;
import controller.CustomerController;
import controller.LoginController;
import controller.SignUpController;
import dao.BankTellerDAO;
import dao.SystemAdminDAO;
import model.Customer;
import model.bankTeller;
import model.systemAdmin;
import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        DBConnection.initializeDatabase();

        JFrame frame = new JFrame("Banking System Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridLayout(4, 2, 10, 10));

        JComboBox<String> userTypeCombo = new JComboBox<>(new String[]{"Customer", "Bank Teller", "System Admin"});
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        LoginController loginController = new LoginController();
        BankTellerDAO tellerDAO = new BankTellerDAO();
        SystemAdminDAO adminDAO = new SystemAdminDAO();
        CustomerController customerController = new CustomerController();
        AccountController accountController = new AccountController();
        SignUpController signUpController = new SignUpController();
        AdminController adminController = new AdminController();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userType = (String) userTypeCombo.getSelectedItem();
                String id = idField.getText().trim();
                String password = new String(passField.getPassword());
                try {
                    if ("Customer".equals(userType)) {
                        loginController.loginCustomer(id, password);
                    } else if ("Bank Teller".equals(userType)) {
                        int tellerID = Integer.parseInt(id);
                        bankTeller teller = tellerDAO.getBankTellerByID(tellerID);
                        if (teller != null && teller.getBankTellerPassword().equals(password)) {
                            BankTellerView view = new BankTellerView(teller, customerController);
                            view.show();
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if ("System Admin".equals(userType)) {
                        int adminID = Integer.parseInt(id);
                        systemAdmin admin = adminDAO.getSystemAdminByID(adminID);
                        if (admin != null && admin.getAdminPassword().equals(password)) {
                            AdminView view = new AdminView(admin, adminController);
                            view.show();
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid ID format", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUpView signUpView = new SignUpView();
                signUpView.show();
            }
        });

        frame.add(new JLabel("User Type:"));
        frame.add(userTypeCombo);
        frame.add(idLabel);
        frame.add(idField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(loginButton);
        frame.add(signUpButton);

        frame.setVisible(true);
    }
}