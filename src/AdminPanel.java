import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class AdminPanel {
    private JPanel panel;

    public AdminPanel(Main main) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(10, 3));
        
        panel.add(new JLabel(""));

        JButton usersButton = new JButton("View Users");
        panel.add(usersButton);
        JButton viewInitiativesButton = new JButton("View Initiatives");
        panel.add(viewInitiativesButton);
        JButton approveInitiativesButton = new JButton("Approve Pending Initiatives");
        panel.add(approveInitiativesButton);

        for (int i = 0; i < 2; i++) { // Adds multiple empty labels
            panel.add(new JLabel(""));
        }

        JButton signoutButton = new JButton("Sign out");
        panel.add(signoutButton);

        usersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateUsersList(main, "");
            }
        });

        viewInitiativesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	initiativesList(main, "");
            }
        });
        
        approveInitiativesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	adminApprovalList(main, "");
            }
        });

        signoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                main.showPanel("Main");
            }
        });
    }

    public JScrollPane getPanel() {
        JScrollPane scrollPane = new JScrollPane(panel,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private void updateUsersList(Main main, String searchQuery) { // Method to update the user list in the admin panel
	    panel.removeAll(); // Remove existing components
	    panel.setLayout(new BorderLayout()); // Use BorderLayout
	
	    JLabel title = new JLabel("Users List");
	    title.setFont(new Font("Arial", Font.PLAIN, 24));
	    panel.add(title);
	
	    // Create a panel for the search bar
	    JPanel searchBarPanel = new JPanel();
	    searchBarPanel.setLayout(new FlowLayout()); // Use FlowLayout for the search bar panel
	
	    // Create the search bar component
	    JTextField searchBar = new JTextField(20);
	    JButton searchButton = new JButton("Search");
	    searchBarPanel.add(searchBar);
	    searchBarPanel.add(searchButton);
	
	    panel.add(searchBarPanel, BorderLayout.NORTH);
	
	    searchButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String searchText = searchBar.getText(); // Get the text from the search bar
	            boolean queryFound = searchQueryInUsers(searchText); // Search the user in the file
	
	            // Check if the user was not found
	            if (!queryFound)
	                // Display error message
	                JOptionPane.showMessageDialog(panel, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
	            else
	                updateUsersList(main, searchText);
	        }
	    });
	
	    try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        String line;
	        boolean isEmpty = true;
	
	        while ((line = reader.readLine()) != null) {
	            isEmpty = false;
	            String[] userInfo = line.split("\t");
	            String username = userInfo[0];
	            String name = userInfo[1];
	
	            if (username.toLowerCase().contains(searchQuery.toLowerCase()) || name.toLowerCase().contains(searchQuery.toLowerCase())) {
	                Box userBox = Box.createHorizontalBox();
	
	                JLabel usernameLabel = new JLabel("Username: " + username);
	                JLabel nameLabel = new JLabel("Name: " + name);
	
	                Box nameBox = Box.createVerticalBox();
	                nameBox.add(usernameLabel);
	                nameBox.add(nameLabel);
	
	                userBox.add(nameBox);
	                userBox.add(Box.createHorizontalStrut(10)); // Add horizontal spacing
	
	                JButton infoButton = new JButton("Info");
	                userBox.add(infoButton);
	
	                infoButton.addActionListener(new ActionListener() {
	                    public void actionPerformed(ActionEvent e) {
	                        // Display user info here
	                        JTextField[] arrayField = new JTextField[8];
	                        for (int i = 0; i < arrayField.length; i++) {
	                            arrayField[i] = new JTextField(userInfo[i]);
	                            arrayField[i].setEditable(false);
	                        }
	
	                        JPanel editPanel = new JPanel(new GridLayout(9, 2));
	                        editPanel.add(new JLabel("User ID:"));
	                        editPanel.add(arrayField[0]);
	                        editPanel.add(new JLabel("Name:"));
	                        editPanel.add(arrayField[1]);
	                        editPanel.add(new JLabel("DOB:"));
	                        editPanel.add(arrayField[2]);
	                        editPanel.add(new JLabel("Email:"));
	                        editPanel.add(arrayField[3]);
	                        editPanel.add(new JLabel("Phone:"));
	                        editPanel.add(arrayField[4]);
	                        editPanel.add(new JLabel("City:"));
	                        editPanel.add(arrayField[5]);
	                        editPanel.add(new JLabel("Hours complete:"));
	                        editPanel.add(arrayField[6]);
	                        editPanel.add(new JLabel("Password:"));
	                        editPanel.add(arrayField[7]);
	
	                        adminUserOptions(main, arrayField, editPanel);
	                    }
	                });
	
	                panel.add(userBox);
	                panel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
	            }
	        }
	
	        if (isEmpty) {
	            JOptionPane.showMessageDialog(panel, "No users found.", "Warning", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (IOException error) {
	        error.printStackTrace();
	    }
	
	    JButton backButton = new JButton("Back");
	    panel.add(backButton);
	
	    backButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            main.showPanel("Admin"); // Back to Admin page
	        }
	    });
	
	    // Revalidate and repaint the panel
	    panel.revalidate();
	    panel.repaint();
	}

    private void adminApprovalList(Main main, String searchQuery) {
    	panel.removeAll(); // Remove existing components
        panel.setLayout(new BorderLayout()); // Use BorderLayout

        JLabel title = new JLabel("Pending Initiatives List");
        title.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(title, BorderLayout.NORTH);

        // Create a panel for the search bar
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout()); // Use FlowLayout for the search bar panel

        // Create the search bar component
        JTextField searchBar = new JTextField(20); // Adjust the size as needed
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchBar);
        searchBarPanel.add(searchButton);

        panel.add(searchBarPanel, BorderLayout.NORTH);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText(); // Get the text from the search bar
                boolean queryFound = searchQueryInPending(searchText); // Search the initiative in the file

                // Check if the initiative was not found
                if (!queryFound)
                    // Display error message
                    JOptionPane.showMessageDialog(panel, "Pending initiative not found", "Error", JOptionPane.ERROR_MESSAGE);
                else
                	adminApprovalList(main, searchText);
            }
        });

        // Create a new panel for the initiatives
        JPanel initiativesPanel = new JPanel();
        initiativesPanel.setLayout(new BoxLayout(initiativesPanel, BoxLayout.Y_AXIS));

        // Add the initiativesPanel to the main panel before reading the file
        panel.add(initiativesPanel, BorderLayout.CENTER);

        try {
            File file = new File("pendingInitiatives.txt");

            if (file.length() == 0) {
                JOptionPane.showMessageDialog(panel, "No pending initiatives found.", "Warning", JOptionPane.INFORMATION_MESSAGE);  
                main.showPanel("Admin");
            }
            BufferedReader reader = new BufferedReader(new FileReader("pendingInitiatives.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
            	String id = line;
            	String name = reader.readLine();
            	if (name.toLowerCase().contains(searchQuery.toLowerCase())) {
	        		String date = reader.readLine().trim();
	                String time = reader.readLine().trim();
	                String creditPoints = reader.readLine();
	                String description = reader.readLine();
	                String status = InitiatorPanel.checkStatus(date, time);
	                reader.readLine();
	                String initiatorName = reader.readLine();
	                int volunteers = Integer.parseInt(reader.readLine());
	                String volunteerNames = reader.readLine().trim();
	                reader.readLine(); // Skips the seperator line
	
	                Box titleBox = Box.createHorizontalBox();
	                titleBox.add(new JLabel("Name: " + name));
	                titleBox.add(Box.createHorizontalStrut(10)); // Add some horizontal spacing
	
	                JButton infoButton = new JButton("Info");
	                titleBox.add(infoButton);
	
	                infoButton.addActionListener(new ActionListener() {
	                	public void actionPerformed(ActionEvent e) {
	                		String [] specificArray = {id, name, date, time, creditPoints, description, status, initiatorName, Integer.toString(volunteers), volunteerNames};
	                        try {
	                            StringBuilder initiatives = new StringBuilder();
	
	                            initiatives.append("ID: ").append(specificArray[0]).append("\n");
	                            initiatives.append("Name: ").append(specificArray[1]).append("\n");
	                            initiatives.append("Date: ").append(specificArray[2]).append("\n");
	                            initiatives.append("Time: ").append(specificArray[3]).append("\n");
	                            initiatives.append("Credit Points: ").append(specificArray[4]).append("\n");
	                            initiatives.append("Description: ").append(specificArray[5]).append("\n");
	                            initiatives.append("Status: ").append(specificArray[6]).append("\n");
	                            initiatives.append("Initiator Name: ").append(specificArray[7]).append("\n");
	                            initiatives.append("Volunteers: ").append(specificArray[8]).append("\n");
	                            initiatives.append("\n");
	                            
	                            adminApprovalOptions(main, specificArray, initiatives, searchQuery);
	                            
	                        } catch (NullPointerException ex) {
	                        	ex.printStackTrace();
	                        }
	                    }
	                });
	                
	                initiativesPanel.add(titleBox);
	            } else {
	            	for (int i = 0; i < 9; i++) {
            			reader.readLine();
            		}
            		continue;
	            }
	        }
            reader.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
        panel.add(initiativesPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.showPanel("Admin");
            }
        });

        panel.revalidate(); // Revalidate the panel to update the layout
        panel.repaint(); // Repaint the panel to reflect the changes
    	}

    private boolean searchQueryInUsers(String searchText) { // Search for users in users.txt file
	    try {
	        File file = new File("users.txt");
	        Scanner scanner = new Scanner(file);
	
	        while (scanner.hasNextLine()) {
	            String[] line = scanner.nextLine().split("\t");
	            if (line[0].toLowerCase().contains(searchText.toLowerCase()) || line[1].toLowerCase().contains(searchText.toLowerCase())) {
	                scanner.close();
	                return true; // Query found
	            }
	        }
	        scanner.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    if (searchText.equals(""))
	        return true;
	
	    return false; // Query not found
	}

    public static boolean searchQueryInInitiatives(String searchText) { // Search for initiatives in initiatives.txt file
        try {
            File file = new File("initiatives.txt");
            Scanner scanner = new Scanner(file);
            
            String line = "";
            while (scanner.hasNextLine()) {
            	scanner.nextLine();
                line = scanner.nextLine();
                if (line.toLowerCase().contains(searchText.toLowerCase())) {
                    scanner.close();
                    return true; // Query found
                }
                for (int i = 0; i < 9; i++) {
                	if (!scanner.hasNextLine()) {
                		scanner.close();
                		return false;
                	}
                	line = scanner.nextLine();
        		}
                
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (searchText.equals(""))
            return true;

        return false; // Query not found
    }

    public static boolean searchQueryInPending(String searchText) { // Search for initiatives in initiatives.txt file
        try {
            File file = new File("pendingInitiatives.txt");
            Scanner scanner = new Scanner(file);
            
            String line = "";
            while (scanner.hasNextLine()) {
            	scanner.nextLine();
                line = scanner.nextLine();
                if (line.toLowerCase().contains(searchText.toLowerCase())) {
                    scanner.close();
                    return true; // Query found
                }
                for (int i = 0; i < 9; i++) {
                	if (!scanner.hasNextLine()) {
                		scanner.close();
                		return false;
                	}
                	line = scanner.nextLine();
        		}
                
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (searchText.equals(""))
            return true;

        return false; // Query not found
    }

    private void adminUserOptions(Main main, JTextField[] arrayField, JPanel editPanel) { // Admin user options
    	Object[] options = {"OK", "Remove"};
    	JPanel panel2 = new JPanel();
        int choice = JOptionPane.showOptionDialog(panel2, editPanel, "User Info",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[1]);

        if (choice == 1) { // If "Remove" is clicked
            int dialogResult = JOptionPane.showConfirmDialog(panel, "Are you sure you want to remove this user?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) { // If admin confirms deletion of user
                // Remove user here
                try {
                    File inputFile = new File("users.txt");
                    File tempFile = new File("temp.txt");

                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                    String lineToRemove = ""; // The line that contains the user's information
                    for (JTextField field : arrayField) {
                    	lineToRemove += field.getText();
                    }
                    
                    String currentLine;
                    while((currentLine = reader.readLine()) != null) {
                        // trim newline when comparing with lineToRemove
                        
                        String [] trimmedLineArray = currentLine.split("\t");
                        String trimmedLine = "";
                        for (String word : trimmedLineArray) {
                            trimmedLine += word;
                        }
                        if(trimmedLine.equals(lineToRemove))
                            continue; // Skips this line to be removed
                        writer.write(currentLine + "\n");
                    }
                    writer.close(); 
                    reader.close();
                    
                    if (tempFile.renameTo(inputFile)) {// Updating file
                        JOptionPane.showMessageDialog(panel, "User successfully removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        updateUsersList(main, ""); // Update the user list
                    } else {
                        JOptionPane.showMessageDialog(panel, "User failed to remove.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        }
    }

    private void adminApprovalOptions(Main main, String [] specificArray, StringBuilder initiatives, String searchQuery) { // Admin initiative approval options
    	Object[] options = {"OK", "Remove", "Approve"};
    	JPanel panel2 = new JPanel();
        int choice = JOptionPane.showOptionDialog(panel2, initiatives.toString(), "Initiative Info",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        switch(choice) {
            case 1:
            	int dialogResult = JOptionPane.showConfirmDialog(panel, "Are you sure you want to remove the pending initiative?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    // Remove user here
                    try {
                        File inputFile = new File("pendingInitiatives.txt");
                        File tempFile = new File("temp.txt");                        

                        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                        
                        String lineToRemove = specificArray[0]; // The line that contains the user's information

                        String currentLine;
                        while((currentLine = reader.readLine()) != null) {
                            // If the current line is the line to remove, skip the next lines
                        	if(currentLine.equals(lineToRemove)) {
                                for (int i = 0; i < 11; i++) {
                                    currentLine = reader.readLine();
                                }
                            } else {
                                writer.write(currentLine + "\n");
                            }
                        }
                        writer.close(); 
                        reader.close();
                        
                        if (tempFile.renameTo(inputFile)) {// Updating file
                            JOptionPane.showMessageDialog(panel, "Pending initiative successfully removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            adminApprovalList(main, searchQuery); // Update the user list
                        } else {
                            JOptionPane.showMessageDialog(panel, "Pending initiative failed to remove.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            	break;
            case 2:
            	dialogResult = JOptionPane.showConfirmDialog(panel, "Are you sure you want to approve the pending initiative?", "Confirm Approval", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    // Remove user here
                    try {
                    	InitiativesPanel.approvalInitiatives(specificArray[0]);
                        JOptionPane.showMessageDialog(panel, "Pending initiative successfully approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        adminApprovalList(main, searchQuery); // Update the user list
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    
                }
            	break;
            default:
            	break;
        }
    }

    public void initiativesList(Main main, String searchQuery) {
    	panel.removeAll(); // Remove existing components
        panel.setLayout(new BorderLayout()); // Use BorderLayout

        JLabel title = new JLabel("Initiatives List");
        title.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(title, BorderLayout.NORTH);

        // Create a panel for the search bar
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout()); // Use FlowLayout for the search bar panel

        // Create the search bar component
        JTextField searchBar = new JTextField(20); // Adjust the size as needed
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchBar);
        searchBarPanel.add(searchButton);

        panel.add(searchBarPanel, BorderLayout.NORTH);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText(); // Get the text from the search bar
                boolean queryFound = searchQueryInInitiatives(searchText); // Search the initiative in the file

                // Check if the initiative was not found
                if (!queryFound)
                    // Display error message
                    JOptionPane.showMessageDialog(panel, "Initiative not found", "Error", JOptionPane.ERROR_MESSAGE);
                else
                    initiativesList(main, searchText);
            }
        });

        // Create a new panel for the initiatives
        JPanel initiativesPanel = new JPanel();
        initiativesPanel.setLayout(new BoxLayout(initiativesPanel, BoxLayout.Y_AXIS));

        // Add the initiativesPanel to the main panel before reading the file
        panel.add(initiativesPanel, BorderLayout.CENTER);

        try {
            File file = new File("initiatives.txt");

            if (file.length() == 0) {
                JOptionPane.showMessageDialog(panel, "No initiatives found.", "Warning", JOptionPane.INFORMATION_MESSAGE);  
                main.showPanel("Initiator", UserPanel.name);
            }
            BufferedReader reader = new BufferedReader(new FileReader("initiatives.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
            	String id = line;
            	String name = reader.readLine();
            	if (name.toLowerCase().contains(searchQuery.toLowerCase())) {
	        		String date = reader.readLine().trim();
	                String time = reader.readLine().trim();
	                String creditPoints = reader.readLine();
	                String description = reader.readLine();
	                String status = InitiatorPanel.checkStatus(date, time);
	                reader.readLine();
	                String initiatorName = reader.readLine();
	                String volunteers = reader.readLine();
	                String volunteerNames = reader.readLine().trim();
	                reader.readLine(); // Skips the seperator line

	                	Box titleBox = Box.createHorizontalBox();
		                titleBox.add(new JLabel("Name: " + name));
		                titleBox.add(Box.createHorizontalStrut(10)); // Add some horizontal spacing
		
		                JButton infoButton = new JButton("Info");
		                titleBox.add(infoButton);
		
		                infoButton.addActionListener(new ActionListener() {
		                	public void actionPerformed(ActionEvent e) {
		                		String [] specificArray = {id, name, date, time, creditPoints, description, status, initiatorName, volunteers, volunteerNames};
		                        try {
		                            StringBuilder initiatives = new StringBuilder();
		
		                            initiatives.append("ID: ").append(specificArray[0]).append("\n");
		                            initiatives.append("Name: ").append(specificArray[1]).append("\n");
		                            initiatives.append("Date: ").append(specificArray[2]).append("\n");
		                            initiatives.append("Time: ").append(specificArray[3]).append("\n");
		                            initiatives.append("Credit Points: ").append(specificArray[4]).append("\n");
		                            initiatives.append("Description: ").append(specificArray[5]).append("\n");
		                            initiatives.append("Status: ").append(specificArray[6]).append("\n");
		                            initiatives.append("Initiator Name: ").append(specificArray[7]).append("\n");
		                            initiatives.append("Volunteers: ").append(specificArray[8]).append("\n");
		                            initiatives.append("\n");
		                            
		                            initiativesOptions(main, specificArray, initiatives, searchQuery);
		                            
		                        } catch (NullPointerException ex) {
		                        	ex.printStackTrace();
		                        }
		                    }
		                });
		                
		                initiativesPanel.add(titleBox);
	                } else {
		            	for (int i = 0; i < 9; i++) {
	            			reader.readLine();
	            		}
	            		continue;
		            }
	        }
            reader.close();
            
        } catch (IOException error) {
            error.printStackTrace();
        }
        panel.add(initiativesPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        panel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.showPanel("Initiator", UserPanel.name);
            }
        });

        panel.revalidate(); // Revalidate the panel to update the layout
        panel.repaint(); // Repaint the panel to reflect the changes
    }
    
    public void initiativesOptions(Main main, String [] specificArray, StringBuilder initiatives, String searchQuery) {
    	Object[] options = {"OK", "Remove", "View Volunteers"};
    	JPanel panel2 = new JPanel();
        int choice = JOptionPane.showOptionDialog(panel2, initiatives.toString(), "Initiative Info",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[1]);
        switch (choice) {
        case 1: // If "Remove" was pressed
            int dialogResult = JOptionPane.showConfirmDialog(panel2, "Are you sure you want to remove the initiative?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                // Remove initiative here
                try {
                    File inputFile = new File("initiatives.txt");
                    File tempFile = new File("temp.txt");                        

                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                    
                    String lineToRemove = specificArray[0]; // The line that contains the initiative's information
                
                    String currentLine;
                    while((currentLine = reader.readLine()) != null) {
                        // If the current line is the line to remove, skip the next lines
                    	if(currentLine.equals(lineToRemove)) {
                            for (int i = 0; i < 10; i++) {
                                currentLine = reader.readLine();
                            }
                        } else {
                            writer.write(currentLine + "\n");
                        }
                    }

                    writer.close(); 
                    reader.close();
                    
                    if (tempFile.renameTo(inputFile)) { // Updating file
                        JOptionPane.showMessageDialog(panel, "Initiative successfully removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        initiativesList(main, searchQuery); // Update the initiative list
                    } else {
                        JOptionPane.showMessageDialog(panel, "Initiative failed to remove.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException ex) {
                	ex.printStackTrace();
                }
            }
            else
            	initiativesOptions(main, specificArray, initiatives, searchQuery);
            break;
        case 2: // If "View Volunteers" was pressed
        	StringBuilder volunteerList =  new StringBuilder();
        	String[] volunteerNames = specificArray[9].split(" ");
        	for (int i = 0; i < volunteerNames.length; i++)
        		volunteerList.append((i+1) + ". ").append(volunteerNames[i]).append("\n");

        	if (volunteerList.length() <= 4) {
        		JOptionPane.showMessageDialog(panel2, "No registered volunteer found.", "Error", JOptionPane.ERROR_MESSAGE);
        	} else
        		JOptionPane.showMessageDialog(panel2, volunteerList.toString(), "Volunteers Info", JOptionPane.PLAIN_MESSAGE);
        	initiativesOptions(main, specificArray, initiatives, searchQuery);
        	break;
        default:
        	break;
        }
    }

}

	
