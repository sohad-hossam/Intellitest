package unisa.gps.etour.gui.travelagencyoperator;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.*;
import java.util.*;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import javax.swing.border.*;
import unisa.gps.etour.bean.CulturalAssetBean;
import unisa.gps.etour.bean.TagBean;
import unisa.gps.etour.bean.VisitBean;
import unisa.gps.etour.control.CulturalAssetsManagement.ICulturalAssetsManagementAgency;
import unisa.gps.etour.control.TagManagement.ICommonTagManagement;
import unisa.gps.etour.gui.travelagencyoperator.documents.LimitedDocument;
import unisa.gps.etour.gui.travelagencyoperator.documents.NumericDocument;
import unisa.gps.etour.gui.travelagencyoperator.documents.OnlyCharactersDocument;
import unisa.gps.etour.gui.travelagencyoperator.tables.FeedBackTableModel;
import unisa.gps.etour.gui.travelagencyoperator.tables.AverageRatingRenderer;
import unisa.gps.etour.util.Point3D;

/**
 * Class modeling the interface for viewing, modifying data, and inserting a new cultural object.
 *
 * @Version 1.0
 * @Author Mario Gallo
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class CulturalCard extends JInternalFrame implements ICulturalCard {

    private static final String[] texts = {"Name", "Address", "City", "Location", "ZIP Code",
            "Province", "Geographical Location", "Phone",
            "Opening Hours", "Closing Time",
            "Closing Date", "Ticket Price", "Description"};
    private static final String[] helpMessages = {
            "Enter the name of the cultural asset.",
            "Enter the address where the cultural asset is located.",
            "Enter the city where the cultural asset is located.",
            "Enter the location of the cultural object.",
            "Enter the ZIP code of the area where the cultural asset is located.",
            "Select the province where the cultural asset belongs.",
            "Enter three-dimensional coordinates for the location of the cultural asset.",
            "Enter the phone number of the cultural management office.",
            "Select the public opening time of the cultural asset.",
            "Select the public closing time of the cultural asset.",
            "Select the weekly closing day.",
            "Provide the ticket price for admission to the cultural asset.",
            "<html> Enter a comprehensive description for the cultural asset. <br> Please note that this description will be used as a source of keywords <br> in tourist searches.</html>",
            "<html> Select search tags for the cultural asset. <br> Search tags allow tourists to find sites with features of interest.</html>"
    };

    private JPanel contentPane = null;
    private JToolBar toolbarCulturalCard = null;
    private JToggleButton btnModify = null;
    private JButton btnSave = null;
    private JButton btnCancel = null;
    private JButton btnModifyComment = null;
    private JTabbedPane tabbedPane = null;
    private JPanel statistics = null;
    private JPanel feedbackPanel = null;
    private JTextField address2 = null;
    private JComboBox address1 = null;
    private JTextField cityCulturalAsset = null;
    private JTextField locationCulturalAsset = null;
    private JTextField zipCodeCulturalAsset = null;
    private JTextField geoPosX = null;
    private JScrollPane scrollPane = null;
    private JTextArea descriptionCulturalAsset = null;
    private JTextField phoneCulturalAsset = null;
    private JComboBox openingTimeHours = null;
    private JComboBox openingTimeMinutes = null;
    private TagPanel tagPanel;
    private JTextField priceCulturalAsset = null;
    private JComboBox closingTimeHours = null;
    private JComboBox closingTimeMinutes = null;
    private JComboBox provinceCulturalAsset = null;
    private JPanel dataPanel = null;
    private JTextField nameCulturalAsset = null;
    private JScrollPane feedbackScrollPane = null;
    private JTable feedbackTable = null;
    private JLabel txtCulturalAssetName = null;
    private JLabel averageRatingCulturalAsset = null;
    private JPanel currentMonthStatistics = null;
    private JPanel totalStatistics = null;
    private JTextField geoPosY = null;
    private JTextField geoPosZ = null;
    private Vector<JLabel> suggestions;
    private CulturalAssetBean culturalAsset;
    private JComboBox closingDay;
    private JLabel[] currentMonthStats;
    private JLabel[] totalStats;
    private AssetsParent parent;
    private FeedBackTableModel feedbackModel;
    protected ICommonTagManagement tags;
    protected ICulturalAssetsManagementAgency culturalAssetsManagement;
    private ArrayList<Integer> tagIds = null;

    /**
     * Default constructor for including the interface model of a new cultural asset.
     */
    public CulturalCard(AssetsParent pParent) {
        super("New Cultural Asset");
        setFrameIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "newCA.png")));
        setClosable(true);
        setResizable(false);
        setIconifiable(true);
        setSize(600, 560);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        suggestions = new Vector<>();
        parent = pParent;
        culturalAsset = null;

        // Initialize the content pane
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(getCulturalCardToolbar(), BorderLayout.CENTER);
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Cultural Asset Data", new ImageIcon(getClass().getResource(Home.URL_IMAGES + "data.png")), getDataPanel(), null);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(contentPane);

        // Dialog closing to close the entry window.
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                JInternalFrame frame = e.getInternalFrame();
                try {
                    Registry registry = LocateRegistry.getRegistry(Home.HOST);
                    tags = (ICommonTagManagement) registry.lookup("TagManagement");
                    culturalAssetsManagement = (ICulturalAssetsManagementAgency) registry.lookup("CulturalAssetsManagementAgency");
                    loadTags();
                } catch (Exception ex) {
                    JLabel error = new JLabel("<html><h2>Unable to communicate with the eTour server.</h2>"
                            + "<h3><u>The card for entering a new cultural asset will be closed.</u></h3>"
                            + "<p><b>Possible causes:</b>"
                            + "<ul><li>No network connection.</li>"
                            + "<li>Inactive server.</li>"
                            + "<li>Server congested.</li></ul>"
                            + "<p>Please try again later.</p>"
                            + "<p>If the error persists, please contact technical support.</p>"
                            + "<p>We apologize for the inconvenience.</html>");
                    ImageIcon icon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(frame, error, "Error!", JOptionPane.ERROR_MESSAGE, icon);
                    frame.dispose();
                }
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                JPanel root = new JPanel(new BorderLayout());
                JLabel message = new JLabel("Are you sure you want to cancel the creation of a new cultural asset?");
                message.setFont(new Font("Dialog", Font.BOLD, 14));
                JLabel alert = new JLabel("Warning! Unsaved data will be lost.", SwingConstants.CENTER);
                alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png")));
                root.add(message, BorderLayout.NORTH);
                root.add(alert, BorderLayout.CENTER);
                String[] options = {"Close", "Cancel"};
                int choice = JOptionPane.showInternalOptionDialog(contentPane, root, "Confirm Closure",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
                if (choice == JOptionPane.OK_OPTION) {
                    parent.closeCulturalCard((CulturalCard) e.getInternalFrame());
                }
            }
        });

        // Initialize buttons
        btnModify.setVisible(false);
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
        btnCancel.setText("Clear");
    }

    /**
     * Constructor for displaying the form for modifying data and viewing the tab of a cultural asset.
     *
     * @param pParent  - the parent frame
     * @param pCA      - the bean containing the data of the selected cultural asset
     * @param pModify  - true if modifying the cultural asset, false if viewing the tab
     */
    public CulturalCard(AssetsParent pParent, CulturalAssetBean pCA, boolean pModify) {
        super();
        setFrameIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "tab.png")));
        setClosable(true);
        setResizable(false);
        setIconifiable(true);
        setSize(600, 560);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Initialize instance variables
        culturalAsset = pCA;
        parent = pParent;
        suggestions = new Vector<>();
        initializeCulturalCard();

        if (pModify) {
            btnModify.setSelected(true);
            btnSave.setVisible(true);
            btnCancel.setVisible(true);
            btnCancel.setText("Reset");
        } else {
            showHideSuggestions();
            enableDisableEdit();
        }
    }

    /**
     * Returns the id of the cultural asset for which you are viewing the details or modifying data.
     *
     * @return int - the id of the cultural asset
     */
    public int getId() {
        if (culturalAsset == null) {
            return -1;
        }
        return culturalAsset.getId();
    }

    /**
     * Initializes the interface for displaying the tab of a cultural asset.
     */
    private void initializeCulturalCard() {
        setTitle(culturalAsset.getName());

        // Dialog closed frame
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                JInternalFrame frame = e.getInternalFrame();
                try {
                    Registry registry = LocateRegistry.getRegistry(Home.HOST);
                    tags = (ICommonTagManagement) registry.lookup("TagManagement");
                    culturalAssetsManagement = (ICulturalAssetsManagementAgency) registry.lookup("CulturalAssetsManagementAgency");
                    loadTags();
                    loadStatistics();
                } catch (Exception ex) {
                    JLabel error = new JLabel("<html><h2>Unable to communicate with the eTour server.</h2>"
                            + "<h3><u>The cultural asset tab will be closed.</u></h3>"
                            + "<p><b>Possible causes:</b>"
                            + "<ul><li>No network connection.</li>"
                            + "<li>Inactive server.</li>"
                            + "<li>Server congested.</li></ul>"
                            + "<p>Please try again later.</p>"
                            + "<p>If the error persists, please contact technical support.</p>"
                            + "<p>We apologize for the inconvenience.</html>");
                    ImageIcon icon = new ImageIcon(getClass().getResource(Home.URL_IMAGES + "error48.png"));
                    JOptionPane.showMessageDialog(frame, error, "Error!", JOptionPane.ERROR_MESSAGE, icon);
                    frame.dispose();
                }
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (btnModify.isSelected()) {
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Are you sure you want to close the tab of this cultural asset?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("Warning! Unsaved data will be lost.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "warning16.png")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = {"Close", "Cancel"};
                    int choice = JOptionPane.showInternalOptionDialog(contentPane, root,
                            "Confirm Closing Tab for Cultural Asset " + culturalAsset.getName(),
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, frameIcon, options, options[1]);
                    if (choice == JOptionPane.OK_OPTION) {
                        parent.closeCulturalCard((CulturalCard) e.getInternalFrame());
                    }
                } else {
                    parent.closeCulturalCard((CulturalCard) e.getInternalFrame());
                }
            }
        });

        // Initialize the content pane
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(getCulturalCardToolbar(), BorderLayout.CENTER);
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Cultural Asset Data", new ImageIcon(getClass().getResource(Home.URL_IMAGES + "data.png")), getDataPanel(), null);
        JScrollPane scrollPane = new JScrollPane(getStatisticsPanel());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tabbedPane.addTab("Statistics", new ImageIcon(getClass().getResource(Home.URL_IMAGES + "stat24.png")), scrollPane, null);
        tabbedPane.addTab("Feedback Received", new ImageIcon(getClass().getResource(Home.URL_IMAGES + "feedback.png")), getFeedbackPanel(), null);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(contentPane);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    toolbarCulturalCard.setVisible(true);
                    btnModify.setVisible(true);
                    if (btnModify.isSelected()) {
                        btnSave.setVisible(true);
                        btnCancel.setVisible(true);
                    }
                    btnModifyComment.setVisible(false);
                } else if (tabbedPane.getSelectedIndex() == 1) {
                    toolbarCulturalCard.setVisible(false);
                } else {
                    if (btnModify.isSelected()) {
                        btnSave.setVisible(false);
                        btnCancel.setVisible(false);
                    }
                    toolbarCulturalCard.setVisible(true);
                    btnModifyComment.setVisible(true);
                    btnModify.setVisible(false);
                }
            }
        });
    }

    /**
     * Loads the data supplied to the constructor of the cultural asset into the form.
     */
    private void loadDataIntoForm() {
        nameCulturalAsset.setText(culturalAsset.getName());
        zipCodeCulturalAsset.setText(culturalAsset.getZipCode());
        cityCulturalAsset.setText(culturalAsset.getCity());
        priceCulturalAsset.setText("" + culturalAsset.getTicketPrice());
        descriptionCulturalAsset.setText(culturalAsset.getDescription());
        StringTokenizer tokenizer = new StringTokenizer(culturalAsset.getAddress());
        String address = tokenizer.nextToken();
        address1.setSelectedItem(address);
        address2.setText(culturalAsset.getAddress().substring(address.length()));
        provinceCulturalAsset.setSelectedItem(culturalAsset.getProvince());
        Point3D position = culturalAsset.getPosition();
        geoPosX.setText("" + position.getLatitude());
        geoPosY.setText("" + position.getLongitude());
        geoPosZ.setText("" + position.getAltitude());
        phoneCulturalAsset.setText(culturalAsset.getPhoneNumber());
        int openingMinutes = culturalAsset.getOpeningTime().getMinutes();
        if (openingMinutes == 0) {
            openingTimeMinutes.setSelectedIndex(0);
        } else {
            openingTimeMinutes.setSelectedItem(openingMinutes);
        }
        int openingHours = culturalAsset.getOpeningTime().getHours();
        if (openingHours < 10) {
            openingTimeHours.setSelectedItem("0" + openingHours);
        } else {
            openingTimeHours.setSelectedItem(openingHours);
        }
        int closingMinutes = culturalAsset.getClosingTime().getMinutes();
        if (closingMinutes == 0) {
            closingTimeMinutes.setSelectedIndex(0);
        } else {
            closingTimeMinutes.setSelectedItem(closingMinutes);
        }
        int closingHours = culturalAsset.getClosingTime().getHours();
        if (closingHours < 10) {
            closingTimeHours.setSelectedItem("0" + closingHours);
        } else {
            closingTimeHours.setSelectedItem(closingHours);
        }
    }

