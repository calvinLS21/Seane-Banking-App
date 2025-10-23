package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen {
    public void show() {
        System.out.println(" [DEBUG] WelcomeScreen.show() STARTED");

        JFrame frame = new JFrame("Seane Banking App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(new Color(52, 152, 219));

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(52, 152, 219));
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("Welcome to", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appNameLabel = new JLabel("SEANE BANKING APP", SwingConstants.CENTER);
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 48));
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton continueButton = new JButton("Continue to Login");
        continueButton.setPreferredSize(new Dimension(200, 50));
        continueButton.setFont(new Font("Arial", Font.BOLD, 16));
        continueButton.setBackground(Color.WHITE);
        continueButton.setForeground(new Color(52, 152, 219));
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ULTRA-SIMPLE: NO SwingUtilities - DIRECT CALL
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(" [DEBUG] Continue button CLICKED");
                frame.dispose();  // Close welcome
                System.out.println(" [DEBUG] Welcome frame CLOSED");

                try {
                    //  DIRECT CALL - NO THREADING
                    System.out.println(" [DEBUG] Creating LoginView...");
                    LoginView loginView = new LoginView();
                    System.out.println(" [DEBUG] LoginView CREATED");

                    loginView.show();
                    System.out.println(" [DEBUG] LoginView.show() CALLED - SUCCESS!");
                } catch (Exception ex) {
                    System.out.println(" [ERROR] LoginView FAILED: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(titleLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        welcomePanel.add(appNameLabel);
        welcomePanel.add(Box.createVerticalGlue());
        welcomePanel.add(continueButton);

        frame.add(welcomePanel, BorderLayout.CENTER);
        frame.setVisible(true);
        System.out.println(" [DEBUG] WelcomeScreen COMPLETELY LOADED");
    }
}