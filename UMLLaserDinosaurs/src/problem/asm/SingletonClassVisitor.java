package problem.asm;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

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
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
				
		if((access & Opcodes.ACC_STATIC)>=1){
			if(Type.getType(desc).getClassName().equals(DesignParser.getCurrentClass())){
				this.fieldSingletonCriteria = true;
			}
//			System.out.println(Type.getType(desc));
//			System.out.println(name + " , signature = " +signature);
//			System.out.println(name + " , value = " +value);
//			System.out.println("static");
//			System.out.println();
		}
		
		return toDecorate;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		
		
		if((name.equals("getInstance")) && ((access & Opcodes.ACC_STATIC)>=1)){
			if(Type.getArgumentTypes(desc).length == 0){
				if(Type.getReturnType(desc).getClassName().equals(DesignParser.getCurrentClass())){
					this.methodSingletonCriteria = true;
					System.out.println(DesignParser.getCurrentClass() + "getInstance");
					System.out.println(Arrays.toString(Type.getArgumentTypes(desc)));
					System.out.println(Type.getReturnType(desc));
					System.out.println();
					
				}
			}
		}
		
		return toDecorate;
	}
	
	

}
