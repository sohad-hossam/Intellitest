package unisa.gps.etour.bean;
import java.io.Serializable;

/**
 * Bean containing information relating to food
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */

public class PlateBean implements Serializable {
    private int id;
    private String name;
    private double price;
    private int idMenu;
    private static final long serialVersionUID = -3775462843748984482L;

    /**
     * Parameterized constructor
     *
     * @param id
     * @param name
     * @param price
     * @param idMenu
     */
    public PlateBean(int id, String name, double price, int idMenu) {
        setId(id);
        setName(name);
        setPrice(price);
        setIdMenu(idMenu);
    }

    /**
     * Empty Constructor
     */
    public PlateBean() {

    }

    /**
     * Returns the value of name
     *
     * @return value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the new name value
     *
     * @param name New value for name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of price
     *
     * @return value price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the new value of price
     *
     * @param price New value for price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the value of id
     *
     * @return value id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the value of idMenu
     *
     * @return value idMenu.
     */
    public int getIdMenu() {
        return idMenu;
    }

    /**
     * Sets the new value of id
     *
     * @param id New value for id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the new value of idMenu
     *
     * @param idMenu New idMenu.
     */
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }
}
