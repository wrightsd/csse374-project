package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassSequenceVisitor extends ClassVisitor {

	private int chosenDepth;
	private String chosenMethodName;
	private String[] chosenParams;
	private StringBuilder classSequenceBuilder;
	private StringBuilder methodSequenceBuilder;

	public ClassSequenceVisitor(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ClassSequenceVisitor(int opcode, int depth, String MethodName, String[] params,
			StringBuilder classSequenceBuilder, StringBuilder methodSequenceBuilder) {
		super(opcode);
		this.chosenDepth = depth;
		this.chosenMethodName = MethodName;
		this.chosenParams = params;
		this.classSequenceBuilder = classSequenceBuilder;
		this.methodSequenceBuilder = methodSequenceBuilder;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		Type[] argTypes = Type.getArgumentTypes(desc);
		String[] paramNames = new String[argTypes.length];
		for (int i = 0; i < argTypes.length; i++) {
			paramNames[i] = argTypes[i].getClassName();
		}
		if (name.equals(this.chosenMethodName) && Arrays.equals(paramNames, chosenParams)) {
			MethodVisitor methodSequence = new MethodSequenceVisitor(Opcodes.ASM5, toDecorate, classSequenceBuilder,
					methodSequenceBuilder);
			return methodSequence;
		}

		return toDecorate;

	}

}
