/**
 * Stubs for dynamic class DBBanner. Used for testing.
 * Class package GestioneAdvertisement.
 *
 * @Author Fabio Palladino
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
package unisa.gps.etour.control.GestioneAdvertisement.test.stubs;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanBanner;
import unisa.gps.etour.repository.IDBBanner;

public class DBBanner implements IDBBanner {

    private static int NUM_TEST = 0;

    public boolean deleteBanner(int pIdBanner) throws SQLException {
        if (NUM_TEST == 5) {
            throw new SQLException();
        } else {
            return true;
        }
    }

    /**
     * @see unisa.gps.etour.repository.IDBBanner#insertBanner(unisa.gps.etour.bean.BeanBanner)
     */
    public boolean insertBanner(BeanBanner pBanner) throws SQLException {
        if (NUM_TEST == 1 || NUM_TEST == 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see unisa.gps.etour.repository.IDBBanner#modifyBanner(unisa.gps.etour.bean.BeanBanner)
     */
    public boolean modifyBanner(BeanBanner pBanner) throws SQLException {
        return true;
    }

    /**
     * @see unisa.gps.etour.repository.IDBBanner#getBanner(int)
     */
    public ArrayList<BeanBanner> getBanner(int pIdRestaurant) throws SQLException {
        ArrayList<BeanBanner> toReturn = new ArrayList<>();

        if (NUM_TEST == 1 || NUM_TEST == 2) {
            // Must return an ArrayList with 3 elements
            toReturn.add(new BeanBanner());
            toReturn.add(new BeanBanner());
            toReturn.add(new BeanBanner());
            return toReturn;
        } else if (NUM_TEST == 4) {
            toReturn.add(new BeanBanner(3, "c:\\ProvaBannerInsertion.jpg", 55));
            toReturn.add(new BeanBanner(4, "c:\\ProvaBannerInsertion.jpg", 55));
            toReturn.add(new BeanBanner(5, "c:\\ProvaBannerInsertion.jpg", 55));
            toReturn.add(new BeanBanner(5, "c:\\ProvaBannerInsertion.jpg", 55));
            return toReturn;
        } else {
            return null;
        }
    }

    /**
     * @see unisa.gps.etour.repository.IDBBanner#getBannerById(int)
     */
    public BeanBanner getBannerById(int pIdBanner) throws SQLException {
        if (NUM_TEST == 7) {
            return null;
        } else {
            return new BeanBanner(55, "c:/ProvaBanner.jpg", 3);
        }
    }

    public static void setNUM_TEST(int num_test) {
        NUM_TEST = num_test;
    }
}
