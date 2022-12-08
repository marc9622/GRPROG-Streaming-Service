package presentation;

import javax.swing.JFrame;

import domain.User;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class ApplicationWindow implements ActionListener {
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private JFrame frame;

    private WelcomePage welcomePage;
    private HomePage homePage;
    private InformationPage informationPage;
    private PlaybackPage playbackPage;

    private static enum Page {
        WELCOME, HOME, INFORMATION, PLAYBACK
    }

    private Page currentPage;

    public ApplicationWindow(Collection<User> users) {
        // Create the frame
        frame = new JFrame();

        // Initialize the frame
        {
            // Get the screen size
            Rectangle screenSize = frame.getGraphicsConfiguration().getBounds();

            // Set the frame size to default size and center it on the screen
            frame.setBounds((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2, WIDTH, HEIGHT);

            // Set the frame title
            frame.setTitle("1234 Movies");

            // Set the frame to exit when closed
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        // Change the page to the welcome page
        changePage(Page.WELCOME, users);

        // Show the frame
        frame.setVisible(true);
    }

    private void changePage(Page page, Collection<User> users) {
        // Clear the frame
        clearFrame();

        // Set the current page
        currentPage = page;

        // Add the page to the frame
        switch (currentPage) {
            case WELCOME:
                welcomePage = new WelcomePage(users, this);
                frame.add(welcomePage.getPanel());
                break;
            case HOME:
                homePage = new HomePage();
                frame.add(homePage.getPanel());
                break;
            case INFORMATION:
                informationPage = new InformationPage();
                frame.add(informationPage.getPanel());
                break;
            case PLAYBACK:
                playbackPage = new PlaybackPage();
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

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}