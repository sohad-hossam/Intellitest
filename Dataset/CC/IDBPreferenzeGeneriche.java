package unisa.gps.etour.repository;

import java.sql.SQLException;

import unisa.gps.etour.bean.GenericPreferenceBean;

/**
 * Interface for handling general preferences in database
 *
 * @ Author Mauro Miranda
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface GenericPreferencesDBInterface {

    /**
     * Add a general preference
     *
     * @ Param pPreference Preference to be added
     * @ Throws SQLException
     */
    public boolean addGenericPreference(GenericPreferenceBean pPreference) throws SQLException;

    /**
     * Edit a general preference
     *
     * @ Param pPreference Preference to change
     * @ Throws SQLException
     * @ Return True if and 'been changed otherwise false
     */
    public boolean editGenericPreference(GenericPreferenceBean pPreference) throws SQLException;

    /**
     * Delete a general preference
     *
     * @ Param preferenceId ID of the preference to delete
     * @ Throws SQLException
     * @ Return True if and 'have been deleted false otherwise
     */
    public boolean deleteGenericPreference(int preferenceId) throws SQLException;

    /**
     * Returns the generic preference for tourists
     *
     * @ Param touristId ID of the tourist
     * @ Throws SQLException
     * @ Return generic preference
     */
    public GenericPreferenceBean getGenericPreference(int touristId) throws SQLException;
}