/**
 * This method loads the cultural statistics provided
 * as input to the class constructor.
 */
private void loadStatistics() {
    txtAssetName.setText(ca.getName());
    double rating = ca.getAverageRating();
    if (rating >= 4) {
        ratingIcon.setIcon(new ImageIcon(getClass().getResource(
                Home.URL_IMAGES + "star5.gif")));
    } else if (rating < 4 && rating >= 3) {
        ratingIcon.setIcon(new ImageIcon(getClass().getResource(
                Home.URL_IMAGES + "star4.gif")));
    } else if (rating < 3 && rating >= 2) {
        ratingIcon.setIcon(new ImageIcon(getClass().getResource(
                Home.URL_IMAGES + "star3.gif")));
    } else if (rating < 2 && rating >= 1) {
        ratingIcon.setIcon(new ImageIcon(getClass().getResource(
                Home.URL_IMAGES + "star2.gif")));
    } else {
        ratingIcon.setIcon(new ImageIcon(getClass().getResource(
                Home.URL_IMAGES + "star1.gif")));
    }

    ArrayList<Integer> stats = null;
    try {
        stats = assetManager.getAssetStatistics(ca.getId());
    } catch (RemoteException e) {
        // Handle RemoteException
    }
    monthStats[0].setText("" + stats.get(0));
    monthStats[1].setText("" + stats.get(1));
    monthStats[2].setText("" + stats.get(2));
    monthStats[3].setText("" + stats.get(3));
    monthStats[4].setText("" + stats.get(4));
    monthStats[5].setText("" + stats.get(5));
}

