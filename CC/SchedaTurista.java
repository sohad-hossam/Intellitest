package unisa.gps.etour.gui.agencyoperator;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.util.DateUtil;

/**
 * Class that models the interface for displaying and modifying the data of a tourist account.
 *
 * @version 1.0
 * @author Mario Gallo
 *
 * © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class TouristCard extends JInternalFrame implements ITouristCard {

    private static final String[] help = {""};
    private JPanel jContentPane = null;
    private JToolBar toolbarCard = null;
    private JToggleButton btnEdit = null;
    private JButton btnSave = null;
    private JButton btnReset = null;
    private JTabbedPane tabbedPane = null;
    private JTextField address2 = null;
    private JComboBox address1 = null;
    private JTextField city = null;
    private JTextField zipCode = null;
    private JTextField phone = null;
    private JComboBox province = null;
    private JPanel touristData = null;
    private JTextField firstName = null;
    private Vector<JLabel> suggestions;
    private BeanTourist tourist;
    private JTextField lastName;
    private JComboBox day;
    private JComboBox month;
    private JComboBox years;
    private JTextField placeOfBirth;
    private JTextField email;
    private JTextField username;
    private JPasswordField password;
    private JLabel registrationDate;
    private Tourists parent;

    /**
     * Constructs the tourist card for either viewing or modifying tourist data.
     *
     * @param pParent   unisa.gps.etour.gui.agencyoperator.Tourists - the parent window.
     * @param pTourist  unisa.gps.etour.bean.BeanTourist - the bean containing tourist data.
     * @param pEditMode true if modifications are being made to the data, false if viewing the card.
     */
    public TouristCard(Tourists pParent, BeanTourist pTourist, boolean pEditMode) {
        super();
        this.parent = pParent;
        setIconifiable(true);
        setSize(560, 520);
        suggestions = new Vector<>();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setClosable(true);
        tourist = pTourist;
        if (tourist.isActive()) {
            setFrameIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/agencyoperator/images/tourist_tab.png")));
        } else {
            setFrameIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/agencyoperator/images/DeactivateTourist32.png")));
        }
        initialize();
        if (pEditMode) {
            btnEdit.setSelected(true);
            btnSave.setVisible(true);
            btnReset.setVisible(true);
        } else {
            showHideSuggestions();
            enableDisableEdit();
        }
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent pEvent) {
                if (btnEdit.isSelected()) {
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Are you sure you want to close this tourist's card?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("Warning! Unsaved data will be lost.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/agencyoperator/images/warning16.png")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = {"Close", "Cancel"};
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Confirm closing Tourist Card " + tourist.getFirstName() + " " + tourist.getLastName(),
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, getFrameIcon(), options, options[1]);
                    if (choice == JOptionPane.OK_OPTION) {
                        parent.closeCard((TouristCard) pEvent.getInternalFrame());
                    }
                } else {
                    parent.closeCard((TouristCard) pEvent.getInternalFrame());
                }
            }
        });
    }

    /**
     * Initializes the tourist card interface.
     */
    private void initialize() {
        jContentPane = new JPanel();
        jContentPane.setLayout(new BorderLayout());
        jContentPane.add(getToolbarCard(), BorderLayout.CENTER);
        tabbedPane = new JTabbedPane();
        tabbedPane.setCursor(Cursor.getDefaultCursor());
        tabbedPane.addTab("Tourist Information", getTouristDataForm());
        jContentPane.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(jContentPane);
        loadFormData();
    }

    /**
     * Loads the bean data provided by tourist camps into the form.
     */
    private void loadFormData() {
        setTitle("Tourist Profile - " + tourist.getFirstName() + " " + tourist.getLastName());
        firstName.setText(tourist.getFirstName());
        lastName.setText(tourist.getLastName());
        Date dob = tourist.getDateOfBirth();
        day.setSelectedIndex(dob.getDate());
        month.setSelectedIndex(dob.getMonth());
        years.setSelectedIndex(dob.getYear());
        StringTokenizer tokenizer = new StringTokenizer(tourist.getAddress());
        String street = tokenizer.nextToken();
        address1.setSelectedItem(street);
        address2.setText(tourist.getAddress().substring(street.length()));
        placeOfBirth.setText(tourist.getPlaceOfBirth());
        phone.setText(tourist.getPhone());
        city.setText(tourist.getCity());
        password.setText(tourist.getPassword());
        province.setSelectedItem(tourist.getProvince());
        username.setText(tourist.getUsername());
        zipCode.setText(tourist.getZipCode());
        email.setText(tourist.getEmail());
        registrationDate.setText(DateUtil.toExtendedFormat(tourist.getRegistrationDate()));
    }

    /**
     * Shows or hides the suggestions related to the form fields.
     */
    private void showHideSuggestions() {
        Iterator<JLabel> it = suggestions.iterator();
        while (it.hasNext()) {
            JLabel current = it.next();
            current.setVisible(!current.isVisible());
        }
    }

    /**
     * Enables or disables editable form fields.
     */
    private void enableDisableEdit() {
        Component[] components = touristData.getComponents();
        for (Component current : components) {
            if (current instanceof JTextField) {
                JTextField textField = (JTextField) current;
                textField.setEditable(!textField.isEditable());
                textField.setBackground(Color.WHITE);
            } else if (current instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) current;
                comboBox.setEnabled(!comboBox.isEnabled());
            }
        }
    }

    /**
     * Initializes the toolbar for the functionality of the tourist card.
     */
    private JToolBar getToolbarCard() {
        if (toolbarCard == null) {
            toolbarCard = new JToolBar();
            toolbarCard.setFloatable(false);
            toolbarCard.add(getBtnEdit());
            toolbarCard.addSeparator();
            toolbarCard.add(getBtnSave());
            toolbarCard.addSeparator();
            toolbarCard.add(getBtnReset());
            toolbarCard.addSeparator();
        }
        return toolbarCard;
    }

    /**
     * Initializes the button for editing data.
     */
    private JToggleButton getBtnEdit() {
        if (null == btnEdit) {
            btnEdit = new JToggleButton();
            btnEdit.setText("Edit Data");
            btnEdit.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/EditTourist32.png")));
            btnEdit.setToolTipText("Enable or disable editing of selected tourist data.");
            btnEdit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    showHideSuggestions();
                    enableDisableEdit();
                    btnSave.setVisible(btnEdit.isSelected());
                    btnReset.setVisible(btnEdit.isSelected());
                }
            });
        }
        return btnEdit;
    }

    /**
     * Initializes the button to save the changes made to the data of the tourist.
     */
    private JButton getBtnSave() {
        if (null == btnSave) {
            btnSave = new JButton();
            btnSave.setText("Save");
            btnSave.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/save.png")));
            btnSave.setToolTipText("Save changes to the selected tourist profile.");
            btnSave.setVisible(false);
            btnSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    // Construction of the dialog for confirmation of the change
                    JPanel root = new JPanel(new BorderLayout());
                    JLabel message = new JLabel("Update the tourist profile of " + tourist.getFirstName() + " " + tourist.getLastName() + " with the data from the form?");
                    message.setFont(new Font("Dialog", Font.BOLD, 14));
                    JLabel alert = new JLabel("The previous data can no longer be recovered.", SwingConstants.CENTER);
                    alert.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/warning16.png")));
                    root.add(message, BorderLayout.NORTH);
                    root.add(alert, BorderLayout.CENTER);
                    String[] options = {"Edit", "Cancel"};
                    // The dialog screen appears
                    int choice = JOptionPane.showInternalOptionDialog(jContentPane, root, "Commit Changes to Tourist Profile", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/EditTourist48.png")), options, options[1]);
                    // If you chose to confirm the change
                    if (choice == JOptionPane.YES_OPTION) {
                        tourist.setFirstName(firstName.getText());
                        tourist.setLastName(lastName.getText());
                        tourist.setZipCode(zipCode.getText());
                        tourist.setPlaceOfBirth(placeOfBirth.getText());
                        tourist.setDateOfBirth(new Date(years.getSelectedIndex(), month.getSelectedIndex(), day.getSelectedIndex()));
                        tourist.setCity(city.getText());
                        tourist.setUsername(username.getText());
                        tourist.setEmail(email.getText());
                        tourist.setPhone(phone.getText());
                        tourist.setAddress(address1.getSelectedItem().toString() + " " + address2.getText());
                        tourist.setProvince(province.getSelectedItem().toString());
                        String pass = "";
                        char[] password = password.getPassword();
                        for (int i = 0; i < password.length; i++) {
                            pass += password[i];
                        }
                        tourist.setPassword(pass);
                        loadFormData();
                        enableDisableEdit();
                        btnSave.setVisible(false);
                        btnReset.setVisible(false);
                        btnEdit.setSelected(false);
                        showHideSuggestions();
                        parent.updateTableModel(tourist);
                        JOptionPane.showInternalMessageDialog(jContentPane, "The tourist's data have been updated successfully.", "Modified Tourist Profile", JOptionPane.OK_OPTION, new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/ok48.png")));
                    }
                }
            });
        }
        return btnSave;
    }

    /**
     * Initializes the button to reset the tourist data in the form.
     */
    private JButton getBtnReset() {
        if (null == btnReset) {
            btnReset = new JButton();
            btnReset.setText("Reset");
            btnReset.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/operatoreagenzia/images/Reset32.png")));
            btnReset.setToolTipText("Reload the selected tourist information.");
            btnReset.setVisible(false);
            btnReset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    loadFormData();
                }
            });
        }
        return btnReset;
    }

    /**
     * Initializes the form containing tourist data.
     */
    private JPanel getTouristDataForm() {
        if (null == touristData) {
            touristData = new JPanel(null);
            touristData.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            // Creation of Tips
            String[] labels = {"First Name", "Last Name", "Date of Birth", "Place of Birth", "Phone", "Address", "City", "Postal Code", "Province", "E-Mail", "Username", "Password", "Save"};

            for (String s : help) {
                JLabel newLabel = new JLabel();
                newLabel.setIcon(new ImageIcon(getClass().getResource("/unisa/gps/etour/gui/images/Info16.png")));
                newLabel.setBounds(145, 8 + 30 * suggestions.size(), 24, 24);
                newLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                newLabel.setToolTipText(s);
                suggestions.add(newLabel);
                touristData.add(newLabel);
            }

            for (int i = 0; i < labels.length; i++) {
                JLabel newLabel = new JLabel(labels[i], SwingConstants.RIGHT);
                newLabel.setBounds(25, 10 + 30 * i, 120, 20);
                newLabel.repaint();
                touristData.add(newLabel, null);
            }

            // First Name
            firstName = new JTextField(12);
            firstName.setBounds(185, 10, 136, 20);
            firstName.setName("First Name");
            touristData.add(firstName, null);

            // Last Name
            lastName = new JTextField(12);
            lastName.setBounds(185, 40, 136, 20);
            lastName.setName("Last Name");
            touristData.add(lastName, null);

            // Date of Birth
            day = new JComboBox<>();
            day.setBounds(185, 70, 40, 20);
            for (int i = 1; i <= 31; i++) {
                day.addItem(i);
            }
            month = new JComboBox<>();
            month.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent pEvent) {
                    int count = day.getItemCount();
                    switch (month.getSelectedIndex()) {
                        case 0:
                        case 2:
                        case 4:
                        case 6:
                        case 7:
                        case 9:
                        case 11:
                            for (int i = count + 1; i <= 31; i++) {
                                day.addItem(i);
                            }
                            break;
                        case 1:
                            int year = (Integer) years.getSelectedItem();
                            boolean leap = ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
                            if (count != 28) {
                                for (int i = count - 1; i > 27; i--) {
                                    day.removeItemAt(i);
                                }
                            }
                            if (leap && count != 29) {
                                day.addItem("29");
                            }
                            break;
                        case 3:
                        case 5:
                        case 8:
                        case 10:
                            if (count == 31) {
                                day.removeItemAt(30);
                            } else {
                                for (int i = count + 1; i <= 30; i++) {
                                    day.addItem(i);
                                }
                            }
                            break;
                    }
                }
            });
            month.setBounds(245, 70, 40, 20);
            for (int i = 1; i <= 12; i++) {
                month.addItem(i);
            }
            years = new JComboBox<>();
            years.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    if (month.getSelectedIndex() == 1) {
                        int year = (Integer) years.getSelectedItem();
                        boolean leap = ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
                        int count = day.getItemCount();
                        if (leap && count != 29) {
                            day.addItem("29");
                        } else if (leap && count == 29) {
                            day.removeItemAt(28);
                        }
                    }
                }
            });
            years.setBounds(305, 70, 80, 20);
            Date today = new Date();
            for (int i = 0; i <= today.getYear() - 14; i++) {
                years.addItem(1900 + i);
            }
            touristData.add(day, null);
            touristData.add(month, null);
            touristData.add(years, null);

            // Place of Birth
            placeOfBirth = new JTextField(12);
            placeOfBirth.setBounds(185, 100, 136, 20);
            placeOfBirth.setName("Place of Birth");
            touristData.add(placeOfBirth, null);

            // Phone
            phone = new JTextField(12);
            phone.setBounds(185, 130, 136, 20);
            phone.setName("Phone");
            touristData.add(phone, null);

            // Address
            address2 = new JTextField(12);
            address2.setBounds(270, 160, 136, 20);
            address1 = new JComboBox<>();
            address1.setSelectedIndex(-1);
            address1.setBounds(185, 160, 60, 20);
            touristData.add(address2, null);
            touristData.add(address1, null);

            // City
            city = new JTextField(12);
            city.setBounds(185, 190, 136, 20);
            city.setName("City");
            touristData.add(city, null);

            // Postal Code
            zipCode = new JTextField(8);
            zipCode.setBounds(185, 220, 92, 20);
            touristData.add(zipCode, null);

            // Province
            province = new JComboBox<>();
            province.setSelectedIndex(-1);
            province.setBounds(185, 250, 50, 20);
            touristData.add(province, null);

            // E-Mail
            email = new JTextField();
            email.setBounds(185, 280, 200, 20);
            email.setName("E-Mail");
            touristData.add(email, null);

            // Username
            username = new JTextField();
            username.setBounds(185, 310, 136, 20);
            username.setName("Username");
            touristData.add(username, null);

            // Password
            password = new JPasswordField(12);
            password.setBounds(185, 340, 136, 20);
            password.setName("Password");
            touristData.add(password, null);

            // Registration Date
            registrationDate = new JLabel();
            registrationDate.setBounds(185, 370, 140, 20);
            touristData.add(registrationDate, null);
        }
        return touristData;
    }

    /**
     * Returns the id of the tourist who is viewing/editing.
     *
     * @return int - the id of the tourist.
     */
    public int getId() {
        return tourist.getId();
    }
}
