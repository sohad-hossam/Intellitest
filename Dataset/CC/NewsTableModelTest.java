/**
  * Class tests for NewsTableModel
  *
  * @ Author Mario Gallo
  * @ Version 0.1
  *
  *    2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
  */
package unisa.gps.etour.gui.operatoreagenzia.tables.test;

import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.gui.operatoreagenzia.tables.NewsTableModel;
import junit.framework.TestCase;

public class NewsTableModelTest extends TestCase {

    private NewsTableModel tableModel;
    private BeanNews aNews;
    private BeanNews aNewsModified;

    public NewsTableModelTest(String pName) {
        super(pName);
        aNews = new BeanNews("An example of news", new Date(), new Date(), 2, 1);
        aNewsModified = new BeanNews("A news amended sample", new Date(), new Date(), 3, 1);
    }

    protected void setUp() throws Exception {
        super.setUp();
        tableModel = new NewsTableModel();
    }

    // Test constructor with ArrayList
    public void testConstructorWithArrayList() {
        ArrayList<BeanNews> testList = new ArrayList<BeanNews>();
        for (int i = 0; i < 10; i++) {
            testList.add(new BeanNews("text" + i, new Date(), new Date(), 5, i));
        }
        NewsTableModel model = new NewsTableModel(testList);
        for (int i = 0; i < 10; i++) {
            assertSame(testList.get(i).getId(), model.getID(i));
        }
    }

    // Test constructor with null ArrayList
    public void testConstructorWithNullArrayList() {
        NewsTableModel model = new NewsTableModel(null);
        assertNull(model); // You might want to adjust this based on your implementation
    }

    // Test constructor with empty ArrayList
    public void testConstructorWithEmptyArrayList() {
        NewsTableModel model = new NewsTableModel(new ArrayList<BeanNews>());
        assertNotNull(model);
        assertEquals(0, model.getRowCount());
    }

}
