
package unisa.gps.etour.control.fuzzy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class Fuzzy {

    /**
     * Class that implements the methods used to calculate the Fuzzy
     * Category of membership of a refreshment or a cultural property.
     */

    /**
     * Method for calculating the relevance of a term.
     *
     * @param distance: Contains the distance of the term from category Analyzed
     * @param pMaxDist: Contains the maximum distance of all terms in all Categories.
     * @return Returns the relevance of the term in the category.
     */
    private static float relevance(float distance, float pMaxDist) {
        return (distance / pMaxDist);
    }

    /**
     * Method for calculating the distance between a term and a category.
     *
     * @param pTerm:         Contains the term analyzed
     * @param pTotalTerms:     Tables of the total frequency of terms.
     * @param textFrequency:   Table of the terms of the text analyzed.
     * @param pCategory:       Category analyzed.
     * @return Returns the distance of the term pTerm by category PCategoria
     */
    private static float distance(String pTerm, Hashtable<String, float[]> pTotalTerms, float textFrequency, Category pCategory) {

        // The first variable tracks the frequency of a term
        // Relating to a category
        // The second keeps track of frequency of a term for all
        // Categories
        float[] categoryFrequency = new float[3], textFrequency = new float[3];

        if (pCategory.existsTerm(pTerm)) { // if the term is in category
            // Its frequency in this category is equal to the frequency
            // KnowledgeBase
            // Plus frequency in the search text
            categoryFrequency = pCategory.getVal(pTerm);
            categoryFrequency[0] += textFrequency;
        } else {

            // Otherwise it is equal to the frequency of the term in the text
            // Analyzed
            categoryFrequency[0] = textFrequency;
        }
        if (PTotalTerms.get(pTerm) != null) { // if the term exists in
            // Table of the total frequency
            textFrequency = PTotalTerms.get(pTerm); // Get the value

            textFrequency[0] += textFrequency; // the total frequency is given
            // Frequency in the text
            // Analyzed
            // Plus any frequency stored in memory in the table
            // Total frequency

            return (categoryFrequency[0] / textFrequency[0]); // distance is
            // Equal to
            // Frequency in
            // Category
            // The total frequency divided by
        }
        return 0;
    }

    /**
     * Method for calculating the distance of a term from a category.
     * Used for training
     *
     * @param pTerm term to analyze
     * @param pCategory category from which you must calculate the distance
     * @param PTotalTerms total frequency table
     * @return Returns the distance of a term from a category
     */
    private static float distance(String pTerm, Category pCategory, Hashtable<String, float[]> PTotalTerms) {
        return ((pCategory.getVal(pTerm))[0] / (PTotalTerms.get(pTerm))[0]);
    }

/**
 * Implementation of a T-Norm function
 *
 * @param a first value
 * @param b the second value
 * @return returns the value calculated using a T-Norm function
 */
private static float tNorm(float a, float b) {
    return ((a * b) / ((2 - ((a + b) - (a * b)))));
    // Return Math.min(a, b);
    // Return a * b;
    // Return Math.max(0, a + b - 1);
}

/**
 * Implementation of an S-Norm function
 *
 * @param a first value
 * @param b the second value
 * @return returns the value calculated using an S-Norm function
 */
private static float sNorm(float a, float b) {
    return ((a + b) / (1 + a * b));
    // Return Math.max(a, b);
    // Return (a + b - (a * b));
    // Return Math.min(1, 1 + b);
}

/**
 * Calculation of similarity between a category and a given text input
 *
 * @param pTerm a Hashtable containing words. It must contain for each term values of importance and membership situated in a float Vector at positions 1 and 2.
 * @return returns a numeric value that indicates the similarity of a text with the category on which importance and membership values have been calculated
 */
private static float similarity(Hashtable<String, float[]> pTerm) {
    float sum = 0; // return value

    for (float[] val : pTerm.values()) { // for all elements of the table
        sum += (tNorm(val[1], val[0])) / (sNorm(val[1], val[0])); // performs sum of the values given by the division of T-Norm function with the S-Norm function made between relevance and membership
    }

    return sum;
}

/**
 * Method for the calculation of membership of a text to a category
 *
 * @param val the similarity of a text with a category
 * @param maxSimilarity the maximum similarity found
 * @return returns a value in the interval [0,1] that indicates the degree of membership of the text to the category
 */
private static float membership(float val, float maxSimilarity) {
    return (val / maxSimilarity);
}

/**
 * Method to remove special characters and convert all uppercase characters to lowercase
 *
 * @param pStr string to transform
 * @return returns the text in lowercase without special characters
 */
private static String replaceAndLower(String pStr) {
    pStr = pStr.toLowerCase();
    pStr = pStr.replace(",", "");
    pStr = pStr.replace(".", "");
    pStr = pStr.replace("!", "");
    pStr = pStr.replace("?", "");
    pStr = pStr.replace("'", "");

    return pStr;
}
/**
 * Method for retrieving the category to which a text belongs
 *
 * @param pDescription text to analyze
 * @return a string indicating the category
 * @throws RemoteException
 */
public static String calculateCategory(String pDescription) throws RemoteException {
    if (pDescription == null || pDescription.equals("")) return "NULL";
    String text = pDescription;
    // Hashtable of terms associated with the text portion. Will contain values of frequency, relevance, membership for each term
    Hashtable<String, float[]> textData = new Hashtable<>();
    // Hashtable of categories, each category will contain the value of similarity and text membership
    Hashtable<String, float[]> textCategory = new Hashtable<>();
    text = replaceAndLower(text); // delete special characters and convert uppercase to lowercase
    String[] textSplit = text.split(" ");
    for (int i = 0; i < textSplit.length; i++) { // for each word of the text
        float[] toPut = new float[3]; // value to assign to the string in the hash table
        float[] tmpVal; // temporary variable containing the values associated with the string if it already exists in the hash table
        if (textSplit[i].length() <= 3) continue; // delete undefined terms such as articles, prepositions, etc.
        if (exists(textSplit[i], textData)) { // if the term analyzed is present in the table of terms of the analyzed text
            // We get the value of frequency in the table and increment it by one
            tmpVal = textData.get(textSplit[i]);
            toPut[0] = tmpVal[0];
            toPut[0] += (float) 1 / textSplit.length;
        } else {
            // Otherwise initialize the frequency value to 1 over the total number of terms (relative frequency)
            toPut[0] = (float) 1 / textSplit.length;
        }
        // Insert the new entry in the table
        textData.put(textSplit[i], toPut);
    }
    
    // Attempt to open the knowledge base
    CategoryList list;
    try {
        list = openList();
    } catch (ClassNotFoundException e) { // error opening kb.sbt file
        throw new RemoteException("The knowledge base is missing or corrupt");
    } catch (Exception e) {
        throw new RemoteException("The knowledge base is missing or corrupt");
    }

    // Take from the knowledge base to the table of total term frequencies
    Hashtable<String, Float[]> termTotals = list.getTotalTerms();
    float maxSimilarity = -1; // holds the value of maximum similarity
    for (String categoryName : list.getCategories()) { // for all categories in the knowledge base
        float[] toPut = new float[3]; // value to assign to the string in the hash table
        for (Enumeration<String> val = textData.keys(); val.hasMoreElements();) { // for all elements of the table of terms of the text
            String term = val.nextElement(); // get the value of a term
            float[] tmp = textData.get(term);
            // Calculate distance and relevance
            tmp[1] = distance(term, termTotals, tmp[0], list.getCategory(categoryName));
            tmp[2] = relevance(tmp[1], list.getMaxDist());
            textData.put(term, tmp);
        }
        // Calculate the similarity after analyzing all the terms in a category
        toPut[0] = similarity(textData);
        textCategory.put(categoryName, toPut);
        // Update the value of maximum similarity if necessary
        if (maxSimilarity < toPut[0]) {
            maxSimilarity = toPut[0];
        }
    }

    for (String categoryName : list.getCategories()) { // for each category
        // Get the value of text similarity with the analyzed category
        float[] tmp = textCategory.get(categoryName);
        // Calculate text membership to the similarity
        tmp[1] = membership(tmp[0], maxSimilarity);
        // Save everything in the category table
        textCategory.put(categoryName, tmp);
    }

    return maxMembership(textCategory); // returns the name of the output category with the maximum membership degree
}

/**
 * Method to find the category with which the text has the highest degree of
 * Membership
 *
 * @param pTextCategory table of categories for the text
 * @return a string indicating the name of the category with which
 * the text has the highest degree of membership
 */
private static String maxMembership(Hashtable<String, float[]> pTextCategory) {
    String toReturn = null; // return value
    float max = -1; // Maximum value of membership

    for (Enumeration<String> keys = pTextCategory.keys(); keys.hasMoreElements();) {
        // For all categories in the table of categories for the text
        String category = keys.nextElement();
        // Get the values of similarity and membership associated with the category
        float[] tmp = pTextCategory.get(category);
        if (tmp[1] > max) {
            // If the degree of membership just updated is greater than the previous one, update max and toReturn
            toReturn = category;
            max = tmp[1];
        }
    }
    return toReturn;
}

/**
 * Method used to check whether a term is present in the table
 * of terms for the text
 *
 * @param pStr term to analyze
 * @param pTable table of terms for the text
 * @return true if the term exists, false otherwise
 */
private static boolean exists(String pStr, Hashtable<String, float[]> pTable) {
    try {
        if (pTable.get(pStr) != null) return true;
    } catch (NullPointerException e) {
        return false;
    }
    return false;
}

/**
 * Method used to retrieve the knowledge base
 *
 * @return an object representing the CategoryList type knowledge base
 * @throws IOException
 * @throws ClassNotFoundException
 */
private static CategoryList openList() throws IOException, ClassNotFoundException {
    File file = new File("kb.sbt"); // open the kb.sbt file
    FileInputStream kBaseStream = new FileInputStream(file); // creates a stream with the file
    ObjectInputStream kBaseObj = new ObjectInputStream(kBaseStream); // create a stream object with the file
    CategoryList toReturn;
    toReturn = (CategoryList) kBaseObj.readObject();
    // Extract the object and save it in the returned file
    return toReturn;
}
/**
 * Method used to create a file. Used in training.
 *
 * @param path string indicating the path in which to create the file
 * @return returns an ObjectOutputStream to the created file
 * @throws IOException
 */
private static ObjectOutputStream createFile(String path) throws IOException {
    ObjectOutputStream toReturn;
    File f = new File(path); // file is created
    if (f.exists()) f.delete();

    FileOutputStream fout = new FileOutputStream(path);
    toReturn = new ObjectOutputStream(fout); // create the stream

    return toReturn;
}

/**
 * Method used to create the knowledge base.
 *
 * @throws RemoteException
 */
public static void training() throws RemoteException {
    String[] categoryList = new String[4]; // array containing category names for analysis
    CategoryList list = new CategoryList();

    ObjectOutputStream listOut;
    try {
        // Try to create the file
        listOut = createFile("kb.sbt");
    } catch (Exception e) {
        throw new RemoteException("Error creating file kb.sbt");
    }

    categoryList[0] = "art";
    categoryList[1] = "cinema";
    categoryList[2] = "sport";

    for (int i = 0; i < 3; i++) {
        // for each category
        // Create a new object of type Category, which will contain all
        // Category data to be analyzed
        Category category = new Category(categoryList[i]);
        // If the inclusion of the category in the table of categories
        // Is not successful, throw an exception
        if (!list.addCategory(categoryList[i], category)) {
            throw new RemoteException("Error creating category data for " + categoryList[i]);
        }

        // Try to read from the folder containing the texts of a category
        // 100 sample tests
        for (int j = 1; j <= 100; j++) {
            // Path of the folder category
            String path = "kb/" + categoryList[i] + "/" + j;
            // Try to read the file j
            FileReader textReader;
            try {
                textReader = new FileReader(path);
            } catch (FileNotFoundException e) {
                // If the file does not exist, continue execution from
                // File j + 1
                continue;
            }

            Scanner scanner = new Scanner(textReader);
            while (scanner.hasNextLine()) {
                // Read the text file line by line
                String text = scanner.nextLine();
                text = replaceAndLower(text);
                String[] toIterate = text.split(" ");
                for (String term : toIterate) {
                    // For each term of the line
                    if (term.length() <= 3) continue; // remove undefined terms
                    float[] termVal, totalTermVal;
                    // If the term is present in the table of terms of the analyzed category
                    if (list.getCategory(categoryList[i]).existsTerm(term)) {
                        // Its frequency is equal to the value stored in
                        // Table plus one divided by the total number of
                        // Terms of the text
                        termVal = list.getCategory(categoryList[i]).getValue(term);
                        termVal[0] += (float) 1 / toIterate.length;
                        totalTermVal = list.getTerm(term);
                        totalTermVal[0] += (float) 1 / toIterate.length;
                    } else {
                        // Otherwise it is equal to one divided by the total number of words of text
                        termVal = new float[3];
                        totalTermVal = new float[1];
                        termVal[0] = (float) 1 / toIterate.length;
                        totalTermVal[0] = (float) 1 / toIterate.length;
                    }
                    // save the calculated values in the table of terms of the analyzed category
                    list.setTerm(term, totalTermVal);
                    list.getCategory(categoryList[i]).addTerm(term, termVal);
                    if (list.getMaxDist() < totalTermVal[0]) list.setMaxDist(totalTermVal[0]);
                }
            }
        }
    }

    for (String categoryName : list.getCategories()) {
        // for each category
        // the table of terms is flushed
        Hashtable<String, float[]> categoryTerms = list.getCategory(categoryName).getTerms();

        // all the terms are analyzed in the loaded terms table
        for (Enumeration<String> termEnum = categoryTerms.keys(); termEnum.hasMoreElements();) {
            // is calculated distance and relevance
            String term = termEnum.nextElement();
            float[] val = categoryTerms.get(term);
            val[1] = distance(term, list.getCategory(categoryName), list.getTermList());
            val[2] = relevance(val[1], list.getMaxDist());
        }
        // data is stored in the table of terms of the category
        list.getCategory(categoryName).setTerms(categoryTerms);
    }

    try {
        // writing the results of operations to files
        listOut.writeObject(list);
    } catch (Exception e) {
        throw new RemoteException("Error writing file");
    }
}
}







