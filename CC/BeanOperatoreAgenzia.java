package unisa.gps.etour.bean;
import java.io.Serializable;

/**
 * Bean containing information relating to an Agency Operator
 *
 * @Author Joseph Martone
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class BeanAgencyOperator implements Serializable {

    private static final long serialVersionUID = -3489147679484477440L;
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;

    /**
     * Parameterized constructor
     *
     * @param id        Operator ID
     * @param username  Operator username
     * @param firstName Operator first name
     * @param lastName  Operator last name
     * @param password  Operator password
     */
    public BeanAgencyOperator(int id, String username, String firstName, String lastName, String password) {
        setId(id);
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
    }

    /**
     * Empty Constructor
     */
    public BeanAgencyOperator() {

    }

    /**
     * Returns the value of last name
     *
     * @return Last name value.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the new value of last name
     *
     * @param lastName New last name value.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the value of first name
     *
     * @return First name value.
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
     * @return Password value.
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
     * @return Username value.
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
     * Sets the new value of id
     *
     * @param pid New value of id.
     */
    public void setId(int pid) {
        id = pid;
    }

    /**
     * Returns the value of id
     *
     * @return ID value.
     */
    public int getId() {
        return id;
    }
}
