import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.net.URISyntaxException;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * This class implements the mechanism for context-sensitive help.
 *
 * @version 0.2
 * @author Mario Gallo
 *
 * 2007 eTour Project - Copyright by DMI SE @ SA Lab --
 * University of Salerno
 */
public class HelpManager extends MouseAdapter {
    private HashMap<String, String> dictionary;
    private JTextComponent destination;
    private String previousMessage;

    /**
     * The constructor creates a new instance of the class for managing online help files
     * from the specified content and the component in which it appears.
     *
     * @param pPath String - the path of the help file.
     * @param pComponent JTextComponent - the component in which to display the help.
     * @throws FileNotFoundException - if the help file is not found.
     */
    public HelpManager(String pPath, JTextComponent pComponent) throws FileNotFoundException {
        dictionary = new HashMap<>();
        destination = pComponent;
        File file = null;
        try {
            file = new File(getClass().getResource(pPath).toURI());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
        StringTokenizer tokenizer;
        while (scanner.hasNext()) {
            tokenizer = new StringTokenizer(scanner.nextLine(), "##");
            dictionary.put(tokenizer.nextToken(), tokenizer.nextToken());
        }
        scanner.close();
    }

    /**
     * This method handles the event when the mouse pointer enters the component.
     *
     * @param pMouseEnteredEvent MouseEvent - The event generated by the mouse.
     */
    @Override
    public void mouseEntered(MouseEvent pMouseEnteredEvent) {
        previousMessage = destination.getText();
        JComponent component = (JComponent) pMouseEnteredEvent.getComponent();
        String help = dictionary.get(component.getName());
        destination.setText(help);
        destination.repaint();
    }

    /**
     * This method handles the event when the mouse pointer exits a component.
     *
     * @param pMouseExitedEvent MouseEvent - The event generated by the mouse.
     */
    @Override
    public void mouseExited(MouseEvent pMouseExitedEvent) {
        destination.setText(previousMessage);
        destination.repaint();
    }
}