/**
 * This method shows or hides the labels next to the suggestions
 * in the form.
 */
private void showHideSuggestions() {
    Iterator<JLabel> iterator = suggestions.iterator();
    while (iterator.hasNext()) {
        JLabel current = iterator.next();
        current.setVisible(!current.isVisible());
    }
}

/**
 * This method enables or disables form editing.
 */
private void enableDisableEdit() {
    Component[] components = dataBC.getComponents();
    for (int i = 0; i < components.length; i++) {
        Component current = components[i];
        if (current instanceof JTextField) {
            JTextField textField = (JTextField) current;
            textField.setEditable(!textField.isEditable());
            textField.setBackground(Color.white);
        } else if (current instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) current;
            comboBox.setEnabled(!comboBox.isEnabled());
        }
    }
    descriptionBC.setEditable(!descriptionBC.isEditable());
    tagPanel.enableDisable();
}

/**
 * This method initializes the toolbar for the cultural object tab.
 *
 * @return javax.swing.JToolBar - the toolbar.
 */
private JToolBar getToolbarAssetTab() {
    if (null == assetTabToolbar) {
        assetTabToolbar = new JToolBar();
        assetTabToolbar.setFloatable(false);
        assetTabToolbar.add(getEditButton());
        assetTabToolbar.addSeparator();
        assetTabToolbar.add(getSaveButton());
        assetTabToolbar.addSeparator();
        assetTabToolbar.add(getCancelButton());
        assetTabToolbar.addSeparator();
        if (ca != null) {
            assetTabToolbar.add(getEditCommentButton());
            assetTabToolbar.addSeparator();
        }
    }
    return assetTabToolbar;
}

