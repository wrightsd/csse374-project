package problem.asm;

import java.io.IOException;
import java.util.ArrayList;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class DecorateRecursiveVisitor extends ClassVisitor {

	private String classNameToCheck;
	private ArrayList<String> subclasses;

	public DecorateRecursiveVisitor(int arg0) {
		super(arg0);
	}

	public DecorateRecursiveVisitor(int arg0, String classNameToCheck, ArrayList<String> subclasses) {
		super(arg0);
		this.classNameToCheck = classNameToCheck;
		this.subclasses = subclasses;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		boolean continueCheck = true;
		if (interfaces.length > 0) {
			for (int i = 0; i < interfaces.length; i++) {
				String interfaceName = interfaces[i].replaceAll("/", ".");
				if (interfaceName.equals(classNameToCheck)) {
					continueCheck = false;
					boolean blacklistedCheck = false;
					for (String blacklisted : BlacklistHolder.getInstance()) {
						if (blacklisted.equals(superName)) {
							blacklistedCheck = true;
							break;
						}
					}
					if (!blacklistedCheck) {
						for (int j = 0; j < this.subclasses.size(); j++) {
							UMLMaker.addPattern(this.subclasses.get(j), "decorator","decorator",DesignParser.getCurrentClass());
						}
						for (int j = 2; j < this.subclasses.size(); j++) {
							UMLMaker.addExtendsArrow(this.subclasses.get(j - 1), this.subclasses.get(j));
						}
						UMLMaker.addImplementsArrow(name.replaceAll("/", "."), superName);
						UMLMaker.addPattern(superName, "component", "decorator", DesignParser.getCurrentClass());
						UMLMaker.addLabelledArrow(name.replaceAll("/", "."), superName, "\\<\\<decorates\\>\\>");
					}

				}
			}
		}
		if (continueCheck) {
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
					if (superName.equals(this.classNameToCheck)) {
						for (int i = 0; i < this.subclasses.size(); i++) {
							UMLMaker.addPattern(this.subclasses.get(i), "decorator", "decorator", DesignParser.getCurrentClass());
						}
						for (int i = 2; i < this.subclasses.size(); i++) {
							UMLMaker.addExtendsArrow(this.subclasses.get(i - 1), this.subclasses.get(i));
						}
						UMLMaker.addExtendsArrow(name.replaceAll("/", "."), superName);
						UMLMaker.addPattern(superName, "component", "decorator", DesignParser.getCurrentClass());
						UMLMaker.addLabelledArrow(name.replaceAll("/", "."), superName, "\\<\\<decorates\\>\\>");
					} else if (!superName.equals("java.lang.Object")) {
						this.checkDecorates(superName);
					}
				}
			}
		}

		super.visit(version, access, name, signature, superName, interfaces);
	}

	private void checkDecorates(String superName) {
		subclasses.add(superName);
		ClassReader superReader;
		try {
			superReader = new ClassReader(superName);
			ClassVisitor decoratesChecker = new DecorateRecursiveVisitor(Opcodes.ASM5, this.classNameToCheck,
					subclasses);
			superReader.accept(decoratesChecker, ClassReader.EXPAND_FRAMES);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
