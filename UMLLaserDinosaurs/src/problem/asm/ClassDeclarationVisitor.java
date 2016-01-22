package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;

public class ClassDeclarationVisitor extends ClassVisitor {

	private StringBuilder builder;

	public ClassDeclarationVisitor(int arg0) {
		super(arg0);
	}

	public ClassDeclarationVisitor(int arg0, StringBuilder builder) {
		super(arg0);
		this.builder = builder;
	}

	public ClassDeclarationVisitor(int asm5, ClassVisitor visitor, StringBuilder builder) {
		super(asm5, visitor);
		this.builder = builder;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

		name = name.replaceAll("/", ".");
		ArbitraryNodeNames.getInstance().addNewNode(name);
		String nameNodeName = ArbitraryNodeNames.getInstance().getNodeName(name);

		if (superName != null) {

			boolean superValid = true;
			superName = superName.replaceAll("/", ".");
			for (String blacklisted : BlacklistHolder.getInstance()) {
				if (blacklisted.equals(superName)) {
					superValid = false;
					break;
				}
			}
			if (superValid) {
				builder.append(nameNodeName + "->");
				ArbitraryNodeNames.getInstance().addNewNode(superName);
				String superNodeName = ArbitraryNodeNames.getInstance().getNodeName(superName);
				builder.append(superNodeName + "[arrowhead=\"onormal\", style=\"solid\"];\n");
				
				if(!DesignParser.getArguments().contains(superName)){
					UMLMaker.addNonIncludedClassBuilder(superName);
				}
			}
		}

		for (String s : interfaces) {
			boolean interfaceValid = true;
			s = s.replaceAll("/", ".");
			for (String blacklisted : BlacklistHolder.getInstance()) {
				if (blacklisted.equals(s)) {
					interfaceValid = false;
					break;
				}
			}
			if (interfaceValid) {
				builder.append(nameNodeName + "->");
				ArbitraryNodeNames.getInstance().addNewNode(s);
				String interfaceNodeName = ArbitraryNodeNames.getInstance().getNodeName(s);
				builder.append(interfaceNodeName + "[arrowhead=\"onormal\", style=\"dashed\"];\n");
				
				if(!DesignParser.getArguments().contains(s)){
					UMLMaker.addNonIncludedClassBuilder(s);
				}
			}
		}
		// TODO: construct an internal representation of the class for later use
		// by decorators
		super.visit(version, access, name, signature, superName, interfaces);
	}
}
