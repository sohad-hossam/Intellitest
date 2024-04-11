package unisa.gps.etour.control.fuzzy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class CategoryList implements Serializable {

    /**
     * Keeps track of data in each category
     */
    private static final long serialVersionUID = 1L;
    private Hashtable<String, Category> categories; // hash table that keeps
                                                    // For each category a
                                                    // Class category
    private Hashtable<String, float[]> termTotals;   // hash table that keeps
                                                    // The terms of all
                                                    // Categories
    private float maxDistance;  // contains the maximum distances

    /**
     * The constructor initializes the two hash tables that contain
     * Categories and terms of all categories
     */
    public CategoryList() {
        categories = new Hashtable<>();
        termTotals = new Hashtable<>();
    }

    /**
     * Access method to get the maximum distance of all the terms in all categories
     *
     * @return the maximum distance
     */
    public float getMaxDistance() {
        return maxDistance;
    }

    /**
     * Access method to get all categories
     *
     * @return categories
     */
    public Hashtable<String, Category> getAllCategories() {
        return categories;
    }

    /**
     * Access method to get the total terms
     *
     * @return termTotals
     */
    public Hashtable<String, float[]> getTotalTerms() {
        return termTotals;
    }

    /**
     * Method to access a category in the category table
     *
     * @param categoryName the name of the category
     * @return the category object associated with the name
     */
    public Category getCategory(String categoryName) {
        if (categoryExists(categoryName)) {
            return categories.get(categoryName);
        }
        return null;
    }

    /**
     * Method to access the values of a specific term in this category table
     *
     * @param term the term to access
     * @return the values associated with the term
     */
    public float[] getTerm(String term) {
        if (termExists(term)) {
            return termTotals.get(term);
        }
        return null;
    }
}
/**
 * Method which allows you to add a category to the category table
 *
 * @param categoryName the name of the category to add
 * @param category the associated category object
 * @return true if the operation was successfully carried out, false otherwise
 */
public boolean addCategory(String categoryName, Category category) {
    if (!categoryExists(categoryName)) { // if the category exists
        return false; // return false
    }
    categories.put(categoryName, category); // otherwise load the category in the table
    return true; // return true
}

/**
 * Edit a category in the category table
 *
 * @param categoryName the name of the category to edit
 * @param category the object to be associated with this category
 * @return true if the operation was successfully carried out, false otherwise
 */
public boolean setCategory(String categoryName, Category category) {
    if (categoryExists(categoryName)) { // if the category does not exist
        return false; // return false
    }
    categories.put(categoryName, category); // update the category table
    return true; // return true
}

/**
 * Method which allows you to set the value of a term in the total terms table
 *
 * @param term the term name
 * @param value the value to associate with the term
 */
public void setTerm(String term, float[] value) {
    termTotals.put(term, value);
}

/**
 * Method which allows setting the maximum distance value of terms from one category
 *
 * @param maxDistance the maximum distance
 */
public void setMaxDistance(float maxDistance) {
    this.maxDistance = maxDistance;
}

/**
 * Method which allows to derive a collection of names of all categories in the categories table
 *
 * @return an iterable collection of category names
 */
public Iterable<String> getCategories() {
    List<String> toReturn = new ArrayList<>(); // create a new list
    for (Enumeration<String> val = categories.keys(); val.hasMoreElements();) { // iterates N times where N is the number of categories in the table
        toReturn.add(val.nextElement()); // adds to the list the name of a category
    }
    return toReturn;
}

/**
 * Method to verify the existence of a category in the category table
 *
 * @param categoryName the name of the category
 * @return true if the category exists, false otherwise
 */
public boolean categoryExists(String categoryName) {
    try {
        categories.get(categoryName); // try to extract the category name from the category table
        return true; // if the transaction does not raise exceptions, the category exists and returns true
    } catch (NullPointerException e) {
        return false; // false otherwise
    }
}

/**
 * Method to verify the existence of a term in the total terms table
 *
 * @param term the term name
 * @return true if the term exists, false otherwise
 */
public boolean termExists(String term) {
    try {
        if (termTotals.get(term) != null) {
            return true;
        }
    } catch (NullPointerException e) {
        return false;
    }
    return false;
}


