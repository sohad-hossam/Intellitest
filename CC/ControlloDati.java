package unisa.gps.etour.util;

import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.ImageIcon;

import unisa.gps.etour.bean.BeanBanner;
import unisa.gps.etour.bean.BeanCulturalAsset;
import unisa.gps.etour.bean.BeanAgreement;
import unisa.gps.etour.bean.BeanMenu;
import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.bean.BeanRestStopOperator;
import unisa.gps.etour.bean.BeanDish;
import unisa.gps.etour.bean.BeanSearchPreference;
import unisa.gps.etour.bean.BeanGenericPreferences;
import unisa.gps.etour.bean.BeanRestStop;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitCA;
import unisa.gps.etour.bean.BeanVisitRS;

/**
 * Class for managing the control of the correctness of the strings
 *
 * @Author Joseph Penna
 * @Version 0.1 © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class DataValidation {

    private static final String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String numbers = "0123456789";

    public static final int max_length = 64;

    /**
     * Static method for verifying correctness of a string
     *
     * @param string to check pString
     * @param allowedLetters Boolean: True if it is allowed to be present
     * Letters in the string, False otherwise
     * @param allowedNumbers Boolean: True if it is allowed to be present
     * Numbers in the string, False otherwise
     * @param allowedCharacters String containing all characters
     * Allowed in the string
     * @param requiredCharacters String containing all required characters
     * Must be present in the string
     * @param minCharacters integer representing the minimum number of
     * Characters allowed in string
     * @param maxCharacters integer representing the maximum number of
     * Characters allowed in string
     * @return Boolean: True if the string meets the conditions, False
     * Otherwise
     */
    public static boolean validateString(String pString,
                                         boolean allowedLetters, boolean allowedNumbers,
                                         String allowedCharacters, String requiredCharacters,
                                         int minCharacters, int maxCharacters) {
        if (pString == null) // the function returns false if null
            return false;

        int stringLength = pString.length();
        char currentChar;

        // Check the length of the string
        if (stringLength < minCharacters || stringLength > maxCharacters) {
            return false;
        }

        // Check the presence of the necessary characters in the string
        if (requiredCharacters != null) {
            if (!requiredCharacters.equals("")) {
                for (int i = 0; i < requiredCharacters.length(); i++) {
                    currentChar = requiredCharacters.charAt(i);
                    if (!pString.contains(String.valueOf(currentChar))) {
                        return false;
                    }
                }
            }
        }

        // Create the string containing all allowed characters
        String allowedCharsString = allowedCharacters +
                (allowedLetters ? letters : "") +
                (allowedNumbers ? numbers : "");

        // Loop for the inspection of the characters of the string to check
        for (int i = 0; i < stringLength; i++) {
            currentChar = pString.charAt(i);

            // If the character being checked is not in the string of allowed characters,
            // the string is incorrect and out of the loop.
            if (!(allowedCharsString.contains(String.valueOf(currentChar)))) {
                return false;
            }
        }

        return true;
    }

    public static String correctString(String pString,
                                       boolean allowedLetters, boolean allowedNumbers,
                                       String allowedCharacters, int maxCharacters) {
        if (pString == null)
            return null;

        String allowedCharsString = allowedCharacters +
                (allowedLetters ? letters : "") +
                (allowedNumbers ? numbers : "");

        char currentChar;
        int stringLength = pString.length();
        int i = 0;

        while (i < stringLength) {
            currentChar = pString.charAt(i);

            if (!(allowedCharsString.contains(String.valueOf(currentChar)))) {
                pString = pString.replaceAll("\\" + currentChar, "");
                stringLength = pString.length();
            } else {
                i++;
            }
        }

        if (stringLength > maxCharacters) {
            pString = pString.substring(0, maxCharacters);
        }

        return pString;
    }


public static boolean checkData(String data) {
    // Still I have no idea how I will spend the time
    return true;
}

public static boolean checkDates(Date startDate, Date endDate) {
    boolean result = false;
    if (startDate != null && endDate != null) {
        if (startDate.before(endDate)) {
            result = true;
        }
    }
    return result;
}

public static boolean checkTouristBean(BeanTourist tourist) {
    if (tourist != null && tourist instanceof BeanTourist) {
        return true;
    }
    return false;
}

