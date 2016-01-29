package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class DecoratorClassVisitor extends ClassVisitor {

	private String superName;

	public DecoratorClassVisitor(int api) {
		super(api);
	}

	public DecoratorClassVisitor(int api, ClassVisitor decorator) {
		super(api, decorator);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
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
		if (name.equals("<init>")) {
			Type[] args = Type.getArgumentTypes(desc);
			for (int i = 0; i < args.length; i++) {
				if (this.superName.equals(args[i].getClassName())) {
					UMLMaker.addPattern(DesignParser.getCurrentClass(), "decorator");
					UMLMaker.addPattern(args[i].getClassName(), "component");
				}
			}
		}
		return toDecorate;
	}

}
