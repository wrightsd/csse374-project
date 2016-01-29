package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class AdapterMethodVisitor extends MethodVisitor {

	private String paramToCheck;
	private AdapterClassVisitor visitor;

	public AdapterMethodVisitor(int arg0, MethodVisitor arg1) {
		super(arg0, arg1);
	}
	
	public AdapterMethodVisitor(int api, AdapterClassVisitor visitor, String paramToCheck) {
		super(api);
		this.visitor = visitor;
		this.paramToCheck = paramToCheck;
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		owner = owner.replaceAll("/", ".");
		if (owner.equals(paramToCheck)) {
			visitor.verify(name + desc);
		}
	}


}
