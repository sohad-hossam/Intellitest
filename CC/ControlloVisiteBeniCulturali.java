package unisa.gps.etour.control.CulturalAssetsManagement;

import unisa.gps.etour.bean.BeanCulturalVisit;

/**
 * This class is responsible for monitoring data related to a cultural visit.
 * Various consistency checks are performed, such as string length,
 * Null references, incorrect dynamic types.
 *
 * @ Author Michelangelo De Simone
 * @ Version 0.1
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
public class CulturalVisitDataCheck {
    
    /**
     * Perform consistency check by calling the appropriate methods.
     *
     * @param bean The cultural visit bean to check.
     * @return boolean The result of the check: true if OK, false otherwise.
     */
    public static boolean checkCulturalVisitData(BeanCulturalVisit bean) {
        // If the bean is null
        if (bean == null || !(bean instanceof BeanCulturalVisit))
            return false;

        // Check the ID of a cultural visit
        // And the ID of its tourist
        if (!(bean.getCulturalAssetId() > 0) || !(bean.getTouristId() > 0))
            return false;

        // Check the rating (ratings are accepted only between 1 and 5)
        if (!(bean.getRating() >= 1 && bean.getRating() <= 5))
            return false;

        // Check for null references in the bean
        if (!checkNullData(bean))
            return false;

        // Check the correct length of string
        if (!(bean.getComment().length() > 0))
            return false;

        return true;
    }

    /**
     * Check for null data in a cultural visit bean.
     *
     * @param bean The cultural visit bean to check.
     * @return boolean The result of the check: true if there are no null references, false otherwise.
     */
    public static boolean checkNullData(BeanCulturalVisit bean) {
        // Check the nullity of the feedback fields
        if (bean.getComment() == null || bean.getVisitDate() == null)
            return false;

        return true;
    }
}
