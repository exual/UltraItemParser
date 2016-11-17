import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joel on 11/17/16.
 */
public class XMLParser implements IParser {
    static final String PROD_NAME = "Product";
    static final String PROD_ID = "identifier";

    Controller controller;

    public XMLParser(Controller controller) {
        this.controller = controller;
    }

    @Override
    /**
     * This method parses an xml file
     * @param file The file to be parsed
     * @return Product object or null if an error occurs
     */
    public Product parseFromFile(File file) {
        Product newProd = null;

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

            // we expect only a single product per file, so we only care about the first item, thus the .item(0)
            Element el = (Element) document.getElementsByTagName(PROD_NAME).item(0);

            String id = el.getAttribute(PROD_ID);
            String name = el.getTextContent();
            newProd = new Product(id, name);
        } catch (FileNotFoundException ex) {
            controller.outputError("File not found: " + file + "(" + ex.getMessage() + ")");
        } catch (ParserConfigurationException ex) {
            controller.outputError("ParserConfigurationException: " + ex.getMessage());
        } catch (SAXException ex) {
            controller.outputError("SAXException: " + ex.getMessage());
        } catch (IOException ex) {
            controller.outputError("IOException: " + ex.getMessage());
        } catch (Exception ex) {
            controller.outputError("parseXMLProduct general exception with file " + file.getPath() + ":" + ex.getMessage());
        }

        return newProd;
    }

    /**
     * This method is an inactive stub, for future use.
     * @param incoming ArrayList of products
     * @param outgoing File to write
     * @return True if parsing and file writing succeeds.
     */
    @Override
    public boolean parseToFile(ArrayList<Product> incoming, File outgoing) {
        return false;
    }
}
