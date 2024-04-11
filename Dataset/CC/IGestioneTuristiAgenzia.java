package unisa.gps.etour.control.GestioneUtentiRegistrati;

import java.rmi.RemoteException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTurista;
import unisa.gps.etour.bean.BeanVisitaBC;
import unisa.gps.etour.bean.BeanVisitaPR;

/**
 * Interface for managing tourists from the perspective of the agency
 *
 * @Author Joseph Morelli
 * @Version 0.1
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface TouristManagementAgency extends CommonTouristManagement {

    /**
     * Method for deleting a tourist from the database
     *
     * @param touristID Identifier of the tourist to delete
     * @return Boolean: true if the operation is successful, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean deleteTourist(int touristID) throws RemoteException;

    /**
     * Method to activate a registered tourist
     *
     * @param touristID ID of the tourist to activate
     * @return Boolean: true if the operation is successful, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean activateTourist(int touristID) throws RemoteException;

    /**
     * Method to deactivate an active tourist
     *
     * @param touristID Identifier of the tourist to deactivate
     * @return Boolean: true if the operation is successful, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean deactivateTourist(int touristID) throws RemoteException;

    /**
     * Method to obtain a collection of tourists
     *
     * @return ArrayList of BeanTurista
     * @throws RemoteException Remote Exception
     */
    public ArrayList<BeanTurista> getTourists() throws RemoteException;

    /**
     * Method to obtain a collection of active or inactive tourists
     *
     * @param accountStatus State of the tourist account (active or inactive)
     * @return ArrayList of BeanTurista
     * @throws RemoteException Remote Exception
     */
    public ArrayList<BeanTurista> getTourists(boolean accountStatus) throws RemoteException;

    /**
     * Method to retrieve all the feedback issued by a tourist for the refreshment points
     *
     * @param touristID ID of the tourist to retrieve feedback for
     * @return ArrayList containing all the released feedback beans
     * @throws RemoteException Remote Exception
     */
    public ArrayList<BeanVisitaPR> getFeedbackPR(int touristID) throws RemoteException;

    /**
     * Method to retrieve all the feedback issued by a tourist for cultural heritage
     *
     * @param touristID ID of the tourist to retrieve feedback for
     * @return ArrayList containing all the released feedback beans
     * @throws RemoteException Remote Exception
     */
    public ArrayList<BeanVisitaBC> getFeedbackBC(int touristID) throws RemoteException;
}