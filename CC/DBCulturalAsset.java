package unisa.gps.etour.control.ManagingCulturalHeritageResources.test.stub;

import java.sql.SQLException;
import java.util.ArrayList;
import unisa.gps.etour.bean.BeanCulturalAsset;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.repository.IDBCulturalAsset;
import unisa.gps.etour.util.Point3D;

public class DBCulturalAsset implements IDBCulturalAsset {
    private ArrayList<BeanCulturalAsset> b;

    public DBCulturalAsset() {
        b = new ArrayList<BeanCulturalAsset>(0);
    }

    public boolean deleteCulturalAsset(int pIdAsset) throws SQLException {
        boolean res = false;

        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).getId() == pIdAsset) {
                b.remove(i);
                res = true;
            }
        }

        return res;
    }

    public boolean insertCulturalAsset(BeanCulturalAsset pAsset) throws SQLException {
        return b.add(pAsset);
    }

    public boolean modifyCulturalAsset(BeanCulturalAsset pAsset) throws SQLException {
        boolean res = false;

        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).getId() == pAsset.getId()) {
                b.set(i, pAsset);
                return true;
            }
        }

        return res;
    }

    public BeanCulturalAsset getCulturalAsset(int pid) throws SQLException {
        BeanCulturalAsset res = null;

        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).getId() == pid) {
                res = b.get(i);
            }
        }

        return res;
    }

    public ArrayList<BeanCulturalAsset> getList() throws SQLException {
        return b;
    }

    public int getNumberOfSearchElements(String pKeyword, ArrayList<BeanTag> pTags, Point3D pPosition, double pMaxDistance) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getNumberOfAdvancedSearchElements(int pTouristId, String pKeyword, ArrayList<BeanTag> pTags, Point3D pPosition, double pMaxDistance) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public ArrayList<BeanCulturalAsset> search(String pKeyword, ArrayList<BeanTag> pTags, int pPageNumber, int pElementsPerPage, Point3D pPosition, double pMaxDistance) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<BeanCulturalAsset> advancedSearch(int pTouristId, String pKeyword, ArrayList<BeanTag> pTags, int pPageNumber, int pElementsPerPage, Point3D pPosition, double pMaxDistance) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}
