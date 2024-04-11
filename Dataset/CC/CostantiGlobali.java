package unisa.gps.etour.util;

import java.io.File;

/**
 * Class that contains the constants of the environmental system.
 *
 * @ Author Fabio Palladino
 * @ Version 0.1
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
public class GlobalConstants {

    /** Highest precedence of news. */
    public static final int MAX_PRIORITY_NEWS = 5;
    /** Priority least one news. */
    public static final int MIN_PRIORITY_NEWS = 1;
    /** Maximum number of active news on the system. */
    public static final int MAX_ACTIVE_NEWS = 10;
    /** Path to directory containing the images
     * Banners stored on the server. */
    public static final String SERVER_IMAGE_PATH = "c:" + File.separator + "BannerImages" + File.separator;
    /** URL of the server for RMI calls. */
    public static final String SERVER_URL = "localhost/";
    /** Milliseconds of 30 days. */
    public static final long THIRTY_DAYS = 2592000000L;
}