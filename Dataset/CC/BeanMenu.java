package unisa.gps.etour.bean;
import java.io.Serializable;

/**
 * Bean containing information relating to a Menu
 *
 * @Author Joseph Martone
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class BeanMenu implements Serializable {
    private static final long serialVersionUID = -3112032222839565409L;
    private int id;
    private String day;
    private int restaurantId;

    /**
     * Parameterized constructor
     *
     * @param pId               Menu ID
     * @param pDay              Day of the menu
     * @param pRestaurantId     ID of the restaurant associated with the menu
     */
    public BeanMenu(int pId, String pDay, int pRestaurantId) {
        setId(pId);
        setDay(pDay);
        setRestaurantId(pRestaurantId);
    }

    /**
     * Empty Constructor
     */
    public BeanMenu() {

    }

    /**
     * Returns the value of the day
     *
     * @return Value of the day.
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the new value of the day
     *
     * @param value New day value.
     */
    public void setDay(String pDay) {
        day = pDay;
    }

    /**
     * Returns the value of the menu ID
     *
     * @return Value of the menu ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the value of the restaurant ID
     *
     * @return Value of the restaurant ID.
     */
    public int getRestaurantId() {
        return restaurantId;
    }

    /**
     * Sets the new value of the menu ID
     *
     * @param pId New value for the menu ID.
     */
    public void setId(int pId) {
        id = pId;
    }

    /**
     * Sets the new value of the restaurant ID
     *
     * @param pRestaurantId New restaurant ID.
     */
    public void setRestaurantId(int pRestaurantId) {
        restaurantId = pRestaurantId;
    }
}