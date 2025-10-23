package view;

import controller.CustomerController;
import model.Customer;
import model.bankTeller;
import dao.CustomerDAO;
import controller.AccountController;

import javax.swing.*;
import java.awt.*;

public class BankTellerView {
    private bankTeller teller;
    private CustomerController customerController;

    public BankTellerView(bankTeller teller, CustomerController customerController) {
        this.teller = teller;
        this.customerController = customerController;
    }

    public void show() {
        JFrame frame = new JFrame("Seane Banking App - Bank Teller Dashboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(new Color(245, 248, 255));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(52, 152, 219));  //BLUE TABS
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("Profile", createProfilePanel());
        tabbedPane.addTab("Create Customer", createCustomerPanel());
        tabbedPane.addTab("Open Account", createOpenAccountPanel());

        frame.add(tabbedPane, BorderLayout.CENTER);

        //LOGOUT PANEL
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(52, 152, 219));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(new Color(52, 152, 219));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginView().show();
        });
        bottomPanel.add(logoutButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        formPanel.setBackground(new Color(245, 248, 255));

        JTextField natIDField = new JTextField(String.valueOf(teller.getNationalID()), 20);
        natIDField.setEditable(false);
        JTextField firstNameField = new JTextField(teller.getFirstName(), 20);
        JTextField lastNameField = new JTextField(teller.getLastName(), 20);
        JTextField dobField = new JTextField(teller.getDateOfBirth(), 20);
        JTextField addressField = new JTextField(teller.getAddress(), 20);
        JTextField emailField = new JTextField(teller.getEmailAddress(), 20);
        JTextField contactField = new JTextField(String.valueOf(teller.getContact()), 20);
        JTextField tellerIDField = new JTextField(String.valueOf(teller.getBankTellerID()), 20);
        tellerIDField.setEditable(false);
        JPasswordField passField = new JPasswordField(teller.getBankTellerPassword(), 20);

        formPanel.add(new JLabel("National ID:", SwingConstants.RIGHT)); formPanel.add(natIDField);
        formPanel.add(new JLabel("First Name:", SwingConstants.RIGHT)); formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:", SwingConstants.RIGHT)); formPanel.add(lastNameField);
        formPanel.add(new JLabel("Date of Birth:", SwingConstants.RIGHT)); formPanel.add(dobField);
        formPanel.add(new JLabel("Address:", SwingConstants.RIGHT)); formPanel.add(addressField);
        formPanel.add(new JLabel("Email:", SwingConstants.RIGHT)); formPanel.add(emailField);
        formPanel.add(new JLabel("Contact:", SwingConstants.RIGHT)); formPanel.add(contactField);
        formPanel.add(new JLabel("Teller ID:", SwingConstants.RIGHT)); formPanel.add(tellerIDField);
        formPanel.add(new JLabel("Password:", SwingConstants.RIGHT)); formPanel.add(passField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(245, 248, 255));

        JButton updateButton = new JButton("Update Profile");
        JButton deleteButton = new JButton("Delete Profile");

        updateButton.setBackground(Color.WHITE); updateButton.setForeground(new Color(52, 152, 219));
        deleteButton.setBackground(Color.WHITE); deleteButton.setForeground(new Color(52, 152, 219));

        // ACTION LISTENERS (UNCHANGED)
        updateButton.addActionListener(e -> {
            try {
                teller.setFirstName(firstNameField.getText().trim());
                teller.setLastName(lastNameField.getText().trim());
                teller.setDateOfBirth(dobField.getText().trim());
                teller.setAddress(addressField.getText().trim());
                teller.setEmailAddress(emailField.getText().trim());
                teller.setContact(Integer.parseInt(contactField.getText().trim()));
                teller.setBankTellerPassword(new String(passField.getPassword()));
                new dao.BankTellerDAO().updateBankTeller(teller);
                JOptionPane.showMessageDialog(null, "Profile updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid contact", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Delete profile?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new dao.BankTellerDAO().deleteBankTeller(teller.getBankTellerID());
                JOptionPane.showMessageDialog(null, "Profile deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        formPanel.setBackground(new Color(245, 248, 255));
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Customer"));

        JTextField[] fields = {new JTextField(), new JTextField(), new JTextField(), new JTextField(),
                new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JPasswordField()};
        String[] labels = {"National ID", "First Name", "Last Name", "DOB", "Address", "Email", "Contact", "Customer ID", "Password"};

        for (int i = 0; i < 8; i++) {
            formPanel.add(new JLabel(labels[i] + ":", SwingConstants.RIGHT));
            formPanel.add(fields[i]);
        }
        formPanel.add(new JLabel("Password:", SwingConstants.RIGHT));
        formPanel.add(fields[8]);

        JButton createButton = new JButton("Create Customer");
        createButton.setBackground(Color.WHITE);
        createButton.setForeground(new Color(52, 152, 219));
        createButton.addActionListener(e -> {
            try {
                Customer customer = new Customer(Integer.parseInt(fields[0].getText()), fields[1].getText(),
                        fields[2].getText(), fields[3].getText(), fields[4].getText(), fields[5].getText(),
                        Integer.parseInt(fields[6].getText()), fields[7].getText(), new String(((JPasswordField)fields[8]).getPassword()));
                new CustomerDAO().addCustomer(customer);
                JOptionPane.showMessageDialog(null, "Customer created!", "Success", JOptionPane.INFORMATION_MESSAGE);
                for (int j = 0; j < 8; j++) fields[j].setText("");
                ((JPasswordField)fields[8]).setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(createButton);
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOpenAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        formPanel.setBackground(new Color(245, 248, 255));
        formPanel.setBorder(BorderFactory.createTitledBorder("Open New Account"));

        JTextField customerIDField = new JTextField();
        JComboBox<String> accountTypeCombo = new JComboBox<>(new String[]{"savings", "investment", "cheque"});
        JTextField accountNameField = new JTextField();
        JTextField initialBalanceField = new JTextField();
        JTextField companyNameField = new JTextField();
        JTextField companyAddressField = new JTextField();

        formPanel.add(new JLabel("Customer ID:", SwingConstants.RIGHT)); formPanel.add(customerIDField);
        formPanel.add(new JLabel("Account Type:", SwingConstants.RIGHT)); formPanel.add(accountTypeCombo);
        formPanel.add(new JLabel("Account Name:", SwingConstants.RIGHT)); formPanel.add(accountNameField);
        formPanel.add(new JLabel("Initial Balance:", SwingConstants.RIGHT)); formPanel.add(initialBalanceField);
        formPanel.add(new JLabel("Company Name:", SwingConstants.RIGHT)); formPanel.add(companyNameField);
        formPanel.add(new JLabel("Company Address:", SwingConstants.RIGHT)); formPanel.add(companyAddressField);

        JButton openButton = new JButton("Open Account");
        openButton.setBackground(Color.WHITE);
        openButton.setForeground(new Color(52, 152, 219));
        openButton.addActionListener(e -> {
            try {
                Customer customer = new CustomerDAO().getCustomerByID(customerIDField.getText());
                if (customer == null) {
                    JOptionPane.showMessageDialog(null, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                AccountController ac = new AccountController();
                String result = ac.openAccount(customer, (String)accountTypeCombo.getSelectedItem(),
                        Double.parseDouble(initialBalanceField.getText()), accountNameField.getText(),
                        companyNameField.getText(), companyAddressField.getText());
                JOptionPane.showMessageDialog(null, result, result.startsWith("Success") ? "Success" : "Error",
                        result.startsWith("Success") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(openButton);
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }
}
