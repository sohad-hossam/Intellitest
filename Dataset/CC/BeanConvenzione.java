package unisa.gps.etour.bean;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean containing information relating to a Convention
 *
 * @Author Joseph Martone
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class BeanConvention implements Serializable {

    private static final long serialVersionUID = -3255500940680220001L;
    private int id;
    private int maxBanner;
    private Date startDate;
    private Date endDate;
    private double price;
    private boolean active;
    private int restaurantId;

    /**
     * Parameterized constructor
     *
     * @param pId
     * @param pMaxBanner
     * @param pStartDate
     * @param pEndDate
     * @param pPrice
     * @param pActive
     * @param pRestaurantId
     */
    public BeanConvention(int pId, int pMaxBanner, Date pStartDate,
                           Date pEndDate, double pPrice, boolean pActive,
                           int pRestaurantId) {
        setId(pId);
        setMaxBanner(pMaxBanner);
        setStartDate(pStartDate);
        setEndDate(pEndDate);
        setPrice(pPrice);
        setActive(pActive);
        setRestaurantId(pRestaurantId);
    }

    /**
     * Empty Constructor
     */
    public BeanConvention() {

    }

    /**
     * Returns the value of active
     *
     * @return value of active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the new value of active
     *
     * @param pActive new value of active.
     */
    public void setActive(boolean pActive) {
        active = pActive;
    }

    /**
     * Returns the value of endDate
     *
     * @return Value endDate.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the new value for endDate
     *
     * @param pEndDate New value for endDate.
     */
    public void setEndDate(Date pEndDate) {
        endDate = pEndDate;
    }

    /**
     * Returns the value of startDate
     *
     * @return value startDate.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the new value of startDate
     *
     * @param pStartDate new value pStartDate startDate.
     */
    public void setStartDate(Date pStartDate) {
        startDate = pStartDate;
    }

    /**
     * Returns the value of maxBanner
     *
     * @return value maxBanner.
     */
    public int getMaxBanner() {
        return maxBanner;
    }

    /**
     * Sets the new value of maxBanner
     *
     * @param pMaxBanner value pMaxBanner New maxBanner.
     */
    public void setMaxBanner(int pMaxBanner) {
        maxBanner = pMaxBanner;
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
     * @param pPrice new value for price.
     */
    public void setPrice(double pPrice) {
        price = pPrice;
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
     * Returns the value of restaurantId
     *
     * @return value restaurantId.
     */
    public int getRestaurantId() {
        return restaurantId;
    }

    /**
     * Sets the new value of id
     *
     * @param pId new value for id.
     */
    public void setId(int pId) {
        id = pId;
    }

    /**
     * Sets the new value of restaurantId
     *
     * @param pRestaurantId value pRestaurantId New restaurantId.
     */
    public void setRestaurantId(int pRestaurantId) {
        restaurantId = pRestaurantId;
    }
}