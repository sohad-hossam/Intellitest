package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanTourist;

/**
  * Interface for managing tourists in the database
  *
  * @ Author Mauro Miranda
  * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
  * Of Salerno
  */
public interface IDBTourist {
    /**
    * Add a tourist
    *
    * @ Param tourist Tourist to add
    * @ Throws SQLException
    */
    public boolean insertTourist(BeanTourist tourist) throws SQLException;

    /**
    * Modify a tourist
    *
    * @ Param tourist Tourist to modify
    * @ Throws SQLException
    * @ Return True if and 'been changed otherwise false
    */
    public boolean modifyTourist(BeanTourist tourist) throws SQLException;

    /**
    * Delete a tourist from the database
    *
    * @ Param touristId ID of the tourist to delete
    * @ Throws SQLException
    * @ Return True if and 'been changed otherwise false
    */
    public boolean deleteTourist(int touristId) throws SQLException;

    /**
    * Returns the data of the Tourist
    *
    * @ Param username Username of the tourist
    * @ Throws SQLException
    * @ Return Information about tourist
    */
    public BeanTourist getTourist(String username) throws SQLException;

    /**
    * Attach a cultural heritage to a tourist's favorites
    *
    * @ Param touristId ID of the tourist
    * @ Param culturalHeritageId ID of the cultural heritage
    * @ Throws SQLException
    */
    public boolean addFavoriteCulturalHeritage(int touristId, int culturalHeritageId) throws SQLException;

    /**
    * Attach a refreshment to a tourist's favorites
    *
    * @ Param touristId ID of the tourist
    * @ Param restaurantId ID of the refreshment
    * @ Throws SQLException
    */
    public boolean addFavoriteRestaurant(int touristId, int restaurantId) throws SQLException;

    /**
    * Delete a favorite cultural heritage
    *
    * @ Param touristId ID of the tourist
    * @ Param culturalHeritageId ID of the cultural heritage
    * @ Throws SQLException
    * @ Return True if and 'been changed otherwise false
    */
    public boolean deleteFavoriteCulturalHeritage(int touristId, int culturalHeritageId) throws SQLException;

    /**
    * Delete a favorite refreshment
    *
    * @ Param touristId ID of the tourist
    * @ Param restaurantId ID of the refreshment
    * @ Throws SQLException
    * @ Return True if and 'was deleted false otherwise
    */
    public boolean deleteFavoriteRestaurant(int touristId, int restaurantId) throws SQLException;

    /**
    * Returns an ArrayList of tourists with a username similar to the one given as argument
    *
    * @ Param touristUsername Username of the tourists to search
    * @ Throws SQLException
    * @ Return data for Tourists
    */
    public ArrayList<BeanTourist> getTourists(String touristUsername) throws SQLException;

    /**
    * Returns the list of tourists turned on or off
    *
    * @ Param active True False for tourists turned off
    * @ Return data for Tourists
    * @ Throws SQLException
    */
    public ArrayList<BeanTourist> getTourists(boolean active) throws SQLException;

    /**
    * Returns the data of the tourist with ID equal to the one given as argument
    *
    * @ Param touristId ID of the tourist to find
    * @ Return Tourist with id equal to the input, null if there is
    * @ Throws SQLException
    */
    public BeanTourist getTourist(int touristId) throws SQLException;

    /**
    * Returns the list of favorite cultural heritages for a particular tourist
    *
    * @ Param touristId ID of the tourist to find
    * @ Return List of Cultural Heritage Favorites
    * @ Throws SQLException
    */
    public ArrayList<Integer> getFavoriteCulturalHeritages(int touristId) throws SQLException;

    /**
    * Returns a list of favorite refreshments for a particular tourist
    *
    * @ Param touristId ID of the tourist to find
    * @ Return List of Refreshment Favorites
    * @ Throws SQLException
    */
    public ArrayList<Integer> getFavoriteRestaurants(int touristId) throws SQLException;
}