/**
 * Class that implements management services of advertisements
 * For the restaurant operator.
 *
 * @Author Fabio Palladino
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by SE @ SA Lab DMI University of Salerno
 */
package unisa.gps.etour.control.GestioneAdvertisement;

import java.rmi.RemoteException;

public class AdvertisementManagementRestaurant extends AdvertisementManagement implements IAdvertisementManagementRestaurant {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor, calls the constructor of the superclass.
     * @throws RemoteException
     */
    public AdvertisementManagementRestaurant() throws RemoteException {
        super();
    }

}