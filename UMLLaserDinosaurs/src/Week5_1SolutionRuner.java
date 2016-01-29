import java.io.IOException;

import problem.asm.DesignParser;

public class Week5_1SolutionRuner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "problem.client.App", "problem.client.IteratorToEnumerationAdapter",
				"problem.lib.LinearTransformer" };
		DesignParser.parse(arguments, "./output/Week5_1_output.txt", "uml");
	}

}
