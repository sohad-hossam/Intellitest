	
/* 
 * Tourists.java
 *
 * 1.0
 *
 * 05/26/2007
 *
 *    2007 eTour Project - Copyright by SE @ SA Lab - DMI - University of Salerno
 */
package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Font;
import javax.swing.ImageIcon;
import unisa.gps.etour.util.Data;
import unisa.gps.etour.bean.TouristBean;
import unisa.gps.etour.gui.DeskManager;
import unisa.gps.etour.gui.operatoragency.tables.TouristActivationRenderer;
import unisa.gps.etour.gui.operatoragency.tables.ScrollableTable;
import unisa.gps.etour.gui.operatoragency.tables.TouristsTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class implements the interface for the management of tourists
 * Agency by the Operator.
 *
 * @Version 1.0
 * @See javax.swing.JInternalFrame
 * @Author Mario Gallo
 */
public class TouristsFrame extends JInternalFrame {

    private JPanel contentPane = null;
    private JToolBar touristsToolbar = null;
    private JButton modifyDataBtn = null;
    private JScrollPane touristsTableScrollPane = null;
    private JTable touristsTable = null;
    private JPanel searchPanel = null;
    private JTextField touristSurnameField = null;
    private JPanel helpPanel = null;
    private JTextPane guideTextPane = null;
    private JPanel rightPanel = null;
    private JButton activateBtn;
    private JButton viewProfileBtn;
    private JButton deleteBtn;
    private TouristsTableModel tableModel;
    private JToggleButton inactiveTouristsToggle;
    private JToggleButton activeTouristsToggle;
    private JButton resetBtn;
    private JButton searchBtn;
    protected DeskManager desktopManager;
    protected JDesktopPane JDesktopPane;
    private ArrayList<TouristBean> children;

    /**
     * This is the default constructor.
     */
    public TouristsFrame() {
        super("Tourists");
        setPreferredSize(new Dimension(700, 480));
        frameIcon = new ImageIcon(getClass().getResource(
                "/unisa/gps/etour/gui/operatoragency/images/Tourists.png"));
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        setClosable(true);
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);
        setContentPane(getContentPane());
        children = new ArrayList<>();
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameOpened(InternalFrameEvent event) {
                JDesktopPane event.getInternalFrame().getDesktopPane();
                desktopManager = (DeskManager) jDesktopPane.getDesktopManager();
            }

            public void internalFrameClosing(InternalFrameEvent event) {
                JPanel root = new JPanel(new BorderLayout());
                JLabel message = new JLabel(
                        "Are you sure you want to close the management of tourists?");
                message.setFont(new Font("Dialog", Font.BOLD, 14));
                JLabel alert = new JLabel(
                        "NB will be closed all the windows opened by this administration."
                        SwingConstants.CENTER);
                alert.setIcon(new ImageIcon(
                        getClass().getResource(
                                "/unisa/gps/etour/gui/operatoragency/images/warning16.png")));
                root.add(message, BorderLayout.NORTH);
                root.add(alert, BorderLayout.CENTER);
                String[] options = {"Close", "Cancel"};
                int choice = JOptionPane.showInternalOptionDialog(getContentPane(), root,
                        "Confirm closure Tourists", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
                if (choice == JOptionPane.OK_OPTION) {
                    for (int i = 0; i < children.size(); i++) {
                        children.get(i).dispose();
                    }
                    event.getInternalFrame().dispose();
                }
            }
        });
    }

    /**
     * This method initializes the internal frame's content pane.
     *
     * @return javax.swing.JPanel - the content pane.
     */
    private JPanel getContentPane() {
        if (contentPane == null) {
            contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(getTouristsToolbar(), BorderLayout.NORTH);
            contentPane.add(getRightPanel(), BorderLayout.EAST);
            contentPane.add(getTouristsTableScrollPane(), BorderLayout.CENTER);
        }
        return contentPane;
    }
