package presentation;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

public class InformationPage {
    
    public final JPanel panel;

    public InformationPage() {
        panel = new JPanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            createAndShowInformationGUI();
          }
        } );
      }

    private static void createAndShowInformationGUI() {
    // Create a JFrame and set its content pane to a panel
    JFrame infoFrame = new JFrame("Information page");
    infoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create a JPanel and set its layout manager to BoxLayout
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));

    // Create some buttons and add them to the infoPanel
    JButton playButton = new JButton("PLAY MEDIA");
    JButton backButton = new JButton("GO BACK");
    
    infoPanel.add(Box.createHorizontalStrut(infoFrame.getHeight()/2));
    infoPanel.add(Box.createVerticalStrut((infoFrame.getWidth())/2));
    infoPanel.add(backButton);
    infoPanel.add(playButton);

    // Add the panel to the frame's content pane
    infoFrame.add(infoPanel);

    // Set the size and location of the frame and make it visible
    infoFrame.setSize(800, 600);
    infoFrame.setLocationRelativeTo(null);
    infoFrame.setVisible(true);

      }
}
