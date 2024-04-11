/*
 * FeedbackTableModel.java
 *
 * 1.0
 *
 * 21/05/2007
 *
 * 2007 eTour Project - Copyright by SE @ SA Lab - DMI University of Salerno
 */
package unisa.gps.etour.gui.operatoragency.tables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import unisa.gps.etour.bean.BeanVisitBC;
import unisa.gps.etour.bean.BeanVisitPR;

/**
 * <b>FeedbackTableModel</b>
 * <p>Container model of data for feedback received
 * related to cultural visits or refreshments.</p>
 *
 * @see javax.swing.table.AbstractTableModel
 * @see unisa.gps.etour.bean.BeanVisitBC
 * @see unisa.gps.etour.bean.BeanVisitPR
 * @version 1.0
 * @author Mario Gallo
 */
public class FeedbackTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] headers = {"Rating", "Comment", "Release Date", "Issued by"};
    private static final Class<?>[] columnClasses = {Integer.class, String.class, Date.class, String.class};
    private ArrayList<Object[]> data;

    /**
     * Default Constructor. We only provide the model but do not
     * load any data into it.
     */
    public FeedbackTableModel() {
        data = new ArrayList<>();
    }

    /**
     * Constructor that takes an ArrayList of BeanVisitBC or
     * BeanVisitPR as input and copies the data into the model
     * preparing it for display.
     *
     * @param pFeedbackData java.util.HashMap - the HashMap of bean.
     */
    public FeedbackTableModel(HashMap<?, String> pFeedbackData) {
        this();
        if (pFeedbackData == null || pFeedbackData.size() == 0) {
            return;
        }
        Iterator<?> iterator = pFeedbackData.keySet().iterator();
        while (iterator.hasNext()) {
            Object current = iterator.next();
            if (current instanceof BeanVisitBC) {
                insertVisitBC((BeanVisitBC) current, pFeedbackData.get(current));
            } else if (current instanceof BeanVisitPR) {
                insertVisitPR((BeanVisitPR) current, pFeedbackData.get(current));
            }
        }
    }
}
/**
 * Returns the number of columns provided by the model.
 *
 * @return int - the number of columns.
 */
public int getColumnCount() {
    return headers.length;
}

/**
 * Returns the number of rows currently in the model.
 *
 * @return int - the number of rows.
 */
public int getRowCount() {
    return data.size();
}

/**
 * Returns the column name from the provided index.
 *
 * @param pColumn - the column index.
 * @return String - the name of the column.
 * @throws IllegalArgumentException - if the column index is not present in the model.
 */
public String getColumnName(int pColumn) throws IllegalArgumentException {
    if (pColumn >= getColumnCount() || pColumn < 0) {
        throw new IllegalArgumentException("The column index is not present in the model.");
    }
    return headers[pColumn];
}

/**
 * Returns the object in the model at the provided row and column.
 *
 * @param pRow - the row index.
 * @param pColumn - the column index.
 * @return Object - the object contained in the selected cell.
 * @throws IllegalArgumentException - if the row or column index is not present in the model.
 */
public Object getValueAt(int pRow, int pColumn) throws IllegalArgumentException {
    if (pRow >= getRowCount() || pRow < 0) {
        throw new IllegalArgumentException("The row index is not present in the model.");
    }
    if (pColumn >= getColumnCount() || pColumn < 0) {
        throw new IllegalArgumentException("The column index is not present in the model.");
    }
    return data.get(pRow)[pColumn];
}

/**
 * Returns the class of objects in the column provided by the index.
 *
 * @param pColumn - the column index.
 * @return Class - the class of objects in the selected column.
 * @throws IllegalArgumentException - if the column index is not present in the model.
 */
public Class<?> getColumnClass(int pColumn) throws IllegalArgumentException {
    if (pColumn >= getColumnCount() || pColumn < 0) {
        throw new IllegalArgumentException("The column index is not present in the model.");
    }
    return columnClasses[pColumn];
}

/**
 * Returns whether the selected cell is editable.
 *
 * @param pRow - the row index.
 * @param pColumn - the column index.
 * @return boolean - true if the cell is editable, false otherwise.
 * @throws IllegalArgumentException - if the row or column index is not present in the model.
 */
