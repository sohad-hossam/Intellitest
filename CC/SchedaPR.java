/*
 * SchedaPR.java
 *
 * 1.0
 *
 * 28/05/2007
 *
 * 2007 eTour Project - Copyright by SE @ SA Lab - DMI - University of Salerno
 */

package unisa.gps.etour.gui.operatoragency;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import unisa.gps.etour.bean.PointOfRefreshment;
import unisa.gps.etour.bean.util.Point3D;

/**
 * Class that models the interface for viewing the card,
 * Modify the data and the insertion of a new resting spot.
 *
 * @Author Lello
 */
public class RefreshmentCard extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane = null;
    private JToolBar toolbarRefreshment = null;
    private JToggleButton btnModify = null;
    private JButton btnSave = null;
    private JButton btnCancel = null;
    private JButton btnModifyComment = null;
    private JTabbedPane tabbedPane = null;
    private JPanel statisticsPanel = null;
    private JPanel feedbackPanel = null;
    private JLabel lblName = null;
    private JLabel lblAddress = null;
    private JLabel lblPostalCode = null;
    private JLabel lblCity = null;
    private JLabel lblLocation = null;
    private JLabel lblProvince = null;
    private JLabel lblPosition = null;
    private JLabel lblPhone = null;
    private JLabel lblOpeningHour = null;
    private JLabel lblClosingHour = null;

    private JLabel lblAddressField = null;
    private JTextField addressField = null;
    private JComboBox<String> addressComboBox = null;
    private JTextField cityField = null;
    private JComboBox<String> locationComboBox = null;
    private JTextField postalCodeField = null;
    private JScrollPane descriptionScrollPane = null;
    private JTextArea descriptionArea = null;
    private JTextField phoneField = null;
    private JComboBox<Integer> openingHourHourComboBox = null;
    private JLabel lblColon1 = null;
    private JComboBox<Integer> openingHourMinuteComboBox = null;
    private TagPanel tagPanel;
    private JTextField costField = null;

    private JLabel lblColon2 = null;
    private JComboBox<Integer> closingHourHourComboBox = null;
    private JComboBox<Integer> provinceComboBox = null;
    private JPanel dataPanel = null;
    private JTextField nameField = null;
    private JPanel imagePanel = null;
    private JScrollPane feedbackScrollPane = null;
    private JTable feedbackTable = null;
    private JLabel lblAssetName = null;
    private JLabel averageRatingLabel = null;
    private JPanel currentMonthStatisticsPanel = null;
    private JPanel totalStatisticsPanel = null;
    private JLabel lblColon3 = null;
    private JLabel lblColon4 = null;
    private ActionListener fieldCompleted;
    private FocusListener validation;
    private JToolBar toolbarRefreshmentCard = null;
    private JTextField geoPosXField = null;
    private JTextField geoPosYField = null;
    private JTextField geoPosZField = null;
    private JLabel lblColon5 = null;
    private JComboBox<Integer> closingHourMinuteComboBox = null;
    private JComboBox<Integer> openingHourHourComboBox1 = null;

    /**
     * The default constructor for inclusion of the interface model
     * A new refreshment.
     */
    public RefreshmentCard() {
        super("New Refreshment");
        fieldCompleted = new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                ((JComponent) actionEvent.getSource()).transferFocus();
            }

        };
        validation = new FocusListener() {

            private final Color ERROR_BACKGROUND = new Color(255, 215, 215);
            private final Color WARNING_BACKGROUND = new Color(255, 235, 205);
            private String text;

            public void focusGained(FocusEvent fe) {
                if (fe.getSource() instanceof JTextField) {
                    JTextField textbox = (JTextField) fe.getSource();
                    text = textbox.getText();
                }
            }

            public void focusLost(FocusEvent fe) {
                if (fe.getSource() instanceof JTextField) {
                    JTextField textbox = (JTextField) fe.getSource();
                    if (!text.equals(textbox.getText())) {
                        text = textbox.getText();
                        if (text.equals("")) {
                            textbox.setBackground(ERROR_BACKGROUND);
                            Rectangle bounds = textbox.getBounds();
                            JLabel errorLabel = new JLabel();
                            errorLabel.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/images/error.png")));
                            errorLabel.setBounds(bounds.x - 24, bounds.y, 24, 24);
                            errorLabel.setToolTipText("Field " + textbox.getName() + " can not be empty!");
                            dataPanel.add(errorLabel, null);
                            dataPanel.repaint();
                        }
                    }
                }
            }
        };
        initialize();
    }

    /**
     * This interface models the manufacturer regarding modification of data and
     * Display board a refreshment.
     *
     * @param pointOfRefreshment - the bean contains the data of
     *                           PointOfRefreshment selected.
     * @param change             - indicates whether the fields should be editable, so if
     *                           You are viewing a card or change the cultural property.
     */
    public RefreshmentCard(PointOfRefreshment pointOfRefreshment, boolean change) {
        this();
        nameField.setText(pointOfRefreshment.getName());
        setTitle(pointOfRefreshment.getName());
        postalCodeField.setText(pointOfRefreshment.getPostalCode());
        cityField.setText(pointOfRefreshment.getCity());

        descriptionArea.setText(pointOfRefreshment.getDescription());
        StringTokenizer tokenizer = new StringTokenizer(pointOfRefreshment.getAddress());

        String[] path = {"Street", "P.zza", "V.le", "V.co", "Largo", "Course"};
        String string = tokenizer.nextToken();
        int i;
        for (i = 0; i < path.length; i++) {
            if (string.equalsIgnoreCase(path[i])) {
                break;
            }
        }
        this.addressComboBox.setSelectedIndex(i);
        while (tokenizer.hasMoreTokens()) {
            this.addressField.setText(this.addressField.getText() + " " + tokenizer.nextToken());
        }
        this.provinceComboBox.setSelectedItem(pointOfRefreshment.getProvince());
        Point3D pos = pointOfRefreshment.getPosition();
        this.geoPosXField.setText("" + pos.getX());
        this.geoPosYField.setText("" + pos.getY());
        this.geoPosZField.setText("" + pos.getZ());
        this.phoneField.setText(pointOfRefreshment.getPhone());
        int minutes = pointOfRefreshment.getOpeningHour().getMinutes();
        if (minutes == 0) {
            this.openingHourMinuteComboBox.setSelectedIndex(0);
        } else {
            this.openingHourMinuteComboBox.setSelectedItem(minutes);
        }
        int hours = pointOfRefreshment.getOpeningHour().getHours();
        if (hours < 10) {
            this.openingHourHourComboBox.setSelectedItem("0" + hours);
        } else {
            this.openingHourHourComboBox.setSelectedItem(hours);
        }
        this.closingHourMinuteComboBox.setSelectedItem(pointOfRefreshment.getClosingHour().getMinutes());
        this.openingHourHourComboBox.setSelectedItem(pointOfRefreshment.getOpeningHour().getHours());
        this.closingHourHourComboBox.setSelectedItem(pointOfRefreshment.getClosingHour().getHours());
        if (change) {
            btnModify.setSelected(true);
        } else {
            makeEditable();
        }
    }

    /**
     * Method called by the constructor
     */
    private void initialize() {
        this.setIconifiable(true);
        this.setBounds(new Rectangle(0, 0, 600, 540));
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setFrameIcon(new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/operatoragency/images/card.png")));
        this.setClosable(true);
        this.setContentPane(getContentPane());
    }

    private void makeEditable() {
        Component[] components = dataPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component current = components[i];
            if (current instanceof JTextField) {
                JTextField textbox = (JTextField) current;
                textbox.setEditable(!textbox.isEditable());
                textbox.setBackground(Color.white);
            } else if (current instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) current;
                combo.setEnabled(!combo.isEnabled());
            }
        }
        descriptionArea.setEditable(!descriptionArea.isEditable());
        tagPanel.activateDeactivate();
    }

    private JPanel getContentPane() {
        if (contentPane == null) {
            contentPane = new JPanel();
            contentPane.setLayout(new BorderLayout());

            contentPane.add(getTabbedPane(), BorderLayout.CENTER);
            contentPane.add(getToolbarRefreshmentCard(), BorderLayout.CENTER);
        }
        return contentPane;
    }

