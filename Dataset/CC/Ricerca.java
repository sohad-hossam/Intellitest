package unisa.gps.etour.control.SearchManagement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import unisa.gps.etour.bean.CulturalAssetBean;
import unisa.gps.etour.bean.RestaurantBean;
import unisa.gps.etour.bean.TagBean;
import unisa.gps.etour.repository.CulturalAssetDB;
import unisa.gps.etour.repository.IRCulturalAsset;
import unisa.gps.etour.repository.IRRestaurant;
import unisa.gps.etour.repository.IRTag;
import unisa.gps.etour.repository.RestaurantDB;
import unisa.gps.etour.repository.TagDB;
import unisa.gps.etour.util.DataControl;
import unisa.gps.etour.util.ErrorMessage;
import unisa.gps.etour.util.Point3D;

/**
 * Class for managing search operations.
 */
public class Search extends UnicastRemoteObject implements ISearch {

    private static final long serialVersionUID = -6009809097302884655L;

    public Search() throws RemoteException {
        super();
    }

    // Search Parameters
    private int touristId = -1;
    private String keywords;
    private double maxRadius;
    private Point3D userPosition;
    private int itemsPerPage = -1;
    private byte siteType = -1;

    // Objects for the database connection
    private IRCulturalAsset culturalAssetDB = new CulturalAssetDB();
    private IRRestaurant restaurantDB = new RestaurantDB();
    private IRTag tagDB = new TagDB();

    // List of obtained tags
    private ArrayList<TagBean> tags;

    // Search Results pages partitioned
    private Hashtable<Integer, ArrayList<CulturalAssetBean>> culturalAssetSearchResults = new Hashtable<>();
    private Hashtable<Integer, ArrayList<RestaurantBean>> restaurantSearchResults = new Hashtable<>();

    // Quantity of items and pages results in the research phase
    private int numberOfResultPages = -1;
    private int numberOfSearchElements = -1;

    @Override
    public int search(int touristId, String keywords, int[] tagIds, double maxRadius, int itemsPerPage,
                      Point3D userPosition, byte siteType) throws RemoteException {
        // Reset parameters of the previous search
        resetParameters();

        // Start checking the correctness of the search parameters
        try {
            this.touristId = touristId;
            this.tags = arrayToArrayListTag(tagIds);
            this.keywords = DataControl.correctString(keywords, true, true, "", DataControl.MAX_LENGTH);
            this.maxRadius = maxRadius;
            this.userPosition = userPosition;
            this.siteType = siteType;

            // If parameters are valid, get the number of results
            if (itemsPerPage > 0 && ((siteType == 0) || (siteType == 1)) &&
                    maxRadius > 0 && userPosition != null &&
                    (this.numberOfSearchElements = getNumberOfSearchElementsSpecialized()) >= 0) {
                this.itemsPerPage = itemsPerPage;
                this.numberOfResultPages = calculateNumberOfSearchPages();

                return this.numberOfSearchElements;
            } else {
                return -1; // Otherwise, return -1
            }
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessage.DBMS_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessage.UNKNOWN_ERROR);
        }
    }

    /**
     * Method to obtain the number of search results.
     *
     * @return the number of search results. Returns -1 on error.
     * @throws SQLException Database Connection Exception.
     */
    private int getNumberOfSearchElementsSpecialized() throws SQLException {
        // Check the site type and obtain the number of results
        if (checkTouristId()) {
            switch (siteType) {
                case CULTURAL_ASSET:
                    return touristId > 0 ? culturalAssetDB
                            .getNumberOfAdvancedSearchElements(
                                    this.touristId, this.keywords,
                                    this.tags, this.userPosition,
                                    this.maxRadius) : culturalAssetDB
                            .getNumberOfSearchElements(this.keywords,
                                    this.tags, this.userPosition,
                                    this.maxRadius);
                case RESTAURANT:
                    return touristId > 0 ? restaurantDB
                            .getNumberOfAdvancedSearchElements(
                                    this.touristId, this.keywords,
                                    this.tags, this.userPosition,
                                    this.maxRadius) : restaurantDB
                            .getNumberOfSearchElements(this.keywords,
                                    this.tags, this.userPosition,
                                    this.maxRadius);
            }
        }
        return -1;
    }

    /**
     * Method to reset all the variables related to a search.
     *
     * @return A constant that indicates the correct zero.
     */
    private int resetParameters() {
        // Clear all the search parameters
        touristId = -1;
        siteType = -1;
        itemsPerPage = -1;
        numberOfSearchElements = -1;
        numberOfResultPages = -1;
        culturalAssetSearchResults.clear();
        restaurantSearchResults.clear();

        return -1;
    }

/**
 * Method for returning the list of results.
 *
 * @param pageNumber Range of results to return.
 * @return Container of sites emerged in the research phase within the interval.
 * @throws RemoteException Remote Exception.
 */
