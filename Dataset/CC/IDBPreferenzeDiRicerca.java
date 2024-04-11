package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.SearchPreferenceBean;

/**
 * Interface for managing search preferences in database
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface SearchPreferencesDBInterface {

    /**
     * Add a search preference
     *
     * @ Param pPreference Search Preferences
     * @ Throws SQLException
     */
    public boolean addSearchPreference(SearchPreferenceBean pPreference) throws SQLException;

    /**
     * Delete a search preference
     *
     * @ Param pPreferenceId ID of the preference to eliminate
     * @ Throws SQLException
     * @ Return True if and 'have been deleted false otherwise
     */
    public boolean deleteSearchPreference(int pPreferenceId) throws SQLException;

    /**
     * Returns the list of preferences to find a tourist
     *
     * @ Param touristId ID of the tourist
     * @ Throws SQLException
     * @ Return List of Search Preferences
     */
    public ArrayList<SearchPreferenceBean> getTouristSearchPreferences(int touristId) throws SQLException;

    /**
     * Returns the list of preferences for research of a cultural
     *
     * @ Param culturalId ID of the cultural
     * @ Throws SQLException
     * @ Return list of search preferences.
     */
    public ArrayList<SearchPreferenceBean> getCulturalSearchPreferences(int culturalId) throws SQLException;

    /**
     * Returns the list of preferences to find a resting spot
     *
     * @ Param restSpotId point identification Refreshments
     * @ Throws SQLException
     * @ Return list of search preferences.
     */
    public ArrayList<SearchPreferenceBean> getRestSpotSearchPreferences(int restSpotId) throws SQLException;

    /**
     * Add a preference for a cultural
     *
     * @ Param culturalId ID of the cultural
     * @ Param preferenceId ID of the search preference
     * @ Throws SQLException
     */
    public boolean addCulturalSearchPreference(int culturalId, int preferenceId) throws SQLException;

    /**
     * Add a search preference to a tourist
     *
     * @ Param touristId ID of the tourist
     * @ Param preferenceId ID of the search preference
     * @ Throws SQLException
     */
    public boolean addTouristSearchPreference(int touristId, int preferenceId) throws SQLException;

    /**
     * Add a preference research to a refreshment
     *
     * @ Param restSpotId point identification Refreshments
     * @ Param preferenceId ID of the search preference
     * @ Throws SQLException
     */
    public boolean addRestSpotSearchPreference(int restSpotId, int preferenceId) throws SQLException;

    /**
     * Deletes a preference to find a Tourist
     *
     * @ Param touristId ID of the tourist
     * @ Param preferenceId ID of the Search Preferences
     * @ Throws SQLException
     * @ Return True if and 'have been deleted false otherwise
     */
    public boolean deleteTouristSearchPreference(int touristId, int preferenceId) throws SQLException;

    /**
     * Deletes a preference for research of a cultural
     *
     * @ Param preferenceId ID of the Search Preferences
     * @ Param culturalId ID of the cultural
     * @ Throws SQLException
     * @ Return True if and 'have been deleted false otherwise
     */
    public boolean deleteCulturalSearchPreference(int culturalId, int preferenceId) throws SQLException;

    /**
     * Deletes a preference to find a resting spot
     *
     * @ Param preferenceId ID of the Search Preferences
     * @ Param restSpotId point identification Refreshments
     * @ Throws SQLException
     * @ Return True if and 'have been deleted false otherwise
     */
    public boolean deleteRestSpotSearchPreference(int restSpotId, int preferenceId) throws SQLException;

    /**
     * Returns a list of all search preferences in the DB
     *
     * @ Throws SQLException
     * @ Return List of search preferences in the DB
     */
    public ArrayList<SearchPreferenceBean> getAllSearchPreferences() throws SQLException;
}