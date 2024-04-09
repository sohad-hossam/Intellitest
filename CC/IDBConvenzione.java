package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.ConventionBean;
import unisa.gps.etour.bean.RestPointBean;

/**
 * Interface for managing the Business database
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface BusinessDBInterface {

    /**
     * Add a convention in the database
     *
     * @ Param pConvention Convention by adding
     * @ Throws SQLException
     */
    public boolean addConvention(ConventionBean pConvention) throws SQLException;

    /**
     * Modify a convention in the database
     *
     * @ Param data pConvention Convention of the Convention to be updated
     * @ Return True if there 'was a modified false otherwise
     * @ Throws SQLException
     */
    public boolean modifyConvention(ConventionBean pConvention) throws SQLException;

    /**
     * Delete an agreement by the database
     *
     * @ Param pIdConvention ID of the Convention by removing
     * @ Return True if been deleted false otherwise
     * @ Throws SQLException
     */
    public boolean deleteConvention(int pIdConvention) throws SQLException;

    /**
     * Returns the historical conventions of a refreshment
     *
     * @ Param restPointId point identification Refreshments
     * @ Return List of conventions of Refreshment given as argument
     * @ Throws SQLException
     */
    public ArrayList<ConventionBean> getHistoricalConventions(int restPointId) throws SQLException;

    /**
     * Returns the Convention active a refreshment
     *
     * @ Param restPointId point identification Refreshments
     * @ Return Convention Turns
     * @ Throws SQLException
     */
    public ConventionBean getActiveConvention(int restPointId) throws SQLException;

    /**
     * Returns a list of all the PR that have a Convention active
     *
     * @ Return list of all the PR with the Convention active
     * @ Throws SQLException
     */
    public ArrayList<RestPointBean> getListOfActiveConventionPR() throws SQLException;
}