/**
 * This method initializes the button to modify cultural asset data.
 *
 * @return javax.swing.JToggleButton - the edit button.
 */
private JToggleButton getEditButton() {
    if (null == editButton) {
        editButton = new JToggleButton();
        editButton.setText("Edit Data");
        editButton.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "EditBC32.png")));
        editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                showHideSuggestions();
                enableDisableEdit();
                saveButton.setVisible(editButton.isSelected());
                cancelButton.setVisible(editButton.isSelected());
            }
        });
    }
    return editButton;
}

/**
 * This method initializes the save button.
 *
 * @return javax.swing.JButton
 */
private JButton getSaveButton() {
    if (null == saveButton) {
        saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "save.png")));
        saveButton.setVisible(false);
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent pEvent) {
                if (ca == null) {
                    ca = fillBeanWithData();
                    try {
                        assetManager.insertCulturalAsset(ca);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    return saveButton;
}

/**
 * This method initializes the button to clear the form (reset cultural asset data)
 * or reload the cultural data (modify data).
 *
 * @return javax.swing.JButton - the button.
 */
private JButton getCancelButton() {
    if (null == cancelButton) {
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Cancel32.png")));
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setVisible(false);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (ca == null) {
                    Component[] components = dataBC.getComponents();
                    for (int i = 0; i < components.length; i++) {
                        Component current = components[i];
                        if (current instanceof JTextComponent) {
                            ((JTextComponent) current).setText("");
                        } else if (current instanceof JComboBox) {
                            JComboBox combo = (JComboBox) current;
                            combobox.setSelectedIndex(-1);
                        }
                    }
                    tagPanel.clear();
                    descriptionBC.setText("");
                } else {
                    loadDataForm();
                }
            }
        });
    }
    return cancelButton;
}

