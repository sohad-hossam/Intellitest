/*
* TAGTableModel.java
*
* 1.0
*
* 28/05/2007
*
*  2007 eTour Project - Copyright by SE @ SA Lab - DMI - University of Salerno
*/
package unisa.gps.etour.gui.agencyoperator;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import unisa.gps.etour.bean.BeanTag;

/**
* <p>
* <B>Title:</B> TagTableModel
* </p>
* <p>
* <B>Description:</B> TableModel for dynamic management of Table
* Within the section Tag Management
* </p>
*
* @Author _Lello_
* @Version 1.0
*/
public class TAGTableModel extends AbstractTableModel {


private static final long serialVersionUID = 1L;
private static final String[] headers = { "Name", "Description" };
private static final Class<?>[] columnClasses = { String.class, String.class };
private Vector<Object[]> data;

/**
* Constructor for class TAGTableModel
*
* @param tags BeanTag[]
*/
public TAGTableModel(BeanTag[] tags) {
    data = new Vector<>();
    for (int i = 0; i < tags.length; i++) {
        Object[] newData = new Object[2];
        newData[0] = tags[i].getId();
        newData[1] = tags[i].getName();
        newData[2] = tags[i].getDescription();
        data.add(newData);
    }
}

/**
* Returns the number of columns
*/
public int getColumnCount() {
    return headers.length;
}

/**
* Returns the number of rows
*/
public int getRowCount() {
    return data.size();
}

/**
* Returns the column heading at index pCol
*
* @param pCol
*/
public String getColumnName(int pCol) {
    return headers[pCol];
}

/**
* Returns the value at the specified cell (row, column)
*
* @param pRow
* @param pCol
*/
public Object getValueAt(int pRow, int pCol) {
    return data.get(pRow)[pCol];
}

/**
* Returns the class of the specified column
*
* @param pCol
*/
public Class<?> getColumnClass(int pCol) {
    return columnClasses[pCol];
}

/**
* Always returns false because the cells in the table are not editable
*
* @param row
* @param col
* @return false
*/
public boolean isCellEditable(int row, int col) {
    return false;
}

/**
* This method is empty.
* Cannot include an element within a cell.
*
* @deprecated
*/
public void setValueAt(Object value, int row, int col) {
    // Empty method body
}

}
