package unisa.gps.etour.control.fuzzy;

import java.io.Serializable;
import java.util.Hashtable;

public class Category implements Serializable {

    /**
     * Class that describes the characteristics of a Category
     * Contains a Hashtable that represents the dictionary on
     * The category that contains and for each term associated
     * In the category values of frequency, distance and relevance.
     * Provides methods to access, modify and auxiliary methods.
     */
    private static final long serialVersionUID = -8652232946927756089L;
    private String name; // name of the category
    private Hashtable<String, float[]> terms; // list of terms and their frequencies and distance relevance

    /**
     * Constructor:
     * Get the category name as a parameter to create
     */
    public Category(String name) {
        this.name = name;
        terms = new Hashtable<>();
    }

    /**
     * Returns the Hashtable containing the terms
     * With the respective values of frequency, relevance and distance
     */
    public Hashtable<String, float[]> getTerms() {
        return terms;
    }

    /**
     * Returns the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Get the string representing the term as a parameter
     * Of which you want to get the values of frequency, range, and bearing
     */
    public float[] getValue(String term) throws NullPointerException {
        if (termExists(term))
            return terms.get(term);
        return null;
    }

    /**
     * Adds a term to the category's dictionary
     */
    public void addTerm(String term) {
        terms.put(term, new float[3]);
    }

    /**
     * Adds a term to the category's dictionary
     * Also sets the values of frequency, distance, and relevance
     */
    public boolean addTerm(String term, float[] val) {
        if (val == null || term.equals(""))
            return false;
        terms.put(term, val);
        return true;
    }

    /**
     * Sets the values for the term
     */
    public boolean setTermValue(String term, float[] val) throws NullPointerException {
        if (termExists(term)) {
            terms.put(term, val);
            return true;
        }
        return false;
    }

    public void setTerms(Hashtable<String, float[]> terms) {
        this.terms = terms;
    }

    /**
     * Returns True if the term is present in
     * Dictionary of Category, False otherwise
     */
    public boolean termExists(String term) {
        try {
            float[] ret = terms.get(term);
            return ret != null;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
