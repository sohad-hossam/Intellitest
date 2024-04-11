package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTag;

/**
  * Interface for managing the Tag database
  *
  * @ Author Mauro Miranda
  * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
  * Of Salerno
  */
public interface IDBTag {
    /**
    * Add a tag
    *
    * @ Param tag Tag to add
    * @ Throws SQLException
    */
    public boolean insertTag(BeanTag tag) throws SQLException;

    /**
    * Modify the data in a tag
    *
    * @ Param tag Tag to modify
    * @ Throws SQLException
    * @ Return True if and 'been changed otherwise false
    */
    public boolean modifyTag(BeanTag tag) throws SQLException;

    /**
    * Delete a tag from the database
    *
    * @ Param tagId ID Tag to be deleted
    * @ Throws SQLException
    * @ Return True if and 'was deleted false otherwise
    */
    public boolean deleteTag(int tagId) throws SQLException;

    /**
    * Returns the list of tags in the database
    *
    * @ Throws SQLException
    * @ Return List containing the tags
    */
    public ArrayList<BeanTag> getTagList() throws SQLException;

    /**
    * Returns a single tag
    *
    * @ Param tagId ID tag
    * @ Throws SQLException
    * @ Return Tags
    */
    public BeanTag getTag(int tagId) throws SQLException;

    /**
    * Tag with immovable cultural
    *
    * @ Param culturalHeritageId of Cultural Heritage
    * @ Param tagId ID tag
    * @ Throws SQLException
    */
    public boolean addCulturalHeritageTag(int culturalHeritageId, int tagId) throws SQLException;

    /**
    * Tag to a refreshment
    *
    * @ Param restaurantId point identification Refreshments
    * @ Param tagId ID tag
    * @ Throws SQLException
    */
    public boolean addRestaurantTag(int restaurantId, int tagId) throws SQLException;

    /**
    * Returns the list of tags of a cultural
    *
    * @ Param culturalHeritageId of Cultural Heritage
    * @ Throws SQLException
    * @ Return list of tags
    */
    public ArrayList<BeanTag> getCulturalHeritageTags(int culturalHeritageId) throws SQLException;

    /**
    * Returns a list of tags of a refreshment
    *
    * @ Param restaurantId point identification Refreshments
    * @ Throws SQLException
    * @ Return list of tags
    */
    public ArrayList<BeanTag> getRestaurantTags(int restaurantId) throws SQLException;

    /**
    * Delete a tag to a cultural
    *
    * @ Param culturalHeritageId of Cultural Heritage
    * @ Param tagId ID tag
    * @ Throws SQLException
    * @ Return True if and 'was deleted false otherwise
    */
    public boolean deleteCulturalHeritageTag(int culturalHeritageId, int tagId) throws SQLException;

    /**
    * Delete a tag to a refreshment
    *
    * @ Param restaurantId ID
    * @ Param tagId ID tag
    * @ Throws SQLException
    * @ Return True if and 'was deleted false otherwise
    */
    public boolean deleteRestaurantTag(int restaurantId, int tagId) throws SQLException;
}