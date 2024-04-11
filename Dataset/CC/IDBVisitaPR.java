package unisa.gps.etour.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanVisitaPR;

/**
  * Interface for managing feedback related to a specific refreshment point
  *
  * @ Author Joseph Martone
  * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
  * Of Salerno
  */
public interface IDBVisitaPR {

    /**
    * Add a visit to a refreshment point
    *
    * @ Param visit Visit to add
    * @ Throws SQLException
    */
    public boolean insertVisitPR(BeanVisitaPR visit) throws SQLException;

    /**
    * Modify a visit
    *
    * @ Param visit Visit to edit
    * @ Throws SQLException
    * @ Return True if and 'been changed otherwise false
    */
    public boolean modifyVisitPR(BeanVisitaPR visit) throws SQLException;

    /**
    * Extract the list of visits to a refreshment point
    *
    * @ Param refreshmentId point identification Refreshments
    * @ Throws SQLException
    * @ Return List of visits
    */
    public ArrayList<BeanVisitaPR> getVisitListPR(int refreshmentId) throws SQLException;

    /**
    * Extract a visit by a tourist at a refreshment point
    *
    * @ Param refreshmentId point identification Refreshments
    * @ Param touristId ID of the tourist
    * @ Throws SQLException
    * @ Return visit
    */
    public BeanVisitaPR getVisitPR(int refreshmentId, int touristId) throws SQLException;

    /**
    * Extract the list of visits of a tourist
    *
    * @ Param touristId ID of the tourist
    * @ Return List of visits
    * @ Throws SQLException
    */
    public ArrayList<BeanVisitaPR> getTouristVisitListPR(int touristId) throws SQLException;

}