package problem_asm;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class MainRunner {

	static String inputFolder;
	static ArrayList<String> inputClasses;
	static String outputDirectory;
	static String dotPath;
	static ArrayList<String> patterns;

	static JFrame configWindowField;
	static JPanel configLoadPanelField;
	static JPanel imageDisplayPane;
	static JButton loadButtonField;
	static JMenuBar menu;

	static ArrayList<String[]> listOfSelectedClasses = new ArrayList<String[]>();
	static HashMap<String, ArrayList<String[]>> patternLists;
	private static JLabel diagram = new JLabel();
	private static JScrollPane imageScrollPane;
	private static JScrollPane scrollPane;
	private static ImageIcon icon;

	public static void main(String[] args) {

		JFrame configWindow = new JFrame();
		configWindowField = configWindow;
		configWindowField.setBounds(0, 0, 1920, 1080);
		configWindowField.setPreferredSize(new Dimension(1920, 1080));
		JPanel configLoadPanel = new JPanel();
		configLoadPanelField = configLoadPanel;
		JButton loadButton = new JButton("Load Config File");
		loadButtonField = loadButton;
		
		menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem restartItem = new JMenuItem("Restart");
		restartItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				configWindow.dispose();
				listOfSelectedClasses.clear();
				patternLists.clear();
				patterns.clear();
				MainRunner.main(args);
			}
			
		});
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpItem = new JMenuItem("Instructions");
		helpItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (Desktop.isDesktopSupported()) {
						File pdf = new File("docs/projectAPI.pdf");
						Desktop.getDesktop().open(pdf);
					} else {
						System.err.println("Error: Cannot open pdf file");
					}
				} catch (Exception x) {
					x.printStackTrace();
				}
			}});
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (Desktop.isDesktopSupported()) {
						File pdf = new File("docs/About.pdf");
						Desktop.getDesktop().open(pdf);
					} else {
						System.err.println("Error: Cannot open pdf file");
					}
				} catch (Exception x) {
					x.printStackTrace();
				}
			}});

		fileMenu.add(restartItem);
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);
		menu.add(fileMenu);
		menu.add(helpMenu);

		configWindow.setJMenuBar(menu);
		
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser configSelector = new JFileChooser();
				int returnVal = configSelector.showOpenDialog((Component) e.getSource());

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = configSelector.getSelectedFile();
					try {
						processConfig(file, loadButton);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}

			}

		});

		configLoadPanel.add(loadButton);
		configWindow.add(configLoadPanel);

		configWindow.pack();
		configWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		configWindow.setVisible(true);
	}

	private static void displayResults() throws Exception {
		configWindowField.remove(configLoadPanelField);
		configWindowField.revalidate();
		configWindowField.repaint();
		loadCheckBoxes();
	}

	private static void loadCheckBoxes() {
		configWindowField.getContentPane().setLayout(new BoxLayout(configWindowField.getContentPane(), BoxLayout.X_AXIS));
		
		JPanel checkboxes = new JPanel();
		checkboxes.setBounds(0, 0, configWindowField.getWidth()/4, configWindowField.getHeight());
		checkboxes.setPreferredSize(new Dimension(configWindowField.getWidth()/4, configWindowField.getHeight()));
		checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
		for (int i = 0; i < patterns.size(); i++) {
			JLabel pattern = new JLabel(patterns.get(i));
			checkboxes.add(pattern);
			HashSet<String> patternsDetected = new HashSet<String>();
			for (String className : patternLists.keySet()) {
				for (String[] classPatterns : patternLists.get(className)) {
					if (classPatterns[1].compareToIgnoreCase(patterns.get(i)) == 0) {
						patternsDetected.add(classPatterns[2]);
					}
				}
			}
			for (String s : patternsDetected) {
				JCheckBox box = new JCheckBox(s);
				box.setName(s + "," + patterns.get(i));
				box.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						String[] list = box.getName().split(",");
						if (box.isSelected()) {
							listOfSelectedClasses.add(list);
						} else {
							for (int i = 0; i < listOfSelectedClasses.size(); i++) {
								String[] current = listOfSelectedClasses.get(i);
								if (current[0].equals(list[0]) && current[1].equals(list[1])) {
									listOfSelectedClasses.remove(i);
									break;
								}
							}
						}
						// for (String[] arr : listOfSelectedClasses) {
						// System.out.print(Arrays.toString(arr) + "\t");
						// }
						// System.out.println("");
						try {
							MainRunner.updateImage();
						} catch (Exception e) {
						}
					}

				});
				checkboxes.add(box);
			}
		}
		JScrollPane checkboxScroll = new JScrollPane(checkboxes);
		configWindowField.getContentPane().add(checkboxScroll);
