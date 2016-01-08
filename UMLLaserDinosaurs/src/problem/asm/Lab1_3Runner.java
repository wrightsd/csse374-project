package problem.asm;

import java.io.IOException;

public class Lab1_3Runner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String[] arguments = {"problem.WebsiteLoader"};
		/*String[] arguments = { "problem.AppLauncher", "problem.BackwardsTextPrinter", "problem.FileNamePrinter",
				"problem.Observer", "problem.Subject", "problem.TextLoader", "problem.WebsiteLoader",
				"problem.WordLoader" };*/
		DesignParser.parse(arguments, "./output/lab1-3_output.txt");
	}

}
