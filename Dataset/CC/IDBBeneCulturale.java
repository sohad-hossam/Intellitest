package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.CulturalHeritageBean;
import unisa.gps.etour.bean.TagBean;
import unisa.gps.etour.util.Point3D;

/**
 * Interface for the management of cultural heritage database
 *
 * @ Author Mauro Miranda
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */

public interface CulturalHeritageDB {

    /**
     * Add a cultural heritage, given input
     *
     * @ Param pCulturalHeritage Cultural Heritage for inclusion in database
     * @ Throws SQLException
     */
    public boolean insertCulturalHeritage(CulturalHeritageBean pCulturalHeritage) throws SQLException;

    /**
     * Modify the information in the cultural
     *
     * @ Param pCulturalHeritage contains the information to modify the database
     * @ Throws SQLException
     * @ Return True if there 'was a modified false otherwise
     */
    public boolean modifyCulturalHeritage(CulturalHeritageBean pCulturalHeritage) throws SQLException;

    /**
     * Delete a cultural object from the database
     *
     * @ Param ID pCulturalPropertyId to delete
     * @ Throws SQLException
     * @ Return True if and 'was deleted false otherwise
     */
    public boolean deleteCulturalHeritage(int pCulturalPropertyId) throws SQLException;

    /**
     * Returns the cultural object with id as input
     *
     * @ Param pId cultural property to be extracted from the database
     * @ Throws SQLException
     * @ Return cultural property obtained from the database
     */
    public CulturalHeritageBean getCulturalHeritage(int pid) throws SQLException;

    /**
     * Research. Returns the list of cultural property in their name or
     * Description given string as input, filtered according to tags and
     * Maximum distance. The returned list contains the number of goods given as input.
     * To browse the real list, which may contain more 'of
     * Ten elements, you use the paramtro numPagina.
     *
     * @ param string that contains the keyword to search the
     * Name or description of the cultural
     * @ Param pTags list of tags used to filter the search. the
     * Maximum number of tags to be included should not exceed five
     * Units'. If you exceed this number the other tags
     * Excess will be ignored.
     * @ Param pNumPagina The page number you want to view. O for
     * 1 page (the first 10 results), 1 for 2 page (s
     * Results from 11 to 20) etc ...
     * @ Param pPosition position of the person who carried out the research
     * @ Param pMaximumDistance Maximum distance from the user to search for good
     * @ Param pNumberOfItemsPerPage number of items to return per page
     * @ Throws SQLException
     * @ Return list contained ten cultural
     */
    public ArrayList<CulturalHeritageBean> search(String pKeyword,
                                                  ArrayList<TagBean> pTags, int pNumPage,
                                                  int pNumberOfItemsPerPage, Point3D pPosition,
                                                  double pMaximumDistance) throws SQLException;

    /**
     * Advanced Search. Returns the list of cultural goods which have in
     * Name or description given string as input, sorted according to
     * Preferences of tourists and filtered according to the tag and the maximum distance. The
     * Returned list contains the number of goods given as input. To scroll
     * The actual list, which may contain multiple 'items, you
     * Use paramtro numPagina.
     *
     * @ Param ID pTouristId tourists who carried out the research
     * @ param string that contains the keyword to search the
     * Name or description of the cultural
     * @ Param pTags list of tags used to filter the search. the
     * Maximum number of tags to be included should not exceed five
     * Units'. If you exceed this number the other tags
     * Excess will be ignored.
     * @ Param pNumPage The page number you want to view. O for
     * 1 page (the first 10 results), 1 for 2 page (s
     * Results from 11 to 20) etc ...
     * @ Param pPosition position of the person who carried out the research
     * @ Param pMaximumDistance Maximum distance from the user to search for good
     * @ Param pNumberOfItemsPerPage number of items to return per page
     * @ Throws SQLException
     * @ Return list contained ten cultural
     */
    public ArrayList<CulturalHeritageBean> advancedSearch(int pTouristId,
                                                           String pKeyword, ArrayList<TagBean> pTags,
                                                           int pNumPage, int pNumberOfItemsPerPage,
                                                           Point3D pPosition, double pMaximumDistance) throws SQLException;

    /**
     * Method to get the number of elements to search.
     *
     * @ param string that contains the keyword to search the
     * Name or description of the cultural
     * @ Param pTags list of tags used to filter the search. the
     * Maximum number of tags to be included should not exceed five
     * Units'. If you exceed this number the other tags
     * Excess will be ignored.
     * @ Param pPosition position of the person who carried out the research
     * @ Param pMaximumDistance Maximum distance from the user to search for good
     * @ Throws SQLException
     * @ Return number of pages.
     */
    public int getNumberOfSearchElements(String pKeyword,
                                         ArrayList<TagBean> pTags, Point3D pPosition,
                                         double pMaximumDistance) throws SQLException;

    /**
     * Method to get the number of elements to search.
     *
     * @ Param identifier pTouristId tourists who carried out the research
     * @ param string that contains the keyword to search the
     * Name or description of the cultural
     * @ Param pTags list of tags used to filter the search. the
     * Maximum number of tags to be included should not exceed five
     * Units'. If you exceed this number the other tags
     * Excess will be ignored.
     * @ Param pPosition position of the person who carried out the research
     * @ Param pMaximumDistance Maximum distance from the user to search for good
     * @ Throws SQLException
     * @ Return number of pages.
     */
    public int getNumberOfAdvancedSearchElements(int pTouristId,
                                                 String pKeyword, ArrayList<TagBean> pTags,
                                                 Point3D pPosition, double pMaximumDistance) throws SQLException;

    /**
     * Returns a list of all cultural
     *
     * @ Throws SQLException
     * @ Return List of all cultural
     */
    public ArrayList<CulturalHeritageBean> getAllCulturalHeritage() throws SQLException;
}