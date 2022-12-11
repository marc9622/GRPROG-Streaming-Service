package presentation;

import java.io.IOException;

import domain.ApplicationData;
import domain.User;
import domain.MediaParsing.InvalidStringFormatException;
import domain.UserList.UserAlreadyExistsException;
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
            case SelectUserButton button -> {
                window.gotoHomePage(data.getUser(button.getUsername()), data.getAllMedia(), this);
            }
            case AddUserButton button -> {
                try {
                    data.addUser(new User(button.getUsername(), button.getPassword()));
                } catch (UserAlreadyExistsException e1) {
                    window.showError(e1.getMessage());
                }
                window.gotoWelcomePage(data.getUsers(), this);
            }
            default -> throw new IllegalStateException("Invalid source for action event");
        }
    }

}
