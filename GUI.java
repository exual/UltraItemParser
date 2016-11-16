	import javax.swing.*;
	import java.awt.event.*;
	import java.io.File;
	import java.util.*;
	import java.sql.Timestamp;

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;

	//Text fields to be accessed by GUI methods
	private JTextField openDirTextField;

	//Panes
	private JTextArea errorDisplay;
	private JTextArea resultsDisplay;

	//Directory Selector
	private JFileChooser dirSelector;

	//Controller which created me
	private Controller parent;

	public GUI(Controller parent) {
		super("Ultra Product Parser");
		this.parent = parent;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);

		// Base Pane
		JPanel basePane = new JPanel();
		setContentPane(basePane);
		basePane.setLayout(null);

		// Input Pane for buttons and text fields
		JPanel inputPane = new JPanel();
		inputPane.setBounds(10, 10, 674, 170);
		basePane.add(inputPane);
		inputPane.setLayout(null);

		// Labels
		JLabel openDirLabel = new JLabel("Selected Directory: ");
		openDirLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		openDirLabel.setBounds(16, 11, 140, 14);
		inputPane.add(openDirLabel);

		JLabel errorLabel = new JLabel("Messages: ");
		errorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		errorLabel.setBounds(16, 56, 140, 14);
		inputPane.add(errorLabel);

		JLabel resultLabel = new JLabel("Results: ");
		resultLabel.setHorizontalAlignment(SwingConstants.LEFT);
		resultLabel.setBounds(0, 155, 140, 14);
		inputPane.add(resultLabel);

		//TextField
		openDirTextField = new JTextField();
		openDirTextField.setBounds(160, 8, 350, 20);
		openDirTextField.setEditable(false);
		inputPane.add(openDirTextField);
		openDirTextField.setColumns(15);

		//Buttons
		JButton openDirButton = new JButton("Open Directory");
		openDirButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectDirectory();
			}
		});
		openDirButton.setBounds(540, 11, 125, 28);
		inputPane.add(openDirButton);

		JButton clearButton = new JButton("Clear");
		clearButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				clearFields();
			}
		});
		clearButton.setBounds(540, 51, 125, 28);
		inputPane.add(clearButton);

		JButton runButton = new JButton("Run");
		runButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				run();
			}
		});
		runButton.setBounds(540, 110, 125, 28);
		inputPane.add(runButton);

		//Error Text Area
		JScrollPane errorPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		errorPane.setBounds(160, 50, 350, 90);
		inputPane.add(errorPane);
		errorDisplay = new JTextArea();
		errorPane.setViewportView(errorDisplay);

		//Results Text Area
		JScrollPane resultPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultPane.setBounds(10, 185, 674, 265);
		basePane.add(resultPane);
		resultsDisplay = new JTextArea();
		resultPane.setViewportView(resultsDisplay);

		setLocationRelativeTo(null);
		setVisible(true);
	}


	//Select a directory
	public void selectDirectory ()	{
		//Get Time
		Date date= new Date();
		dirSelector = new JFileChooser();
		dirSelector.setCurrentDirectory(new File("."));
		dirSelector.setDialogTitle("Select folder with JSON/XML product files");
		//Directories Only
		dirSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirSelector.setAcceptAllFileFilterUsed(false);
		//Display selected folder
		if (dirSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			openDirTextField.setText("" + dirSelector.getSelectedFile());
		else
		  outputError(new Timestamp(date.getTime()) + ": Invalid Selection. See log.");
	}

	//Run parser
	public void run ()	{
		if(openDirTextField.getText().length() > 0) {
			parent.parseDirectory(openDirTextField.getText());
		}
		else {
			outputError("Directory must be set before parsing can be completed.");
		}

	}

	//Clear all fields for user convenience
	public void clearFields()	{
		errorDisplay.setText("");
		resultsDisplay.setText("");
	}

	public void outputResult(String message) {
		resultsDisplay.append(message);
	}

	public void outputError(String message) {
		errorDisplay.append(message);
	}

}

