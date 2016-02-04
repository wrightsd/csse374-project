package problem.asm;

import java.util.ArrayList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class CompositeVisitor extends ClassVisitor {
	
	private ArrayList<ArrayList<String>> compositeComponents;

	public CompositeVisitor(int arg0, ClassVisitor arg1, ArrayList<ArrayList<String>> compositeComponents) {
		super(arg0, arg1);
		this.compositeComponents = compositeComponents;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		return toDecorate;
	}

}
