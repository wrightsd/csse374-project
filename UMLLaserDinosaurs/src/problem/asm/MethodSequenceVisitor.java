package problem.asm;

import java.io.IOException;
import java.sql.Types;
import java.util.Arrays;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class MethodSequenceVisitor extends MethodVisitor {

	private StringBuilder classSequenceBuilder;
	private StringBuilder methodSequenceBuilder;
	private MethodVisitor visitor;
	private int chosenDepth;

	public MethodSequenceVisitor(int arg0) {
		super(arg0);
	}

	public MethodSequenceVisitor(int asm5, MethodVisitor toDecorate, StringBuilder classSequenceBuilder,
			StringBuilder methodSequenceBuilder, int depth) {
		super(asm5, toDecorate);
		this.visitor = toDecorate;
		this.classSequenceBuilder = classSequenceBuilder;
		this.methodSequenceBuilder = methodSequenceBuilder;
		this.chosenDepth = depth;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String currentClass = DesignParser.getCurrentClass().replace("/", ".");
		String currentClassTag = SequenceMaker.getClassTag(currentClass);
		System.out.println(name);
		if (currentClassTag.equals("")) {
			currentClassTag = currentClass.replace(".", "");
			SequenceMaker.addClassTag(currentClass, currentClassTag);
			classSequenceBuilder.append(currentClassTag + ":" + currentClass + "[a]\n");
		}
		
		String writeOwner = owner.replace("/", ".");
		String classTag = SequenceMaker.getClassTag(writeOwner);
		if (classTag.equals("")) {
			classTag = owner.replace("/", "");
			SequenceMaker.addClassTag(writeOwner, classTag);
			classSequenceBuilder.append(classTag + ":" + writeOwner + "[a]\n");
		}

		String returnStringName = Type.getReturnType(desc).getClassName();
		if (!returnStringName.equals("")) {
			returnStringName += "=";
		}

		String overallParameterListString = "";
		Type[] parameterTypes = Type.getArgumentTypes(desc);
		for (int i = 0; i < parameterTypes.length; i++) {
			String parameterStringName = parameterTypes[i].getClassName();
			if (overallParameterListString.length() > 0) {
				overallParameterListString += ", ";
			}
			overallParameterListString += parameterStringName;
		}

		methodSequenceBuilder.append(currentClassTag + ":" + returnStringName + classTag + "." + name + "("
				+ overallParameterListString + ")\n");
		if(this.chosenDepth>0){
			int depthToPassIn = chosenDepth -1;
			int sizeOfArgs = 3+parameterTypes.length;
			String[] args = new String[sizeOfArgs];
			args[0]=Integer.toString(depthToPassIn);
			args[1]=owner;
			args[2]=name;
			for(int i=0;i<parameterTypes.length;i++){
				args[i+3]=parameterTypes[i].getClassName().replace("/", ".");
			}
				
			try {
				SequenceMaker.recursiveSequenceGenerator(args, this.classSequenceBuilder, this.methodSequenceBuilder);
			} catch (IOException e) {
			}
		}

	}

}
