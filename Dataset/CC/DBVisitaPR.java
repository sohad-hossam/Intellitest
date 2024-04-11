package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import unisa.gps.etour.bean.BeanVisitPR;

/**
 * Class that implements the interface IDBVisitPR
 *
 * @Author Joseph Martone
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University Of Salerno
 */
public class DBVisitPR implements IDBVisitPR {

    // Empty constructor
    public DBVisitPR() {

    }

    public boolean inserisciVisitPR(BeanVisitPR PVIS) throws SQLException {
        // Variable for the connection
        Connection conn = null;
        // Variable for the query
        Statement stat = null;
        // Variable for the query results
        ResultSet result = null;
        try {
            // Create the date of visit
            java.sql.Date dataVisit = new Date(PVisit.getDataVisit().getTime());
            // Get the connection
            conn = DBConnectionPool.getConnection();
            // Create the Statement
            stat = conn.createStatement();
            // Query to get the average rating of a property
            String queryMediaVotes = "SELECT MediaVotes, NumberOfVotes FROM Refreshmentpoint WHERE Id = "
                    + PVisit.getIdRefreshmentpoint();
            result = stat.executeQuery(queryMediaVotes);
            // Variable for the average rating
            double average = 0;
            // Variable for the number of votes
            int NumberOfVotes = 0;
            if (result.next()) {
                average = result.getDouble("MediaVotes");
                NumberOfVotes = result.getInt("NumberOfVotes");
                average = ((average * NumberOfVotes) + pVisit.getVotes()) / (NumberOfVotes + 1);
            }
            // Query for the insertion
            String query = "INSERT INTO VisitPR (IdTourist, IdRefreshmentpoint, DataVisit, Vote, Comment) VALUES ("
                    + PVisit.getIdTourist() + ", "
                    + PVisit.getIdRefreshmentpoint() + ", '"
                    + dataVisit + "', "
                    + PVisit.getVoto() + ", '"
                    + pVisit.getCommento() + "')";
            String query2 = "UPDATE Refreshmentpoint SET MediaVotes = " + average
                    + ", NumberOfVotes = " + (NumberOfVotes + 1) + " WHERE Id = "
                    + PVisit.getIdRefreshmentpoint();
            // Execute the insert query
            stat.executeQuery("BEGIN");
            int i = stat.executeUpdate(query);
            i *= stat.executeUpdate(query2);
            stat.executeQuery("COMMIT");
            // Return the result of the operation
            return i == 1;
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

public boolean modificaVisitPR(BeanVisitPR PVIS) throws SQLException {
    // Variable for the connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    try {
        // Create the date of visit
        java.sql.Date dataVisit = new Date(pVisit.getDataVisit().getTime());
        // Get the connection
        DBConnectionPool.getConnection conn = ();
        // Create the Statement
        stat = conn.createStatement();
        // Query for amendment
        String query = "UPDATE VisitPR SET DataVisit = '" + dataVisit + "', Comment = '" + pVisit.getCommento()
                + "' WHERE IdRefreshmentpoint = " + pVisit.getIdRefreshmentpoint() + " AND IdTourist = "
                + pVisit.getIdTourist();
        // You run the query for Change
        int i = stat.executeUpdate(query);
        // This returns the backup
        return (i == 1);
    }
    // Always runs and takes care to close the Result, the Statement
    // And Connection
    finally {
        if (stat != null) {
            stat.close();
        }
        if (conn != null) {
            DBConnectionPool.releaseConnection(conn);
        }
    }
}
public ArrayList<BeanVisitPR> getListVisitPR(int pIdRefreshmentpoint) throws SQLException {
    // Variable for the connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    // Variable for the query results
    ResultSet result = null;
    try {
        // Get the connection
        DBConnectionPool.getConnection conn = ();
        // Create the Statement
        stat = conn.createStatement();
        // Query to extract the list of requests for a Refreshment
        String query = "SELECT * FROM VisitPR WHERE IdRefreshmentpoint =" + pIdRefreshmentpoint;
        // The query is executed
        result = stat.executeQuery(query);
        ArrayList<BeanVisitPR> list = new ArrayList<BeanVisitPR>();
        // We extract the results from the result set and moves in List
        // To be returned
        while (result.next()) {
            java.util.Date dataVisit = new java.util.Date(result.getDate("DataVisit").getTime());
            list.add(new BeanVisitPR(result.getInt("Customer"), result.getInt("IdRefreshmentpoint"),
                    result.getString("Comment"), result.getInt("IdTourist"), dataVisit));
        }
        return list;
    }
    // Always runs and takes care to close the Result, the Statement
    // And Connection
    finally {
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

public ArrayList<BeanVisitPR> getListVisitPRTourist(int pIdTourist) throws SQLException {
    // Variable for the connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    // Variable for the query results
    ResultSet result = null;
    try {
        // Get the connection
        DBConnectionPool.getConnection conn = ();
        // Create the Statement
        stat = conn.createStatement();
        // Query to extract the list of requests for a Eating place for tourists
        String query = "SELECT * FROM VisitPR WHERE IdTourist =" + pIdTourist;
        // The query is executed
        result = stat.executeQuery(query);
        // List that will contain the BeanVisitPR
        ArrayList<BeanVisitPR> list = new ArrayList<BeanVisitPR>();
        // We extract the results from the result set and moves in List to be returned
        while (result.next()) {
            // Add to the list BeanVisitPR
            java.util.Date dataVisit = new java.util.Date(result.getDate("DataVisit").getTime());
            list.add(new BeanVisitPR(result.getInt("Customer"), result.getInt("IdRefreshmentpoint"),
                    result.getString("Comment"), result.getInt("IdTourist"), dataVisit));
        }
        // Return the list
        return list;
    }
    // Always runs and takes care to close the Result, the Statement and Connection
    finally {
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

public BeanVisitPR getVisitPR(int pIdRefreshmentpoint, int pIdTourist) throws SQLException {
    // Variable for the connection
    Connection conn = null;
    // Variable for the query
    Statement stat = null;
    // Variable for the query results
    ResultSet result = null;
    try {
        // Get the connection
        DBConnectionPool.getConnection conn = ();
        // Create the Statement
        stat = conn.createStatement();
        // Query for the extraction of the visit made by a tourist to a given point of comfort
        String query = "SELECT * FROM VisitPR WHERE IdRefreshmentpoint =" + pIdRefreshmentpoint + " AND IdTourist =" + pIdTourist;
        // The query is executed
        result = stat.executeQuery(query);
        // Get the bean's visit sought based on the ID of the tourist and of refreshment
        BeanVisitPR beanTemp = null;
        if (result.next()) {
            // Create the BeanVisitPR
            java.util.Date dataVisit = new java.util.Date(result.getDate("DataVisit").getTime());
            beanTemp = new BeanVisitPR(result.getInt("Customer"), result.getInt("IdRefreshmentpoint"),
                    result.getString("Comment"), result.getInt("IdTourist"), dataVisit);
        }
        // Return the BeanTemp
        return beanTemp;
    }
    // Always runs and takes care to close the Result, the Statement and Connection
    finally {
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