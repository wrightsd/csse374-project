package problem.asm;

import org.objectweb.asm.MethodVisitor;

public class MethodAssociationVisitor extends MethodVisitor {

	public MethodAssociationVisitor(int api, MethodVisitor decorated) {
		super(api, decorated);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {

	}

	@Override
	public void visitTypeInsn(int opcode, String type) {

	}

	@Override
	public void visitVarInsn(int opcode, int var) {

	}

}
