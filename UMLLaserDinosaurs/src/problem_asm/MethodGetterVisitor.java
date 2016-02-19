package problem_asm;

import java.util.ArrayList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class MethodGetterVisitor extends ClassVisitor {

	private ArrayList<String> superMethods;

	public MethodGetterVisitor(int arg0) {
		super(arg0);
	}
	
	public MethodGetterVisitor(int arg0, ArrayList<String> superMethods){
		super(arg0);
		this.superMethods = superMethods;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		if (!name.equals("<init>")) {
			this.superMethods.add(name+desc);
		}
		return toDecorate;
	}


}
