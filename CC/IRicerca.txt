package unisa.gps.etour.control.GestioneRicerche;

import java.rmi.Remote;
import java.rmi.RemoteException;

import unisa.gps.etour.bean.BeanBeneCulturale;
import unisa.gps.etour.bean.BeanPuntoDiRistoro;
import unisa.gps.etour.util.Punto3D;

/**
 * Interface for managing searches
 *
 * @Author Joseph Penna
 * @Version 0.1
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public interface ResearchManagement extends Remote, Research {

    // Constants to identify the types of sites
    public final static byte CULTURAL_HERITAGE = 0;
    public final static byte RESTAURANT = 1;

    /**
     * Initialization method for searches
     *
     * @param touristID            ID of the tourist. Pass -1 in case of a Guest
     * @param searchKeywords       Keywords for search
     * @param tagIDs               Search tags
     * @param maxRadius            Maximum distance between the user and the site
     * @param itemsPerPage         Number of items to look for in a search session
     * @param userPosition         Detected position of the user by GPS
     * @param siteType             Type of site to search
     * @return Number of elements emerged from the search. On error returns -1
     * @throws RemoteException Remote exception
     */
    public int search(int touristID, String searchKeywords, int[] tagIDs, double maxRadius,
                      int itemsPerPage, Punto3D userPosition, byte siteType) throws RemoteException;

    /**
     * Method to return the list of cultural heritage emerging from the research, within a given interval
     *
     * @param page Page range of items to be included in the results
     * @return Array of cultural heritage related to the selected search results range. Returns null in case of error
     * @throws RemoteException Remote Exception
     */
    public BeanBeneCulturale[] getSearchResultsPageCulturalHeritage(int page) throws RemoteException;

    /**
     * Method to return the list of restaurants emerging from the research, within a given interval
     *
     * @param page Page range of items to be included in the results
     * @return Array of restaurant points related to the selected search results range. Returns null in case of error
     * @throws RemoteException Remote Exception
     */
    public BeanPuntoDiRistoro[] getSearchResultsPageRestaurant(int page) throws RemoteException;

    /**
     * Method to return the number of elements resulting from the search
     *
     * @return Number of elements emerged in the search phase. Returns -1 if the search is not initialized
     * @throws RemoteException Remote Exception
     */
    public int getNumberOfSearchResults() throws RemoteException;

    /**
     * Method to return the number of pages appearing in the search results
     *
     * @return Number of pages emerged in the search phase. Returns -1 if the search is not initialized
     * @throws RemoteException Remote Exception
     */
    public int getNumberOfSearchResultPages() throws RemoteException;
}