/**
 * This method initializes the button to edit a comment.
 *
 * @return javax.swing.JButton - the button to edit a comment.
 */
private JButton getEditCommentButton() {
    if (null == editCommentButton) {
        editCommentButton = new JButton();
        editCommentButton.setText("Edit Comment");
        editCommentButton.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "editComment.png")));
        editCommentButton.setVisible(false);
        editCommentButton.setEnabled(false);
        editCommentButton.addActionListener(new ActionListener() {
            ...
        });
    }
    return editCommentButton;
}

public void actionPerformed(ActionEvent pEvent) {
    int selectedRow = tableFeedback.getSelectedRow();
    String newComment = (String) JOptionPane.showInternalInputDialog(
            jContentPane,
            "Edit the selected comment:",
            "Edit Comment",
            JOptionPane.QUESTION_MESSAGE,
            new ImageIcon(getClass().getResource(Home.URL_IMAGES + "editComment.png")),
            null,
            feedbackModel.getValueAt(selectedRow, 1));
    if (newComment != null) {
        feedbackModel.modifyComment(newComment, selectedRow);
    }
}

private JPanel getDataBCForm() {
    if (null == dataBC) {
        dataBC = new JPanel(null);
        dataBC.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

        // Creating tooltips
        for (int i = 0; i < tips.length; i++) {
            JLabel newTip = new JLabel();
            newTip.setIcon(new ImageIcon(getClass().getResource(Home.URL_IMAGES + "Info16.png")));
            newTip.setBounds(145, 8 + 30 * i, 24, 24);
            newTip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            newTip.setToolTipText(tips[i]);
            suggestions.add(newTip);
            dataBC.add(newTip);
            if (i == tips.length - 1) {
                newTip.setBounds(400, 155, 24, 24);
            }
        }

        for (int i = 0; i < labels.length; i++) {
            JLabel newLabel = new JLabel(labels[i], SwingConstants.RIGHT);
            newLabel.setBounds(25, 10 + 30 * i, 120, 16);
            newLabel.repaint();
            dataBC.add(newLabel, null);
        }

        // Name of Cultural Heritage
        nameBC = new JTextField();
        nameBC.setColumns(12);
        nameBC.setDocument(new LimitedDocument(30));
        nameBC.setBounds(185, 10, 136, 20);
        nameBC.setName("Name of Cultural Heritage");
        dataBC.add(nameBC, null);

        // Address
        address2 = new JTextField();
        address2.setBounds(270, 40, 136, 20);
        address2.setDocument(new LimitedDocument(30));
        address1 = new JComboBox(address);
        address1.setSelectedIndex(-1);
        address1.setBounds(185, 40, 60, 20);
        address2.setName("Address");
        dataBC.add(address2, null);
        dataBC.add(address1, null);

        // City
        cityBC = new JTextField();
        cityBC.setColumns(12);
        cityBC.setBounds(185, 70, 136, 20);
        cityBC.setName("City");
        cityBC.setDocument(new OnlyCharactersDocument(25));
        dataBC.add(cityBC);

        // Location
        localityBC = new JTextField();
        localityBC.setBounds(185, 100, 136, 20);
        localityBC.setName("Location");
        localityBC.setDocument(new OnlyCharactersDocument(25));
        dataBC.add(localityBC, null);

        // Postal Code
        postalCodeBC = new JTextField();
        postalCodeBC.setColumns(8);
        postalCodeBC.setBounds(185, 130, 92, 20);
        postalCodeBC.setDocument(new NumericDocument(5));
        dataBC.add(postalCodeBC, null);

        // Geographical Location
        JLabel txtX = new JLabel("X");
        JLabel txtY = new JLabel("Y");
        JLabel txtZ = new JLabel("Z");
        Font newFont = new Font("Dialog", Font.BOLD, 14);
        txtX.setFont(newFont);
        txtY.setFont(newFont);
        txtZ.setFont(newFont);
        txtZ.setBounds(365, 190, 10, 22);
        txtY.setBounds(295, 190, 10, 22);
        txtX.setBounds(227, 190, 14, 20);
        posGeoX = new JTextField(12);
        posGeoX.setBounds(185, 190, 40, 20);
        posGeoY = new JTextField(12);
        posGeoY.setBounds(255, 190, 40, 20);
        posGeoZ = new JTextField(12);
        posGeoZ.setBounds(325, 190, 40, 20);
        posGeoX.setName("X-coordinate");
        posGeoY.setName("Y coordinate");
        posGeoZ.setName("z coordinate");
        dataBC.add(txtX, null);
        dataBC.add(txtY, null);
        dataBC.add(txtZ, null);
        dataBC.add(posGeoX, null);
        dataBC.add(posGeoY, null);
        dataBC.add(posGeoZ, null);

        /// State
        provBC = new JComboBox(province);
        provBC.setSelectedIndex(-1);
        provBC.setBounds(185, 160, 50, 20);
        dataBC.add(provBC, null);

        // Description
        descriptionBC = new JTextArea();
        descriptionBC.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        descriptionBC.setWrapStyleWord(true);
        descriptionBC.setLineWrap(true);
        JScrollPane = new JScrollPane(descriptionBC);
        JScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setBounds(new Rectangle(185, 370, 395, 69));
        descriptionBC.setName("Description");
        dataBC.add(jScrollPane, null);

        // Phone
        phoneBC = new JTextField(12);
        phoneBC.setBounds(185, 220, 136, 20);
        phoneBC.setDocument(new NumericDocument(10));
        phoneBC.setName("Phone");
        dataBC.add(phoneBC, null);

        // Opening
        hoursOP = new JComboBox();
        hoursOP.setBounds(185, 250, 40, 20);
        hoursCL = new JComboBox();
        hoursCL.setBounds(185, 280, 40, 20);
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hoursCL.addItem("0" + i);
                hoursOP.addItem("0" + i);
            } else {
                hoursOP.addItem(i);
                hoursCL.addItem(i);
            }
        }
        minutesOP = new JComboBox();
        minutesOP.setBounds(241, 250, 40, 20);
        minutesOP.addItem("00");
        minutesOP.addItem("15");
        minutesOP.addItem("30");
        minutesOP.addItem("45");
        minutesCL = new JComboBox();
        minutesCL.setBounds(241, 280, 40, 20);
        minutesCL.addItem("00");
        minutesCL.addItem("15");
        minutesCL.addItem("30");
        minutesCL.addItem("45");
        minutesOP.setSelectedIndex(0);
        hoursOP.setSelectedIndex(-1);
        minutesCL.setSelectedIndex(0);
        hoursCL.setSelectedIndex(-1);
        newFont = new Font("Dialog", Font.BOLD, 18);
        labelColon1 = new JLabel(":");
        labelColon1.setBounds(230, 245, 10, 24);
        labelColon1.setFont(newFont);
        JLabel labelColon2 = new JLabel(":");
        labelColon2.setBounds(230, 275, 10, 24);
        labelColon2.setFont(newFont);
        dataBC.add(hoursOP, null);
        dataBC.add(minutesOP, null);
        dataBC.add(minutesCL, null);
        dataBC.add(hoursCL, null);
        dataBC.add(labelColon1, null);
        dataBC.add(labelColon2, null);

        // Closed
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        dayClosure = new JComboBox(days);
        dayClosure.setBounds(185, 310, 96, 20);
        dayClosure.setSelectedIndex(-1);
        dataBC.add(dayClosure, null);

        // Cost
        costBC = new JTextField();
        costBC.setColumns(8);
        costBC.setBounds(185, 340, 40, 20);
        JLabel euro = new JLabel("Euro");
        euro.setBounds(230, 340, 30, 16);
        dataBC.add(costBC, null);
        dataBC.add(euro, null);

        // TagPanel
        tagPanel = new TagPanel();
        tagPanel.setBounds(405, 180, 180, 170);
        JLabel lblTag = new JLabel("Search Tags");
        lblTag.setBounds(420, 150, 140, 30);
        dataBC.add(lblTag, null);
        dataBC.add(tagPanel, null);

        return dataBC;
    }
    return dataBC;
}


