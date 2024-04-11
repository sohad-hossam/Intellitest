package unisa.gps.etour.control.GestioneBeniCulturali;

import java.rmi.RemoteException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanCulturalAsset;
import unisa.gps.etour.bean.BeanCulturalAssetVisit;

/**
 * Interface for operations specific to cultural assets management
 * by the Agency Operator.
 *
 * @Author Michelangelo De Simone
 * @Version 0.1
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public interface CulturalAssetManagement extends AgencyCulturalAssetManagement, CommonCulturalAssetManagement {

    /**
     * Method for inserting a new cultural asset.
     *
     * @param culturalAsset The bean to be inserted into the database
     * @return boolean The result of the operation; true if successful, false otherwise
     * @throws RemoteException Exception flow
     */
    public boolean insertCulturalAsset(BeanCulturalAsset culturalAsset) throws RemoteException;

    /**
     * Method for deleting a cultural asset by ID.
     *
     * @param culturalAssetID The ID of the bean to be deleted
     * @return boolean The result of the operation; true if successful, false otherwise
     * @throws RemoteException Exception flow
     */
    public boolean deleteCulturalAsset(int culturalAssetID) throws RemoteException;

    /**
     * Method for retrieving all cultural assets from the database.
     *
     * @return ArrayList all the beans in the database
     * @throws RemoteException Exception flow
     */
    public ArrayList<BeanCulturalAsset> getAllCulturalAssets() throws RemoteException;

    /**
     * Method for updating (or changing) the data of a cultural asset.
     *
     * @param culturalAsset The bean with the new information of the cultural asset
     * @return boolean The result of the operation; true if successful, false otherwise
     * @throws RemoteException Exception flow
     */
    public boolean modifyCulturalAsset(BeanCulturalAsset culturalAsset) throws RemoteException;

    /**
     * Method for adding a tag to a certain cultural asset.
     *
     * @param culturalAssetID The identifier of the cultural asset to which to add a tag
     * @param tagID The ID tag to add to the indicated cultural asset
     * @return boolean The result of the operation; true if successful, false otherwise
     * @throws RemoteException Exception flow
     */
    public boolean addTagToCulturalAsset(int culturalAssetID, int tagID) throws RemoteException;

    /**
     * Method for removing a tag from a certain cultural asset.
     * To ensure that the operation is successful, it is necessary that the cultural asset has
     * actually set the specified tag.
     *
     * @param culturalAssetID The identifier of the cultural asset from which to remove the tag
     * @param tagID The ID tag to be removed from the indicated cultural asset
     * @return boolean The result of the operation; true if successful, false otherwise
     * @throws RemoteException Exception flow
     */
    public boolean removeTagFromCulturalAsset(int culturalAssetID, int tagID) throws RemoteException;
}
