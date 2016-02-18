package problem.asm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Lab1_3Runner {
	static String inputFolder;
	static ArrayList<String> inputClasses;
	static String outputDirectory;
	static String dotPath;
	static ArrayList<String> patterns;

	public static void main(String[] args) throws Exception {
		parseConfiguration("./Configurations/Lab1-3Configuration");
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
		DesignParser.parse((String[]) arguments.toArray(new String[arguments.size()]), inputFilePath, "uml");

		Runtime runTimeEnvironment = Runtime.getRuntime();
		Process showingProcess = runTimeEnvironment
				.exec("cmd /c \"" + dotPath + "\" -Tpng " + inputFilePath + " > " + outputFilePath);
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
				//System.out.println(line);
				throw new UnsupportedOperationException();
			}
		}
		file.close();
	}

}
