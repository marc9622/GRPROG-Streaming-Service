package domain;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.ObjectSaving;
import data.ObjectSaving.Saveable;

/** A set of all users.
 * <p> Users are identified by their name,
 * and two users are considered equal if they have the same name,
 * which means that no two users can have the same name.
 * @see User
*/
public class UserSet implements Saveable {
    
    /** A set of all users. */
    private Set<User> users;

    /** Creates a new empty user set. */
    UserSet() {
        users = new HashSet<User>();
    }

    /** Loads a saved userset from a file.
     * @param folderName The name of the folder to load from.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     * @throws IOException If an I/O error occurs.
     */
    static UserSet loadUserSet(String folderName) throws ClassNotFoundException, IOException {
        return ObjectSaving.loadFromFile(UserSet.class, folderName);
    }

    /** Loads a saved userset and adds its users to this userset.
     * @param folderName The name of the folder to load from.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     * @throws IOException If an I/O error occurs.
     */
    void loadUsersAndAdd(String folderName) throws ClassNotFoundException, IOException {
        UserSet loadedUsers = loadUserSet(folderName);
        users.addAll(loadedUsers.users);
    }

    /** Returns an unmodifiable list of all users.
     * @return An unmodifiable list of all users.
     */
    List<User> getUsers() {
        return List.copyOf(users);
    }

    /** Returns the user with the given name.
     * @param username The name of the user.
     * @return The user with the given name.
     * @throws UserDoesNotExistException If the user does not exist.
     */
    User getUser(String username) throws UserDoesNotExistException {
        User user = users.stream()
                         .filter(u -> u.getUsername().equals(username))
                         .findFirst()
                         .orElse(null);
        
        if(user == null)
            throw new UserDoesNotExistException("User does not exist");

        return user;
    }

    /** Adds the given user to the set.
     * @param user The user to add.
     * @throws UserAlreadyExistsException If the user already exists.
     */
    void addUser(User user) throws UserAlreadyExistsException {
        if(users.contains(user))
            throw new UserAlreadyExistsException("User already exists");

        users.add(user);
    }

    /** Removes the given user from the set.
     * @param user The user to remove.
     * @return Whether the user was removed.
     */
    boolean removeUser(User user) throws UserDoesNotExistException {
        if(!users.contains(user))
            throw new UserDoesNotExistException("User does not exist");

        return users.remove(user);
    }

    /** Removes the user with the given name from the set.
     * @param username The name of the user to remove.
     * @return Whether the user was removed.
     */
    boolean removeUser(String username) throws UserDoesNotExistException {
        if(!users.removeIf(u -> u.getUsername().equals(username)))
            throw new UserDoesNotExistException("User does not exist");
        return true;
    }

    public int hashCode() {
        return users.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof UserSet))
            return false;

        UserSet other = (UserSet) obj;
        return users.equals(other.users);
    }

    /** A user already exists exception. */
    public class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    /** A user does not exist exception. */
    public class UserDoesNotExistException extends Exception {
        public UserDoesNotExistException(String message) {
            super(message);
        }
    }

}
