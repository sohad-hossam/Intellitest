/**
* Test case for class TextNewsRenderer
*
* @ Author Mario Gallo
* @ Version 0.1    2007 eTour Project - Copyright by DMI SE @ SA Lab --
* University of Salerno
*/
package unisa.gps.etour.gui.operatoreagenzia.tables.test;

import java.util.Date;
import javax.swing.JTable;
import javax.swing.JTextArea;
import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.gui.operatoreagenzia.tables.NewsTableModel;
import unisa.gps.etour.gui.operatoreagenzia.tables.TextNewsRenderer;
import junit.framework.TestCase;

public class TestCase extends TextNewsRendererTest {

    private TextNewsRenderer renderer;
    private BeanNews activeNews;
    private BeanNews expiredNews;
    private JTable table;

    public TestCase() {
        super();
        renderer = new TextNewsRenderer();
        activeNews = new BeanNews("Here's a news active", new Date(),
                                  new Date(120, 1, 1), 5, 0);
        expiredNews = new BeanNews("Here's a news Expired", new Date(),
                                    new Date(), 5, 0);
        table = new JTable(new NewsTableModel());
    }

    /*
    * Verify the behavior of the method with the correct parameters.
    */
    public void testGetTableCellRendererCorrectParameters() {
        NewsTableModel model = (NewsTableModel) table.getModel();
        model.insertNews(activeNews);
        model.insertNews(expiredNews);

        // Test the renderer with a news active.
        JTextArea area = (JTextArea) renderer.getTableCellRendererComponent(
                                        table, "Here's a news active", true, true, 0, 0);
        assertEquals(activeNews.getNews(), area.getText());

        // Test the renderer with a news expired.
        area = (JTextArea) renderer.getTableCellRendererComponent(table,
                                        "Here's a news Expired", true, true, 0, 0);
        assertEquals(expiredNews.getNews(), area.getText());
    }

    /*
    * Verification comparing the table with a table without NewsTableModel
    * Associated.
    */
    public void testGetTableCellRendererNoNewsModel() {

        JTable anotherTable = new JTable();
        try {
            renderer.getTableCellRendererComponent(anotherTable,
                            "Here's a news", true, true, 0, 0);
            fail("Should be thrown.");
        } catch (IllegalArgumentException success) {

        }
    }

    /*
    * Verify the behavior of the method with a parameter to null.
    */
    public void testGetTableCellRendererNullParameter() {
        try {
            renderer.getTableCellRendererComponent(table, null, true, true, 0,
                                                    0);
            fail("Should be thrown.");
        } catch (IllegalArgumentException success) {

        }
    }

    /*
    * Verify the behavior of the method with a data type unexpected.
    */
    public void testGetTableCellRendererUnexpectedType() {
        try {
            renderer.getTableCellRendererComponent(table, 12, true, true, 0, 0);
            fail("Should be thrown.");
        } catch (IllegalArgumentException success) {

        }

    }
}
