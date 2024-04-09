package unisa.gps.etour.control.CulturalHeritageManagement.test.stub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.repository.IDBTourist;

public class DBTourist implements IDBTourist {

    public boolean deletePreferredCulturalAsset(int touristId, int culturalAssetId) throws SQLException {
        return false;
    }

    public boolean deletePreferredRestaurantPoint(int touristId, int restaurantPointId) throws SQLException {
        return false;
    }

    public boolean deleteTourist(int touristId) throws SQLException {
        return false;
    }

    public boolean insertPreferredCulturalAsset(int touristId, int culturalAssetId) throws SQLException {
        return false;
    }

    public boolean insertPreferredRestaurantPoint(int touristId, int restaurantPointId) throws SQLException {
        return false;
    }

    public boolean insertTourist(BeanTourist tourist) throws SQLException {
        return false;
    }

    public boolean updateTourist(BeanTourist tourist) throws SQLException {
        return false;
    }

    public BeanTourist getTourist(String username) throws SQLException {
        return null;
    }

    public BeanTourist getTourist(int touristId) throws SQLException {
        return new BeanTourist(1, "username", "Astrubale", "Silberschatz", "Naples", "Naples", "0111111", "80100th", "Way of the systems, 1", "NA",
                "trapano@solitario.it", "passwordsolomia", new Date(), new Date(), true);
    }

    public ArrayList<BeanTourist> getTourists(String touristUsername) throws SQLException {
        return null;
    }

    public ArrayList<BeanTourist> getTourists(boolean condition) throws SQLException {
        return null;
    }
}
