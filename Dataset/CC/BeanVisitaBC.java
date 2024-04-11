package unisa.gps.etour.bean;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean containing information relating to the feedback of a cultural visit
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class BeanCulturalVisitFeedback implements Serializable {
    private static final long serialVersionUID = 3331567128449243852L;
    private int rating;
    private int culturalAssetId;
    private String comment;
    private int touristId;
    private Date visitDate;

    /**
     * Parameterized constructor
     *
     * @param rating
     * @param culturalAssetId
     * @param comment
     * @param touristId
     * @param visitDate
     */
    public BeanCulturalVisitFeedback(int rating, int culturalAssetId,
                                     String comment, int touristId, Date visitDate) {
        setRating(rating);
        setCulturalAssetId(culturalAssetId);
        setComment(comment);
        setTouristId(touristId);
        setVisitDate(visitDate);
    }

    /**
     * Empty Constructor
     */
    public BeanCulturalVisitFeedback() {

    }

    /**
     * Returns the value of comment
     *
     * @return value of comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the new value of comment
     *
     * @param comment New value for comment.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the value of visitDate
     *
     * @return value of visitDate.
     */
    public Date getVisitDate() {
        return visitDate;
    }

    /**
     * Sets the new value of visitDate
     *
     * @param visitDate New visitDate value.
     */
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    /**
     * Returns the value of culturalAssetId
     *
     * @return value of culturalAssetId.
     */
    public int getCulturalAssetId() {
        return culturalAssetId;
    }

    /**
     * Sets the new value of culturalAssetId
     *
     * @param culturalAssetId New culturalAssetId value.
     */
    public void setCulturalAssetId(int culturalAssetId) {
        this.culturalAssetId = culturalAssetId;
    }

    /**
     * Returns the value of touristId
     *
     * @return value of touristId.
     */
    public int getTouristId() {
        return touristId;
    }

    /**
     * Sets the new value of touristId
     *
     * @param touristId New touristId value.
     */
    public void setTouristId(int touristId) {
        this.touristId = touristId;
    }

    /**
     * Returns the value of rating
     *
     * @return value of rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the new value of rating
     *
     * @param rating New rating value.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }
}