public boolean isCellEditable(int pRow, int pColumn) throws IllegalArgumentException {
    return false;
}
/**
 * The method inherited from setValueAt in TableModel does not need to be included in FeedBackTableModel
 * since it provides the ability to change a single cell.
 * @deprecated
 */
@Deprecated
public void setValueAt(Object value, int row, int col) {
    // Method body intentionally left empty
}

/**
 * Inserts data about the feedback received from a cultural visit into the model from the Bean.
 *
 * @param pVisitBC - the bean that contains the feedback from a cultural visit.
 * @param pUsername - the username associated with the feedback.
 * @throws IllegalArgumentException - if the input parameters are invalid.
 */
public void insertVisitBC(BeanVisitBC pVisitBC, String pUsername) throws IllegalArgumentException {
    if (pVisitBC == null || pUsername == null || pUsername.equals("")) {
        throw new IllegalArgumentException("Invalid input parameters supplied.");
    }
    Object[] aRow = new Object[6];
    aRow[0] = pVisitBC.getVoto();
    aRow[1] = pVisitBC.getCommento();
    aRow[2] = pVisitBC.getDataVisit();
    aRow[3] = pUsername;
    aRow[4] = pVisitBC.getIdBeneCulturale();
    aRow[5] = pVisitBC.getIdTourist();
    data.add(aRow);
}

/**
 * Inserts data about the feedback received from a refreshment visit into the model from the Bean.
 *
 * @param pVisitPR - the bean that contains the feedback from a refreshment visit.
 * @param pUsername - the username associated with the feedback.
 * @throws IllegalArgumentException - if the input parameters are invalid.
 */
public void insertVisitPR(BeanVisitPR pVisitPR, String pUsername) throws IllegalArgumentException {
    if (pVisitPR == null || pUsername == null || pUsername.equals("")) {
        throw new IllegalArgumentException("Invalid input parameters supplied.");
    }
    Object[] aRow = new Object[6];
    aRow[0] = pVisitPR.getVote();
    aRow[1] = pVisitPR.getCommento();
    aRow[2] = pVisitPR.getDataVisit();
    aRow[3] = pUsername;
    aRow[4] = pVisitPR.getIdRefreshmentpoint();
    aRow[5] = pVisitPR.getIdTourist();
    data.add(aRow);
}

/**
 * Updates the comment feedback contained in the selected table row.
 *
 * @param pNewComment - the new comment.
 * @param pRow - the row to update.
 * @throws IllegalArgumentException - if the row index is not present in the model or the new comment is null.
 */
public void modificationCommento(String pNewComment, int pRow) throws IllegalArgumentException {
    if (pRow >= getRowCount() || pRow < 0) {
        throw new IllegalArgumentException("The row index is not present in the model.");
    }
    if (pNewComment == null) {
        throw new IllegalArgumentException("The new comment cannot be null.");
    }
    data.get(pRow)[1] = pNewComment;
    fireTableDataChanged();
}

/**
 * Returns the ID of the feedback for the provided row number.
 *
 * @param pRow - the row number.
 * @return int[] - the ID of the feedback.
 * @throws IllegalArgumentException - if the row index is not present in the model.
 */
public int[] getIDFeedback(int pRow) throws IllegalArgumentException {
    if (pRow >= getRowCount() || pRow < 0) {
        throw new IllegalArgumentException("The row index is not present in the model.");
    }
    int[] ids = new int[2];
    ids[0] = (Integer) data.get(pRow)[4];
    ids[1] = (Integer) data.get(pRow)[5];
    return ids;
}

/**
 * Returns the ID of the feedback for the provided row number and removes it from the model.
 *
 * @param pRow - the row number.
 * @return int[] - the ID of the feedback.
 * @throws IllegalArgumentException - if the row index is not present in the model.
 */
public int[] removeFeedback(int pRow) throws IllegalArgumentException {
    int[] ids = getIDFeedback(pRow);
    data.remove(pRow);
    return ids;
}









