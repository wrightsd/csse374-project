package problem.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassFieldVisitor extends ClassVisitor {

	private StringBuilder builder;
	
	public ClassFieldVisitor(int api) {
		super(api);
	}

	public ClassFieldVisitor(int api, ClassVisitor decorated) {
		super(api, decorated);
	}
	
	public ClassFieldVisitor(int api, ClassVisitor decorated, StringBuilder builder) {
		super(api, decorated);
		this.builder = builder;
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		String[] typeSplitArray = Type.getType(desc).getClassName().split("[.]");
		String type = typeSplitArray[typeSplitArray.length-1];
		// TODO: delete the line below
		if (access == Opcodes.ACC_PUBLIC) {
			builder.append("+");
		} else if (access == Opcodes.ACC_PROTECTED) {
			builder.append("#");
		} else { // field is private
			builder.append("-");
		}
		builder.append(name + " : " + type + "\\l");
		// TODO: add this field to your internal representation of the current
		// class.
		// What is a good way to know what the current class is?
		return toDecorate;
	};

}
