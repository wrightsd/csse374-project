package problem_asm;

import org.objectweb.asm.MethodVisitor;

public class AdapterMethodVisitor extends MethodVisitor {

	private String paramToCheck;
	private AdapterClassVisitor visitor;
	private String currentMethod;

	public AdapterMethodVisitor(int arg0, MethodVisitor arg1) {
		super(arg0, arg1);
	}
	
	public AdapterMethodVisitor(int api, AdapterClassVisitor visitor, String paramToCheck, String currentMethod) {
		super(api);
		this.visitor = visitor;
		this.paramToCheck = paramToCheck;
		this.currentMethod = currentMethod;
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		owner = owner.replaceAll("/", ".");
		if (owner.equals(paramToCheck)) {
			visitor.verify(currentMethod);
		}
	}


}
