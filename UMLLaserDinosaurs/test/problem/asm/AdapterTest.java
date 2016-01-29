package problem.asm;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class AdapterTest {
	public String contents;
	public String contents2;

	@Before
	public void setup() throws IOException {
		String[] arguments = { "problem.client.App", "problem.client.IteratorToEnumerationAdapter",
				"problem.lib.LinearTransformer" };
		DesignParser.parse(arguments, "./test/problem/asm/adapter_test_output.txt", "uml");

		FileInputStream file = new FileInputStream("./test/problem/asm/adapter_test_output.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
		file.close();
	}
	
	@Test
	public void testDecoratorsCreated() throws IOException {
		assertTrue(contents.contains("firebrick2"));
		assertTrue(contents.contains("\\<\\<adapter"));
		assertTrue(contents.contains("\\<\\<adaptee"));
		assertTrue(contents.contains("\\<\\<target"));
	}

	@Test
	public void testCorrectNumberOfDecoratorsAndComponents() throws IOException {
		assertEquals(3, numOccurencesInString("fillcolor =firebrick2", contents));
		assertEquals(1, numOccurencesInString("\\<\\<adapter", contents));
		assertEquals(1, numOccurencesInString("\\<\\<adaptee", contents));
		assertEquals(1, numOccurencesInString("\\<\\<target", contents));

		assertTrue(contents.contains("label = \"{problem.client.IteratorToEnumerationAdapter\\n\\<\\<adapter"));
		assertTrue(contents.contains("label = \"java.util.Enumeration\\n\\<\\<target"));
		assertTrue(contents.contains("label = \"java.util.Iterator\\n\\<\\<adaptee"));
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
