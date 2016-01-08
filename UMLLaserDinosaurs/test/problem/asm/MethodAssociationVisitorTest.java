package problem.asm;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class MethodAssociationVisitorTest {

	private String contents;
	
	@Before
	public void setUp() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String[] argumentStringArray = {"problem.asm.TestClass2", "problem.asm.AnotherTestClass","problem.asm.TestClass","problem.asm.TestClassSuper"};
		
		DesignParser.parse(argumentStringArray,  "./test/problem/asm/test2Output.txt");;
		
		FileInputStream file = new FileInputStream("./test/problem/asm/test2Output.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
		
	}
	
	@Test
	public void testField() {
		assertTrue(contents.contains("-t : TestClass\\l"));
	}
	
	@Test
	public void testParameter(){
		assertTrue(contents.contains("AnotherTestClass->TestClass[arrowhead=\"ovee\", style=\"dashed\"]"));
	}
	
	@Test
	public void testNewInstance(){
		assertTrue(contents.contains("TestClass2->AnotherTestClass[arrowhead=\"ovee\", style=\"dashed\"]"));
	}
	
	@Test
	public void testAggregates(){
		assertTrue(contents.contains("TestClass2->String[arrowhead=\"ovee\", style=\"solid\"]"));
	}

}
