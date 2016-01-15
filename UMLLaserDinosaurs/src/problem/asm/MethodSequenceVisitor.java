package problem.asm;

import java.sql.Types;
import java.util.Arrays;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class MethodSequenceVisitor extends MethodVisitor {

	private StringBuilder classSequenceBuilder;
	private StringBuilder methodSequenceBuilder;
	private MethodVisitor visitor;

	public MethodSequenceVisitor(int arg0) {
		super(arg0);
	}

	public MethodSequenceVisitor(int asm5, MethodVisitor toDecorate, StringBuilder classSequenceBuilder,
			StringBuilder methodSequenceBuilder) {
		super(asm5, toDecorate);
		this.visitor = toDecorate;
		this.classSequenceBuilder = classSequenceBuilder;
		this.methodSequenceBuilder = methodSequenceBuilder;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String writeOwner = owner.replace("/", ".");
		String classTag = SequenceMaker.getClassTag(writeOwner);
		if (classTag.equals("")) {
			classTag = owner.replace("/", "");
			SequenceMaker.addClassTag(writeOwner, classTag);
			classSequenceBuilder.append(classTag + ":" + writeOwner + "\n");
		}

		String returnStringName = Type.getReturnType(desc).getClassName();
		if (!returnStringName.equals("")) {
			returnStringName += "=";
		}

		String overallParameterListString = "";
		Type[] parameterTypes = Type.getArgumentTypes(desc);
		for (int i = 0; i < parameterTypes.length - 1; i++) {
			String parameterStringName = parameterTypes[i].getClassName();
			overallParameterListString += parameterStringName;
		}

		String currentClass = DesignParser.getCurrentClass();
		String currentClassTag = SequenceMaker.getClassTag(currentClass);
		if (currentClassTag.equals("")) {
			currentClassTag = currentClass.replace(".", "");
			SequenceMaker.addClassTag(currentClass, currentClassTag);
			classSequenceBuilder.append(currentClassTag + ":" + currentClass + "\n");
		}

		methodSequenceBuilder.append(currentClassTag + ":" + returnStringName + classTag + "." + name + "("
				+ overallParameterListString + ")\n");

	}

	@Override
	public void visitTypeInsn(int opcode, String type) {

	}

}
