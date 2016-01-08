package problem.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassAssociationVisitor extends ClassVisitor {
	
	private StringBuilder builder;
	private ArrayList<String> associatesList;
	private ArrayList<String> usesList;

	public ClassAssociationVisitor(int api, ClassVisitor decorated, ArrayList<String> usesList, ArrayList<String> associatesList) {
		super(api, decorated);
		this.usesList = usesList;
		this.associatesList = associatesList;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		MethodVisitor mine = new MethodAssociationVisitor(Opcodes.ASM5, toDecorate, associatesList, usesList);
		
		return mine;
	}

}
