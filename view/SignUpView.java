package view;

import controller.SignUpController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpView {
    public void show() {
        JFrame frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new GridLayout(10, 2, 10, 10));

        JTextField natIDField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField custIDField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton signUpButton = new JButton("Sign Up");
        SignUpController controller = new SignUpController();
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int nationalID = Integer.parseInt(natIDField.getText());
                    int contact = Integer.parseInt(contactField.getText());
                    String password = new String(passField.getPassword());
                    if (password.length() < 8) {
                        JOptionPane.showMessageDialog(null, "Password must be at least 8 characters", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Required fields cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    controller.signUpCustomer(nationalID, firstNameField.getText(), lastNameField.getText(),
                            dobField.getText(), addressField.getText(), emailField.getText(), contact,
                            custIDField.getText(), password);
                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input for ID or contact", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(new JLabel("National ID:"));
        frame.add(natIDField);
        frame.add(new JLabel("First Name:"));
        frame.add(firstNameField);
        frame.add(new JLabel("Last Name:"));
        frame.add(lastNameField);
        frame.add(new JLabel("Date of Birth:"));
        frame.add(dobField);
        frame.add(new JLabel("Address:"));
        frame.add(addressField);
        frame.add(new JLabel("Email:"));
        frame.add(emailField);
        frame.add(new JLabel("Contact:"));
        frame.add(contactField);
        frame.add(new JLabel("Customer ID:"));
        frame.add(custIDField);
        frame.add(new JLabel("Password:"));
        frame.add(passField);
        frame.add(new JLabel(""));
        frame.add(signUpButton);

        frame.setVisible(true);
    }
}