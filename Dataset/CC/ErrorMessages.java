package unisa.gps.etour.util;

/**
 * Standard self-describing error messages
 *
 * @Author Michelangelo De Simone
 * @Version 0.1
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
public class ErrorMessages {

    // Occurs when connecting to the DBMS
    public static final String DB_CONNECTION_ERROR = "Connection to DBMS Failed";

    // Occurs when an operation cannot be performed on the DBMS
    public static final String DB_ERROR = "DBMS Error";

    // Occurs in unspecified conditions
    public static final String UNKNOWN_ERROR = "Unknown error";

    // Occurs when there are format errors in a bean
    public static final String BEAN_FORMAT_ERROR = "Bean data format error";

    // Occurs when there is a data error
    public static final String DATA_ERROR = "Data Error";

    // Occurs when there is an error in reading/writing files
    public static final String FILE_ERROR = "Error reading/writing file";

    // Occurs when the maximum number of displayed banners is reached
    public static final String BANNER_LIMIT_ERROR = "Exceeded maximum number of banners";
}
