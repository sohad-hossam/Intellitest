package unisa.gps.etour.bean;

/**
 * Bean containing information relating to the General Preferences
 *
 * @ Author Mauro Miranda
 * @ Version 0.1 2007 eTour Project - Copyright by SE @ SA Lab DMI University
 * Of Salerno
 */

import java.io.Serializable;

public class BeanGenericPreferences implements Serializable {
    private static final long serialVersionUID = 6805656922951334071L;
    private int id;
    private int fontSize;
    private String font;
    private String theme;
    private int touristId;

    /**
     * Parameterized constructor
     *
     * @param id
     * @param fontSize
     * @param font
     * @param theme
     * @param touristId
     */
    public BeanGenericPreferences(int id, int fontSize, String font, String theme, int touristId) {
        setId(id);
        setFontSize(fontSize);
        setFont(font);
        setTheme(theme);
        setTouristId(touristId);
    }

    /**
     * Empty Constructor
     */
    public BeanGenericPreferences() {

    }

    /**
     * Returns the value of font size
     *
     * @return value of font size.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the new value of font size
     *
     * @param fontSize New value for font size.
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Returns the value of font
     *
     * @return Value of font.
     */
    public String getFont() {
        return font;
    }

    /**
     * Sets the new value of font
     *
     * @param font New value for font.
     */
    public void setFont(String font) {
        this.font = font;
    }

    /**
     * Returns the value of the theme
     *
     * @return value of the theme.
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Sets the new value of the theme
     *
     * @param theme New theme value.
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Returns the value of id
     *
     * @return value of id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the value of touristId
     *
     * @return value of touristId.
     */
    public int getTouristId() {
        return touristId;
    }

    /**
     * Sets the new value of touristId
     *
     * @param touristId New touristId value.
     */
    public void setTouristId(int touristId) {
        this.touristId = touristId;
    }

    /**
     * Sets the new value of id
     *
     * @param id New value for id.
     */
    public void setId(int id) {
        this.id = id;
    }
}