package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class InterfaceCheckVisitor extends ClassVisitor{

	private StringBuilder builder;

	public InterfaceCheckVisitor(int api) {
		super(api);
	}
	
	public InterfaceCheckVisitor(int api, ClassVisitor previousVisitor, StringBuilder builder){
		super(api, previousVisitor);
		this.builder = builder;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if((access&Opcodes.ACC_INTERFACE)!=0){
            builder.append("\\<\\<interface\\>\\>\\n");
        }
        
		// TODO: construct an internal representation of the class for later use
		// by decorators
		super.visit(version, access, name, signature, superName, interfaces);
	}

}