/**
 * This method initializes the toolbar on the management of
 * Tourists.
 *
 * @return javax.swing.JToolBar - the toolbar.
 */
private JToolBar getTouristsToolbar() {
    if (null == touristsToolbar) {
        touristsToolbar = new JToolBar();
        touristsToolbar.setLayout(null);
        touristsToolbar.setPreferredSize(new Dimension(1, 50));
        touristsToolbar.setOrientation(JToolBar.HORIZONTAL);
        touristsToolbar.setFloatable(false);
        touristsToolbar.add(getActivateBtn());
        touristsToolbar.add(getModifyDataBtn());
        touristsToolbar.add(getViewProfileBtn());
        touristsToolbar.add(getDeleteBtn());
    }
    return touristsToolbar;
}

/**
 * This method initializes the button to activate
 * A tourist.
 *
 * @return javax.swing.JButton - the button.
 */
private JButton getActivateBtn() {
    if (null == activateBtn) {
        activateBtn = new JButton();
        activateBtn.setText("Enable <html> <br> region </html>");
        activateBtn.setBounds(5, 5, 140, 40);
        activateBtn.setEnabled(false);
        activateBtn.setIcon(new ImageIcon(getClass().getResource(
                "/unisa/gps/etour/gui/operatoragency/images/ActivateTourist32.png")));

        activateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int selected = touristsTable.getSelectedRow();
                String[] fields = {"Name", "Surname", "Date of Birth",
                        "Place of Birth", "E-Mail", "Phone",
                        "Address", "Postal Code", "Province", "City"};
                int[] modelReference = {1, 2, 5, 6, 3, 4, 7, 9, 10, 8};
                JPanel touristDataPanel = new JPanel(new GridBagLayout());
                JPanel rootDialog = new JPanel(new GridBagLayout());
                touristDataPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(51, 102, 255), 2),
                        "Tourist Information",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, new Font("Dialog",
                        Font.BOLD, 12), new Color(0, 102, 204)));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridx = 0;
                gbc.gridy = 0;
                for (int i = 0; i < fields.length; i++) {
                    touristDataPanel.add(new JLabel(fields[i]), gbc);
                    gbc.gridx++;
                    if (i == 2) { // Date of Birth
                        Date dob = (Date) tableModel.getValueAt(selected, 5);
                        touristDataPanel.add(new JLabel(
                                Data.toCompact(dob)), gbc);
                    } else {
                        touristDataPanel.add(new JLabel(
                                tableModel.getValueAt(selected, modelReference[i]).toString()), gbc);
                    }
                    gbc.gridx--;
                    gbc.gridy++;
                }
                gbc.gridx = 0;
                gbc.gridy = 0;
                rootDialog.add(touristDataPanel, gbc);
                gbc.gridy++;
                JLabel txtActivate = new JLabel();
                rootDialog.add(txtActivate, gbc);
                String[] options = new String[2];
                ImageIcon iconDialog;
                options[1] = "Cancel";
                String title;
                boolean enabled = (Boolean) tableModel.getValueAt(selected, 0);
                if (enabled) { // The process of deactivation
                    title = "Turn off the tourist "
                            + tableModel.getValueAt(selected, 1) + " "
                            + tableModel.getValueAt(selected, 2) + "?";
                    options[0] = "Disable";
                    txtActivate.setText("Turn off the selected tourist?");
                    iconDialog = new ImageIcon(
                            getClass().getResource(
                                    "/unisa/gps/etour/gui/operatoragency/images/DeactivateTourist48.png"));
                } else { // The process of activation
                    title = "Turn on the tourist "
                            + tableModel.getValueAt(selected, 1) + " "
                            + tableModel.getValueAt(selected, 2) + "?";
                    options[0] = "Enable";
                    txtActivate.setText("Activate the selected tourist?");
                    iconDialog = new ImageIcon(
                            getClass().getResource(
                                    "/unisa/gps/etour/gui/operatoragency/images/ActivateTourist48.png"));
                }
                txtActivate.setForeground(Color.red);
                int choice = JOptionPane.showInternalOptionDialog(
                        getContentPane(), rootDialog, title,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, iconDialog, options,
                        options[1]);
                if (choice == JOptionPane.OK_OPTION) {
                    tableModel.setValueAt((enabled) ? false : true,
                            selected, 0);
                    tableModel.fireTableDataChanged();
                }
            }
        });
    }
    return activateBtn;
}

