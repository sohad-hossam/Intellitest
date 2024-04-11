package unisa.gps.etour.control.GestioneUtentiRegistrati;

import java.rmi.RemoteException;

import unisa.gps.etour.bean.BeanBeneCulturale;
import unisa.gps.etour.bean.BeanPreferenzaDiRicerca;
import unisa.gps.etour.bean.BeanPreferenzeGeneriche;
import unisa.gps.etour.bean.BeanPuntoDiRistoro;
import unisa.gps.etour.bean.BeanTurista;
import unisa.gps.etour.bean.BeanVisitaBC;
import unisa.gps.etour.bean.BeanVisitaPR;

/**
 * Interface for managing tourist information.
 *
 * @Author Joseph Penna, Federico Leon
 * @Version 0.1
 * 2007 eTour Project - Copyright by SE @ SA Lab University of DMI
 * Salerno
 */
public interface TouristClientManagement extends CommonTouristManagement {

    /**
     * Method for inserting a Tourist.
     *
     * @param tourist container for all data relating to tourism to insert
     * @return Boolean: True if the insertion is successful, False otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean insertTourist(BeanTurista tourist) throws RemoteException;

    /**
     * Method for including the General Preferences of a Tourist.
     *
     * @param touristID Identifier of the Tourist whose General Preferences are to be included
     * @param generalPreferences General Preferences to be included
     * @return Boolean: True if the insertion is successful, False otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean insertGeneralPreferences(int touristID, BeanPreferenzeGeneriche generalPreferences) throws RemoteException;

    /**
     * Method for extracting the General Preferences given by a Tourist.
     *
     * @param touristID Identifier of the Tourist for whom General Preferences are to be obtained
     * @return General Preferences information related to tourism
     * @throws RemoteException Remote Exception
     */
    public BeanPreferenzeGeneriche getGeneralPreferences(int touristID) throws RemoteException;

    /**
     * Method for changing the General Preferences given by a Tourist.
     *
     * @param newGeneralPreferences New General Preferences to be included
     * @return Boolean: True if the modification is successful, False otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean modifyGeneralPreferences(BeanPreferenzeGeneriche newGeneralPreferences) throws RemoteException;

    /**
     * Method for deleting General Preferences associated with a Tourist.
     *
     * @param touristID Identifier of the Tourist for whom General Preferences are to be deleted
     * @return General Preferences deleted
     * @throws RemoteException Remote Exception
     */
    public BeanPreferenzeGeneriche deleteGeneralPreferences(int touristID) throws RemoteException;

    // Similar methods for search preferences, visited cultural heritage, visited refreshments, etc.
}