package domain;

public class User implements Comparable<User> {
    
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Compares this user to the given user.
     * @param user The user to compare.
     * @return A negative integer, zero, or a positive integer
     * as this user is less than, equal to, or greater than the given user.
     */
    public int compareTo(User user) {
        return username.compareTo(user.username);
    }

}
