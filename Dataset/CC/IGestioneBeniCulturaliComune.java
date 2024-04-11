package unisa.gps.etour.control.GestioneBeniCulturali;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import unisa.gps.etour.bean.BeanCulturalAsset;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitToCulturalAsset;

/**
 * Interface for operations common to users and operators of the Cultural Assets Agency.
 *
 * @Author Michelangelo De Simone
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
public interface CommonCulturalAssetManagement extends Remote {

    /**
     * Method to retrieve a particular Cultural Asset.
     *
     * @param culturalAssetID The identifier of the cultural asset to be retrieved
     * @return BeanCulturalAsset Contains required data of the Cultural Asset
     * @throws RemoteException Exception flow
     */
    public BeanCulturalAsset getCulturalAsset(int culturalAssetID) throws RemoteException;

    /**
     * Returns the list of tags of a cultural asset.
     *
     * @param culturalAssetID The ID of the Cultural Asset
     * @return ArrayList of the <BeanTag> tags specified for the Cultural Asset
     * @throws RemoteException Exception flow
     */
    public ArrayList<BeanTag> getTagsOfCulturalAsset(int culturalAssetID) throws RemoteException;

    /**
     * Returns a list of feedback for the specified cultural asset.
     *
     * @param culturalAssetID The ID of the Cultural Asset
     * @return HashMap<BeanVisitToCulturalAsset, String> The feedback for the Cultural Asset
     * @throws RemoteException Exception flow
     */
    public HashMap<BeanVisitToCulturalAsset, String> getFeedbackOfCulturalAsset(int culturalAssetID) throws RemoteException;

    /**
     * Returns an array for the specified cultural asset, where each position contains the number of
     * feedback corresponding to the value of the array greater than one.
     * The calculation is made in the period between 30 days ago and today.
     *
     * @param culturalAssetID The ID of the Cultural Asset
     * @return ArrayList<Integer> The statistics of the last thirty days
     * @throws RemoteException Exception flow
     */
    public ArrayList<Integer> getStatisticsOfCulturalAsset(int culturalAssetID) throws RemoteException;

    /**
     * Method for updating (or modifying) feedback for a certain Cultural Asset.
     *
     * @param culturalAssetID The ID of the Cultural Asset to modify the feedback
     * @param visitToCulturalAsset The new feedback for the indicated Cultural Asset
     * @return boolean The result of the operation; true if successful, false otherwise
     * @throws RemoteException Exception flow
     */
    public boolean modifyFeedbackOfCulturalAsset(int culturalAssetID, BeanVisitToCulturalAsset visitToCulturalAsset) throws RemoteException;
}
