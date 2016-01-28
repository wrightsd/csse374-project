package problem.asm;

import java.io.IOException;

public class TestRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = {"1", "problem.asm.TestForLoop" ,"testMethod"};
		DesignParser.parse(arguments, "./output/sample_output.txt", "seq");
	}

}
