package problem.asm;

import java.io.IOException;

public class SequenceMakerSequenceDiagramRunner {
	
	public static void main(String[] args) throws IOException {
		String[] arguments = { "0", "problem.asm.SequenceMaker", "generateDiagramText",
				"java.lang.String[]"};
		DesignParser.parse(arguments, "./output/maker_sequence_output.txt", "seq");
	}

}
