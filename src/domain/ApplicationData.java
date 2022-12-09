package domain;

import java.util.List;

public class ApplicationData {
    
    private UserList users;
    private MediaLibrary allMedia;

    public ApplicationData() {
        users = new UserList();
        allMedia = new MediaLibrary();
    }

    public List<User> getUsers() {
        return users.getUsers();
    }

    public User getUser(String username) {
        return users.getUser(username);
    }

    public void addUser(User user) {
        users.addUser(user);
    }

    public void removeUser(User user) {
        users.removeUser(user);
    }

    public List<Media> getAllMedia() {
        return allMedia.sortBy(MediaSorting.SortBy.DEFAULT, MediaSorting.SortOrder.DEFAULT);
    }

    public List<Media> searchAllMedia(String query) {
        return allMedia.sortBySearch(query, true, true);
    }

}
