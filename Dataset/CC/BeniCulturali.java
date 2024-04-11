	
package unisa.gps.etour.gui.operatoragency;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import unisa.gps.etour.bean.CulturalHeritageBean;
import unisa.gps.etour.bean.TagBean;
import unisa.gps.etour.control.CulturalHeritageManagement.AgencyCulturalHeritageManagement;
import unisa.gps.etour.control.CulturalHeritageManagement.MunicipalityCulturalHeritageManagement;
import unisa.gps.etour.control.TagManagement.MunicipalityTagManagement;
import unisa.gps.etour.gui.DeskManager;
import unisa.gps.etour.gui.HelpManager;
import unisa.gps.etour.gui.operatoragency.tables.AverageRatingRenderer;
import unisa.gps.etour.gui.operatoragency.tables.Point3DRenderer;
import unisa.gps.etour.gui.operatoragency.tables.ScrollableTable;
import unisa.gps.etour.gui.operatoragency.tables.SiteTableModel;
import unisa.gps.etour.util.Point3D;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Class that implements the interface for the management of cultural heritage
 * for Operator Agency.
 *
 * @Author Mario Gallo
 * @Version 0.8
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class CulturalHeritageInternalFrame extends JInternalFrame {
    private JDesktopPane desktopPane;
    private JPanel contentPane = null;
    private JToolBar toolbar = null;
    private JButton btnNewCH = null;
    private JButton btnCHDetails = null;
    private JButton btnDeleteCH = null;
    private JButton btnEditCH = null;
    private JPanel rightPanel = null;
    private JPanel searchPanel = null;
    private JPanel helpPanel = null;
    private JScrollPane scrollPane = null;
    private JTable tableCH = null;
    private TagPanel tagPanel = null;
    private JTextPane helpTextPane = null;
    private JTextField chNameField = null;
    private JButton btnSearch = null;
    private JButton btnReset = null;
    private DeskManager desktopManager;
    private ArrayList<CHDetails> children;
    private SiteTableModel tableModel;
    private HelpManager chHelp;
    private AgencyCulturalHeritageManagement chManagement;
    private MunicipalityTagManagement tags;
    protected MunicipalityCulturalHeritageManagement chSearch;

    /**
     * This is the default constructor.
     */
    public CulturalHeritageInternalFrame() {
        super("Cultural Heritage");
        setPreferredSize(Home.CHILD_SIZE);
        frameIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "CH.png"));
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        closable = true;
        resizable = true;
        iconable = true;
        maximizable = true;

        // Setting up the help manager for cultural heritage.
        helpTextPane = new JTextPane();
        try {
            chHelp = new HelpManager(Home.URL_HELP + "CulturalHeritage.txt", helpTextPane);
        } catch (FileNotFoundException e) {
            helpTextPane.setText("<html><b>Help not available</b></html>");
        }
        setContentPane(getContentPane());
        children = new ArrayList<>();
        addInternalFrameListener(new InternalFrameAdapter() {
            /**
             * Inclusion of the frame on the desktop retrieves desktop pane
             * and desktop manager, and initializes the remote objects
             * for management of cultural heritage.
             */
            public void internalFrameOpened(InternalFrameEvent e) {
                CulturalHeritageInternalFrame frame = e.getInternalFrame();
                JDesktopPane desktopPane = frame.getDesktopPane();
                desktopManager = (DeskManager) desktopPane.getDesktopManager();
            }
        });
    }
// Setting up objects for remote asset management for Cultural Heritage.
try {
    Registry reg = LocateRegistry.getRegistry(Home.HOST);
    chManagement = (AgencyCulturalHeritageManagement) reg.lookup("AgencyCulturalHeritageManagement");
    tags = (MunicipalityTagManagement) reg.lookup("MunicipalityTagManagement");
    chSearch = (MunicipalityCulturalHeritageManagement) reg.lookup("MunicipalityCulturalHeritageManagement");
    
    // Load data.
    loadTable(false);
    loadTags();
} catch (Exception ex) {
    JLabel error = new JLabel(
        "<html><h2>Unable to communicate with the eTour server.</h2>"
        + "<h3><u>The management dialog request is closed.</u></h3>"
        + "<p><b>Possible Causes:</b>"
        + "<ul><li>No network connection.</li>"
        + "<li>Inactive server.</li>"
        + "<li>Server overloaded.</li></ul>"
        + "<p>Please try again later.</p>"
        + "<p>If the error persists, please contact technical support.</p>"
        + "<p>We apologize for the inconvenience.</html>"
    );
    ImageIcon errIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
    JOptionPane.showMessageDialog(desktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE, errIcon);
    dispose();
}

