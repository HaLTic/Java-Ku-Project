import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class MainPanel {
    private JPanel panel;
    public static String userID;
    public static String password;

    public MainPanel(Main main) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2));

        JLabel title = new JLabel("Welcome to Volunteer for the Earth!");
        title.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(title);

        JTextField userIDField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        panel.add(new JLabel("User ID:"));
        panel.add(userIDField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("")); // Empty label

        JButton loginButton = new JButton("Login"); // Login button
        // Set the login button as the default button
        main.getRootPane().setDefaultButton(loginButton);
        panel.add(loginButton);

        JButton signupButton = new JButton("Sign Up"); // Sign up button
        panel.add(signupButton);
        
        for (int i = 0; i < 3; i++) { // Adds multiple empty lables
            panel.add(new JLabel(""));
        }
        
        JButton quitButton = new JButton("Exit"); // Exit button
        panel.add(quitButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passwordInput = String.valueOf(passwordField.getPassword());

                if (passwordInput.equals(AdminInfo.getPassword()) && userIDField.getText().equals(AdminInfo.getID())) {
                    main.showPanel("Admin");
                } else if ((passwordInput.equals("")) || (userIDField.getText().equals(""))) {
                    JOptionPane.showMessageDialog(panel, "Please enter your user ID and password.", "Error!", 
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean hasAccount = false;
                    userID = "";
                    password = "";
                    try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] fields = line.split("\t");
                            userID = fields[0]; // First element is the username
                            password = fields[fields.length - 1]; // Last element is the password

                            if (userIDField.getText().equals(userID) && passwordInput.equals(password)) {
                                hasAccount = true;
                                break;
                            }
                        }

                        if (hasAccount) {
                            main.showPanel("User");
                        } else {
                            JOptionPane.showMessageDialog(panel, "Incorrect user ID or password.", "Error!", 
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.showPanel("Sign up");
            }
        });
        
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}

