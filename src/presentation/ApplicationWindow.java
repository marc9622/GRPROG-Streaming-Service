package presentation;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import domain.Media;
import domain.User;
import domain.MediaSorting.SortBy;
import presentation.WelcomePage.QuadStringConsumer;

import static presentation.UIUtils.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ApplicationWindow {
    
    private final JFrame frame;

    private WelcomePage welcomePage;

    private HomePage homePage;

    private InformationPage informationPage;

    public ApplicationWindow() {

        { // Changes the default UI settings
            // Change default text color to white for labels
            UIManager.put("Label.foreground", Color.WHITE);
        }

        // Create the frame
        frame = new JFrame();

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

    public void addOnCloseListener(Runnable onClose) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                onClose.run();
            }
        });
    }

    public void gotoWelcomePage(List<User> users, BiConsumer<String, String> loginUserListener,
                                QuadStringConsumer addUserListener, Consumer<String> deleteUserListener) {
        clearFrame();

        welcomePage = new WelcomePage(users, loginUserListener, addUserListener, deleteUserListener);
        frame.add(welcomePage.panel);

        frame.revalidate();
        frame.repaint();
    }

    public void gotoHomePage(User user, List<Media> allMedia, Function<SortBy, List<Media>> sorter, Function<String, List<Media>> searcher,
                             BiConsumer<Media, User> selectMediaAsUserListener, Runnable logoutListener) {
        clearFrame();

        homePage = new HomePage(allMedia, user::getFavorites, sorter, searcher, media -> selectMediaAsUserListener.accept(media, user), logoutListener);
        frame.add(homePage.panel);

        frame.revalidate();
        frame.repaint();
    }

    public void gotoInformationPage(Media media, Function<Media, Boolean> isMediaFavoriteFunction, Consumer<Media> addToFavoritesListener,
                                    Consumer<Media> removeFromFavoritesListener, Consumer<Media> playMediaListener, Runnable goBackListner) {
        clearFrame();

        informationPage = new InformationPage(media, isMediaFavoriteFunction, addToFavoritesListener,
                                              removeFromFavoritesListener, playMediaListener, goBackListner);
        frame.add(informationPage.panel);

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
