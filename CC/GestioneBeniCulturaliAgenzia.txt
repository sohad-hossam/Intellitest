package unisa.gps.etour.control.ManagementCulturalHeritage;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import unisa.gps.etour.bean.CulturalHeritageBean;
import unisa.gps.etour.bean.TagBean;
import unisa.gps.etour.bean.CulturalHeritageVisitBean;
import unisa.gps.etour.util.ErrorMessages;

/**
 * Class managing cultural heritage specific to the agency.
 *
 * @Author Michelangelo De Simone
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
public class AgencyCulturalHeritageManagement extends CommonCulturalHeritageManagement implements IAgencyCulturalHeritageManagement {

    /**
     * Constructor of the class, invokes and initializes the common management class.
     */
    public AgencyCulturalHeritageManagement() throws RemoteException {
        super();
    }

    /**
     * Implements the method for deleting a cultural asset.
     *
     * @see unisa.gps.etour.control.ManagementCulturalHeritage.IAgencyCulturalHeritageManagement#deleteCulturalHeritage(int)
     */
    public boolean deleteCulturalHeritage(int culturalHeritageID) throws RemoteException {
        if (!CulturalHeritageControl.checkCulturalHeritageID(culturalHeritageID))
            throw new RemoteException(ErrorMessages.DATA_ERROR);

        try {
            return dbbc.deleteCulturalHeritage(culturalHeritageID);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    /**
     * Implements the method for inserting a new cultural asset.
     *
     * @see unisa.gps.etour.control.ManagementCulturalHeritage.IAgencyCulturalHeritageManagement#insertCulturalHeritage(unisa.gps.etour.bean.CulturalHeritageBean)
     */
    public boolean insertCulturalHeritage(CulturalHeritageBean culturalHeritage) throws RemoteException {
        if (!CulturalHeritageControl.checkCulturalHeritageData(culturalHeritage))
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);

        try {
            return dbbc.insertCulturalHeritage(culturalHeritage);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    /**
     * Implements the method for obtaining all the cultural assets currently in the system.
     *
     * @see unisa.gps.etour.control.ManagementCulturalHeritage.IAgencyCulturalHeritageManagement#getCulturalHeritages()
     */
    public ArrayList<CulturalHeritageBean> getCulturalHeritages() throws RemoteException {
        try {
            return dbbc.getCulturalHeritageList();
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    /**
     * Implements the method for modifying a cultural asset in the system.
     *
     * @see unisa.gps.etour.control.ManagementCulturalHeritage.IAgencyCulturalHeritageManagement#modifyCulturalHeritage(unisa.gps.etour.bean.CulturalHeritageBean)
     */
    public boolean modifyCulturalHeritage(CulturalHeritageBean culturalHeritage) throws RemoteException {
        if (!CulturalHeritageControl.checkCulturalHeritageData(culturalHeritage))
            throw new RemoteException(ErrorMessages.BEAN_FORMAT_ERROR);

        try {
            return dbbc.modifyCulturalHeritage(culturalHeritage);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
}


/**
 * Implements the method for adding a tag to a cultural object.
 *
 * @See unisa.gps.etour.control.ManagementCulturalHeritage.IAgencyCulturalHeritageManagement#addCulturalHeritageTag(int, int)
 */
public boolean addCulturalHeritageTag(int culturalHeritageID, int tagID) throws RemoteException {
    if (!CulturalHeritageControl.checkCulturalHeritageID(culturalHeritageID) || !(tagID > 0))
        throw new RemoteException(ErrorMessages.DATA_ERROR);

    // This section of code actually verifies if the specified cultural heritage
    // already has the defined tag.
    
    // Get all tags for the specified cultural heritage
    ArrayList<TagBean> tempTags = null;
    boolean containsTag = false;
    
    try {
        tempTags = dbtag.getCulturalHeritageTags(culturalHeritageID);
    } catch (SQLException e) {
        throw new RemoteException(ErrorMessages.DBMS_ERROR);
    }

    // Here we iterate to find the specified tag, if found, a flag is set
    // to avoid adding a tag twice to the same cultural object.
    for (TagBean tag : tempTags) {
        if (tag.getId() == tagID)
            containsTag = true;
    }

    if (!containsTag) {
        try {
            return dbtag.addCulturalHeritageTag(culturalHeritageID, tagID);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DBMS_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }

    return false;
}

/**
 * Implement the method for removing a tag from a cultural object.
 *
 * @See unisa.gps.etour.control.ManagementCulturalHeritage.IAgencyCulturalHeritageManagement#removeCulturalHeritageTag(int, int)
 */
public boolean removeCulturalHeritageTag(int culturalHeritageID, int tagID) throws RemoteException {
    if (!CulturalHeritageControl.checkCulturalHeritageID(culturalHeritageID) || !(tagID > 0))
        throw new RemoteException(ErrorMessages.DATA_ERROR);

    // This section of code actually verifies if the specified cultural heritage
    // has the specified tag.

    // Get all tags for the specified cultural heritage
    ArrayList<TagBean> tempTags = null;

    try {
        tempTags = dbtag.getCulturalHeritageTags(culturalHeritageID);
    } catch (SQLException e) {
        throw new RemoteException(ErrorMessages.DBMS_ERROR);
    }

    // Here we iterate to find the specified tag, if found, the transaction is made
    // for removal of the tag and control is returned.
    for (TagBean tag : tempTags) {
        if (tag.getId() == tagID) {
            try {
                return dbtag.removeCulturalHeritageTag(culturalHeritageID, tagID);
            } catch (SQLException e) {
                throw new RemoteException(ErrorMessages.DBMS_ERROR);
            } catch (Exception e) {
                throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
            }
        }
    }

    return false;
}
