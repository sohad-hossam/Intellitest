package unisa.gps.etour.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates the connection to the database using JDBC and
 * Allows you to query both read and edit the contents of
 * Database. E 'implemented to provide a pool of connections to
 * Provide a connection to each thread.
 *
 * @ Author Mauro Miranda
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class DBConnectionPool {
    private final static String driver = "com.mysql.jdbc.Driver";
    private final static String connectionUrl = "jdbc:mysql://localhost/eTour?user=&password=mauro";
    private static List<Connection> freeConnections;

    /* Private constructor that initiates the connection to the database */

    /*
     * Static initialization block is used to load the driver
     * Memory
     */
    static {
        freeConnections = new ArrayList<>();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the connection to the server.
     *
     * @return Returns the database connection
     * @throws SQLException
     */
    public static synchronized Connection getConnection() throws SQLException {
        Connection connection;

        if (!freeConnections.isEmpty()) {
            // Extract a connection from the free db connection queue
            connection = freeConnections.get(0);
            freeConnections.remove(0);

            try {
                // If the connection is not valid, a new connection will be
                // Analyzed
                if (connection.isClosed())
                    connection = createConnection();
            } catch (SQLException e) {
                connection = createConnection();
            }
        } else {
            // The free db connection queue is empty, so a new connection will
            // Be created
            connection = createConnection();
        }

        return connection;
    }

    public static void releaseConnection(Connection releasedConnection) {
        // Add the connection to the free db connection queue
        freeConnections.add(releasedConnection);
    }

    private static Connection createConnection() throws SQLException {
        Connection newConnection = null;
        // Create a new db connection using the db properties
        newConnection = DriverManager.getConnection(connectionUrl);
        newConnection.setAutoCommit(true);
        return newConnection;
    }
}