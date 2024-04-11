package unisa.gps.etour.bean;

/**
 * Bean that contains the data for a Tag
 *
 * @ Author Mauro Miranda
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */

import java.io.Serializable;

public class BeanTag implements Serializable {
    private static final long serialVersionUID = -6320421006595188597L;
    private int id;
    private String name;
    private String description;

    /**
     * Parameterized constructor
     *
     * @param id
     * @param name
     * @param description
     */
    public BeanTag(int id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    /**
     * Empty Constructor
     */
    public BeanTag() {

    }

    /**
     * Returns the value of description
     *
     * @return value of description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the new value of description
     *
     * @param description New value of description.
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Sets the new value of name
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
