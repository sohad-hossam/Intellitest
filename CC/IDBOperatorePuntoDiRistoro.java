package unisa.gps.etour.repository;

import java.sql.SQLException;

import unisa.gps.etour.bean.RestaurantOperatorBean;

/**
 * Interface for the operator to the point of comfort in the database
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface RestaurantOperatorDBInterface {

    /**
     * Adds a restaurant operator
     *
     * @ Param operator Restaurant operator to add
     * @ Throws SQLException
     */
    public boolean addRestaurantOperator(RestaurantOperatorBean operator) throws SQLException;

    /**
     * Modify a restaurant operator in the database
     *
     * @ Param operator New data of the operator
     * @ Throws SQLException
     * @ Return True if there 'was a modified false otherwise
     */
    public boolean modifyRestaurantOperator(RestaurantOperatorBean operator) throws SQLException;

    /**
     * Delete a restaurant operator
     *
     * @ Param operatorId Operator ID to delete
     * @ Throws SQLException
     * @ Return True if and 'was deleted false otherwise
     */
    public boolean deleteRestaurantOperator(int operatorId) throws SQLException;

    /**
     * Returns data of a restaurant operator
     *
     * @ Param operatorId Operator ID
     * @ Throws SQLException
     * @ Return Restaurant operator
     */
    public RestaurantOperatorBean getRestaurantOperator(int operatorId) throws SQLException;
}
