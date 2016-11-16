import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
	private ArrayList<Product> products;
	private GUI frame = null;

	public Controller(String path) {
		parseDirectory(path);
	}

	public Controller() {
		frame = new GUI(this);
		frame.setVisible(true);
	}

	public void parseDirectory(String path) {
		System.out.println("Inside parseDirectory");
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