/**
* This method initializes the button (ToggleButton) for modifying the data of refreshment point
*
* @return javax.swing.JToggleButton
*/
private JToggleButton getBtnModify() {
    if (btnModify == null) {
        btnModify = new JToggleButton();
        btnModify.setText("Change Data");
        btnModify.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/operatoragency/images/modify.png")));
        btnModify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                makeEditable();
                btnSave.setVisible(btnModify.isSelected());
                btnCancel.setVisible(btnModify.isSelected());
            }
        });
    }
    return btnModify;
}

/**
* Method to initialize the Save button (btnSave)
*
* @return javax.swing.JButton
*/
private JButton getBtnSave() {
    if (btnSave == null) {
        btnSave = new JButton();
        btnSave.setText("Save");
        btnSave.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/operatoragency/images/save.png")));
        btnSave.setVisible(false);
    }
    return btnSave;
}

/**
* Method to initialize the Cancel button (btnCancel)
*
* @return javax.swing.JButton
*/
private JButton getBtnCancel() {
    if (btnCancel == null) {
        btnCancel = new JButton();
        btnCancel.setText("Cancel");
        btnCancel.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/operatoragency/images/cancel.png")));
        btnCancel.setVisible(false);
    }
    return btnCancel;
}

/**
* Method to initialize the button for changing a comment (btnModifyComment)
*
* @return javax.swing.JButton
*/
private JButton getBtnModifyComment() {
    if (btnModifyComment == null) {
        btnModifyComment = new JButton();
        btnModifyComment.setText("Edit Comment");
        btnModifyComment.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/operatoragency/images/modifyComment.png")));
        btnModifyComment.setVisible(false);
    }
    return btnModifyComment;
}

