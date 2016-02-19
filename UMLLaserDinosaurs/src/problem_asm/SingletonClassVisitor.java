package problem_asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import sun.security.krb5.internal.crypto.Des;

public class SingletonClassVisitor extends ClassVisitor {

	private boolean fieldSingletonCriteria;
	private boolean methodSingletonCriteria;

	public SingletonClassVisitor(int arg0, ClassVisitor arg1) {
		super(arg0, arg1);
		this.fieldSingletonCriteria = false;
		this.methodSingletonCriteria = false;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);

		if ((access & Opcodes.ACC_STATIC) >= 1) {
			if (Type.getType(desc).getClassName().equals(DesignParser.getCurrentClass())) {
				this.fieldSingletonCriteria = true;
				if (this.methodSingletonCriteria) {
					UMLMaker.addPattern(DesignParser.getCurrentClass(),"singleton", "singleton", DesignParser.getCurrentClass());
				}
			}
		}

		return toDecorate;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);

		if ((access & Opcodes.ACC_STATIC) >= 1) {
			if (Type.getArgumentTypes(desc).length == 0) {
				if (Type.getReturnType(desc).getClassName().equals(DesignParser.getCurrentClass())) {
					this.methodSingletonCriteria = true;
					if (this.fieldSingletonCriteria) {
						UMLMaker.addPattern(DesignParser.getCurrentClass(),"singleton", "singleton", DesignParser.getCurrentClass());
					}
				}
			}
		}

		return toDecorate;
	}

}