/**
 * This method initializes the button for changing
 * Data of a tourist.
 *
 * @return javax.swing.JButton - the button.
 */
private JButton getModifyDataBtn() {
    if (null == modifyDataBtn) {
        modifyDataBtn = new JButton();
        modifyDataBtn.setText("Edit Data <html> <br> region </html>");
        modifyDataBtn.setBounds(155, 5, 140, 40);
        modifyDataBtn.setEnabled(false);
        modifyDataBtn.setIcon(new ImageIcon(getClass().getResource(
                "/unisa/gps/etour/gui/operatoragency/images/EditTourist32.png")));
        modifyDataBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // TEST
                Date newDate = new Date();
                newDate.setMonth(12);
                newDate.setDate(30);
                newDate.setYear(82);
                Date newDate2 = new Date();
                newDate2.setMonth(4);
                newDate2.setDate(30);
                newDate2.setYear(107);
                TouristBean test = new TouristBean(0, "mlmlml", "Ottabio",
                        "of Michil", "Ottawa", "Frattamaggiore", "61616161",
                        "84932nd", "V.le della Mimosa 33", "NA",
                        "ottavio_ottawa@wawa.com", "micacae", newDate2, false);
                // TEST
                // OpenOffice.org Messenger MSN Gaim (TEST, true);
            }
        });
    }
    return modifyDataBtn;
}

/**
 * This method initializes the button to display the
 * Board a tourist.
 *
 * @return javax.swing.JButton - the button.
 */
private JButton getViewProfileBtn() {
    if (null == viewProfileBtn) {
        viewProfileBtn = new JButton();
        viewProfileBtn.setText("Show <html> <br> tourist profile </html>");
        viewProfileBtn.setBounds(305, 5, 140, 40);
        viewProfileBtn.setIcon(new ImageIcon(getClass().getResource(
                "/unisa/gps/etour/gui/operatoragency/images/ProfileTourist.png")));
        viewProfileBtn.setEnabled(false);
        viewProfileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // TEST
                Date newDate = new Date();
                newDate.setMonth(12);
                newDate.setDate(30);
                newDate.setYear(82);
                Date newDate2 = new Date();
                newDate2.setMonth(4);
                newDate2.setDate(30);
                newDate2.setYear(107);
                TouristBean test = new TouristBean(0, "mlmlml", "Ottabio",
                        "of Michil", "Ottawa", "Frattamaggiore", "61616161",
                        "84932nd", "V.le della Mimosa 33", "NA",
                        "ottavio_ottawa@wawa.com", "micacae", newDate2, false);
                // TEST
                // OpenOffice.org Messenger MSN Gaim (TEST, false);
            }
        });
    }
    return viewProfileBtn;
}

/**
 * This method initializes the delete button for a tourist.
 *
 * @return javax.swing.JButton - the button.
 */
