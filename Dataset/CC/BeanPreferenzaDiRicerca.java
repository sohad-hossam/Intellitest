package unisa.gps.etour.bean;
import java.io.Serializable;

/**
 * Bean which contains data search preferences
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class BeanSearchPreference implements Serializable {
    private static final long serialVersionUID = 7576354037868937929L;
    private int id;
    private String name;

    /**
     * Parameterized constructor
     *
     * @param id
     * @param name
     */
    public BeanSearchPreference(int id, String name) {
        setId(id);
        setName(name);
    }

    /**
     * Empty Constructor
     */
    public BeanSearchPreference() {

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
     * Returns the value of id
     *
     * @return value id.
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
