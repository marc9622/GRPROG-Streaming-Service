package presentation;

import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.User;

import static javax.swing.JPanel.*;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Collection;

public class WelcomePage {
    
    private JPanel panel;

    public WelcomePage(Collection<User> users, ActionListener selectUserListener) {
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

            Filler selectUsersFiller = new Filler(new Dimension(0, 0), new Dimension(0, 25), new Dimension(0, 100));
            panel.add(selectUsersFiller);

            UserListButtons userListButtons = new UserListButtons(users, selectUserListener);
            panel.add(userListButtons);

            Filler usersButtomFiller = new Filler(new Dimension(0, 0), new Dimension(0, 100), new Dimension(0, 400));
            panel.add(usersButtomFiller);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    class UserListButtons extends JPanel {
        
        private final UserButton[] userButtons;

        /** Creates a new user list buttons object. */
        private UserListButtons(Collection<User> users, ActionListener listener) {
            //panel.userList = userList;

            // Creates the buttons
            userButtons = users.stream()
                               .sorted()
                               .map(user -> new UserButton(user.getUsername(), listener))
                               .toArray(UserButton[]::new);
            
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

        class UserButton extends JButton {
        
            public final String username;
    
            /** Creates a new user button object. */
            private UserButton(String username, ActionListener listener) {
                // Sets the username
                this.username = username;
    
                // Sets the text of the button
                this.setText(username);

                // Sets the action command of the button
                this.setActionCommand(username);

                // Adds the listener to the button
                this.addActionListener(listener);
            }
            
        }

    }

}