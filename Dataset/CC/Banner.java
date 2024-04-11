package unisa.gps.etour.gui.operatoragency;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import unisa.gps.etour.gui.operatoragency.BannerDialog;
import unisa.gps.etour.gui.operatoragency.Home;
import unisa.gps.etour.gui.operatoragency.TagPanel;
import unisa.gps.etour.gui.operatoragency.tables.BannerNode;
import unisa.gps.etour.gui.operatoragency.tables.PRNode;
import unisa.gps.etour.bean.RestaurantPointBean;
import unisa.gps.etour.bean.BannerBean;
import unisa.gps.etour.bean.TagBean;
import unisa.gps.etour.control.AdvertisementManagement.IAdvertisementAgencyManagement;
import unisa.gps.etour.control.RestaurantManagement.IRestaurantPointAgencyManagement;
import unisa.gps.etour.control.TagManagement.ICommonTagManagement;
import unisa.gps.etour.gui.DeskManager;
import unisa.gps.etour.gui.HelpManager;
import unisa.gps.etour.gui.operatoragency.tables.BannerRenderer;

public class Banner extends JInternalFrame {
    private JPanel contentPane = null;
    private JPanel rightPanel = null;
    private JToolBar bannerToolbar = null;
    private JButton btnInsert = null;
    private JButton btnReplace = null;
    private JButton btnDelete = null;
    private JScrollPane jScrollPane = null;
    private JPanel helpPanel = null;
    private JTextPane textGuide = null;
    private TagPanel panelTag = null;
    private JButton btnSearch = null;
    private JButton btnReset = null;
    private JPanel panelSearch = null;
    private JTextField restaurantName = null;
    private JTree treeBanner = null;
    private JDesktopPane jDesktopPane;
    private HelpManager bannerHelp;
    protected DeskManager desktopManager;
    protected IRestaurantPointAgencyManagement restaurantManagement;
    protected IAdvertisementAgencyManagement bannerManagement;
    protected ICommonTagManagement commonTags;

    /**
     * This is the default constructor.
     */
    public Banner() {
        super("Banner");
        resizable = true;
        closable = true;
        iconable = true;
        maximizable = true;
        setPreferredSize(Home.CHILD_SIZE);
        frameIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "banner2.png"));

        // Setting up the help manager for cultural.
        textGuide = new JTextPane();

        try {
            bannerHelp = new HelpManager(Home.URL_HELP + "Banner.txt", textGuide);
        } catch (FileNotFoundException e) {
            textGuide.setText("<html><b>Help not available</b></html>");
        }

        setContentPane(getContentPane());
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameOpened(InternalFrameEvent pEvent) {
                JInternalFrame frame = pEvent.getInternalFrame();
                JDesktopPane frameDesktopPane = frame.getDesktopPane();
                desktopManager = (DeskManager) frameDesktopPane.getDesktopManager();

                // Setting up of remote objects for the management of cultural heritage.
                try {
                    Registry reg = LocateRegistry.getRegistry(Home.HOST);
                    bannerManagement = (IAdvertisementAgencyManagement) reg.lookup("AdvertisementManagementAgency");
                    commonTags = (ICommonTagManagement) reg.lookup("CommonTagManagement");
                    restaurantManagement = (IRestaurantPointAgencyManagement) reg.lookup("RestaurantManagementAgency");

                    // Load data.
                    createTree();
                    loadTags();
                } catch (Exception ex) {
                    JLabel error = new JLabel("<html><h2>Unable to communicate with the eTour server.</h2>"
                            + "<h3><u>The dialog management request is closed.</u></h3>"
                            + "<p><b>Possible Causes:</b>"
                            + "<ul><li>No connection to the network.</li>"
                            + "<li>Server inactive.</li>"
                            + "<li>Server overloaded.</li></ul>"
                            + "<p>Please try again later.</p>"
                            + "<p>If the error persists, please contact technical support.</p>"
                            + "<p>We apologize for the inconvenience.</html>");
                    ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(jDesktopPane, error, "Error!",
                            JOptionPane.ERROR_MESSAGE, err);
                    frame.dispose();
                }
            }
        });
    }

/**
 * This method initializes the content pane.
 *
 * @return javax.swing.JPanel - the content pane.
 */
private JPanel getJContentPane() {
    if (null == contentPane) {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(getRightPanel(), BorderLayout.EAST);
        contentPane.add(getBannerToolbar(), BorderLayout.NORTH);
        contentPane.add(getTreeBanner(), BorderLayout.CENTER);
    }
    return contentPane;
}

/**
 * This method initializes the toolbar for banner management functions.
 *
 * @return javax.swing.JToolBar - the toolbar.
 */
