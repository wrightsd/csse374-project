package problem.asm;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class ClassDeclarationVisitorTest {

	StringBuilder testBuilder;

	@Before
	public void setUp() throws IOException {
		testBuilder = new StringBuilder();
		ClassReader reader = new ClassReader("problem.asm.TestClass");
		ClassVisitor testDeclVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, testBuilder);
		reader.accept(testDeclVisitor, ClassReader.EXPAND_FRAMES);
	}

	@Test
	public void testExtends() {
		String[] lines = testBuilder.toString().split("\\n");
		assertTrue(lines[0].contains("Object"));
		assertTrue(lines[0].contains("solid"));
	}

	@Test
	public void testImplements() {
		String[] lines = testBuilder.toString().split("\\n");
		assertTrue(lines[1].contains("TestInterface"));
		assertTrue(lines[1].contains("dashed"));
	}

}
