package problem.asm;

import java.io.IOException;

public class CollectionsShuffleSequenceRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "0", "java.util.Collections", "shuffle",
				"java.util.List"};
		DesignParser.parse(arguments, "./output/shuffle_sequence_output.txt", "seq");
	}

}