/**
* Create and initialize a JTabbedPane
*
* @return javax.swing.JTabbedPane
*/
private JTabbedPane getJTabbedPane() {
    if (jTabbedPane == null) {
        jTabbedPane = new JTabbedPane();
        jTabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        jTabbedPane.addTab("Data Refreshment", new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/operatoragency/images/data.png")), getDataPR(), null);
        jTabbedPane.addTab("Tourist Menu", new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoragency/images/stat24.png")), null, null);
        jTabbedPane.addTab("Statistics", new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoragency/images/stat24.png")), getStatistics(), null);
        jTabbedPane.addTab("Feedback received", new ImageIcon(getClass().getResource("/unisa/gps/eTour/gui/operatoragency/images/feedback.png")), getFeedback(), null);
    }
    return jTabbedPane;
}

/**
* Method to initialize a panel (dataRefreshment)
*
* @return javax.swing.JPanel
*/
private JPanel getDataRefreshment() {
    if (dataRefreshment == null) {
        GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
        gridBagConstraints27.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints27.gridy = 9;
        gridBagConstraints27.weightx = 1.0;
        gridBagConstraints27.anchor = GridBagConstraints.WEST;
        gridBagConstraints27.insets = new Insets(5, 5, 36, 0);
        gridBagConstraints27.ipadx = 18;
        gridBagConstraints27.gridx = 1;
        
        GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
        gridBagConstraints34.gridx = 7;
        gridBagConstraints34.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints34.gridy = 6;
        
        jLabel2 = new JLabel();
        jLabel2.setText("z");
        
        GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
        gridBagConstraints33.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints33.gridy = 6;
        gridBagConstraints33.weightx = 1.0;
        gridBagConstraints33.ipadx = 50;
        gridBagConstraints33.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints33.anchor = GridBagConstraints.WEST;
        gridBagConstraints33.gridx = 6;
        
        GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
        gridBagConstraints38.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints38.gridy = 6;
        gridBagConstraints38.weightx = 1.0;
        gridBagConstraints38.ipadx = 50;
        gridBagConstraints38.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints38.anchor = GridBagConstraints.WEST;
        gridBagConstraints38.gridx = 4;
        
        GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
        gridBagConstraints22.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints22.gridy = 6;
        gridBagConstraints22.weightx = 0.0;
        gridBagConstraints22.ipadx = 50;
        gridBagConstraints22.anchor = GridBagConstraints.WEST;
        gridBagConstraints22.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints22.gridx = 1;
        
        GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
        gridBagConstraints36.insets = new Insets(0, 5, 0, 5);
        gridBagConstraints36.gridy = 6;
        gridBagConstraints36.ipadx = 0;
        gridBagConstraints36.ipady = 0;
        gridBagConstraints36.gridwidth = 1;
        gridBagConstraints36.gridx = 5;
        
        GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
        gridBagConstraints35.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints35.gridy = 6;
        gridBagConstraints35.ipadx = 0;
        gridBagConstraints35.ipady = 0;
        gridBagConstraints35.gridwidth = 1;
        gridBagConstraints35.anchor = GridBagConstraints.WEST;
        gridBagConstraints35.gridx = 3;
        
        GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
        gridBagConstraints32.insets = new Insets(15, 20, 5, 0);
        gridBagConstraints32.gridx = 16;
        gridBagConstraints32.gridy = 4;
        gridBagConstraints32.ipadx = 172;
        gridBagConstraints32.ipady = 125;
        gridBagConstraints32.gridwidth = 0;
        gridBagConstraints32.gridheight = 6;
        
        GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
        gridBagConstraints31.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints31.gridwidth = 9;
        gridBagConstraints31.gridx = 1;
        gridBagConstraints31.gridy = 0;
        gridBagConstraints31.weightx = 0.0;
        gridBagConstraints31.ipadx = 240;
        gridBagConstraints31.anchor = GridBagConstraints.WEST;
        gridBagConstraints31.insets = new Insets(20, 5, 5, 0);
        
        GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
        gridBagConstraints30.fill = GridBagConstraints.BOTH;
        gridBagConstraints30.gridwidth = 17;
        gridBagConstraints30.gridx = 1;
        gridBagConstraints30.gridy = 10;
        gridBagConstraints30.ipadx = 265;
        gridBagConstraints30.ipady = 70;
        gridBagConstraints30.weightx = 1.0;
        gridBagConstraints30.weighty = 1.0;
        gridBagConstraints30.gridheight = 4;
        gridBagConstraints30.anchor = GridBagConstraints.WEST;
        gridBagConstraints30.insets = new Insets(5, 5, 2, 5);
        
        GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
        gridBagConstraints29.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints29.gridwidth = 3;
        gridBagConstraints29.gridx = 4;
        gridBagConstraints29.gridy = 9;
        gridBagConstraints29.weightx = 1.0;
        gridBagConstraints29.ipadx = 18;
        gridBagConstraints29.anchor = GridBagConstraints.WEST;
        gridBagConstraints29.insets = new Insets(5, 5, 36, 2);
        
        GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
        gridBagConstraints28.insets = new Insets(3, 5, 34, 4);
        gridBagConstraints28.gridy = 9;
        gridBagConstraints28.gridx = 3;
        
        GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
        gridBagConstraints26.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints26.gridwidth = 3;
        gridBagConstraints26.gridx = 4;
        gridBagConstraints26.gridy = 8;
        gridBagConstraints26.weightx = 1.0;
        gridBagConstraints26.anchor = GridBagConstraints.WEST;
        gridBagConstraints26.ipadx = 18;
        gridBagConstraints26.insets = new Insets(6, 5, 4, 2);
        
        GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
        gridBagConstraints25.insets = new Insets(4, 5, 2, 4);
        gridBagConstraints25.gridy = 8;
        gridBagConstraints25.anchor = GridBagConstraints.WEST;
        gridBagConstraints25.gridx = 3;
        
        GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
        gridBagConstraints24.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints24.gridx = 1;
        gridBagConstraints24.gridy = 8;
        gridBagConstraints24.weightx = 1.0;
        gridBagConstraints24.ipadx = 18;
        gridBagConstraints24.gridwidth = 3;
        gridBagConstraints24.anchor = GridBagConstraints.WEST;
        gridBagConstraints24.insets = new Insets(6, 5, 4, 1);
        
        GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
        gridBagConstraints23.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints23.gridwidth = 9;
        gridBagConstraints23.gridx = 1;
        gridBagConstraints23.gridy = 7;
        gridBagConstraints23.weightx = 1.0;
        gridBagConstraints23.ipadx = 120;
        gridBagConstraints23.anchor = GridBagConstraints.WEST;
        gridBagConstraints23.insets = new Insets(4, 5, 4, 17);
        
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints21.gridwidth = 7;
        gridBagConstraints21.gridx = 1;
        gridBagConstraints21.gridy = 5;
        gridBagConstraints21.ipadx = 70;
        gridBagConstraints21.ipady = 0;
        gridBagConstraints21.weightx = 1.0;
        gridBagConstraints21.anchor = GridBagConstraints.WEST;
        gridBagConstraints21.insets = new Insets(5, 5, 5, 6);
        
        GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
        gridBagConstraints20.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints20.gridwidth = 7;
        gridBagConstraints20.gridx = 1;
        gridBagConstraints20.gridy = 4;
        gridBagConstraints20.weightx = 1.0;
        gridBagConstraints20.ipadx = 60;
        gridBagConstraints20.anchor = GridBagConstraints.WEST;
        gridBagConstraints20.insets = new Insets(0, 5, 0, 0);
        
        GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
        gridBagConstraints19.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints19.gridwidth = 4;
        gridBagConstraints19.gridx = 1;
        gridBagConstraints19.gridy = 3;
        gridBagConstraints19.weightx = 1.0;
        gridBagConstraints19.ipadx = 20;
        gridBagConstraints19.anchor = GridBagConstraints.WEST;
        gridBagConstraints19.insets = new Insets(6, 5, 5, 18);
        
        GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
        gridBagConstraints18.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints18.gridwidth = 6;
        gridBagConstraints18.gridx = 1;
        gridBagConstraints18.gridy = 2;
        gridBagConstraints18.weightx = 1.0;
        gridBagConstraints18.ipadx = 100;
        gridBagConstraints18.anchor = GridBagConstraints.WEST;
        gridBagConstraints18.insets = new Insets(0, 5, 0, 0);
        
        GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
        gridBagConstraints17.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints17.gridwidth = 9;
        gridBagConstraints17.gridx = 2;
        gridBagConstraints17.gridy = 1;
        gridBagConstraints17.weightx = 1.0;
        gridBagConstraints17.ipadx = 200;
        gridBagConstraints17.anchor = GridBagConstraints.WEST;
        gridBagConstraints17.insets = new Insets(5, 5, 5, 0);
        
        GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
        gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints16.gridwidth = 3;
        gridBagConstraints16.gridx = 1;
        gridBagConstraints16.gridy = 1;
        gridBagConstraints16.weightx = 1.0;
        gridBagConstraints16.anchor = GridBagConstraints.WEST;
        gridBagConstraints16.ipadx = 0;
        gridBagConstraints16.insets = new Insets(5, 5, 5, 0);
        
        GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
        gridBagConstraints15.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints15.gridy = 10;
        gridBagConstraints15.gridwidth = 1;
        gridBagConstraints15.gridheight = 0;
        gridBagConstraints15.gridx = 0;
        
        GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
        gridBagConstraints14.insets = new Insets(5, 15, 36, 0);
        gridBagConstraints14.gridy = 9;
        gridBagConstraints14.gridx = 0;
        
        GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        gridBagConstraints13.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints13.gridy = 8;
        gridBagConstraints13.gridx = 0;
        
        GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
        gridBagConstraints12.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints12.gridy = 7;
        gridBagConstraints12.gridx = 0;
        
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints11.gridy = 6;
        gridBagConstraints11.gridx = 0;
        
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints10.gridy = 5;
        gridBagConstraints10.gridx = 0;
        
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints9.gridy = 4;
        gridBagConstraints9.gridx = 0;
        
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints8.gridy = 3;
        gridBagConstraints8.gridx = 0;
        
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints7.gridy = 2;
        gridBagConstraints7.gridx = 0;
        
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.insets = new Insets(5, 15, 5, 0);
        gridBagConstraints6.gridy = 1;
        gridBagConstraints6.gridx = 0;
        
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.insets = new Insets(20, 15, 5, 0);
        gridBagConstraints5.gridy = 0;
        gridBagConstraints5.gridwidth = 1;
        gridBagConstraints5.gridx = 0;
        
        jLabel41 = new JLabel();
        jLabel41.setFont(new Font("Dialog", Font.BOLD, 14));
        jLabel41.setText("y");
        
        jLabel4 = new JLabel();
        jLabel4.setFont(new Font("Dialog", Font.BOLD, 14));
        jLabel4.setText("x");
        
        jLabel3 = new JLabel();
        jLabel3.setFont(new Font("Dialog", Font.BOLD, 18));
        jLabel3.setText(":");
        
        jLabel1 = new JLabel();
        jLabel1.setFont(new Font("Dialog", Font.BOLD, 18));
        jLabel1.setText(":");
        
        JLabel jLabel = new JLabel();
        jLabel.setText("Description");
        
        JLabel txtOpeningHours = new JLabel();
        txtOpeningHours.setText("Opening Hours");
        
        JLabel txtClosingHours = new JLabel();
        txtClosingHours.setText("Closing Time");
        
        JLabel txtPhone = new JLabel();
        txtPhone.setText("Phone");
        
        JLabel txtPosition = new JLabel();
        txtPosition.setText("Geographic Position");
        
        JLabel txtProvince = new JLabel();
        txtProvince.setText("Province");
        
        JLabel txtLocation = new JLabel();
        txtLocation.setText("Location");
        
        JLabel txtCity = new JLabel();
        txtCity.setText("City");
        
        JLabel txtPostalCode = new JLabel();
        txtPostalCode.setText("CAP");
        
        JLabel txtAddress = new JLabel();
        txtAddress.setText("Address");
        
        JLabel txtName = new JLabel();
        txtName.setText("Name Refreshment");
        
        dataRefreshment = new JPanel();
        dataRefreshment.setLayout(new GridBagLayout());
        dataRefreshment.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        
        dataRefreshment.add(txtName, gridBagConstraints5);
        dataRefreshment.add(txtAddress, gridBagConstraints6);
        dataRefreshment.add(txtCity, gridBagConstraints7);
        dataRefreshment.add(txtLocation, gridBagConstraints8);
        dataRefreshment.add(txtPostalCode, gridBagConstraints9);
        dataRefreshment.add(txtProvince, gridBagConstraints10);
        dataRefreshment.add(txtPosition, gridBagConstraints11);
        dataRefreshment.add(txtPhone, gridBagConstraints12);
        dataRefreshment.add(txtOpeningHours, gridBagConstraints13);
        dataRefreshment.add(txtClosingHours, gridBagConstraints14);
        dataRefreshment.add(jLabel, gridBagConstraints15);
        dataRefreshment.add(getAddressPR1(), gridBagConstraints16);
        dataRefreshment.add(getAddressPR(), gridBagConstraints17);
        dataRefreshment.add(getCityPR(), gridBagConstraints18);
        dataRefreshment.add(getLocationPR(), gridBagConstraints19);
        dataRefreshment.add(getCapPR(), gridBagConstraints20);
        dataRefreshment.add(getProvPR(), gridBagConstraints21);
        dataRefreshment.add(getPhonePR(), gridBagConstraints23);
        dataRefreshment.add(getOpeningHoursPR(), gridBagConstraints24);
        dataRefreshment.add(jLabel1, gridBagConstraints25);
        dataRefreshment.add(getOpeningHoursMinPR(), gridBagConstraints26);
        dataRefreshment.add(jLabel3, gridBagConstraints28);
        dataRefreshment.add(getClosingHoursMinPR(), gridBagConstraints29);
        dataRefreshment.add(getScrollPane(), gridBagConstraints30);
        dataRefreshment.add(getNamePR(), gridBagConstraints31);
        dataRefreshment.add(getPanel(), gridBagConstraints32);
        dataRefreshment.add(jLabel4, gridBagConstraints35);
        dataRefreshment.add(jLabel41,gridBagConstraints36);
        dataRefreshment.add(getGeoPosX(),gridBagConstraints22);
        dataRefreshment.add(getGeoPosY(),gridBagConstraints38);
        dataRefreshment.add(getGeoPosZ(),gridBagConstraints33);
        dataRefreshment.add(jLabel2,gridBagConstraints34);
        dataRefreshment.add(getClosingHoursPR(),gridBagConstraints27);
    }
    return dataRefreshment;
}

/**
 * Method to initialize a panel (statistics)
 *
 * @return javax.swing.JPanel
 */
private JPanel getStatistics() {
    if (statistics == null) {
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridwidth = 0;
        gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.insets = new Insets(20, 0, 0, 0);
        gridBagConstraints4.gridy = 2;

        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new Insets(0, 0, 20, 0);
        gridBagConstraints3.gridy = 1;

        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.insets = new Insets(0, 30, 30, 0);
        gridBagConstraints2.anchor = GridBagConstraints.WEST;
        gridBagConstraints2.gridy = 0;

        mediaVotePR = new JLabel();
        mediaVotePR.setText("JLabel");

        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.insets = new Insets(0, 0, 30, 0);
        gridBagConstraints1.gridy = 0;

        txtGoodName = new JLabel();
        txtGoodName.setText("Well name> Cultural>");
        txtGoodName.setFont(new Font("Dialog", Font.BOLD, 18));

        statistics = new JPanel();
        statistics.setLayout(new GridBagLayout());
        statistics.add(txtGoodName, gridBagConstraints1);
        statistics.add(mediaVotePR, gridBagConstraints2);
        statistics.add(getCurrentMonthStatistics(), gridBagConstraints3);
        statistics.add(getTotalStatistics(),gridBagConstraints4);
    }
    return statistics;
}

/**
 * Method to initialize a panel (feedback)
 *
 * @return javax.swing.JPanel
 */
private JPanel getFeedback() {
    if (feedback == null) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;

        feedback = new JPanel();
        feedback.setLayout(new GridBagLayout());
        feedback.add(getScrollPane2(), gridBagConstraints);
    }
    return feedback;
}

/**
 * Initialize a JTextField for entering
 * An address indirizzoPR
 *
 * @return javax.swing.JTextField
 */
private JTextField getAddressPR() {
    if (addressPR == null) {
        addressPR = new JTextField();
        addressPR.setColumns(12);
        addressPR.addActionListener(fieldCompiled);
    }
    return addressPR;
}

/**
 * Method to initialize the type field address (indirizzoPR)
 * Or via, square ....
 *
 * @return javax.swing.JComboBox
 */
private JComboBox getAddressTypePR() {
    if (addressTypePR == null) {
        addressTypePR = new JComboBox();
        addressTypePR.setPreferredSize(new Dimension(60, 20));
        addressTypePR.setMinimumSize(new Dimension(60, 25));
        addressTypePR.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        addressTypePR.addItem("Street");
        addressTypePR.addItem("Piazza");
        addressTypePR.addItem("Avenue");
        addressTypePR.addItem("Alley");
        addressTypePR.addItem("Wide");
        addressTypePR.addItem("Course");
    }
    return addressTypePR;
}

/**
 * Initialize a JTextField for entering
 * A city CittaPR
 *
 * @return javax.swing.JTextField
 */
private JTextField getCityPR() {
    if (cityPR == null) {
        cityPR = new JTextField();
        cityPR.setColumns(12);
        cityPR.addActionListener(fieldCompiled);
    }
    return cityPR;
}

/**
 * This method initializes locationPR
 *
 * @return javax.swing.JComboBox
 */
private JComboBox getLocationPR() {
    if (locationPR == null) {
        locationPR = new JComboBox();
        locationPR.setMinimumSize(new Dimension(80, 25));
        locationPR.setPreferredSize(new Dimension(80, 20));
        locationPR.addActionListener(fieldCompiled);
    }
    return locationPR;
}

/**
 * Code of refreshment. Definition of capPR JTextField
 *
 * @return javax.swing.JTextField
 */
private JTextField getPostalCodePR() {
    if (postalCodePR == null) {
        postalCodePR = new JTextField();
        postalCodePR.setColumns(8);
        postalCodePR.addActionListener(fieldCompiled);
    }
    return postalCodePR;
}

/**
 * Create JScrollPane
 *
 * @return javax.swing.JScrollPane
 */
private JScrollPane getScrollPane() {
    if (scrollPane == null) {
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(getDescriptionPR());
    }
    return scrollPane;
}

/**
 * Method to create JTextArea for the whole descriptionPR
 *
 * @return javax.swing.JTextArea
 */
private JTextArea getDescriptionPR() {
    if (descriptionPR == null) {
        descriptionPR = new JTextArea();
        descriptionPR.setColumns(12);
        descriptionPR.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    }
    return descriptionPR;
}

/**
 * Method to create the JTextField for phone number telefonoPR
 *
 * @return javax.swing.JTextField
 */
private JTextField getPhoneNumberPR() {
    if (phoneNumberPR == null) {
        phoneNumberPR = new JTextField();
        phoneNumberPR.setColumns(12);
        phoneNumberPR.addActionListener(fieldCompiled);
    }
    return phoneNumberPR;
}

/**
 * Method to initialize a JComboBox with hours (orarioAPOrePR)
 *
 * @return javax.swing.JComboBox
 */
private JComboBox getOpeningTimeHoursPR() {
    if (openingTimeHoursPR == null) {
        openingTimeHoursPR = new JComboBox();
        openingTimeHoursPR.setPreferredSize(new Dimension(40, 20));
        for (int i = 0; i < 24; i++) {
            if (i < 10)
                openingTimeHoursPR.addItem("0" + i);
            else
                openingTimeHoursPR.addItem(String.valueOf(i));
        }
        openingTimeHoursPR.addActionListener(fieldCompiled);
    }
    return openingTimeHoursPR;
}

/**
 * Method to initialize a JComboBox with minutes (orarioApMinPR)
 *
 * @return javax.swing.JComboBox
 */
private JComboBox getOpeningTimeMinutesPR() {
    if (openingTimeMinutesPR == null) {
        openingTimeMinutesPR = new JComboBox();
        openingTimeMinutesPR.setLightWeightPopupEnabled(true);
        openingTimeMinutesPR.setPreferredSize(new Dimension(40, 20));
        openingTimeMinutesPR.addItem("00");
        openingTimeMinutesPR.addItem("15");
        openingTimeMinutesPR.addItem("30");
        openingTimeMinutesPR.addItem("45");
        openingTimeMinutesPR.addActionListener(fieldCompiled);
    }
    return openingTimeMinutesPR;
}

/**
 * Method to initialize a JComboBox with minutes (orarioCHMinPR)
 *
 * @return javax.swing.JComboBox
 */
private JComboBox getClosingTimeMinutesPR() {
    if (closingTimeMinutesPR == null) {
        closingTimeMinutesPR = new JComboBox();
        closingTimeMinutesPR.setPreferredSize(new Dimension(40, 20));
        closingTimeMinutesPR.addItem("00");
        closingTimeMinutesPR.addItem("15");
        closingTimeMinutesPR.addItem("30");
        closingTimeMinutesPR.addItem("45");
        closingTimeMinutesPR.addActionListener(fieldCompiled);
    }
    return closingTimeMinutesPR;
}

/**
 * Create and initialize a JComboBox with all the provinces (provPR)
 *
 * @return javax.swing.JTextField
 */
private JComboBox getProvincePR() {
    if (provincePR == null) {
        final String[] Province = {"AG", "AL", "AN", "AO", "AQ", "AR", "AP", "AT", "AV", "BA", "BL", "BN", "BG", "BI", "BO", "BR", "BS", "BZ",
                "CA", "CB", "CE", "CH", "CI", "CL", "CN", "CO", "CR", "CS", "KR", "EN", "FC", "FE", "FI", "FG", "FR", "GE", "GO", "GR", "IM", "IS", "LC",
                "LE", "LI", "LO", "LT", "LU", "MC", "ME", "MF", "MN", "MO", "MS", "MT", "NA", "NO", "NU", "OG", "OR", "OT", "PA", "PC", "PD", "PE", "PG", "PO", "PR", "PU", "R", "RA", "RC", "RE", "RG",
                "RI", "RM", "RN", "RO", "SA", "SI", "SO", "SP", "SS", "SV", "TA", "TE", "TN", "TP", "TR", "TS", "TV", "UD", "VA", "VB", "VC", "VE", "VI",
                "VR", "VS", "VT", "VV"};
        provincePR = new JComboBox();
        for (int i = 0; i < Province.length; i++) {
            provincePR.addItem(Province[i]);
        }
        provincePR.addActionListener(fieldCompiled);
    }
    return provincePR;
}

/**
 * Numeric Document class extends PlainDocument
 */
private int limit;

public NumericDocument(int limit) {
    this.limit = limit;
}

/**
 * Initialization and management of position
 *
 * @param pOffset integer
 * @param pString String
 * @param attr AttributeSet
 */
public void insertString(int pOffset, String pStr, AttributeSet attr) throws BadLocationException {
    if (pStr == null)
        return;

    if ((getLength() + pStr.length()) <= limit) {
        super.insertString(pOffset, pStr, attr);
    }
}

/**
 * Initialization of a data point of the snack (namePR)
 *
 * @return javax.swing.JTextField
 */
private JTextField getNamePR() {
    if (namePR == null) {
        namePR = new JTextField();
        namePR.setColumns(12);
        namePR.setPreferredSize(new Dimension(180, 20));
        namePR.addActionListener(fieldCompiled);
        namePR.addFocusListener(validating);
        namePR.setDocument(new NumericDocument(20));
    }
    return namePR;
}

/**
 * Initialize and create a panel (JPanel)
 *
 * @return javax.swing.JPanel
 */
private JPanel getJPanel() {
    if (jPanel == null) {
        jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Tag the 'Search TitledBorder.DEFAULT_JUSTIFICATION",
                TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
                Color.black));
        BeanTag[] test = new BeanTag[8];
        test[0] = new BeanTag(0, "castle", "really a castle");
        test[1] = new BeanTag(1, "stronghold", "really a hostel");
        test[2] = new BeanTag(3, "statue", "really a basket");
        test[3] = new BeanTag(4, "Column", "really a basket");
        test[4] = new BeanTag(5, "internal", "really a basket");
        test[5] = new BeanTag(6, "external", "really a basket");
        test[6] = new BeanTag(7, "eight hundred", "really a basket");
        test[7] = new BeanTag(8, "Novecento", "really a basket");
        tagPanel = new TagPanel(test);
        jPanel.add(tagPanel, BorderLayout.CENTER);
    }
    return jPanel;
}

