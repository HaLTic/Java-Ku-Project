import java.io.*;
import javax.swing.*;

public class InitiativesPanel {
	private JPanel panel;

    public InitiativesPanel(Main main) {
    	
    }

    public JScrollPane getPanel() {
        JScrollPane scrollPane = new JScrollPane(panel,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
    
    public static void approvalInitiatives(String id, String name, String date, String time, String creditPoints, String description, String status, String initiatorName, int volunteers, String volunteerNames) {
        // Save the initiative information to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pendingInitiatives.txt", true))) {
            writer.write(id + "\n" + name + "\n" + date + "\n" + time + "\n" + creditPoints + "\n" + description + "\n"+ status + "\n" + initiatorName + "\n" + volunteers + "\n" + volunteerNames);
            writer.write("\n------------------\n");
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void approvalInitiatives(String id) throws IOException {
        // Append approved initiative to existing initiatives
        BufferedWriter writer = new BufferedWriter(new FileWriter("initiatives.txt", true));
        BufferedReader reader = new BufferedReader(new FileReader("pendingInitiatives.txt"));
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
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
        
        pendingInitiativesTemp.renameTo(pendingInitiatives);
    }


}
