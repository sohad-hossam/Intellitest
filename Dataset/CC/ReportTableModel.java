/* ReportTableModel.java
*
* 1.0
*
* 21/05/2007
*
* 2007 eTour Project - Copyright by SE @ SA Lab - DMI - University of Salerno
*/
package unisa.gps.etour.gui.operatoreagenzia;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

import unisa.gps.etour.bean.BeanCulturalAsset;
import unisa.gps.etour.bean.BeanRestaurant;
import unisa.gps.etour.bean.util.Point3D;

public class ReportTableModel extends AbstractTableModel {

/**
*
*/
private static final long serialVersionUID = 1L;
private static final String[] headers = {"Name", "Description", "Address", "City", "Province"};
private static final Class<?>[] columnClasses = {String.class, String.class, String.class, String.class, String.class};
private Vector<Object[]> data;

public ReportTableModel(BeanCulturalAsset[] culturalAssets, BeanRestaurant[] restaurants) {
    data = new Vector<>();
    for (int i = 0; i < restaurants.length; i++) {
        Object[] newRow = new Object[5];
        newRow[0] = restaurants[i].getName();
        newRow[1] = restaurants[i].getDescription();
        newRow[2] = restaurants[i].getAddress();
        newRow[3] = restaurants[i].getCity();
        newRow[4] = restaurants[i].getProvince();

        setValueAt(newRow, i);
    }
    for (int i = 0; i < culturalAssets.length; i++) {
        Object[] newRow = new Object[5];
        newRow[0] = culturalAssets[i].getName();
        newRow[1] = culturalAssets[i].getDescription();
        newRow[2] = culturalAssets[i].getAddress();
        newRow[3] = culturalAssets[i].getCity();
        newRow[4] = culturalAssets[i].getProvince();
        setValueAt(newRow, restaurants.length + i);
    }
}

public int getColumnCount() {
    return headers.length;
}

public int getRowCount() {
    return data.size();
}

public String getColumnName(int col) {
    return headers[col];
}

public Object getValueAt(int row, int col) {
    return data.get(row)[col];
}

public Class<?> getColumnClass(int col) {
    return columnClasses[col];
}

public boolean isCellEditable(int row, int col) {
    return false;
}

public void setValueAt(Object value, int row, int col) {
    if (row >= getRowCount()) {
        Object[] newRow = new Object[headers.length];
        newRow[col] = value;
        data.add(newRow);
    } else {
        data.get(row)[col] = value;
    }
}

public void setValueAt(Object[] value, int row) throws IllegalArgumentException {
    if (value.length != headers.length) {
        System.out.println(value.length);
        System.out.println(headers.length);
        throw new IllegalArgumentException();
    }
    if (row >= getRowCount()) {
        data.add(value);
    } else {
        data.remove(row);
        data.add(row, value);
    }
}
}
