package problem.asm;

import java.io.IOException;

public class TestRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = {"problem.sprites.ISprite"};
		DesignParser.parse(arguments, "./output/sample_output.txt", "uml");
	}

}
