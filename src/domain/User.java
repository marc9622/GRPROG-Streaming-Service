package domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import data.ObjectSaving.Saveable;

public class User implements Comparable<User>, Saveable {
    
    /** The user's name. */
    private final String username;

    /** The user's password. (Encrypted)
     * @see XOREncryption
    */
    private final String password;

    /** The user's profile picture. */
    private final Optional<String> imagePath;

    /** A library of the users favorite media. */
    private final MediaLibrary favorites;

    /** Creates a new user with the given name and password.
     * @param username The user's name.
     * @param password The user's password.
     * @throws InvalidUsernameException
     * @throws InvalidPasswordException
     */
    public User(String username, String password, String imagePath) throws InvalidUsernameException, InvalidPasswordException, InvalidImagePathException {
        Objects.requireNonNull(username, "The username cannot be null.");
        Objects.requireNonNull(password, "The password cannot be null.");

        // Username must not be blank
        if(username.isBlank())
            throw new InvalidUsernameException("The username cannot be empty.");

        // Username must only contain letters, numbers and spaces
        if(username.matches("[^a-zA-Z0-9 ]"))
            throw new InvalidUsernameException("The username can only contain letters, numbers and spaces.");

        // Username must not be longer than 20 characters
        if(username.length() > 20)
            throw new InvalidUsernameException("The username cannot be longer than 20 characters.");

        // Password must not be blank
        if(password.isBlank())
            throw new InvalidPasswordException("The password cannot be empty.");

        // Password must not be longer than 30 characters
        if(password.length() > 30)
            throw new InvalidPasswordException("The password cannot be longer than 30 characters.");

        this.username = username;
        this.password = XOREncryption.encrypt(password);
        this.imagePath = Optional.ofNullable(imagePath);
        this.favorites = new MediaLibrary();
    }

    /** Returns the user's name.
     * @return The user's name.
     */
    public String getUsername() {
        return username;
    }

    /** Checks whether the given password is correct.
     * @param password The password to check.
     * @return Whether the given password is correct.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(XOREncryption.encrypt(password));
    }

    /** Returns the user's profile picture.
     * The path is stored in an optional, so it may be empty.
     * @return The user's profile picture.
     */
    public Optional<String> getImagePath() {
        return imagePath;
    }

    /** A list of the user's favorites library.
     * @return A list of the user's favorites library.
     */
    public List<Media> getFavorites() {
        return favorites.getSortedByDefault();
    }

    /** Adds the given media to the user's favorites library.
     * @param media The media to add.
     * @return Whether the media was added.
     */
    public void addFavorite(Media media) {
        favorites.add(media);
    }

    /** Removes the given media from the user's favorites library.
     * @param media The media to remove.
     * @return Whether the media was removed.
     */
    public void removeFavorite(Media media) {
        favorites.remove(media);
    }

    /** Returns whether the given media is in the user's favorites library.
     * @param media The media to check.
     * @return Whether the given media is in the user's favorites library.
     */
    public boolean isFavorite(Media media) {
        return favorites.contains(media);
    }

    /** Compares this user to the given user.
     * @param user The user to compare.
     * @return A negative integer, zero, or a positive integer
     * as this user is less than, equal to, or greater than the given user.
     */
    public int compareTo(User user) {
        return username.compareTo(user.username);
    }

    /** Returns whether the given object is equal to this user.
     * @param obj The object to compare.
     * @return Whether the given object is equal to this user.
     */
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(obj == this)
            return true;

        if(!(obj instanceof User))
            return false;

        User other = (User) obj;
        return this.username.equals(other.username);
    }

    public int hashCode() {
        return username.hashCode();
    }

    /** Returns a string representation of the user.
     * @return The user's name.
     */
    public String toString() {
        return username;
    }

    public class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }

    public class InvalidPasswordException extends Exception {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }

    public class InvalidImagePathException extends Exception {
        public InvalidImagePathException(String message) {
            super(message);
        }
    }
}
