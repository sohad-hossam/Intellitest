package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import unisa.gps.etour.bean.BeanOperatoreRestaurant;

/**
 * Class that implements the interface Operator Refreshment
 *
 * @Author Joseph Martone
 * @Version 0.1
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class DBOperatoreRestaurant implements IDBOperatoreRestaurant {
    // Empty constructor
    public DBOperatoreRestaurant() {
    }

    public boolean deleteOperatorRefreshment(int pIdOperator) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query cancellation
            String query = "DELETE FROM operatorrestaurant WHERE Id =" + pIdOperator;
            // You run the query Cancellation
            int i = stat.executeUpdate(query);
            // This returns the backup
            return (i == 1);
        } finally {
            // Always runs and takes care of closing the Statement and the Connect
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public boolean insertOperatorRefreshment(BeanOperatorRestaurant operator) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        // Variable for the query results
        ResultSet single = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query for the insertion
            String query = "INSERT INTO operatorrestaurant (Name, Surname, Username, Password, Email, IdRestaurant) VALUES ('"
                    + operator.getName() + "','" + operator.getSurname()
                    + "','" + operator.getUsername() + "','"
                    + operator.getPassword() + "','" + operator.getEmail()
                    + "'," + operator.getIdRestaurant() + ")";
            // Query for checking the ID of the Restaurant as the association is 1 to 1 between OPPR and PR
            String unique = "SELECT IdRestaurant FROM operatorrestaurant WHERE IdRestaurant ="
                    + operator.getIdRestaurant();
            // Execute the query to control
            single = stat.executeQuery(unique);
            int j = 0;
            // Check if there are tuples
            while (single.next()) {
                j++;
            }
            // If it is empty
            if (j == 0) {
                // You run the insert query
                int i = stat.executeUpdate(query);
                // This returns the backup
                System.out.println("If you include the PR");
                return (i == 1);
            } else {
                System.out.println("Operator PR already exists for the PR");
                return false;
            }
        } finally {
            // Always runs and takes care of closing the Statement and the Connect
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
            if (single != null) {
                single.close();
            }
        }
    }

    public boolean updateOperatorRefreshment(BeanOperatorRestaurant operator) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query for amendment
            String query = "UPDATE operatorrestaurant SET Name = '"
                    + operator.getName() + "', Name ='"
                    + operator.getSurname() + "', password ='"
                    + operator.getPassword() + "', Email ='"
                    + operator.getEmail() + "' WHERE IdRestaurant ="
                    + operator.getIdRestaurant();
            // You run the query for Change
            int i = stat.executeUpdate(query);
            // This returns the backup
            return (i == 1);
        } finally {
            // Always runs and takes care of closing the Statement and the Connect
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public BeanOperatorRestaurant getOperatorRefreshment(int pIdOperator) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        // Variable for the query results
        ResultSet result = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            //
            // Create the Statement
            stat = conn.createStatement();
            // Query for the extraction of the dot Refreshments required
            String query = "SELECT * FROM WHERE id = operatorrestaurant" + pIdOperator;
            // The query is executed
            result = stat.executeQuery(query);
            // Get the bean Operator refreshment passing the id
            BeanOperatoreRestaurant beanTemp = null;
            if (result.next()) {
                // Built on BeanOPR
                beanTemp = new BeanOperatorRestaurant(result.getInt("Id"),
                        result.getString("Name"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email"), result.getInt("IdRestaurant"));
            }
            return beanTemp;
        } catch (Exception e) {
            throw new SQLException();
        } finally {
            // Always runs and takes care to close the Result, the Statement and Connection
            if (result != null) {
                result.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }
}
