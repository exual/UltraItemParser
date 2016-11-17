import java.io.File;
import java.util.ArrayList;

/**
 * Created by joel on 11/17/16.
 */
public interface IParser {

    Product parseFromFile(File incoming);

    boolean parseToFile(ArrayList<Product> incoming, File outgoing);

}
