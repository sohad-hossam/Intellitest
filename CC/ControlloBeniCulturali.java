package unisa.gps.etour.control.CulturalHeritageManagement;

import java.util.Date;
import unisa.gps.etour.bean.CulturalHeritageBean;

/**
 * This class has the task of checking the data of a cultural object.
 * Various consistency checks are performed, such as length of strings,
 * Null reference, dynamic types incorrect.
 *
 * @Author Michelangelo De Simone
 * @Version 0.1
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
public class CulturalAssetsControl {

    /**
     * Performs consistency checks by calling the appropriate methods.
     * This method wraps all other control methods with a single call.
     * The flow is interrupted at the first false value.
     *
     * @param culturalObjectBean The cultural object's bean to be inspected
     * @return boolean The result of the check: true if OK, false otherwise
     */
    public static boolean checkCulturalObjectData(BeanCulturalObject culturalObjectBean) {
        // This method checks the input parameter for null reference or incorrect dynamic types
        if (culturalObjectBean == null || !(culturalObjectBean instanceof BeanCulturalObject))
            return false;

        // This method checks if the ID passed as a parameter in BeanCulturalObject is valid or not
        if (!checkCulturalObjectId(culturalObjectBean.getId()))
            return false;

        // This method checks the objects contained in BeanCulturalObject such as Date,
        // performing null reference checks and dynamic type checks.
        if (!checkCulturalObjectDates(culturalObjectBean))
            return false;

        // This method checks all the fields in BeanCulturalObject, searching for any null references
        if (!checkNullData(culturalObjectBean))
            return false;

        // Check the correct length of strings, in this case the CAP must be exactly five digits,
        // while the province must be two digits.
        // TODO: To be completed
        if (culturalObjectBean.getPostalCode().length() != 5 || culturalObjectBean.getProvince().length() != 2)
            return false;

        return true;
    }

    /**
     * Check for null data in a cultural object's bean.
     * The check is performed on all fields of the bean.
     *
     * @param culturalObjectBean The cultural object's bean to be checked
     * @return boolean The result of the check: true if there are no null references, false otherwise
     */
    public static boolean checkNullData(BeanCulturalObject culturalObjectBean) {
        if (culturalObjectBean.getPostalCode() == null || culturalObjectBean.getCity() == null ||
                culturalObjectBean.getDescription() == null || culturalObjectBean.getClosingDay() == null ||
                culturalObjectBean.getLocation() == null || culturalObjectBean.getName() == null ||
                culturalObjectBean.getOpeningTime() == null || culturalObjectBean.getClosingTime() == null ||
                culturalObjectBean.getProvince() == null || culturalObjectBean.getPhoneNumber() == null ||
                culturalObjectBean.getAddress() == null)
            return false;

        return true;
    }

    /**
     * Check the consistency of dates within the BeanCulturalObject.
     * The check is performed only on objects, while no validity check is performed on a date
     * as a cultural object may also have dates later than today (see for example ongoing exhibitions).
     *
     * @param culturalObjectBean The BeanCulturalObject to check the dates
     * @return boolean The result of the check: true if the dates have consistency; false otherwise
     */
    public static boolean checkCulturalObjectDates(BeanCulturalObject culturalObjectBean) {
        if (culturalObjectBean.getOpeningTime() == null || !(culturalObjectBean.getOpeningTime() instanceof Date) ||
                culturalObjectBean.getClosingTime() == null || !(culturalObjectBean.getClosingTime() instanceof Date))
            return false;

        return true;
    }

    /**
     * Check the ID of BeanCulturalObject
     *
     * @param id The ID of the BeanCulturalObject to be checked
     * @return boolean The result of the check: true if the ID is correct, false otherwise
     */
    public static boolean checkCulturalObjectId(int id) {
        return id > 0;
    }
}
