package unisa.gps.etour.bean;

/**
 * Bean containing information related to the News
 *
 * @Author Mauro Miranda
 * @Version 0.1 2007 eTour Project - Copyright by SE@SALab DMI University
 * of Salerno
 */

import java.io.Serializable;
import java.util.Date;

public class BeanNews implements Serializable {
    private String news;
    private Date publicationDate;
    private Date expirationDate;
    private int priority;
    private int id;
    private static final long serialVersionUID = -6249491056436689386L;

    /**
     * Parameterized constructor
     *
     * @param news             News
     * @param publicationDate Date of publication
     * @param expirationDate   Expiration date
     * @param priority         Priority
     * @param id               ID
     */
    public BeanNews(String news, Date publicationDate, Date expirationDate, int priority, int id) {
        setNews(news);
        setPublicationDate(publicationDate);
        setExpirationDate(expirationDate);
        setPriority(priority);
        setId(id);
    }

    /**
     * Empty Constructor
     */
    public BeanNews() {

    }

    /**
     * Returns the value of publicationDate
     *
     * @return Publication date value.
     */
    public Date getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the new value of publicationDate
     *
     * @param publicationDate New publication date.
     */
    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * Returns the value of expirationDate
     *
     * @return Expiration date value.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the new value of expirationDate
     *
     * @param expirationDate New expiration date.
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Returns the value of news
     *
     * @return News value.
     */
    public String getNews() {
        return news;
    }

    /**
     * Sets the new value of news
     *
     * @param news New news value.
     */
    public void setNews(String news) {
        this.news = news;
    }

    /**
     * Returns the priority value
     *
     * @return The priority value.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set the new priority value
     *
     * @param priority New priority value.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the value of id
     *
     * @return ID value.
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