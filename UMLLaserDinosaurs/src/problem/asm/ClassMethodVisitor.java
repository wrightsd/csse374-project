package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassMethodVisitor extends ClassVisitor {
	
	private StringBuilder builder;
	
	public ClassMethodVisitor(int api) {
		super(api);
	}

	public ClassMethodVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
	}
	
	public ClassMethodVisitor(int api, ClassVisitor decorated, StringBuilder builder) {
		super(api, decorated);
		this.builder = builder;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		// TODO: delete the line below
		Type[] argTypes = Type.getArgumentTypes(desc); 
		String[] classNames = new String[argTypes.length];
		for(int i = 0; i < argTypes.length; i++){
			classNames[i] = argTypes[i].getClassName();
		}
		String symbol="";
		if((access & Opcodes.ACC_PUBLIC) != 0){
			symbol = "+";
		}
		System.out.println("            method " + symbol +name + " " + Type.getReturnType(desc).getClassName() + Arrays.toString(argTypes));
		// TODO: create an internal representation of the current method and
		// pass it to the methods below
		// addAccessLevel(access);
		// addReturnType(desc);
		// addArguments(desc);
		// TODO: add the current method to your internal representation of the
		// current class
		// What is a good way for the code to remember what the current class
		// is?
		
		return toDecorate;
	}
}
