
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import javax.json.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;


/**
 * Created by joel on 10/5/16.
 *
 *
 * I don't like that I've put all the file IO in this class - the next
 * iteration might break this class down to only deal in streams, and
 * have another class handle the file-based stuff - Joel
 *
 * TODO: move the file IO from here into its own class
 */
public class ProductParser {
    static final String JSON_PROD_NAME = "product_name";
    static final String JSON_ID_NAME = "product_id";
    static final String XML_PROD_NAME = "Product";
    static final String XML_PROD_ID = "identifier";

    private Controller controller;

    public ProductParser(Controller controller) {
        this.controller = controller;
    }

    /**
     * This method gabs product files from a directory and parses them
     * and stores them in an array list and if it fails it returns an error.
     * @param directory The directory to be parsed
     * @return ArrayList of Product objects or null if an error
     */
    public ArrayList<Product> parseDirectory(String directory) {
        File dir = new File(directory);
        ArrayList<Product> products = new ArrayList<>();

        if(dir.isDirectory()) {
            for(File file:dir.listFiles()) {
                Product product = parseFile(file);
                // parseFile returns a null object if it fails, so don't add any null products to list
                if( product != null ) products.add(product);
            }
        }
        else {
            controller.outputError("Invalid parse directory: " + directory);
        }
        return products;
    }
    /**
     * This method decides whether  the content is either a .xml file or a .json file
     * and logs an error if the file is neither.
     * @param file File to be parsed.
     * @return Product object, null if file does not end with .xml or .json
     */
    public Product parseFile(File file) {
        if(file.getPath().endsWith(".xml")) {
            return parseXMLProduct(file);
        }
        else if(file.getPath().endsWith(".json")) {
            return parseJsonProduct(file);
        }
        else {
            controller.outputError("File not parsed: " + file.getPath());
            return null;
        }
    }

    // TODO: the last catches for both of the parsers need to catch the real error: parsing a file with unexpected data
    /**
     * This method parses a json file.
     * @param file The json file to parse.
     * @return Product object, null if an error
     */
    private Product parseJsonProduct(File file) {
        Product newProd = null;

        try {
            JsonReader reader = Json.createReader(new FileReader(file));
            JsonObject prodJson = reader.readObject().getJsonObject("product_record");
            reader.close();

            String id = prodJson.getString(JSON_ID_NAME);
            String name = prodJson.getString(JSON_PROD_NAME);
            newProd = new Product(id, name);

        } catch (JsonException ex) {
            controller.outputError(ex.getMessage());
        } catch (FileNotFoundException ex) {
            controller.outputError("File not found: " + file + "(" + ex.getMessage() + ")");
        } catch (Exception ex) {
            controller.outputError("parseJsonProduct general exception with file " + file.getPath() + ":" + ex.getMessage());
        }
        return newProd;
    }

    /**
     * This method parses an xml file
     * @param file The file to be parsed
     * @return Product object or null if an error occurs
     */
    private Product parseXMLProduct(File file) {
        Product newProd = null;

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

            // we expect only a single product per file, so we only care about the first item, thus the .item(0)
            Element el = (Element) document.getElementsByTagName(XML_PROD_NAME).item(0);

            String id = el.getAttribute(XML_PROD_ID);
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
     * This method saves a list of products to a file.
     * @param products ArrayList of Product objects
     * @param file File to be saved
     * @return boolean success of the file save
     */
    public boolean writeProductsToFile(ArrayList<Product> products, File file) {
        try {
            JsonArrayBuilder prodBuilder = Json.createArrayBuilder();
            for(Product product:products) {
                prodBuilder.add(Json.createObjectBuilder()
                    .add(JSON_PROD_NAME, product.getName())
                    .add(JSON_ID_NAME, product.getId()).build());
            }
            JsonObject productsObject = Json.createObjectBuilder().add("product_records", prodBuilder).build();

            JsonWriter writer = Json.createWriter(new FileOutputStream(file));
            writer.writeObject(productsObject);
            writer.close();
            return true;

        } catch (FileNotFoundException ex) {
            controller.outputError("File not found: " + file.getPath());
        }
        return false;
    }
}
