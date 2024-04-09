/*
 * SiteTableModel.java
 *
 * 1.0
 *
 * 21/05/2007
 *
 * 2007 eTour Project - Copyright by SE @ SA Lab - DMI - University of Salerno
 */
package Handheld;

import Bean.BeanRefreshmentPoint;
import javax.swing.table.AbstractTableModel;
import Bean.*;
import Util.Point3D;

/**
 * <b>SiteTableModel</b>
 * Serves as a data container <p> of cultural or refreshment areas that need
 * to be displayed in a JTable.
 * @See javax.swing.table.AbstractTableModel
 * @See javax.swing.JTable
 * @See unisa.gps.etour.bean.BeanCulturalAsset
 * @See unisa.gps.etour.bean.BeanRefreshmentPoint
 * @Version 1.0
 * @Author Raphael Landi
 */
public class SiteTableModel extends AbstractTableModel {
    String[] columnNames = {"Name", "City", "Distance"};
    Object[][] cells;
    Point3D sitePosition;
    Point3D myLocation;
    
    SiteTableModel(BeanRefreshmentPoint[] pr, Point3D myLocation) {
        super();
        cells = new Object[pr.length][3]; // First value = rows, second value = columns
        for (int i = 0; i < pr.length; i++) {
            cells[i][0] = pr[i].getName();
            cells[i][1] = pr[i].getCity();
        }
    }
    
    SiteTableModel(BeanCulturalAsset[] bc, Point3D myLocation) {
        super();
        cells = new Object[bc.length][3]; // First value = rows, second value = columns
        for (int i = 0; i < bc.length; i++) {
            cells[i][0] = bc[i].getName();
            cells[i][1] = bc[i].getCity();
        }
    }
    
    public int getRowCount() {
        return cells.length;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public Object getValueAt(int r, int c) {
        if (c < columnNames.length - 1)
            return cells[r][c];
        else {
            double value = myLocation.distance(sitePosition);
            return new Double(value);
        }
    }
    
    public String getColumnName(int c) {
        return columnNames[c];
    }
}
