package problem.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassAssociationVisitor extends ClassVisitor {
	
	private StringBuilder builder;

	public ClassAssociationVisitor(int api) {
		super(api);
	}

	public ClassAssociationVisitor(int api, StringBuilder builder) {
		super(api);
		this.builder = builder;
	}

	public ClassAssociationVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
	}

	public ClassAssociationVisitor(int api, ClassVisitor decorated, StringBuilder builder) {
		super(api, decorated);
		this.builder = builder;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		ArrayList<String> associatesList = new ArrayList<String>();
		ArrayList<String> usesList = new ArrayList<String>();
		MethodVisitor mine = new MethodAssociationVisitor(Opcodes.ASM5, toDecorate, builder, associatesList,usesList);
		
		return mine;
	}

}
