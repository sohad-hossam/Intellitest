package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.RestaurantPointBean;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.util.Point3D;

/**
 * Class that implements the interface of Refreshment
 *
 * @ Author Joseph Martone
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class DBRefreshmentPoint implements IDBRefreshmentPoint {

    // Empty constructor
    public DBRefreshmentPoint() {}

    public boolean deleteRefreshmentPoint(int refreshmentPointId) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query cancellation
            String query = "DELETE FROM refreshmentpoint WHERE Id =" + refreshmentPointId;
            // Execute the query
            int i = stat.executeUpdate(query);
            // Return the result of deletion
            return (i == 1);
        } finally {
            // Always runs and takes care of closing the Statement and the Connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public boolean insertRefreshmentPoint(BeanRefreshmentPoint refreshmentPoint) throws SQLException {
        // Create the organization of the Opening and Closing
        Time openingTime = new Time(refreshmentPoint.getOpeningTime().getTime());
        Time closingTime = new Time(refreshmentPoint.getClosingTime().getTime());
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query for the insertion of a refreshment
            String query = "INSERT INTO refreshmentpoint (Name, Description, Telephone, Latitude, Longitude, Altitude, OpeningTime, ClosingTime, ClosingDay, Town, City, Street, Postcode, Province, AverageRatings, NumberRatings, VAT) VALUES ('" 
                    + refreshmentPoint.getName() + "','" + refreshmentPoint.getDescription() + "','" 
                    + refreshmentPoint.getTelephone() + "'," + refreshmentPoint.getPosition().getLatitude() + "," 
                    + refreshmentPoint.getPosition().getLongitude() + "," + refreshmentPoint.getPosition().getAltitude() + ",'" 
                    + openingTime.toString() + "','" + closingTime.toString() + "','" + refreshmentPoint.getClosingDay() + "','" 
                    + refreshmentPoint.getLocation() + "','" + refreshmentPoint.getCity() + "','" 
                    + refreshmentPoint.getStreet() + "','" + refreshmentPoint.getPostalCode() + "','" 
                    + refreshmentPoint.getProvince() + "'," + refreshmentPoint.getAverageRatings() + "," 
                    + refreshmentPoint.getNumberRatings() + ",'" + refreshmentPoint.getVAT() + "')";
            // Execute the insert query
            int i = stat.executeUpdate(query);
            // Return the result of insertion
            return (i == 1);
        } finally {
            // Always runs and takes care of closing the Statement and the Connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public boolean modifyRefreshmentPoint(RestaurantPointBean refreshmentPoint) throws SQLException {
        // Create objects for opening and closing times
        java.sql.Time openingTime = new Time(refreshmentPoint.getOpeningTime().getTime());
        java.sql.Time closingTime = new Time(refreshmentPoint.getClosingTime().getTime());
        
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the statement
            stat = conn.createStatement();
            // Query for modification
            String query = "UPDATE refreshmentpoint SET "
                    + "Name = '" + refreshmentPoint.getName() + "', Description = '"
                    + refreshmentPoint.getDescription() + "', Phone = '"
                    + refreshmentPoint.getPhone() + "', Latitude = "
                    + refreshmentPoint.getPosition().getLatitude() + ", Longitude = "
                    + refreshmentPoint.getPosition().getLongitude() + ", Altitude = "
                    + refreshmentPoint.getPosition().getAltitude() + ", OpeningTime = '"
                    + openingTime.toString() + "', ClosingTime = '"
                    + closingTime.toString() + "', ClosingDay = '"
                    + refreshmentPoint.getClosingDay() + "', Location = '"
                    + refreshmentPoint.getLocation() + "', City = '"
                    + refreshmentPoint.getCity() + "', Street = '"
                    + refreshmentPoint.getStreet() + "', Postcode = '"
                    + refreshmentPoint.getPostcode() + "', Province = '"
                    + refreshmentPoint.getState() + "', AverageRating = "
                    + refreshmentPoint.getAverageRating() + ", NumberOfVotes = "
                    + refreshmentPoint.getNumberOfVotes() + ", VATNumber = '"
                    + refreshmentPoint.getVATNumber() + "' WHERE Id = "
                    + refreshmentPoint.getId();
            // Execute the modification query
            int i = stat.executeUpdate(query);
            // Return the result
            return (i == 1);
        } finally {
            // Always close the statement and release the connection
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                DBConnectionPool.releaseConnection(conn);
            }
        }
    }

    public int getNumberOfSearchItems(String keyword, ArrayList<BeanTag> tags, Point3D position, double maxDistance) throws SQLException {
        // Variables for database connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        // Variable for the query results
        ResultSet result = null;
        int count = 0;
        try {
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query to get the number of pages
            String query = "";
            if (tags.size() == 0) {
                // Query without tags
                query = "SELECT count(number) FROM (SELECT count(refreshmentpoint.Id) AS number "
                    + "FROM refreshmentpoint "
                    + "WHERE (refreshmentpoint.Nome LIKE '%" + keyword + "%' OR refreshmentpoint.Description LIKE '%" + keyword + "%') "
                    + "AND distance(refreshmentpoint.Latitudine, refreshmentpoint.Longitudine, " + position.getLatitude() + ", " + position.getLongitude() + ") < " + maxDistance + " "
                    + "GROUP BY refreshmentpoint.Id "
                    + "ORDER BY count  (refreshmentpoint.Id) DESC) AS table";
            } else {
                // Query with tags
                query = "SELECT count(number) FROM (SELECT count(refreshmentpoint.Id) AS number "
                    + "FROM refreshmentpoint "
                    + "JOIN (membership JOIN tag ON IdTag = Id) "
                    + "ON refreshmentpoint.Id = membership.Idrefreshmentpoint "
                    + "WHERE (refreshmentpoint.Nome LIKE '%" + keyword + "%' OR refreshmentpoint.Description LIKE '%" + keyword + "%') AND (tag.Nome = '" + tags.get(0).getName() + "'";
                for (int i = 1; i < Math.min(tags.size(), 5); i++) {
                    query += " OR tag.Nome = '" + tags.get(i).getName() + "'";
                }
                query += ") "
                    + "AND distance(refreshmentpoint.Latitudine, refreshmentpoint.Longitudine, " + position.getLatitude() + ", " + position.getLongitude() + ") < " + maxDistance + " "
                    + "GROUP BY refreshmentpoint.Id "
                    + "ORDER BY count(refreshmentpoint.Id) DESC) AS table";
            }
            // Execute the query
            result = stat.executeQuery(query);
            // Retrieve the count value
            if (result.next()) {
                count = result.getInt(1);
            }
        } finally {
            // Always close the Result, the Statement, and the Connection
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
        return count;
    }
}
public int getNumberOfAdvancedSearchItems(int touristId, String keyword, ArrayList<BeanTag> tags, Point3D position, double maxDistance) throws SQLException {
    // Variables for database connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    // Variable for the query results
    ResultSet result = null;
    int count = 0;
    try {
        // Get the connection
        conn = DBConnectionPool.getConnection();
        // Create the Statement
        stat = conn.createStatement();
        // Query to get the number of pages of advanced search
        String query = "";
        if (tags.size() == 0) {
            // Query without tags
            query = "SELECT count(number) FROM (SELECT count(refreshmentpoint.Id) AS number "
                + "FROM (refreshmentpoint LEFT JOIN "
                + "(SELECT Idrefreshmentpoint "
                + "FROM PreferenceAssociation, ("
                + "SELECT IdSearchPreferences "
                + "FROM rating "
                + "WHERE IdTourist = " + touristId
                + ") AS pref "
                + "WHERE PreferenceAssociation.IdSearchPreferences = pref.IdSearchPreferences) "
                + "AS Preferences ON refreshmentpoint.Id = Preferences.Idrefreshmentpoint) "
                + "WHERE (refreshmentpoint.Name LIKE '%" + keyword + "%' OR refreshmentpoint.Description LIKE '%" + keyword + "%') "
                + "AND distance(refreshmentpoint.Latitude, refreshmentpoint.Longitude, " + position.getLatitude() + ", " + position.getLongitude() + ") < " + maxDistance + " "
                + "GROUP BY refreshmentpoint.Id "
                + "ORDER BY count(refreshmentpoint.Id) DESC) AS table";
        } else {
            // Query with tags
            query = "SELECT count(number) FROM (SELECT count(refreshmentpoint.Id) AS number "
                + "FROM (refreshmentpoint LEFT JOIN "
                + "(SELECT Idrefreshmentpoint "
                + "FROM PreferenceAssociation, ("
                + "SELECT IdSearchPreferences "
                + "FROM rating "
                + "WHERE IdTourist = " + touristId
                + ") AS pref "
                + "WHERE PreferenceAssociation.IdSearchPreferences = pref.IdSearchPreferences) "
                + "AS Preferences ON refreshmentpoint.Id = Preferences.Idrefreshmentpoint) "
                + "JOIN (membership JOIN tag ON IdTag = Id) "
                + "ON refreshmentpoint.Id = membership.Idrefreshmentpoint "
                + "WHERE (refreshmentpoint.Name LIKE '%" + keyword + "%' OR refreshmentpoint.Description LIKE '%" + keyword + "%') AND (tag.Name = '" + tags.get(0).getName() + "'";
            for (int i = 1; i < Math.min(tags.size(), 5); i++) {
                query += " OR tag.Name = '" + tags.get(i).getName() + "'";
            }
            query += ") "
                + "AND distance(refreshmentpoint.Latitude, refreshmentpoint.Longitude, " + position.getLatitude() + ", " + position.getLongitude() + ") < " + maxDistance + " "
                + "GROUP BY refreshmentpoint.Id "
                + "ORDER BY count(refreshmentpoint.Id) DESC) AS table";
        }
        // Execute the query
        result = stat.executeQuery(query);
        // Retrieve the count value
        if (result.next()) {
            count = result.getInt(1);
        }
    } finally {
        // Always close the Result, the Statement, and the Connection
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
    return count;
}

public RestaurantPointBean getRefreshmentPoint(int pid) throws SQLException {
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
        // Query
        String query = "SELECT * FROM refreshmentpoint WHERE Id =" + pid;
        // Execute the query
        result = stat.executeQuery(query);
        if (result.next()) {
            // Create the objects to be returned
            Point3D point = new Point3D(result.getDouble("Latitude"), result.getDouble("Longitude"), result.getDouble("Altitude"));
            Time openingTime = result.getTime("OpeningTime");
            Time closingTime = result.getTime("ClosingTime");
            // Generate the bean for the refreshment spot
            RestaurantPointBean refreshmentPoint = new RestaurantPointBean(result.getInt("Id"), result.getInt("NumberRatings"), result.getDouble("AverageRatings"), result.getString("Name"), result.getString("Description"), result.getString("Telephone"), result.getString("Location"), result.getString("City"), result.getString("Street"), result.getString("Postcode"), result.getString("Province"), result.getString("VAT"), point, openingTime, closingTime, result.getString("ClosingDay"));
            // Return the refreshment
            return refreshmentPoint;
        } else {
            return null;
        }
    } finally {
        // Always close the Result, the Statement, and the Connection
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

public ArrayList<RestaurantPointBean> search(String keyword,
        ArrayList<BeanTag> tags, int pageNumber,
        int elementsPerPage, Point3D position,
        double maxDistance) throws SQLException {
    
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

        // Query for search
        String query = "";
        if (tags.size() == 0) {
            // Query without tags
            query = "SELECT * FROM refreshmentpoint "
                    + "WHERE (refreshmentpoint.Name LIKE '%" + keyword
                    + "%' OR refreshmentpoint.Description LIKE '%" + keyword
                    + "%') "
                    + "AND distance(refreshmentpoint.Latitude, refreshmentpoint.Longitude, "
                    + position.getLatitude() + ", " + position.getLongitude() + ") < "
                    + maxDistance
                    + " GROUP BY refreshmentpoint.Id ORDER BY count(refreshmentpoint.Id) DESC LIMIT "
                    + (pageNumber * elementsPerPage) + ", " + elementsPerPage;
        } else {
            // Query with tags
            query = "SELECT * FROM refreshmentpoint "
                    + "JOIN (membership JOIN tag ON IdTag = Id) "
                    + "ON refreshmentpoint.Id = membership.Idrefreshmentpoint "
                    + "WHERE (refreshmentpoint.Name LIKE '%" + keyword
                    + "%' OR refreshmentpoint.Description LIKE '%" + keyword
                    + "%') AND (tag.Name = '" + tags.get(0).getName() + "'";
            for (int i = 1; i < Math.min(tags.size(), 5); i++) {
                query += " OR tag.Name = '" + tags.get(i).getName() + "'";
            }
            query += ") AND distance(refreshmentpoint.Latitude, refreshmentpoint.Longitude, "
                    + position.getLatitude() + ", " + position.getLongitude() + ") < "
                    + maxDistance
                    + " GROUP BY refreshmentpoint.Id ORDER BY count(refreshmentpoint.Id) DESC LIMIT "
                    + (pageNumber * elementsPerPage) + ", " + elementsPerPage;
        }

        // Execute the query
        result = stat.executeQuery(query);

        // List that will contain the RestaurantPointBean
        ArrayList<RestaurantPointBean> list = new ArrayList<>();

        while (result.next()) {
            // Create the objects to be returned
            Point3D point = new Point3D(result.getDouble("Latitude"),
                    result.getDouble("Longitude"), result.getDouble("Altitude"));
            Time openingTime = result.getTime("OpeningTime");
            Time closingTime = result.getTime("ClosingTime");

            // Build the refreshment
            RestaurantPointBean tempPoint = new RestaurantPointBean(result.getInt("Id"),
                    result.getInt("NumberRatings"), result.getDouble("AverageRatings"),
                    result.getString("Name"), result.getString("Description"),
                    result.getString("Telephone"), result.getString("Location"),
                    result.getString("City"), result.getString("Street"),
                    result.getString("Postcode"), result.getString("Province"),
                    result.getString("VAT"), point, openingTime, closingTime,
                    result.getString("ClosingDay"));

            // Insert the bean in the list
            list.add(tempPoint);
        }

        // Return the list
        return list;
    } finally {
        // Always close the Result, the Statement, and the Connection
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
public ArrayList<RestaurantPointBean> advancedSearch(int touristId, String keyword,
        ArrayList<BeanTag> tags, int pageNumber, int itemsPerPage,
        Point3D position, double maxDistance) throws SQLException {
    
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
        // Query for advanced search
        String query = "";
        if (tags.size() == 0) {
            // Query without tags
            query = "SELECT * FROM (refreshmentpoint LEFT JOIN " +
                    "(SELECT Idrefreshmentpoint FROM associationpr, " +
                    "(SELECT IdSearchPreferences FROM rating " +
                    "WHERE IdTourist = " + touristId + ") AS pref " +
                    "WHERE associationpr.IdSearchPreferences = pref.IdSearchPreferences) " +
                    "Preferences ON refreshmentpoint.Id = Preferences.Idrefreshmentpoint) " +
                    "WHERE (refreshmentpoint.Name LIKE '%" + keyword + "%' " +
                    "OR refreshmentpoint.Description LIKE '%" + keyword + "%') " +
                    "AND distance(refreshmentpoint.Latitude, refreshmentpoint.Longitude, " +
                    position.getLatitude() + ", " + position.getLongitude() + ") < " + maxDistance +
                    " GROUP BY refreshmentpoint.Id ORDER BY count(refreshmentpoint.Id) DESC LIMIT " +
                    (pageNumber * itemsPerPage) + ", " + itemsPerPage;
        } else {
            // Query with tags
            query = "SELECT * FROM (refreshmentpoint LEFT JOIN " +
                    "(SELECT Idrefreshmentpoint FROM associationpr, " +
                    "(SELECT IdSearchPreferences FROM rating " +
                    "WHERE IdTourist = " + touristId + ") AS pref " +
                    "WHERE associationpr.IdSearchPreferences = pref.IdSearchPreferences) " +
                    "Preferences ON refreshmentpoint.Id = Preferences.Idrefreshmentpoint) " +
                    "JOIN (membership JOIN tag ON IdTag = Id) " +
                    "ON refreshmentpoint.Id = membership.Idrefreshmentpoint " +
                    "WHERE (refreshmentpoint.Name LIKE '%" + keyword + "%' " +
                    "OR refreshmentpoint.Description LIKE '%" + keyword + "%') " +
                    "AND (tag.Name = '" + tags.get(0).getName() + "'";
            for (int i = 1; i < tags.size(); i++) {
                query += " OR tag.Name = '" + tags.get(i).getName() + "'";
            }
            query += ") AND distance(refreshmentpoint.Latitude, refreshmentpoint.Longitude, " +
                    position.getLatitude() + ", " + position.getLongitude() + ") < " +
                    maxDistance + " GROUP BY refreshmentpoint.Id ORDER BY count(refreshmentpoint.Id) DESC LIMIT " +
                    (pageNumber * itemsPerPage) + ", " + itemsPerPage;
        }
        // Execute the query
        result = stat.executeQuery(query);

        ArrayList<RestaurantPointBean> list = new ArrayList<>();
        while (result.next()) {
            // Create the objects to be returned
            Point3D point = new Point3D(result.getDouble("Latitude"), result.getDouble("Longitude"),
                    result.getDouble("Elevation"));
            Time openingTime = result.getTime("OpeningTime");
            Time closingTime = result.getTime("ClosingTime");
            // Create the bean for the refreshment
            RestaurantPointBean tempPoint = new RestaurantPointBean(result.getInt("Id"),
                    result.getInt("NumberRatings"), result.getDouble("AverageRatings"),
                    result.getString("Name"), result.getString("Description"),
                    result.getString("Telephone"), result.getString("Location"),
                    result.getString("City"), result.getString("Street"),
                    result.getString("Postcode"), result.getString("Province"),
                    result.getString("VAT"), point, openingTime,
                    closingTime, result.getString("ClosingDay"));
            // Add the bean to the list
            list.add(tempPoint);
        }
        // Return the list
        return list;
    } finally {
        // Always close the Result, the Statement, and the Connection
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

public ArrayList<RestaurantPointBean> getListOfPR() throws SQLException {
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
        // Query for a list of all the refreshment
        String query = "SELECT * FROM refreshmentpoint";
        // Execute the query
        result = stat.executeQuery(query);
        // List that will contain the RestaurantPointBean
        ArrayList<RestaurantPointBean> list = new ArrayList<>();
        while (result.next()) {
            // Create the objects to be returned
            Point3D point = new Point3D(result.getDouble("Latitude"),
                    result.getDouble("Longitude"),
                    result.getDouble("Altitude"));
            Time openingTime = result.getTime("OpeningTime");
            Time closingTime = result.getTime("ClosingTime");
            // Build the refreshment
            RestaurantPointBean tempPoint = new RestaurantPointBean(
                    result.getInt("Id"),
                    result.getInt("NumberRatings"),
                    result.getDouble("AverageRatings"),
                    result.getString("Name"),
                    result.getString("Description"),
                    result.getString("Telephone"),
                    result.getString("Location"),
                    result.getString("City"),
                    result.getString("Street"),
                    result.getString("Postcode"),
                    result.getString("Province"),
                    result.getString("VAT"),
                    point,
                    openingTime,
                    closingTime,
                    result.getString("ClosingDay"));
            // Insert the bean in the list
            list.add(tempPoint);
        }
        // Return the list
        return list;
    } finally {
        // Always close the Result, the Statement, and the Connection
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