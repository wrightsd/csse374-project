package problem.asm;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import problem_asm.DesignParser;

public class AdapterDinosaurTest {
	String contents;

	@Before
	public void setup() throws IOException {
		String[] arguments = { "problem.asm.ClassDeclarationVisitor", "problem.asm.ClassFieldVisitor",
				"problem.asm.ClassMethodVisitor", "problem.asm.DesignParser",
				"problem.asm.InterfaceAbstractCheckVisitor", "problem.asm.MethodAssociationVisitor",
				"problem.asm.ClassAssociationVisitor", "problem.asm.UMLMaker", "problem.asm.DiagramMaker",
				"problem.asm.SequenceMaker", "problem.asm.NoSuchDiagramMaker", "problem.asm.BlacklistHolder",
				"problem.asm.ClassSequenceVisitor", "problem.asm.MethodSequenceVisitor",
				"problem.asm.SingletonClassVisitor", "problem.asm.ArbitraryNodeNames",
				"problem.asm.DecoratorClassVisitor", "problem.asm.DecorateRecursiveVisitor",
				"problem.asm.AdapterMethodVisitor", "problem.asm.MethodGetterVisitor",
				"problem.asm.AdapterManagementVisitor", "problem.asm.AdapterClassVisitor" };
		DesignParser.parse(arguments, "./test/problem/asm/adapter_test1_output.txt", "uml");
		FileInputStream file = new FileInputStream("./test/problem/asm/adapter_test1_output.txt");
		int i = file.read();
		while (i != -1) {
			contents += (char) i;
			i = file.read();
		}
		file.close();
	}

	@Test
	public void testUMLLaserDinosaursProjectAdapter() throws IOException {
		assertEquals(0, numOccurencesInString("fillcolor =firebrick2", contents));
		assertEquals(0, numOccurencesInString("\\<\\<adapter", contents));
		assertEquals(0, numOccurencesInString("\\<\\<adaptee", contents));
		assertEquals(0, numOccurencesInString("\\<\\<target", contents));

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
