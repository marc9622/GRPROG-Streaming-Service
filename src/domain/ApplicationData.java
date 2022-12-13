package domain;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import domain.MediaParsing.InvalidStringFormatException;
import domain.UserList.UserAlreadyExistsException;
import domain.UserList.UserDoesNotExistException;

public class ApplicationData {

    private static final String FILE_PATH_MOVIES = "./Data/film.txt";
    private static final String FILE_PATH_SERIES = "./Data/serier.txt";
    private static final String FILE_PATH_MOVIES_IMAGES = "./Data/filmplakater/";
    private static final String FILE_PATH_SERIES_IMAGES = "./Data/serieforsider/";
    
    private final UserList users;
    private final MediaLibrary allMedia;

    public ApplicationData() {
        users = new UserList();
        allMedia = new MediaLibrary();
    }

    public void readMedia() throws IOException, InvalidStringFormatException {
        allMedia.readMediaFromFiles(FILE_PATH_MOVIES, FILE_PATH_SERIES, FILE_PATH_MOVIES_IMAGES, FILE_PATH_SERIES_IMAGES);
    }

    public Set<User> getUsers() {
        return users.getUsers();
    }

    public User getUser(String username) throws UserDoesNotExistException {
        return users.getUser(username);
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        users.addUser(user);
    }

    public void removeUser(User user) {
        users.removeUser(user);
    }

    public List<Media> getAllMedia() {
        return allMedia.getSortedBy(MediaSorting.SortBy.DEFAULT, MediaSorting.SortOrder.DEFAULT);
    }

    public List<Media> searchAllMedia(String query) {
        return allMedia.getSortedBySearch(query, true, true);
    }

}
