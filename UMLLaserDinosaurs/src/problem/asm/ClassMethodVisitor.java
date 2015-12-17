package problem.asm;

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
		for (int i = 0; i < argTypes.length; i++) {
			String[] classNameSplitArray = argTypes[i].getClassName().split("[.]");
			classNames[i] = classNameSplitArray[classNameSplitArray.length - 1];
		}
		// TODO: create an internal representation of the current method and
		// pass it to the methods below
		// addAccessLevel(access);
		// addReturnType(desc);
		// addArguments(desc);
		// TODO: add the current method to your internal representation of the
		// current class
		// What is a good way for the code to remember what the current class
		// is?
		String type = Type.getReturnType(desc).getClassName();
		// TODO: delete the line below
		if (access == Opcodes.ACC_PUBLIC) {
			builder.append("+");
		} else if (access == Opcodes.ACC_PROTECTED) {
			builder.append("#");
		} else { // method is private
			builder.append("-");
		}
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
		builder.append(name + "(" + argsToPrint + ")" + " : " + type + "\\l");

		return toDecorate;
	}
}
