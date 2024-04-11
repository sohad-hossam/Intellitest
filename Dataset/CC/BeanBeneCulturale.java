package unisa.gps.etour.bean;

/**
 * Bean containing information relating to a cultural heritage
 *
 * @Author Mauro Miranda
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */

import java.io.Serializable;
import java.util.Date;

import unisa.gps.etour.util.Punto3D;

public class CulturalHeritageBean implements Serializable {

    private static final long serialVersionUID = -460705346474984466L;

    private int id;
    private int voteNumber;
    private String name;
    private String city;
    private String phone;
    private String description;
    private String location;
    private String street;
    private String postalCode;
    private String province;
    private String closingDay;
    private Punto3D position;
    private Date openingTime;
    private Date closingTime;
    private double ticketCost;
    private double averageVotes;

    /**
     * Parameterized constructor
     *
     * @param pId
     * @param pVoteNumber
     * @param pName
     * @param pCity
     * @param pPhone
     * @param pDescription
     * @param pLocation
     * @param pStreet
     * @param pPostalCode
     * @param pProvince
     * @param pPosition
     * @param pOpeningTime
     * @param pClosingTime
     * @param pClosingDay
     * @param pTicketCost
     * @param pAverageVotes
     */
    public CulturalHeritageBean(int pId, int pVoteNumber, String pName, String pCity, String pPhone, String pDescription,
                                String pLocation, String pStreet, String pPostalCode, String pProvince, Punto3D pPosition,
                                Date pOpeningTime, Date pClosingTime, String pClosingDay, double pTicketCost, double pAverageVotes) {
        setId(pId);
        setVoteNumber(pVoteNumber);
        setName(pName);
        setCity(pCity);
        setPhone(pPhone);
        setDescription(pDescription);
        setLocation(pLocation);
        setStreet(pStreet);
        setPostalCode(pPostalCode);
        setProvince(pProvince);
        setPosition(pPosition);
        setOpeningTime(pOpeningTime);
        setClosingTime(pClosingTime);
        setClosingDay(pClosingDay);
        setTicketCost(pTicketCost);
        setAverageVotes(pAverageVotes);
    }

    /**
     * Empty Constructor
     */
    public CulturalHeritageBean() {

    }

    /**
     * Returns the value of closingDay
     *
     * @return value of closingDay.
     */
    public String getClosingDay() {
        return closingDay;
    }

    /**
     * Sets the new value of closingDay
     *
     * @param pClosingDay New closingDay.
     */
    public void setClosingDay(String pClosingDay) {
        closingDay = pClosingDay;
    }


/**
 * Returns the value of postalCode
 *
 * @return value of postalCode.
 */
public String getPostalCode() {
    return postalCode;
}

/**
 * Sets the new value of postalCode
 *
 * @param pPostalCode New postalCode.
 */
public void setPostalCode(String pPostalCode) {
    postalCode = pPostalCode;
}

/**
 * Returns the value of city
 *
 * @return Value of city.
 */
public String getCity() {
    return city;
}

/**
 * Sets the new value of city
 *
 * @param pCity New city value.
 */
public void setCity(String pCity) {
    city = pCity;
}

/**
 * Returns the value of ticketCost
 *
 * @return value of ticketCost.
 */
public double getTicketCost() {
    return ticketCost;
}

/**
 * Sets the new value of ticketCost
 *
 * @param pTicketCost New ticketCost.
 */
public void setTicketCost(double pTicketCost) {
    ticketCost = pTicketCost;
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
 * @param pDescription New value of description.
 */
public void setDescription(String pDescription) {
    description = pDescription;
}

/**
 * Returns the value of location
 *
 * @return Locale values.
 */
public String getLocation() {
    return location;
}

/**
 * Sets the new value of location
 *
 * @param pLocation New locale values.
 */
public void setLocation(String pLocation) {
    location = pLocation;
}

/**
 * Returns the value of averageVotes
 *
 * @return value of averageVotes.
 */
public double getAverageVotes() {
    return averageVotes;
}

/**
 * Sets the new value of averageVotes
 *
 * @param pAverageVotes New averageVotes.
 */
public void setAverageVotes(double pAverageVotes) {
    averageVotes = pAverageVotes;
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
 * @param pName New value for name.
 */
public void setName(String pName) {
    name = pName;
}

/**
 * Returns the value of voteNumber
 *
 * @return value of voteNumber.
 */
public int getVoteNumber() {
    return voteNumber;
}

/**
 * Sets the new value of voteNumber
 *
 * @param pVoteNumber New voteNumber.
 */
public void setVoteNumber(int pVoteNumber) {
    voteNumber = pVoteNumber;
}

/**
 * Returns the value of openingTime
 *
 * @return value of openingTime.
 */
public Date getOpeningTime() {
    return openingTime;
}

/**
 * Sets the new value of openingTime
 *
 * @param pOpeningTime New openingTime.
 */
public void setOpeningTime(Date pOpeningTime) {
    openingTime = pOpeningTime;
}

/**
 * Returns the value of closingTime
 *
 * @return value of closingTime.
 */
public Date getClosingTime() {
    return closingTime;
}

/**
 * Sets the new value of closingTime
 *
 * @param pClosingTime New closingTime.
 */
public void setClosingTime(Date pClosingTime) {
    closingTime = pClosingTime;
}

/**
 * Returns the value of position
 *
 * @return value of position.
 */
public Punto3D getPosition() {
    return position;
}

/**
 * Sets the new position value
 *
 * @param pPosition New position value.
 */
public void setPosition(Punto3D pPosition) {
    position = pPosition;
}

/**
 * Returns the value of province
 *
 * @return value of province.
 */
public String getProvince() {
    return province;
}

/**
 * Sets the new value of province
 *
 * @param pProvince New value for province.
 */
public void setProvince(String pProvince) {
    province = pProvince;
}

/**
 * Returns the value of phone
 *
 * @return Value of phone.
 */
public String getPhone() {
    return phone;
}

/**
 * Sets the new value of phone
 *
 * @param pPhone New phone value.
 */
public void setPhone(String pPhone) {
    phone = pPhone;
}

/**
 * Returns the value of street
 *
 * @return value of street.
 */
public String getStreet() {
    return street;
}

/**
 * Sets the new value of street
 *
 * @param pStreet New value for street.
 */
public void setStreet(String pStreet) {
    street = pStreet;
}

/**
 * Returns the value of id
 *
 * @return value of id.
 */
public int getId() {
    return id;
}

/**
 * Sets the new value of id
 *
 * @param pId New value for id.
 */
public void setId(int pId) {
    id = pId;
}
}