package problem.asm;

import java.io.IOException;

public class Lab5_1_Runner {
	
	public static void main(String[] args) throws IOException {
		String[] arguments = { "problem.client.App", "problem.client.IteratorToEnumerationAdapter", 
				"problem.lib.LinearTransformer"};
		DesignParser.parse(arguments, "./output/Week5_1_output.txt", "uml");
	}

}
