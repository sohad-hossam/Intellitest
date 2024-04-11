/**
 * Implements the management of advertisements for the operator agency. Contains methods for managing news.
 *
 * @Author Fabio Palladino
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University of Salerno
 */
package unisa.gps.etour.control.ManagementAdvertisement;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanNews;

/* Import the stub */
// import unisa.gps.etour.control.ManagementAdvertisement.test.stubs.DBNews;
import unisa.gps.etour.repository.DBNews;
import unisa.gps.etour.repository.IDBNews;
import unisa.gps.etour.util.DataCheck;
import unisa.gps.etour.util.GlobalConstants;
import unisa.gps.etour.util.MessageError;

public class ManagementAdvertisementAgency extends ManagementAdvertisement implements IManagementAdvertisementAgency {

    private static final long serialVersionUID = 1L;
    /** Contains the methods for collecting news in the database */
    private IDBNews dbNews;

    /**
     * Constructor. Instantiates an object of type (@link unisa.gps.etour.repository.DBNews).
     *
     * @throws RemoteException
     */
    public ManagementAdvertisementAgency() throws RemoteException {
        super();
        dbNews = new DBNews();
    }

    /**
     * Method which removes news from the database. Uses the
     * (@Link Boolean unisa.gps.etour.repository.IDBNews#cancellaNews(int))
     *
     * @param pNewsID news to be erased.
     * @return true if the deletion was successful or false otherwise.
     * @throws RemoteException
     */
    public boolean deleteNews(int pNewsID) throws RemoteException {

        try {
            /* Check that the ID is valid */
            if (pNewsID > 0) {
                return dbNews.deleteNews(pNewsID);
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_CONNECTION_DBMS);
        }
    }

    /**
     * Insert a new news in the database.
     * Use the method (@link unisa.gps.etour.repository.IDBNews#insertNews(BeanNews))
     * To insert the news and the method (@link unisa.gps.etour.repository.IDBNews#getNews())
     * To count the number of news in the system.
     *
     * @param Pnews bean containing data news.
     * @return true if the insertion is successful, false if it was
     * Reached the maximum number of news stored or if the insertion fails.
     * @throws RemoteException
     */
    public boolean insertNews(BeanNews Pnews) throws RemoteException {

        try {
            /* Check the data of the news */
            if (!DataCheck.checkBeanNews(Pnews)) {
                throw new RemoteException(MessageError.Error_DATA);
            }
            /* Check that the maximum number of news has not been exceeded */
            int numNews = dbNews.getNews().size();
            if (numNews < GlobalConstants.MAX_NEWS_ATTIVE) {
                /* Possible inclusion */
                return dbNews.insertNews(Pnews);
            } else {
                /* Insertion is not possible */
                return false;
            }
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_CONNECTION_DBMS);
        }
    }

    /**
     * Method to change data of a news.
     * Use the method (@link unisa.gps.etour.repository.IDBNews#modificationNews(BeanNews)).
     *
     * @param Pnews bean containing the data of news changed.
     * @return true if the change goes through.
     * @throws RemoteException
     */
    public boolean updateNews(BeanNews Pnews) throws RemoteException {

        try {
            /* Check the data of the news */
            if (!DataCheck.checkBeanNews(Pnews)) {
                throw new RemoteException(MessageError.Error_DATA);
            }
            return dbNews.updateNews(Pnews);
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_CONNECTION_DBMS);
        }
    }

    /**
     * Method that returns an array containing all the news stored in the system.
     * Use the method (@link unisa.gps.etour.repository.IDBNews#getNews()).
     *
     * @return ArrayList containing beans of news.
     * @throws RemoteException
     */
    public ArrayList<BeanNews> getAllNews() throws RemoteException {

        try {
            return dbNews.getNews();
        } catch (SQLException e) {
            throw new RemoteException(MessageError.Error_CONNECTION_DBMS);
        }
    }
}