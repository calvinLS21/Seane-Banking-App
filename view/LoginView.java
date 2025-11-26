package view;

import controller.AccountController;
import controller.CustomerController;
import controller.AdminController;
import dao.AccountDAO;
import dao.BankTellerDAO;
import dao.CustomerDAO;
import dao.SystemAdminDAO;
import model.Customer;
import model.Account;
import model.bankTeller;
import model.systemAdmin;
import util.DBConnection;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView {
    public void show() {
        System.out.println("[DEBUG] LoginView.show() STARTED");

        DBConnection.initializeDatabase();

        JFrame frame = new JFrame("Seane Banking App - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(new Color(52, 152, 219));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(52, 152, 219));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // COMPONENTS
        JComboBox<String> userTypeCombo = new JComboBox<>(new String[]{"Customer", "Bank Teller", "System Admin"});
        userTypeCombo.setPreferredSize(new Dimension(200, 30));
        userTypeCombo.setBackground(Color.WHITE);

        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 30));

        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 30));

        JButton loginButton = new JButton("LOGIN");
        loginButton.setPreferredSize(new Dimension(100, 40));
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(new Color(52, 152, 219));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        // DAOs
        CustomerDAO customerDAO = new CustomerDAO();
        BankTellerDAO tellerDAO = new BankTellerDAO();
        SystemAdminDAO adminDAO = new SystemAdminDAO();
        CustomerController customerController = new CustomerController();
        AccountController accountController = new AccountController();
        AdminController adminController = new AdminController();

        // LAYOUT
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(userTypeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        //LOGIN
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[DEBUG] LOGIN BUTTON CLICKED");
                String userType = (String) userTypeCombo.getSelectedItem();
                String id = idField.getText().trim();
                String password = new String(passField.getPassword());

                try {
                    if ("Customer".equals(userType)) {
                    System.out.println("[DEBUG] Customer login attempt: " + id);
                    Customer customer = customerDAO.getCustomerByID(id);
                    if (customer != null && customer.getCustomerPassword().equals(password)) {
                        System.out.println("[DEBUG] Customer login SUCCESS");

                        // NEW: Load accounts from DB
                        AccountDAO accountDAO = new AccountDAO();
                        List<Account> accounts = accountDAO.getAccountsByCustomer(id);
                        for (Account acc : accounts) {
                            customer.addAccount(acc);
                        }

                        new CustomerView(customer, customerController, accountController).show();
                        frame.dispose();
                    } else {
                        System.out.println("[DEBUG] Customer login FAILED");
                        JOptionPane.showMessageDialog(frame, "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                    else if ("Bank Teller".equals(userType)) {
                        System.out.println("[DEBUG] Bank Teller login attempt: " + id);
                        int tellerID = Integer.parseInt(id);
                        bankTeller teller = tellerDAO.getBankTellerByID(tellerID);
                        if (teller != null && teller.getBankTellerPassword().equals(password)) {
                            System.out.println("[DEBUG] Bank Teller login SUCCESS");
                            new BankTellerView(teller, customerController).show();
                            frame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else if ("System Admin".equals(userType)) {
                        System.out.println("[DEBUG] System Admin login attempt: " + id);
                        int adminID = Integer.parseInt(id);
                        systemAdmin admin = adminDAO.getSystemAdminByID(adminID);
                        if (admin != null && admin.getAdminPassword().equals(password)) {
                            System.out.println("[DEBUG] System Admin login SUCCESS");
                            new AdminView(admin, adminController).show();
                            frame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid ID format", "Login Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    System.out.println("[ERROR] Login failed: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        System.out.println("[DEBUG] LOGIN SCREEN LOADED");
    }
}
