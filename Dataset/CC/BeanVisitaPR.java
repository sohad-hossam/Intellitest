package unisa.gps.etour.bean;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean that contains the data for feedback to a refreshment point
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class BeanRefreshmentFeedback implements Serializable {
    private static final long serialVersionUID = -4065240072283418782L;
    private int rating;
    private int refreshmentPointId;
    private String comment;
    private int touristId;
    private Date visitDate;

    /**
     * Parameterized constructor
     *
     * @param rating
     * @param refreshmentPointId
     * @param comment
     * @param touristId
     * @param visitDate
     */
    public BeanRefreshmentFeedback(int rating, int refreshmentPointId,
                                   String comment, int touristId, Date visitDate) {
        setRating(rating);
        setRefreshmentPointId(refreshmentPointId);
        setComment(comment);
        setTouristId(touristId);
        setVisitDate(visitDate);
    }

    /**
     * Empty Constructor
     */
    public BeanRefreshmentFeedback() {

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
     * Returns the value of refreshmentPointId
     *
     * @return value of refreshmentPointId.
     */
    public int getRefreshmentPointId() {
        return refreshmentPointId;
    }

    /**
     * Sets the new value of refreshmentPointId
     *
     * @param refreshmentPointId New refreshmentPointId value.
     */
    public void setRefreshmentPointId(int refreshmentPointId) {
        this.refreshmentPointId = refreshmentPointId;
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