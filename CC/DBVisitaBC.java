package unisa.gps.etour.control.CulturalHeritageManagement.test.stub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanCulturalVisitFeedback;
import unisa.gps.etour.repository.IDBCulturalHeritageVisit;

public class DBCulturalHeritageVisit implements IDBCulturalHeritageVisit {

    public boolean insertVisitBC(BeanCulturalVisitFeedback visit) throws SQLException {
        return false;
    }

    public boolean modifyVisitBC(BeanCulturalVisitFeedback visit) throws SQLException {
        return false;
    }

    public ArrayList<BeanCulturalVisitFeedback> getListOfVisitBC(int culturalAssetId) throws SQLException {
        ArrayList<BeanCulturalVisitFeedback> fakeVisits = new ArrayList<BeanCulturalVisitFeedback>();

        fakeVisits.add(new BeanCulturalVisitFeedback(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(3, 1, "show particular but interesting", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(4, 1, "beautiful exhibition", 1, new Date()));
        fakeVisits.add(new BeanCulturalVisitFeedback(3, 1, "show particular but interesting", 1, new Date()));
        
        // You can add more fake visits here
        
        return fakeVisits;
    }

    public ArrayList<BeanCulturalVisitFeedback> getListOfVisitBCTourist(int touristId) throws SQLException {
        return null;
    }

    public BeanCulturalVisitFeedback getVisitBC(int culturalAssetId, int touristId) throws SQLException {
        return null;
    }
}