/**
 * Creating a JScrollPane (jScrollPane2)
 *
 * @return javax.swing.JScrollPane
 */
private JScrollPane getJScrollPane2() {
    if (jScrollPane2 == null) {
        jScrollPane2 = new JScrollPane();
        jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setViewportView(getFeedbackTable());
    }
    return jScrollPane2;
}

/**
 * Create a JTable (feedbackTable)
 *
 * @return javax.swing.JTable
 */
private JTable getFeedbackTable() {
    if (feedbackTable == null) {
        feedbackTable = new JTable();
    }
    return feedbackTable;
}

/**
 * Creation of a panel (currentMonthStatistics)
 *
 * @return javax.swing.JPanel
 */
private JPanel getCurrentMonthStatistics() {
    if (currentMonthStatistics == null) {
        currentMonthStatistics = new JPanel();
        currentMonthStatistics.setLayout(new GridBagLayout());
        currentMonthStatistics.setPreferredSize(new Dimension(500, 120));
    }
    return currentMonthStatistics;
}

/**
 * Creation of a panel (totalStatistics)
 *
 * @return javax.swing.JPanel
 */
private JPanel getTotalStatistics() {
    if (totalStatistics == null) {
        totalStatistics = new JPanel();
        totalStatistics.setLayout(new GridBagLayout());
        totalStatistics.setPreferredSize(new Dimension(500, 120));
    }
    return totalStatistics;
}

