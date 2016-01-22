package problem.asm;

import org.objectweb.asm.ClassVisitor;

public class SingletonClassVisitor extends ClassVisitor {
	
	private boolean fieldSingletonCriteria;
	private boolean methodSingletonCriteria;

	public SingletonClassVisitor(int arg0, ClassVisitor arg1) {
		super(arg0, arg1);
		this.fieldSingletonCriteria = false;
		this.methodSingletonCriteria = false;
	}

	public SingletonClassVisitor(int asm5, ClassVisitor visitor, StringBuilder builder) {
		super(asm5, visitor);
		this.fieldSingletonCriteria = false;
		this.methodSingletonCriteria = false;
	}
	
	
	
	

}
