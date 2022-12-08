package presentation;

import javax.swing.JFrame;

public class PlaybackPage {
    
    private JFrame frame;

    public PlaybackPage() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

}
