package unisa.gps.etour.control.SearchManagement;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class for managing Standard Search
 *
 * @Author Joseph Penna
 * @Version 0.1    2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class Search extends StandardSearch {

    /** Constructor of the class */
    public Search() {

    }

    protected int getNumberOfSpecializedSearchElements() throws SQLException {
        // Check the type of site and gets the number of results
        switch (siteType) {
            case CULTURAL_HERITAGE:
                return CulturalHeritage.getNumberOfSearchElements(
                        this.searchKeywords, this.tags, this.userPosition,
                        this.maxRadius);
            case RESTAURANT:
                return Restaurant.getNumberOfSearchElements(
                        this.searchKeywords, this.tags, this.userPosition,
                        this.maxRadius);
            default:
                return -1;
        }
    }

    protected ArrayList<?> specializedSearchByPage(int pageNumber) throws SQLException {
        // Check the type of site and search
        switch (siteType) {
            case CULTURAL_HERITAGE:
                return CulturalHeritage.search(
                        searchKeywords, tags,
                        pageNumber, numberOfElementsPerPage,
                        userPosition, maxRadius);
            case RESTAURANT:
                return Restaurant.search(
                        searchKeywords, tags,
                        pageNumber, numberOfElementsPerPage,
                        userPosition, maxRadius);
            default:
                return null;

        }
    }
}
