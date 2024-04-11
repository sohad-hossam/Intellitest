package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanBanner;

/**
 * Interface for managing the banner on the database.
 *
 * @Author Mauro Miranda
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University Of Salerno
 */
public interface IDBBanner {

    /**
     * Add a banner in the database.
     *
     * @param pBanner bean containing the information of the banner
     * @throws SQLException
     */
    public boolean addBanner(BeanBanner pBanner) throws SQLException;

    /**
     * Modify the contents of the advertisement, and returns the contents before Edit.
     *
     * @param pBanner Bean that contains the new information of the banner
     * @return True if there 'was a modified false otherwise
     * @throws SQLException
     */
    public boolean modifyBanner(BeanBanner pBanner) throws SQLException;

    /**
     * Delete a banner from the database and returns.
     *
     * @param pIdBanner ID BeanBanner
     * @return True if and 'was deleted false otherwise
     * @throws SQLException
     */
    public boolean deleteBanner(int pIdBanner) throws SQLException;

    /**
     * Returns a list of banners for a refreshment point, if the id of
     * Refreshment and 'equal to -1 will' return the complete list of Banners.
     *
     * @param pIdRefreshmentPoint ID of refreshment point from which to obtain the list of Banners
     * @return list of banners linked to Refreshment
     * @throws SQLException
     */
    public ArrayList<BeanBanner> getBanners(int pIdRefreshmentPoint) throws SQLException;

    /**
     * Method which returns a banner given its id.
     *
     * @param pIdBanner the banner ID to return
     * @return Banner found in the database, null if there is' match
     * @throws SQLException
     */
    public BeanBanner getBannerById(int pIdBanner) throws SQLException;
}