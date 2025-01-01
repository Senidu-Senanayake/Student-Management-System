/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.studentmanagementsystemproject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.*;
/**
 *
 * @author ASUS
 */
public class LoginForm extends JFrame {

    // Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        
        initComponents();
        
    }
    
    private void initComponents(){
    
        // Frame settings
        setTitle("Student Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        // Left Panel (Logo and Text)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(44, 62, 80));
        leftPanel.setPreferredSize(new Dimension(300, 500));
        leftPanel.setLayout(new GridBagLayout());
        
        // Load and resize the logo image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/education.png"));
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel lblIcon = new JLabel(resizedIcon);
        JLabel lblTitle = new JLabel("Student Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        leftPanel.add(lblIcon, gbc);

        gbc.gridy = 1;
        leftPanel.add(lblTitle, gbc);
        
        // Right Panel (Login Form)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);
        
        
        // Load and resize the user icon
        ImageIcon userIcon = new ImageIcon(getClass().getResource("/images/user.png"));
        Image userImage = userIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedUserIcon = new ImageIcon(userImage);

        JLabel lblUserIcon = new JLabel(resizedUserIcon);
        lblUserIcon.setBounds(160, 50, 100, 100);
        

        JLabel lblWelcome = new JLabel("Welcome back!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 22));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setBounds(100, 150, 250, 30);

        txtUsername = new JTextField();
        txtUsername.setBounds(100, 200, 250, 35);
        txtUsername.setToolTipText("Enter Username");

        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 250, 250, 35);
        txtPassword.setToolTipText("Enter Password");

        btnLogin = new JButton("Login");
        btnLogin.setBounds(100, 310, 250, 40);
        btnLogin.setBackground(new Color(52, 73, 94));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));

        // Add components to the right panel
        rightPanel.add(lblUserIcon);
        rightPanel.add(lblWelcome);
        rightPanel.add(txtUsername);
        rightPanel.add(txtPassword);
        rightPanel.add(btnLogin);
        

        // Use JSplitPane to divide the left and right panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300); // Fixed width for left panel
        splitPane.setDividerSize(0); // Remove the divider
        splitPane.setBorder(null); // Remove borders

        // Add the split pane to the frame
        add(splitPane, BorderLayout.CENTER);

        // Event Handling
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction(e);
            }
        });
    }

    private void loginAction(ActionEvent evt) {
         String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (validateLogin(username, password)) {
            
                    dispose();  // Close the login window
                    HomeUI homeUI = new HomeUI();
                    homeUI.setVisible(true);
            // Proceed to next window or dashboard
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials");
        }
    }
      private boolean validateLogin(String username, String password) {
        String dbUrl = "jdbc:mysql://localhost:3306/StudentManagementSystemDB";
        String dbUser = "root"; // Replace with your MySQL username
        String dbPassword = "200369"; // Replace with your MySQL password

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM tbLogin WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true; // Login successful
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Login failed
    }

    /**
     * @param args the command line arguments
     */
}