private JToolBar getBannerToolbar() {
    if (null == bannerToolbar) {
        bannerToolbar = new JToolBar();
        bannerToolbar.setLayout(null);
        bannerToolbar.setPreferredSize(new Dimension(1, 50));
        bannerToolbar.setFloatable(false);
        bannerToolbar.add(getBtnInsert());
        bannerToolbar.add(getBtnReplace());
        bannerToolbar.add(getBtnDelete());
    }
    return bannerToolbar;
}

/**
 * This method initializes the button to insert a banner.
 *
 * @return javax.swing.JButton - the button for insertion.
 */
private JButton getBtnInsert() {
    if (null == btnInsert) {
        btnInsert = new JButton();
        btnInsert.setBounds(5, 5, 140, 40);
        btnInsert.setText("<html><br>Show Banner</html>");
        btnInsert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "NewBanner32.png")));
        btnInsert.setEnabled(false);
        btnInsert.setName("btnInsert");
        btnInsert.addMouseListener(bannerHelp);
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeBanner
                        .getSelectionPath().getLastPathComponent();
                openDialog(selectedNode);
            }
        });
    }
    return btnInsert;
}

/**
 * This method initializes the button for editing a banner.
 *
 * @return javax.swing.JButton - the button for replacement.
 */
private JButton getBtnReplace() {
    if (null == btnReplace) {
        btnReplace = new JButton();
        btnReplace.setBounds(155, 5, 140, 40);
        btnReplace.setText("Replace <html><br>Banner</html>");
        btnReplace.setEnabled(false);
        btnReplace.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ReplaceBanner32.png")));
        btnReplace.setName("btnReplace");
        btnReplace.addMouseListener(bannerHelp);
        btnReplace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeBanner
                        .getSelectionPath().getLastPathComponent();
                openDialog(selectedNode);
            }
        });
    }
    return btnReplace;
}
/**
 * This method initializes the delete button for a banner.
 *
 * @return javax.swing.JButton - the delete button.
 */
private JButton getBtnDelete() {
    if (null == btnDelete) {
        btnDelete = new JButton();
        btnDelete.setBounds(305, 5, 140, 40);
        btnDelete.setText("Delete <html><br>Banner</html>");
        btnDelete.setEnabled(false);
        btnDelete.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteBanner32.png")));
        btnDelete.setName("btnDelete");
        btnDelete.addMouseListener(bannerHelp);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JPanel root = new JPanel(new BorderLayout());
                JLabel message = new JLabel("Are you sure you want to delete the selected banner?");
                message.setFont(new Font("Dialog", Font.BOLD, 14));
                JLabel alert = new JLabel("The banner cannot be recovered.");
                alert.setHorizontalAlignment(SwingConstants.CENTER);
                alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Warning16.png")));
                root.add(message, BorderLayout.NORTH);
                root.add(alert, BorderLayout.CENTER);
                String[] options = { "Delete", "Cancel" };
                int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm Delete",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon(getClass().getResource(Home.URL_IMAGES + "DeleteBanner48.png")), options,
                        options[1]);
                if (choice == JOptionPane.YES_OPTION) {
                    DefaultTreeModel model = (DefaultTreeModel) treeBanner.getModel();
                    model.removeNodeFromParent((DefaultMutableTreeNode) treeBanner.getSelectionPath()
                            .getLastPathComponent());
                    JLabel confirm = new JLabel("The selected banner has been deleted.");
                    confirm.setFont(new Font("Dialog", Font.BOLD, 14));
                    JOptionPane.showInternalMessageDialog(jContentPane, confirm, "Banner Removed",
                            JOptionPane.OK_OPTION, new ImageIcon(
                                    getClass().getResource(Home.URL_IMAGES + "Ok32.png")));
                }
            }
        });
    }
    return btnDelete;
}

/**
 * This method creates the tree starting from the information contained in the two ArrayLists of beans.
 *
 * @param pPR ArrayList<BeanPuntoDiRistoro> - the array of dining places.
 * @param pBanner ArrayList<BeanBanner> - the array of associated banners.
 */
