package ubb.scs.map.domain.entities;

import java.security.MessageDigest;
import java.util.Objects;

public class User extends Entity<Long> {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private UserType userType;

    /**
     * Constructor for the User class
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     */
    public User(String userName, String firstName, String lastName, String password, String userType) {
        this.username = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = UserType.valueOf(userType.toUpperCase());
    }
    public User(Long id, String userName, String firstName, String lastName,
                String password, String userType) {
        this.username = userName;
        this.password = password;
        this.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = UserType.valueOf(userType.toUpperCase());
    }


    /**
     * Getter for the first name of the user
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for the first name of the user
     *
     * @param firstName the new first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for the last name of the user
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the last name of the user
     *
     * @param lastName the new last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Method that returns the string representation of the user
     *
     * @return the string representation of the user
     */


    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method that checks if two users are equal
     *
     * @param o the object to be compared to
     * @return true if the users are equal, false otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return false;
    }

    public boolean isAdmin(){
        return userType.equals(UserType.ADMIN);
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userType=" + userType +
                '}';
    }

    /**
     * Method that returns the hash code of the user
     *
     * @return the hash code of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}