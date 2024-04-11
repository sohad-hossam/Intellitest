package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import unisa.gps.etour.bean.BeanCulturalAsset;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.util.Point3D;

public class RefreshmentPoints extends JInternalFrame {

    private Dimension SIZE;
    private JPanel jContentPane = null;
    private JPanel RightPanel = null;
    private JToolBar toolbarRP = null;
    private JLabel status = null;
    private JPanel CentrePanel = null;
    private JButton btnView = null;
    private JButton btnEdit = null;
    private JButton btnDelete = null;
    private JScrollPane JScrollPane = null;
    private JPanel HelpPanel = null;
    private JTextPane jTextPane = null;
    private JScrollPane TagPanel = null;
    private JButton btnSearch2 = null;
    private JButton btnClear2 = null;
    private JPanel SearchPanel = null;
    private JLabel LabelRP = null;
    private JTextField nameRP = null;
    private JLabel LabelTag = null;
    private JTable TableRP = null;
    private JDesktopPane JDesktopPane;
    private RefreshmentPoints internalFrame;
    private JButton btnActivate = null;
    private JButton btnHistory = null;

    /**
     * This is the default constructor xxx
     */
    public RefreshmentPoints() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @ Return void
     */
    private void initialize() {
        SIZE = new Dimension(700, 480);
        setPreferredSize(SIZE);
        this.setSize(SIZE);
        this.setResizable(true);
        this.setClosable(true);
        this.setTitle("Refreshments");
        this.setFrameIcon(new ImageIcon(getClass().getResource("/operatoragency/images/RP.png")));
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     *
     * @ Return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getRightPanel(), BorderLayout.EAST);
            jContentPane.add(getToolbarRP(), BorderLayout.NORTH);
            jContentPane.add(getCentrePanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes RightPanel
     *
     * @ Return javax.swing.JPanel
     */
    private JPanel getRightPanel() {
        if (RightPanel == null) {
            RightPanel = new JPanel();
            RightPanel.setLayout(new BorderLayout());
            RightPanel.add(getHelpPanel(), BorderLayout.CENTER);
            RightPanel.add(getSearchPanel(), BorderLayout.CENTER);
        }
        return RightPanel;
    }

    /**
     * This method initializes toolbarRP
     *
     * @ Return javax.swing.JToolBar
     */
    private JToolBar getToolbarRP() {
        if (toolbarRP == null) {
            toolbarRP = new JToolBar();
            toolbarRP.setLayout(null);
            toolbarRP.setPreferredSize(new Dimension(1, 49));
            toolbarRP.setFloatable(false);
            toolbarRP.add(getBtnActivate());
            toolbarRP.addSeparator();
            toolbarRP.add(getBtnHistory());
            toolbarRP.addSeparator();
            toolbarRP.add(getBtnEdit());
            toolbarRP.addSeparator();
            toolbarRP.add(getBtnView());
            toolbarRP.addSeparator();
            toolbarRP.add(getBtnDelete());
        }
        return toolbarRP;
    }

    /**
     * This method initializes CentrePanel
     *
     * @ Return javax.swing.JPanel
     */
    private JPanel getCentrePanel() {
        if (CentrePanel == null) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0;
            gbc.weightx = 1.0;
            CentrePanel = new JPanel();
            CentrePanel.setLayout(new GridBagLayout());
            CentrePanel.setPreferredSize(new Dimension(1, 30));
            CentrePanel.add(getJScrollPane(), gbc);
        }
        return CentrePanel;
    }

    /**
     * This method initializes btnView
     *
     * @ Return javax.swing.JButton
     */
    private JButton getBtnView() {
        if (btnView == null) {
            btnView = new JButton();
            btnView.setText("Point Card <html> <br> Refreshments </ html>");
            btnView.setLocation(new Point(16, 3));
            btnView.setSize(new Dimension(130, 42));
            btnView.setPreferredSize(new Dimension(130, 42));
            btnView.setIcon(new ImageIcon(getClass().getResource("/operatoragency/images/Browse 1.png")));
            btnView.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                }
            });
        }
        return btnView;
    }


/**
 * This method initializes the btnEdit button.
 *
 * @return javax.swing.JButton
 */