private void createTree() {
    // Create the root
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("eTour");

    // For each refreshment in the ArrayList, call the getBanner method.
    try {
        ArrayList<BeanPuntoDiRistoro> PPR = gestionePuntiRistoro.ottieniPuntiDiRistoro();
        for (int i = 0; i < PPR.size(); i++) {
            BeanPuntoDiRistoro current = PPR.get(i);
            int id = current.getId();
            PRNode puntoDiRistoro = new PRNode(current.getNome(), id);
            HashMap<BeanBanner, ImageIcon> banners = gestioneBanner.getBannerByID(id);
            Iterator<BeanBanner> bannerIterator = banners.keySet().iterator();
            while (bannerIterator.hasNext()) {
                BeanBanner currentBanner = bannerIterator.next();
                BannerNode newBanner = new BannerNode(banners.get(currentBanner), currentBanner.getId());
                puntoDiRistoro.add(newBanner);
            }
            root.add(puntoDiRistoro);
        }
    } catch (RemoteException ex) {
        JLabel error = new JLabel("<html><h2>Unable to communicate with the eTour server.</h2>"
                + "<h3><u>The list of banners was not loaded.</u></h3>"
                + "<p>Please try again later.</p>"
                + "<p>If the error persists, please contact technical support.</p>"
                + "<p>We apologize for the inconvenience.</html>");
        ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Error48.png"));
        JOptionPane.showInternalMessageDialog(this, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
    } finally {
        treeBanner.setModel(new DefaultTreeModel(root));
    }
}

/**
* This method initializes the tree where it displays the banner.
*
* @return javax.swing.JTree
*/
private JScrollPane getTreeBanner() {
    if (null == treeBanner) {
        treeBanner = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("")));
        treeBanner.setScrollsOnExpand(true);
        treeBanner.setAutoscrolls(true);
        treeBanner.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeBanner.setName("treeBanner");
        treeBanner.addMouseListener(bannerHelp);
        treeBanner.setRootVisible(false);
        treeBanner.setCellRenderer(new BannerRenderer());
        treeBanner.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent s) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeBanner.getLastSelectedPathComponent();

                if (node instanceof PRNode) {
                    btnInsert.setEnabled(true);
                    btnDelete.setEnabled(false);
                    btnReplace.setEnabled(false);
                } else if (node instanceof BannerNode) {
                    btnInsert.setEnabled(false);
                    btnReplace.setEnabled(true);
                    btnDelete.setEnabled(true);
                } else {
                    btnInsert.setEnabled(false);
                    btnReplace.setEnabled(false);
                    btnDelete.setEnabled(false);
                }
            }
        });
    }
    JScrollPane scrollPane = new JScrollPane(treeBanner);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    return scrollPane;
}

/**
* This method initializes the right side panel.
*
* @return javax.swing.JPanel - the right panel.
*/
private JPanel getRightPanel() {
    if (null == rightPanel) {
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.7;
        rightPanel.add(getSearchPanel(), gbc);
        gbc.gridy++;
        gbc.weighty = 0.3;
        rightPanel.add(getHelpPanel(), gbc);
    }
    return rightPanel;
}

/**
* This method initializes the panel that contains the online help.
*
* @return javax.swing.JPanel - the panel for the guide.
*/
private JPanel getHelpPanel() {
    if (null == helpPanel) {
        helpPanel = new JPanel();
        helpPanel.setLayout(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Help", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
        helpPanel.setPreferredSize(new Dimension(200, 60));
        textGuide.setPreferredSize(new Dimension(6, 30));
        textGuide.setOpaque(false);
        textGuide.setContentType("text/html");
        textGuide.setText("<html>Move your mouse pointer over a control " +
                "of interest to display the context-sensitive help.</html>");
        textGuide.setEditable(false);
        textGuide.setName("textGuide");
        textGuide.addMouseListener(bannerHelp);
        helpPanel.add(textGuide, BorderLayout.CENTER);
    }
    return helpPanel;
}
/**
* This method initializes the panel for refreshing point detection.
*
* @return javax.swing.JPanel - the panel for searching.
*/
private JPanel getSearchPanel() {
    if (null == panelSearch) {
        panelSearch = new JPanel();
        panelSearch.setLayout(new GridBagLayout());
        panelSearch.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
            "Refreshment Search",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
            new Color(0, 102, 204)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        panelSearch.add(new JLabel("Refreshment Name:"), gbc);
        nomePR = new JTextField();
        nomePR.setName("nomePR");
        nomePR.addMouseListener(bannerHelp);
        nomePR.setColumns(12);
        gbc.insets = new Insets(5, 5, 10, 5);
        gbc.gridy++;
        panelSearch.add(nomePR, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy++;
        panelSearch.add(new JLabel("Select search tags:"), gbc);
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 10, 5);
        gbc.gridy++;
        panelTag = new TagPanel();
        panelTag.setName("panelTag");
        panelTag.addMouseListener(bannerHelp);
        panelTag.setPreferredSize(new Dimension(180, 10));
        gbc.fill = GridBagConstraints.VERTICAL;
        panelSearch.add(panelTag, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weighty = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        panelSearch.add(getSearchButton(), gbc);
        gbc.gridx++;
        panelSearch.add(getResetButton(), gbc);
    }
    return panelSearch;
}

/**
* This method initializes the button for submitting the search form for a refreshment.
*
* @return javax.swing.JButton - the search button.
*/
private JButton getSearchButton() {
    if (null == btnSearch) {
        btnSearch = new JButton();
        btnSearch.setPreferredSize(new Dimension(98, 26));
        btnSearch.setText("Search");
        btnSearch.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Search16.png")));
        btnSearch.setName("btnSearch");
        btnSearch.addMouseListener(bannerHelp);
    }
    return btnSearch;
}

/**
* This method initializes the button to reset the fields of the search form for a refreshment.
*
* @return javax.swing.JButton - the Reset button.
*/
private JButton getResetButton() {
    if (null == btnReset) {
        btnReset = new JButton();
        btnReset.setPreferredSize(new Dimension(98, 26));
        btnReset.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Reset16.png")));
        btnReset.setText("Clear");
        btnReset.setHorizontalTextPosition(SwingConstants.LEADING);
        btnReset.setName("btnReset");
        btnReset.addMouseListener(bannerHelp);
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                panelTag.clear();
                nomePR.setText("");
            }
        });
    }
    return btnReset;
}

