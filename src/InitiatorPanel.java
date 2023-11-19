import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.Date;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InitiatorPanel {
    private JPanel panel;
    private String initiatorName = "";
    private int initiativesCounter = 0;

    public InitiatorPanel(Main main, String initiatorName) {
    	this.initiatorName = initiatorName;
        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        panel.add(new JLabel("Get Started as an Initiator by Creating an Initiative!"));

        JButton createInitiativeButton = new JButton("Create Initiative");
        panel.add(createInitiativeButton);
        JButton viewInitiativesButton = new JButton("View Created Initiatives");
        panel.add(viewInitiativesButton);

        panel.add(new JLabel("")); // Empty label

        JButton backButton = new JButton("Back");
        panel.add(backButton);

        createInitiativeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		createInitiative(main);
            }
        });

        viewInitiativesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	initiativesList(main, "");
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		main.showPanel("User");
            }
        });
    }

    public JScrollPane getPanel() {
        JScrollPane scrollPane = new JScrollPane(panel,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private String generateID() {
        // Generate a random 4-digit ID
        int id = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(id);
    }

    public static String checkStatus(String date, String time) {
        // Get the current date and time
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            Date today = dateFormat.parse(dateFormat.format(currentDate));
            Date currentTime = timeFormat.parse(timeFormat.format(currentDate));

            // Parse the initiative date and time
            Date initiativeDate = dateFormat.parse(date);
            Date initiativeTime = timeFormat.parse(time);

            if (initiativeDate.before(today) || (initiativeDate.equals(today) && initiativeTime.before(currentTime))) {
                return "Expired";
            } else {
                return "Active";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "???";
        }
    }

    private void createInitiative(Main main) {
    	int counter = 0;
        String line = "";
        
        // Reads from both initiatives.txt and pendingInitiatives.txt files.
        // Appends counter to limit initiator from having more than 2 active initiatives.
        try (BufferedReader reader1 = new BufferedReader(new FileReader("initiatives.txt"));
        	     BufferedReader reader2 = new BufferedReader(new FileReader("pendingInitiatives.txt"))) {
        	    for (int i = 0; i < 7; i++) {
        	        reader1.readLine();
        	        reader2.readLine();
        	    }
        	    while ((line = reader1.readLine()) != null) {
        	        if (line.equals(initiatorName)) {
        	            counter++;
        	            if (counter > 1) {
        	                break;
        	            }
        	        }
        	        for (int i = 0; i < 10; i++) {
        	            reader1.readLine();
        	        }
        	    }
        	    while ((line = reader2.readLine()) != null) {
        	        if (line.equals(initiatorName)) {
        	            counter++;
        	            if (counter > 1) {
        	                break;
        	            }
        	        }
        	        for (int i = 0; i < 10; i++) {
        	            reader2.readLine();
        	        }
        	    }
        	} catch (IOException ex) {
        	    ex.printStackTrace();
        	}

        
        if (counter > 1) {
        	JOptionPane.showMessageDialog(panel, "You're not allowed to create a new initiative\nbecause you have 2 active initiatives.\n ", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
	    	boolean allFieldsFilled = false;
	        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
	
	        JTextField nameField = new JTextField();
	        JTextField dateField = new JTextField();
	        JTextField timeField = new JTextField();
	        JTextField creditPointsField = new JTextField();
	        JTextField descriptionField = new JTextField();
	        JTextField initiatorNameField = new JTextField(initiatorName);
	        JTextField [] arrayField = {nameField, dateField, timeField, creditPointsField, descriptionField};
	        initiatorNameField.setEditable(false);
	
	        inputPanel.add(new JLabel("Name:"));
	        inputPanel.add(nameField);
	        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
	        inputPanel.add(dateField);
	        inputPanel.add(new JLabel("Time (HH:MM):"));
	        inputPanel.add(timeField);
	        inputPanel.add(new JLabel("Credit Points:"));
	        inputPanel.add(creditPointsField);
	        inputPanel.add(new JLabel("Description:"));
	        inputPanel.add(descriptionField);
	        inputPanel.add(new JLabel("Initiator Name:"));
	        inputPanel.add(initiatorNameField);
	        
	        while (!allFieldsFilled) {
	            int result = JOptionPane.showConfirmDialog(panel, inputPanel, "Create Initiative", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	            if (result == JOptionPane.OK_OPTION) {
	                int emptyCounter = 0;
	                for (JTextField field : arrayField) {
	                    if (field.getText().isEmpty()) {
	                        emptyCounter++;
	                    }
	                }
	                if (emptyCounter != 0) {
	                    JOptionPane.showMessageDialog(panel, "Please fill in all the fields.\n" + emptyCounter + " fields remaining", "Error!", JOptionPane.ERROR_MESSAGE);
	                } else {
	                    allFieldsFilled = true;		                
	                    String name = initiatorName;
		                String date = dateField.getText();
		                String time = timeField.getText();
		                String creditPoints = creditPointsField.getText();
		                String description = descriptionField.getText();
		                String initiatorName = initiatorNameField.getText();
		                String id = generateID();
		                String status = checkStatus(date, time);
		                int volunteers = 0;
		                String volunteerNames = ""; 
		                
		                
	                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("pendingInitiatives.txt", true))) {
	                        InitiativesPanel.approvalInitiatives(id, name, date, time, creditPoints, description, status, initiatorName, volunteers, volunteerNames);
	                        JOptionPane.showMessageDialog(panel, "Initiative created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	                    } catch (IOException ex) {
	                        ex.printStackTrace();
	                    }
		                
	        		}
	        	} else {
	        		break;
	        	}
	        }
        }
    }

    public static boolean searchQueryInInitiatives(String searchText, String initiatorName) { // Search for initiatives in initiatives.txt file
        try {
            File file = new File("initiatives.txt");
            Scanner scanner = new Scanner(file);
            
            String line = "";
            while (scanner.hasNextLine()) {
            	scanner.nextLine();
                line = scanner.nextLine();
                if (line.toLowerCase().contains(searchText.toLowerCase())) {
                	for (int i = 0; i < 9; i++) {
                    	if (!scanner.hasNextLine()) {
                    		scanner.close();
                    		return false;
                    	}
                    	if (line.equals(initiatorName)) {
    	                    scanner.close();
    	                    return true; // Query found
                    	} else {
                    		line = scanner.nextLine();
                    	}
            		}
                }
                else {
	                for (int i = 0; i < 9; i++) {
	                	if (!scanner.hasNextLine()) {
	                		scanner.close();
	                		return false;
	                	}
	                	line = scanner.nextLine();
	        		}
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
                boolean queryFound = searchQueryInInitiatives(searchText, initiatorName); // Search the initiative in the file

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
	
	                if (initiatorName.equals(this.initiatorName)) {
	                	initiativesCounter++;
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
	                }
	                
	            } else {
	            	for (int i = 0; i < 9; i++) {
            			reader.readLine();
            		}
            		continue;
	            }
	        }
            reader.close();
            
            switch(initiativesCounter) {
            	case 0:
            		JOptionPane.showMessageDialog(panel, "No initiatives have been created.", "Created Initiatives", JOptionPane.ERROR_MESSAGE);
            		main.showPanel("Initiator", UserPanel.name);
            		break;
            	default:
            		break;
            }
            
            
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
    	Object[] options = {"OK", "Remove", "View Volunteers", "Edit"};
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
        case 3: // If "Edit" was pressed
        	JTextField IDField = new JTextField(specificArray[0]);
            JTextField nameField = new JTextField(specificArray[1]);
            JTextField dateField = new JTextField(specificArray[2]);
            JTextField timeField = new JTextField(specificArray[3]);
            JTextField creditField = new JTextField(specificArray[4]);
            JTextField descriptionField = new JTextField(specificArray[5]);
            JTextField statusField = new JTextField(specificArray[6]);
            statusField.setEditable(false);
            JTextField initiatorField = new JTextField(specificArray[7]);
            initiatorField.setEditable(false);
            
            JTextField [] fieldArray = {IDField, nameField, dateField, timeField, creditField, descriptionField, statusField, initiatorField};

            JPanel editPanel = new JPanel(new GridLayout(8, 2));
            editPanel.add(new JLabel("ID:"));
            editPanel.add(IDField);
            editPanel.add(new JLabel("Name:"));
            editPanel.add(nameField);
            editPanel.add(new JLabel("Date:"));
            editPanel.add(dateField);
            editPanel.add(new JLabel("Time:"));
            editPanel.add(timeField);
            editPanel.add(new JLabel("Credit:"));
            editPanel.add(creditField);
            editPanel.add(new JLabel("Description:"));
            editPanel.add(descriptionField);
            editPanel.add(new JLabel("Status:"));
            editPanel.add(statusField);
            editPanel.add(new JLabel("Initiator:"));
            editPanel.add(initiatorField);

            int result = JOptionPane.showConfirmDialog(panel2, editPanel, "Edit Personal Info",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Update the initiative information
            	
            	File inputFile = new File("initiatives.txt");
                File tempFile = new File("temp.txt");
                
            	try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));) {

    	            String line;
    	            while ((line = reader.readLine()) != null) {
    	            	
    	            	if (line.equals(fieldArray[0].getText())) {
    	            		for (JTextField i : fieldArray) {
    	            			if (i.getText().equals(fieldArray[6].getText())) {
    	            				fieldArray[6].setText(checkStatus(fieldArray[2].getText(), fieldArray[3].getText()));
    	            				writer.write(fieldArray[6] + "\n");
    	            			}
    	            			else
    	            				writer.write(i.getText() + "\n");
	    	            		line = reader.readLine();
	    	            	}
    	            		for (int i = 0; i < 2; i++) {
    	            			writer.write(line + "\n");
    	            			line = reader.readLine();
    	            		}
    	            		writer.write(line + "\n");
    	            	
    	            	} else {
    	            		writer.write(line + "\n");
    	            	}
    	            	
    	            }
            	} catch (IOException e) {
            	
            	}
            	if (tempFile.renameTo(inputFile)) { // Updating file
                    JOptionPane.showMessageDialog(panel2, "Initiative successfully edited.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    StringBuilder updatedInitiatives = new StringBuilder();
            		
                    updatedInitiatives.append("ID: ").append(fieldArray[0].getText()).append("\n");
                    updatedInitiatives.append("Name: ").append(fieldArray[1].getText()).append("\n");
                    updatedInitiatives.append("Date: ").append(fieldArray[2].getText()).append("\n");
                    updatedInitiatives.append("Time: ").append(fieldArray[3].getText()).append("\n");
                    updatedInitiatives.append("Credit Points: ").append(fieldArray[4].getText()).append("\n");
                    updatedInitiatives.append("Description: ").append(fieldArray[5].getText()).append("\n");
                    updatedInitiatives.append("Status: ").append(fieldArray[6].getText()).append("\n");
                    updatedInitiatives.append("Initiator Name: ").append(specificArray[7]).append("\n");
                    updatedInitiatives.append("Volunteers: ").append(specificArray[8]).append("\n");
                    updatedInitiatives.append("\n");
                    
                    initiativesOptions(main, specificArray, updatedInitiatives, searchQuery); // Update the initiative
                } else {
                    JOptionPane.showMessageDialog(panel2, "Initiative failed to edit.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                    initiativesOptions(main, specificArray, initiatives, searchQuery);
                }
            }
            initiativesOptions(main, specificArray, initiatives, searchQuery);
        	break;
        default:
        	break;
        }
    }

}