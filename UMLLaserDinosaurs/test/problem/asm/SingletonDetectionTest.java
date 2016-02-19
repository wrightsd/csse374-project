package problem.asm;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import problem_asm.DesignParser;

public class SingletonDetectionTest {
	public String contents;
	public String javaContents;

	@Before
	public void setup() throws IOException {
		String[] arguments = { "problem.asm.LazySingletonSample", "problem.asm.EagerSingletonSample" };
		DesignParser.parse(arguments, "./test/problem/asm/singleton_test_output.txt", "uml");

		FileInputStream file = new FileInputStream("./test/problem/asm/singleton_test_output.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
		file.close();
	}

	@Before
	public void setupJava() throws IOException {
		String[] arguments = { "java.util.Calendar", "java.lang.Runtime", "java.awt.Desktop",
				"java.io.FilterInputStream" };
		DesignParser.parse(arguments, "./test/problem/asm/singleton_test2_output.txt", "uml");

		FileInputStream file = new FileInputStream("./test/problem/asm/singleton_test2_output.txt");
		int i = file.read();
		while (i != -1) {
			javaContents += (char) i;
			i = file.read();
		}
		file.close();
	}

	@Test
	public void testSingletonsCreated() {
		assertTrue(contents.contains("blue"));
		assertTrue(contents.contains("\\<\\<Singleton"));
		assertTrue(contents.indexOf("blue") < contents.lastIndexOf("blue"));
		assertTrue(contents.indexOf("\\<\\<Singleton") < contents.lastIndexOf("\\<\\<Singleton"));
	}
	
	@Test
	public void testRuntimeSingletonOthersNot() {
		assertTrue(javaContents.contains("blue"));
		int num = 0;
		int lastIndex = 0;
		int currentSpot = 0;
		while (lastIndex != -1) {
			lastIndex = javaContents.substring(currentSpot).indexOf("blue");
			
			if (lastIndex != -1) {
				num++;
				currentSpot += lastIndex + 4;
			}
		}
		assertTrue(num == 1);
		assertTrue(javaContents.contains("Runtime [\nshape=\"record\",\ncolor =blue,"));
	}
}
