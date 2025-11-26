package view;

import controller.AccountController;
import controller.CustomerController;
import dao.AccountDAO;
import model.Account;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerView {
    private Customer customer;
    private CustomerController customerController;
    private AccountController accountController;

    private JList<Account> accountList;
    private JTable transactionTable;
    private DefaultListModel<Account> accountListModel;  // Shared model

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

        // Load accounts from DB after UI is ready
        loadAccountsFromDatabase();
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 15, 15));
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField firstNameField = new JTextField(customer.getFirstName(), 20);
        JTextField lastNameField = new JTextField(customer.getLastName(), 20);
        JTextField dobField = new JTextField(customer.getDateOfBirth(), 20);
        JTextField addressField = new JTextField(customer.getAddress(), 20);
        JTextField emailField = new JTextField(customer.getEmailAddress(), 20);
        JTextField contactField = new JTextField(String.valueOf(customer.getContact()), 20);

        panel.add(new JLabel("First Name:", SwingConstants.RIGHT)); panel.add(firstNameField);
        panel.add(new JLabel("Last Name:", SwingConstants.RIGHT));  panel.add(lastNameField);
        panel.add(new JLabel("Date of Birth:", SwingConstants.RIGHT)); panel.add(dobField);
        panel.add(new JLabel("Address:", SwingConstants.RIGHT)); panel.add(addressField);
        panel.add(new JLabel("Email:", SwingConstants.RIGHT)); panel.add(emailField);
        panel.add(new JLabel("Contact:", SwingConstants.RIGHT)); panel.add(contactField);

        JButton updateBtn = new JButton("Update Information");
        updateBtn.addActionListener(e -> {
            boolean success = customer.updatePersonalInformation(
                    firstNameField.getText(), lastNameField.getText(), dobField.getText(),
                    addressField.getText(), emailField.getText(),
                    Integer.parseInt(contactField.getText())
            );
            JOptionPane.showMessageDialog(null, success ? "Updated!" : "Invalid data");
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        panel.add(new JLabel()); panel.add(btnPanel);

        return panel;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Shared model
        accountListModel = new DefaultListModel<>();
        accountList = new JList<>(accountListModel);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        accountList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account acc) {
                    String type = acc.getClass().getSimpleName().replace("Account", "");
                    setText(acc.getAccountName() + " (" + type + ", ID: " + acc.getAccountID() +
                            ", BWP " + String.format("%.2f", acc.getAccountBalance()) + ")");
                }
                return this;
            }
        });

        JPanel actionPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        actionPanel.setBackground(new Color(245, 248, 255));

        JTextField amountField = new JTextField(10);
        JTextField toAccountField = new JTextField(10);

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton transferBtn = new JButton("Transfer");

        actionPanel.add(new JLabel("Amount:"));
        actionPanel.add(amountField);
        actionPanel.add(new JLabel("To Account ID:"));
        actionPanel.add(toAccountField);
        actionPanel.add(depositBtn);
        actionPanel.add(withdrawBtn);
        actionPanel.add(transferBtn);

        depositBtn.addActionListener(e -> performDeposit(amountField));
        withdrawBtn.addActionListener(e -> performWithdraw(amountField));
        transferBtn.addActionListener(e -> performTransfer(amountField, toAccountField));

        panel.add(new JScrollPane(accountList), BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"Transaction ID", "Type", "Amount", "Date", "To Account ID"}, 0
        );
        transactionTable = new JTable(tableModel);

        // Use the SAME list and model
        accountList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refreshTransactionHistory();
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245, 248, 255));
        top.add(new JLabel("Select Account:"), BorderLayout.WEST);
        top.add(new JScrollPane(accountList), BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        return panel;
    }

    // ACTION METHODS
    private void performDeposit(JTextField field) {
        String amt = field.getText().trim();
        if (amt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double amount = Double.parseDouble(amt);
            Account acc = accountList.getSelectedValue();
            if (acc == null) {
                JOptionPane.showMessageDialog(null, "Please select an account", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String result = accountController.deposit(acc.getAccountID(), amount);
            JOptionPane.showMessageDialog(null, result, "Success", JOptionPane.INFORMATION_MESSAGE);
            field.setText("");
            loadAccountsFromDatabase(); // This fixes everything
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performWithdraw(JTextField field) {
        String amt = field.getText().trim();
        if (amt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double amount = Double.parseDouble(amt);
            Account acc = accountList.getSelectedValue();
            if (acc == null) {
                JOptionPane.showMessageDialog(null, "Please select an account", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String result = accountController.withdraw(acc.getAccountID(), amount);
            JOptionPane.showMessageDialog(null, result, "Success", JOptionPane.INFORMATION_MESSAGE);
            field.setText("");
            loadAccountsFromDatabase();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performTransfer(JTextField amountField, JTextField toField) {
        String amt = amountField.getText().trim();
        String toId = toField.getText().trim();
        if (amt.isEmpty() || toId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fill both fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double amount = Double.parseDouble(amt);
            int toAccountId = Integer.parseInt(toId);
            Account fromAcc = accountList.getSelectedValue();
            if (fromAcc == null) {
                JOptionPane.showMessageDialog(null, "Please select an account", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String result = accountController.transfer(fromAcc.getAccountID(), toAccountId, amount);
            JOptionPane.showMessageDialog(null, result, "Transfer", JOptionPane.INFORMATION_MESSAGE);
            amountField.setText("");
            toField.setText("");
            loadAccountsFromDatabase();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Load accounts and refresh transaction history
    private void loadAccountsFromDatabase() {
        accountListModel.clear();
        AccountDAO dao = new AccountDAO();
        List<Account> accounts = dao.getAccountsByCustomer(customer.getCustomerID());

        for (Account acc : accounts) {
            accountListModel.addElement(acc);
            customer.addAccount(acc);
        }

        if (!accounts.isEmpty() && accountList.getSelectedIndex() == -1) {
            accountList.setSelectedIndex(0);
        }

        refreshTransactionHistory();
    }

    private void refreshTransactionHistory() {
        if (transactionTable == null) return;
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);

        Account selected = accountList.getSelectedValue();
        if (selected != null) {
            for (String[] row : selected.transactionHistory()) {
                model.addRow(row);
            }
        }
    }
}