private JButton getDeleteBtn() {
    if (null == deleteBtn) {
        deleteBtn = new JButton();
        deleteBtn.setText("Delete <html> <br> region </html>");
        deleteBtn.setBounds(455, 5, 140, 40);
        deleteBtn.setEnabled(false);
        deleteBtn.setIcon(new ImageIcon(getClass().getResource(
                "/unisa/gps/etour/gui/operatoragency/images/DeleteTourist32.png")));
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int selectedRow = touristsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String name = (String) tableModel.getValueAt(
                            selectedRow, 1)
                            + " "
                            + (String) tableModel
                                    .getValueAt(selectedRow, 2);
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel(
                            "Are you sure you want to delete the account of the tourist "
                                    + name + "?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel(
                            "The data account and all personal settings "
                                    + "Can not be filled again."
                                    SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(
                            getClass().getResource(
                                    "/unisa/gps/etour/gui/operatoragency/images/warning16.png")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = {"Delete", "Cancel"};
                    int choice = JOptionPane
                            .showInternalOptionDialog(
                                    getContentPane(),
                                    root,
                                    "Confirm Delete",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    new ImageIcon(
                                            getClass().getResource(
                                                    "/unisa/gps/etour/gui/operatoragency/images/DeleteTourist48.png")),
                                    options, options[1]);
                    if (choice == JOptionPane.YES_OPTION) {
                        tableModel.removeTourist(selectedRow);
                        tableModel.fireTableDataChanged();
                        JLabel confirm = new JLabel("Account Tourist "
                                + name + " was deleted.");
                        confirm.setFont(new Font("Dialog", Font.BOLD, 14));
                        JOptionPane.showInternalMessageDialog(
                                getContentPane(),
                                confirm,
                                "Account Deleted!",
                                JOptionPane.OK_OPTION,
                                new ImageIcon(
                                        getClass().getResource(
                                                "/unisa/gps/etour/gui/operatoragency/images/ok32.png")));
                    }
                }
            }
        });
    }
    return deleteBtn;
}

/**
 * This method initializes the scroll with the table of tourists.
 *
 * @return javax.swing.JScrollPane - the scroll pane.
 */
private JScrollPane getTouristsTableScrollPane() {
    if (null == touristsTableScrollPane) {
        touristsTableModel = new TouristsTableModel();
        // TEST
        Date newDate = new Date();
        newDate.setMonth(12);
        newDate.setDate(30);
        newDate.setYear(82);
        Date newDate2 = new Date();
        newDate2.setMonth(4);
        newDate2.setDate(30);
        newDate2.setYear(107);
        TouristBean newTourist1 = new TouristBean(0, "mlmlml", "Ottabio",
                "of Michil", "Ottawa", "Frattamaggiore", "61616161",
                "84932nd", "V.le della Mimosa 33", "NA",
                "ottavio_ottawa@wawa.com", "micacae", newDate2, false);
        TouristBean newTourist2 = new TouristBean(1, "mlmlml", "Ottabiolino",
                "of Michil", "Ottawa", "Frattamaggiore", "61616161",
                "84932nd", "V.le della Mimosa 33", "NA",
                "ottavio_ottawa@wawa.com", "micacae", newDate2, true);
        TouristBean newTourist3 = new TouristBean(2, "mlmlml", "Ottavio",
                "Michil", "Ottawa", "Frattamaggiore", "61616161", "84932nd",
                "V.le Mimose 33", "NA", "ottavio_ottawa@wawa.com",
                "micacae", newDate2, false);
        touristsTableModel.insertTourist(newTourist1);
        touristsTableModel.insertTourist(newTourist2);
        touristsTableModel.insertTourist(newTourist3);
        for (int i = 0; i < 12; i++) {
            touristsTableModel.insertTourist(newTourist1);
        }
        // END TEST
        touristsTable = new ScrollableTable(touristsTableModel);
        touristsTable.setAutoCreateColumnsFromModel(true);
        touristsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        touristsTable.setSelectionForeground(Color.RED);
        touristsTable.setSelectionBackground(Color.WHITE);
        touristsTable.setColumnSelectionAllowed(false);
        touristsTable.setShowVerticalLines(false);
        touristsTable.setRowHeight(32);
        // Status
        touristsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        touristsTable.getColumnModel().getColumn(0).setCellRenderer(
                new TouristActivationRenderer());
        // Name
        touristsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        // Surname
        touristsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        // Email
        touristsTable.getColumnModel().getColumn(3).setPreferredWidth(140);
        // Phone
        touristsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        // Date of Birth
        touristsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        // City of Birth
        touristsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        // Address
        touristsTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        // Residence
        touristsTable.getColumnModel().getColumn(8).setPreferredWidth(80);
        // Postal Code
        touristsTable.getColumnModel().getColumn(9).setPreferredWidth(60);
        // State
        touristsTable.getColumnModel().getColumn(10).setPreferredWidth(30);
        // Data entry
        touristsTable.getColumnModel().getColumn(11).setPreferredWidth(90);

        ListSelectionModel selectionModel = touristsTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = touristsTable.getSelectedRow();
                if (selectedRow != -1) {
                    activateBtn.setEnabled(true);
                    modifyDataBtn.setEnabled(true);
                    viewProfileBtn.setEnabled(true);
                    deleteBtn.setEnabled(true);
                    if ((Boolean) touristsTableModel.getValueAt(selectedRow, 0)) {
                        activateBtn.setText("Enable <html> <br> region </html>");
                        activateBtn.setIcon(new ImageIcon(getClass().getResource(
                                "/unisa/gps/etour/gui/operatoragency/images/ActivateTourist32.png")));
                    } else {
                        activateBtn.setText("Disable <html> <br> region </html>");
                        activateBtn.setIcon(new ImageIcon(getClass().getResource(
                                "/unisa/gps/etour/gui/operatoragency/images/DeactivateTourist32.png")));
                    }
                } else {
                    deleteBtn.setEnabled(false);
                    modifyDataBtn.setEnabled(false);
                    activateBtn.setEnabled(false);
                    viewProfileBtn.setEnabled(false);
                }
            }
        });

        touristsTableScrollPane = new JScrollPane();
        touristsTableScrollPane.setViewportView(touristsTable);
        touristsTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        touristsTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return touristsTableScrollPane;
}

