package problem.asm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Lab1_3Runner {

	public static void main(String[] args) throws IOException {
		
		String dotPath = "C:/Program Files (x86)/Graphviz2.38/bin/dot";
		
		String inputFilePath = "C:/EclipseWorkspaces/csse374/csse374-project/UMLLaserDinosaurs/output/lab1-3_output.txt";
		
		String outputFilePath = "C:/EclipseWorkspaces/csse374/csse374-project/UMLLaserDinosaurs/output/lab1-3_output.png";
		
		File classFolder = new File(
				"C:/EclipseWorkspaces/csse374/csse374-project/UMLLaserDinosaurs/Lab1-3ClassesToUML");
		File[] packageArray = classFolder.listFiles();
		String[] packageArguments = new String[packageArray.length];
		for(int i = 0; i < packageArray.length; i++){
			packageArguments[i] = packageArray[i].getName();
		}
		ArrayList<String> arguments = new ArrayList<String>();
		for(int i = 0; i < packageArguments.length; i++){
			File packageFolder = new File(
					"C:/EclipseWorkspaces/csse374/csse374-project/UMLLaserDinosaurs/Lab1-3ClassesToUML/" + packageArguments[i]);
			File[] classArray = packageFolder.listFiles();
			for(int  j= 0; j < classArray.length; j++){
				arguments.add(packageArguments[i] + "." + classArray[j].getName().split(".java")[0]);
			}
		}
//		String[] arguments = { "problem.AppLauncher", "problem.BackwardsTextPrinter", "problem.FileNamePrinter",
//				"problem.Observer", "problem.Subject", "problem.TextLoader", "problem.WebsiteLoader",
//				"problem.WordLoader" };
		String gvFile = "./output/lab1-3_output.txt";
		DesignParser.parse((String[]) arguments.toArray(new String[arguments.size()]), gvFile, "uml");
		
		Runtime runTimeEnvironment = Runtime.getRuntime();
		Process showingProcess = runTimeEnvironment.exec("cmd /c \""+dotPath+"\" -Tpng "+inputFilePath+" > "+outputFilePath);
	}

}
