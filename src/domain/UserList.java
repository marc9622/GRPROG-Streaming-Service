package domain;

import java.util.HashSet;
import java.util.Set;

public class UserList {
    
    private Set<User> users;

    public UserList() {
        users = new HashSet<User>();
    }

    public Set<User> getUsers() {
        return new HashSet<User>(users);
    }

    public User getUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addUser(User user) throws UserAlreadyExistsException {
        if(users.contains(user))
            throw new UserAlreadyExistsException("User already exists");

        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void removeUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

}
