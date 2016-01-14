package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassSequenceVisitor extends ClassVisitor {
	
	private int chosenDepth;
	private String chosenClassName;
	private String chosenMethodName;
	private String[] chosenParams;
	
	public ClassSequenceVisitor(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void ClassSequenceVistor(int depth, String ClassName, String MethodName, String[] params){
		this.chosenDepth = depth;
		this.chosenClassName = ClassName;
		this.chosenMethodName = MethodName;
		this.chosenParams = params;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		Type[] argTypes = Type.getArgumentTypes(desc);
		String[] paramNames = new String[argTypes.length];
		for(int i=0;i<argTypes.length;i++){
			paramNames[i] = argTypes[i].toString();
		}
		if(name.equals(this.chosenMethodName) && paramNames.equals(this.chosenParams)){
			MethodVisitor methodSequence = new MethodSequenceVisitor();
		}
		
		return toDecorate;
		
	}

}
