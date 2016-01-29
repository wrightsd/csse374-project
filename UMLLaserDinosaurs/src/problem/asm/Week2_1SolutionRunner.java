package problem.asm;

import java.io.IOException;

public class Week2_1SolutionRunner {

	public static void main(String[] args) throws IOException {
		String[] arguments = { "problem.DecryptionInputStream", "problem.EncryptionOutputStream", "problem.IDecryption",
				"problem.IEncryption", "problem.SubstitutionCipher", "problem.TextEditorApp" };
		DesignParser.parse(arguments, "./output/Week2_1_output.txt", "uml");
	}

}
