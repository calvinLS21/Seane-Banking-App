package view;

import controller.AdminController;
import dao.BankTellerDAO;
import model.bankTeller;
import model.systemAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminView {
    private systemAdmin admin;
    private AdminController adminController;
    private BankTellerDAO tellerDAO = new BankTellerDAO();

    public AdminView(systemAdmin admin, AdminController adminController) {
        this.admin = admin;
        this.adminController = adminController;
    }

    public void show() {
        JFrame frame = new JFrame("Admin Dashboard - " + admin.getFirstName() + " " + admin.getLastName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();

        // Profile Tab
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextArea profileText = new JTextArea(admin.toString(), 5, 30);
        profileText.setEditable(false);
        profileText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        profilePanel.add(new JLabel("Admin Profile:"));
        profilePanel.add(profileText);

        JButton updateSystemButton = new JButton("Update System");
        updateSystemButton.setPreferredSize(new Dimension(150, 30));
        updateSystemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.updateBankingSystem();
                JOptionPane.showMessageDialog(null, "System updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        profilePanel.add(updateSystemButton);

        JButton enforcePoliciesButton = new JButton("Enforce Policies");
        enforcePoliciesButton.setPreferredSize(new Dimension(150, 30));
        enforcePoliciesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.enforcePolicies();
                JOptionPane.showMessageDialog(null, "Policies enforced", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        profilePanel.add(enforcePoliciesButton);

        tabbedPane.addTab("Profile", profilePanel);

        // Password Reset Tab
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField customerIDField = new JTextField(15);
        JPasswordField customerPassField = new JPasswordField(15);
        JTextField tellerIDField = new JTextField(15);
        JPasswordField tellerPassField = new JPasswordField(15);
        JButton resetCustomerButton = new JButton("Reset Customer Password");
        resetCustomerButton.setPreferredSize(new Dimension(200, 30));
        JButton resetTellerButton = new JButton("Reset Teller Password");
        resetTellerButton.setPreferredSize(new Dimension(200, 30));

        resetCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerID = customerIDField.getText().trim();
                String newPassword = new String(customerPassField.getPassword());
                if (customerID.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Customer ID and new password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                adminController.resetCustomerPassword(customerID, newPassword);
                JOptionPane.showMessageDialog(null, "Customer password reset", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        resetTellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int tellerID = Integer.parseInt(tellerIDField.getText().trim());
                    String newPassword = new String(tellerPassField.getPassword());
                    if (newPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "New password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    adminController.resetTellerPassword(tellerID, newPassword);
                    JOptionPane.showMessageDialog(null, "Teller password reset", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Teller ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        passwordPanel.add(new JLabel("Customer ID:"));
        passwordPanel.add(customerIDField);
        passwordPanel.add(new JLabel("New Password:"));
        passwordPanel.add(customerPassField);
        passwordPanel.add(resetCustomerButton);

        passwordPanel.add(new JLabel("Teller ID:"));
        passwordPanel.add(tellerIDField);
        passwordPanel.add(new JLabel("New Password:"));
        passwordPanel.add(tellerPassField);
        passwordPanel.add(resetTellerButton);

        tabbedPane.addTab("Password Reset", passwordPanel);

        // Manage Tellers Tab
        JPanel tellerPanel = new JPanel(new BorderLayout(10, 10));
        tellerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultListModel<bankTeller> tellerListModel = new DefaultListModel<>();
        JList<bankTeller> tellerList = new JList<>(tellerListModel);
        tellerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof bankTeller) {
                    bankTeller teller = (bankTeller) value;
                    setText(teller.getFirstName() + " " + teller.getLastName() + " (ID: " + teller.getBankTellerID() + ")");
                }
                return c;
            }
        });
        for (bankTeller teller : tellerDAO.getAllBankTellers()) {
            tellerListModel.addElement(teller);
        }

        JTextField tellerNatIDField = new JTextField(15);
        JTextField tellerFirstNameField = new JTextField(15);
        JTextField tellerLastNameField = new JTextField(15);
        JTextField tellerDobField = new JTextField(15);
        JTextField tellerAddressField = new JTextField(15);
        JTextField tellerEmailField = new JTextField(15);
        JTextField tellerContactField = new JTextField(15);
        JTextField tellerIDField1 = new JTextField(15);
        JTextField tellerPassField1 = new JTextField(15);
        JButton addTellerButton = new JButton("Add Teller");
        JButton removeTellerButton = new JButton("Remove Teller");

        addTellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int natID = Integer.parseInt(tellerNatIDField.getText().trim());
                    int contact = Integer.parseInt(tellerContactField.getText().trim());
                    int tellerID = Integer.parseInt(tellerIDField1.getText().trim());
                    String email = tellerEmailField.getText().trim();
                    String password = tellerPassField1.getText().trim();
                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    bankTeller teller = new bankTeller(natID, tellerFirstNameField.getText().trim(), tellerLastNameField.getText().trim(),
                            tellerDobField.getText().trim(), tellerAddressField.getText().trim(), email,
                            contact, tellerID, password
                    );
                    adminController.addBankTeller(teller);
                    tellerListModel.addElement(teller);
                    JOptionPane.showMessageDialog(null, "Teller added", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear fields after successful addition
                    tellerNatIDField.setText("");
                    tellerFirstNameField.setText("");
                    tellerLastNameField.setText("");
                    tellerDobField.setText("");
                    tellerAddressField.setText("");
                    tellerEmailField.setText("");
                    tellerContactField.setText("");
                    tellerIDField1.setText("");
                    tellerPassField1.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input for ID or contact", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        removeTellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bankTeller selected = tellerList.getSelectedValue();
                if (selected != null) {
                    adminController.removeBankTeller(selected.getBankTellerID());
                    tellerListModel.removeElement(selected);
                    JOptionPane.showMessageDialog(null, "Teller removed", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No teller selected", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel tellerInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        tellerInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tellerInputPanel.add(new JLabel("National ID:"));
        tellerInputPanel.add(tellerNatIDField);
        tellerInputPanel.add(new JLabel("First Name:"));
        tellerInputPanel.add(tellerFirstNameField);
        tellerInputPanel.add(new JLabel("Last Name:"));
        tellerInputPanel.add(tellerLastNameField);
        tellerInputPanel.add(new JLabel("Date of Birth:"));
        tellerInputPanel.add(tellerDobField);
        tellerInputPanel.add(new JLabel("Address:"));
        tellerInputPanel.add(tellerAddressField);
        tellerInputPanel.add(new JLabel("Email:"));
        tellerInputPanel.add(tellerEmailField);
        tellerInputPanel.add(new JLabel("Contact:"));
        tellerInputPanel.add(tellerContactField);
        tellerInputPanel.add(new JLabel("Teller ID:"));
        tellerInputPanel.add(tellerIDField1);
        tellerInputPanel.add(new JLabel("Password:"));
        tellerInputPanel.add(tellerPassField1);
        tellerInputPanel.add(addTellerButton);
        tellerInputPanel.add(removeTellerButton);

        tellerPanel.add(new JScrollPane(tellerList), BorderLayout.CENTER);
        tellerPanel.add(tellerInputPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Manage Tellers", tellerPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}