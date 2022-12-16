package presentation;

import java.util.Optional;

import domain.ApplicationData;
import domain.Media;
import domain.User;

public class Application {
    
    private final ApplicationData data;
    private final ApplicationWindow window;

    public Application() {
        data = new ApplicationData();
        window = new ApplicationWindow();
        window.addOnCloseListener(() -> ErrorHandling.tryOrShowExceptionMessage(data::saveUsers, window));

        ErrorHandling.tryOrShowCustomMessage(data::loadUsers, "Failed to load users.", window);
        ErrorHandling.tryOrShowExceptionMessage(data::readMedia, window);
        
        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser, this::deleteUser);
    }

    public void loginUser(String username, String password) {
        final Optional<User> user = ErrorHandling.tryReturnOrShowCustomMessage(
                                                    () -> data.getUser(username),
                                                    "Failed to delete user: " + username,
                                                    window);
        if(!user.isPresent())
            return;

        if(user.get().checkPassword(password))
            window.gotoHomePage(user.get(), data.getAllMedia(), data::searchAllMedia, this::selectMedia, this::logoutUser);
        else
            window.showError("Incorrect password");
    }

    public void logoutUser() {
        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser, this::deleteUser);
    }

    public void addUser(String username, String password, String confirmPassword, String imagePath) {
        if(!password.equals(confirmPassword)) {
            ErrorHandling.showMessage("Passwords do not match", window);
            return;
        }

        ErrorHandling.tryOrShowExceptionMessage(() -> data.addUser(username, password, imagePath), window);

        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser, this::deleteUser);
    }

    public void deleteUser(String username) {
        ErrorHandling.tryOrShowExceptionMessage(() -> data.removeUser(username), window);

        window.gotoWelcomePage(data.getUsers(), this::loginUser, this::addUser, this::deleteUser);
    }

    public void selectMedia(Media media, User user) {
        window.gotoInformationPage(media, user::isFavorite, user::addFavorite, user::removeFavorite,
                                    m -> System.out.println("Playing " + m.title),
                                   () -> window.gotoHomePage(user, data.getAllMedia(), data::searchAllMedia, this::selectMedia, this::logoutUser));
    }
}
