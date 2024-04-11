package unisa.gps.etour.gui.operatoreagenzia.tables;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * <b>PRNode</b>
 * <p>This class creates a node in a JTree to store
 * Information for a refreshment.</p>
 *
 * @See javax.swing.JTree;
 * @See javax.swing.tree.DefaultMutableTreeNode;
 * @Version 1.0
 * @Author Mario Gallo
 */
public class PRNode extends DefaultMutableTreeNode {
    private int id;

    public PRNode() {
        super();
    }

    /**
     * Create a node with the name of refreshment and
     * Your id supplied as parameters.
     *
     * @param nome - the name of refreshment.
     * @param id   - the id of refreshment.
     * @throws IllegalArgumentException - if the name provided as input is invalid.
     */
    public PRNode(String nome, int id) throws IllegalArgumentException {
        super(nome);
        if (nome == null || nome.equals("")) {
            throw new IllegalArgumentException("Name of refreshment supplied invalid input.");
        }
        this.id = id;
    }

    /**
     * Returns the id of the point of comfort for which information
     * Are stored in this node.
     *
     * @return int - the id of refreshment.
     */
    public int getID() {
        return id;
    }

    /**
     * Stores the id of the refreshment provided input.
     *
     * @param id - an ID of an eating place.
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Return the name of refreshment.
     *
     * @return String - the name of refreshment.
     */
    public String getName() {
        return (String) super.getUserObject();
    }

    /**
     * Stores the name of the refreshment provided input.
     *
     * @param nome - the name of a refreshment.
     * @throws IllegalArgumentException - if the name provided as input is invalid.
     */
    public void setNome(String nome) throws IllegalArgumentException {
        if (nome == null || nome.equals("")) {
            throw new IllegalArgumentException("Name of refreshment supplied invalid input.");
        }
        super.setUserObject(nome);
    }
}
