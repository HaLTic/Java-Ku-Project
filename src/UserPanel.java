import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class UserPanel {
    private JPanel panel;
    private boolean isNew = true;
    static String temp_name = "";
    static String name;
    
    public UserPanel(Main main) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        String userID = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                userID = fields[0]; // First element is the userID
                temp_name = fields[1];
                
                if (userID.equals(MainPanel.userID)) {
                	name = temp_name;
                	panel.add(new JLabel("Welcome back, " + name));
                	isNew = false;
                    break;
                }
            }
            if (isNew)
            	panel.add(new JLabel("Welcome, " + temp_name));
        } catch (IOException ex) {
            ex.printStackTrace(); // print error
        }
        JButton initiativesButton = new JButton("List of Initiatives");
        JButton initiatorButton = new JButton("Become an Initiator!");
        JButton editPersonalInfoButton = new JButton("Edit Personal Info");
        JButton printVolunteeringHistoryButton = new JButton("Print Volunteering History");
        panel.add(initiativesButton);
        panel.add(initiatorButton);
        panel.add(editPersonalInfoButton);
        panel.add(printVolunteeringHistoryButton);
        		
        panel.add(new JLabel("")); // Adds empty label

        JButton signoutButton = new JButton("Sign out");
        panel.add(signoutButton);

        initiativesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	initiativesList(main, "");
            }
        });

        initiatorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                main.showPanel("Initiator", name);
            }
        });

        editPersonalInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editPersonalInfo(main);
            }
        });
        
        printVolunteeringHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	printVolunteerHistory(main);
            }
        });

        signoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                main.showPanel("Main");
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    private void editPersonalInfo(Main main) {
        String filePath = "users.txt";
        String tempFile = "temp.txt";
        String userID = MainPanel.userID;
        String password = MainPanel.password;

        BufferedReader reader = null;
        BufferedWriter writer = null;
        
        try {
            reader = new BufferedReader(new FileReader(filePath));
            writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                String existingUserID = fields[0];
                String existingPassword = fields[fields.length - 1];

                if (existingUserID.equals(userID) && existingPassword.equals(password)) {
                    // Display a dialog to get the updated personal information from the user
                	JTextField userIDField = new JTextField(fields[0]);
                    JTextField nameField = new JTextField(fields[1]);
                    JTextField DOBField = new JTextField(fields[2]);
                    JTextField emailField = new JTextField(fields[3]);
                    JTextField phoneField = new JTextField(fields[4]);
                    JTextField cityField = new JTextField(fields[5]);
                    JTextField hoursField = new JTextField(fields[6]);
                    hoursField.setEditable(false);
                    JTextField passwordField = new JTextField(fields[fields.length - 1]);

                    JPanel editPanel = new JPanel(new GridLayout(8, 2));
                    editPanel.add(new JLabel("User ID:"));
                    editPanel.add(userIDField);
                    editPanel.add(new JLabel("Name:"));
                    editPanel.add(nameField);
                    editPanel.add(new JLabel("DOB:"));
                    editPanel.add(DOBField);
                    editPanel.add(new JLabel("Email:"));
                    editPanel.add(emailField);
                    editPanel.add(new JLabel("Phone:"));
                    editPanel.add(phoneField);
                    editPanel.add(new JLabel("City:"));
                    editPanel.add(cityField);
                    editPanel.add(new JLabel("Hours complete:"));
                    editPanel.add(hoursField);
                    editPanel.add(new JLabel("Password:"));
                    editPanel.add(passwordField);

                    int result = JOptionPane.showConfirmDialog(panel, editPanel, "Edit Personal Info",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        // Update the line with the new personal information
                        String updatedLine = userIDField.getText() + "\t" + nameField.getText() + "\t" +
                                DOBField.getText() + "\t" + emailField.getText() + "\t" +
                                phoneField.getText() + "\t" + cityField.getText() + "\t" +
                                hoursField.getText() + "\t" + passwordField.getText();
                        writer.write(updatedLine);
                        JOptionPane.showMessageDialog(panel, "Information has been updated!\nPlease login to continue", "Success", JOptionPane.INFORMATION_MESSAGE);
                        main.showPanel("Main"); // Goes back to main panel
                        
                    } else {
                        // Keep the existing line
                        writer.write(line);
                    }
                } else {
                    // Keep the existing line
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Rename the temporary file to the original file
        File originalFile = new File(filePath);
        File tempFileObject = new File(tempFile);
        tempFileObject.renameTo(originalFile);
    }

    public void initiativesList(Main main, String searchQuery) {
    	panel.removeAll(); // Remove existing components
        panel.setLayout(new BorderLayout()); // Use BorderLayout

        JLabel title = new JLabel("Initiatives List");
        title.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JLabel("Help Save the Earth by Volunteering!"));

        // Create a panel for the search bar
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout()); // Use FlowLayout for the search bar panel

        // Create the search bar component
        JTextField searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchBar);
        searchBarPanel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText(); // Get the text from the search bar
                boolean queryFound = AdminPanel.searchQueryInInitiatives(searchText); // Search the initiative in the file

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
        
		File file = new File("initiatives.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
			initiativesPanel.setLayout(new BoxLayout(initiativesPanel, BoxLayout.Y_AXIS));

            if (file.length() == 0) {
                JOptionPane.showMessageDialog(panel, "No initiatives found.", "Warning", JOptionPane.INFORMATION_MESSAGE);  
                main.showPanel("Initiator", name);
            }
            
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
	                            
	                            initiativesUserOptions(main, specificArray, initiatives, searchQuery);
	                            
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
        } catch (IOException error) {
            error.printStackTrace();
        }
        // Create the initiatives panel and add it to a JScrollPane
        JScrollPane initiativesScrollPane = new JScrollPane(initiativesPanel);
        initiativesScrollPane.setPreferredSize(new Dimension(Main.width/2, Main.height*3/4)); // Set preferred size

		// Create the back button component
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(panel.getWidth(), 30)); // Set preferred size
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.showPanel("User");
            }
        });

		// Create a new JPanel for the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Add the search bar panel, user panel, and back button panel to the main panel
        mainPanel.add(searchBarPanel);
        mainPanel.add(initiativesScrollPane);
        mainPanel.add(backButtonPanel);

        // Add the main panel to the panel
        panel.add(mainPanel);

        panel.revalidate(); // Revalidate the panel to update the layout
        panel.repaint(); // Repaint the panel to reflect the changes
    }

    public void initiativesUserOptions(Main main, String [] specificArray, StringBuilder initiatives, String searchQuery) {
    	Object[] options;
    	int choice = 0;
    	JPanel panel2 = new JPanel();
    	
    	if (specificArray[6].equals("Active")) {
    		boolean isRegistered = false;
    		for (String registeredName : specificArray[9].split(" ")) {
    			if (name.equals(registeredName)) {
    				isRegistered = true;
    				break;
    			}
    		}
    		if (isRegistered) {
    		     options = new Object[]{"OK", "Withdraw"};
    		 } else {
    		     options = new Object[]{"OK", "Register"};
    		 }
    		choice = JOptionPane.showOptionDialog(panel2, initiatives.toString(), "Initiative Info",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[1]);
    	}
    	else {
    		options = new Object[]{"OK"};
    		choice = JOptionPane.showOptionDialog(panel2, initiatives.toString(), "Initiative Info",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
    	}
        
        switch (choice) {
	        case 1: // If "Register"/"Withdraw" was pressed
	        	String volunteerID = specificArray[0];
                int creditHours = Integer.parseInt(specificArray[4]);
                String volunteers = specificArray[8];
                String volunteerNamesLine = specificArray[9] + " "; // The line that contains the initiative's information
	        	if (options[1].equals("Register")) {
		            int dialogResult = JOptionPane.showConfirmDialog(panel2, "Confirm registration to this initiative?", "Confirm Registration", JOptionPane.YES_NO_OPTION);
		            if (dialogResult == JOptionPane.YES_OPTION) {
		                // Register volunteer here
		                try {
		                	File inputFile = new File("initiatives.txt");
		                    File tempFile = new File("temp.txt"); 
		                    File inputFile2 = new File("users.txt");
		                    File tempFile2 = new File("temp2.txt"); 
		
		                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		                    BufferedReader reader2 = new BufferedReader(new FileReader(inputFile2));
		                    BufferedWriter writer2 = new BufferedWriter(new FileWriter(tempFile2));
		                
		                    String currentLine;
		                    while((currentLine = reader.readLine()) != null) {
		                        // If the current line contains the number of volunteers, append both the counter and the list
		                    	if(currentLine.equals(volunteerID)) {
		                    		while (!currentLine.equals(volunteers)) {
		                    			writer.write(currentLine + "\n");
		                    			currentLine = reader.readLine();
		                    		}
		                    			
		                    		int volunteerCount = Integer.parseInt(volunteers);
		                    		volunteerCount++;
		                    		currentLine = Integer.toString(volunteerCount);
		                    		writer.write(currentLine + "\n");
		                    		if (volunteerNamesLine.equals(" "))
		                    			volunteerNamesLine = name;
		                    		else
		                    			volunteerNamesLine += name + " ";
		                            currentLine = volunteerNamesLine;
		                            writer.write(currentLine + "\n");
		                            reader.readLine();
		                        } else {
		                        	writer.write(currentLine + "\n");
		                        }
		                    }
		                    while((currentLine = reader2.readLine()) != null) {
		                        // If the current line contains the users info, appends the volunteer hours
	                    		String [] line = currentLine.split("\t");
	                    		String [] updatedLine = new String[8];
	                    		String id = line[0];
	                    		for (int i = 0; i < line.length; i++) {
	                    			if(line[i].equals(line[6]) && id.equals(MainPanel.userID)) {
	                    				int updatedHours = Integer.parseInt(line[i]) + creditHours;
	                    				updatedLine[i] = Integer.toString(updatedHours);
	                    			}
	                    			else
	                    				updatedLine[i] = line[i];
	                    		}
	                    		for (int i = 0; i < updatedLine.length; i++) {
	                    			if (i == updatedLine.length - 1)
	                    				writer2.write((updatedLine[i]));
	                    			else
	                    				writer2.write((updatedLine[i] + "\t"));
	                    		}
	                    		writer2.newLine();
		                    }
		
		                    writer.close(); 
		                    reader.close();
		                    writer2.close();
		                    reader2.close();
		                    
		                    if (tempFile.renameTo(inputFile) && tempFile2.renameTo(inputFile2)) { // Updating both file
		                        JOptionPane.showMessageDialog(panel2, "Initiative successfully registered.", "Success", JOptionPane.INFORMATION_MESSAGE);
		                        initiativesList(main, searchQuery); // Update the initiative list
		                    } else {
		                        JOptionPane.showMessageDialog(panel2, "Initiative failed to register.", "Failed", JOptionPane.INFORMATION_MESSAGE);
		                    }
		                } catch (IOException ex) {
		                    ex.printStackTrace();
		                } catch (NullPointerException ex) {
		                	ex.printStackTrace();
		                }
		            } else {
		            	initiativesUserOptions(main, specificArray, initiatives, searchQuery);
		            }
	        	} else {
	        		int dialogResult = JOptionPane.showConfirmDialog(panel2, "Confirm withdrawal to this initiative?", "Confirm Withdrawal", JOptionPane.YES_NO_OPTION);
		            if (dialogResult == JOptionPane.YES_OPTION) {
		                // Withdraw volunteer here
		                try {
		                	File inputFile = new File("initiatives.txt");
		                    File tempFile = new File("temp.txt"); 
		                    File inputFile2 = new File("users.txt");
		                    File tempFile2 = new File("temp2.txt"); 
		
		                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		                    BufferedReader reader2 = new BufferedReader(new FileReader(inputFile2));
		                    BufferedWriter writer2 = new BufferedWriter(new FileWriter(tempFile2));
		                
		                    String currentLine;
		                    while((currentLine = reader.readLine()) != null) {
		                        // If the current line contains the number of volunteers, subtracts both the counter and the list
		                    	if(currentLine.equals(volunteerID)) {
		                    		while (!currentLine.equals(volunteers)) {
		                    			writer.write(currentLine + "\n");
		                    			currentLine = reader.readLine();
		                    		}
		                    			
		                    		int volunteerCount = Integer.parseInt(volunteers);
		                    		volunteerCount--;
		                    		currentLine = Integer.toString(volunteerCount);
									writer.write(currentLine + "\n");
		                    		String[] names = volunteerNamesLine.split(" ");
									StringBuilder sb = new StringBuilder();
									for (String specificName : names) {
										if (!specificName.equals(name)) {
											sb.append(specificName);
											sb.append(" ");
										}
									}
									volunteerNamesLine = sb.toString();
		                            currentLine = volunteerNamesLine;
		                            writer.write(currentLine + "\n");
									reader.readLine();
		                        } else {
		                        	writer.write(currentLine + "\n");
		                        }
		                    }
		                    while((currentLine = reader2.readLine()) != null) {
		                        // If the current line contains the users info, subtracts the volunteer hours
	                    		String [] line = currentLine.split("\t");
	                    		String [] updatedLine = new String[8];
	                    		String id = line[0];
	                    		for (int i = 0; i < line.length; i++) {
	                    			if(line[i].equals(line[6]) && id.equals(MainPanel.userID)) {
	                    				int updatedHours = Integer.parseInt(line[i]) - creditHours;
	                    				updatedLine[i] = Integer.toString(updatedHours);
	                    			}
	                    			else
	                    				updatedLine[i] = line[i];
	                    			
	                    			if (i == line.length - 1)
	                    				writer2.write((updatedLine[i]));
	                    			else
	                    				writer2.write((updatedLine[i] + "\t"));
	                    		}
	                    		writer2.newLine();
		                    }
		                    
		
		                    writer.close(); 
		                    reader.close();
		                    writer2.close();
		                    reader2.close();		                    
		                    if (tempFile.renameTo(inputFile) && tempFile2.renameTo(inputFile2)) {// Updating file
		                        JOptionPane.showMessageDialog(panel2, "Initiative successfully withdrawn.", "Success", JOptionPane.INFORMATION_MESSAGE);
		                        initiativesList(main, searchQuery); // Update the initiative list
		                    } else {
		                        JOptionPane.showMessageDialog(panel2, "Initiative failed to withdraw.", "Failed", JOptionPane.INFORMATION_MESSAGE);
		                    }
		                } catch (IOException ex) {
		                    ex.printStackTrace();
		                } catch (NullPointerException ex) {
		                	ex.printStackTrace();
		                }
		            } else {
		            	initiativesUserOptions(main, specificArray, initiatives, searchQuery);
		            }
	        	}
	            break;
	            
	        default:
	        	break;
	        	
        }
    }

    public void printVolunteerHistory(Main main) {
    	int dialogResult = JOptionPane.showConfirmDialog(panel, "Would you like to print out your volunteering history?", "Confirm Printing", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            // Print user volunteer history here
        	File userFile = new File("users.txt");
        	File initiativesFile = new File("initiatives.txt");
            File generatedFile = new File(name + "'s Volunteering History Report (" + MainPanel.userID + ").txt");
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile));
            	 BufferedReader reader2 = new BufferedReader(new FileReader(initiativesFile));
            	 BufferedWriter writer = new BufferedWriter(new FileWriter(generatedFile));
            	)
            {
                String currentLine;
                while((currentLine = reader.readLine()) != null) {
                	String [] line = currentLine.split("\t");
                	if (line[0].equals(MainPanel.userID)) {
                		writer.write("Volunteering History for: " + name + " (" + MainPanel.userID + ")\n");
                		writer.write("Total volunteering hours: " + line[6] + " Credit Hours\n\n");
                		writer.write("Initiatives Participated:\n\n");
                		
                		String [] initiativeDetails = new String[10];
                		int initiativesCounter = 0;
                		while((currentLine = reader2.readLine()) != null) {
                			for (int i = 0; i < initiativeDetails.length - 1; i++) {
                				initiativeDetails[i] = currentLine;
                				currentLine = reader2.readLine();
                			}
                			initiativeDetails[initiativeDetails.length - 1] = currentLine;
                			String [] volunteerNames = initiativeDetails[9].split(" ");
                			
                			for (String i : volunteerNames) {
	                			if (i.equals(name)) {
	                				initiativesCounter++;
	                				writer.write(initiativesCounter + ".\nID: " + initiativeDetails[0]
	                												+ "\nTitle: " + initiativeDetails[1]
	                												+ "\nDate: " + initiativeDetails[2]
	                												+ "\nTime: " + initiativeDetails[3]
	                												+ "\nCredit Hours: " + initiativeDetails[4] 
	                												+ "\nInitiator Name: " + initiativeDetails[7]
	                												+ "\n\n");
	                				break;
	                			}
                			}
                			currentLine = reader2.readLine();
                		}
						if (initiativesCounter == 0) {
							writer.write("No initiative participations were found.\n\n");
						}
						writer.write("Regards,\nKhalifa University of Science and Technology");
                	}
                	
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
            	ex.printStackTrace();
            }
			JOptionPane.showMessageDialog(panel, "Volunteering history has been successfully printed.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
