package problem.asm;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import problem_asm.DesignParser;

public class SequenceDiagramTest {
	public String contents;

	@Before
	public void setupShuffle() throws IOException {
		String[] arguments = { "1", "java.util.Collections", "shuffle", "java.util.List" };
		DesignParser.parse(arguments, "./test/problem/asm/shuffle_test_output.txt", "seq");

		FileInputStream file = new FileInputStream("./test/problem/asm/shuffle_test_output.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
		file.close();
	}

	@Test
	public void testCorrectClassesCreated() {
		assertTrue(contents.contains("javautilCollections:java.util.Collections[a]"));
		assertTrue(contents.contains("javautilRandom:java.util.Random[a]"));
		assertTrue(contents.contains("javalangSystem:java.lang.System[a]"));
		assertTrue(contents.contains("javautilList:java.util.List[a]"));
		assertTrue(contents.contains("javautilListIterator:java.util.ListIterator[a]"));
	}

	@Test
	public void testCorrectSequenceCalls() {
		assertTrue(contents.contains("javautilCollections:void=javautilRandom.<init>()"));
		assertTrue(contents.contains("javautilRandom:long=javautilRandom.seedUniquifier()"));
		assertTrue(contents.contains("javautilRandom:long=javalangSystem.nanoTime()"));
		assertTrue(contents.contains("javautilRandom:void=javautilRandom.<init>(long)"));
		assertTrue(
				contents.contains("javautilRandom:void=javautilCollections.shuffle(java.util.List, java.util.Random)"));
		assertTrue(contents.contains("javautilCollections:int=javautilList.size()"));
		assertTrue(contents.contains("javautilCollections:int=javautilRandom.nextInt(int)"));
		assertTrue(contents.contains("javautilCollections:void=javautilCollections.swap(java.util.List, int, int)"));
		assertTrue(contents.contains("javautilCollections:java.lang.Object[]=javautilList.toArray()"));
		assertTrue(contents.contains("javautilCollections:int=javautilRandom.nextInt(int)"));
		assertTrue(
				contents.contains("javautilCollections:void=javautilCollections.swap(java.lang.Object[], int, int)"));
		assertTrue(contents.contains("javautilCollections:java.util.ListIterator=javautilList.listIterator()"));
		assertTrue(contents.contains("javautilCollections:java.lang.Object=javautilListIterator.next()"));
		assertTrue(contents.contains("javautilCollections:void=javautilListIterator.set(java.lang.Object)"));
	}
	
	@Test
	public void depthFunctions() throws IOException {
		String newContents = "";
		String[] arguments = { "2", "java.util.Collections", "shuffle", "java.util.List" };
		DesignParser.parse(arguments, "./test/problem/asm/shuffle_test_output.txt", "seq");

		FileInputStream file = new FileInputStream("./test/problem/asm/shuffle_test_output.txt");
		int i = file.read();
		while (i != -1) {
			newContents += (char) i;
			i = file.read();
		}
		file.close();
		
		assertTrue(newContents.length() > contents.length());
	}
	
	@Test
	public void testDefaultDepthWorks() throws IOException {
		String newContents = "";
		String[] arguments = { "5", "java.util.Collections", "shuffle", "java.util.List" };
		DesignParser.parse(arguments, "./test/problem/asm/shuffle_d5_output.txt", "seq");

		FileInputStream file = new FileInputStream("./test/problem/asm/shuffle_d5_output.txt");
		int i = file.read();
		while (i != -1) {
			newContents += (char) i;
			i = file.read();
		}
		file.close();
		
		String defaultContents = "";
		String[] defaultArguments = { "java.util.Collections", "shuffle", "java.util.List" };
		DesignParser.parse(defaultArguments, "./test/problem/asm/shuffle_default_output.txt", "seq");

		FileInputStream defaultFile = new FileInputStream("./test/problem/asm/shuffle_default_output.txt");
		i = defaultFile.read();
		while (i != -1) {
			defaultContents += (char) i;
			i = defaultFile.read();
		}
		defaultFile.close();
		assertTrue(newContents.equals(defaultContents));
	}
}
