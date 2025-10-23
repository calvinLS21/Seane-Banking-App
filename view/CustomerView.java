package view;

import controller.AccountController;
import controller.CustomerController;
import model.Account;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
        JFrame frame = new JFrame("Seane Banking App - Customer Dashboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(new Color(245, 248, 255));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(52, 152, 219));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("Personal Info", createPersonalInfoPanel());
        tabbedPane.addTab("Accounts", createAccountsPanel());
        tabbedPane.addTab("Transaction History", createTransactionHistoryPanel());

        // LOGOUT PANEL
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
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ✅ SIMPLE GRIDLAYOUT - NO ERRORS
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 15, 15));
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField firstNameField = new JTextField(customer.getFirstName());
        JTextField lastNameField = new JTextField(customer.getLastName());
        JTextField dobField = new JTextField(customer.getDateOfBirth());
        JTextField addressField = new JTextField(customer.getAddress());
        JTextField emailField = new JTextField(customer.getEmailAddress());
        JTextField contactField = new JTextField(String.valueOf(customer.getContact()));

        panel.add(new JLabel("First Name:", SwingConstants.RIGHT));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:", SwingConstants.RIGHT));
        panel.add(lastNameField);
        panel.add(new JLabel("Date of Birth:", SwingConstants.RIGHT));
        panel.add(dobField);
        panel.add(new JLabel("Address:", SwingConstants.RIGHT));
        panel.add(addressField);
        panel.add(new JLabel("Email:", SwingConstants.RIGHT));
        panel.add(emailField);
        panel.add(new JLabel("Contact:", SwingConstants.RIGHT));
        panel.add(contactField);

        JButton updateButton = new JButton("Update Profile");
        updateButton.setBackground(new Color(52, 152, 219));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(new JLabel(""));
        panel.add(updateButton);

        updateButton.addActionListener(e -> {
            try {
                int contact = Integer.parseInt(contactField.getText().trim());
                customerController.updatePersonalInformation(
                        customer.getCustomerID(), firstNameField.getText().trim(), lastNameField.getText().trim(),
                        dobField.getText().trim(), addressField.getText().trim(), emailField.getText().trim(), contact
                );
                JOptionPane.showMessageDialog(null, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid contact number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ACCOUNT LIST
        DefaultListModel<Account> accountListModel = new DefaultListModel<>();
        for (Account account : customer.getAccounts()) {
            accountListModel.addElement(account);
        }

        JList<Account> accountList = new JList<>(accountListModel);
        accountList.setBackground(Color.WHITE);
        accountList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account) {
                    Account acc = (Account) value;
                    setText(acc.getAccountName() + " (" + acc.getClass().getSimpleName() + ", BWP " + acc.getAccountBalance() + ")");
                }
                return c;
            }
        });

        // ACTION PANEL - SIMPLE GRID
        JPanel actionPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        actionPanel.setBackground(new Color(245, 248, 255));

        JTextField amountField = new JTextField();
        JTextField toAccountField = new JTextField();
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");

        // STYLE BUTTONS
        JButton[] buttons = {depositButton, withdrawButton, transferButton};
        for (JButton btn : buttons) {
            btn.setBackground(new Color(52, 152, 219));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
        }

        actionPanel.add(new JLabel("Amount:"));
        actionPanel.add(amountField);
        actionPanel.add(new JLabel("To Account:"));
        actionPanel.add(toAccountField);
        actionPanel.add(depositButton);
        actionPanel.add(withdrawButton);
        actionPanel.add(transferButton);
        actionPanel.add(new JLabel(""));

        // ACTION LISTENERS
        depositButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                Account selected = accountList.getSelectedValue();
                if (selected != null) {
                    String result = accountController.deposit(selected.getAccountID(), amount);
                    JOptionPane.showMessageDialog(null, result, "Deposit", JOptionPane.INFORMATION_MESSAGE);
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an account", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                Account selected = accountList.getSelectedValue();
                if (selected != null) {
                    String result = accountController.withdraw(selected.getAccountID(), amount);
                    JOptionPane.showMessageDialog(null, result, "Withdraw", JOptionPane.INFORMATION_MESSAGE);
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an account", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        transferButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                int toAccountID = Integer.parseInt(toAccountField.getText());
                Account selected = accountList.getSelectedValue();
                if (selected != null) {
                    String result = accountController.transfer(selected.getAccountID(), toAccountID, amount);
                    JOptionPane.showMessageDialog(null, result, "Transfer", JOptionPane.INFORMATION_MESSAGE);
                    amountField.setText("");
                    toAccountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an account", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount or account ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JScrollPane(accountList), BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ACCOUNT LIST
        DefaultListModel<Account> accountListModel = new DefaultListModel<>();
        for (Account account : customer.getAccounts()) {
            accountListModel.addElement(account);
        }

        JList<Account> accountList = new JList<>(accountListModel);
        accountList.setBackground(Color.WHITE);
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

        // TABLE
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"Transaction ID", "Type", "Amount", "Date", "To Account ID"}, 0
        );
        JTable transactionTable = new JTable(tableModel);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setBackground(Color.WHITE);

        // SELECTION LISTENER
        accountList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Account selected = accountList.getSelectedValue();
                    tableModel.setRowCount(0);
                    if (selected != null) {
                        for (String[] transaction : selected.transactionHistory()) {
                            tableModel.addRow(transaction);
                        }
                    }
                }
            }
        });

        // LAYOUT
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBackground(new Color(245, 248, 255));
        selectionPanel.add(new JLabel("Select Account:"));
        selectionPanel.add(accountList);

        panel.add(selectionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        return panel;
    }
}
