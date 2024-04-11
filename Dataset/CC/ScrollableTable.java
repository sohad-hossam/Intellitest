package unisa.gps.etour.gui.agencyoperator.tables;

import java.awt.*;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.table.*;

/**
 * Create a custom JTable that can be displayed through
 * components that enable scrolling.
 * @See javax.swing.JTable
 * @See javax.swing.Scrollable
 * @Author _OniZuKa_
 * @Version 1.0
 */
public class ScrollableTable extends JTable implements Scrollable {

    private static final int maxUnitIncrement = 20;

    public ScrollableTable() {
        super();
    }

    public ScrollableTable(TableModel tm) {
        super(tm);
        setGridColor(Color.LIGHT_GRAY);
        setIntercellSpacing(new Dimension(5, 0));
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {

        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        if (direction < 0) {
            int newPos = currentPosition - (currentPosition / maxUnitIncrement) * maxUnitIncrement;
            return (newPos == 0) ? maxUnitIncrement : newPos;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement - currentPosition;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
