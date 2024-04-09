package unisa.gps.etour.gui.operatoragency.tables;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import unisa.gps.etour.util.Point3D;

/**
 * <b> Point3DRenderer </b>
 * <p> * This class creates a custom renderer for
 * Objects of type Point3D. </ p>
 *
 * @ See javax.swing.table.TableCellRenderer
 * @ See unisa.gps.etour.util.Point3D
 * @ Version 1.0
 * @ Author Mario Gallo
 *
 *
 */
public class Point3DRenderer implements TableCellRenderer {

/**
* Method that returns the custom component for the
* Display of the data contained in the cell of a table.
*
* @ Param pTable JTable - the table.
* @ Param Object pValue - the data.
* @ Param boolean pSelected --
* <ul>
* <li> <i> True </i> if the cell is selected.
* <li> <i> False </i> otherwise.
* </ Ul>
* @ Param boolean pHasFocus --
* <ul>
* <li> <i> True </i> if the cell has the focus.
* <li> <i> False </i> otherwise.
* </ Ul>
* @ Param int pRow - the line number.
* @ Param int pColumn - the column number.
* @ Return Component - the component that customizes render the cell.
* @ Throws IllegalArgumentException - if the value of the cell can not
* Be rendered by this renderer.
 */
public Component getTableCellRendererComponent(JTable pTable,
Object pValue, boolean pSelected, boolean pHasFocus, int pRow,
int pColumn) {
if (!(pValue instanceof Point3D)) {
throw new IllegalArgumentException("Unexpected cell value.");
}
Point3D aPoint = (Point3D) pValue;
String point = "(" + aPoint.getLatitude() + ", " + aPoint.getLongitude() + ", " + aPoint.getAltitude() + ")";
JLabel aLabel = new JLabel(point, SwingConstants.CENTER);
if (pSelected) {
aLabel.setForeground(pTable.getSelectionForeground());
aLabel.setBackground(pTable.getSelectionBackground());
}
return aLabel;
}
}
