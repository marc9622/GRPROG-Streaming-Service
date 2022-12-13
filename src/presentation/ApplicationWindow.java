package presentation;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import domain.Media;
import domain.User;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ApplicationWindow {
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private final JFrame frame;

    private WelcomePage welcomePage;

    private HomePage homePage;

    @SuppressWarnings("unused")
    private InformationPage informationPage;

    @SuppressWarnings("unused")
    private PlaybackPage playbackPage;

    public ApplicationWindow(Runnable onClose) {
        // Create the frame
        frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                onClose.run();
            }
        });

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

    public void gotoWelcomePage(List<User> users, BiConsumer<String, String> loginUserListener, BiConsumer<String, String> addUserListener) {
        clearFrame();

        welcomePage = new WelcomePage(users, loginUserListener, addUserListener);
        frame.add(welcomePage.panel);

        frame.revalidate();
        frame.repaint();
    }

    public void gotoHomePage(User user, List<Media> allMedia, Function<String, List<Media>> searcher, Consumer<Media> selectMediaListener) {
        clearFrame();

        homePage = new HomePage(allMedia, user::getFavorites, searcher, selectMediaListener);
        frame.add(homePage.panel);

        frame.revalidate();
        frame.repaint();
    }

    private void clearFrame() {
        frame.getContentPane().removeAll();
        if(welcomePage != null) welcomePage.disposeExtraFrames();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

}