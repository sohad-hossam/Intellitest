package unisa.gps.etour.bean;
import java.io.Serializable;
import java.util.Date;

/**
 * Bean containing information relating to a tourist.
 *
 * @ Author Mauro Miranda
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class TouristBean implements Serializable {
    private static final long serialVersionUID = 4214744963263090577L;
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String birthCity;
    private String residenceCity;
    private String phone;
    private String postalCode;
    private String street;
    private String province;
    private String email;
    private String password;
    private Date dob;
    private Date registrationDate;
    private boolean active;

    /**
     * Parameterized constructor.
     *
     * @param id
     * @param username
     * @param firstName
     * @param lastName
     * @param birthCity
     * @param residenceCity
     * @param phone
     * @param postalCode
     * @param street
     * @param province
     * @param email
     * @param password
     * @param dob
     * @param registrationDate
     * @param active
     */
    public TouristBean(int id, String username, String firstName, String lastName,
                       String birthCity, String residenceCity, String phone,
                       String postalCode, String street, String province,
                       String email, String password, Date dob,
                       Date registrationDate, boolean active) {
        setId(id);
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setBirthCity(birthCity);
        setResidenceCity(residenceCity);
        setPhone(phone);
        setPostalCode(postalCode);
        setStreet(street);
        setProvince(province);
        setEmail(email);
        setPassword(password);
        setDob(dob);
        setRegistrationDate(registrationDate);
        setActive(active);
    }

    /**
     * Empty Constructor.
     */
    public TouristBean() {
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
     * @param postalCode new value.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Returns the value of birthCity.
     *
     * @return birthCity value.
     */
    public String getBirthCity() {
        return birthCity;
    }

    /**
     * Sets the new value of birthCity.
     *
     * @param birthCity new value.
     */
    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }
/**
 * Returns the value of dob.
 *
 * @return dob value.
 */
public String getDob() {
    return dob;
}

/**
 * Sets the new value of dob.
 *
 * @param birthCity New dob value.
 */
public void setDob(Date dob) {
    this.dob = dob;
}

/**
 * Returns the value of residenceCity.
 *
 * @return residenceCity value.
 */
public String getResidenceCity() {
    return residenceCity;
}

/**
 * Sets the new value of residenceCity.
 *
 * @param residenceCity New residenceCity value.
 */
public void setResidenceCity(String residenceCity) {
    this.residenceCity = residenceCity;
}

/**
 * Returns the value of lastName.
 *
 * @return lastName value.
 */
public String getLastName() {
    return lastName;
}

/**
 * Sets the new value of lastName.
 *
 * @param lastName New lastName value.
 */
public void setLastName(String lastName) {
    this.lastName = lastName;
}

/**
 * Returns the value of dateOfBirth.
 *
 * @return dateOfBirth value.
 */
public Date getDateOfBirth() {
    return dob;
}

/**
 * Sets the new value of dateOfBirth.
 *
 * @param dateOfBirth New dateOfBirth value.
 */
public void setDateOfBirth(Date dateOfBirth) {
    dob = dateOfBirth;
}

/**
 * Returns the value of registrationDate.
 *
 * @return registrationDate value.
 */
public Date getRegistrationDate() {
    return registrationDate;
}

/**
 * Sets the new value of registrationDate.
 *
 * @param registrationDate New registrationDate value.
 */
public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
}

/**
 * Returns the value of email.
 *
 * @return email value.
 */
public String getEmail() {
    return email;
}

/**
 * Sets the new value of email.
 *
 * @param email New email value.
 */
public void setEmail(String email) {
    this.email = email;
}

/**
 * Returns the value of firstName.
 *
 * @return firstName value.
 */
public String getFirstName() {
    return firstName;
}

/**
 * Sets the new value of firstName.
 *
 * @param firstName New firstName value.
 */
public void setFirstName(String firstName) {
    this.firstName = firstName;
}

/**
 * Returns the value of password.
 *
 * @return password value.
 */
public String getPassword() {
    return password;
}

/**
 * Sets the new value of password.
 *
 * @param password New password value.
 */
public void setPassword(String password) {
    this.password = password;
}

/**
 * Returns the value of province.
 *
 * @return province value.
 */
public String getProvince() {
    return province;
}

/**
 * Sets the new value of province.
 *
 * @param province New province value.
 */
public void setProvince(String province) {
    this.province = province;
}

/**
 * Returns the value of phone.
 *
 * @return phone value.
 */
public String getPhone() {
    return phone;
}

/**
 * Sets the new value of phone.
 *
 * @param phone New phone value.
 */
public void setPhone(String phone) {
    this.phone = phone;
}
/**
 * Returns the value of street.
 *
 * @return street value.
 */
public String getStreet() {
    return path;
}

/**
 * Sets the new value of street.
 *
 * @param pStreet New street value.
 */
public void setStreet(String pStreet) {
    street = pStreet;
}

/**
 * Returns the value of username.
 *
 * @return username value.
 */
public String getUsername() {
    return username;
}

/**
 * Returns 1 or 0, indicating whether the tourist is active or not.
 *
 * @return Activation status value.
 */
public boolean isActive() {
    return active;
}

/**
 * Sets the new value of active.
 *
 * @param active New activation status value.
 */
public void setActive(boolean active) {
    this.active = active;
}

/**
 * Sets the new value of username.
 *
 * @param pUsername New username value.
 */
public void setUsername(String pUsername) {
    username = pUsername;
}

/**
 * Sets the new value of id.
 *
 * @param pid New id value.
 */
public void setId(int pid) {
    id = pid;
}

/**
 * Returns the value of id.
 *
 * @return id value.
 */
public int getId() {
    return id;
}
}