/**
 * This method initializes the form for searching tourists.
 *
 * @return javax.swing.JPanel - the search panel.
 */
private JPanel getSearchPanel() {
    if (null == searchPanel) {
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Search Tourists", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Dialog",
                        Font.BOLD, 12), new Color(0, 102, 204)));
        GridBagConstraints gbc = new GridBagConstraints();
        // Create Panel for selecting tourists
        JPanel touristsPanel = new JPanel(new GridBagLayout());
        touristsPanel.setPreferredSize(new Dimension(200, 120));
        touristsPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        JToggleButton activatedTouristsToggleBtn = new JToggleButton("Active Tourists");
        activatedTouristsToggleBtn.setPreferredSize(new Dimension(165, 30));
        JToggleButton deactivatedTouristsToggleBtn = new JToggleButton("Inactive Tourists");
        deactivatedTouristsToggleBtn.setPreferredSize(new Dimension(165, 30));
        activatedTouristsToggleBtn.setIcon(new ImageIcon(getClass().getResource(
                "/unisa/gps/etour/gui/operatoragency/images/touristprofile24.png")));
        ActionListener atLeastOneSelected = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (event.getSource() == activatedTouristsToggleBtn) {
                    if (!activatedTouristsToggleBtn.isSelected() && !deactivatedTouristsToggleBtn.isSelected()) {
                        deactivatedTouristsToggleBtn.setSelected(true);
                    }
                } else {
                    if (!activatedTouristsToggleBtn.isSelected() && !deactivatedTouristsToggleBtn.isSelected()) {
                        activatedTouristsToggleBtn.setSelected(true);
                    }
                }
            }
        };
        activatedTouristsToggleBtn.addActionListener(atLeastOneSelected);
        deactivatedTouristsToggleBtn.addActionListener(atLeastOneSelected);
        activatedTouristsToggleBtn.setSelected(true);
        deactivatedTouristsToggleBtn.setSelected(true);
        deactivatedTouristsToggleBtn.setIcon(new ImageIcon(getClass().getResource(
                "/unisa/gps/etour/gui/operatoragency/images/DeactivateTourist24.png")));
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        touristsPanel.add(activatedTouristsToggleBtn, gbc);
        gbc.gridy++;
        touristsPanel.add(deactivatedTouristsToggleBtn, gbc);

        // Create search panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 5, 5);
        searchPanel.add(new JLabel("Tourist Name"), gbc);
        cognomeTurista = new JTextField(12);
        gbc.gridy++;
        searchPanel.add(cognomeTurista, gbc);
        gbc.gridy++;
        searchPanel.add(new JLabel("View"), gbc);
        gbc.gridy++;
        gbc.weighty = 0.2;
        gbc.insets = new Insets(5, 5, 20, 5);
        searchPanel.add(touristsPanel, gbc);
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1;
        gbc.gridy++;
        searchPanel.add(getSearchButton(), gbc);
        gbc.gridx = 1;
        searchPanel.add(getResetButton(), gbc);
    }
    return searchPanel;
}

