import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class InitiativesPanel {
	private static JPanel panel;

    public InitiativesPanel(Main main) {
    	// Do nothing (Doesn't need to be displayed)
    }

    public JPanel getPanel() {
        return panel;
    }
    
    // Display the initiatives list for user panel
    public static void initiativesList(Main main, UserPanel userPanel, String searchQuery) {
        panel = userPanel.getPanel();
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
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText(); // Get the text from the search bar
                boolean queryFound = searchQueryInInitiatives(searchText); // Search the initiative in the file

                // Check if the initiative was not found
                if (!queryFound)
                    // Display error message
                    JOptionPane.showMessageDialog(panel, "Initiative not found", "Error", JOptionPane.ERROR_MESSAGE);
                else
                    initiativesList(main, userPanel, searchText);
            }
        });

        // Create a new panel for the initiatives
        JPanel initiativesPanel = new JPanel();
        
		File file = new File("initiatives.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
			initiativesPanel.setLayout(new BoxLayout(initiativesPanel, BoxLayout.Y_AXIS));

            if (file.length() == 0) {
                JOptionPane.showMessageDialog(panel, "No initiatives found.", "Warning", JOptionPane.INFORMATION_MESSAGE);  
                main.showPanel("User");
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
	                String volunteers = reader.readLine();
	                String volunteerNames = reader.readLine().trim();
	                reader.readLine(); // Skips the seperator line
	
	                Box titleBox = Box.createHorizontalBox();
	                titleBox.add(new JLabel("Name: " + name));
	                titleBox.add(Box.createHorizontalStrut(10)); // Add some horizontal spacing
	
	                JButton infoButton = new JButton("Info");
	                titleBox.add(infoButton);
	
	                infoButton.addActionListener(new ActionListener() {
                        @Override
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
	                            
	                            UserPanel.initiativesUserOptions(main, userPanel, specificArray, initiatives, searchQuery);
	                            
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
            @Override
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

    // Display the initiatives list for initiator panel
    public static void initiativesList(Main main, InitiatorPanel initiatorPanel, String searchQuery, String initiator_name) {
        int initiativesCounter = 0;
        panel = initiatorPanel.getPanel();
    	panel.removeAll(); // Remove existing components
        panel.setLayout(new BorderLayout()); // Use BorderLayout
        File file;
        JLabel title;
        if (AdminPanel.isRegistered) {
            file = new File("initiatives.txt");
            title = new JLabel("Initiatives List");
        }
        else {
            file = new File("pendingInitiatives.txt");
            title = new JLabel("Pending Initiatives List");
        }
        title.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(title, BorderLayout.NORTH);

        // Create a panel for the search bar
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout()); // Use FlowLayout for the search bar panel

        // Create the search bar component
        JTextField searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchBarPanel.add(searchBar);
        searchBarPanel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText(); // Get the text from the search bar
                boolean queryFound = searchQueryInInitiatives(searchText, initiator_name, InitiatorPanel.isRegistered); // Search the initiative in the file

                // Check if the initiative was not found
                if (!queryFound)
                    // Display error message
                    JOptionPane.showMessageDialog(panel, "Initiative not found", "Error", JOptionPane.ERROR_MESSAGE);
                else
                    initiativesList(main, initiatorPanel, searchText, initiator_name);
            }
        });

        // Create a new panel for the initiatives
        JPanel initiativesPanel = new JPanel();
        if (file.length() == 0) {
            JOptionPane.showMessageDialog(panel, "No initiatives found.", "Warning", JOptionPane.INFORMATION_MESSAGE);  
            main.showPanel("Initiator", UserPanel.name);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            initiativesPanel.setLayout(new BoxLayout(initiativesPanel, BoxLayout.Y_AXIS));
            
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
                
	                if (initiator_name.equals(initiatorName)) {
	                	initiativesCounter++;
	                	Box titleBox = Box.createHorizontalBox();
		                titleBox.add(new JLabel("Name: " + name));
		                titleBox.add(Box.createHorizontalStrut(10)); // Add some horizontal spacing
		
		                JButton infoButton = new JButton("Info");
		                titleBox.add(infoButton);
		
		                infoButton.addActionListener(new ActionListener() {
                            @Override
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
		                            
		                            InitiatorPanel.initiativesOptions(main, initiatorPanel, specificArray, initiatives, searchQuery);
		                            
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
        // Create the initiatives panel and add it to a JScrollPane
        JScrollPane initiativesScrollPane = new JScrollPane(initiativesPanel);
        initiativesScrollPane.setPreferredSize(new Dimension(Main.width/2, Main.height*3/4)); // Set preferred size

		// Create the back button component
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(panel.getWidth(), 30)); // Set preferred size
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	main.showPanel("Initiator", UserPanel.name);
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

    // Display the initiatives list for admin panel
    public static void initiativesList(Main main, AdminPanel adminPanel, String searchQuery) {
        panel = adminPanel.getPanel();
    	panel.removeAll(); // Remove existing components
        panel.setLayout(new BorderLayout()); // Use BorderLayout
        File file;
        JLabel title;
        if (AdminPanel.isRegistered) {
            file = new File("initiatives.txt");
            title = new JLabel("Initiatives List");
        }
        else {
            file = new File("pendingInitiatives.txt");
            title = new JLabel("Pending Initiatives List");
        }
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
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchBar.getText(); // Get the text from the search bar
                boolean queryFound = searchQueryInInitiatives(searchText, AdminPanel.isRegistered); // Search the initiative in the file

                // Check if the initiative was not found
                if (!queryFound)
                    // Display error message
                    JOptionPane.showMessageDialog(panel, "Initiative not found", "Error", JOptionPane.ERROR_MESSAGE);
                else
                    initiativesList(main, adminPanel, searchText);
            }
        });

        // Create a new panel for the initiatives
        JPanel initiativesPanel = new JPanel();
        if (file.length() == 0) {
                JOptionPane.showMessageDialog(panel, "No initiatives found.", "Warning", JOptionPane.INFORMATION_MESSAGE);  
                main.showPanel("Admin");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            initiativesPanel.setLayout(new BoxLayout(initiativesPanel, BoxLayout.Y_AXIS));
            
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
                            @Override
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
		                            
		                            AdminPanel.initiativesOptions(main, adminPanel, specificArray, initiatives, searchQuery);
		                            
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
        
        // Create the user panel and add it to a JScrollPane
        JScrollPane initiativesScrollPane = new JScrollPane(initiativesPanel);
        initiativesScrollPane.setPreferredSize(new Dimension(Main.width/2, Main.height*3/4)); // Set preferred size

        // Create the back button component
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(panel.getWidth(), 30)); // Set preferred size
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	main.showPanel("Admin");
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
    
    // Append the initiative information to the "pendingInitiatives.txt" file when initiative is created
    public static void approvalInitiatives(String ... fields) {
        // Save the initiative information to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pendingInitiatives.txt", true))) {
            writer.write(fields[0] + "\n" + fields[1] + "\n" + fields[2] + "\n" + fields[3] + "\n" + fields[4] + "\n" + fields[5] + "\n"+ fields[6] + "\n" + fields[7] + "\n0\n");
            writer.write("\n------------------\n");
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Transfer the pending initiative information from the "pendingInitiatives.txt" to the "initiatives.txt" file
    public static void approvalInitiatives(String id) throws IOException {
        // Append approved initiative to existing initiatives
        BufferedWriter writer = new BufferedWriter(new FileWriter("initiatives.txt", true));
        BufferedReader reader = new BufferedReader(new FileReader("pendingInitiatives.txt"));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.equals(id)) { // Use equals() method to compare strings
                for (int i = 0; i < 11; i++) {
                    writer.write(line + "\n");
                    line = reader.readLine();
                }
            } else {
                continue;
            }
        }

        reader.close();
        writer.close();

        File pendingInitiatives = new File("pendingInitiatives.txt");
        File pendingInitiativesTemp = new File("pendingInitiativesTemp.txt");

        reader = new BufferedReader(new FileReader(pendingInitiatives));
        writer = new BufferedWriter(new FileWriter(pendingInitiativesTemp));
        
        while ((line = reader.readLine()) != null) {
            if (line.equals(id)) { // Use equals() method to compare strings
                for (int i = 0; i < 11; i++) {
                    line = reader.readLine();
                }
            }
            if (line != null)
            	writer.write(line + "\n");
        }
        reader.close();
        writer.close();
        
        Main.renameFile(pendingInitiativesTemp, pendingInitiatives);
    }

    // Search for initiatives in initiatives.txt file for user panel
    public static boolean searchQueryInInitiatives(String searchText) {
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

    // Search for initiatives in initiatives.txt file for initiator panel
    public static boolean searchQueryInInitiatives(String searchText, String initiatorName, boolean isRegistered) {
        try {
            File file;
            if (isRegistered) {
                file = new File("initiatives.txt");
            }
            else
                file = new File("pendingInitiatives.txt");
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

    // Search for initiatives in initiatives.txt file for admin panel
    public static boolean searchQueryInInitiatives(String searchText, boolean isRegistered) {
        try {
            File file;
            if (isRegistered)
                file = new File("initiatives.txt");
            else
                file = new File("pendingInitiatives.txt");
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

}
