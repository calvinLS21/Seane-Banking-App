package view;

import controller.AccountController;
import controller.CustomerController;
import model.Account;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerView {
    private Customer customer;
    private CustomerController customerController;
    private AccountController accountController;

    public CustomerView(Customer customer, CustomerController customerController, AccountController accountController) {
        this.customer = customer;
        this.customerController = customerController;
        this.accountController = accountController;
    }

    public void show() {
        JFrame frame = new JFrame("Customer Dashboard - " + customer.getFirstName() + " " + customer.getLastName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel infoPanel = createPersonalInfoPanel();
        tabbedPane.addTab("Personal Info", infoPanel);

        JPanel accountsPanel = createAccountsPanel();
        tabbedPane.addTab("Accounts", accountsPanel);

        JPanel transactionPanel = createTransactionHistoryPanel();
        tabbedPane.addTab("Transaction History", transactionPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField firstNameField = new JTextField(customer.getFirstName(), 15);
        JTextField lastNameField = new JTextField(customer.getLastName(), 15);
        JTextField dobField = new JTextField(customer.getDateOfBirth(), 15);
        JTextField addressField = new JTextField(customer.getAddress(), 15);
        JTextField emailField = new JTextField(customer.getEmailAddress(), 15);
        JTextField contactField = new JTextField(String.valueOf(customer.getContact()), 15);

        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(150, 30));
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int contact = Integer.parseInt(contactField.getText().trim());
                    String firstName = firstNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String dob = dobField.getText().trim();
                    String address = addressField.getText().trim();
                    String email = emailField.getText().trim();
                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "First Name, Last Name, and Email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    customerController.updatePersonalInformation(customer.getCustomerID(), firstName, lastName, dob, address, email, contact);
                    JOptionPane.showMessageDialog(null, "Personal information updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid contact format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Date of Birth:"));
        panel.add(dobField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);
        panel.add(updateButton);

        return panel;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultListModel<Account> accountListModel = new DefaultListModel<>();
        JList<Account> accountList = new JList<>(accountListModel);
        for (Account account : customer.getAccounts()) {
            accountListModel.addElement(account);
        }
        accountList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account) {
                    Account acc = (Account) value;
                    setText(acc.getAccountName() + " (" + acc.getClass().getSimpleName() + ", ID: " + acc.getAccountID() + ")");
                }
                return c;
            }
        });

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        JTextField amountField = new JTextField(10);
        JTextField toAccountField = new JTextField(10);
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selected = accountList.getSelectedValue();
                if (selected == null) {
                    JOptionPane.showMessageDialog(null, "No account selected", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String result = accountController.deposit(selected.getAccountID(), amount);
                    JOptionPane.showMessageDialog(null, result, "Deposit", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selected = accountList.getSelectedValue();
                if (selected == null) {
                    JOptionPane.showMessageDialog(null, "No account selected", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String result = accountController.withdraw(selected.getAccountID(), amount);
                    JOptionPane.showMessageDialog(null, result, "Withdraw", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selected = accountList.getSelectedValue();
                if (selected == null) {
                    JOptionPane.showMessageDialog(null, "No account selected", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int toAccountID = Integer.parseInt(toAccountField.getText().trim());
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String result = accountController.transfer(selected.getAccountID(), toAccountID, amount);
                    JOptionPane.showMessageDialog(null, result, "Transfer", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount or account ID format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        actionPanel.add(new JLabel("Amount:"));
        actionPanel.add(amountField);
        actionPanel.add(new JLabel("To Account ID (Transfer):"));
        actionPanel.add(toAccountField);
        actionPanel.add(depositButton);
        actionPanel.add(withdrawButton);
        actionPanel.add(transferButton);

        panel.add(new JScrollPane(accountList), BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultListModel<Account> accountListModel = new DefaultListModel<>();
        JList<Account> accountList = new JList<>(accountListModel);
        for (Account account : customer.getAccounts()) {
            accountListModel.addElement(account);
        }
        accountList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account) {
                    Account acc = (Account) value;
                    setText(acc.getAccountName() + " (" + acc.getClass().getSimpleName() + ", ID: " + acc.getAccountID() + ")");
                }
                return c;
            }
        });

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"Transaction ID", "Type", "Amount", "Date", "To Account ID"}, 0
        );
        JTable transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setFillsViewportHeight(true);

        accountList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Account selected = accountList.getSelectedValue();
                tableModel.setRowCount(0); // Clear table
                if (selected != null) {
                    for (String[] transaction : selected.transactionHistory()) {
                        tableModel.addRow(transaction);
                    }
                }
            }
        });

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        selectionPanel.add(new JLabel("Select Account:"));
        selectionPanel.add(accountList);

        panel.add(selectionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        return panel;
    }
}