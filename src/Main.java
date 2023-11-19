import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main {
    private JFrame frame;
    public static String PanelName;

    public Main() {
        frame = new JFrame("Volunteer for the Earth");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        // Start with the main panel
        showPanel("Main");
    }
    
    public void showPanel() {
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
    }
    
    public void showPanel(String panelName) { // All panels except for Initiator panel)
        Component component = null;
        PanelName = panelName;
        
        if (panelName.equals("Admin")) {
            AdminPanel adminPanel = new AdminPanel(this);
            JScrollPane scrollPane = adminPanel.getPanel();
            component = scrollPane;
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
            JScrollPane scrollPane = initiativesPanel.getPanel();
            component = scrollPane;
        }
        
        frame.getContentPane().removeAll();
        frame.add(component);
        frame.revalidate();
        frame.repaint();
    }

    public void showPanel(String panelName, String name) { // Initiator Panel
        Component component = null;
        PanelName = panelName;
        
        if (panelName.equals("Initiator")) {
            InitiatorPanel initiatorPanel = new InitiatorPanel(this, name);
            JScrollPane scrollPane = initiatorPanel.getPanel();
            component = scrollPane;
        }
        frame.getContentPane().removeAll();
        frame.add(component);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Main();
    }
}
