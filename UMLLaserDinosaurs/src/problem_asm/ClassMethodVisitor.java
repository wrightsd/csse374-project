package problem_asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassMethodVisitor extends ClassVisitor {

	private StringBuilder builder;

	public ClassMethodVisitor(int api) {
		super(api);
	}

	public ClassMethodVisitor(int api, StringBuilder builder) {
		super(api);
		this.builder = builder;
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
		ArrayList<String> listOfMethods = new ArrayList<String>();
		Type[] argTypes = Type.getArgumentTypes(desc);
		String[] classNames = new String[argTypes.length];
		for (int i = 0; i < argTypes.length; i++) {
			classNames[i] = argTypes[i].getClassName();
		}
		
		String type = Type.getReturnType(desc).getClassName();

		String argsToPrint = Arrays.toString(classNames);
		if (argsToPrint.length() > 2) {
			argsToPrint = argsToPrint.substring(1, argsToPrint.length() - 1);
		} else {
			argsToPrint = "";
		}
		// TODO: Will get current class and display a constructor instead
		if (name.equals("<init>")) {
			name = "init";
		}
		if (name.equals("<clinit>")){
			return toDecorate;
		}
		listOfMethods.add(name);
		
		if ((access & Opcodes.ACC_PUBLIC) > 0) {
			builder.append("+");
		} else if ((access & Opcodes.ACC_PROTECTED) > 0) {
			builder.append("#");
		} else { // method is private
			builder.append("-");
		}

		builder.append(name + "(" + argsToPrint + ")" + " : " + type + "\\l");

		listOfMethods.add(argsToPrint);
		listOfMethods.add(type);
		UMLMaker.addMethodsToList(DesignParser.getCurrentClass(), listOfMethods);
		
		return toDecorate;
	}
}