//		configWindowField.add(new JSeparator(SwingConstants.VERTICAL));

		diagram.setIcon(null);
		scrollPane = new JScrollPane(diagram);
		configWindowField.getContentPane().add(scrollPane);

		configWindowField.revalidate();
		configWindowField.repaint();

	}

	private static void displayImage() {
		
		String name = outputDirectory + "output.png";
		name = name.replace('\\', '/');

		icon = new ImageIcon(name);
		diagram.setText("");
		diagram.setIcon(icon);
		((ImageIcon) diagram.getIcon()).getImage().flush();
		
		imageScrollPane.revalidate();
		imageScrollPane.repaint();

		configWindowField.revalidate();
		configWindowField.repaint();
	}

	protected static void updateImage() throws Exception {
		if (listOfSelectedClasses.size() < 1) {
			diagram.setIcon(null);
			diagram.setText("");
			return;
		}
		diagram.setIcon(null);
		diagram.setText("Now loading diagram");
		ArrayList<String> arguments = new ArrayList<String>();
		HashMap<String, ArrayList<String[]>> patternLists = UMLMaker.getPatternLists();
		for (String key : patternLists.keySet()) {
			for (String[] arr : patternLists.get(key)) {
				for (String[] selected : listOfSelectedClasses) {
					if (arr[1].equals(selected[1]) && arr[2].equals(selected[0])) {
						arguments.add(key);
					}
				}
			}
		}

		String inputFilePath = outputDirectory + "output.txt";
		String outputFilePath = outputDirectory + "output.png";

		DesignParser.parse((String[]) arguments.toArray(new String[arguments.size()]), inputFilePath, "uml", patterns);

		Runtime runTimeEnvironment = Runtime.getRuntime();
		Process showingProcess = runTimeEnvironment
				.exec("cmd /c \"" + dotPath + "\" -Tpng " + inputFilePath + " > " + outputFilePath);

		showingProcess.waitFor();
		displayImage();
	}

	private static JProgressBar displayLoadingBar(JButton loadButton, String fileName) {
		JProgressBar bar = new JProgressBar();
		JLabel nameOfFile = new JLabel("Loading: " + fileName);
		configLoadPanelField.remove(loadButton);
		configLoadPanelField.setLayout(new BoxLayout(configLoadPanelField, BoxLayout.Y_AXIS));
		configLoadPanelField.add(nameOfFile);
		configLoadPanelField.add(bar);
		configWindowField.pack();
		configLoadPanelField.setVisible(true);
		return bar;

	}

	private static void processConfig(File configFile, JButton loadButton) throws Exception {
		JProgressBar bar = displayLoadingBar(loadButton, configFile.getName());

		updateLoadBar(bar, 25);

		parseConfiguration(configFile.getPath());
		if (patterns == null || dotPath == null || (inputClasses == null && inputFolder == null)
				|| outputDirectory == null) {
			System.out.println("Configuration incorrect.");
			return;
		}

		String inputFilePath = outputDirectory + "output.txt";
		String outputFilePath = outputDirectory + "output.png";

		ArrayList<String> arguments = new ArrayList<String>();

		if (inputFolder != null) {
			File classFolder = new File(inputFolder);
			File[] packageArray = classFolder.listFiles();
			String[] packageArguments = new String[packageArray.length];
			for (int i = 0; i < packageArray.length; i++) {
				packageArguments[i] = packageArray[i].getName();
			}
			for (int i = 0; i < packageArguments.length; i++) {
				File packageFolder = new File(inputFolder + "\\" + packageArguments[i]);
				File[] classArray = packageFolder.listFiles();
				for (int j = 0; j < classArray.length; j++) {
					arguments.add(packageArguments[i] + "." + classArray[j].getName().split(".java")[0]);
				}
			}
		}
		if (inputClasses != null) {
			arguments.addAll(inputClasses);
		}
		// String[] arguments = { "problem.AppLauncher",
		// "problem.BackwardsTextPrinter", "problem.FileNamePrinter",
		// "problem.Observer", "problem.Subject", "problem.TextLoader",
		// "problem.WebsiteLoader",
		// "problem.WordLoader" };
		DesignParser.parse((String[]) arguments.toArray(new String[arguments.size()]), inputFilePath, "uml", patterns);

		patternLists = UMLMaker.getPatternLists();

		displayResults();
	}

	private static void updateLoadBar(JProgressBar bar, int value) {
		bar.setValue(value);

	}

	private static void parseConfiguration(String fileName) throws Exception {
		String line;
		FileInputStream file = new FileInputStream(fileName);
		InputStreamReader r = new InputStreamReader(file);
		BufferedReader br = new BufferedReader(r);
		while ((line = br.readLine()) != null) {
			if (line.startsWith("input-folder")) {
				if (line.length() > 13) {
					inputFolder = line.substring(13);
				}
			} else if (line.startsWith("input-classes")) {
				if (line.length() > 14) {
					inputClasses = new ArrayList<String>();
					for (String s : line.substring(14).split(",")) {
						inputClasses.add(s);
					}
				}
			} else if (line.startsWith("output-directory")) {
				if (line.length() > 17) {
					outputDirectory = line.substring(17);
				}
			} else if (line.startsWith("dot-path")) {
				dotPath = line.substring(9);
			} else if (line.startsWith("patterns")) {
				if (line.length() > 9) {
					patterns = new ArrayList<String>();
					for (String s : line.substring(9).split(",")) {
						patterns.add(s);
					}
				}
			} else {
				String[] fieldSet = line.split("-");
				UMLMaker.setFieldIndicator(fieldSet[0], fieldSet[1], fieldSet[2]);
			}
		}
		file.close();
		br.close();
	}

}
