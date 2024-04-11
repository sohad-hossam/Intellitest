package unisa.gps.etour.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import unisa.gps.etour.gui.operatoragency.IScheda;

/**
 * Class for handling custom internal frames inserted in a JDesktopPane.
 *
 * @version 0.1
 * @author Mario Gallo
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class DeskManager extends DefaultDesktopManager {
    private static final String URL_IMAGES = "/unisa/gps/eTour/gui/images/";
    private JPopupMenu deskMenu;
    private JMenuItem minimizeAll;
    private JMenuItem restoreAll;
    private JMenuItem closeAll;
    private Vector<JInternalFrame> iconifiedFrames;
    private int locationX;
    private int locationY;

    /**
     * Default Constructor.
     */
    public DeskManager() {
        super();
        iconifiedFrames = new Vector<>();
        initializeDeskMenu();
        locationX = 0;
        locationY = -1;
    }

    /**
     * Manages the movement of JInternalFrame inside the area of
     * JDesktopPane, preventing the frames from being moved outside the visible area.
     *
     * @param jComponent - the component of which to manage the move.
     * @param x - x coordinate of the point where the component was moved.
     * @param y - y coordinate of the point where the component was moved.
     */
    public void dragFrame(JComponent jComponent, int x, int y) {
        if (jComponent instanceof JInternalFrame) {
            JInternalFrame frame = (JInternalFrame) jComponent;
            if (frame.isIcon()) {
                x = frame.getLocation().x;
                y = frame.getLocation().y;
            } else {
                JDesktopPane desk = frame.getDesktopPane();
                Dimension d = desk.getSize();
                if (x < 0) {
                    x = 0;
                } else if (x + frame.getWidth() > d.width) {
                    x = d.width - frame.getWidth();
                }

                if (y < 0) {
                    y = 0;
                } else if (y + frame.getHeight() > d.height) {
                    y = d.height - frame.getHeight();
                }
            }
        }

        super.dragFrame(jComponent, x, y);
    }

    /**
     * Customize the action of minimizing the JInternalFrame to an icon, creating
     * clickable bars at the bottom of JDesktopPane.
     *
     * @param frame - a frame inside a JDesktopPane.
     */
    public void iconifyFrame(JInternalFrame frame) {
        try {
            JDesktopPane desk = frame.getDesktopPane();
            Dimension d = desk.getSize();
            frame.setClosable(false);
            frame.setMaximizable(true);
            frame.setIconifiable(false);
            Rectangle bounds;
            if (frame.isMaximum()) {
                bounds = frame.getNormalBounds();
            } else {
                bounds = frame.getBounds();
            }
            frame.setSize(200, 30);
            setPreviousBounds(frame, bounds);
            if (iconifiedFrames.isEmpty()) {
                locationX = 0;
            } else {
                locationX += 200;
            }
            if (locationY == -1) {
                locationY = d.height - 30;
            }
            if (locationX + 200 > d.width) {
                locationX = 0;
                locationY -= 30;
            }
            frame.setLocation(locationX, locationY);
            frame.setResizable(false);
            iconifiedFrames.add(frame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

/**
 * Restore the frame from the effect of minimizing, resetting the
 * position and size it had before.
 *
 * @param frame - a frame inside a JDesktopPane.
 */
public void deiconifyFrame(JInternalFrame frame) {
    try {
        JDesktopPane desk = frame.getDesktopPane();
        Dimension deskSize = desk.getSize();
        iconifiedFrames.remove(frame);
        Rectangle previousBounds = getPreviousBounds(frame);
        if (previousBounds.width > deskSize.width) {
            previousBounds.width = deskSize.width;
            previousBounds.x = 0;
        }
        if (previousBounds.width + previousBounds.x > deskSize.width) {
            previousBounds.x = (deskSize.width - previousBounds.width) / 2;
        }
        if (previousBounds.height > deskSize.height) {
            previousBounds.height = deskSize.height;
            previousBounds.y = 0;
        }
        if (previousBounds.height + previousBounds.y > deskSize.height) {
            previousBounds.y = (deskSize.height - previousBounds.height) / 2;
        }
        frame.setSize(previousBounds.width, previousBounds.height);
        frame.setLocation(previousBounds.x, previousBounds.y);
        frame.setIconifiable(true);
        frame.setClosable(true);
        if (frame instanceof IScheda) {
            frame.setMaximizable(false);
            frame.setResizable(false);
        } else {
            frame.setMaximizable(true);
            frame.setResizable(true);
        }
        locationX -= 200;
        if (locationX < 0) {
            locationX = deskSize.width / 200 - 200;
            if (locationY != deskSize.height - 30) {
                locationY -= 30;
            }
        }
        repaintIconifiedFrames(desk);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
/**
 * Return the focus to a selected frame, and, if the frame
 * is iconified, then deiconify it.
 *
 * @param frame - a frame within a JDesktopPane.
 */
public void activateFrame(JInternalFrame frame) {
    try {
        if (frame.isIcon()) {
            frame.setIcon(false);
        }
        frame.setSelected(true);
        super.activateFrame(frame);
    } catch (PropertyVetoException e) {
        e.printStackTrace();
    }
}

/**
 * Center the frame supplied as a parameter in JDesktopPane.
 *
 * @param frame - a frame inside a JDesktopPane.
 */
public void centerFrame(JInternalFrame frame) {
    JDesktopPane desk = frame.getDesktopPane();
    Dimension d = desk.getSize();
    Dimension f = frame.getSize();
    frame.setLocation(d.width / 2 - f.width / 2, d.height / 2 - f.height / 2);
}

/**
 * Redraw the iconified frames on the desktop provided.
 *
 * @param desk - a desktop associated with a DeskManager.
 * @throws IllegalArgumentException - if the supplied JDesktopPane is not associated with a DeskManager.
 */
public void repaintIconifiedFrames(JDesktopPane desk) throws IllegalArgumentException {
    if (desk.getDesktopManager() != this) {
        throw new IllegalArgumentException(
            "No object of type DeskManager associated was found"
        );
    }
    Iterator<JInternalFrame> iconified = iconifiedFrames.iterator();
    int i = 0;
    int xLocation;
    int yLocation = desk.getHeight() - 30;
    while (iconified.hasNext()) {
        JInternalFrame current = iconified.next();
        xLocation = 200 * i;
        if (xLocation + 200 >= desk.getWidth()) {
            xLocation = 0;
            yLocation -= 30;
            i = 0;
        }
        current.setLocation(xLocation, yLocation);
        i++;
    }
}
/**
 * Redraw (and resize if necessary) all the frames contained in a
 * JDesktopPane.
 *
 * @param desk - a desktop pane.
 * @throws IllegalArgumentException - if the supplied JDesktopPane is not
 * Associated with a desktop manager like DeskManager.
 */
public void repaintAllFrames(JDesktopPane desk) throws IllegalArgumentException {
    if (desk.getDesktopManager() != this) {
        throw new IllegalArgumentException(
            "No object of type DeskManager associated was found"
        );
    }
    JInternalFrame[] frames = desk.getAllFrames();
    Dimension deskSize = desk.getSize();
    for (int i = 0; i < frames.length; i++) {
        JInternalFrame current = frames[i];
        if (!current.isIcon()) {
            Rectangle frameBounds = current.getBounds();
            if (frameBounds.width > deskSize.width)
                frameBounds.width = deskSize.width;
            if (frameBounds.height > deskSize.height)
                frameBounds.height = deskSize.height;
            if (frameBounds.x + frameBounds.width > deskSize.width)
                frameBounds.x = deskSize.width - frameBounds.width;
            if (frameBounds.y + frameBounds.height > deskSize.height)
                frameBounds.y = deskSize.height - frameBounds.height;
            current.setBounds(frameBounds);
        }
    }
    repaintIconifiedFrames(desk);
}

/**
 * Open a frame of the specified class using the cascade display.
 * If a frame of the given class already exists, it is activated.
 *
 * @param clazz - a class type that extends JInternalFrame.
 * @param desk - a desktop pane.
 * @throws IllegalArgumentException - if the provided class is not a JInternalFrame.
 */
public void openFrame(Class clazz, JDesktopPane desk) throws IllegalArgumentException {
    if (clazz.getSuperclass() != JInternalFrame.class) {
        throw new IllegalArgumentException(
            "The provided class has to be a subclass of javax.swing.JInternalFrame."
        );
    }
    try {
        JInternalFrame[] frames = desk.getAllFrames();
        int i;
        for (i = 0; i < frames.length; i++) {
            if (frames[i].getClass().equals(clazz))
                break;
        }
        if (i == frames.length) {
            JInternalFrame newFrame = (JInternalFrame) clazz.newInstance();
            desk.add(newFrame, Integer.MAX_VALUE);
            Dimension frameSize = newFrame.getPreferredSize();
            newFrame.setSize(frameSize);
            Dimension deskSize = desk.getSize();
            Point newPos = new Point(10, 10);
            for (i = frames.length - 1; i >= 0; i--) {
                if (frames[i].getLocation().equals(newPos)) {
                    newPos.x = frames[i].getLocation().x + 30;
                    newPos.y = frames[i].getLocation().y + 30;
                }
            }
            if ((newPos.x + frameSize.width > deskSize.width) || (newPos.y + frameSize.height > deskSize.height))
                centerFrame(newFrame);
            else
                newFrame.setLocation(newPos);
            newFrame.setVisible(true);
        } else {
            activateFrame(frames[i]);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
/**
 * Displays a popup menu with options for frames of a desktop pane
 * at the selected location.
 *
 * @param point - the point where to place the menu.
 * @param desk - a JDesktopPane which has an associated
 * instance of DeskManager.
 * @throws IllegalArgumentException - if a JDesktopPane that is not associated
 * with a DeskManager is provided as a parameter.
 */
public void showPopupMenu(Point point, JDesktopPane desk) {
    if (desk.getDesktopManager() != this) {
        throw new IllegalArgumentException(
            "No object of type DeskManager associated was found"
        );
    }
    restoreAll.setEnabled(true);
    closeAll.setEnabled(true);
    restoreAll.setEnabled(true);
    JInternalFrame[] frames = desk.getAllFrames();
    if (frames.length == 0) {
        restoreAll.setEnabled(false);
        closeAll.setEnabled(false);
        restoreAll.setEnabled(false);
    }
    if (iconifiedFrames.size() == 0) {
        restoreAll.setEnabled(false);
    }
    if (iconifiedFrames.size() == frames.length) {
        restoreAll.setEnabled(false);
    }
    deskMenu.show(desk, point.x, point.y);
}

/**
 * Deiconifies all frames previously iconified.
 */
public void deiconifyAll() {
    if (iconifiedFrames.size() != 0) {
        Vector<JInternalFrame> copy = (Vector<JInternalFrame>) iconifiedFrames.clone();
        Iterator<JInternalFrame> frames = copy.iterator();
        while (frames.hasNext()) {
            try {
                frames.next().setIcon(false);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        copy = null;
        iconifiedFrames.removeAllElements();
    }
}
/**
 * Minimizes all frames of a provided JDesktopPane associated with an
 * instance of DeskManager.
 *
 * @param desk - a desktop pane.
 * @throws IllegalArgumentException - if a JDesktopPane that is not associated
 * with a DeskManager is provided as a parameter.
 */
public void iconifyAll(JDesktopPane desk) {
    if (desk.getDesktopManager() != this) {
        throw new IllegalArgumentException(
            "No object of type DeskManager associated was found"
        );
    }
    JInternalFrame[] frames = desk.getAllFrames();
    for (int i = 0; i < frames.length; i++) {
        try {
            frames[i].setIcon(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Closes all frames in a given JDesktopPane.
 *
 * @param desk - a desktop pane associated with an instance of DeskManager.
 * @throws IllegalArgumentException - if a JDesktopPane that is not associated
 * with a DeskManager is provided as a parameter.
 */
public void closeAll(JDesktopPane desk) {
    if (desk.getDesktopManager() != this) {
        throw new IllegalArgumentException(
            "No object of type DeskManager associated was found"
        );
    }
    JInternalFrame[] frames = desk.getAllFrames();
    if (frames.length != 0) {
        for (int i = 0; i < frames.length; i++) {
            frames[i].dispose();
        }
        iconifiedFrames.removeAllElements();
    }
}

/**
 * Initialize the DeskMenu.
 */
public void initializeDeskMenu() {
    deskMenu = new JPopupMenu();
    restoreAll = new JMenuItem("Collapse All");
    restoreAll.setIcon(new ImageIcon(getClass().getResource(
        URL_IMAGES + "reduceAll.png")));
    restoreAll = new JMenuItem("Reset All");
    restoreAll.setIcon(new ImageIcon(getClass().getResource(
        URL_IMAGES + "activateall.png")));
    Closeall = new JMenuItem("Close All");
    closeAll.setIcon(new ImageIcon(getClass().getResource(
        URL_IMAGES + "closeall.png")));
    deskMenu.add(restoreAll);
    deskMenu.addSeparator();
    deskMenu.add(restoreAll);
    deskMenu.addSeparator();
    deskMenu.add(Closeall);
    ActionListener menuListener = new ActionListener() {
        public void actionPerformed(ActionEvent aEvent) {
            if (aEvent.getSource() == restoreAll) deiconifyAll();
            if (aEvent.getSource() == Closeall) closeAll((JDesktopPane) deskMenu.getInvoker());
            if (aEvent.getSource() == restoreAll) iconifyAll((JDesktopPane) deskMenu.getInvoker());
        }
    };
    restoreAll.addActionListener(menuListener);
    restoreAll.addActionListener(menuListener);
    closeAll.addActionListener(menuListener);
}
}