import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

public class SignupPanel {
    private JPanel panel;
    private int ID;

    public SignupPanel(Main main) {
        panel = new JPanel();
        JTextField nameInput, DOBInput, emailInput, phoneInput, addressInput, passwordInput;
        
        panel.setLayout(new GridLayout(18, 2));
        JLabel title = new JLabel("Sign Up");
        title.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(title);

        nameInput = new JTextField();
        panel.add(new JLabel("First name:"));
        panel.add(nameInput);

        DOBInput = new JTextField();
        panel.add(new JLabel("Date of Birth (YYYY/MM/DD):"));
        panel.add(DOBInput);

        emailInput = new JTextField();
        panel.add(new JLabel("Email:"));
        panel.add(emailInput);

        phoneInput = new JTextField();
        panel.add(new JLabel("Phone:"));
        panel.add(phoneInput);

        addressInput = new JTextField();
        panel.add(new JLabel("Address:"));
        panel.add(addressInput);

        passwordInput = new JTextField();
        panel.add(new JLabel("Password:"));
        panel.add(passwordInput);

        JButton signupButton = new JButton("Sign Up");
        panel.add(signupButton);

        JButton backButton = new JButton("Back");
        panel.add(backButton);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameInput.getText();
                String dob = DOBInput.getText();
                String email = emailInput.getText();
                String phone = phoneInput.getText();
                String address = addressInput.getText();
                String password = passwordInput.getText(); 
                
                // Validate the input fields
                if (name.isEmpty() || dob.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please fill in all the fields.", "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Save the user information to a file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
                        Random random = new Random();
                        ID = random.nextInt(9000) + 1000;
                        writer.write(ID + "\t" + name + "\t" + dob + "\t" + email + "\t" + phone + "\t" + address + "\t" + 0 + "\t" + password); // Adds a new user to user.txt file
                        writer.newLine();
                        writer.flush();
                        MainPanel.userID = Integer.toString(ID); // Set the userID to the generated ID
                        MainPanel.password = password;
                        
                        JOptionPane.showMessageDialog(panel, "Sign up successful!\nYour user ID is: " + ID + "\n", "Success", JOptionPane.INFORMATION_MESSAGE);
                        main.showPanel("User"); // Proceeds to the user panel
                    } catch (IOException ex) {
                        ex.printStackTrace(); // print error
                    }
                }
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.showPanel("Main");
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}
