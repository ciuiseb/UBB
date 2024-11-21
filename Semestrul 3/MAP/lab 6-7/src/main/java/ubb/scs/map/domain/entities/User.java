package ubb.scs.map.domain.entities;

import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;

    /**
     * Constructor for the User class
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public User(Long id, String firstName, String lastName) {
        this.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
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
    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
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
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
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