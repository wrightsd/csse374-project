package problem.asm;

import java.io.IOException;

public class UMLLaserDinosaursRunner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String[] arguments = { "problem.asm.ClassDeclarationVisitor", "problem.asm.ClassFieldVisitor",
				"problem.asm.ClassMethodVisitor", "problem.asm.DesignParser",
				"problem.asm.InterfaceAbstractCheckVisitor", "problem.asm.MethodAssociationVisitor",
				"problem.asm.ClassAssociationVisitor", "problem.asm.UMLMaker", "problem.asm.DiagramMaker",
				"problem.asm.SequenceMaker", "problem.asm.NoSuchDiagramMaker", "problem.asm.BlacklistHolder",
				"problem.asm.ClassSequenceVisitor", "problem.asm.MethodSequenceVisitor",
				"problem.asm.SingletonClassVisitor", "problem.asm.ArbitraryNodeNames" };
		DesignParser.parse(arguments, "./output/UMLLaserDinosaurs_output.txt", "uml");
	}

}