/**
 * This method initializes the panel of statistics for the current month.
 *
 * @return javax.swing.JPanel - the panel of statistics for the current month.
 */
private JPanel getCurrentMonthStats() {
    if (currentMonthStats == null) {
        currentMonthStats = new JPanel();
        currentMonthStats.setLayout(new GridBagLayout());
        currentMonthStats.setPreferredSize(new Dimension(500, 280));
        currentMonthStats.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Statistics Current Month",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
                new Color(0, 102, 204)));
        currentMonthStats.setBackground(Color.white);
        monthStatsC = new JButton[6];
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5, 5, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        currentMonthStats.add(new JLabel("Details of votes received this month:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        Font newFont = new Font("Dialog", Font.BOLD, 16);
        for (int i = 5; i >= 1; i--) {
            int gridX = gbc.gridx;
            gbc.gridy++;
            JLabel aLabel = new JLabel(new ImageIcon(getClass().getResource(
                    Home.URL_IMAGES + "star" + i + ".gif")), JLabel.CENTER);
            currentMonthStats.add(aLabel, gbc);
            gbc.gridx++;
            currentMonthStats.add(new JLabel("=="), gbc);
            gbc.gridx++;
            gbc.anchor = GridBagConstraints.EAST;
            monthStatsC[i - 1] = new JButton();
            monthStatsC[i - 1].setFont(newFont);
            currentMonthStats.add(monthStatsC[i - 1], gbc);
            gbc.gridx = gridX;
            gbc.anchor = GridBagConstraints.CENTER;
        }
    }
    return currentMonthStats;
}

