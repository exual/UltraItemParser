import javax.json.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by joel on 11/17/16.
 */
public class JsonParser implements IParser {
    static final String INCOMING_PARENT_NAME = "product_record";
    static final String OUTGOING_PARENT_NAME = "product_records";
    static final String PROD_NAME = "product_name";
    static final String ID_NAME = "product_id";

    Controller controller;

    public JsonParser(Controller controller) {
        this.controller = controller;
    }

    @Override
    /**
     * This method parses a json file.
     * @param file The json file to parse.
     * @return Product object, null if an error
     */
    public Product parseFromFile(File file) {
        Product newProd = null;

        try {
            JsonReader reader = Json.createReader(new FileReader(file));
            JsonObject prodJson = reader.readObject().getJsonObject(INCOMING_PARENT_NAME);
            reader.close();

            String id = prodJson.getString(ID_NAME);
            String name = prodJson.getString(PROD_NAME);
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

    @Override
    /**
     * This method saves a list of products to a file.
     * @param products ArrayList of Product objects
     * @param file File to be saved
     * @return boolean success of the file save
     */
    public boolean parseToFile(ArrayList<Product> products, File file) {
        try {
            JsonArrayBuilder prodBuilder = Json.createArrayBuilder();
            for(Product product:products) {
                prodBuilder.add(Json.createObjectBuilder()
                        .add(PROD_NAME, product.getName())
                        .add(ID_NAME, product.getId()).build());
            }
            JsonObject productsObject = Json.createObjectBuilder().add(OUTGOING_PARENT_NAME, prodBuilder).build();

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
