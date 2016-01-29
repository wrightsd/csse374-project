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
		if (UMLMaker.isBlacklisted(owner)) {
			return;
		}

		if (!owner.equals(DesignParser.getCurrentClass())) {

			ArbitraryNodeNames.getInstance().addNewNode(owner);
			String ownerNodeName = ArbitraryNodeNames.getInstance().getNodeName(owner);
			String currentNodeName = ArbitraryNodeNames.getInstance().getNodeName(DesignParser.getCurrentClass());

			boolean contains = false;
			for (String s : usesList) {
				if (s.equals(currentNodeName + "->" + ownerNodeName)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				usesList.add(currentNodeName + "->" + ownerNodeName);

				if (!DesignParser.getArguments().contains(owner)) {
					UMLMaker.addNonIncludedClass(owner);
				}
			}
			return;
		}

		boolean ownerValid = false;
		for (String s : DesignParser.getArguments()) {
			if (s.equals(owner)) {
				ownerValid = true;
				break;
			}
		}
		if (!ownerValid) {
			return;
		}
		ArbitraryNodeNames.getInstance().addNewNode(owner);
		String ownerNodeName = ArbitraryNodeNames.getInstance().getNodeName(owner);

		String[] parameterReturnSplit = desc.split("[)]");
		String returnString = parameterReturnSplit[1];
		if (returnString.charAt(0) == 'L') {
			String returnStringName = Type.getType(returnString).getClassName();
			boolean returnValid = false;
			for (String s : DesignParser.getArguments()) {
				if (s.equals(returnStringName)) {
					returnValid = true;
					break;
				}
			}
			if (!returnValid) {
				return;
			}
			ArbitraryNodeNames.getInstance().addNewNode(returnStringName);
			String returnNodeName = ArbitraryNodeNames.getInstance().getNodeName(returnStringName);
			boolean contains = false;
			for (String s : usesList) {
				if (s.equals(ownerNodeName + "->" + returnNodeName)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				usesList.add(ownerNodeName + "->" + returnNodeName);

				if (!DesignParser.getArguments().contains(returnStringName)) {
					UMLMaker.addNonIncludedClass(returnStringName);
				}
			}
		}

		String parameterString = parameterReturnSplit[0].substring(1);
		if (!parameterString.equals("")) {
			String[] parameters = parameterString.split("\\[");
			for (int i = 0; i < parameters.length; i++) {
				if (!parameters[i].equals("")) {
					if (parameters[i].charAt(0) == 'L') {
						String parameterStringName = Type.getType(parameters[i]).getClassName();
						boolean parameterValid = false;
						for (String s : DesignParser.getArguments()) {
							if (s.equals(parameterStringName)) {
								parameterValid = true;
								break;
							}
						}
						if (!parameterValid) {
							return;
						}
						ArbitraryNodeNames.getInstance().addNewNode(parameterStringName);
						String parameterNodeName = ArbitraryNodeNames.getInstance().getNodeName(parameterStringName);
						boolean contains = false;
						for (String s : usesList) {
							if (s.equals(ownerNodeName + "->" + parameterNodeName)) {
								contains = true;
								break;
							}
						}
						if (!contains) {
							usesList.add(ownerNodeName + "->" + parameterNodeName);

							if (!DesignParser.getArguments().contains(parameterStringName)) {
								UMLMaker.addNonIncludedClass(parameterStringName);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		owner = owner.replaceAll("/", ".");
		if (UMLMaker.isBlacklisted(owner)) {
			return;
		}
		// if owner is not in designparser list then just return
		boolean ownerValid = false;
		for (String s : DesignParser.getArguments()) {
			if (s.equals(owner)) {
				ownerValid = true;
				break;
			}
		}
		if (!ownerValid) {
			return;
		}
		ArbitraryNodeNames.getInstance().addNewNode(owner);
		String ownerNodeName = ArbitraryNodeNames.getInstance().getNodeName(owner);

		if (desc.charAt(0) == 'L') {
			String descStringName = Type.getType(desc).getClassName();
			boolean descValid = false;
			for (String s : DesignParser.getArguments()) {
				if (s.equals(descStringName)) {
					descValid = true;
					break;
				}
			}
			if (!descValid) {
				return;
			}

			ArbitraryNodeNames.getInstance().addNewNode(descStringName);
			String descNodeName = ArbitraryNodeNames.getInstance().getNodeName(descStringName);

			boolean contains = false;
			for (String s : usesList) {
				if (s.equals(ownerNodeName + "->" + descNodeName)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				usesList.add(ownerNodeName + "->" + descNodeName);

				if (!DesignParser.getArguments().contains(descStringName)) {
					UMLMaker.addNonIncludedClass(descStringName);
				}
			}
		}

	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		String owner = DesignParser.getCurrentClass();

		ArbitraryNodeNames.getInstance().addNewNode(owner);
		String ownerNodeName = ArbitraryNodeNames.getInstance().getNodeName(owner);

		String typeCheck = type.replaceAll("/", ".");
		boolean typeValid = false;
		for (String s : DesignParser.getArguments()) {
			if (s.equals(typeCheck)) {
				typeValid = true;
				break;
			}
		}
		if (!typeValid) {
			return;
		}

		ArbitraryNodeNames.getInstance().addNewNode(typeCheck);
		String typeNodeName = ArbitraryNodeNames.getInstance().getNodeName(typeCheck);

		boolean contains = false;
		for (String s : usesList) {
			if (s.equals(ownerNodeName + "->" + typeNodeName)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			usesList.add(ownerNodeName + "->" + typeNodeName);

			if (!DesignParser.getArguments().contains(typeCheck)) {
				UMLMaker.addNonIncludedClass(typeCheck);
			}
		}
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		// Unused
	}
}
