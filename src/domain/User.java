package domain;

import java.util.List;

public class User implements Comparable<User> {
    
    /** The user's name. */
    private final String username;

    /** The user's password. (Encrypted) */
    private final String password;

    /** A library of the users favorite media. */
    private final MediaLibrary favorites;

    public User(String username, String password) {
        this.username = username;
        this.password = XOREncryption.encrypt(password);
        this.favorites = new MediaLibrary();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(XOREncryption.encrypt(password));
    }

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

}