/**
 * This method initializes the panel's online help.
 *
 * @return javax.swing.JPanel
 */
private JPanel getPanelHelp() {
    if (null == panelHelp) {
        panelHelp = new JPanel();
        panelHelp.setLayout(new BorderLayout());
        panelHelp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Help", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(0, 102, 204)));
        panelHelp.setPreferredSize(new Dimension(200, 100));
        textHelp = new JTextPane();
        textHelp.setOpaque(false);
        textHelp.setContentType("text/html");
        textHelp.setEditable(false);
        textHelp.setOpaque(false);
        panelHelp.add(textHelp, BorderLayout.CENTER);
    }
    return panelHelp;
}

/**
 * This method initializes the side panel of the interface.
 *
 * @return javax.swing.JPanel
 */
private JPanel getRightPanel() {
    if (null == rightPanel) {
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        rightPanel.add(getPanelHelp(), gbc);
        gbc.weighty = 0.7;
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(getSearchPanel(), gbc);
    }
    return rightPanel;
}

/**
 * This method initializes the search button for tourists.
 *
 * @return javax.swing.JButton
 */
private JButton getSearchButton() {
    if (null == searchButton) {
        searchButton = new JButton();
        searchButton.setText("Search");
        searchButton.setPreferredSize(new Dimension(98, 26));
        searchButton.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/Search16.png")));
    }
    return searchButton;
}

/**
 * This method initializes the button to reset the search form.
 *
 * @return javax.swing.JButton
 */
private JButton getClearButton() {
    if (null == clearButton) {
        clearButton = new JButton();
        clearButton.setText("Clear");
        clearButton.setHorizontalTextPosition(SwingConstants.LEADING);
        clearButton.setPreferredSize(new Dimension(98, 26));
        clearButton.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/Clear16.png")));
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                lastNameTourist.setText("");
                activatedTourists.setSelected(true);
                deactivatedTourists.setSelected(true);
            }
        });
    }
    return clearButton;
}
/**
 * This method opens a tab for the supplied tourist bean input.
 *
 * @param touristBean BeanTourist - the tourist bean
 * @param modify boolean - true if modifying the data, false if adding a new card.
 * @return void
 */
private void openTouristTab(BeanTourist touristBean, boolean modify) {
    for (int i = 0; i < children.size(); i++) {
        TouristTab currentTab = children.get(i);
        if (touristBean.getId() == currentTab.getId()) {
            desktopManager.activateFrame(currentTab);
            return;
        }
    }
    TouristTab newTab = new TouristTab(this, touristBean, modify);
    jDesktopPane.add(newTab, Integer.MAX_VALUE);
    newTab.setVisible(true);
    children.add(newTab);
}

/**
 * Closes the selected tab.
 *
 * @param tab TouristTab - the tab to close.
 * @return void
 */
protected void closeTab(TouristTab tab) {
    children.remove(tab);
    tab.dispose();
}

/**
 * Update the tourists table model with the supplied tourist bean input.
 *
 * @param touristBean BeanTourist - the tourist bean.
 * @return void
 */
protected void updateTableModel(BeanTourist touristBean) {
    tableModel.updateTourist(touristBean);
}
}