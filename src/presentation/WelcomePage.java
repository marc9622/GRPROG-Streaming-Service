package presentation;

import javax.swing.Box.Filler;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import domain.User;
import presentation.AddUserPage.QuadStringConsumer;

import static java.awt.Component.CENTER_ALIGNMENT;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WelcomePage {
    
    public final JPanel panel;

    private final LoginPage loginPage;
    private final AddUserPage addUserPage;

    /** Creates the welcome page.
     * @param users the list of users to display.
     * @param loginFunction the function to call when a user logs in.
     * The first parameter is the username, the second is the password.
     * @param addUserFunction the function to call when a user is added.
     * The first parameter is the username, the second is the password.
     */
    public WelcomePage(List<User> users, BiConsumer<String, String> loginFunction, QuadStringConsumer addUserFunction, Consumer<String> deleteUserFunction) {

        // Creates the panel
        panel = new JPanel();

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.setAlignmentX(CENTER_ALIGNMENT);
        }

        // Creates the login page
        loginPage = new LoginPage(loginFunction, deleteUserFunction);

        // Creates the add user page
        addUserPage = new AddUserPage(addUserFunction);

        { // Creates and adds the components to the panel
            Filler topWelcomeFiller = new Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 250));
            panel.add(topWelcomeFiller);

            JLabel welcomeLabel = new JLabel("Welcome to");
            welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
            welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(32f));
            panel.add(welcomeLabel);

            JLabel titleLabel = new JLabel("1234 Movies");
            titleLabel.setAlignmentX(CENTER_ALIGNMENT);
            titleLabel.setFont(titleLabel.getFont().deriveFont(64f));
            panel.add(titleLabel);

            Filler titleSelectFiller = new Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 250));
            panel.add(titleSelectFiller);

            JLabel selectLabel = new JLabel("Select a user");
            selectLabel.setAlignmentX(CENTER_ALIGNMENT);
            selectLabel.setFont(selectLabel.getFont().deriveFont(32f));
            panel.add(selectLabel);

            Filler selectUsersFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(selectUsersFiller);

            UserSelectionPanel userListButtons = new UserSelectionPanel(users, loginPage::show);
            panel.add(userListButtons);

            Filler usersAddFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(usersAddFiller);

            OpenAddUserButton addUserButton = new OpenAddUserButton("Add User", addUserPage::show);
            addUserButton.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(addUserButton);

            Filler bottomFiller = new Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 250));
            panel.add(bottomFiller);
        }
    
    }

    public void disposeExtraFrames() {
        loginPage.dispose();
        addUserPage.dispose();
    }
}

class UserSelectionPanel extends JPanel {

    /** Creates a new user selection panel object. */
    public UserSelectionPanel(List<User> users, Consumer<String> loginPagerShower) {

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
            this.setLayout(layout);
            this.setAlignmentY(CENTER_ALIGNMENT);
        }

        // Creates the buttons
        final SelectUserButton[] userButtons =
            users.stream()
                 .sorted()
                 .map(user -> new SelectUserButton(user.getUsername(), user.getImagePath(), loginPagerShower))
                 .toArray(SelectUserButton[]::new);
        
        // Adds the buttons to the panel
        for(int i = 0; i < userButtons.length; i++) {
            this.add(userButtons[i]);

            // Adds a filler between the buttons
            if (i < userButtons.length - 1)
                this.add(new Filler(new Dimension(10, 0), new Dimension(25, 0), new Dimension(50, 0)));
        }
    }

}

class SelectUserButton extends JButton {
    
    private final String username;