// At the end of the frame, display the confirmation dialog.
public void internalFrameClosing(InternalFrameEvent e) {
    // Create the confirmation dialog.
    JPanel root = new JPanel(new BorderLayout());
    JLabel message = new JLabel("Are you sure you want to close the Cultural Heritage management?");
    message.setFont(new Font("Dialog", Font.BOLD, 14));
    JLabel alert = new JLabel("NB: All windows opened by this management will be closed.", SwingConstants.CENTER);
    alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png")));
    root.add(message, BorderLayout.NORTH);
    root.add(alert, BorderLayout.CENTER);
    String[] options = {"Close", "Cancel"};
    int choice = JOptionPane.showInternalOptionDialog(contentPane, root, "Confirm Closing of Cultural Heritage",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);

    // If closing of management is confirmed, close all "child" classes.
    if (choice == JOptionPane.OK_OPTION) {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).dispose();
        }
        e.getInternalFrame().dispose();
    }
}

// Update the current model of the cultural property table with the provided cultural bean.
protected void updateTableModel(CulturalHeritageBean chBean) {
    tableModel.updateCH(chBean);
}

// Closes the selected cultural tab.
protected void closeCHDetails(CHDetails chDetails) {
    children.remove(chDetails);
    chDetails.dispose();
}

/**
 * This method initializes the content pane of the frame.
 *
 * @return javax.swing.JPanel - the content pane.
 */
private JPanel getContentPane() {
    if (null == contentPane) {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(getBCToolbar(), BorderLayout.NORTH);
        contentPane.add(getRightPanel(), BorderLayout.EAST);
        contentPane.add(getScrollPane(), BorderLayout.CENTER);
    }
    return contentPane;
}

/**
 * This method initializes the toolbar for managing cultural heritage assets.
 *
 * @return javax.swing.JToolBar - the toolbar for managing cultural assets.
 */
private JToolBar getBCToolbar() {
    if (null == toolbar) {
        toolbar = new JToolBar();
        toolbar.setPreferredSize(new Dimension(1, 50));
        toolbar.setFloatable(false);
        toolbar.setOrientation(JToolBar.HORIZONTAL);
        toolbar.setLayout(null);
        toolbar.add(getBtnNewCH());
        toolbar.addSeparator();
        toolbar.add(getBtnEditCH());
        toolbar.addSeparator();
        toolbar.add(getBtnCHDetails());
        toolbar.addSeparator();
        toolbar.add(getBtnDeleteCH());
    }
    return toolbar;
}

/**
 * This method initializes the button to insert a new cultural heritage.
 *
 * @return javax.swing.JButton - the button for insertion.
 */
private JButton getBtnNewCH() {
    if (null == btnNewCH) {
        btnNewCH = new JButton();
        btnNewCH.setText("<html>New<br>Cultural Heritage</html>");
        btnNewCH.setBounds(5, 5, 140, 40);
        btnNewCH.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "newCH.png")));
        btnNewCH.setName("btnNewCH");
        btnNewCH.addMouseListener(chHelp);
        btnNewCH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNewCH.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Opens a form for entering a new cultural heritage.
                openForm(null, false);
            }
        });
    }
    return btnNewCH;
}

/**
 * This method initializes the button to access the details of the selected cultural heritage.
 *
 * @return javax.swing.JButton - the button for the card.
 */
private JButton getBtnCHDetails() {
    if (null == btnCHDetails) {
        btnCHDetails = new JButton();
        btnCHDetails.setBounds(305, 5, 140, 40);
        btnCHDetails.setText("<html>Cultural Heritage<br>Details</html>");
        btnCHDetails.setVerticalTextPosition(SwingConstants.TOP);
        btnCHDetails.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "details.png")));
        btnCHDetails.setEnabled(false);
        btnCHDetails.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCHDetails.setName("btnCHDetails");
        btnCHDetails.addMouseListener(chHelp);
        btnCHDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCH.getSelectedRow();
                CulturalHeritageBean chBean = null;
                try {
                    chBean = chManagement.getCulturalHeritage(TableModel.getID(selectedRow));
                    // Open the details with the cultural heritage data.
                    openDetails(chBean, false);
                } catch (Exception ex) {
                    JLabel error = new JLabel("<html><h2>Unable to communicate with the eTour server.</h2>"
                            + "<h3><u>The details request could not be loaded.</u></h3>"
                            + "<p>Please try again later.</p>"
                            + "<p>If the error persists, please contact technical support.</p>"
                            + "<p>We apologize for the inconvenience.</html>");
                    ImageIcon errIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(desktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE, errIcon);
                }
            }
        });
    }
    return btnCHDetails;
}

