package presentation;

import java.io.IOException;

import domain.ApplicationData;
import domain.User;
import domain.MediaParsing.InvalidStringFormatException;
import domain.UserList.UserAlreadyExistsException;
import domain.UserList.UserDoesNotExistException;

public class Application {
    
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
        
        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser);
    }

    private void loginUser(String username, String password) {
        User user;
        try {
            user = data.getUser(username);
        }
        catch (UserDoesNotExistException e1) {
            throw new RuntimeException("Could not find user with the name of :" + username + ", " +
                                       "this should never happen.", e1);
        }
        if(user.checkPassword(password)) {
            window.gotoHomePage(user);
            System.out.println("Logged in as " + username + "!");
        }
        else {
            window.showError("Incorrect password");
        }
    }

    private void addUser(String username, String password) {
        try {
            data.addUser(new User(username, password));
        }
        catch (UserAlreadyExistsException e1) {
            window.showError(e1.getMessage());
        }
        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser);
    }

}
