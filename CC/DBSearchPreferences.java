package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanPreferenceSearch;

/**
 * Class that implements the interface SearchPreferences
 *
 * @Author Joseph Martone
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class DBSearchPreferences implements IDBSearchPreferences {

    // Empty constructor
    public DBSearchPreferences() {}

    public boolean deleteSearchPreference(int preferenceId) throws SQLException {
        // Database connection variables
        Connection conn = null;
        // Query variable
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Deletion query
            String query = "DELETE FROM searchpreferences WHERE Id =" + preferenceId;
            // Execute the deletion query
            int i = stat.executeUpdate(query);
            // Return the result
            return (i == 1);
        } finally {
            // Always execute and close the Statement and the Connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    // Other methods have similar translations...

    public boolean deleteTouristSearchPreference(int touristId, int preferenceId) throws SQLException {
        // Variables for connecting to the database
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Deletion query
            String query = "DELETE FROM rating WHERE TouristId = " + touristId + " AND SearchPreferenceId = " + preferenceId;
            // Execute the deletion query
            int i = stat.executeUpdate(query);
            // Return the result
            return (i == 1);
        } finally {
            // Always execute and close the Statement and the connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public boolean insertBCSearchPreference(int culturalHeritageId, int searchPreferenceId) throws SQLException {
        // Variables for connecting to the database
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Insertion query
            String query = "INSERT INTO bcassociation (SearchPreferenceId, CulturalHeritageId) VALUES (" + searchPreferenceId + ", " + culturalHeritageId + ")";
            // Execute the insertion query
            int i = stat.executeUpdate(query);
            // Return the result
            return (i == 1);
        } finally {
            // Always execute and close the Statement and the connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }
public boolean insertPreferenceOfPR(int pIdRestaurant, int pIdSearchPreference) throws SQLException {
    // Variables for database connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    try {
        // Get the connection
        conn = DBConnectionPool.getConnection();
        // Create the Statement
        stat = conn.createStatement();
        // Insertion query
        String query = "INSERT INTO associationpr (SearchPreferenceId, RestaurantId) VALUES ("
                     + pIdSearchPreference + ", " + pIdRestaurant + ")";
        // Execute the insertion query
        int i = stat.executeUpdate(query);
        // Return the result
        return (i == 1);
    } finally {
        // Always execute and close the Statement and the connection
        if (stat != null) {
            stat.close();
        }
        if (conn != null) {
            DBConnectionPool.releaseConnection(conn);
        }
    }
}

public boolean insertPreferenceOfTourist(int pIdTourist, int pIdSearchPreference) throws SQLException {
    // Variables for database connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    try {
        // Get the connection
        conn = DBConnectionPool.getConnection();
        // Create the Statement
        stat = conn.createStatement();
        // Insertion query
        String query = "INSERT INTO rating (TouristId, SearchPreferenceId) VALUES ("
                     + pIdTourist + ", " + pIdSearchPreference + ")";
        // Execute the insertion query
        int i = stat.executeUpdate(query);
        // Return the result
        return (i == 1);
    } finally {
        // Always execute and close the Statement and the connection
        if (stat != null) {
            stat.close();
        }
        if (conn != null) {
            DBConnectionPool.releaseConnection(conn);
        }
    }
}

public ArrayList<SearchPreferenceBean> getPreferencesOfBC(int culturalHeritageId) throws SQLException {
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
        // Query to extract the list of search preferences for a cultural heritage
        String query = "SELECT * FROM associationbc, searchpreferences WHERE CulturalHeritageId = "
                     + culturalHeritageId
                     + " AND SearchPreferenceId = searchpreferences.Id";
        // The query is executed
        result = stat.executeQuery(query);
        // List that will contain the SearchPreferenceBean
        ArrayList<SearchPreferenceBean> list = new ArrayList<SearchPreferenceBean>();
        // We extract the results from the result set and move them into the list to be returned
        while (result.next()) {
            // Add to the list SearchPreferenceBean
            list.add(new SearchPreferenceBean(result.getInt("Id"), result.getString("Name")));
        }
        return list;
    } finally {
        // Always runs and takes care to close the Result, the Statement, and Connection
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

public ArrayList<SearchPreferenceBean> getPreferencesOfPR(int refreshmentId) throws SQLException {
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
        // Query to extract the list of search preferences for a refreshment
        String query = "SELECT * FROM associationpr, searchpreferences WHERE RefreshmentId = "
                     + refreshmentId
                     + " AND SearchPreferenceId = searchpreferences.Id";
        // The query is executed
        result = stat.executeQuery(query);
        // List that will contain the SearchPreferenceBean
        ArrayList<SearchPreferenceBean> list = new ArrayList<SearchPreferenceBean>();
        // We extract the results from the result set and move them into the list to be returned
        while (result.next()) {
            // Add to the list SearchPreferenceBean
            list.add(new SearchPreferenceBean(result.getInt("Id"), result.getString("Name")));
        }
        return list;
    } finally {
        // Always runs and takes care to close the Result, the Statement, and Connection
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

public ArrayList<SearchPreferenceBean> getPreferencesOfTourist(int touristId) throws SQLException {
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
        // Query to extract the list of search preferences for a tourist
        String query = "SELECT * FROM liking, searchpreferences WHERE TouristId = "
                     + touristId
                     + " AND SearchPreferenceId = searchpreferences.Id";
        // The query is executed
        result = stat.executeQuery(query);
        // List that will contain the SearchPreferenceBean
        ArrayList<SearchPreferenceBean> list = new ArrayList<SearchPreferenceBean>();
        // We extract the results from the result set and move them into the list to be returned
        while (result.next()) {
            // Add to the list SearchPreferenceBean
            list.add(new SearchPreferenceBean(result.getInt("Id"), result.getString("Name")));
        }
        return list;
    } finally {
        // Always runs and takes care to close the Result, the Statement, and Connection
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

public ArrayList<SearchPreferenceBean> getSearchPreferences() throws SQLException {
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
        // Query to extract the list of search preferences
        String query = "SELECT * FROM searchpreferences";
        // The query is executed
        result = stat.executeQuery(query);
        // List that will contain the SearchPreferenceBean
        ArrayList<SearchPreferenceBean> list = new ArrayList<SearchPreferenceBean>();
        // We extract the results from the result set and move them into the list to be returned
        while (result.next()) {
            // Add to the list SearchPreferenceBean
            list.add(new SearchPreferenceBean(result.getInt("Id"), result.getString("Name")));
        }
        // Return the list of search preferences in the DB
        return list;
    } finally {
        // Always runs and takes care to close the Result, the Statement, and Connection
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
