package presentation;

import java.io.IOException;

import domain.ApplicationData;
import domain.User;
import domain.MediaParsing.InvalidStringFormatException;
import domain.User.InvalidImagePathException;
import domain.User.InvalidPasswordException;
import domain.User.InvalidUsernameException;
import domain.UserSet.UserAlreadyExistsException;
import domain.UserSet.UserDoesNotExistException;

public class Application {
    
    private final ApplicationData data;
    private final ApplicationWindow window;

    public Application() {
        data = new ApplicationData();
        window = new ApplicationWindow(() -> {
            try {
                data.saveUsers();
            } catch (IOException e) {
                System.out.println(e.getMessage());

                // TODO: Could not save users
            }
        });

        { // Read and load data
            // Load the users
            try {
                data.loadUsers();
            }
            // Catch the exceptions
            catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                
                // TODO: Could not load users
            }
            try {
                data.readMedia();
            }
            // Catch the exceptions
            catch (IOException | InvalidStringFormatException e) {
                System.out.println(e.getMessage());
                
                // TODO: Could not read files
            }
        }
        
        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser, this::deleteUser);
    }

    private void loginUser(String username, String password) {
        User user;
        try {
            user = data.getUser(username);
        }
        catch (UserDoesNotExistException e) {
            throw new RuntimeException("Could not find user with the name of :" + username + ", " +
                                       "this should never happen.", e);
        }
        if(user.checkPassword(password)) {
            window.gotoHomePage(user, data.getAllMedia(), data::searchAllMedia, null); // TODO: Add selectMediaListener
            System.out.println("Logged in as " + username + "!");
        }
        else {
            window.showError("Incorrect password");
        }
    }

    private void addUser(String username, String password, String confirmPassword, String imagePath) {
        if(!password.equals(confirmPassword)) {
            window.showError("Passwords do not match");
            return;
        }
        try {
            data.addUser(new User(username, password, imagePath));
        }
        catch (UserAlreadyExistsException | InvalidUsernameException | InvalidPasswordException | InvalidImagePathException e) {
            window.showError(e.getMessage());
        }
        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser, this::deleteUser);
    }

    private void deleteUser(String username) {
        try {
            data.removeUser(username);
        }
        catch (UserDoesNotExistException e) {
            window.showError(e.getMessage());
        }
        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser, this::deleteUser);
    }

}
