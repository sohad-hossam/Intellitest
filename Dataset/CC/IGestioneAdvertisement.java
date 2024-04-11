package unisa.gps.etour.control.GestioneAdvertisement;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.swing.ImageIcon;

import unisa.gps.etour.bean.BeanBanner;

/**
 * Interface General Manager of Banner and news.
 *
 * @Author Fabio Palladino
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University of Salerno
 */
public interface AdvertisementManagement extends Remote {
    /**
     * Inserts a new banner.
     *
     * @param restaurantId ID of the restaurant where the banner is displayed
     * @param bannerImage ImageIcon object representing the banner image
     * @throws RemoteException
     */
    public boolean insertBanner(int restaurantId, ImageIcon bannerImage) throws RemoteException;

    /**
     * Deletes a banner from the system.
     *
     * @param bannerId ID of the banner to be deleted
     * @return true if the operation is successful, false otherwise
     * @throws RemoteException
     */
    public boolean deleteBanner(int bannerId) throws RemoteException;

    /**
     * Modifies the data of the banner or the associated image.
     *
     * @param bannerId ID of the banner to be modified
     * @param bannerImage ImageIcon object representing the new banner image
     * @return true if the operation is successful, false otherwise
     * @throws RemoteException
     */
    public boolean modifyBanner(int bannerId, ImageIcon bannerImage) throws RemoteException;

    /**
     * Returns a list of banners of a particular restaurant.
     *
     * @param restaurantId ID of the restaurant owner of banners
     * @return HashMap containing the list of restaurant banners
     * @throws RemoteException
     */
    public HashMap<BeanBanner, ImageIcon> getBannersById(int restaurantId) throws RemoteException;
}