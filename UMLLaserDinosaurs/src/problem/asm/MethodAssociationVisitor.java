package problem.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class MethodAssociationVisitor extends MethodVisitor {

	private StringBuilder builder;
	private ArrayList<String> associatesList;
	private ArrayList<String> usesList;

	public MethodAssociationVisitor(int api, MethodVisitor decorated, StringBuilder builder,
			ArrayList<String> associatesList, ArrayList<String> usesList) {
		super(api, decorated);
		this.builder = builder;
		this.associatesList = associatesList;
		this.usesList = usesList;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String ownerCheck = owner.replaceAll("\\\\", "[.]");
		if(name.equals("<init>") || ownerCheck.equals(DesignParser.currentClass)){
			String[] parameterReturnSplit = desc.split("[)]");
			String returnString = parameterReturnSplit[1];
			String returnStringName = Type.getType(returnString).getClassName();
			System.out.println(name + " " + returnStringName);

			String parameterString = parameterReturnSplit[0].substring(1);
			if (!parameterString.equals("")) {
				String[] parameters = parameterString.split("\\[");
				for (int i = 1; i < parameters.length; i++) {
					System.out.println(name + " " + Type.getType(parameters[i]).getClassName());
				}
			}
		}
		

		// System.out.println(Type.getType(desc).getClass().getComponentType());
		// System.out.println(owner + " -- " +name);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		// System.out.println("visitFieldInsn " + name + " " +
		// Type.getType(desc).getClassName());
		// System.out.println(desc);
		// System.out.println("owner: " + owner + ", name: " + name);
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		// System.out.println("visitTypeInsn " + type);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		// Unused
	}

}
