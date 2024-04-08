package unisa.gps.etour.control;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import unisa.gps.etour.control.CulturalAssetsManagement.CommonCulturalAssetsManagement;
import unisa.gps.etour.control.CulturalAssetsManagement.ICommonCulturalAssetsManagement;

/**
 * This is the entry point of the control server. This class is responsible for
 * deploying services on the RMI registry, thus making some services available and usable.
 *
 * @ Author Michelangelo De Simone
 * @ Version 0.1 © 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
public class ControlServerLauncher {

    /**
     * Entry point of ControlServer.
     *
     * @param args The command line parameters.
     */
    public static void main(String[] args) {
        // *** WARNING *** EXPERIMENTAL

        try {
            // CommonCulturalAssetsManagement is the class that implements the interface ICommonCulturalAssetsManagement,
            // This interface is used by both the client and the server.
            CommonCulturalAssetsManagement commonCulturalAssetsManagement = new CommonCulturalAssetsManagement();

            // Here you create the stub for the registry, making it clear to the RMI system that you are exporting the object on a commonCulturalAssetsManagement.
            // Using an anonymous port.
            ICommonCulturalAssetsManagement stubCommonCulturalAssetsManagement = (ICommonCulturalAssetsManagement) UnicastRemoteObject.exportObject(commonCulturalAssetsManagement, 0);

            // This calls the registry (default is on localhost) and "binds" the stub.
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("ICommonCulturalAssetsManagement", stubCommonCulturalAssetsManagement);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
        }
    }
}
