package unisa.gps.etour.control.CulturalHeritageManagement.test.stub;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.repository.IDBTag;

public class DBTag implements IDBTag {

    private ArrayList<BeanTag> b;

    public DBTag() {
        b = new ArrayList<BeanTag>(0);
    }

    public boolean addCulturalAssetTag(int culturalAssetId, int tagId) throws SQLException {
        return true;
    }

    public boolean addRestaurantPointTag(int restaurantPointId, int tagId) throws SQLException {
        return true;
    }

    public boolean deleteTag(int tagId) throws SQLException {
        return true;
    }

    public boolean deleteCulturalAssetTag(int culturalAssetId, int tagId) throws SQLException {
        return true;
    }

    public boolean deleteRestaurantPointTag(int restaurantPointId, int tagId) throws SQLException {
        return true;
    }

    public boolean insertTag(BeanTag tag) throws SQLException {
        return true;
    }

    public boolean updateTag(BeanTag tag) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    public ArrayList<BeanTag> getTagList() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public BeanTag getTag(int tagId) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<BeanTag> getCulturalAssetTags(int culturalAssetId) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<BeanTag> getRestaurantPointTags(int restaurantPointId) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}