package unisa.gps.etour.control.GestionePuntiDiRistoro;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import unisa.gps.etour.bean.BeanAgreement;
import unisa.gps.etour.bean.BeanRestaurant;
import unisa.gps.etour.bean.BeanRestaurantVisit;

/**
 * Interface for managing restaurants from the agency side.
 *
 * @Author Joseph Morelli
 * @Version 0.1
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University
 * Of Salerno
 */
public interface RestaurantManagement extends AgencyRestaurantManagement, CommonRestaurantManagement {

    /**
     * Method for inserting a new restaurant.
     *
     * @param restaurant Contains all the data of the restaurant to be added
     * @throws RemoteException Remote Exception
     */
    public boolean insertRestaurant(BeanRestaurant restaurant) throws RemoteException;

    /**
     * Method for deleting a restaurant by its ID.
     *
     * @param restaurantID The unique identifier of the restaurant to be deleted
     * @throws RemoteException Remote Exception
     */
    public boolean deleteRestaurant(int restaurantID) throws RemoteException;

    /**
     * Method to retrieve all the restaurants from the database.
     *
     * @return ArrayList containing all the beans of the present restaurants in the database
     * @throws RemoteException Remote Exception
     */
    public ArrayList<BeanRestaurant> getRestaurants() throws RemoteException;

    /**
     * Method to retrieve all the restaurants with or without agreements.
     *
     * @param agreementStatus Boolean indicating the type of restaurants to retrieve (contracted or not)
     * @return ArrayList containing all the beans of the present restaurants in the database depending on the status of the agreement
     * @throws RemoteException Remote Exception
     */
    public ArrayList<BeanRestaurant> getRestaurants(boolean agreementStatus) throws RemoteException;

    /**
     * Method for activating a new agreement for a certain restaurant.
     *
     * @param restaurantID The unique identifier of the restaurant
     * @param agreement The agreement to be activated
     * @return boolean for confirmation of operation
     * @throws RemoteException Remote Exception
     */
    public boolean activateAgreement(int restaurantID, BeanAgreement agreement) throws RemoteException;

    /**
     * Method to get all the feedback associated with a certain restaurant.
     *
     * @param restaurantID The unique identifier of the restaurant to get feedback from
     * @return HashMap containing the bean as the key value of feedback and the tourist who issued the feedback
     * @throws RemoteException Remote Exception
     */
    public HashMap<BeanRestaurantVisit, String> getRestaurantFeedback(int restaurantID) throws RemoteException;

    /**
     * Method for updating (or changing) the data of a restaurant.
     *
     * @param updatedRestaurant The new data to be saved
     * @return Boolean value - true if the operation went successfully, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean modifyRestaurant(BeanRestaurant updatedRestaurant) throws RemoteException;

    /**
     * Method which allows inserting a tag for searching for a useful restaurant.
     *
     * @param restaurantId The unique identifier of the restaurant
     * @param tagId The unique ID of the tag to be inserted
     * @return Boolean value - true if the operation went successfully, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean insertRestaurantTag(int restaurantId, int tagId) throws RemoteException;

    /**
     * Method which allows deleting a tag for searching for a useful restaurant.
     *
     * @param restaurantId The unique identifier of the restaurant
     * @param tagId The unique ID of the tag to be deleted
     * @return Boolean value - true if the operation went successfully, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean deleteRestaurantTag(int restaurantId, int tagId) throws RemoteException;
}