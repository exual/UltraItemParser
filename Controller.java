import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
	private ArrayList<Product> products;
	private GUI frame = null;

	// this is not the right place for these!
	// I just don't know where is the right place yet. Joel.
	static final String JSON_INCOMING_PARENT_NAME = "product_record";
	static final String JSON_OUTGOING_PARENT_NAME = "product_records";
	static final String JSON_PROD_NAME = "product_name";
	static final String JSON_ID_NAME = "product_id";
	static final String XML_PROD_NAME = "Product";
	static final String XML_PROD_ID = "identifier";


	public Controller(String path) {
		parseDirectory(path);
	}

	public Controller() {
		frame = new GUI(this);
		frame.setVisible(true);
	}

	public void parseDirectory(String path) {
		ProductParser pp = new ProductParser(this);
		File file;
		Boolean success;
		Date date= new Date();

		products = pp.parseDirectory(path);

		for(Product product:products) {
			outputResult(product.toString());
		}

		//Outputs to the path of the parsed directory
		File dir = new File(path);
		file = new File(dir, "output.json");

		success = pp.writeProductsToFile(products, file);
		if (success) {
			outputError(new Timestamp(date.getTime()) + ": Success!\n" +
				"The location of the output file is:\n" + file.toString());
		}

	}

	public void outputResult(String message) {
		message += "\n";
		if(frame == null) {
			System.out.println(message);
		}
		else {
			frame.outputResult(message);
		}
	}

	public void outputError(String message) {
		message += "\n";
		if(frame == null) {
			System.err.println(message);
		}
		else {
			frame.outputError(message);
		}
	}

}
