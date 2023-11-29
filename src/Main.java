import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main extends JFrame {
    public static String PanelName;
    public static final String osName = System.getProperty("os.name").toLowerCase();
    static final int width = 400;
    static final int height = 600;

    public Main() {
        new JFrame("Volunteer for the Earth");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);

        // Start with the main panel
        showPanel("Main");
    }
    
    public void showPanel(String panelName) { // All panels except for Initiator panel)
        Component component = null;
        PanelName = panelName;
        
        if (panelName.equals("Admin")) {
            AdminPanel adminPanel = new AdminPanel(this);
            JPanel panel = adminPanel.getPanel();
            component = panel;
        } else if (panelName.equals("User")) {
            UserPanel userPanel = new UserPanel(this);
            JPanel panel = userPanel.getPanel();
            component = panel;
        } else if (panelName.equals("Sign up")) {
            SignupPanel signupPanel = new SignupPanel(this);
            JPanel panel = signupPanel.getPanel();
            component = panel;
        } else if (panelName.equals("Main")) {
            MainPanel mainPanel = new MainPanel(this);
            JPanel panel = mainPanel.getPanel();
            component = panel;
        } else if (panelName.equals("Initiatives")) {
            InitiativesPanel initiativesPanel = new InitiativesPanel(this);
            JPanel panel = initiativesPanel.getPanel();
            component = panel;
        }
        
        getContentPane().removeAll();
        add(component);
        revalidate();
        repaint();
    }

    public void showPanel(String panelName, String name) { // Initiator Panel
        Component component = null;
        PanelName = panelName;
        
        if (panelName.equals("Initiator")) {
            InitiatorPanel initiatorPanel = new InitiatorPanel(this, name);
            JPanel panel = initiatorPanel.getPanel();
            component = panel;
        }
        getContentPane().removeAll();
        add(component);
        revalidate();
        repaint();
    }

    public static boolean renameFile(File tempFile, File targetFile) {
        boolean isRenamed = false;
    
        if (osName.contains("win")) { // If the OS is Windows
            if (targetFile.delete()) {
                isRenamed = tempFile.renameTo(targetFile);
            }
        } else { // For other OS (like macOS)
            isRenamed = tempFile.renameTo(targetFile);
        }
    
        return isRenamed;
    }
    public static void main(String[] args) throws FileNotFoundException {
        new Main();
    }
}
