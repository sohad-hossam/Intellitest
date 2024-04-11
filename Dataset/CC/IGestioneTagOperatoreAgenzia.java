package unisa.gps.etour.control.GestioneTag;

import java.rmi.RemoteException;

import unisa.gps.etour.bean.BeanTag;

/**
 * Interface for tag management by the Agency Operator.
 *
 * @Author Joseph Morelli
 * @Version 0.1
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */
public interface OperatorAgencyTagManagement extends CommonTagManagement {

    /**
     * Method to insert a new tag.
     *
     * @param newTag containing all the data of the new Tag
     * @return Boolean: true if the operation is successful, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean insertTag(BeanTag newTag) throws RemoteException;

    /**
     * Method for deleting an existing tag.
     *
     * @param tagID to identify the tag to be deleted
     * @return Boolean: true if the operation is successful, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean deleteTag(int tagID) throws RemoteException;

    /**
     * Method for editing a tag.
     *
     * @param modifiedTag containing the details of the modified Tag
     * @return Boolean: true if the operation is successful, false otherwise
     * @throws RemoteException Remote Exception
     */
    public boolean modifyTag(BeanTag modifiedTag) throws RemoteException;

    /**
     * Method which returns a tag identified by its ID.
     *
     * @param tagID to identify a particular tag
     * @return a BeanTag containing data of the specified Tag
     * @throws RemoteException Remote Exception
     */
    public BeanTag getTag(int tagID) throws RemoteException;

}