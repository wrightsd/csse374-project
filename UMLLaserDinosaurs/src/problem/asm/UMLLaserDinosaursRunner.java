package problem.asm;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class UMLLaserDinosaursRunner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String[] arguments = { "problem.asm.ClassDeclarationVisitor", "problem.asm.ClassFieldVisitor",
				"problem.asm.ClassMethodVisitor", "problem.asm.DesignParser",
				"problem.asm.InterfaceAbstractCheckVisitor", "problem.asm.MethodAssociationVisitor",
				"problem.asm.ClassAssociationVisitor" };
		DesignParser.parse(arguments, "./output/UMLLaserDinosaurs_output.txt");
	}

}