/**
 * This method initializes the button to access the modification of a Cultural Heritage.
 *
 * @return javax.swing.JButton - the button for the card.
 */
private JButton getBtnEditCH() {
    if (null == btnEditCH) {
        btnEditCH = new JButton();
        btnEditCH.setBounds(155, 5, 140, 40);
        btnEditCH.setText("Edit Cultural Heritage Data");
        btnEditCH.setEnabled(false);
        btnEditCH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditCH.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "editCH32.png")));
        btnEditCH.setName("btnEditCH");
        btnEditCH.addMouseListener(chHelp);
        btnEditCH.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCH.getSelectedRow();
                CulturalHeritageBean chBean = null;
                try {
                    chBean = chManagement.getCulturalHeritage(TableModel.getID(selectedRow));
                    // Open the details with the cultural heritage data enabled for editing.
                    openDetails(chBean, true);
                } catch (Exception ex) {
                    JLabel error = new JLabel("<html><h2>Unable to communicate with the eTour server.</h2>"
                            + "<h3><u>The details request could not be loaded.</u></h3>"
                            + "<p>Please try again later.</p>"
                            + "<p>If the error persists, please contact technical support.</p>"
                            + "<p>We apologize for the inconvenience.</html>");
                    ImageIcon errIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(desktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE, errIcon);
                }
            }
        });
    }
    return btnEditCH;
}

/**
 * This method initializes the button for the elimination of selected cultural heritage.
 *
 * @return javax.swing.JButton - the delete button.
 */
private JButton getBtnDeleteCH() {
    if (null == btnDeleteCH) {
        btnDeleteCH = new JButton();
        btnDeleteCH.setBounds(455, 5, 140, 40);
        btnDeleteCH.setText("Delete Cultural Heritage");
        btnDeleteCH.setVerticalTextPosition(SwingConstants.TOP);
        btnDeleteCH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDeleteCH.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "deleteCH32.png")));
        btnDeleteCH.setEnabled(false);
        btnDeleteCH.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableCH.getSelectedRow();
                String name = (String) tableModel.getValueAt(selectedRow, 0);

                // Create the delete confirmation dialog.
                JPanel root = new JPanel(new BorderLayout());
                JLabel message = new JLabel("Are you sure you want to delete the cultural heritage " + name + "?");
                message.setFont(new Font("Dialog", Font.BOLD, 14));
                JLabel alert = new JLabel("The deleted data cannot be recovered.", SwingConstants.CENTER);
                alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png")));
                root.add(message, BorderLayout.NORTH);
                root.add(alert, BorderLayout.CENTER);
                String[] options = {"Delete", "Cancel"};
                int choice = JOptionPane.showInternalOptionDialog(contentPane, root, "Confirm Delete",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon(getClass().getResource(Home.URL_IMAGES + "deleteCH48.png")),
                        options, options[1]);

                // If the deletion is confirmed, remove the selected cultural heritage.
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        chManagement.deleteCulturalHeritage(TableModel.getID(selectedRow));
                        tableModel.removeCulturalHeritage(selectedRow);
                        JLabel confirm = new JLabel("The cultural heritage " + name + " was deleted.");
                        confirm.setFont(new Font("Dialog", Font.BOLD, 14));
                        JOptionPane.showInternalMessageDialog(contentPane, confirm, "Cultural Heritage",
                                JOptionPane.OK_OPTION, new ImageIcon(getClass().getResource(Home.URL_IMAGES + "ok32.png")));
                    } catch (Exception ex) {
                        JLabel error = new JLabel("<html><h2>Unable to communicate with the eTour server.</h2>"
                                + "<h3><u>Delete operation request could not be completed.</u></h3>"
                                + "<p>Please try again later.</p>"
                                + "<p>If the error persists, please contact technical support.</p>"
                                + "<p>We apologize for the inconvenience.</html>");
                        ImageIcon errIcon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                        JOptionPane.showMessageDialog(desktopPane, error, "Error!", JOptionPane.ERROR_MESSAGE, errIcon);
                    }
                }
            }
        });
        btnDeleteCH.setName("btnDeleteCH");
        btnDeleteCH.addMouseListener(chHelp);
    }
    return btnDeleteCH;
}

/**
 * This method initializes the right side of the interface.
 *
 * @return javax.swing.JPanel - the right pane of the interface.
 */
