package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.RestaurantBean;
import unisa.gps.etour.bean.TagBean;
import unisa.gps.etour.util.Point3D;

/**
 * Interface for management of eateries in the database
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface RestaurantDBInterface {

    /**
     * Add a refreshment
     *
     * @ Param restaurant Restaurant to add
     * @ Throws SQLException
     */
    public boolean addRestaurant(RestaurantBean restaurant) throws SQLException;

    /**
     * Modify a refreshment
     *
     * @ Param restaurant Restaurant to edit
     * @ Throws SQLException
     * @ Return True if and 'been changed otherwise false
     */
    public boolean editRestaurant(RestaurantBean restaurant) throws SQLException;

    /**
     * Delete a refreshment
     *
     * @ Param ID restaurant ID of the refreshment to eliminate
     * @ Throws SQLException
     * @ Return True if and 'have been deleted false otherwise
     */
    public boolean deleteRestaurant(int restaurantId) throws SQLException;

    /**
     * Returns data from a point of comfort with the ID given as argument
     *
     * @ Param pId identification ID of the refreshment
     * @ Throws SQLException
     * @ Return Refreshment
     */
    public RestaurantBean getRestaurant(int pId) throws SQLException;

    /**
     * Advanced Search. Returns the list of eateries that have in
     * Name or description given string as input, sorted according to
     * Preferences of tourists, the tags and filtered according to the distance
     * Max. The list returned contains only the number of catering outlets input data.
     * To scroll the real list, which may contain multiple 'items, you
     * Use paramtro numPagina.
     *
     * @ Param touristId tourists who carried out the research
     * @ Param keyword string that contains the keyword to search the
     * Name or description of refreshment
     * @ Param tags list of tags used to filter the search. the
     * Maximum number of tags to be included should not exceed five
     * Units'. If you exceed this number the other tags
     * Excess will be ignored.
     * @ Param pageNumber The page number you want to view. O
     * The 1 page (the first 10 results), 1 for 2 page (s
     * Results from 11 to 20) etc ... *
     * @ Param position position of the person who carried out the research
     * @ Param numberOfElementsPerpage int Number of elements to return
     * @ Param maxDistance Maximum distance from the user to refreshment
     * To seek
     * @ Throws SQLException
     * @ Return list containing ten points Refreshments
     */
    public ArrayList<RestaurantBean> advancedSearch(int touristId,
                                                     String keyword,
                                                     ArrayList<TagBean> tags,
                                                     int pageNumber,
                                                     int numberOfElementsPerPage,
                                                     Point3D position,
                                                     double maxDistance) throws SQLException;

    /**
     * Method to get the number of elements for an advanced search.
     *
     * @ See advancedSearch ()
     * @ Param touristId tourists who carried out the research
     * @ Param keyword string that contains the keyword to search the
     * Name or description of refreshment
     * @ Param tags list of tags used to filter the search. The
     * Maximum number of tags to be included should not exceed five
     * Units'. If you exceed this number the other tags
     * Excess will be ignored.
     * @ Param position position of the person who carried out the research
     * @ Param maxDistance Maximum distance from the user to refreshment
     * To seek
     * @ Throws SQLException
     * @ Return number of pages.
     */
    public int getNumberOfElementsForAdvancedSearch(int touristId,
                                                     String keyword,
                                                     ArrayList<TagBean> tags,
                                                     Point3D position,
                                                     double maxDistance) throws SQLException;

    /**
     * Research. Returns the list of eateries that have the name or
     * Description given string as input, filtered and tags
     * According to the maximum distance. The returned list contains the number of
     * Points Refreshments input data. To scroll the real list, which
     * May contain more 'items, you use the paramtro
     * NumPagina.
     *
     * @ Param keyword string that contains the keyword to search the
     * Name or description of refreshment
     * @ Param tags list of tags used to filter the search. the
     * Maximum number of tags to be included should not exceed five
     * Units'. If you exceed this number the other tags
     * Excess will be ignored.
     * @ Param pageNumber The page number you want to view. O
     * The 1 page (the first 10 results), 1 for 2 page (s
     * Results from 11 to 20) etc ... *
     * @ Param position position of the person who carried out the research
     * @ Param maxDistance Maximum distance from the user to refreshment
     * @ Param numberOfElementsPerPage int Number of elements to return
     * @ Throws SQLException
     * @
 * Return list containing ten points Refreshments
 */
public ArrayList<RestaurantBean> search(String keyword,
                                        ArrayList<TagBean> tags,
                                        int pageNumber,
                                        int numberOfElementsPerPage,
                                        Point3D position,
                                        double maxDistance) throws SQLException;

/**
 * Method to get you the elements for an advanced search.
 *
 * @ See search ()
 * @ Param touristId tourists who carried out the research
 * @ Param keyword string that contains the keyword to search the
 * Name or description of refreshment
 * @ Param tags list of tags used to filter the search. The
 * Maximum number of tags to be included should not exceed five
 * Units'. If you exceed this number the other tags
 * Excess will be ignored.
 * @ Param position position of the person who carried out the research
 * @ Param maxDistance Maximum distance from the user to refreshment
 * To seek
 * @ Throws SQLException
 * @ Return number of pages.
 */
public int getNumberOfElementsForSearch(int touristId,
                                         String keyword,
                                         ArrayList<TagBean> tags,
                                         Point3D position,
                                         double maxDistance) throws SQLException;

/**
 * Returns a list of all the refreshment
 *
 * @ Throws SQLException
 * @ Return list of all the refreshment
 */
public ArrayList<RestaurantBean> getRestaurantList() throws SQLException;
}
