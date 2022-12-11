package presentation;

import java.io.IOException;

import domain.ApplicationData;
import domain.User;
import domain.MediaParsing.InvalidStringFormatException;
import domain.UserList.UserAlreadyExistsException;
import domain.UserList.UserDoesNotExistException;
import presentation.WelcomePage.UserSelectionButtons.SelectUserButton;
import presentation.WelcomePage.OpenAddUserButton.AddUserPage.AddUserButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Application implements ActionListener {
    
    private final ApplicationData data;
    private final ApplicationWindow window;

    public Application() {
        data = new ApplicationData();
        window = new ApplicationWindow();

        // Read the media files
        try {
            data.readMedia();
        }
        
        // Catch the exceptions
        catch (IOException | InvalidStringFormatException e) {
            System.out.println(e.getMessage());
            
            // TODO: Could not read files
        }
        
        window.gotoWelcomePage(data.getUsers(), this);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getSource()) {
            // If the source is a AddUserButton, add the user and reload the welcome page
            case AddUserButton button -> {
                try {
                    data.addUser(new User(button.getUsername(), button.getPassword()));
                } catch (UserAlreadyExistsException e1) {
                    window.showError(e1.getMessage());
                }
                window.gotoWelcomePage(data.getUsers(), this);
            }
            // If the source is a SelectUserButton, go to the home page with the selected user
            case SelectUserButton button -> {
                try {
                    window.gotoHomePage(data.getUser(button.getUsername()), data.getAllMedia(), this);
                } catch (UserDoesNotExistException e1) {
                    throw new RuntimeException("Could not find user with the name of :" + button.getUsername() + ", " +
                                               "this should never happen.", e1);
                }
            }
            default -> throw new IllegalStateException("Invalid source for action event");
        }
    }

}
