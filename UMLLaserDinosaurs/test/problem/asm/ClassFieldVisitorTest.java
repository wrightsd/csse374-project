package problem.asm;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import problem_asm.ClassFieldVisitor;

public class ClassFieldVisitorTest {

	StringBuilder testBuilder;

	@Before
	public void setUp() throws IOException {
		testBuilder = new StringBuilder();
		ClassReader reader = new ClassReader("problem.asm.TestClass");
		ClassVisitor testFieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, testBuilder);
		reader.accept(testFieldVisitor, ClassReader.EXPAND_FRAMES);
	}

	@Test
	public void testNumberOfFields() {
		String[] fieldLines = testBuilder.toString().split("\\\\l");
		assertEquals(fieldLines.length, 3);
	}
	
	@Test
	public void testAccessModifiers(){
		String[] fieldLines = testBuilder.toString().split("\\\\l");
		//private
		assertEquals(fieldLines[0].charAt(0), '-');
		//public
		assertEquals(fieldLines[1].charAt(0), '+');
		//protected
		assertEquals(fieldLines[2].charAt(0), '#');
	}
	
	@Test
	public void testFieldNames(){
		String[] fieldLines = testBuilder.toString().split("\\\\l");
		
		assertTrue(fieldLines[0].contains("testField1"));
		assertTrue(fieldLines[1].contains("testField2"));
		assertTrue(fieldLines[2].contains("testField3"));
	}
	
	@Test
	public void testFieldTypes(){
		String[] fieldLines = testBuilder.toString().split("\\\\l");
		
		assertTrue(fieldLines[0].contains("int"));
		assertTrue(fieldLines[1].contains("String"));
		assertTrue(fieldLines[2].contains("char[]"));
	}

}