/**
 * This method initializes the panel on the total statistics
 * The cultural property.
 *
 * @return javax.swing.JPanel - the panel statistics totals.
 *
 */
/**
 * This method initializes the panel of total statistics.
 *
 * @return javax.swing.JPanel - the panel of total statistics.
 *
 */
private JPanel getTotalStats() {
    if (null == totalStats) {
        totalStats = new JPanel();
        totalStats.setLayout(new GridBagLayout());
        totalStats.setPreferredSize(new Dimension(500, 280));
        totalStats.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 102, 255), 3),
                "Total Statistics",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
                new Color(0, 102, 204)));
        totalStats.setBackground(Color.white);
        totalStatsButtons = new JButton[6];
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5, 5, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        totalStats.add(new JLabel("Details of votes received this month:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 1;
        Font newFont = new Font("Dialog", Font.BOLD, 16);
        for (int i = 5; i >= 1; i--) {
            int gridX = gbc.gridx;
            gbc.gridy++;
            JLabel aLabel = new JLabel(new ImageIcon(getClass()
                    .getResource(Home.URL_IMAGES + "star" + i + ".gif")), JLabel.CENTER);
            totalStats.add(aLabel, gbc);
            gbc.gridx++;
            totalStats.add(new JLabel("=="), gbc);
            gbc.gridx++;
            gbc.anchor = GridBagConstraints.EAST;
            totalStatsButtons[i - 1] = new JButton();
            totalStatsButtons[i - 1].setFont(newFont);
            totalStats.add(totalStatsButtons[i - 1], gbc);
            gbc.gridx = gridX;
            gbc.anchor = GridBagConstraints.CENTER;
        }
    }
    return totalStats;
}

