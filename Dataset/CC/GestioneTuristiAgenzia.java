package unisa.gps.etour.control.UserManagement;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import unisa.gps.etour.bean.TouristBean;
import unisa.gps.etour.bean.CulturalVisitBean;
import unisa.gps.etour.bean.RestaurantVisitBean;
import unisa.gps.etour.repository.CulturalVisitDB;
import unisa.gps.etour.repository.RestaurantVisitDB;
import unisa.gps.etour.repository.ICulturalVisitDB;
import unisa.gps.etour.repository.IRestaurantVisitDB;
import unisa.gps.etour.util.ErrorMessages;

/**
 * Class that implements the interface for managing Tourists' side
 * Agency and extends UnicastRemoteObject for remote communication
 * Provides basic methods for handling and additional methods for retrieving
 * Tourists with special characteristics
 *
 * @Author Joseph Morelli
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public class AgencyTouristManagement extends CommonTouristManagement implements IAgencyTouristManagement {

    private ICulturalVisitDB feedbackCultural;
    private IRestaurantVisitDB feedbackRestaurant;

    public AgencyTouristManagement() throws RemoteException {
        super();
        try {
            // Instantiate objects for database connections
            feedbackCultural = new CulturalVisitDB();
            feedbackRestaurant = new RestaurantVisitDB();
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    // Method for obtaining all tourists present in the database
    public ArrayList<TouristBean> getTourists() throws RemoteException {
        ArrayList<TouristBean> toReturn;
        try {
            // Invoke the method with empty string to get all Tourists
            toReturn = tourist.getTourists("");
            if (toReturn == null) {
                throw new RemoteException(ErrorMessages.DB_ERROR);
            }
        } catch (SQLException e) {
            System.out.println("Error in method getTourists: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method getTourists: " + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        if (toReturn == null) {
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        }
        return toReturn;
    }

    // Method that allows to obtain all the tourists who have an account active or not
    public ArrayList<TouristBean> getTourists(boolean accountStatus) throws RemoteException {
        ArrayList<TouristBean> toReturn;
        try {
            toReturn = tourist.getTourists(accountStatus);
        } catch (SQLException e) {
            System.out.println("Error in method getTourists(boolean): " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method getTourists(boolean): " + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        if (toReturn == null) {
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        }
        return toReturn;
    }

    // Method that allows the activation of a tourist that is not yet activated
    public boolean activateTourist(int touristId) throws RemoteException {
        if (touristId < 0) {
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        }
        TouristBean toChange;
        try {
            toChange = tourist.getTourist(touristId);
            if (toChange.isActive()) {
                throw new RemoteException(ErrorMessages.DATA_ERROR);
            }
            toChange.setActive(true);
            if (tourist.modifyTourist(toChange)) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in method activateTourist: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method activateTourist: " + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        return false;
    }

    // Method that allows to deactivate a tourist
    public boolean deactivateTourist(int touristId) throws RemoteException {
        if (touristId < 0) {
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        }
        TouristBean toChange;
        try {
            toChange = tourist.getTourist(touristId);
            if (!toChange.isActive()) {
                throw new RemoteException(ErrorMessages.DATA_ERROR);
            }
            toChange.setActive(false);
            if (tourist.modifyTourist(toChange)) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in method deactivateTourist: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method deactivateTourist: " + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        return false;
    }

    // Method that deletes a tourist from the Database
    public boolean deleteTourist(int touristId) throws RemoteException {
        if (touristId < 0) {
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        }
        try {
            if (tourist.deleteTourist(touristId)) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error in deleteTourist method: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in deleteTourist method: " + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        return false;
    }

    // Method that returns an ArrayList containing the feedback issued by
    // Some for the Cultural Tourist
    public ArrayList<CulturalVisitBean> getFeedbackCultural(int touristId) throws RemoteException {
        if (touristId < 0) {
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        }
        ArrayList<CulturalVisitBean> toReturn;
        try {
            toReturn = feedbackCultural.getTouristCulturalFeedback(touristId);
        } catch (SQLException e) {
            System.out.println("Error in method getFeedbackCultural: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method getFeedbackCultural: " + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
        if (toReturn == null) {
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        }
        return toReturn;
    }

    // Method that returns an ArrayList containing the feedback issued by
    // Some for the Restaurant Tourist
    public ArrayList<RestaurantVisitBean> getFeedbackRestaurant(int touristId) throws RemoteException {
        if (touristId < 0) {
            throw new RemoteException(ErrorMessages.DATA_ERROR);
        }
        ArrayList<RestaurantVisitBean> toReturn;
        try {
            toReturn = feedbackRestaurant.getTouristRestaurantFeedback(touristId);
        } catch (SQLException e) {
            System.out.println("Error in method getFeedbackRestaurant: " + e.toString());
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception ee) {
            System.out.println("Error in method getFeedbackRestaurant: " + ee.toString());
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
       
if (toReturn == null) {
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);
        }
        return toReturn;
    }
}
