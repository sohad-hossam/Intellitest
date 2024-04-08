/**
 * Class that contains static methods that perform
 * Consistency checks on the data bean banner
 * And news.
 *
 * @ Author Fabio Palladino
 * @ Version 0.1
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
package com.trapan.spg.control.AdvertisementManagement;

import java.util.Date;

import com.trapan.spg.bean.BeanBanner;
import com.trapan.spg.bean.BeanNews;

public class DataAdvertisementCheck {

    /**
     * Perform formal control and consistency on
     * Data content of the banner in the bean passed by parameter.
     * @param pBanner bean contains the data of the banner.
     * @return true if the data in the bean is consistent.
     */
    public static boolean bannerCheck(BeanBanner pBanner) {
        boolean toReturn = false;

        if (pBanner != null) {
            toReturn = (pBanner.getId() > 0 && pBanner.getFilePath() != "" && pBanner.getRestaurantId() > 0);
        }

        return toReturn;
    }

    /**
     * Method that performs consistency checks and
     * Correctness of the information contained in the bean passed
     * As a parameter, in particular checks that the dates
     * Publication and expiration of the news are consistent,
     * Or that the second is later.
     *
     * @param pNews bean containing news data
     * @return Returns true if the bean contains consistent data
     */
    public static boolean newsCheck(BeanNews pNews) {
        boolean toReturn = false;

        /* Check the validity of the general parameter */
        if (pNews != null) {
            Date publishDate = pNews.getPublishDate();
            Date expiryDate = pNews.getExpiryDate();
            String newsText = pNews.getNewsText();

            /* Checking the validity of the fields */
            if (publishDate != null && expiryDate != null && newsText != null) {
                /* Check the consistency of the dates */
                toReturn = publishDate.before(expiryDate);

                /* Check that the text is not empty */
                toReturn = toReturn && (!newsText.equals(""));

                /* Check that the ID is greater than 0 */
                toReturn = toReturn && (pNews.getId() > 0);

                /* Check the priority value */
            }
        }

        return toReturn;
    }
}
