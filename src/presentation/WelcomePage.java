package presentation;

import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import domain.User;

import static java.awt.Component.CENTER_ALIGNMENT;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.function.BiConsumer;

public class WelcomePage {
    
    public final JPanel panel;

    private final LoginPage loginPage;
    private final AddUserPage addUserPage;

    public WelcomePage(Set<User> users, BiConsumer<String, String> loginListener, BiConsumer<String, String> addUserListener) {

        // Creates the panel
        panel = new JPanel();

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(layout);
            panel.setAlignmentX(CENTER_ALIGNMENT);
        }

        // Creates the login page
        loginPage = new LoginPage(loginListener, new Runnable() {
            public void run() {
                loginPage.hide();
            }
        });

        // Creates the add user page
        addUserPage = new AddUserPage(addUserListener, new Runnable() {
            public void run() {
                addUserPage.hide();
            }
        });

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

            UserSelectionPanel userListButtons = new UserSelectionPanel(users, loginPage);
            panel.add(userListButtons);

            Filler usersAddFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(usersAddFiller);

            OpenAddUserButton addUserButton = new OpenAddUserButton("Add User", addUserPage);
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

    /** Creates a new user list buttons object. */
    public UserSelectionPanel(Set<User> users, LoginPage loginPage) {

        { // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
            this.setLayout(layout);
            this.setAlignmentY(CENTER_ALIGNMENT);
        }

        // Creates the buttons
        final SelectUserButton[] userButtons =
            users.stream()
                 .sorted()
                 .map(user -> new SelectUserButton(user.getUsername(), loginPage))
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
    public SelectUserButton(String username, LoginPage loginPage) {
        // Sets the username
        this.username = username;

        // Sets the text of the button
        this.setText(username);

        // Adds the listener to the button
        this.addActionListener(e -> loginPage.show(username));
    }
    
    public String getUsername() {
        return username;
    }
}

class LoginPage {

    public final JFrame frame;

    private final JLabel usernameLabel;

    public LoginPage(BiConsumer<String, String> loginListener, Runnable cancelLoginListener) {
        
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
            panel.add(usernameLabel);

            this.usernameLabel = new JLabel("");
            panel.add(this.usernameLabel);

            JLabel passwordLabel = new JLabel("Password:");
            panel.add(passwordLabel);

            JPasswordField passwordField = new JPasswordField(20);
            panel.add(passwordField);

            Filler bottomFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(bottomFiller);

            // Creates the buttons panel
            JPanel buttonPanel = new JPanel();
            
            // Creates the buttons
            JButton loginButton = new JButton("Login") {
                public void fireActionPerformed(ActionEvent e) {
                    loginListener.accept(getUsername(), new String(passwordField.getPassword()));
                }
            };
            buttonPanel.add(loginButton);
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> cancelLoginListener.run());
            buttonPanel.add(cancelButton);

            // Adds the button panel to the panel
            panel.add(buttonPanel);
        }

    }

    private String getUsername() {
        return usernameLabel.getText();
    }

    public void show(String username) {
        usernameLabel.setText(username);
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void dispose() {
        frame.dispose();
    }
}

class OpenAddUserButton extends JButton {

    public OpenAddUserButton(String text, AddUserPage addUserPage) {
        super(text);
        this.addActionListener(e -> addUserPage.show());
    }

}

class AddUserPage {

    public final JFrame frame;

    public AddUserPage(BiConsumer<String, String> addUserListener, Runnable cancelAddUserListener) {

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
            panel.add(usernameLabel);

            JTextField usernameField = new JTextField(20);
            panel.add(usernameField);

            JLabel passwordLabel = new JLabel("Password:");
            panel.add(passwordLabel);

            JPasswordField passwordField = new JPasswordField(20);
            panel.add(passwordField);

            Filler bottomFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(bottomFiller);

            // Creates the buttons panel
            JPanel buttonPanel = new JPanel();
            
            // Creates the buttons
            JButton addButton = new JButton("Add") {
                public void fireActionPerformed(ActionEvent e) {
                    addUserListener.accept(usernameField.getText(), new String(passwordField.getPassword()));
                }
            };
            buttonPanel.add(addButton);
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> cancelAddUserListener.run());
            buttonPanel.add(cancelButton);

            // Adds the button panel to the panel
            panel.add(buttonPanel);
        }

        // Sets the frame properties
        frame.pack();
        frame.setResizable(false);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void dispose() {
        frame.dispose();
    }
}