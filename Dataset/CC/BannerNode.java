package unisa.gps.etour.gui.operatoragency.tables;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class creates a node in a JTree to store
 * Information about a banner.
 *
 * @Version 1.0
 * @Author Mario Gallo
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class BannerNode extends DefaultMutableTreeNode {
    private int id;

    /**
     * This is the default constructor.
     */
    public BannerNode() {
        super();
    }

    /**
     * Create a node with the image of the banner and
     * Your id supplied as parameters.
     *
     * @param pBanner ImageIcon - the image of the banner.
     * @param pid int - the id of the banner.
     * @throws IllegalArgumentException - if the image provided as input is invalid.
     */
    public BannerNode(ImageIcon pBanner, int pid) throws IllegalArgumentException {
        super();
        if (null == pBanner) {
            throw new IllegalArgumentException("Image is invalid.");
        }
        setUserObject(pBanner);
        id = pid;
    }

    /**
     * Returns the node type PRNode father.
     *
     * @return PRNode - the parent node.
     */
    public PRNode getParent() {
        return (PRNode) super.getParent();
    }

    /**
     * Returns the id of the banner for which information
     * Are stored in this node.
     *
     * @return int - the id of the banner.
     */
    public int getID() {
        return id;
    }

    /**
     * Stores the id of the banner supplied input.
     *
     * @param pid int - an ID of a banner.
     */
    public void setID(int pid) {
        id = pid;
    }

    /**
     * Returns the banner image.
     *
     * @return ImageIcon - the image.
     */
    public ImageIcon getBanner() {
        return (ImageIcon) getUserObject();
    }

    /**
     * Save the image provided as input.
     *
     * @param pImage ImageIcon - the image to be stored.
     * @throws IllegalArgumentException - if the supplied parameter is null.
     */
    public void setBanner(ImageIcon pImage) throws IllegalArgumentException {
        if (null == pImage) {
            throw new IllegalArgumentException("Image is invalid.");
        }
        setUserObject(pImage);
    }
}
