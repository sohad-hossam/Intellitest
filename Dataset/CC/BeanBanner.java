package unisa.gps.etour.bean;

/**
 * Bean which contains data on the Banner
 *
 * @Author Mauro Miranda
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */

import java.io.Serializable;

public class BeanBanner implements Serializable {
    private static final long serialVersionUID = -872783211721655763L;
    private int id;
    private int restaurantId;
    private String filepath;

    /**
     * Parameterized constructor
     *
     * @param pId            - Banner ID
     * @param pFilePath      - File path for the banner
     * @param pRestaurantId  - Restaurant ID associated with the banner
     */
    public BeanBanner(int pId, String pFilePath, int pRestaurantId) {
        setId(pId);
        setFilePath(pFilePath);
        setRestaurantId(pRestaurantId);
    }

    /**
     * Empty Constructor
     */
    public BeanBanner() {

    }

    /**
     * Returns the value of FilePath
     *
     * @return Value of FilePath.
     */
    public String getFilePath() {
        return filepath;
    }

    /**
     * Sets the new value of FilePath
     *
     * @param pFilePath New value for FilePath.
     */
    public void setFilePath(String pFilePath) {
        filepath = pFilePath;
    }

    /**
     * Returns the value of id
     *
     * @return Value of id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the value of restaurantId
     *
     * @return Value of restaurantId.
     */
    public int getRestaurantId() {
        return restaurantId;
    }

    /**
     * Sets the new value of id
     *
     * @param pId New value for id.
     */
    public void setId(int pId) {
        id = pId;
    }

    /**
     * Sets the new value of restaurantId
     *
     * @param pRestaurantId New restaurantId.
     */
    public void setRestaurantId(int pRestaurantId) {
        restaurantId = pRestaurantId;
    }
}
