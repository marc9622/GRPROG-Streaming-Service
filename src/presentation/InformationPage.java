package presentation;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;

public class InformationPage {
    
    private JPanel panel;

    public InformationPage() {
        panel = new JPanel();
    }

    public JPanel getPanel() {
        return panel;
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
        JFrame playButtonFrame = new JFrame("Play video");
        playButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        // Create a JButton with some text and add it to the panel
        JButton playButton = new JButton("PLAY");
        playButton.setBounds(100,100,100,100);
        playButtonFrame.add(playButton);

    
        // Set the size and location of the frame and make it visible
        playButtonFrame.setSize(800, 600);
        playButtonFrame.setLocationRelativeTo(null);
        playButtonFrame.setVisible(true);

      }
    
}