/**
 * Method for creating a toolbar
 * (ToolbarPRCard)
 *
 * @return javax.swing.JToolBar
 */
private JToolBar getToolbarPRCard() {
    if (toolbarPRCard == null) {
        toolbarPRCard = new JToolBar();
        toolbarPRCard.setFloatable(false);
        toolbarPRCard.add(getBtnModify());
        toolbarPRCard.addSeparator();
        toolbarPRCard.add(getBtnSave());
        toolbarPRCard.addSeparator();
        toolbarPRCard.add(getBtnCancel());
        toolbarPRCard.addSeparator();
        toolbarPRCard.add(getBtnModifyComment());
        toolbarPRCard.addSeparator();
    }
    return toolbarPRCard;
}

/**
 * Method to initialize posGeoX
 * The X position of the GPS
 *
 * @return javax.swing.JTextField
 */
private JTextField getGeoPosX() {
    if (geoPosX == null) {
        geoPosX = new JTextField();
    }
    return geoPosX;
}

/**
 * Method to initialize posGeoY
 * The Y position of the GPS
 *
 * @return javax.swing.JTextField
 */
private JTextField getGeoPosY() {
    if (geoPosY == null) {
        geoPosY = new JTextField();
    }
    return geoPosY;
}

/**
 * Method to initialize posGeoZ
 * The Z position of the GPS
 *
 * @return javax.swing.JTextField
 */
private JTextField getGeoPosZ() {
    if (geoPosZ == null) {
        geoPosZ = new JTextField();
    }
    return geoPosZ;
}

/**
 * Method to initialize a JComboBox with the hours (closingTimeHoursPR)
 *
 * @return javax.swing.JComboBox
 */
private JComboBox getClosingTimeHoursPR() {
    if (closingTimeHoursPR == null) {
        closingTimeHoursPR = new JComboBox();
        closingTimeHoursPR.setPreferredSize(new Dimension(40, 20));
    }
    return closingTimeHoursPR;
}
}