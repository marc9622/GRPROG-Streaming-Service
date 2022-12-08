package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserList {
    
    private Set<User> users;

    public UserList() {
        users = new HashSet<User>();
    }

    public List<User> getUsers() {
        return new ArrayList<User>(users);
    }

    public User getUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void removeUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
