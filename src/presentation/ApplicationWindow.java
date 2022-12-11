package presentation;

import javax.swing.JFrame;

import domain.User;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.Collection;

public class ApplicationWindow {
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private JFrame frame;

    private WelcomePage welcomePage;
    private HomePage homePage;
    private InformationPage informationPage;
    private PlaybackPage playbackPage;

    public static enum Page {
        WELCOME, HOME, INFORMATION, PLAYBACK
    }

    private Page currentPage;

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

    public void changeToPage(Page page, Collection<User> users, ActionListener actionListener) {
        // Clear the frame
        clearFrame();

        // Set the current page
        currentPage = page;

        // Add the page to the frame
        switch (currentPage) {
            case WELCOME:
                welcomePage = welcomePage != null ? welcomePage : new WelcomePage(users, actionListener);
                frame.add(welcomePage.getPanel());
                break;
            case HOME:
                homePage = homePage != null ? homePage : new HomePage();
                frame.add(homePage.getPanel());
                break;
            case INFORMATION:
                informationPage = informationPage != null ? informationPage : new InformationPage();
                frame.add(informationPage.getPanel());
                break;
            case PLAYBACK:
                playbackPage = playbackPage != null ? playbackPage : new PlaybackPage();
                frame.add(playbackPage.getPanel());
                break;
        }

        // Refresh the frame
        frame.revalidate();
        frame.repaint();
    }

    private void clearFrame() {
        frame.getContentPane().removeAll();
    }

}