private JPanel getRightPanel() {
    if (null == rightPanel) {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(getHelpPanel(), BorderLayout.CENTER);
        rightPanel.add(getSearchPanel(), BorderLayout.CENTER);
    }
    return rightPanel;
}

/**
 * This method initializes the panel for finding cultural heritage.
 *
 * @return javax.swing.JPanel - the search panel.
 */
private JPanel getSearchPanel() {
    if (null == searchPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Search for Cultural Heritage",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Dialog", Font.BOLD, 12),
                new Color(0, 102, 204)));
        gbc.anchor = GridBagConstraints.CENTER;
        // Top - Left - Bottom - Right
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Name of Cultural Heritage"), gbc);
        gbc.gridy = 1;
        nomeBC = new JTextField();
        nomeBC.setColumns(12);
        nomeBC.setName("nomeBC");
        nomeBC.addMouseListener(bcHelp);
        searchPanel.add(nomeBC, gbc);
        gbc.gridy = 2;
        searchPanel.add(new JLabel("Select search tags:"), gbc);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 10, 5);
        tagPanel = new TagPanel();
        tagPanel.setPreferredSize(new Dimension(180, 40));
        tagPanel.setName("tagPanel");
        tagPanel.addMouseListener(bcHelp);
        searchPanel.add(tagPanel, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(getBtnSearch(), gbc);
        gbc.gridx = 1;
        searchPanel.add(getBtnReset(), gbc);
    }
    return searchPanel;
}

/**
 * This method initializes the panel containing the online help.
 *
 * @return javax.swing.JPanel - the panel of the guide.
 */
private JPanel getHelpPanel() {
    if (null == helpPanel) {
        helpPanel = new JPanel();
        helpPanel.setLayout(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Help",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Dialog", Font.BOLD, 12),
                new Color(0, 102, 204)));
        helpText.setPreferredSize(new Dimension(100, 80));
        helpText.setContentType("text/html");
        helpText.setText("<html>Move your mouse pointer over a control "
                + "of interest to display the context-sensitive help.</html>");
        helpText.setEditable(false);
        helpText.setOpaque(false);
        helpPanel.add(helpText, BorderLayout.CENTER);
    }
    return helpPanel;
}

/**
 * This method initializes the scroll pane for cultural heritage table.
 *
 * @return javax.swing.JScrollPane - the scrollPane.
 */
private JScrollPane getJScrollPane() {
    if (null == jScrollPane) {
        if (null == tableBC) {
            siteTableModel = new TableModel();
            tableBC = new ScrollableTable(siteTableModel);
            tableBC.setRowHeight(32);
            tableBC.setDefaultRenderer(Double.class, new MediaVotesRenderer());
            tableBC.setDefaultRenderer(Point3D.class, new Point3DRenderer());
            tableBC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableBC.setSelectionForeground(Color.RED);
            tableBC.setSelectionBackground(Color.WHITE);
            tableBC.setShowVerticalLines(false);
            tableBC.setColumnSelectionAllowed(false);
            tableBC.addMouseListener(bcHelp);
            tableBC.setName("tableBC");

            // SelectionListener - if a row is selected, the buttons Tab, edit, and delete are active. Otherwise, they are disabled.
            ListSelectionModel selectionModel = tableBC.getSelectionModel();
            selectionModel.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    if (tableBC.getSelectedRow() != -1) {
                        btnDeleteBC.setEnabled(true);
                        btnDetailsBC.setEnabled(true);
                        btnEditBC.setEnabled(true);
                    } else {
                        btnDeleteBC.setEnabled(false);
                        btnDetailsBC.setEnabled(false);
                        btnEditBC.setEnabled(false);
                    }
                }
            });

            // KeyListener <ENTER> - Details of the selected cultural. <Backspace> - Delete the selected cultural. <space> - Modify the selected cultural.
            tableBC.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent event) {
                    int keyCode = event.getKeyCode();
                    if (keyCode == KeyEvent.VK_ENTER) {
                        btnDetailsBC.doClick();
                    } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
                        btnDeleteBC.doClick();
                    } else if (keyCode == KeyEvent.VK_SPACE) {
                        btnEditBC.doClick();
                    }
                }
            });
        }

        jScrollPane = new JScrollPane(tableBC);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return jScrollPane;
}

/**
 * This method initializes the button to search for cultural heritage.
 *
 * @return javax.swing.JButton - the search button.
 */
