package problem.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

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
		compileAggregation(signature);
		return toDecorate;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		compileAggregation(signature);
		MethodVisitor mine = new MethodAssociationVisitor(Opcodes.ASM5, toDecorate, associatesList, usesList);
		return mine;
	}

	// TODO: Fix for nested aggregation
	private void compileAggregation(String signature) {
		if (signature != null) {
			ArrayList<String> types = new ArrayList<String>();
			SignatureReader sigRead = new SignatureReader(signature);
			SignatureVisitor sigVis = new SignatureVisitor(Opcodes.ASM5) {

				@Override
				public void visitClassType(String type) {
					types.add(type);
				}

			};
			sigRead.acceptType(sigVis);

			types.remove(0);
			for (String str : types) {
				String[] typeClassSplit = str.split("/");
				str = typeClassSplit[typeClassSplit.length - 1];
				String owner = DesignParser.getCurrentClass();
				String[] ownerSplit = owner.split("[.]");
				owner = ownerSplit[ownerSplit.length - 1];

				String toAdd = owner + "->" + str;
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

}
