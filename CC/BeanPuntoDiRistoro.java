package unisa.gps.etour.bean;

import java.io.Serializable;
import java.util.Date;

import unisa.gps.etour.util.Point3D;

/**
 * Bean for storing refreshment data.
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University Of Salerno
 */
public class RefreshmentPointBean implements Serializable {
    private static final long serialVersionUID = 8417686685147484931L;
    private int id;
    private int numberOfVotes;
    private double averageVotes;
    private String name;
    private String description;
    private String phone;
    private String location;
    private String city;
    private String street;
    private String postalCode;
    private String province;
    private String vatNumber;
    private Point3D position;
    private Date openingTime;
    private Date closingTime;
    private String closingDay;

    /**
     * Parameterized constructor.
     *
     * @param id
     * @param numberOfVotes
     * @param averageVotes
     * @param name
     * @param description
     * @param phone
     * @param location
     * @param city
     * @param street
     * @param postalCode
     * @param province
     * @param vatNumber
     * @param position
     * @param openingTime
     * @param closingTime
     * @param closingDay
     */
    public RefreshmentPointBean(int id, int numberOfVotes, double averageVotes, String name, String description,
                                String phone, String location, String city, String street, String postalCode,
                                String province, String vatNumber, Point3D position, Date openingTime,
                                Date closingTime, String closingDay) {
        setId(id);
        setNumberOfVotes(numberOfVotes);
        setAverageVotes(averageVotes);
        setName(name);
        setDescription(description);
        setPhone(phone);
        setLocation(location);
        setCity(city);
        setStreet(street);
        setPostalCode(postalCode);
        setProvince(province);
        setVatNumber(vatNumber);
        setPosition(position);
        setOpeningTime(openingTime);
        setClosingTime(closingTime);
        setClosingDay(closingDay);
    }

    /**
     * Empty Constructor.
     */
    public RefreshmentPointBean() {
    }

    /**
     * Returns the value of postalCode.
     *
     * @return postalCode value.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the new value of postalCode.
     *
     * @param postalCode New postalCode value.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

/**
 * Returns the value of city.
 *
 * @return City value.
 */
public String getCity() {
    return city;
}

/**
 * Sets the new value of city.
 *
 * @param city New city value.
 */
public void setCity(String city) {
    this.city = city;
}

/**
 * Returns the value of description.
 *
 * @return Description value.
 */
public String getDescription() {
    return description;
}

/**
 * Sets the new value of description.
 *
 * @param description New description value.
 */
public void setDescription(String description) {
    this.description = description;
}

/**
 * Returns the value of closingDay.
 *
 * @return Closing day value.
 */
public String getClosingDay() {
    return closingDay;
}

/**
 * Sets the new value of closingDay.
 *
 * @param closingDay New closing day value.
 */
public void setClosingDay(String closingDay) {
    this.closingDay = closingDay;
}

/**
 * Returns the value of location.
 *
 * @return Location value.
 */
public String getLocation() {
    return location;
}

/**
 * Sets the new value of location.
 *
 * @param location New location value.
 */
public void setLocation(String location) {
    this.location = location;
}

/**
 * Returns the value of averageVotes.
 *
 * @return Average votes value.
 */
public double getAverageVotes() {
    return averageVotes;
}

/**
 * Sets the new value of averageVotes.
 *
 * @param averageVotes New average votes value.
 */
public void setAverageVotes(double averageVotes) {
    this.averageVotes = averageVotes;
}
/**
 * Returns the value of name.
 *
 * @return Name value.
 */
public String getName() {
    return name;
}

/**
 * Sets the new name value.
 *
 * @param name New name value.
 */
public void setName(String name) {
    this.name = name;
}

/**
 * Returns the value of numberOfVotes.
 *
 * @return Number of votes value.
 */
public int getNumberOfVotes() {
    return numberOfVotes;
}

/**
 * Sets the new value of numberOfVotes.
 *
 * @param numberOfVotes New number of votes value.
 */
public void setNumberOfVotes(int numberOfVotes) {
    this.numberOfVotes = numberOfVotes;
}

/**
 * Returns the value of openingTime.
 *
 * @return Opening time value.
 */
public Date getOpeningTime() {
    return openingTime;
}

/**
 * Sets the new value of openingTime.
 *
 * @param openingTime New opening time value.
 */
public void setOpeningTime(Date openingTime) {
    this.openingTime = openingTime;
}

/**
 * Returns the value of closingTime.
 *
 * @return Closing time value.
 */
public Date getClosingTime() {
    return closingTime;
}

/**
 * Sets the new value of closingTime.
 *
 * @param closingTime New closing time value.
 */
public void setClosingTime(Date closingTime) {
    this.closingTime = closingTime;
}

/**
 * Returns the value of vatNumber.
 *
 * @return VAT number value.
 */
public String getVatNumber() {
    return vatNumber;
}

/**
 * Sets the new value of vatNumber.
 *
 * @param vatNumber New VAT number value.
 */
public void setVatNumber(String vatNumber) {
    this.vatNumber = vatNumber;
}

/**
 * Returns the value of position.
 *
 * @return Position value.
 */
public Point3D getPosition() {
    return position;
}
/**
 * Sets the new position value.
 *
 * @param newPosition New position value.
 */
public void setPosition(Point3D newPosition) {
    position = newPosition;
}

/**
 * Returns the value of province.
 *
 * @return Province value.
 */
public String getProvince() {
    return province;
}

/**
 * Sets the new value of province.
 *
 * @param newProvince New value for the province.
 */
public void setProvince(String newProvince) {
    province = newProvince;
}

/**
 * Returns the value of telephone.
 *
 * @return Telephone value.
 */
public String getTelephone() {
    return telephone;
}

/**
 * Sets the new value of telephone.
 *
 * @param newTelephone New telephone value.
 */
public void setTelephone(String newTelephone) {
    telephone = newTelephone;
}

/**
 * Returns the value of street.
 *
 * @return Street value.
 */
public String getStreet() {
    return street;
}

/**
 * Sets the new value of street.
 *
 * @param newStreet New street value.
 */
public void setStreet(String newStreet) {
    street = newStreet;
}

/**
 * Returns the value of ID.
 *
 * @return ID value.
 */
public int getId() {
    return id;
}

/**
 * Sets the new value of ID.
 *
 * @param newId New value for ID.
 */
public void setId(int newId) {
    id = newId;
}
}