package unisa.gps.etour.control.GestioneTag;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.repository.DBTag;
import unisa.gps.etour.repository.IDBTag;
import unisa.gps.etour.util.MessageError;

/**
 * Class that implements common tasks for managing tags
 *
 * @Author Joseph Morelli
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University Of Salerno
 */
public class ManagementofCommonTags extends UnicastRemoteObject implements IManagementofCommonTags {

    private static final long serialVersionUID = 1L;
    // Object for the database connection
    protected IDBTag tags;

    public ManagementofCommonTags() throws RemoteException {
        super();
        // Connect to the Database
        try {
            tags = new DBTag();
        } catch (Exception e) {
            throw new RemoteException(MessageError.Error_SCONOSCIUTO);
        }
    }

    // Method that returns all tags
    public ArrayList<BeanTag> getTags() throws RemoteException {
        // ArrayList to fill with the tags to return
        ArrayList<BeanTag> toReturn;
        // Retrieve data from Database
        try {
            // Get the information from the Database
            toReturn = tags.getListTag();
        } catch (SQLException e) {
            System.out.println("Error in method getTags: " + e.toString());
            throw new RemoteException(MessageError.ERROR_DBMS);
        } catch (Exception ee) {
            System.out.println("Error in method getTags" + ee.toString());
            throw new RemoteException(MessageError.UNKNOWN_ERROR);
        }
        // Check the data returned to avoid returning null values
        if (null == toReturn) {
            throw new RemoteException(MessageError.BEAN_FORMAT_ERROR);
        }
        return toReturn;
    }
}
