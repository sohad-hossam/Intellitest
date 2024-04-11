package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class that implements the local statistics
 *
 * @author Joseph Martone
 * @version 0.1 © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class DBLocationStatistics implements IDBLocationStatistics {

    // Empty constructor
    public DBLocationStatistics() {

    }

    public ArrayList<String> getLocalitiesList() throws SQLException {
        // Variables for database connection
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query for the extraction of locations between the PR and BC
            String query = "(SELECT DISTINCT Locations "
                    + "FROM culturalasset) UNION "
                    + "(SELECT DISTINCT Location FROM refreshmentpoint)";
            // Execute the query
            result = stat.executeQuery(query);
            // Extract the results from the result set and store in a list to be returned
            ArrayList<String> list = new ArrayList<String>();
            while (result.next()) {
                // Add the locations obtained to the list
                list.add(result.getString("Location"));
            }
            return list;
        } finally {
            // Always close the Result, the Statement, and Connection
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

    public double getAverageRatingsByLocality(String locality) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        Statement stat = null;
        ResultSet result = null;

        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query to extract the average ratings of dining points according to a location
            String query = "SELECT avg(AverageRatings) as AverageRatings FROM refreshmentpoint WHERE Location = '"
                    + locality + "'";
            // Execute the query
            result = stat.executeQuery(query);
            // Extract the results from the result set
            double point = 0.0;
            if (result.next()) {
                point = result.getDouble("AverageRatings");
            }
            // Query to extract the average ratings of cultural heritage according to a location
            query = "SELECT avg(AverageRatings) AS AverageRatings FROM culturalasset WHERE Location = '"
                    + locality + "'";
            // Execute the query
            result = stat.executeQuery(query);
            // Extract the results from the result set
            double good = 0.0;
            if (result.next()) {
                good = result.getDouble("AverageRatings");
            }
            // Return the average of the ratings of dining points and cultural heritage
            return (good + point) / 2;
        } finally {
            // Always close the Result, the Statement, and Connection
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