package problem_asm;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
	static JButton loadButtonField;

	public static void main(String[] args) {

		JFrame configWindow = new JFrame();
		configWindowField = configWindow;
		JPanel configLoadPanel = new JPanel();
		configLoadPanelField = configLoadPanel;
		JButton loadButton = new JButton("Load Config File");
		loadButtonField = loadButton;

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
		configLoadPanelField.removeAll();
		configLoadPanelField.setLayout(new BoxLayout(configLoadPanelField, BoxLayout.X_AXIS));
		JPanel checkboxes = new JPanel();
		checkboxes.setLayout(new BoxLayout(checkboxes, BoxLayout.Y_AXIS));
		for (int i = 0; i < patterns.size(); i++) {
			JLabel pattern = new JLabel(patterns.get(i));
			checkboxes.add(pattern);
			HashSet<String> patternsDetected = new HashSet<String>();
			for (String className : UMLMaker.getPatternLists().keySet()) {
				for (String[] classPatterns : UMLMaker.getPatternLists().get(className)) {
					if (classPatterns[1].compareToIgnoreCase(patterns.get(i)) == 0) {
						patternsDetected.add(classPatterns[2]);
					}
				}
			}
			for (String s : patternsDetected) {
				JCheckBox box = new JCheckBox(s);
				checkboxes.add(box);
			}
		}
		configLoadPanelField.add(checkboxes);
		configLoadPanelField.add(new JSeparator(SwingConstants.VERTICAL));
		String name = outputDirectory + "output.png";
		name = name.replace('\\', '/');
		ImageIcon icon;

		icon = new ImageIcon(name);
		JLabel diagram = new JLabel();
		diagram.setIcon(icon);
		configLoadPanelField.add(diagram);
		configWindowField.pack();
		configLoadPanelField.setVisible(true);
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

		Runtime runTimeEnvironment = Runtime.getRuntime();
		Process showingProcess = runTimeEnvironment
				.exec("cmd /c \"" + dotPath + "\" -Tpng " + inputFilePath + " > " + outputFilePath);

		showingProcess.waitFor();
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
				// System.out.println(line);
				file.close();
				br.close();
				throw new UnsupportedOperationException();
			}
		}
		file.close();
		br.close();
	}

}
