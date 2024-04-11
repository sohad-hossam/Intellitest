package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanDish;

/**
 * Class that implements the dish interface
 *
 * @Author Joseph Martone
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University Of Salerno
 */
public class DBDish implements IDBDish {

    // Empty constructor
    public DBDish() {}

    public boolean deleteDish(int dishId) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query for deletion
            String query = "DELETE FROM dishes WHERE ID =" + dishId;
            // Execute the deletion query
            int i = stat.executeUpdate(query);
            // Return the result
            return (i == 1);
        } finally {
            // Always executed and takes care of closing the Statement and the Connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public boolean insertDish(BeanDish dish) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query for insertion
            String query = "INSERT INTO dishes (Name, Price, IdMenu) VALUES ('"
                    + dish.getName() + "'," + dish.getPrice() + ","
                    + dish.getMenuId() + ")";
            // Execute the insertion query
            int i = stat.executeUpdate(query);
            // Return the result
            return (i == 1);
        } finally {
            // Always executed and takes care of closing the Statement and the Connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public boolean updateDish(BeanDish dish) throws SQLException {
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
            String query = "UPDATE dishes SET Name = '"
                    + dish.getName() + "', Price =" + dish.getPrice()
                    + ", IdMenu =" + dish.getMenuId() + " WHERE Id ="
                    + dish.getId();
            // Execute the query for Change
            int i = stat.executeUpdate(query);
            // Return the result
            return (i == 1);
        } finally {
            // Always executed and takes care of closing the Statement and the Connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public ArrayList<BeanDish> getDishes(int menuId) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        // Variable for the query results
        ResultSet result = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query to extract the list of dishes given the id of the Menu
            String query = "SELECT * FROM dishes WHERE IdMenu =" + menuId;
            // The query is executed
            result = stat.executeQuery(query);
            // List that contains all the dishes obtained
            ArrayList<BeanDish> list = new ArrayList<BeanDish>();
            // We extract the results from the result set and moves in List
            // To be returned
            while (result.next()) {
                // Add to the list BeanDish
                list.add(new BeanDish(result.getInt("Price"), result
                        .getString("Name"), result.getInt("IdMenu"), result
                        .getInt("id")));
            }
            return list;
        } finally {
            // Always executed and takes care to close the Result, the Statement, and Connection
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