private JButton getBtnEdit() {
    if (btnEdit == null) {
        btnEdit = new JButton();
        btnEdit.setText("<html>Edit Point <br> Refreshments</html>");
        btnEdit.setPreferredSize(new Dimension(130, 42));
        btnEdit.setMnemonic(KeyEvent.VK_UNDEFINED);
        btnEdit.setEnabled(false);
        btnEdit.setBounds(new Rectangle(413, 3, 140, 42));
        btnEdit.setIcon(new ImageIcon(getClass().getResource("/operatoragency/images/edit-32x32.png")));
    }
    return btnEdit;
}

/**
 * This method initializes the btnDelete button.
 *
 * @return javax.swing.JButton
 */
private JButton getBtnDelete() {
    if (btnDelete == null) {
        btnDelete = new JButton();
        btnDelete.setText("Delete item <html> <br> Refreshments </html>");
        btnDelete.setPreferredSize(new Dimension(130, 42));
        btnDelete.setEnabled(false);
        btnDelete.setMnemonic(KeyEvent.VK_UNDEFINED);
        btnDelete.setBounds(new Rectangle(555, 3, 130, 42));
        btnDelete.setIcon(new ImageIcon(getClass().getResource("/operatoragency/images/edit-delete-32x32.png")));
    }
    return btnDelete;
}

/**
 * This method initializes JScrollPane.
 *
 * @return javax.swing.JScrollPane
 */
private JScrollPane getJScrollPane() {
    if (scrollPane == null) {
        if (tablePr == null) {
            BeanRefreshmentPoint[] test = new BeanRefreshmentPoint[30];
            for (int i = 0; i < 30; i++) {
                test[i] = new BeanRefreshmentPoint(1, 12, 3.5,
                        "Arturo", "Near the sea, great view, romantic and Miao",
                        "089203202", "the mountains", "Amalfi", "Via Principe 35", "84123",
                        "Salerno", "1234567898741", new Point3D(34, 34, 34),
                        new Date(2, 23, 3), new Date(3, 3, 4), "Monday");
            }

            SiteTableModel newSiteTableModel = new SiteTableModel(test);
            tablePr = new ScrollableTable(newSiteTableModel);

            // Name
            tablePr.getColumnModel().getColumn(0).setPreferredWidth(140);
            // Description
            tablePr.getColumnModel().getColumn(1).setPreferredWidth(80);
            // Location
            tablePr.getColumnModel().getColumn(2).setPreferredWidth(140);
            // City
            tablePr.getColumnModel().getColumn(3).setPreferredWidth(80);
            // State
            tablePr.getColumnModel().getColumn(4).setPreferredWidth(70);
            // Number of Votes
            tablePr.getColumnModel().getColumn(5).setPreferredWidth(70);
            // Average Ratings
            tablePr.getColumnModel().getColumn(6).setPreferredWidth(60);
            // PosGeo
            tablePr.getColumnModel().getColumn(7).setPreferredWidth(30);
            tablePr.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
            tablePr.setRowHeight(32);
            tablePr.setShowVerticalLines(false);
            tablePr.setDefaultRenderer(Double.class, new RatingsMediaRenderer());
            tablePr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            tablePr.setSelectionForeground(Color.RED);
            tablePr.setSelectionBackground(Color.white);
            tablePr.setColumnSelectionAllowed(false);
            final javax.swing.ListSelectionModel thisListSelectionModel = tablePr.getSelectionModel();
            thisListSelectionModel.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    btnDelete.setEnabled(true);
                    btnEdit.setEnabled(true);
                    btnActivate.setEnabled(true);
                    btnHistory.setEnabled(true);
                }
            });

            tablePr.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent arg0) {
                    System.out.println("keyPressed");
                }

                public void keyReleased(KeyEvent arg0) {
                    System.out.println("keyReleased");
                }

                public void keyTyped(KeyEvent arg0) {
                    System.out.println("keyTyped");
                }
            });
        }
        scrollPane = new JScrollPane(tablePr);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return scrollPane;
}

/**
 * This method initializes the HelpPanel component.
 *
 * @return javax.swing.JPanel
 */
private JPanel getHelpPanel() {
    if (helpPanel == null) {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.ipadx = 0;
        gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.ipady = 0;
        gridBagConstraints1.gridx = 0;
        helpPanel = new JPanel();
        helpPanel.setLayout(new GridBagLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 153, 255), 3), "Help", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 102, 255)));
        helpPanel.setPreferredSize(new Dimension(100, 100));
        helpPanel.add(getJTextPane(), gridBagConstraints1);
    }
    return helpPanel;
}

