package view;

import controller.AdminController;
import dao.BankTellerDAO;
import model.bankTeller;
import model.systemAdmin;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminView {
    private systemAdmin admin;
    private AdminController adminController;
    private BankTellerDAO tellerDAO = new BankTellerDAO();

    public AdminView(systemAdmin admin, AdminController adminController) {
        this.admin = admin;
        this.adminController = adminController;
    }

    public void show() {
        JFrame frame = new JFrame("Seane Banking App - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(new Color(245, 248, 255));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(52, 152, 219));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));


        tabbedPane.addTab("Profile", createProfilePanel());
        tabbedPane.addTab("Manage Tellers", createTellerPanel());
        tabbedPane.addTab("View Users", createUsersPanel());

        frame.add(tabbedPane, BorderLayout.CENTER);

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
        frame.setVisible(true);
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // PROFILE FORM
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        formPanel.setBackground(new Color(245, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField firstNameField = new JTextField(admin.getFirstName(), 20);
        JTextField lastNameField = new JTextField(admin.getLastName(), 20);
        JTextField dobField = new JTextField(admin.getDateOfBirth(), 20);
        JTextField addressField = new JTextField(admin.getAddress(), 20);
        JTextField emailField = new JTextField(admin.getEmailAddress(), 20);
        JTextField contactField = new JTextField(String.valueOf(admin.getContact()), 20);
        JPasswordField passwordField = new JPasswordField(admin.getAdminPassword(), 20);

        formPanel.add(new JLabel("First Name:", SwingConstants.RIGHT));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:", SwingConstants.RIGHT));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Date of Birth:", SwingConstants.RIGHT));
        formPanel.add(dobField);
        formPanel.add(new JLabel("Address:", SwingConstants.RIGHT));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Email:", SwingConstants.RIGHT));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Contact:", SwingConstants.RIGHT));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Password:", SwingConstants.RIGHT));
        formPanel.add(passwordField);

        // BUTTONS PANEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(245, 248, 255));

        JButton updateButton = new JButton("Update Profile");
        JButton deleteButton = new JButton("Delete Profile");
        JButton enforceButton = new JButton("Enforce Policies");
        JButton updateSystemButton = new JButton("Update System");

        // STYLE BUTTONS
        JButton[] buttons = {updateButton, deleteButton, enforceButton, updateSystemButton};
        for (JButton btn : buttons) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(52, 152, 219));
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setPreferredSize(new Dimension(150, 35));
        }

        // ACTION LISTENERS (UNCHANGED)
        updateButton.addActionListener(e -> {
            try {
                admin.setFirstName(firstNameField.getText().trim());
                admin.setLastName(lastNameField.getText().trim());
                admin.setDateOfBirth(dobField.getText().trim());
                admin.setAddress(addressField.getText().trim());
                admin.setEmailAddress(emailField.getText().trim());
                admin.setContact(Integer.parseInt(contactField.getText().trim()));
                admin.setAdminPassword(new String(passwordField.getPassword()));
                adminController.updateAdminProfile(admin);
                JOptionPane.showMessageDialog(null, "Profile updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid contact number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your profile?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                adminController.deleteAdmin(admin.getAdminID());
                JOptionPane.showMessageDialog(null, "Profile deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

        enforceButton.addActionListener(e -> {
            String result = adminController.enforcePolicies();
            JOptionPane.showMessageDialog(null, result, "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        updateSystemButton.addActionListener(e -> {
            String result = adminController.updateSystem();
            JOptionPane.showMessageDialog(null, result, "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(enforceButton);
        buttonPanel.add(updateSystemButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTellerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TELLER LIST
        DefaultListModel<bankTeller> tellerListModel = new DefaultListModel<>();
        for (bankTeller teller : tellerDAO.getAllBankTellers()) {
            tellerListModel.addElement(teller);
        }

        JList<bankTeller> tellerList = new JList<>(tellerListModel);
        tellerList.setBackground(Color.WHITE);
        tellerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof bankTeller) {
                    bankTeller t = (bankTeller) value;
                    setText(t.getFirstName() + " " + t.getLastName() + " (ID: " + t.getBankTellerID() + ")");
                }
                return c;
            }
        });

        // ADD/REMOVE BUTTONS
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(new Color(245, 248, 255));
        listPanel.add(new JLabel("All Bank Tellers:", SwingConstants.CENTER), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(tellerList), BorderLayout.CENTER);

        JButton removeButton = new JButton("Remove Selected Teller");
        removeButton.setBackground(Color.WHITE);
        removeButton.setForeground(new Color(52, 152, 219));
        removeButton.addActionListener(e -> {
            bankTeller selected = tellerList.getSelectedValue();
            if (selected != null) {
                adminController.removeBankTeller(selected.getBankTellerID());
                tellerListModel.removeElement(selected);
                JOptionPane.showMessageDialog(null, "Teller removed", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        listPanel.add(removeButton, BorderLayout.SOUTH);

        // ADD TELLER FORM (SIMPLIFIED)
        JPanel addForm = new JPanel(new GridLayout(0, 2, 10, 10));
        addForm.setBackground(new Color(245, 248, 255));
        addForm.setBorder(BorderFactory.createTitledBorder("Add New Teller"));

        JTextField[] fields = {
                new JTextField(), new JTextField(), new JTextField(), new JTextField(),
                new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()
        };
        String[] labels = {"National ID", "First Name", "Last Name", "DOB", "Address", "Email", "Contact", "Teller ID", "Password"};

        for (int i = 0; i < labels.length; i++) {
            addForm.add(new JLabel(labels[i] + ":", SwingConstants.RIGHT));
            addForm.add(fields[i]);
        }

        JButton addButton = new JButton("Add Teller");
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(new Color(52, 152, 219));
        addButton.addActionListener(e -> {
            // ADD TELLER LOGIC (UNCHANGED)
            try {
                int nationalID = Integer.parseInt(fields[0].getText());
                int contact = Integer.parseInt(fields[6].getText());
                int tellerID = Integer.parseInt(fields[7].getText());
                bankTeller teller = new bankTeller(nationalID, fields[1].getText(), fields[2].getText(),
                        fields[3].getText(), fields[4].getText(), fields[5].getText(), contact, tellerID, fields[8].getText());
                adminController.addBankTeller(teller);
                tellerListModel.addElement(teller);
                JOptionPane.showMessageDialog(null, "Teller added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear fields
                for (JTextField field : fields) field.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addForm.add(addButton);

        panel.add(listPanel, BorderLayout.CENTER);
        panel.add(addForm, BorderLayout.EAST);
        return panel;
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane usersTabbedPane = new JTabbedPane();
        usersTabbedPane.setBackground(new Color(52, 152, 219));
        usersTabbedPane.setForeground(Color.WHITE);

        // CUSTOMERS TABLE
        DefaultTableModel customerModel = new DefaultTableModel(new String[]{"Customer ID", "Name", "Email", "Contact"}, 0);
        JTable customerTable = new JTable(customerModel);
        customerTable.setBackground(Color.WHITE);
        loadCustomers(customerModel);
        usersTabbedPane.addTab("Customers", new JScrollPane(customerTable));

        // TELLERS TABLE
        DefaultTableModel tellerModel = new DefaultTableModel(new String[]{"Teller ID", "Name", "Email", "Contact"}, 0);
        JTable tellerTable = new JTable(tellerModel);
        tellerTable.setBackground(Color.WHITE);
        loadTellers(tellerModel);
        usersTabbedPane.addTab("Tellers", new JScrollPane(tellerTable));

        panel.add(usersTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadCustomers(DefaultTableModel model) {
        model.setRowCount(0);
        List<Customer> customers = adminController.getAllCustomers();
        for (Customer customer : customers) {
            model.addRow(new Object[]{
                    customer.getCustomerID(),
                    customer.getFirstName() + " " + customer.getLastName(),
                    customer.getEmailAddress(),
                    customer.getContact()
            });
        }
    }

    private void loadTellers(DefaultTableModel model) {
        model.setRowCount(0);
        List<bankTeller> tellers = adminController.getAllBankTellers();
        for (bankTeller teller : tellers) {
            model.addRow(new Object[]{
                    teller.getBankTellerID(),
                    teller.getFirstName() + " " + teller.getLastName(),
                    teller.getEmailAddress(),
                    teller.getContact()
            });
        }
    }
}
