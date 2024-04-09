/**
 * Attribute Definition
 *
 * @Author Fabio Palladino
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
package unisa.gps.etour.control.AdvertisementManagement.test.stubs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanConvention;
import unisa.gps.etour.bean.Restaurant;
import unisa.gps.etour.repository.IDBConvention;

public class DBConvention implements IDBConvention {
    private static int NUM_TEST = 1;

    /* (Non-Javadoc)
     * @see unisa.gps.etour.repository.IDBConvention#cancelConvention(int)
     */
    public boolean deleteAgreement(int pIdConvention) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (Non-Javadoc)
     * @see unisa.gps.etour.repository.IDBConvention#getHistoricalConvention(int)
     */
    public ArrayList<BeanConvention> getAgreementHistory(int idRestaurant
) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (Non-Javadoc)
     * @see unisa.gps.etour.repository.IDBConvention#insertAgreement(unisa.gps.etour.bean.BeanConvention)
     */
    public boolean insertAgreement(BeanConvention pConvention) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (Non-Javadoc)
     * @see unisa.gps.etour.repository.IDBConvention#modifyAgreement(unisa.gps.etour.bean.BeanConvention)
     */
    public boolean modifyAgreement(BeanConvention pConvention) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (Non-Javadoc)
     * @see unisa.gps.etour.repository.IDBConvention#getActiveAgreement(int)
     */
    public BeanConvention getActiveAgreement(int pIdRestaurant
) throws SQLException {
        BeanConvention agreement = new BeanConvention();
        agreement.setActive(true);
        agreement.setEndDate(new Date());
        agreement.setStartDate(new Date());
        agreement.setId(12);
        agreement.setIdRestaurant
(3);
        agreement.setPrice(100);

        if (NUM_TEST == 1) {
            /* Test banners allowed */
            agreement.setMaxBanner(4);
            return agreement;
        } else if (NUM_TEST == 2) {
            /* Test banners not allowed */
            agreement.setMaxBanner(3);
            return agreement;
        } else {
            return null;
        }
    }

    /* (Non-Javadoc)
     * @see unisa.gps.etour.repository.IDBConvention#getActiveAgreementListPR()
     */
    public ArrayList<BeanRestaurant> getActiveAgreementListPR() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public static void setNUM_TEST(int num_test) {
        NUM_TEST = num_test;
    }
}
