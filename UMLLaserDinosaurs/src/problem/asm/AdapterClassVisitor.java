package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class AdapterClassVisitor extends ClassVisitor {
	
	private String nameOfSuper;
	
	public AdapterClassVisitor(int api) {
		super(api);
		// TODO Auto-generated constructor stub
	}

	public AdapterClassVisitor(int api, ClassVisitor visitor) {
		super(api, visitor);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//		if (superName != null) {
//
//			boolean superValid = true;
//			superName = superName.replaceAll("/", ".");
//			for (String blacklisted : BlacklistHolder.getInstance()) {
//				if (blacklisted.equals(superName)) {
//					superValid = false;
//					break;
//				}
//			}
//			if (superValid) {
//				this.nameOfSuper = superName;
//			}
//		}

		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
//		if (name.equals("<init>")) {
//			Type[] args = Type.getArgumentTypes(desc);
//			for (int i = 0; i < args.length; i++) {
//				if (this.nameOfSuper.equals(args[i].getClassName())) {
//					UMLMaker.addPattern(DesignParser.getCurrentClass(), "decorator");
//					UMLMaker.addPattern(args[i].getClassName(), "component");
//					UMLMaker.addLabelledArrow(DesignParser.getCurrentClass(), args[i].getClassName(),
//							"\\<\\<decorates\\>\\>");
//				}
//			}
//		}
		return toDecorate;
	}


}
