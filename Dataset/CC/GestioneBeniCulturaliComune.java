package unisa.gps.etour.control.CulturalHeritageManagement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import unisa.gps.etour.bean.BeanGoodCultural;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.repository.DBTag;
import unisa.gps.etour.repository.IDBGoodCultural;
import unisa.gps.etour.repository.IDBTag;
import unisa.gps.etour.repository.IDBTourist;
import unisa.gps.etour.repository.IDBVisitBC;
import unisa.gps.etour.util.GlobalConstants;
import unisa.gps.etour.util.MessageError;

public class CulturalHeritageManagementCommune extends UnicastRemoteObject implements ICulturalHeritageManagementCommune {
    // Connect to DB for Cultural Heritage
    protected IDBGoodCultural dbGoodCultural;

    // Connect to DB Tag
    protected IDBTag dbTag;

    // Connect to DB for the Feedback / Visits
    protected IDBVisitBC dbVisit;

    // Connect to DB for Tourists
    protected IDBTourist dbTourist;

    public CulturalHeritageManagementCommune() throws RemoteException {
        super();

        try {
            dbGoodCultural = new DBGoodCultural();
            dbTag = new DBTag();
            dbVisit = new DBVisitBC();
            dbTourist = new DBTourist();
        } catch (Exception e) {
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }
    }

    public BeanGoodCultural getGoodCultural(int goodCulturalID) throws RemoteException {
        if (!ControlloBeniCulturali.checkIdGoodCultural(goodCulturalID)) {
            throw new RemoteException(MessageError.Error_DATA);
        }

        BeanGoodCultural beanGoodCultural = null;

        try {
            beanGoodCultural = dbGoodCultural.getGoodCultural(goodCulturalID);
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception e) {
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }

        return beanGoodCultural;
    }

    public ArrayList<BeanTag> getTagGoodCultural(int goodCulturalID) throws RemoteException {
        if (!ControlloBeniCulturali.checkIdGoodCultural(goodCulturalID)) {
            throw new RemoteException(MessageError.Error_DATA);
        }

        ArrayList<BeanTag> beanTags = null;

        try {
            beanTags = dbTag.getTagGoodCultural(goodCulturalID);
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception e) {
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }

        return beanTags;
    }

    public HashMap<BeanVisitBC, String> getFeedbackGoodCultural(int goodCulturalID) throws RemoteException {
        if (!ControlloBeniCulturali.checkIdGoodCultural(goodCulturalID)) {
            throw new RemoteException(MessageError.Error_DATA);
        }

        HashMap<BeanVisitBC, String> feedbackMap;

        try {
            feedbackMap = new HashMap<BeanVisitBC, String>(dbVisit.getListVisitBC(goodCulturalID).size());

            for (BeanVisitBC visit : dbVisit.getListVisitBC(goodCulturalID)) {
                feedbackMap.put(visit, dbTourist.getTourist(visit.getIdTourist()).getUserName());
            }
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception e) {
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }

        return feedbackMap;
    }

    public ArrayList<Integer> getStatisticsGoodCultural(int goodCulturalID) throws RemoteException {
        if (!CulturalAssetsControl.checkIdGoodCultural(goodCulturalID)) {
            throw new RemoteException(MessageError.Error_DATA);
        }

        ArrayList<Integer> resultList = new ArrayList<Integer>(5);

        for (int i = 0; i < 5; i++) {
            resultList.add(i, Integer.valueOf(0));
        }

        Date lastThirtyDays = new Date(new Date().getTime() - GlobalConstants.THIRTY_DAYS);

        try {
            for (BeanVisitBC visit : dbVisit.getListVisitBC(goodCulturalID)) {
                if (visit.getDataVisit().after(lastThirtyDays)) {
                    resultList.set(visit.getVote() - 1, Integer.valueOf(resultList.get(visit.getVote() - 1).intValue() + 1));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception e) {
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }

        return resultList;
    }

    public boolean modificationFeedbackGoodCultural(int goodCulturalID, BeanVisitBC beanVisitBC) throws RemoteException {
        if (!CulturalHeritageControl.checkIdGoodCultural(goodCulturalID) || !CulturalSitesVisitControl.controlDataVisitGoodCultural(beanVisitBC)) {
            throw new RemoteException(MessageError.Error_DATA);
        }

        boolean voteOk = true;

        try {
            voteOk = dbVisit.getVisitBC(goodCulturalID, beanVisitBC.getIdTourist()).getVote() == beanVisitBC.getVote();
            if (voteOk) {
                return dbVisit.modificationVisitBC(beanVisitBC);
            }
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception e) {
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }
        if (voteOk)
        try
        {
        return (dbVisit.modificationVisitBC (pBeanVisitBC));
        }
        catch (SQLException e)
        {
        throw new RemoteException (MessageError.Error_DBMS);
        }
        catch (Exception e)
        {
         throw new RemoteException (MessageError.Error_UNKNOWN);
        }
        return false;
    }
}
