package problem.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassAssociationVisitor extends ClassVisitor {

	private StringBuilder builder;
	private ArrayList<String> associatesList;
	private ArrayList<String> usesList;

	public ClassAssociationVisitor(int api, ClassVisitor decorated, ArrayList<String> usesList,
			ArrayList<String> associatesList) {
		super(api, decorated);
		this.usesList = usesList;
		this.associatesList = associatesList;
	}

	public ClassAssociationVisitor(int api, ArrayList<String> associatesList, ArrayList<String> usesList) {
		super(api);
		this.usesList = usesList;
		this.associatesList = associatesList;
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		//System.out.println(signature);
		compileAggregation(signature);
		return toDecorate;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		//System.out.println(signature);
		compileAggregation(signature);
		MethodVisitor mine = new MethodAssociationVisitor(Opcodes.ASM5, toDecorate, associatesList, usesList);
		return mine;
	}

	// TODO: Fix for nested aggregation
	private void compileAggregation(String signature) {
		if (null == signature) {
			return;
		}
		ArrayList<String> types = new ArrayList<String>();
		boolean type = false;
		String aggregateType = "";
		for (int i = 0; i < signature.length(); i++) {
			if (type) {
				if (signature.charAt(i) == '>') {
					types.add(aggregateType);
					type = false;
				} else {
					aggregateType += signature.charAt(i);
				}
			} else {
				if (signature.charAt(i) == '<') {
					aggregateType = "";
					type = true;
				}
			}
		}

		for (String str : types) {
			String typeClass = Type.getType(str).getClassName();
			String[] typeClassSplit = typeClass.split("[.]");
			typeClass = typeClassSplit[typeClassSplit.length - 1];
			String owner = DesignParser.getCurrentClass();
			String[] ownerSplit = owner.split("[.]");
			owner = ownerSplit[ownerSplit.length - 1];
			
			String toAdd = owner + "->" + typeClass;
			boolean add = true;
			for (String s : associatesList) {
				if (s.equals(toAdd)) {
					add = false;
				}
			}
			if (add) {
				associatesList.add(toAdd);
			}
		}
	}

}
