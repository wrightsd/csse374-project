package problem.asm;

import java.io.IOException;

public class UMLLaserDinosaursRunner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String[] arguments = { "problem.asm.ClassDeclarationVisitor", "problem.asm.ClassFieldVisitor",
				"problem.asm.ClassMethodVisitor", "problem.asm.DesignParser",
				"problem.asm.InterfaceAbstractCheckVisitor" };
		DesignParser.parse(arguments, "./output/UMLLaserDinosaurs_output.txt");
	}

}
