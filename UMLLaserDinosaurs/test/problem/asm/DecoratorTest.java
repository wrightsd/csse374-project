package problem.asm;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class DecoratorTest {
	public String contents;

	@Before
	public void setup() throws IOException {
		String[] arguments = { "problem.DecryptionInputStream", "problem.EncryptionOutputStream", "problem.IDecryption",
				"problem.IEncryption", "problem.SubstitutionCipher", "problem.TextEditorApp" };
		DesignParser.parse(arguments, "./test/problem/asm/decorator_test_output.txt", "uml");

		FileInputStream file = new FileInputStream("./test/problem/asm/decorator_test_output.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
		file.close();
	}

	@Test
	public void testDecoratorsCreated() {
		assertTrue(contents.contains("chartreuse"));
		assertTrue(contents.contains("\\<\\<decorator"));
		assertTrue(contents.contains("\\<\\<component"));
	}

	@Test
	public void testCorrectNumberOfDecoratorsAndComponents() {
		assertEquals(6, numOccurencesInString("chartreuse", contents));
		assertEquals(4, numOccurencesInString("\\<\\<decorator", contents));
		assertEquals(2, numOccurencesInString("\\<\\<component", contents));

		assertTrue(contents.contains("label = \"java.io.OutputStream\\n\\<\\<component"));
		assertTrue(contents.contains("label = \"java.io.FilterOutputStream\\n\\<\\<decorator"));
	}

	private int numOccurencesInString(String sub, String full) {
		int num = 0;
		int lastIndex = 0;
		int currentSpot = 0;
		while (lastIndex != -1) {
			lastIndex = full.substring(currentSpot).indexOf(sub);

			if (lastIndex != -1) {
				num++;
				currentSpot += lastIndex + sub.length();
			}
		}
		return num;
	}
}