/**
* This method opens the dialog box for entering a new banner or editing a selected banner.
*
* @param pSelectedNode DefaultMutableTreeNode - the selected node.
*/
private void openDialog(DefaultMutableTreeNode pSelectedNode) {
    // This class intercepts mouse events and then
    // makes the frame below blocked.
    class ModalAdapter extends InternalFrameAdapter {
        Component glass;

        public ModalAdapter(Component pGlassComponent) {
            this.glass = pGlassComponent;

            MouseInputAdapter adapter = new MouseInputAdapter() {};
            pGlassComponent.addMouseListener(adapter);
            pGlassComponent.addMouseMotionListener(adapter);
        }

        public void internalFrameClosed(InternalFrameEvent s) {
            glass.setVisible(false);
        }
    }

    // Building the dialog
    JOptionPane optionPane = new JOptionPane();
    final JInternalFrame modal = optionPane.createInternalFrame(
        JDesktopPane, "");
    final JPanel glass = new JPanel();
    final BannerDialog dialog = new BannerDialog();
    optionPane.setMessage(dialog);
    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    JButton[] options = new JButton[2];
    options[0] = new JButton();
    options[1] = new JButton("Cancel");
    options[0].setIcon(new ImageIcon(getClass().getResource(
        Home.URL_IMAGES + "Save16.png")));
    options[1].setIcon(new ImageIcon(getClass().getResource(
        Home.URL_IMAGES + "Cancel16.png")));
    optionPane.setOptions(options);
    options[1].addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
            modal.setVisible(false);
            glass.setVisible(false);
        }
    });

    glass.setOpaque(false);
    modal.addInternalFrameListener(new ModalAdapter(glass));
    glass.add(modal);
    setGlassPane(glass);
    modal.setLocation(this.getWidth() / 2, this.getHeight() / 2);
    glass.setVisible(true);
    modal.setVisible(true);
    if (pSelectedNode instanceof BannerNode) // Replace
    {
        final BannerNode banner = (BannerNode) pSelectedNode;
        options[0].setText("Replace");
        options[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultTreeModel model = (DefaultTreeModel) treeBanner.getModel();
                banner.setBanner(dialog.getSelectedBanner());
                model.nodeChanged(banner);
                glass.setVisible(false);
                modal.setVisible(false);
            }
        });

        optionPane.setIcon(new ImageIcon(getClass().getResource(
            Home.URL_IMAGES + "ReplaceBanner48.png")));
        modal.setTitle("Replace the banner for the relief point "
            + ((PRNode) pSelectedNode.getParent()).getUserObject()
                .toString());
    }
    else if (pSelectedNode instanceof PRNode) // Inserting
    {
        final PRNode pr = (PRNode) pSelectedNode;
        options[0].setText("Save");
        options[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                DefaultTreeModel model = (DefaultTreeModel) treeBanner.getModel();
                BannerNode newNode = new BannerNode(dialog.getSelectedBanner(), pr.getID());
                model.insertNodeInto(newNode, pr, 0);
                glass.setVisible(false);
                modal.setVisible(false);
            }
        });
        optionPane.setIcon(new ImageIcon(getClass().getResource(
            Home.URL_IMAGES + "NewBanner48.png")));
        modal.setTitle("Enter banner for the relief point "
            + pSelectedNode.getUserObject().toString());
    }
    }
}