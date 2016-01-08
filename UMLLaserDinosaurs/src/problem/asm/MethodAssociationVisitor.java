package problem.asm;

import java.util.ArrayList;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class MethodAssociationVisitor extends MethodVisitor {
	
	private StringBuilder builder;
	private ArrayList<String> associatesList;
	private ArrayList<String> usesList;

	public MethodAssociationVisitor(int api, MethodVisitor decorated, StringBuilder builder, ArrayList<String> associatesList, ArrayList<String> usesList) {
		super(api, decorated);
		this.builder = builder;
		this.associatesList = associatesList;
		this.usesList = usesList;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		System.out.println("visitMethodInsn "+name+" "+Type.getType(desc).getClassName());
		
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		System.out.println("visitFieldInsn "+name+" "+Type.getType(desc).getClassName());
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		System.out.println("visitTypeInsn "+type);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		//Unused
	}

}
