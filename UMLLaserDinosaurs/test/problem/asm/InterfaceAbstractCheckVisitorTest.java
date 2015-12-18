package problem.asm;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class InterfaceAbstractCheckVisitorTest {

	StringBuilder testBuilder;

	@Before
	public void setUp() throws IOException {
		testBuilder = new StringBuilder();
		ClassReader reader = new ClassReader("problem.asm.TestInterface");
		ClassVisitor testIACheckVisitor = new InterfaceAbstractCheckVisitor(Opcodes.ASM5, testBuilder);
		reader.accept(testIACheckVisitor, ClassReader.EXPAND_FRAMES);
	}

	@Test
	public void testInterfaceLabel() {
		assertTrue(testBuilder.toString().contains("interface"));
	}

	@Test
	public void testAbstractLabel() {
		assertTrue(testBuilder.toString().contains("abstract"));
	}

}

