package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;

public class ClassDeclarationVisitor extends ClassVisitor {

	private StringBuilder builder;
	
	public ClassDeclarationVisitor(int arg0) {
		super(arg0);
	}
	
	public ClassDeclarationVisitor(int arg0, StringBuilder builder) {
		super(arg0);
		this.builder = builder;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		String[] classNameToWriteArray = name.split("/");
		String classNameToWrite = classNameToWriteArray[classNameToWriteArray.length-1];
		
		if(superName != null){
		builder.append(classNameToWrite + "->");
		String[] superClassNameToWriteArray = superName.split("/");
		String superClassNameToWrite = superClassNameToWriteArray[superClassNameToWriteArray.length-1];
		builder.append(superClassNameToWrite + "[arrowhead=\"onormal\", style=\"solid\"];\n");
		}
		
		for(String s: interfaces){
			builder.append(classNameToWrite + "->");
			String[] interfaceNameToWriteArray = s.split("/");
			String interfaceNameToWrite = interfaceNameToWriteArray[interfaceNameToWriteArray.length-1];
			builder.append(interfaceNameToWrite + "[arrowhead=\"onormal\", style=\"dashed\"];\n");
		}
		// TODO: construct an internal representation of the class for later use
		// by decorators
		super.visit(version, access, name, signature, superName, interfaces);
	}
}
