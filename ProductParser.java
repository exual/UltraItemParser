
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
     * This method attempts to parse a single product from a file.
     * @param file File to be parsed.
     * @return Product object, null if file does not end with .xml or .json
     */
    public Product parseFile(File file) {
        IParser parser = getParser(file.getPath());
        if( parser == null ) {
            controller.outputError("File not parsed: " + file.getPath());
            return null;
        }
        return parser.parseFromFile(file);
    }

    /**
     * This method saves a list of products to a file.
     * @param products ArrayList of Product objects
     * @param file File to be saved
     * @return boolean success of the file save
     */
    public boolean writeProductsToFile(ArrayList<Product> products, File file) {
        IParser parser = getParser(file.getPath());
        if( parser == null ) {
            controller.outputError("No parser available for " + file.toString());
            return false;
        }
        return parser.parseToFile(products, file);
    }

    /**
     * This method guesses at the type of data to be parsed, based on
     * the passed string, which will usually be a file path.
     * @param type String with the type of data to be parsed.
     * @return IParser parser object.
     */
    public IParser getParser(String type) {
        if( type.endsWith("XML") || type.endsWith("xml") ) {
            return new XMLParser(controller);
        } else if( type.endsWith("JSON") || type.endsWith("json") ) {
            return new JsonParser(controller);
        } else {
            return null;
        }
    }
}
