package unisa.gps.etour.control.GestioneUtentiRegistrati;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanSearchPreference;
import unisa.gps.etour.bean.BeanGenericPreference;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanCulturalVisit;
import unisa.gps.etour.bean.BeanRefreshmentVisit;
import unisa.gps.etour.repository.DBCulturalHeritage;
import unisa.gps.etour.repository.DBSearchPreferences;
import unisa.gps.etour.repository.DBGenericPreferences;
import unisa.gps.etour.repository.DBRefreshmentPoints;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.repository.DBCulturalVisit;
import unisa.gps.etour.repository.DBRefreshmentVisit;
import unisa.gps.etour.repository.IDBCulturalHeritage;
import unisa.gps.etour.repository.IDBSearchPreferences;
import unisa.gps.etour.repository.IDBGenericPreferences;
import unisa.gps.etour.repository.IDBRefreshmentPoints;
import unisa.gps.etour.repository.IDBTourist;
import unisa.gps.etour.repository.IDBCulturalVisit;
import unisa.gps.etour.repository.IDBRefreshmentVisit;
import unisa.gps.etour.util.DataValidation;
import unisa.gps.etour.util.ErrorMessages;

/**
 * Class on the Management of Tourist Information
 *
 * @Author Federico Leon
 * @Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab University of DMI
 * Salerno
 */
public class TouristManagementClient extends CommonTouristManagement implements IGestioneTuristaClient {
    private static final long serialVersionUID = -6161592850721537385L;
    private IDBTourist touristProfile; // Data Management for tourists
    private IDBGenericPreferences touristGenPreferences; // General preferences manager of the tourist
    private IDBSearchPreferences touristSearchPreferences; // Handle search preferences of tourists
    private IDBCulturalVisit visitedCulturalSites; // Managing visited cultural sites
    private IDBRefreshmentVisit visitedRefreshmentPoints; // Manager of visited refreshment points
    private IDBCulturalHeritage culturalHeritage; // Managing cultural heritage (we only need to obtain a Cultural Heritage bean by its ID
    private IDBRefreshmentPoints refreshmentPoint; // Management of refreshment areas (use the same object "culturalHeritage"

    public TouristManagementClient() throws RemoteException {
        super();

        touristProfile = new DBTourist();
        touristGenPreferences = new DBGenericPreferences();
        touristSearchPreferences = new DBSearchPreferences();
        visitedCulturalSites = new DBCulturalVisit();
        visitedRefreshmentPoints = new DBRefreshmentVisit();
        culturalHeritage = new DBCulturalHeritage();
        refreshmentPoint = new DBRefreshmentPoints();
    }

    /**
     * (Non-Javadoc)
     *
     * @see unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient#cancellaPreferenzeDiRicerca(int,
     * int)
     */
    public BeanSearchPreference deleteSearchPreferences(int touristId, int preferenceId) throws RemoteException {
        try {
            boolean delete = true;
            BeanSearchPreference searchPref = new BeanSearchPreference();
            ArrayList<BeanSearchPreference> searchPrefList = new ArrayList<>();

            searchPrefList = touristSearchPreferences.getTouristSearchPreferences(touristId);
            Iterator<BeanSearchPreference> iterator = searchPrefList.iterator();

            while (iterator.hasNext() && delete == true) {
                searchPref = iterator.next();
                if (searchPref.getId() == preferenceId)
                    delete = false; // Find the preference with the id
                // We leave the interested
                // Cycle
            }

            touristSearchPreferences.deleteTouristSearchPreference(touristId, preferenceId);
            return searchPref;
        } catch (SQLException e) {
            // If the data layer throws an exception SQLException
            // It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessages.DB_ERROR);
        }
    }

    /**
     * (Non-Javadoc)
     *
     * @see unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient#cancellaPreferenzeGeneriche(int)
     */
    public BeanGenericPreference deleteGenericPreferences(int touristId) throws RemoteException {
        try {
            BeanGenericPreference temp = touristGenPreferences.getGenericPreference(touristId);
            boolean canceled = touristGenPreferences.deleteGenericPreference(temp.getId());

            if (canceled)
                return temp;
            else
                return null; // The cancellation occurred
        } catch (SQLException e) {
            // If the data layer throws an exception SQLException
            // It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessages.DB_ERROR);
        }
    }

