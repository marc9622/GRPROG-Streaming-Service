package presentation;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Box.Filler;

import domain.Media;
import domain.User;
import presentation.AddUserPage.QuadStringConsumer;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static presentation.UIConstants.*;

public class ApplicationWindow {
    
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
            frame.setBounds((screenSize.width - DEFAULT_WINDOW_WIDTH) / 2,
                            (screenSize.height - DEFAULT_WINDOW_HEIGHT) / 2,
                            DEFAULT_WINDOW_WIDTH,
                            DEFAULT_WINDOW_HEIGHT);

            // Set the frame title
            frame.setTitle("1234 Movies");

            // Set the frame to exit when closed
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        // Set visible
        frame.setVisible(true);
    }

    public void gotoWelcomePage(List<User> users, BiConsumer<String, String> loginUserListener, QuadStringConsumer addUserListener, Consumer<String> deleteUserFunction) {
        clearFrame();

        welcomePage = new WelcomePage(users, loginUserListener, addUserListener, deleteUserFunction);
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

final class UIConstants {
    private UIConstants() {}
    
    static final int DEFAULT_WINDOW_WIDTH = 1200;
    static final int DEFAULT_WINDOW_HEIGHT = 800;

    private static final Filler fillerHelper(int minWidth, int minHeight, int prefWidth, int prefHeight, int maxWidth, int maxHeight) {
        return new Filler(new Dimension(minWidth, minHeight), new Dimension(prefWidth, prefHeight), new Dimension(maxWidth, maxHeight));
    }

    static final Filler FILLER_HORIZONTAL_SMALL() {
        return fillerHelper(0, 0, 25, 0, 75, 0);
    }

    static final Filler FILLER_HORIZONTAL_MEDIUM() {
        return fillerHelper(0, 0, 75, 0, 200, 0);
    }

    static final Filler FILLER_HORIZONTAL_LARGE() {
        return fillerHelper(0, 0, 100, 0, 250, 0);
    }

    static final Filler FILLER_VERTICAL_SMALL() {
        return fillerHelper(0, 0, 0, 25, 0, 75);
    }

    static final Filler FILLER_VERTICAL_MEDIUM() {
        return fillerHelper(0, 0, 0, 75, 0, 200);
    }

    static final Filler FILLER_VERTICAL_LARGE() {
        return fillerHelper(0, 0, 0, 100, 0, 250);
    }
        
    static final String IMAGE_BACKGROUND = "./Images/Background.png";
    static final String IMAGE_BUTTON     = "./Images/Button.png";

    static final float FONT_SIZE_SMALL  = 12;
    static final float FONT_SIZE_MEDIUM = 16;
    static final float FONT_SIZE_LARGE  = 32;
    static final float FONT_SIZE_TITLE  = 64;

}