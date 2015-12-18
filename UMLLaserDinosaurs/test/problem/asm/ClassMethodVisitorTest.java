package problem.asm;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class ClassMethodVisitorTest {

	StringBuilder testBuilder;

	@Before
	public void setUp() throws IOException {
		testBuilder = new StringBuilder();
		ClassReader reader = new ClassReader("problem.asm.TestClass");
		ClassVisitor testMethodVisitor = new ClassMethodVisitor(Opcodes.ASM5, testBuilder);
		reader.accept(testMethodVisitor, ClassReader.EXPAND_FRAMES);
	}

	@Test
	public void testNumberOfMethods() {
		String[] methodLines = testBuilder.toString().split("\\\\l");
		assertEquals(methodLines.length, 3);
	}

	@Test
	public void testAccessModifiers() {
		String[] fieldLines = testBuilder.toString().split("\\\\l");
		// public
		assertEquals(fieldLines[0].charAt(0), '+');
		// protected
		assertEquals(fieldLines[1].charAt(0), '#');
		// private
		assertEquals(fieldLines[2].charAt(0), '-');
	}

	@Test
	public void testMethodNames() {
		String[] fieldLines = testBuilder.toString().split("\\\\l");

		assertTrue(fieldLines[0].contains("init"));
		assertTrue(fieldLines[1].contains("testMethod1"));
		assertTrue(fieldLines[2].contains("testMethod2"));
	}

	@Test
	public void testReturnTypes() {
		String[] fieldLines = testBuilder.toString().split("\\\\l");

		assertTrue(fieldLines[0].contains("void"));
		assertTrue(fieldLines[1].contains("void"));
		assertTrue(fieldLines[2].contains("String"));
	}

	@Test
	public void testParameterTypes() {
		String[] fieldLines = testBuilder.toString().split("\\\\l");

		assertTrue(fieldLines[0].contains("(int)"));
		assertTrue(fieldLines[1].contains("()"));
		assertTrue(fieldLines[2].contains("(int)"));
	}

}
