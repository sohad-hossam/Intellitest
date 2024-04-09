package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanMenu;

/**
 * Class that implements the Menu interface
 *
 * @Author Joseph Martone
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University Of Salerno
 */
public class DBMenu implements IDBMenu {
    // Empty constructor
    public DBMenu() {}

    public boolean deleteMenu(int pIdMenu) throws SQLException {
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
            String query = "DELETE FROM menu WHERE id =" + pIdMenu;
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

    public boolean insertMenu(BeanMenu pMenu) throws SQLException {
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
            String query = "INSERT INTO menu (Day, IdRestaurant) VALUES ('"
                    + pMenu.getDay() + "',"
                    + pMenu.getIdRestaurant()
                    + ")";
            // Execute the insert query
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

    public boolean modifyMenu(BeanMenu pMenu) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query for modification
            String query = "UPDATE menu SET Date = '"
                    + pMenu.getDay() + "' WHERE Id =" + pMenu.getId();
            // Execute the modification query
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

    public ArrayList<BeanMenu> getMenu(int pIdRestaurant) throws SQLException {
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
            // Query to extract the list of Menu
            String query = "SELECT * FROM menu WHERE IdRestaurant ="
                    + pIdRestaurant;
            // The query is executed
            result = stat.executeQuery(query);
            // List that will contain all BeanMenu obtained
            ArrayList<BeanMenu> list = new ArrayList<BeanMenu>();
            // We extract the results from the result set and add to
            // List to be returned
            while (result.next()) {
                // Fill the list
                list.add(new BeanMenu(result.getInt("Id"), result.getString("Day"), result.getInt("IdRestaurant")));
            }
            // Return the list
            return list;
        } finally {
            // Always executed and takes care to close the Result, the Statement
            // And Connection
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

    public BeanMenu getDailyMenu(int pIdRestaurant, String pGiorno) throws SQLException {
        // Variables for database connection
    }
}

public boolean modifyMenu(BeanMenu pMenu) throws SQLException {
    // Variables for database connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    try {
        // Get the connection
        conn = DBConnectionPool.getConnection();
        // Create the Statement
        stat = conn.createStatement();
        // Query for modification
        String query = "UPDATE menu SET Date = '"
                + pMenu.getDay() + "' WHERE Id =" + pMenu.getId();
        // Execute the query for modification
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

public ArrayList<BeanMenu> getMenu(int pIdRestaurant) throws SQLException {
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
        // Query to extract the list of Menu
        String query = "SELECT * FROM menu WHERE IdRestaurant ="
                + pIdRestaurant;
        // The query is executed
        result = stat.executeQuery(query);
        // List that will contain all BeanMenu obtained
        ArrayList<BeanMenu> list = new ArrayList<BeanMenu>();
        // We extract the results from the result set and move them into the list to be returned
        while (result.next()) {
            // Fill the list
            list.add(new BeanMenu(result.getInt("Id"), result.getString("Day"), result.getInt("IdRestaurant")));
        }
        // Return the list
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

public BeanMenu getDailyMenu(int pIdRestaurant, String pDay) throws SQLException {
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
        // Query for the extraction of Daily Menu
        String query = "SELECT * FROM menu WHERE IdRestaurant ="
                + pIdRestaurant + " AND day = '" + pDay + "'";
        // The query is executed
        result = stat.executeQuery(query);
        // Get the bean of the daily menu based on the ID of the point of
        // Dining and a day
        BeanMenu beanTemp = null;
        if (result.next()) {
            // Create the Bean
            beanTemp = new BeanMenu(result.getInt("Id"), result.getString("Day"), result.getInt("IdRestaurant"));
        }
        // Return the obtained Bean
        return beanTemp;
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