    /**
     * (Non-Javadoc)
     *
     * @see unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient#inserisciPreferenzaDiRicerca(int,
     * unisa.gps.etour.bean.BeanPreferenzaDiRicerca)
     */
    public boolean insertSearchPreference(int touristId, BeanSearchPreference searchPreference) throws RemoteException {
        try {
            boolean checkDate = DataValidation.checkSearchPreferenceBean(searchPreference);

            if (checkDate) {
                // If the data control is positive
                return touristSearchPreferences.insertTouristSearchPreference(touristId, searchPreference.getId());
            } else {
                return false; // Data error
            }
        } catch (SQLException e) {
            // If the data layer throws an exception SQLException
            // It throws RemoteException the remote exception
            throw new RemoteException(ErrorMessages.DB_ERROR);
        }
    }
}


catch (SQLException e) {
    // If the data layer throws an exception SQLException
    // It throws RemoteException the remote exception
    throw new RemoteException(ErrorMessages.DB_ERROR);
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertGenericPreferences (unisa.gps.etour.bean.BeanGenericPreferences)
 */
public boolean insertGenericPreferences(BeanGenericPreferences pGenericPreferences) throws RemoteException {
    try {
        boolean checkDate = DataValidation.checkGenericPreferencesBean(pGenericPreferences);

        if (checkDate) { // If the data control is positive
            return touristGenPreferences.insertGenericPreference(pGenericPreferences);
        } else {
            return false; // Data error
        }
    } catch (SQLException e) {
        // If the data layer throws an exception SQLException
        // It throws RemoteException the remote exception
        throw new RemoteException(ErrorMessages.DB_ERROR);
    }
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertTourist (unisa.gps.etour.bean.BeanTourist)
 */
public boolean insertTourist(BeanTourist pTourist) throws RemoteException {
    try {

        // Check if the username entered is already present in DB
        BeanTourist temp = touristProfile.getTourist(pTourist.getUsername());

        // If there is no tourist with the same username
        if (temp == null) {
            boolean checkDate = DataValidation.checkTouristBean(pTourist);

            if (checkDate) { // If the data control is positive
                return touristProfile.insertTourist(pTourist);
            } else {
                return false; // Data error
            }
        } else {
            return false; // Username already exists in DB
        }

    } catch (SQLException e) {
        // If the data layer throws an exception SQLException
        // It throws RemoteException the remote exception
        throw new RemoteException(ErrorMessages.DB_ERROR);
    }
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # modifyGenericPreferences (unisa.gps.etour.bean.BeanGenericPreferences)
 */
public boolean modifyGenericPreferences(BeanGenericPreferences pNewGenericPreferences) throws RemoteException {
    try {
        boolean checkDate = DataValidation.checkGenericPreferencesBean(pNewGenericPreferences);

        if (checkDate) { // If the data control is positive
            return touristGenPreferences.modifyGenericPreference(pNewGenericPreferences);
        } else {
            return false; // Data error
        }
    } catch (SQLException e) {
        // If the data layer throws an exception SQLException
        // It throws RemoteException the remote exception
        throw new RemoteException(ErrorMessages.DB_ERROR);
    }
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # obtainSearchPreferences (int)
 */
public BeanSearchPreference[] obtainSearchPreferences(int touristId) throws RemoteException {
    // Array containing the list of search preferences for tourist with ID = touristId
    BeanSearchPreference[] preferences = null;

    try {
        // Convert ArrayList (return type of method "obtainSearchPreferences") to simple array
        ArrayList<BeanSearchPreference> searchPreferenceList = touristSearchPreferences.getTouristSearchPreferences(touristId);
        preferences = new BeanSearchPreference[searchPreferenceList.size()];
        preferences = searchPreferenceList.toArray(preferences);
        // End Conversion
    } catch (SQLException e) {
        // If the data layer throws an exception SQLException
        // It throws RemoteException the remote exception
        throw new RemoteException(ErrorMessages.DB_ERROR);
    }

    return preferences;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # obtainGenericPreferences (int)
 */
public BeanGenericPreferences obtainGenericPreferences(int touristId) throws RemoteException {
    try {
        return touristGenPreferences.getGenericPreference(touristId);
    } catch (SQLException e) {
        // If the data layer throws an exception SQLException
        // It throws RemoteException the remote exception
        throw new RemoteException(ErrorMessages.DB_ERROR);
    }
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.CommonTouristManagement # modifyTourist (unisa.gps.etour.bean.BeanTourist)
 */
public boolean modifyTourist(BeanTourist pTouristProfile) throws RemoteException {
    try {
        // Boolean variable that will hold true if the bean is correct, false otherwise
        boolean checkDate = DataValidation.checkTouristBean(pTouristProfile);

        if (checkDate) { // If the data control is positive
            return touristProfile.modifyTourist(pTouristProfile);
        } else {
            return false; // Data error
        }
    } catch (SQLException e) {
        // If the data layer throws an exception SQLException
        // It throws RemoteException the remote exception
        throw new RemoteException(ErrorMessages.DB_ERROR);
    }
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # obtainVisitedCulturalHeritages (int)
 */
public BeanCulturalVisit[] obtainVisitedCulturalHeritages(int touristId) throws RemoteException {
    BeanCulturalVisit[] visited = null;
    if (touristId > 0) {
        try {
            ArrayList<BeanCulturalVisit> visitedList = visitedCulturalSites.getTouristCulturalVisits(touristId);
            visited = new BeanCulturalVisit[visitedList.size()];
            visited = visitedList.toArray(visited);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return visited;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # obtainVisitedRefreshmentPoints (int)
 */
public BeanRefreshmentVisit[] obtainVisitedRefreshmentPoints(int touristId) throws RemoteException {
    // Array containing the list of refreshment points visited by tourist with id "touristId"
    BeanRefreshmentVisit[] visited = null;
    if (touristId > 0) {
        try {
            ArrayList<BeanRefreshmentVisit> visitedList = visitedRefreshmentPoints.getTouristRefreshmentVisits(touristId);
            visited = new BeanRefreshmentVisit[visitedList.size()];
            visited = visitedList.toArray(visited);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return visited;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertVisitedCulturalHeritage (unisa.gps.etour.bean.BeanCulturalVisit)
 */

public boolean insertVisitedCulturalHeritage(BeanCulturalVisit pCulturalVisit) throws RemoteException {
    if (DataValidation.checkBeanCulturalVisit(pCulturalVisit)) {
        try {
            return visitedCulturalSites.insertCulturalVisit(pCulturalVisit);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertVisitedRefreshmentPoint (unisa.gps.etour.bean.BeanRefreshmentVisit)
 */
public boolean insertVisitedRefreshmentPoint(BeanRefreshmentVisit pRefreshmentVisit) throws RemoteException {
    if (DataValidation.checkBeanRefreshmentVisit(pRefreshmentVisit)) {
        try {
            return visitedRefreshmentPoints.insertRefreshmentVisit(pRefreshmentVisit);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertPreferredCulturalHeritage (int, int)
 */
public boolean insertPreferredCulturalHeritage(int touristId, int culturalHeritageId) throws RemoteException {
    if (touristId > 0 && culturalHeritageId > 0) {
        try {
            return touristProfile.insertPreferredCulturalHeritage(touristId, culturalHeritageId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertPreferredRefreshmentPoint (int, int)
 */
public boolean insertPreferredRefreshmentPoint(int touristId, int refreshmentPointId) throws RemoteException {
    if (touristId > 0 && refreshmentPointId > 0) {
        try {
            return touristProfile.insertPreferredRefreshmentPoint(touristId, refreshmentPointId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # deletePreferredCulturalHeritage (int, int)
 */
public boolean deletePreferredCulturalHeritage(int touristId, int culturalHeritageId) throws RemoteException {
    if (touristId > 0 && culturalHeritageId > 0) {
        try {
            return touristProfile.deletePreferredCulturalHeritage(touristId, culturalHeritageId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # deletePreferredRefreshmentPoint (int, int)
 */
public boolean deletePreferredRefreshmentPoint(int touristId, int refreshmentPointId) throws RemoteException {
    if (touristId > 0 && refreshmentPointId > 0) {
        try {
            return touristProfile.deletePreferredRefreshmentPoint(touristId, refreshmentPointId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 *
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # obtainPreferredCulturalHeritages (int)
 */
public BeanCulturalHeritage[] obtainPreferredCulturalHeritages(int touristId) throws RemoteException {
    if (touristId > 0) {
        try {
            // List of tourist's cultural favorites with id = "touristId"
            ArrayList<Integer> preferredList = touristProfile.obtainPreferredCulturalHeritages(touristId);
            // Convert ArrayList to a simple array
            BeanCulturalHeritage[] favorites = new BeanCulturalHeritage[preferredList.size()];
            for (int k = 0; k < preferredList.size(); k++) {
                // Fill the array with the favorite bean of all cultural favorites from the tourist
                favorites[k] = culturalHeritage.obtainCulturalHeritage(preferredList.get(k));
            }
            return favorites;
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return null;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertCulturalHeritageVisited (BeanCulturalVisit)
 */
public boolean insertCulturalHeritageVisited(BeanCulturalVisit pCulturalVisit) throws RemoteException {
    if (DataValidation.checkCulturalVisitBean(pCulturalVisit)) {
        try {
            return visitedCulturalSites.insertCulturalVisit(pCulturalVisit);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertRefreshmentPointVisited (BeanRefreshmentVisit)
 */
public boolean insertRefreshmentPointVisited(BeanRefreshmentVisit pRefreshmentVisit) throws RemoteException {
    if (DataValidation.checkRefreshmentVisitBean(pRefreshmentVisit)) {
        try {
            return visitedRefreshmentPoints.insertRefreshmentVisit(pRefreshmentVisit);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertFavoriteCulturalHeritage (int,
 * int)
 */
public boolean insertFavoriteCulturalHeritage(int touristId, int culturalHeritageId) throws RemoteException {
    if (touristId > 0 && culturalHeritageId > 0) {
        try {
            return touristProfile.insertFavoriteCulturalHeritage(touristId, culturalHeritageId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # insertFavoriteRefreshmentPoint (int,
 * int)
 */
public boolean insertFavoriteRefreshmentPoint(int touristId, int refreshmentPointId) throws RemoteException {
    if (touristId > 0 && refreshmentPointId > 0) {
        try {
            return touristProfile.insertFavoriteRefreshmentPoint(touristId, refreshmentPointId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # deleteFavoriteCulturalHeritage (int,
 * int)
 */
public boolean deleteFavoriteCulturalHeritage(int touristId, int culturalHeritageId) throws RemoteException {
    if (touristId > 0 && culturalHeritageId > 0) {
        try {
            return touristProfile.deleteFavoriteCulturalHeritage(touristId, culturalHeritageId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # deleteFavoriteRefreshmentPoint (int,
 * int)
 */
public boolean deleteFavoriteRefreshmentPoint(int touristId, int refreshmentPointId) throws RemoteException {
    if (touristId > 0 && refreshmentPointId > 0) {
        try {
            return touristProfile.deleteFavoriteRefreshmentPoint(touristId, refreshmentPointId);
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return false;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # obtainFavoriteCulturalHeritages (int)
 */
public BeanCulturalHeritage[] obtainFavoriteCulturalHeritages(int touristId) throws RemoteException {
    if (touristId > 0) {
        try {
            // List of cultural favorites of tourist with id = "touristId"
            ArrayList<Integer> favoritesList = touristProfile.obtainFavoriteCulturalHeritages(touristId);

            // Convert ArrayList to simple array
            BeanCulturalHeritage[] favorites = new BeanCulturalHeritage[favoritesList.size()];
            favorites = favoritesList.toArray(favorites);

            int k = 0; // Cycle counter
            for (Integer i : favoritesList) {
                // Fill the array with the favorite bean of all cultural favorites from the tourist
                favorites[k++] = culturalHeritage.obtainCulturalHeritage(i);
            }
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return null;
}

/**
 * (Non-Javadoc)
 * @ See unisa.gps.etour.control.GestioneUtentiRegistrati.IGestioneTuristaClient # obtainFavoriteRefreshmentPoints (int)
 */
public BeanRefreshmentPoint[] obtainFavoriteRefreshmentPoints(int touristId) throws RemoteException {
    if (touristId > 0) {
        try {
            // List of refreshment points favorite tourist with id = "touristId"
            ArrayList<Integer> favoritesList = touristProfile.obtainFavoriteRefreshmentPoints(touristId);

            // Convert ArrayList to simple array
            BeanRefreshmentPoint[] favorites = new BeanRefreshmentPoint[favoritesList.size()];
            favorites = favoritesList.toArray(favorites);

            int k = 0; // Cycle counter
            for (Integer i : favoritesList) {
                // Fill the array with the favorite bean of all refreshment points from the tourist
                favorites[k++] = refreshmentPoint.obtainRefreshmentPoint(i);
            }
        } catch (SQLException e) {
            throw new RemoteException(ErrorMessages.DB_ERROR);
        } catch (Exception e) {
            throw new RemoteException(ErrorMessages.UNKNOWN_ERROR);
        }
    }
    return null;
}
