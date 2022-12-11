package presentation;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import domain.Media;
import domain.User;

import java.awt.Rectangle;
import java.util.List;
import java.util.Set;

public class ApplicationWindow {
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private JFrame frame;

    private WelcomePage welcomePage;
    private HomePage homePage;
    private InformationPage informationPage;
    private PlaybackPage playbackPage;

    public ApplicationWindow() {
        // Create the frame
        frame = new JFrame();

        { // Initialize the frame
            // Get the screen size
            Rectangle screenSize = frame.getGraphicsConfiguration().getBounds();

            // Set the frame size to default size and center it on the screen
            frame.setBounds((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2, WIDTH, HEIGHT);

            // Set the frame title
            frame.setTitle("1234 Movies");

            // Set the frame to exit when closed
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        // Set visible
        frame.setVisible(true);
    }

    public void gotoWelcomePage(Set<User> users, Application applicationListener) {
        clearFrame();

        welcomePage = new WelcomePage(users, applicationListener);
        frame.add(welcomePage.getPanel());

        frame.revalidate();
        frame.repaint();
    }

    public void gotoHomePage(User user, List<Media> allMedia, Application applicationListener) {
        clearFrame();

        homePage = new HomePage(user, allMedia, applicationListener);
        frame.add(homePage.getPanel());

        frame.revalidate();
        frame.repaint();
    }

    private void clearFrame() {
        frame.getContentPane().removeAll();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

}