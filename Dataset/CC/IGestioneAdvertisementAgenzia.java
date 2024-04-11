/**
 * Interface that provides management services of advertisements
 * for the operator agency.
 *
 * @Author author
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
package com.trapan.spg.control.GestioneAdvertisement;

import java.rmi.RemoteException;
import java.util.ArrayList;

import com.trapan.spg.bean.BeanNews;

public interface IAdvertisementAgencyManagement extends IAdvertisementManagement {

    /**
     * Method that inserts a new news into the system.
     *
     * @param newsBean Bean containing news data
     * @throws RemoteException
     */
    public boolean insertNews(BeanNews newsBean) throws RemoteException;

    /**
     * Method which removes a news from the system.
     *
     * @param newsID ID of the news to be deleted
     * @throws RemoteException
     */
    public boolean deleteNews(int newsID) throws RemoteException;

    /**
     * Method for modifying the text of a news.
     *
     * @param newsBean Bean containing news data
     * @throws RemoteException
     */
    public boolean modifyNews(BeanNews newsBean) throws RemoteException;

    /**
     * Returns the list of active news.
     *
     * @return ArrayList of active news
     * @throws RemoteException
     */
    public ArrayList<BeanNews> getAllNews() throws RemoteException;
}