private ArrayList<?> searchPerPage(int pageNumber) throws RemoteException {
    // Declare the list of sites related to the input page
    ArrayList<?> currentPageResults = null;
    try {
        if (checkTouristId()) {
            // Check the type of site
            switch (siteType) {
                case CULTURAL_ASSET:
                    // If the list has not already been obtained previously, search
                    if ((currentPageResults = culturalAssetSearchResults.get(pageNumber)) == null) {
                        currentPageResults = (ArrayList<?>) (touristId > 0 ? culturalAssetDB
                                .advancedSearch(touristId, keywords, tags, pageNumber, itemsPerPage,
                                        userPosition, maxRadius)
                                : culturalAssetDB.search(keywords, tags, pageNumber, itemsPerPage,
                                        userPosition, maxRadius));
                        culturalAssetSearchResults.put(pageNumber,
                                (ArrayList<CulturalAssetBean>) currentPageResults);
                    }
                    break;
                case RESTAURANT:
                    // If the list has not already been obtained previously, search
                    if ((currentPageResults = restaurantSearchResults.get(pageNumber)) == null) {
                        currentPageResults = (ArrayList<?>) (touristId > 0 ? restaurantDB
                                .advancedSearch(touristId, keywords, tags, pageNumber, itemsPerPage,
                                        userPosition, maxRadius)
                                : restaurantDB.search(keywords, tags, pageNumber, itemsPerPage,
                                        userPosition, maxRadius));
                        restaurantSearchResults.put(pageNumber,
                                (ArrayList<RestaurantBean>) currentPageResults);
                    }
                    break;
            }
        }
        // Return the results
        return currentPageResults;
    } catch (SQLException e) {
        throw new RemoteException(ErrorMessage.DB_ERROR);
    } catch (Exception e) {
        throw new RemoteException(ErrorMessage.UNKNOWN_ERROR);
    }
}

/**
 * Method for calculating the number of pages found during search.
 *
 * @return Number of pages in the research phase.
 */
private int calculateNumberOfSearchPages() {
    // If the number of pages is greater than or equal to 0, return the number of pages
    if (numberOfResultPages >= 0)
        return numberOfResultPages;
    // If the number of elements is a multiple of the number of items per page, return their ratio
    if ((numberOfSearchElements % itemsPerPage == 0))
        return (numberOfSearchElements / itemsPerPage);
    // Otherwise, return their ratio + 1
    return (numberOfSearchElements / itemsPerPage) + 1;
}

/**
 * Method for calculating the number of elements in the interval for a given page.
 *
 * @param pageNumber Results page number.
 * @return Number of elements in a page.
 */
private int calculateNumberOfPageElements(int pageNumber) {
    // If the page number is the last one, return its remainder
    if (pageNumber == (numberOfResultPages - 1))
        return (numberOfSearchElements % itemsPerPage);
    // Otherwise, return the number of items per page
    else
        return (itemsPerPage);
}

/**
 * Method for checking the validity of a page.
 *
 * @param pageNumber Page to check.
 * @return True if the page is valid.
 */
private boolean checkPage(int pageNumber) {
    // If the page number is in the range, return true
    return (pageNumber >= 0 && pageNumber < numberOfResultPages);
}

/**
 * Method for checking the correctness of the Tourist identifier.
 *
 * @return True if the identifier is valid, false otherwise.
 */
private boolean checkTouristId() {
    return ((touristId > 0) || (touristId == -1));
}

/**
 * Method to reset all the variables related to a search.
 *
 * @return A constant indicating the correct zero.
 */
private int resetParameters() {
    // Clear all the search parameters
    touristId = -1;
    siteType = -1;
    itemsPerPage = -1;
    numberOfSearchElements = -1;
    numberOfResultPages = -1;
    culturalAssetSearchResults.clear();
    restaurantSearchResults.clear();

    return -1;
}

/**
 * Method for detecting and converting tag data into a structured list.
 *
 * @param pTagsId List of tag identifiers for the search.
 * @return List of search tags.
 * @throws SQLException Database Connection Exception.
 */
private ArrayList<TagBean> arrayToArrayListTag(int[] pTagsId) throws SQLException {
    // Initialize the list of Tags
    ArrayList<TagBean> tagsList = new ArrayList<>();
    // If the list of tag identifiers is not empty, retrieve the tags
    if (pTagsId != null) {
        TagBean currentTag = null;
        try {
            // Loop for inserting tags found into the list
            for (int i = 0; i < pTagsId.length; i++) {
                // If the identifier is greater than 0
                if (pTagsId[i] > 0) {
                    currentTag = tagDB.getTag(pTagsId[i]);
                    // Check correctness on tags
                    if (DataControl.checkBeanTag(currentTag))
                        tagsList.add(currentTag);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(ErrorMessage.DB_ERROR);
        }
    }
    // Return the list of tags
    return tagsList;
}
}