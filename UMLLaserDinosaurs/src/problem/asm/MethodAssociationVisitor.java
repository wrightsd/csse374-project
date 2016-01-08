package problem.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class MethodAssociationVisitor extends MethodVisitor {

	private StringBuilder builder;
	private ArrayList<String> associatesList;
	private ArrayList<String> usesList;

	public MethodAssociationVisitor(int api, MethodVisitor decorated, ArrayList<String> associatesList,
			ArrayList<String> usesList) {
		super(api, decorated);
		this.associatesList = associatesList;
		this.usesList = usesList;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		owner = owner.replaceAll("/", ".");
		// if owner is not in designparser list then just return
		boolean ownerValid = false;
		for (String s : DesignParser.getInstance()) {
			if (s.equals(owner)) {
				ownerValid = true;
				break;
			}
		}
		if (!ownerValid) {
			return;
		}
		String[] ownerStringArray = owner.split("[.]");
		if (ownerStringArray.length > 0) {
			owner = ownerStringArray[ownerStringArray.length - 1];
		}

		String[] parameterReturnSplit = desc.split("[)]");
		String returnString = parameterReturnSplit[1];
		if (returnString.charAt(0) == 'L') {
			String returnStringName = Type.getType(returnString).getClassName();
			String[] returnStringArray = returnStringName.split("[.]");
			if (returnStringArray.length > 0) {
				returnStringName = returnStringArray[returnStringArray.length - 1];
			}
			boolean contains = false;
			for (String s : usesList) {
				if (s.equals(owner + "->" + returnStringName)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				usesList.add(owner + "->" + returnStringName);
			}
		}

		String parameterString = parameterReturnSplit[0].substring(1);
		if (!parameterString.equals("")) {
			String[] parameters = parameterString.split("\\[");
			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i].charAt(0) == 'L') {
					String parameterStringName = Type.getType(parameters[i]).getClassName();
					String[] parameterStringArray = parameterStringName.split("[.]");
					if (parameterStringArray.length > 0) {
						parameterStringName = parameterStringArray[parameterStringArray.length - 1];
					}
					boolean contains = false;
					for (String s : usesList) {
						if (s.equals(owner + "->" + parameterStringName)) {
							contains = true;
							break;
						}
					}
					if (!contains) {
						usesList.add(owner + "->" + parameterStringName);
					}
				}
			}
		}

	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		owner = owner.replaceAll("/", ".");
		// if owner is not in designparser list then just return
		boolean ownerValid = false;
		for (String s : DesignParser.getInstance()) {
			if (s.equals(owner)) {
				ownerValid = true;
				break;
			}
		}
		if (!ownerValid) {
			return;
		}
		String[] ownerStringArray = owner.split("[.]");
		if (ownerStringArray.length > 0) {
			owner = ownerStringArray[ownerStringArray.length - 1];
		}

		if (desc.charAt(0) == 'L') {
			String descStringName = Type.getType(desc).getClassName();
			String[] descStringArray = descStringName.split("[.]");
			if (descStringArray.length > 0) {
				descStringName = descStringArray[descStringArray.length - 1];
			}
			boolean contains = false;
			for (String s : usesList) {
				if (s.equals(owner + "->" + descStringName)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				usesList.add(owner + "->" + descStringName);
			}
		}

	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		String owner = DesignParser.currentClass;
		String[] ownerStringArray = owner.split("[.]");
		if (ownerStringArray.length > 0) {
			owner = ownerStringArray[ownerStringArray.length - 1];
		}

		String stringName = type;
		String[] stringArray = stringName.split("/");
		if (stringArray.length > 0) {
			stringName = stringArray[stringArray.length - 1];
		}
		boolean contains = false;
		for (String s : usesList) {
			if (s.equals(owner + "->" + stringName)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			usesList.add(owner + "->" + stringName);
		}
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		// Unused
	}

}
