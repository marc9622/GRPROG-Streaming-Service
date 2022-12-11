package domain;

public class User implements Comparable<User> {
    
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = XOREncryption.encrypt(password);
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(XOREncryption.encrypt(password));
    }

    /** Compares this user to the given user.
     * @param user The user to compare.
     * @return A negative integer, zero, or a positive integer
     * as this user is less than, equal to, or greater than the given user.
     */
    public int compareTo(User user) {
        return username.compareTo(user.username);
    }

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

}
