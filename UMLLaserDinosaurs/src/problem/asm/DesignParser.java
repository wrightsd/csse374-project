package problem.asm;

import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class DesignParser {

	public static void main(String[] args) throws IOException {
		for (String className : args) {
			StringBuilder builder = new StringBuilder();
			builder.append("digraph text{\n");
			builder.append("rankdir=BT;\n");
			// ASM's ClassReader does the heavy lifting of parsing the compiled
			// Java class
			ClassReader reader = new ClassReader(className);
			// make class declaration visitor to get superclass and interfaces
			ClassVisitor declVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, builder);
			
			builder.append(className + " [\nshape=\"record\",\n");
			builder.append("label = \"[" + className + "|");
			// DECORATE declaration visitor with field visitor
			ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, declVisitor, builder);
			// DECORATE field visitor with method visitor
			ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, builder);
			// TODO: add more DECORATORS here in later milestones to accomplish
			// specific tasks

			// Tell the Reader to use our (heavily decorated) ClassVisitor to
			// visit the class
			reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
			builder.append("}\"\n];");

			FileOutputStream writer = new FileOutputStream("output/output.txt");
			writer.write(builder.toString().getBytes());
		}
	}

}
