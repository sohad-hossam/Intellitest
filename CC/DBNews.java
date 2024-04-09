/**
 * Stubs used for testing the class ManagementAdvertisementAgency
 *
 * @Author Fabio Palladino
 * @Version 0.1
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
package unisa.gps.etour.control. ManagementAdvertisement.test.stubs;

import java.sql.SQLException;
import java.util.ArrayList;

import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.repository.IDBNews;
import unisa.gps.etour.util.CostantiGlobali;

public class DBNews implements IDBNews {
    /** Static field that stores the number of test */
    private static int NUM_TEST = 0;

    /* (non-Javadoc)
     * @see unisa.gps.etour.repository.IDBNews#cancelNews(int)
     */
    public boolean deleteNews(int pIdNews) throws SQLException {
        if (NUM_TEST == 1) {
            return true;
        } else if (NUM_TEST == 2) {
            throw new SQLException();
        }
        return true;
    }

    /* (non-Javadoc)
     * @see unisa.gps.etour.repository.IDBNews#insertNews(unisa.gps.etour.bean.BeanNews)
     */
    public boolean insertNews(BeanNews pNews) throws SQLException {
        if (NUM_TEST == 7) {
            return false;
        } else {
            return true;
        }
    }

    /* (non-Javadoc)
     * @see unisa.gps.etour.repository.IDBNews#modifyNews(unisa.gps.etour.bean.BeanNews)
     */
    public boolean modifyNews(BeanNews pNews) throws SQLException {
        return true;
    }

    /* (non-Javadoc)
     * @see unisa.gps.etour.repository.IDBNews#getNews()
     */
    public ArrayList<BeanNews> getNews() throws SQLException {
        ArrayList<BeanNews> toReturn = new ArrayList<>();

        if (NUM_TEST == 5) {
            for (int i = 1; i <= GlobalConstants.MAX_NEWS_ACTIVE; i++) {
                toReturn.add(new BeanNews());
            }
            return toReturn;
        } else {
            toReturn.add(new BeanNews());
            return toReturn;
        }
    }

    /**
     * Set the number of tests in progress
     *
     * @param num_test
     */
    public static void setNUM_TEST(int num_test) {
        NUM_TEST = num_test;
    }
}
