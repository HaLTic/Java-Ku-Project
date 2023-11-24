import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JFormattedTextField.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.jdatepicker.impl.*;
import com.github.lgooddatepicker.components.*;

public class InitiatorPanel {
    private JPanel panel;
    public static String INITIATOR_NAME = "";
    public static boolean isRegistered;

    public InitiatorPanel(Main main, String initiator_name) {
    	INITIATOR_NAME = initiator_name;
        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        panel.add(new JLabel("Get Started as an Initiator by Creating an Initiative!"));

        JButton createInitiativeButton = new JButton("Create Initiative");
        panel.add(createInitiativeButton);
        JButton viewActiveButton = new JButton("View Active Initiatives");
        panel.add(viewActiveButton);
        JButton viewPendingButton = new JButton("View Pending Initiatives");
        panel.add(viewPendingButton);

        panel.add(new JLabel("")); // Empty label

        JButton backButton = new JButton("Back");
        panel.add(backButton);

        createInitiativeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		createInitiative(main);
            }
        });

        viewActiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isRegistered = true;
            	InitiativesPanel.initiativesList(main, InitiatorPanel.this, "", INITIATOR_NAME);
            }
        });

        viewPendingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	isRegistered = false;
            	InitiativesPanel.initiativesList(main, InitiatorPanel.this, "", INITIATOR_NAME);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		main.showPanel("User");
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    private String generateID() { // Generate a random 4-digit ID
        int id = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(id);
    }

    public static String checkStatus(String date, String time) { // Get the current date and time
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    
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
        try (BufferedReader reader1 = new BufferedReader(new FileReader("initiatives.txt"))) {
        	    for (int i = 0; i < 7; i++) {
        	        reader1.readLine();
        	    }
        	    while ((line = reader1.readLine()) != null) {
        	        if (line.equals(INITIATOR_NAME)) {
        	            counter++;
        	            if (counter > 1) {
        	                break;
        	            }
        	        }
        	        for (int i = 0; i < 10; i++) {
        	            reader1.readLine();
        	        }
        	    }
        	} catch (IOException ex) {
        	    ex.printStackTrace();
        	}

        
        if (counter >= 2) {
        	JOptionPane.showMessageDialog(panel, "You're not allowed to create a new initiative\nbecause you have 2 active initiatives.\n ", "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
	    	boolean allFieldsFilled = false;
	        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
	
	        JTextField nameField = new JTextField();
            UtilDateModel dateModel = new UtilDateModel();
            Properties p = new Properties();
            p.put("text.day", "Day");
            p.put("text.month", "Month");
            p.put("text.year", "Year");
            JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
            datePicker.getJFormattedTextField().setEditable(false);
            JTextField creditPointsField = new JTextField();
            JTextField descriptionField = new JTextField();
            JTextField initiatorNameField = new JTextField();

            JTextField timeField = new JTextField();
            timeField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    TimePicker timePicker = new TimePicker();
                    int option = JOptionPane.showConfirmDialog(panel, timePicker, "Select Time", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        LocalTime selectedTime = timePicker.getTime();
                        if (selectedTime != null) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                            String formattedTime = selectedTime.format(formatter);
                            timeField.setText(formattedTime);
                        }
                    }
                }
            });

            JTextField[] arrayField = {nameField, timeField, creditPointsField, descriptionField};
            initiatorNameField.setText(INITIATOR_NAME);
            initiatorNameField.setEditable(false);

            inputPanel.add(new JLabel("Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
	        inputPanel.add(datePicker);
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
                    if (datePicker.getJFormattedTextField().getText().isEmpty())
                        emptyCounter++;
                        
	                if (emptyCounter != 0) {
	                    JOptionPane.showMessageDialog(panel, "Please fill in all the fields.\n" + emptyCounter + " fields remaining", "Error!", JOptionPane.ERROR_MESSAGE);
	                } else {
	                    allFieldsFilled = true;		                
	                    String name = nameField.getText();
                        String date = datePicker.getJFormattedTextField().getText();
		                String time = timeField.getText();
		                String creditPoints = creditPointsField.getText();
		                String description = descriptionField.getText();
		                String initiatorName = initiatorNameField.getText();
		                String id = generateID();
		                String status = checkStatus(date, time);
		                int volunteers = 0;
		                String volunteerNames = "";
		                
	                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("pendingInitiatives.txt", true))) {
	                        InitiativesPanel.approvalInitiatives(volunteers, id, name, date, time, creditPoints, description, status, initiatorName, volunteerNames);
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

    public class DateLabelFormatter extends AbstractFormatter { // Create date picker
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
        }
    
    public static void initiativesOptions(Main main, InitiatorPanel initiatorPanel, String [] specificArray, StringBuilder initiatives, String searchQuery) {
        File file;
        Object[] options;
        if (isRegistered) {
            file = new File("initiatives.txt");
    	    options = new Object[]{"OK", "Remove", "Edit", "View Volunteers"};
        }
        else {
            file = new File("pendingInitiatives.txt");
            options = new Object[]{"OK", "Remove", "Edit"};
        }
    	JPanel panel2 = new JPanel();
        int choice = JOptionPane.showOptionDialog(panel2, initiatives.toString(), "Initiative Info",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
        
        switch (choice) {
        case 1: // If "Remove" was pressed
            int dialogResult = JOptionPane.showConfirmDialog(panel2, "Are you sure you want to remove the initiative?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                // Remove initiative here
                try {
                    File tempFile = new File("temp.txt");                        

                    BufferedReader reader = new BufferedReader(new FileReader(file));
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
                    
                    if (Main.renameFile(tempFile, file)) {
                        JOptionPane.showMessageDialog(panel2, "Initiative successfully removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        InitiativesPanel.initiativesList(main, initiatorPanel, searchQuery, INITIATOR_NAME); // Update the initiative list
                    } else {
                        JOptionPane.showMessageDialog(panel2, "Initiative failed to remove.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException ex) {
                	ex.printStackTrace();
                }
            }
            else
            	initiativesOptions(main, initiatorPanel, specificArray, initiatives, searchQuery);
            break;
        case 2: // If "Edit" was pressed
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
    	            				writer.write(fieldArray[6].getText() + "\n");
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
            	if (Main.renameFile(tempFile, inputFile)) { // Updating file
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

                    String[] updatedSpecificArray = new String[9];
                    for (int i = 0; i < fieldArray.length; i++) {
                    	updatedSpecificArray[i] = fieldArray[i].getText();
                    }
                    
                    initiativesOptions(main, initiatorPanel, updatedSpecificArray, updatedInitiatives, searchQuery); // Update the initiative
                } else {
                    JOptionPane.showMessageDialog(panel2, "Initiative failed to edit.", "Failed", JOptionPane.INFORMATION_MESSAGE);
                    initiativesOptions(main, initiatorPanel, specificArray, initiatives, searchQuery);
                }
            }
        	break;
        case 3: // If "View Volunteers" was pressed
        	StringBuilder volunteerList =  new StringBuilder();
        	String[] volunteerNames = specificArray[9].split(" ");
        	for (int i = 0; i < volunteerNames.length; i++)
        		volunteerList.append((i+1) + ". ").append(volunteerNames[i]).append("\n");

        	if (volunteerList.length() <= 4) {
        		JOptionPane.showMessageDialog(panel2, "No registered volunteer found.", "Error", JOptionPane.ERROR_MESSAGE);
        	} else
        		JOptionPane.showMessageDialog(panel2, volunteerList.toString(), "Volunteers Info", JOptionPane.PLAIN_MESSAGE);
        	initiativesOptions(main, initiatorPanel, specificArray, initiatives, searchQuery);
        	break;
        default:
            InitiativesPanel.initiativesList(main, initiatorPanel, searchQuery, INITIATOR_NAME);
        	break;
        }
    }

}