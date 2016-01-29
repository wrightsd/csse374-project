package problem.asm;

import java.io.IOException;
import java.util.ArrayList;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class DecoratorClassVisitor extends ClassVisitor {

	private String superName;
	private String[] interfaces;

	public DecoratorClassVisitor(int api) {
		super(api);
	}

	public DecoratorClassVisitor(int api, ClassVisitor decorator) {
		super(api, decorator);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		if (interfaces.length > 0) {
			this.interfaces = interfaces;
		}

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
				this.superName = superName;
			}
		}

		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		boolean continueCheck = true;
		if (interfaces != null) {
			for (int i = 0; i < interfaces.length; i++) {
				if (name.equals("<init>")) {
					String interfaceName = interfaces[i].replaceAll("/", ".");
					boolean blacklistedCheck = false;
					for (String blacklisted : BlacklistHolder.getInstance()) {
						if (blacklisted.equals(superName)) {
							blacklistedCheck = true;
							break;
						}
					}
					if (!blacklistedCheck) {

						Type[] args = Type.getArgumentTypes(desc);
						for (int k = 0; k < args.length; k++) {
							if (interfaceName.equals(args[k].getClassName())) {
								UMLMaker.addPattern(DesignParser.getCurrentClass(), "decorator");
								UMLMaker.addPattern(args[k].getClassName(), "component");
								UMLMaker.addLabelledArrow(DesignParser.getCurrentClass(), args[k].getClassName(),
										"\\<\\<decorates\\>\\>");
							}
						}
					}
				}
			}
		}

		if (continueCheck) {
			if (this.superName != null) {
				if (name.equals("<init>")) {
					Type[] args = Type.getArgumentTypes(desc);
					for (int i = 0; i < args.length; i++) {
						if (this.superName.equals(args[i].getClassName())) {
							UMLMaker.addPattern(DesignParser.getCurrentClass(), "decorator");
							UMLMaker.addPattern(args[i].getClassName(), "component");
							UMLMaker.addLabelledArrow(DesignParser.getCurrentClass(), args[i].getClassName(),
									"\\<\\<decorates\\>\\>");
						} else if (!this.superName.equals("java.lang.Object")) {
							this.checkDecorates(args[i].getClassName());
						}
					}
				}
			}
		}
		return toDecorate;

	}

	private void checkDecorates(String className) {
		ArrayList<String> subclasses = new ArrayList<String>();
		subclasses.add(DesignParser.getCurrentClass());
		subclasses.add(this.superName);
		ClassReader superReader;
		try {
			superReader = new ClassReader(this.superName);
			ClassVisitor decoratesChecker = new DecorateRecursiveVisitor(Opcodes.ASM5, className, subclasses);
			superReader.accept(decoratesChecker, ClassReader.EXPAND_FRAMES);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