public static boolean checkSearchPreferenceBean(BeanSearchPreference searchPreference) {
    if (searchPreference != null && searchPreference instanceof BeanSearchPreference) {
        return true;
    }
    return true;
}

public static boolean checkGenericPreferencesBean(BeanGenericPreferences genericPreferences) {
    if (genericPreferences != null && genericPreferences instanceof BeanGenericPreferences) {
        return true;
    }
    return false;
}

public static boolean checkCulturalAssetBean(BeanCulturalAsset culturalAsset) {
    if (culturalAsset != null && culturalAsset instanceof BeanCulturalAsset) {
        return true;
    }
    return false;
}

public static boolean checkRestStopBean(BeanRestStop restStop) {
    if (restStop != null && restStop instanceof BeanRestStop) {
        return true;
    }
    return false;
}

public static boolean checkRestStopOperatorBean(BeanRestStopOperator restStopOperator) {
    if (restStopOperator != null && restStopOperator instanceof BeanRestStopOperator) {
        return true;
    }
    return false;
}

/**
 * Please perform formal control and consistency on the data of the banner
 * Content in the bean passed by parameter.
 *
 * @Author Fabio Palladino
 * @param bannerBean bean containing the data of the banner.
 * @return True if the data of the banner is correct, false otherwise.
 */
public static boolean checkBannerBean(BeanBanner bannerBean) {
    boolean result = false;

    if (bannerBean != null && bannerBean instanceof BeanBanner) {
        result = (bannerBean.getId() > 0 && !bannerBean.getFilePath().equals("") && bannerBean.getRestaurantId() > 0);
    }

    return result;
}

/**
 * Method which checks the image contained in the passed ImageIcon object.
 *
 * @Author Fabio Palladino
 * @param image ImageIcon object containing the image to be checked
 * @return true if the image contained in the object is an instance of the ImageIcon class.
 */

public static boolean checkImage(ImageIcon image) {
    if (image != null) {
        return (image.getImage() instanceof BufferedImage);
    }
    return false;
}

/**
 * Function that checks the data in a news;
 *
 * @Author Fabio Palladino
 * @param newsBean Bean containing details of a news.
 * @return True if the data of the news is correct, false otherwise.
 */
public static boolean checkNewsBean(BeanNews newsBean) {
    boolean result = false;

    /* Check the validity of the general method parameter */
    if (newsBean != null) {
        Date publicationDate = newsBean.getPublicationDate(); // Date of Publishing
        Date expirationDate = newsBean.getExpirationDate(); // Due Date
        String newsText = newsBean.getNews(); // Text of News
        int priority = newsBean.getPriority();

        /* Checking the validity of the fields */
        if (publicationDate != null && expirationDate != null && newsText != null) {
            /* Check the consistency of the dates */
            result = publicationDate.before(expirationDate);
            /* Check that the text is not empty */
            result = result && (!newsText.equals(""));
            /* Check that the ID is greater than 0 */
            result = result && (newsBean.getId() > 0);
            /* Check the priority value */
            result = result && (priority <= Constants.MAX_PRIORITY_NEWS)
                    && (priority >= Constants.MIN_PRIORITY_NEWS);
        }
    }
    return result;
}

public static boolean checkTagBean(BeanTag tagBean) {
    if (tagBean != null && tagBean instanceof BeanTag) {
        return true;
    }
    return false;
}

public static boolean checkAgreementBean(BeanAgreement agreementBean) {
    if (agreementBean != null && agreementBean instanceof BeanAgreement) {
        return true;
    }
    return false;
}

public static boolean checkMenuBean(BeanMenu menuBean) {
    if (menuBean != null && menuBean instanceof BeanMenu) {
        return true;
    }
    return false;
}

public static boolean checkDishBean(BeanDish dishBean) {
    if (dishBean != null && dishBean instanceof BeanDish) {
        return true;
    }
    return false;
}

public static boolean checkVisitCulturalAssetBean(BeanVisitCA visitCulturalAssetBean) {
    if (visitCulturalAssetBean != null && visitCulturalAssetBean instanceof BeanVisitCA) {
        return true;
    }
    return false;
}

public static boolean checkVisitRestStopBean(BeanVisitRS visitRestStopBean) {
    if (visitRestStopBean != null && visitRestStopBean instanceof BeanVisitRS) {
        return true;
    }
    return false;
}
}