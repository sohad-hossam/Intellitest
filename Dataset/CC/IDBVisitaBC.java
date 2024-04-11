package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanVisitaBC;

/**
  * Interface for handling feedback on a given cultural asset
  *
  * @ Author Joseph Martone
  * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
  * Of Salerno
  */
public interface IDBVisitaBC {

    /**
    * Inserts a visit
    *
    * @ Param visit Visit to insert
    * @ Throws SQLException
    */
    public boolean insertVisitBC(BeanVisitaBC visit) throws SQLException;

    /**
    * Modify a visit
    *
    * @ Param visit Visit to edit
    * @ Throws SQLException
    * @ Return True if and 'been changed otherwise false
    */
    public boolean modifyVisitBC(BeanVisitaBC visit) throws SQLException;

    /**
    * Extract the list of visits to a cultural asset
    *
    * @ Param culturalHeritageId ID of the cultural asset
    * @ Throws SQLException
    * @ Return list of visits of the cultural asset
    */
    public ArrayList<BeanVisitaBC> getVisitListBC(int culturalHeritageId) throws SQLException;

    /**
    * Extract the list of cultural assets visited by a tourist
    *
    * @ Param touristId ID of the tourist
    * @ Throws SQLException
    * @ Return ArrayList of all feedback issued by a tourist for a specified cultural asset
    */
    public ArrayList<BeanVisitaBC> getTouristVisitListBC(int touristId) throws SQLException;

    /**
    * Extract a visit by a tourist to a cultural asset
    *
    * @ Param culturalHeritageId ID of the cultural asset
    * @ Param touristId ID of the tourist
    * @ Throws SQLException
    * @ Return visit
    */
    public BeanVisitaBC getVisitBC(int culturalHeritageId, int touristId) throws SQLException;
}
