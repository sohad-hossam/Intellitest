package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.MenuBean;

/**
 * Interface for managing the menu in the database
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface MenuDBInterface {

    /**
     * Adds a menu in the database
     *
     * @ Param pMenu menu to add
     * @ Throws SQLException
     */
    public boolean addMenu(MenuBean pMenu) throws SQLException;

    /**
     * Modify a menu in the database
     *
     * @ Param pMenu Contains the data to change
     * @ Throws SQLException
     * @ Return True if there 'was a modified false otherwise
     */
    public boolean modifyMenu(MenuBean pMenu) throws SQLException;

    /**
     * Delete a menu from database
     *
     * @ Param ID pMenuId menu to delete
     * @ Throws SQLException
     * @ Return True if and 'was deleted false otherwise
     */
    public boolean deleteMenu(int pMenuId) throws SQLException;

    /**
     * Returns the menu of the day of a refreshment
     *
     * @ Param pRestPointId point identification Refreshments
     * @ Param pDay Day of the week in which the menu
     * Daily
     * @ Throws SQLException
     * @ Return Day menu de Refreshment
     */
    public MenuBean getMenuOfTheDay(int pRestPointId, String pDay) throws SQLException;

    /**
     * Returns a list of the menu of a refreshment
     *
     * @ Param pRestPointId point identification Refreshment
     * @ Throws SQLException
     * @ Return List of menus
     */
    public ArrayList<MenuBean> getMenuList(int pRestPointId) throws SQLException;
}