    /** Creates a new user button object. */
    public SelectUserButton(String username, Optional<String> imagePath, Consumer<String> loginPageShower) {
        // Sets the username
        this.username = username;

        final int IMAGE_WIDTH = 100;
        final int IMAGE_HEIGHT = 100;

        // Sets the image of the button
        if (imagePath.isPresent()) {
            Image image = new ImageIcon(imagePath.get()).getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            this.setIcon(new ImageIcon(image));

            // Hides the border
            this.setMargin(new Insets(0, 0, 25, 0));
            this.setOpaque(false);
            this.setBorderPainted(false);
            this.setContentAreaFilled(false);
        }
        
        // Sets the size of the button
        this.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 50));
        this.setMaximumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 50));

        // Sets the text of the button
        this.setText(username);
        this.setHorizontalTextPosition(CENTER);
        this.setVerticalTextPosition(BOTTOM);

        // Adds the listener to the button
        this.addActionListener(e -> loginPageShower.accept(username));
    }
    
    public String getUsername() {
        return username;
    }
}

class LoginPage {

    public final JFrame frame;

    private final JLabel actualNameLabel;
    private final JPasswordField passwordField;

    public LoginPage(BiConsumer<String, String> loginFunction, Consumer<String> deleteUserFunction) {
        
        // Creates the frame
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        final Container contentPane = frame.getContentPane();

        { // Sets the layout of this outer panel and adds filler to the sides
            BoxLayout layout = new BoxLayout(contentPane, BoxLayout.X_AXIS);
            contentPane.setLayout(layout);

            Filler leftFiller = new Filler(new Dimension(0, 0), new Dimension(50, 0), new Dimension(200, 0));
            contentPane.add(leftFiller);

            Filler rightFiller = new Filler(new Dimension(0, 0), new Dimension(50, 0), new Dimension(200, 0));
            contentPane.add(rightFiller);
        }

        // Creates inner panel so that outer panel can be centered
        final JPanel panel = new JPanel();
        contentPane.add(panel, 1);

        { // Sets the layout of the inner panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.setAlignmentX(JFrame.LEFT_ALIGNMENT);
        }

        { // Creates and adds the components to the panel
            Filler topFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(topFiller);

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(usernameLabel);

            actualNameLabel = new JLabel("");
            actualNameLabel.setFont(actualNameLabel.getFont().deriveFont(16));
            actualNameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(actualNameLabel);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(passwordLabel);

            passwordField = new JPasswordField(20);
            passwordField.addActionListener(e -> loginFunction.accept(actualNameLabel.getText(), new String(passwordField.getPassword())));
            passwordField.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(passwordField);

            Filler bottomFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(bottomFiller);

            { // Creates the buttons panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                panel.add(buttonPanel);
                
                // Creates the buttons
                JButton loginButton = new JButton("Login") {
                    public void fireActionPerformed(ActionEvent e) {
                        loginFunction.accept(actualNameLabel.getText(), new String(passwordField.getPassword()));
                    }
                };
                buttonPanel.add(loginButton);

                Filler loginDeleteFiller = new Filler(new Dimension(0, 0), new Dimension(25, 0), new Dimension(50, 0));
                buttonPanel.add(loginDeleteFiller);

                JButton deleteButton = new JButton("Delete") {
                    public void fireActionPerformed(ActionEvent e) {
                        deleteUserFunction.accept(actualNameLabel.getText());
                    }
                };
                buttonPanel.add(deleteButton);
                
                Filler deleteCancelFiller = new Filler(new Dimension(0, 0), new Dimension(25, 0), new Dimension(50, 0));
                buttonPanel.add(deleteCancelFiller);

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(e -> hideAndClear());
                buttonPanel.add(cancelButton);
            }
        }

        // Sets the frame properties
        frame.pack();
        frame.setResizable(false);
    }

    public void show(String username) {
        actualNameLabel.setText(username);
        frame.setVisible(true);
    }

    public void hideAndClear() {
        frame.setVisible(false);
        actualNameLabel.setText("");
        passwordField.setText("");
    }

    public void dispose() {
        frame.dispose();
    }
}

class OpenAddUserButton extends JButton {

    public OpenAddUserButton(String text, Runnable addUserPageShower) {
        super(text);
        this.addActionListener(e -> addUserPageShower.run());
    }

}

class AddUserPage {