private JButton getBtnSearch() {
    if (null == btnSearch) {
        btnSearch = new JButton();
        btnSearch.setText("Search");
        btnSearch.setPreferredSize(new Dimension(98, 26));
        btnSearch.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Search16.png")));
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSearch.setName("btnSearch");
        btnSearch.addMouseListener(bcHelp);
    }
    return btnSearch;
}

/**
 * This method initializes the button to clear the cultural heritage search form.
 *
 * @return javax.swing.JButton - the button to reset the form.
 */
private JButton getBtnClear() {
    if (null == btnClear) {
        btnClear = new JButton();
        btnClear.setText("Clear");
        btnClear.setHorizontalTextPosition(SwingConstants.LEADING);
        btnClear.setPreferredSize(new Dimension(98, 26));
        btnClear.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Clear16.png")));
        btnClear.setName("btnClear");
        btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClear.addMouseListener(bcHelp);
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                nameBC.setText("");
                tagPanel.clear();
            }
        });
    }
    return btnClear;
}

/**
 * This method opens a tab of the selected cultural object or opens the
 * window for entering a new cultural object.
 *
 * @param pBC BeanBeneCulturale - the bean of the cultural property to view the details of.
 * @param pModify boolean -
 * <ul>
 * <li><i>True</i> - indicates that you are editing the cultural object's data.
 * <li><i>False</i> - indicates that you are viewing the details of the cultural object.
 * </ul>
 */
private void openCulturalObjectTab(BeanBeneCulturale pBC, boolean pModify) {
    SchedaBC newTab;
    if (pBC == null) {
        newTab = new SchedaBC(this);
    } else {
        for (int i = 0; i < children.size(); i++) {
            SchedaBC current = children.get(i);
            if (pBC.getId() == current.getId()) {
                desktopManager.activateFrame(current);
                return;
            }
        }
        newTab = new SchedaBC(this, pBC, pModify);
    }
    jDesktopPane.add(newTab, Integer.MAX_VALUE);
    desktopManager.centerFrame(newTab);
    newTab.setVisible(true);
    children.add(newTab);
}

/**
 * This method loads the downloaded cultural objects from the server into the table.
 *
 * @param search boolean
 * <ul>
 * <li><i>True</i> - include search parameters.
 * <li><i>False</i> - otherwise.
 * </ul>
 */
private void loadTable(boolean search) {
    ArrayList<BeanBeneCulturale> culturalObjects = null;
    try {
        if (search) {
            // Perform search and obtain cultural objects
        } else {
            culturalObjects = gestioneBC.getAllCulturalObjects();
        }
    } catch (RemoteException e) {
        JLabel error = new JLabel(
                "<html><h2>Unable to communicate with the eTour server.</h2>"
                + "<h3><u>The list of cultural objects could not be loaded.</u></h3>"
                + "<p>Please try again later.</p>"
                + "<p>If the error persists, please contact technical support.</p>"
                + "<p>We apologize for the inconvenience.</html>");
        ImageIcon err = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
        JOptionPane.showInternalMessageDialog(this, error, "Error!", JOptionPane.ERROR_MESSAGE, err);
    } finally {
        siteTableModel = new TableModel(culturalObjects);
        tableBC.setModel(TableModel);
        organizeRow();
    }
}

/**
 * This method loads the tags available in the system and imports them into
 * the tag panel.
 */
private void loadTags() {
    ArrayList<BeanTag> beanTags = null;
    try {
        beanTags = tag.getTags();
        for (BeanTag b : beanTags) {
            tagPanel.insertTag(b);
        }
        tagPanel.repaint();
    } catch (RemoteException e) {
        // If an error occurs, the tag panel remains blank.
    }
}

/**
 * This method sets the preferred width of columns for cultural assets data.
 */
private void organizeRow() {
    // Name
    tableBC.getColumnModel().getColumn(0).setPreferredWidth(120);
    // Address
    tableBC.getColumnModel().getColumn(1).setPreferredWidth(120);
    // Phone
    tableBC.getColumnModel().getColumn(2).setPreferredWidth(80);
    // Location
    tableBC.getColumnModel().getColumn(3).setPreferredWidth(80);
    // City
    tableBC.getColumnModel().getColumn(4).setPreferredWidth(80);
    // ZIP Code
    tableBC.getColumnModel().getColumn(5).setPreferredWidth(50);
    // Test
    tableBC.getColumnModel().getColumn(6).setPreferredWidth(30);
    // RATINGS
    tableBC.getColumnModel().getColumn(7).setPreferredWidth(80);
    // POSGEO
    tableBC.getColumnModel().getColumn(8).setPreferredWidth(120);
}
}