package unisa.gps.etour.control.GestionePuntiDiRistoro;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import unisa.gps.etour.bean.BeanRefreshmentpoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanVisitPR;

import unisa.gps.etour.repository.DBRefreshmentpoint;
import unisa.gps.etour.repository.DBVisitPR;
import unisa.gps.etour.repository.DBTag;
import unisa.gps.etour.repository.IDBRefreshmentpoint;
import unisa.gps.etour.repository.IDBTag;
import unisa.gps.etour.repository.IDBTourist;
import unisa.gps.etour.repository.IDBVisitPR;
import unisa.gps.etour.util.GlobalConstants;
import unisa.gps.etour.util.MessageError;

public class ManagementofCommonRefreshmentPoints extends UnicastRemoteObject implements IManagementofCommonRefreshmentPoints {

    private static final long serialVersionUID = 1L;
    protected IDBRefreshmentpoint puntoRistoro;
    protected IDBTag tags;
    protected IDBVisitPR feeds;
    protected IDBTourist dbTourist;

    public ManagementofCommonRefreshmentPoints() throws RemoteException {
        super();
        // Connect to the Database
        try {
            puntoRistoro = new DBRefreshmentpoint();
            tags = new DBTag();
            feeds = new DBVisitPR();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }
    }

    public BeanRefreshmentpoint getRefreshmentpoint(int pRefreshmentpointID) throws RemoteException {
        // Check identifier passed
        if (pRefreshmentpointID < 0)
            throw new RemoteException(MessageError.Error_DATI);
        // Return Instance
        BeanRefreshmentpoint toReturn = null;
        try {
            // Retrieve data through the instance of the database connection
            toReturn = puntoRistoro.getRefreshmentpoint(pRefreshmentpointID);
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString());
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception ee) {
            System.out.println("Error: " + ee.toString());
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }
        // Check the variable return, so they do not pass bad data
        // To the caller
        // And trigger an exception if the format of the bean is invalid
        if (null == toReturn)
            throw new RemoteException(MessageError.Error_UNKNOWNError_BEAN_FORMAT);
        // Return the bean that contains information about the Refreshment
        // Required
        return toReturn;
    }

    public ArrayList<BeanTag> getDiningPointTags(int pRefreshmentpointID) throws RemoteException {
        if (pRefreshmentpointID < 0)
            throw new RemoteException(MessageError.Error_DATI);
        ArrayList<BeanTag> toReturn = null;
        try {
            toReturn = tags.getDiningPointTags(pRefreshmentpointID);
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString());
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception ee) {
            System.out.println("Error: " + ee.toString());
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }
        if (null == toReturn)
            throw new RemoteException(MessageError.Error_UNKNOWNError_BEAN_FORMAT);
        return toReturn;
    }

    public String[] getLastComments(int pRefreshmentpointID) throws RemoteException {
        if (pRefreshmentpointID < 0)
            throw new RemoteException(MessageError.Error_DATI);
        String[] toReturn = new String[10];
        ArrayList<BeanVisitPR> temp = null;
        try {
            temp = feeds.getLastVisitList(pRefreshmentpointID);
        } catch (SQLException e) {
            System.out.println("Error in method getLastComments: " + e.toString());
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception ee) {
            System.out.println("Error in method getLastComments: " + ee.toString());
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }
        for (int i = 0; i < 10; i++) {
            toReturn[i] = temp.get(temp.size() - (i + 1)).getComment();
        }
        return toReturn;
    }

    public ArrayList<Integer> getDiningPointStatistics(int pRefreshmentpointID) throws RemoteException {
        if (pRefreshmentpointID < 0)
            throw new RemoteException(MessageError.Error_DATI);
        ArrayList<BeanVisitPR> visitBeans = null;
        ArrayList<Integer> resultList = new ArrayList<Integer>(5);
        for (int i = 0; i < 5; i++) {
            resultList.add(Integer.valueOf(0));
        }
        Date lastThirtyDays = new Date(new Date().getTime() - GlobalConstants.TRENTA_GIORNI);

        System.out.println("The date of 30 days ago:" + lastThirtyDays);

        try {
            visitBeans = feeds.getVisitList(pRefreshmentpointID);
            for (Iterator<BeanVisitPR> visitIterator = visitBeans.iterator(); visitIterator.hasNext();) {
                BeanVisitPR visitBeanTemp = visitIterator.next();
                System.out.println("The date of this visit is: " + visitBeanTemp.getVisitDate());
                if (visitBeanTemp.getVisitDate().after(lastThirtyDays)) {
                    resultList.set(visitBeanTemp.getVote() - 1, Integer.valueOf(
                            resultList.get(visitBeanTemp.getVote() - 1).intValue() + 1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in method getDiningPointStatistics: " + e.toString());
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception ee) {
            System.out.println("Error in method getDiningPointStatistics: " + ee.toString());
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }

        if (null == resultList)
            throw new RemoteException(MessageError.Error_UNKNOWNError_BEAN_FORMAT);
        return resultList;
    }

    // Method that allows you to change the comment issued for a
    // Refreshment
    public boolean modifyFeedbackForDiningPoint(int pRefreshmentpointId, BeanVisitPR newVisit) throws RemoteException {
        // Check the validity of the provided data
        if (pRefreshmentpointId < 0 || !(newVisit instanceof BeanVisitPR))
            throw new RemoteException(MessageError.Error_DATI);

        // Retrieve the existing bean from the database to verify if the rating has changed
        BeanVisitPR temp = null;
        try {
            temp = feeds.getVisit(pRefreshmentpointId, newVisit.getIdTourist());
        } catch (SQLException e) {
            System.out.println("Error in method modifyFeedbackForDiningPoint: " + e.toString());
            throw new RemoteException(MessageError.Error_DBMS);
        } catch (Exception ee) {
            System.out.println("Error in method modifyFeedbackForDiningPoint: " + ee.toString());
            throw new RemoteException(MessageError.Error_UNKNOWN);
        }

        // If the rating has changed, throw an exception
        if (temp.getRating() != newVisit.getRating())
            throw new RemoteException(MessageError.Error_UNKNOWNError_BEAN_FORMAT);

        // If the rating has not changed, update the comment in the database
        else {
            try {
                feeds.modifyVisit(newVisit);
                return true;
            } catch (SQLException e) {
                System.out.println("Error in method modifyFeedbackForDiningPoint: " + e.toString());
                throw new RemoteException(MessageError.Error_DBMS);
            } catch (Exception ee) {
                System.out.println("Error in method modifyFeedbackForDiningPoint: " + ee.toString());
                throw new RemoteException(MessageError.Error_UNKNOWN);
            }
        }
    }
}