    public final JFrame frame;

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JFileChooser imageChooser;

    public AddUserPage(QuadStringConsumer addUserFunction) {

        // Creates the frame
        frame = new JFrame("Add User");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        final Container contentPane = frame.getContentPane();

        { // Sets the layout of this outer panel and adds filler to the sides
            BoxLayout layout = new BoxLayout(contentPane, BoxLayout.X_AXIS);
            contentPane.setLayout(layout);

            Filler leftFiller = new Filler(new Dimension(0, 0), new Dimension(50, 0), new Dimension(200, 0));
            contentPane.add(leftFiller);

            Filler rightFiller = new Filler(new Dimension(0, 0), new Dimension(50, 0), new Dimension(200, 0));
            contentPane.add(rightFiller);
        }
        
        // Creates inner panel so that outer panel can be centered
        final JPanel panel = new JPanel();
        contentPane.add(panel, 1);

        { // Sets the layout of the inner panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.setAlignmentX(JFrame.LEFT_ALIGNMENT);
        }

        { // Creates and adds the components to the panel
            Filler topFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(topFiller);

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(usernameLabel);

            usernameField = new JTextField(20);
            usernameField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
            panel.add(usernameField);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(passwordLabel);

            passwordField = new JPasswordField(20);
            passwordField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
            panel.add(passwordField);

            JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
            confirmPasswordLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(confirmPasswordLabel);

            confirmPasswordField = new JPasswordField(20);
            confirmPasswordField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
            panel.add(confirmPasswordField);

            { // Creates panel for the imageChooser and imageLabel
                JPanel imagePanel = new JPanel();
                imagePanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                panel.add(imagePanel);

                JLabel imageLabel = new JLabel("Image:");
                imagePanel.add(imageLabel);

                final int IMAGE_WIDTH = 100;
                final int IMAGE_HEIGHT = 100;

                imageChooser = new JFileChooser();
                imageChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                imageChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
                imageChooser.addActionListener(e -> {
                    if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                        Image image = new ImageIcon(imageChooser.getSelectedFile().getAbsolutePath())
                                        .getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(image));
                        imageLabel.setText(imageChooser.getSelectedFile().getName());
                        imageLabel.setToolTipText(imageChooser.getSelectedFile().getAbsolutePath());
                    }
                });

                // Sets the size of the button
                imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 50));
                imageLabel.setMaximumSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT + 50));

                // Hides the border
                imageLabel.setOpaque(false);

                // Sets the text of the button
                imageLabel.setHorizontalTextPosition(JLabel.CENTER);
                imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
                
                imagePanel.add(imageChooser);
            }

            Filler bottomFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(bottomFiller);

            { // Creates the buttons panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                panel.add(buttonPanel);
                
                // Creates the buttons
                JButton addButton = new JButton("Add") {
                    public void fireActionPerformed(ActionEvent e) {
                        String username = usernameField.getText();
                        String password = new String(passwordField.getPassword());
                        String confirmPassword = new String(confirmPasswordField.getPassword());
                        File imageFile = imageChooser.getSelectedFile();
                        String imagePath = imageFile == null ? null : imageFile.getAbsolutePath();

                        addUserFunction.accept(username, password, confirmPassword, imagePath);
                    }
                };
                buttonPanel.add(addButton);
                
                Filler addCancelFiller = new Filler(new Dimension(0, 0), new Dimension(25, 0), new Dimension(50, 0));
                buttonPanel.add(addCancelFiller);

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(e -> hideAndClear());
                buttonPanel.add(cancelButton);
                
            }
        }

        // Sets the frame properties
        frame.pack();
        frame.setResizable(false);
    }

    @FunctionalInterface
    interface QuadStringConsumer {
        void accept(String a, String b, String c, String d);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hideAndClear() {
        frame.setVisible(false);
        usernameField.setText("");
        passwordField.setText("");
    }

    public void dispose() {
        frame.dispose();
    }
}