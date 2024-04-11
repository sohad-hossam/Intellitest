package unisa.gps.etour.bean;
import java.io.Serializable;

/**
 * Bean containing information relating to food
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class BeanRestaurantOperator implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private int restaurantId;
    private static final long serialVersionUID = -6485826396352557404L;

    /**
     * Parameterized constructor
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param username
     * @param password
     * @param email
     * @param restaurantId
     */
    public BeanRestaurantOperator(int id, String firstName, String lastName,
                                  String username, String password, String email,
                                  int restaurantId) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRestaurantId(restaurantId);
    }

    /**
     * Empty Constructor
     */
    public BeanRestaurantOperator() {

    }

    /**
     * Returns the value of last name
     *
     * @return value of last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the new value of last name
     *
     * @param lastName New last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the value of email
     *
     * @return value of email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the new value of email
     *
     * @param email New value of email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the value of first name
     *
     * @return value of first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the new first name value
     *
     * @param firstName New first name value.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the value of password
     *
     * @return value of password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the new password value
     *
     * @param password New password value.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the value of username
     *
     * @return value of username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the new value of username
     *
     * @param username New value for username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the value of restaurantId
     *
     * @return value of restaurantId.
     */
    public int getRestaurantId() {
        return restaurantId;
    }

    /**
     * Sets the new value of restaurantId
     *
     * @param restaurantId New restaurantId.
     */
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    /**
     * Returns the value of id
     *
     * @return value of id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the new value of id
     *
     * @param id New value for id.
     */
    public void setId(int id) {
        this.id = id;
    }
}