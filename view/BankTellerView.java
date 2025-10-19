package view;

import controller.CustomerController;
import model.Account;
import model.Customer;
import model.bankTeller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankTellerView {
    private bankTeller teller;
    private CustomerController customerController;

    public BankTellerView(bankTeller teller, CustomerController customerController) {
        this.teller = teller;
        this.customerController = customerController;
    }

    public void show() {
        JFrame frame = new JFrame("Bank Teller Dashboard - " + teller.getFirstName() + " " + teller.getLastName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Create Customer Tab
        JPanel createCustomerPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        JTextField natIDField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField custIDField = new JTextField();
        JTextField passField = new JTextField();

        JButton createCustomerButton = new JButton("Create Customer");
        createCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int natID = Integer.parseInt(natIDField.getText().trim());
                    int contact = Integer.parseInt(contactField.getText().trim());
                    String password = passField.getText().trim();
                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
                            emailField.getText().trim().isEmpty() || custIDField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Required fields (First Name, Last Name, Email, Customer ID) cannot be empty",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Customer customer = teller.createCustomerProfile(
                            natID, firstNameField.getText().trim(), lastNameField.getText().trim(),
                            dobField.getText().trim(), addressField.getText().trim(), emailField.getText().trim(),
                            contact, custIDField.getText().trim(), password
                    );
                    customerController.createCustomer(customer);
                    JOptionPane.showMessageDialog(null, "Customer created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear fields
                    natIDField.setText("");
                    firstNameField.setText("");
                    lastNameField.setText("");
                    dobField.setText("");
                    addressField.setText("");
                    emailField.setText("");
                    contactField.setText("");
                    custIDField.setText("");
                    passField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input for National ID or Contact", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        createCustomerPanel.add(new JLabel("National ID:"));
        createCustomerPanel.add(natIDField);
        createCustomerPanel.add(new JLabel("First Name:"));
        createCustomerPanel.add(firstNameField);
        createCustomerPanel.add(new JLabel("Last Name:"));
        createCustomerPanel.add(lastNameField);
        createCustomerPanel.add(new JLabel("Date of Birth:"));
        createCustomerPanel.add(dobField);
        createCustomerPanel.add(new JLabel("Address:"));
        createCustomerPanel.add(addressField);
        createCustomerPanel.add(new JLabel("Email:"));
        createCustomerPanel.add(emailField);
        createCustomerPanel.add(new JLabel("Contact:"));
        createCustomerPanel.add(contactField);
        createCustomerPanel.add(new JLabel("Customer ID:"));
        createCustomerPanel.add(custIDField);
        createCustomerPanel.add(new JLabel("Password:"));
        createCustomerPanel.add(passField);
        createCustomerPanel.add(createCustomerButton);

        tabbedPane.addTab("Create Customer", createCustomerPanel);

        // Open Account Tab
        JPanel openAccountPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        JTextField customerIDField = new JTextField();
        JComboBox<String> accountTypeCombo = new JComboBox<>(new String[]{"Savings", "Investment", "Cheque"});
        JTextField accountNameField = new JTextField();
        JTextField initialBalanceField = new JTextField();
        JTextField companyNameField = new JTextField();
        JTextField companyAddressField = new JTextField();
        JButton openAccountButton = new JButton("Open Account");

        companyNameField.setEnabled(false);
        companyAddressField.setEnabled(false);
        accountTypeCombo.addActionListener(e -> {
            boolean isCheque = accountTypeCombo.getSelectedItem().equals("Cheque");
            companyNameField.setEnabled(isCheque);
            companyAddressField.setEnabled(isCheque);
        });

        openAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String customerID = customerIDField.getText().trim();
                    if (customerID.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Customer ID cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String accountType = (String) accountTypeCombo.getSelectedItem();
                    String accountName = accountNameField.getText().trim();
                    if (accountName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Account name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double initialBalance = Double.parseDouble(initialBalanceField.getText().trim());
                    Customer customer = customerController.getCustomer(customerID);
                    if (customer == null) {
                        JOptionPane.showMessageDialog(null, "Customer not found", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String[] extraParams = accountType.equals("Cheque") ?
                            new String[]{companyNameField.getText().trim(), companyAddressField.getText().trim()} :
                            new String[]{};
                    if (accountType.equals("Cheque") && (extraParams[0].isEmpty() || extraParams[1].isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Customer must be employed and provide valid company name and address for Cheque accounts",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Account account = teller.openCustomerAccount(customer, accountType, initialBalance, accountName, extraParams);
                    customerController.addAccountToCustomer(customerID, account);
                    JOptionPane.showMessageDialog(null, "Account '" + accountName + "' opened successfully with ID " + account.getAccountID(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear fields
                    customerIDField.setText("");
                    accountNameField.setText("");
                    initialBalanceField.setText("");
                    companyNameField.setText("");
                    companyAddressField.setText("");
                    accountTypeCombo.setSelectedIndex(0);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid balance format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        openAccountPanel.add(new JLabel("Customer ID:"));
        openAccountPanel.add(customerIDField);
        openAccountPanel.add(new JLabel("Account Type:"));
        openAccountPanel.add(accountTypeCombo);
        openAccountPanel.add(new JLabel("Account Name:"));
        openAccountPanel.add(accountNameField);
        openAccountPanel.add(new JLabel("Initial Balance:"));
        openAccountPanel.add(initialBalanceField);
        openAccountPanel.add(new JLabel("Company Name (Cheque):"));
        openAccountPanel.add(companyNameField);
        openAccountPanel.add(new JLabel("Company Address (Cheque):"));
        openAccountPanel.add(companyAddressField);
        openAccountPanel.add(openAccountButton);

        tabbedPane.addTab("Open Account", openAccountPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}