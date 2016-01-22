package problem.asm;

import java.io.IOException;

public class TestRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "java.util.Calendar", "java.lang.Runtime", "java.awt.Desktop",
				"java.io.FilterInputStream", "problem.asm.EagerSingletonSample", "problem.asm.LazySingletonSample" };
		DesignParser.parse(arguments, "./output/sample_output.txt", "uml");
	}

}
