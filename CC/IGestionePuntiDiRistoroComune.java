package unisa.gps.etour.control.GestionePuntiDiRistoro;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanRestaurant;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanRestaurantVisit;

/**
 * Interface for common operations on restaurants.
 *
 * @Author Joseph Morelli
 * @Version 0.1
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface CommonRestaurantManagement extends Remote {

    /**
     * Method to retrieve a particular restaurant.
     *
     * @param restaurantID The identifier of the restaurant to retrieve
     * @return BeanRestaurant contains the data of the specified restaurant
     * @throws RemoteException Remote Exception
     */
    public BeanRestaurant getRestaurant(int restaurantID) throws RemoteException;

    /**
     * Method which returns the tags for a specific restaurant.
     *
     * @param restaurantID The identifier of the restaurant
     * @return ArrayList containing all BeanTag associated with the specified restaurant
     * @throws RemoteException Remote Exception
     */
    public ArrayList<BeanTag> getRestaurantTags(int restaurantID) throws RemoteException;

    /**
     * Method which returns the last 10 comments made for a restaurant.
     *
     * @param restaurantID The identifier of the restaurant
     * @return Array of strings containing the last 10 comments
     * @throws RemoteException Remote Exception
     */
    public String[] getLastComments(int restaurantID) throws RemoteException;

    /**
     * Returns an array for the specified restaurant, where each
     * index contains the number of ratings corresponding to the value
     * Index of the array greater than one. The calculation is made
     * in the period between 30 days ago and today.
     *
     * @param restaurantID The identifier of the restaurant
     * @return ArrayList containing the counters as described above
     * @throws RemoteException Remote Exception
     */
    public ArrayList<Integer> getRestaurantStatistics(int restaurantID) throws RemoteException;

    /**
     * Method which allows changing the comment issued for a restaurant.
     *
     * @param restaurantId The identifier of the restaurant
     * @param newVisit Bean containing the new comment
     * @return Boolean value - true if the operation was successful, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean modifyRestaurantFeedback(int restaurantId, BeanRestaurantVisit newVisit) throws RemoteException;
}
