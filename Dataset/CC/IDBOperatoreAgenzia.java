package unisa.gps.etour.repository;

import java.sql.SQLException;

import unisa.gps.etour.bean.AgencyOperatorBean;

/**
 * Interface for managing the database Agency Operator
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface AgencyOperatorDBInterface {

    /**
     * Returns the data of the Agency Operator with ID equal to the given one
     * Input
     *
     * @ Param pUsername Username of the Agency Operator to find
     * @ Return AgencyOperator with id equal to the input, null if there is
     * @ Throws SQLException
     */
    public AgencyOperatorBean getAgencyOperator(String pUsername) throws SQLException;

    /**
     * Modify the password of an Agency Operator
     *
     * @ Param poa Agency Operator object containing the new password
     * @ Throws SQLException
     * @ Return True if the password was successfully modified, false otherwise
     */
    public boolean modifyPassword(AgencyOperatorBean poa) throws SQLException;
}