/**
 * This method initializes the jTextPane component.
 *
 * @return javax.swing.JTextPane
 */
private JTextPane getJTextPane() {
    if (jTextPane == null) {
        jTextPane = new JTextPane();
        jTextPane.setPreferredSize(new Dimension(6, 30));
    }
    return jTextPane;
}


/**
 * This method initializes the searchPanel.
 *
 * @return javax.swing.JPanel
 */
private JPanel getSearchPanel() {
    if (searchPanel == null) {
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = GridBagConstraints.BOTH;
        gridBagConstraints5.gridy = 4;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.weighty = 1.0;
        gridBagConstraints5.gridwidth = 2;
        gridBagConstraints5.gridx = 0;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridwidth = 2;
        gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 0;
        gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints7.gridy = 6;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 1;
        gridBagConstraints6.gridwidth = 2;
        gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints6.gridy = 6;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridwidth = 2;
        gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints4.gridy = 3;
        LabelTag = new JLabel();
        LabelTag.setText("Select search tags:");
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 2;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints3.weightx = 1.0;
        LabelPr = new JLabel();
        LabelPr.setText("Name Refreshments:");
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 153, 255), 3), "Searching for refreshments:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 102, 255)));
        searchPanel.add(LabelPr, gridBagConstraints8);
        searchPanel.add(getRefreshmentNameField(), gridBagConstraints3);
        searchPanel.add(LabelTag,gridBagConstraints4);
        BeanTag[] test = new BeanTag[8];
        test[0] = new BeanTag(0, "castle", "really a castle");
        test[1] = new BeanTag(1, "stronghold", "really a hostel");
        test[2] = new BeanTag(3, "Pub", "really a basket");
        test[3] = new BeanTag(4, "Restaurant", "really a basket");
        test[4] = new BeanTag(5, "Pizza", "really a basket");
        test[5] = new BeanTag(6, "Trattoria", "really a basket");
        test[6] = new BeanTag(7, "range", "really a basket");
        test[7] = new BeanTag(8, "Romantic", "really a basket");
        tagPanel = new TagPanel(test);
        tagPanel.setPreferredSize(new Dimension(180, 40));
        searchPanel.add(getBtnClearSearch(), gridBagConstraints6);
        searchPanel.add(getBtnSearch(), gridBagConstraints7);
        searchPanel.add(tagPanel,gridBagConstraints5);
    }
    return searchPanel;
}

/**
 * This method initializes the refreshmentNameField.
 *
 * @return javax.swing.JTextField
 */
private JTextField getRefreshmentNameField() {
    if (refreshmentNameField == null) {
        refreshmentNameField = new JTextField();
        refreshmentNameField.setColumns(12);
    }
    return refreshmentNameField;
}

/**
 * This method initializes the btnActivate button.
 *
 * @return javax.swing.JButton
 */
private JButton getBtnActivate() {
    if (btnActivate == null) {
        btnActivate = new JButton();
        btnActivate.setText("Enable <html> <br> Convention</html>");
        btnActivate.setPreferredSize(new Dimension(130, 42));
        btnActivate.setSize(new Dimension(130, 42));
        btnActivate.setLocation(new Point(280, 3));
        btnActivate.setMnemonic(KeyEvent.VK_UNDEFINED);
        btnActivate.setEnabled(false);
        btnActivate.setIcon(new ImageIcon(getClass().getResource("/interfacceAgenzia/immagini/wi0054-32x32.png")));
    }
    return btnActivate;
}

/**
 * This method initializes the btnHistory button.
 *
 * @return javax.swing.JButton
 */
private JButton getBtnHistory() {
    if (btnHistory == null) {
        btnHistory = new JButton();
        btnHistory.setText("Historical <html> <br> Conventions</html>");
        btnHistory.setPreferredSize(new Dimension(130, 42));
        btnHistory.setBounds(new Rectangle(148, 3, 130, 42));
        btnHistory.setEnabled(false);
        btnHistory.setIcon(new ImageIcon(getClass().getResource("/interfacceAgenzia/images/Browse1.png")));
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                new StoricoConvenzioni();
            }
        });
    }
    return btnHistory;
}
}