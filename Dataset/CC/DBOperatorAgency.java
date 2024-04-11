/**
 * Class that implements the Agency's Operator
 *
 * @Author Joseph Martone
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */

package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import unisa.gps.etour.bean.BeanOperatorAgency;

public class DBOperatorAgency implements IDBOperatorAgency {

    /* (Non-Javadoc)
    * @See unisa.gps.etour.repository.IDBOperatorAgency#getAgencyOperator(int)
    */
    public BeanOperatorAgency getAgencyOperator(String pUsername) throws SQLException {
        // Connect to the database
        Connection conn = null;
        // Statement for executing queries
        Statement stat = null;
        // ResultSet where the output of the query is stored
        ResultSet result = null;
        // Try block that performs the query and the database connection
        try {
            // Get the database connection from the pool
            conn = DBConnectionPool.getConnection();
            // Create the statement
            stat = conn.createStatement();
            // Query
            String query = "SELECT * FROM agencyoperator WHERE Username = '" + pUsername + "'";
            result = stat.executeQuery(query);
            BeanOperatorAgency oa = null;
            if (result.next()) {
                // Construct the bean when the query returns a value
                // Otherwise, it will return null
                oa = new BeanOperatorAgency();
                oa.setId(result.getInt("Id"));
                oa.setUsername(result.getString("Username"));
                oa.setName(result.getString("Name"));
                oa.setSurname(result.getString("Surname"));
                oa.setPassword(result.getString("Password"));
            }
            return oa;
        } finally {
            // Finally block that contains the instructions to close the connections
            // These are executed in any case
            if (result != null) {
                // Close the result set only if the query was executed
                result.close();
            }
            if (stat != null) {
                // Close the statement if it was opened
                stat.close();
            }
            if (conn != null) {
                // Return the connection to the pool if it was opened
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    /* (Non-Javadoc)
    * @See unisa.gps.etour.repository.IDBOperatorAgency#changePassword(java.lang.String)
    */
    public boolean changePassword(BeanOperatorAgency poa) throws SQLException {
        // Connect to the database
        Connection conn = null;
        // Statement for executing queries
        Statement stat = null;
        // Try block that performs the query and the database connection
        try {
            // Get the database connection from the pool
            conn = DBConnectionPool.getConnection();
            // Create the statement
            stat = conn.createStatement();
            // Query
            String query = "UPDATE agencyoperator SET Password = '" + poa.getPassword() + "' WHERE Id = " + poa.getId();
            // Execute the query
            int i = stat.executeUpdate(query);
            return (i == 1);
        } finally {
            // Finally block that contains the instructions to close the connections
            // These are executed in any case
            if (stat != null) {
                // Close the statement if it was opened
                stat.close();
            }
            if (conn != null) {
                // Return the connection to the pool if it was opened
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }
}
