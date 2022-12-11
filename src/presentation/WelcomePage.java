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

import presentation.WelcomePage.OpenAddUserButton.AddUserPage.CancelAddUserButton;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class WelcomePage {
    
    private JPanel panel;

    public WelcomePage(Set<User> users, Application application) {
        panel = new JPanel();

        // Set the layout of the panel
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.setAlignmentX(CENTER_ALIGNMENT);

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

            UserSelectionButtons userListButtons = new UserSelectionButtons(users, application);
            panel.add(userListButtons);

            Filler usersAddFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 75));
            panel.add(usersAddFiller);

            OpenAddUserButton addUserButton = new OpenAddUserButton("Add User", application);
            addUserButton.setAlignmentX(CENTER_ALIGNMENT);
            panel.add(addUserButton);

            Filler bottomFiller = new Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 250));
            panel.add(bottomFiller);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    class UserSelectionButtons extends JPanel {
        
        private final SelectUserButton[] userButtons;

        /** Creates a new user list buttons object. */
        private UserSelectionButtons(Set<User> users, Application application) {
            //panel.userList = userList;

            // Creates the buttons
            userButtons = users.stream()
                               .sorted()
                               .map(user -> new SelectUserButton(user.getUsername(), application))
                               .toArray(SelectUserButton[]::new);
            
            // Adds the buttons to the panel
            for(int i = 0; i < userButtons.length; i++) {

                this.add(userButtons[i]);

                // Adds a filler between the buttons
                if (i < userButtons.length - 1)
                    this.add(new Filler(new Dimension(10, 0), new Dimension(25, 0), new Dimension(50, 0)));
            }

            // Sets the layout of the panel
            BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
            this.setLayout(layout);
            this.setAlignmentY(CENTER_ALIGNMENT);
        }

        class SelectUserButton extends JButton {
        
            private final String username;
    
            /** Creates a new user button object. */
            private SelectUserButton(String username, ActionListener listener) {
                // Sets the username
                this.username = username;
    
                // Sets the text of the button
                this.setText(username);

                // Adds the listener to the button
                this.addActionListener(listener);
            }
            
            public String getUsername() {
                return username;
            }
        }

    }

    class OpenAddUserButton extends JButton implements ActionListener {

        private final AddUserPage frame;

        public OpenAddUserButton(String text, Application application) {
            super(text);
            this.addActionListener(this);

            frame = new AddUserPage(application);
        }

        public void actionPerformed(ActionEvent e) {
            switch (e.getSource()) {
                case OpenAddUserButton button -> frame.show();
                case CancelAddUserButton button -> frame.hide();
                default -> throw new IllegalStateException("Invalid source for action event");
            }
        }

        class AddUserPage extends JPanel {

            private final JFrame frame;

            public AddUserPage(Application application) {

                // Creates the frame
                frame = new JFrame("Add User");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setContentPane(this);
                frame.setLocationRelativeTo(WelcomePage.this.panel);

                { // Sets the layout of this outer panel and adds filler to the sides
                    BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
                    this.setLayout(layout);
                    this.setAlignmentX(CENTER_ALIGNMENT);

                    Filler leftFiller = new Filler(new Dimension(0, 0), new Dimension(50, 0), new Dimension(200, 0));
                    this.add(leftFiller);

                    Filler rightFiller = new Filler(new Dimension(0, 0), new Dimension(50, 0), new Dimension(200, 0));
                    this.add(rightFiller);
                }
                
                // Creates inner panel so that outer panel can be centered
                JPanel panel = new JPanel();
                this.add(panel, 1);

                { // Sets the layout of the inner panel
                    BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                    panel.setLayout(layout);
                    panel.setAlignmentX(LEFT_ALIGNMENT);
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
                    JButton addButton = new AddUserButton("Add", usernameField, passwordField, application);
                    buttonPanel.add(addButton);
                    
                    CancelAddUserButton cancelButton = new CancelAddUserButton("Cancel");
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

            class CancelAddUserButton extends JButton {

                public CancelAddUserButton(String text) {
                    super(text);
                    this.addActionListener(OpenAddUserButton.this);
                }

            }

            class AddUserButton extends JButton {

                private final JTextField usernameField;
                private final JPasswordField passwordField;

                public AddUserButton(String text, JTextField usernameField, JPasswordField passwordField, Application application) {
                    super(text);
                    this.usernameField = usernameField;
                    this.passwordField = passwordField;
                    this.addActionListener(application);
                }

                public String getUsername() {
                    return usernameField.getText();
                }

                public String getPassword() {
                    return new String(passwordField.getPassword());
                }

            }
        }

    }
}