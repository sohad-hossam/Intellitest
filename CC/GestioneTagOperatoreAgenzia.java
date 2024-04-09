package unisa.gps.etour.control.GestioneTag;

import java.rmi.RemoteException;
import java.sql.SQLException;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.util.ErrorMessages;

/**
 * Class that implements the methods for the functionality of the Operator Agency
 * Extending the class of common Tag Management
 *
 * @Author Joseph Morelli
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class TagManagementOperatorAgency extends CommonTagManagement implements
OperatorAgencyTagManagement {

    private static final long serialVersionUID = 1L;

    public TagManagementOperatorAgency() throws RemoteException {
        // Invoke the constructor of the superclass for communication with
        // Database
        super();
    }

    // Method to delete from database the tag whose ID is passed
    // As parameter
    public boolean deleteTag(int tagID) throws RemoteException {
        // Check the value of passed data
        if (tagID <= 0)
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        try {
            // Make the database operation
            tag.deleteTag(tagID);
            return true;
        } catch (SQLException e) {
            System.out.println("Error in method deleteTag:" + e.toString());
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method deleteTag" + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    // Method that allows the insertion of a new tag as a parameter
    public boolean insertTag(BeanTag newTag) throws RemoteException {
        // Check the validity of the Bean and the data contained within
        if (null == newTag)
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        if ((newTag = checkTag(newTag)) == null)
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        try {
            // Execute the operation on the Database
            tag.insertTag(newTag);
            return true;
        } catch (SQLException e) {
            System.out.println("Error in method insertTag" + e.toString());
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method insertTag" + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    // Method that enables the modification of a tag that is passed as
    // Parameter
    public boolean modifyTag(BeanTag modifiedTag) throws RemoteException {
        // Check the validity of data
        if ((modifiedTag = checkTag(modifiedTag)) == null)
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        try {
            // Execute the operation on the Database
            tag.modifyTag(modifiedTag);
            return true;
        } catch (SQLException e) {
            System.out.println("Error in method modifyTag:" + e.toString());
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method modifyTag" + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    // Method to obtain the tags whose identifier is passed
    // As parameter
    public BeanTag getTag(int tagID) throws RemoteException {
        // Check the validity of data
        if (tagID <= 0)
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        // Bean to return
        BeanTag toReturn;
        try {
            // Execute the operation on the Database
            toReturn = tag.getTag(tagID);
        } catch (SQLException e) {
            System.out.println("Error in method getTag:" + e.toString());
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method getTag:" + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        if (null == toReturn)
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        return toReturn;
    }

    // Method that controls all the attributes of a BeanTag
    private BeanTag checkTag(BeanTag toControl) {
        // Check the ID
        if (toControl.getId() <= 0)
            return null;
        // Check the description
        if (toControl.getDescription().equals(""))
            toControl.setDescription("***");
        // Check the name
        if (toControl.getName().equals(""))
            return null;
        // Check that the name does not contain a space
        if (toControl.getName().contains(""))
            return null;
        return toControl;
    }
}
