package unisa.gps.etour.control.UserManagement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import unisa.gps.etour.bean.TouristBean;
import unisa.gps.etour.repository.TouristDB;
import unisa.gps.etour.repository.ITouristDB;
import unisa.gps.etour.util.ErrorMessages;

/**
 * Class that implements the common tasks for Operators and Tourist Agency
 * such as modifyTourist and getTourist
 *
 * @Author Joseph Morelli
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class CommonTouristManagement extends UnicastRemoteObject implements ICommonTouristManagement {

    protected ITouristDB tourist;

    // Constructor that invokes the superclass constructor
    // UnicastRemoteObject to connect via RMI
    // Instantiate and connect to the database
    public CommonTouristManagement() throws RemoteException {
        super();
        // Connect to the Database
        try {
            tourist = new TouristDB();
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    // Method that allows you to change the data of a tourist through its data
    public boolean modifyTourist(TouristBean profile) throws RemoteException {
        // Check the validity of passed data
        if (profile == null || !(profile instanceof TouristBean)) {
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        }
        // Execution of the amendment
        try {
            // If the changes were made, return true
            if (tourist.modifyTourist(profile)) {
                return true;
            }
        } catch (SQLException e) {
            // If the data layer sends an exception, throw the remote exception
            System.out.println("Error in method modifyTourist: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method modifyTourist: " + ee.toString());
            // Unexpected exception caused by other factors
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        // If there were no exceptions but the changes were not made, return false
        return false;
    }

    // Method to obtain the bean with data from the Tourist identified by the passed parameter
    public TouristBean getTourist(int touristId) throws RemoteException {
        // Check the validity of the identifier
        if (touristId < 0) {
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        }
        TouristBean toReturn = null; // variable return
        // Retrieve data
        try {
            // Request the bean layer to return the tourist bean
            // With id equal to touristId
            toReturn = tourist.getTourist(touristId);
            if (toReturn == null) {
                throw new RemoteException(ErrorMessages.DB_ERROR);
            }
        } catch (SQLException e) {
            // If the data layer sends an exception, throw the remote exception
            System.out.println("Error in method getTourist: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method getTourist: " + ee.toString());
            // Unexpected exceptions caused by other factors
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        // Return the result
        return toReturn;
    }
}