private BeanCulturalAsset transferDataToBean() {
    BeanCulturalAsset newAsset = new BeanCulturalAsset();
    newAsset.setName(nameBC.getText());
    newAsset.setDescription(descriptionBC.getText());
    newAsset.setPostalCode(capBC.getText());
    newAsset.setTicketCost(Double.parseDouble(costBC.getText()));
    newAsset.setClosingDay((String) closingDay.getSelectedItem());
    newAsset.setPhoneNumber(phoneBC.getText());
    newAsset.setCity(cityBC.getText());
    newAsset.setLocation(locationBC.getText());
    // Date (int year, int month, int date, int hrs, int min)
    Date openingTime = new Date(0, 0, 0, hourOpening.getSelectedIndex(), minuteOpening.getSelectedIndex());
    Date closingTime = new Date(0, 0, 0, hourClosing.getSelectedIndex(), minuteClosing.getSelectedIndex());
    newAsset.setOpeningTime(openingTime);
    newAsset.setClosingTime(closingTime);
    newAsset.setProvince((String) provinceBC.getSelectedItem());
    newAsset.setAddress(((String) address1.getSelectedItem()) + " " + address2.getText());
    Point3D position = new Point3D(Double.parseDouble(geoPosX.getText()), 
                                   Double.parseDouble(geoPosY.getText()), 
                                   Double.parseDouble(geoPosZ.getText()));
    newAsset.setPosition(position);
    return newAsset;
}

private void loadTags() {
    ArrayList<BeanTag> beanTags = null;
    try {
        beanTags = tag.getTags();
        if (bc != null) {
            ArrayList<Integer> tagIds = new ArrayList<>();
            ArrayList<BeanTag> selectedTags = managementBC.getAssetTags(bc.getId());
            for (BeanTag b : selectedTags) {
                tagIds.add(b.getId());
            }
        }
    } catch (RemoteException e) {
        // If an error occurs, the tag panel remains blank
    } finally {
        for (BeanTag b : beanTags) {
            tagPanel.insertTag(b);
        }
        tagPanel.setSelectedTags(tagIds);
        tagPanel.repaint();
    }
}
}