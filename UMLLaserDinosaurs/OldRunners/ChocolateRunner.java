package problem.asm;

import java.io.IOException;

public class ChocolateRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "problem.asm.ChocolateBoiler" };
		DesignParser.parse(arguments, "./output/chocolate_output.txt", "uml");
	}

}
