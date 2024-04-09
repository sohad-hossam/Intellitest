package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import unisa.gps.etour.bean.GenericPreferenceBean;

/**
 * Implementing the IDBGenericPreferences
 *
 * @Author Mauro Miranda
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University Of Salerno
 */
public class DBGenericPreferences implements IDBGenericPreferences {

    // Constructor without parameters
    public DBGenericPreferences() {

    }

    public boolean deleteGenericPreference(int preferenceId) throws SQLException {
        // Connect to database
        Connection conn = null;
        // Statement for running queries
        Statement stat = null;
        // Try block which performs the query and the database connection
        try {
            // Get the database connection from the pool
            conn = DBConnectionPool.getConnection();
            // Create the statement
            stat = conn.createStatement();
            // Query
            String query = "DELETE FROM genericpreferences WHERE Id =" + preferenceId;
            // You run the query
            int i = stat.executeUpdate(query);
            return (i == 1);
        } finally {
            // Finally block that contains the instructions to close the connections
            // Always run in any case
            if (stat != null) {
                // This closes the if statement and 'opened
                stat.close();
            }
            // It returns the connection to the pool if and 'opened
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public boolean insertGenericPreference(GenericPreferenceBean preference) throws SQLException {
        // Connect to database
        Connection conn = null;
        // Statement for running queries
        Statement stat = null;
        // Try block which performs the query and the database connection
        try {
            // You get the database connection from the pool
            conn = DBConnectionPool.getConnection();
            // Create the statement
            stat = conn.createStatement();
            // Query
            String query = "INSERT INTO genericpreferences (TouristId, Font, Theme, FontSize)"
                    + " VALUES ("
                    + preference.getTouristId()
                    + ", '"
                    + preference.getFont()
                    + "','"
                    + preference.getTheme()
                    + "',"
                    + preference.getFontSize() + ")";
            // You run the query
            int i = stat.executeUpdate(query);
            return (i == 1);
        } finally {
            // Finally block that contains the instructions to close the connections
            // Always run in any case
            if (stat != null) {
                // This closes the if statement and 'opened
                stat.close();
            }
            // It returns the connection to the pool if and 'opened
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }


public boolean modifyGenericPreference(GenericPreferenceBean preference) throws SQLException {
    // Connect to database
    Connection conn = null;
    // Statement for running queries
    Statement stat = null;
    // Try block which performs the query and the database connection
    try {
        // You get the database connection from the pool
        conn = DBConnectionPool.getConnection();
        // Create the statement
        stat = conn.createStatement();
        // Query
        String query = "UPDATE genericpreferences SET "
            + "TouristId = " + preference.getTouristId()
            + ", Font = '" + preference.getFont()
            + "', Theme = '" + preference.getTheme()
            + "', FontSize = " + preference.getFontSize()
            + " WHERE Id = " + preference.getId();
        // You run the query
        int i = stat.executeUpdate(query);
        return (i == 1);
    } finally {
        // Finally block that contains the instructions to close the connections
        // Hyenas run in any case
        if (stat != null) {
            // This closes the if statement and 'opened
            stat.close();
        }
        // It returns the connection to the pool if and 'opened
        if (conn != null) {
            DBConnectionPool.releaseConnection(conn);
        }
    }
}

public GenericPreferenceBean getGenericPreference(int touristId) throws SQLException {
    // Connect to database
    Connection conn = null;
    // Statement for running queries
    Statement stat = null;
    // ResultSet where the output of the query is inserted
    ResultSet result = null;
    // Try block which performs the query and the database connection
    try {
        // You get the database connection from the pool
        conn = DBConnectionPool.getConnection();
        // Create the statement
        stat = conn.createStatement();
        // Query
        String query = "SELECT * FROM genericpreferences WHERE TouristId = " + touristId;
        // Run the query
        result = stat.executeQuery(query);
        GenericPreferenceBean pref = null;
        // Check that the query returns at least one result
        if (result.next()) {
            pref = new GenericPreferenceBean();
            pref.setId(result.getInt("Id"));
            pref.setTouristId(result.getInt("TouristId"));
            pref.setFont(result.getString("Font"));
            pref.setTheme(result.getString("Theme"));
            pref.setFontSize(result.getInt("FontSize"));
        }
        return pref;
    } finally {
        // Finally block that contains the instructions to close the connections
        // Hyenas run in any case
        if (result != null) {
            result.close();
        }
        // This closes the if statement and 'opened
        if (stat != null) {
            stat.close();
        }
        // It returns the connection to the pool if and 'opened
        if (conn != null) {
            DBConnectionPool.releaseConnection(conn);
        }
    }
}
}