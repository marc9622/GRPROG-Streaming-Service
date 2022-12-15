package presentation;

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
import presentation.UIUtils.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
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
     * The parameters are the username, the password, the confirm password, and the image path.
     * @param deleteUserFunction the function to call when a user is deleted.
     */
    public WelcomePage(List<User> users, BiConsumer<String, String> loginFunction, QuadStringConsumer addUserFunction, Consumer<String> deleteUserFunction) {

        // Creates the panel
        panel = new BackgroundPanel(Images.BACKGROUND());

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        }

        // Creates the login page
        loginPage = new LoginPage(loginFunction, deleteUserFunction);

        // Creates the add user page
        addUserPage = new AddUserPage(addUserFunction);

        { // Creates and adds the components to the panel
            panel.add(Fillers.VERTICAL_MEDIUM());

            JLabel welcomeLabel = new JLabel("Welcome to");
            welcomeLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Fonts.SIZE_LARGE));
            panel.add(welcomeLabel);

            panel.add(Fillers.VERTICAL_SMALL());

            JLabel titleLabel = new JLabel("1234 Movies");
            titleLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            titleLabel.setFont(titleLabel.getFont().deriveFont(Fonts.SIZE_TITLE));
            panel.add(titleLabel);

            panel.add(Fillers.VERTICAL_LARGE());

            JLabel selectLabel = new JLabel("Select a user");
            selectLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            selectLabel.setFont(selectLabel.getFont().deriveFont(Fonts.SIZE_LARGE));
            panel.add(selectLabel);

            panel.add(Fillers.VERTICAL_SMALL());

            UserSelectionPanel userListButtons = new UserSelectionPanel(users, loginPage::show);
            panel.add(userListButtons);

            panel.add(Fillers.VERTICAL_SMALL());

            OpenAddUserButton addUserButton = new OpenAddUserButton("Add User", addUserPage::show);
            addUserButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            panel.add(addUserButton);

            panel.add(Fillers.VERTICAL_LARGE());
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

        this.setOpaque(false);

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
                this.add(Fillers.HORIZONTAL_SMALL());
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
        this.setForeground(Color.WHITE);
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
        frame.setLocationRelativeTo(null);

        final Container contentPane = new BackgroundPanel(Images.BACKGROUND());
        frame.setContentPane(contentPane);

        { // Sets the layout of this outer panel and adds filler to the sides
            BoxLayout layout = new BoxLayout(contentPane, BoxLayout.X_AXIS);
            contentPane.setLayout(layout);

            // Left Filler
            contentPane.add(Fillers.HORIZONTAL_MEDIUM());
            // Right Filler
            contentPane.add(Fillers.HORIZONTAL_MEDIUM());
        }

        // Creates inner panel so that outer panel can be centered
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        contentPane.add(panel, 1);

        { // Sets the layout of the inner panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.setAlignmentX(JFrame.LEFT_ALIGNMENT);
        }

        { // Creates and adds the components to the panel
            panel.add(Fillers.VERTICAL_SMALL());

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(usernameLabel);

            actualNameLabel = new JLabel("");
            actualNameLabel.setFont(actualNameLabel.getFont().deriveFont(Fonts.SIZE_MEDIUM));
            actualNameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(actualNameLabel);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(passwordLabel);

            passwordField = new JPasswordField();
            passwordField.addActionListener(e -> loginFunction.accept(actualNameLabel.getText(), new String(passwordField.getPassword())));
            passwordField.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(passwordField);

            panel.add(Fillers.VERTICAL_SMALL());

            { // Creates the buttons panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setOpaque(false);
                buttonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                panel.add(buttonPanel);
                
                // Creates the buttons
                JButton loginButton = new JButton("Login") {
                    public void fireActionPerformed(ActionEvent e) {
                        loginFunction.accept(actualNameLabel.getText(), new String(passwordField.getPassword()));
                    }
                };
                buttonPanel.add(loginButton);

                buttonPanel.add(Fillers.HORIZONTAL_SMALL());

                JButton deleteButton = new JButton("Delete") {
                    public void fireActionPerformed(ActionEvent e) {
                        deleteUserFunction.accept(actualNameLabel.getText());
                    }
                };
                buttonPanel.add(deleteButton);
                
                buttonPanel.add(Fillers.HORIZONTAL_SMALL());

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(e -> hideAndClear());
                buttonPanel.add(cancelButton);
            }
        
            panel.add(Fillers.VERTICAL_SMALL());
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
        frame.setLocationRelativeTo(null);

        final Container contentPane = new BackgroundPanel(Images.BACKGROUND());
        frame.setContentPane(contentPane);

        { // Sets the layout of this outer panel and adds filler to the sides
            BoxLayout layout = new BoxLayout(contentPane, BoxLayout.X_AXIS);
            contentPane.setLayout(layout);

            // Left Filler
            contentPane.add(Fillers.HORIZONTAL_MEDIUM());
            // Right Filler
            contentPane.add(Fillers.HORIZONTAL_MEDIUM());
        }
        
        // Creates inner panel so that outer panel can be centered
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        contentPane.add(panel, 1);

        { // Sets the layout of the inner panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.setAlignmentX(JFrame.LEFT_ALIGNMENT);
        }

        { // Creates and adds the components to the panel
            panel.add(Fillers.VERTICAL_SMALL());

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(usernameLabel);

            usernameField = new JTextField();
            usernameField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
            panel.add(usernameField);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(passwordLabel);

            passwordField = new JPasswordField();
            passwordField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
            panel.add(passwordField);

            JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
            confirmPasswordLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            panel.add(confirmPasswordLabel);

            confirmPasswordField = new JPasswordField();
            confirmPasswordField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
            panel.add(confirmPasswordField);

            { // Creates panel for the imageChooser and imageLabel
                JPanel imagePanel = new JPanel();
                imagePanel.setOpaque(false);
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
                
                { // Sets the imageChooser to be transparent
                    // Prints the amount of containers in the imageChooser recursively
                    // var printContainers = new BiConsumer<Component, Integer>() {
                    //     public void accept(Component component, Integer indent) {
                    //         if(component instanceof Container container) {
                    //             for(int i = 0; i < container.getComponentCount(); i++) {
                    //                 System.out.println("    ".repeat(indent) + "(" + i + ")" + container.getComponent(i).getClass().getSimpleName());
                    //                 accept(container.getComponent(i), indent + 1);
                    //             }
                    //         }
                    //         else System.out.println("    ".repeat(indent) + "(" + 0 + ")" + component.getClass().getSimpleName());
                    //     }
                    // };
                    // printContainers.accept(imageChooser, 0);
                    
                    var setOpaqueFalse = new BiConsumer<Component, int[]>() {
                        public void accept(Component component, int[] index) {
                            if(index.length == 0) {
                                if(component instanceof JPanel panel)
                                panel.setOpaque(false);
                            }
                            else if(component instanceof Container container)
                            accept(container.getComponent(index[0]), Arrays.copyOfRange(index, 1, index.length));
                        }
                    };
                    
                    setOpaqueFalse.accept(imageChooser, new int[] {0});
                    setOpaqueFalse.accept(imageChooser, new int[] {0, 0});
                    setOpaqueFalse.accept(imageChooser, new int[] {3});
                    setOpaqueFalse.accept(imageChooser, new int[] {3, 0});
                    setOpaqueFalse.accept(imageChooser, new int[] {3, 2});
                    setOpaqueFalse.accept(imageChooser, new int[] {3, 3});
                }
                
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

            panel.add(Fillers.VERTICAL_SMALL());

            { // Creates the buttons panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setOpaque(false);
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
                
                buttonPanel.add(Fillers.HORIZONTAL_SMALL());

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(e -> hideAndClear());
                buttonPanel.add(cancelButton);
            }
        
            panel.add(Fillers.VERTICAL_SMALL());
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