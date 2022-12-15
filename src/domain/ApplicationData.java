package domain;

import java.io.IOException;
import java.util.List;

import domain.MediaParsing.InvalidStringFormatException;
import domain.UserSet.UserAlreadyExistsException;
import domain.UserSet.UserDoesNotExistException;

public class ApplicationData {

    private static final String FILE_PATH_MOVIES = "./Data/film.txt";
    private static final String FILE_PATH_SERIES = "./Data/serier.txt";
    private static final String FILE_PATH_MOVIES_IMAGES = "./Data/filmplakater/";
    private static final String FILE_PATH_SERIES_IMAGES = "./Data/serieforsider/";

    private static final String FILE_NAME_USERLIST = "allUsers";
    
    private final UserSet users;
    private final MediaLibrary allMedia;

    public ApplicationData() {
        users = new UserSet();
        allMedia = new MediaLibrary();
    }

    public void loadUsers() throws IOException, ClassNotFoundException {
        users.loadUsersAndAdd(FILE_NAME_USERLIST);
    }

    public void saveUsers() throws IOException {
        users.saveToFile(FILE_NAME_USERLIST);
    }

    public void readMedia() throws IOException, InvalidStringFormatException {
        try {
            allMedia.readMediaFromFiles(FILE_PATH_MOVIES, FILE_PATH_SERIES, FILE_PATH_MOVIES_IMAGES, FILE_PATH_SERIES_IMAGES);
        }
        catch(InvalidStringFormatException e) {
            throw new InvalidStringFormatException(e.errorDescription, e.invalidStrings[0]);
        }
    }

    public List<User> getUsers() {
        return users.getUsers();
    }

    public User getUser(String username) throws UserDoesNotExistException {
        return users.getUser(username);
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        users.addUser(user);
    }

    public boolean removeUser(String username) throws UserDoesNotExistException {
        return users.removeUser(username);
    }

    public List<Media> getAllMedia() {
        return allMedia.getSortedBy(MediaSorting.SortBy.DEFAULT, MediaSorting.SortOrder.DEFAULT);
    }

    public List<Media> searchAllMedia(String query) {
        return allMedia.getSortedBySearch(query, true, true);
    }

}
