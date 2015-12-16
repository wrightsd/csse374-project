package problem.asm;

import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class DesignParser {

	public static void main(String[] args) throws IOException {
		for (String className : args) {
			StringBuilder completeBuilder = new StringBuilder();
			StringBuilder methodBuilder = new StringBuilder();
			StringBuilder fieldBuilder = new StringBuilder();
			StringBuilder arrowBuilder = new StringBuilder();
			completeBuilder.append("digraph text{\n");
			completeBuilder.append("rankdir=BT;\n");
			// ASM's ClassReader does the heavy lifting of parsing the compiled
			// Java class
			ClassReader reader = new ClassReader(className);
			// make class declaration visitor to get superclass and interfaces
			ClassVisitor declVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, arrowBuilder);
			String[] classNameToWriteArray = className.split("[.]");
			String classNameToWrite = classNameToWriteArray[classNameToWriteArray.length-1];
			completeBuilder.append(classNameToWrite + " [\nshape=\"record\",\n");
			completeBuilder.append("label = \"{" + className + "|");
			// DECORATE declaration visitor with field visitor
			ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, declVisitor, fieldBuilder);
			// DECORATE field visitor with method visitor
			ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, methodBuilder);
			// TODO: add more DECORATORS here in later milestones to accomplish
			// specific tasks

			// Tell the Reader to use our (heavily decorated) ClassVisitor to
			// visit the class
			reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
			completeBuilder.append(fieldBuilder.toString());
			completeBuilder.append("|");
			completeBuilder.append(methodBuilder);
			completeBuilder.append("}\"\n];\n");
			completeBuilder.append(arrowBuilder.toString());
			completeBuilder.append("}");

			FileOutputStream writer = new FileOutputStream("output/output.txt");
			writer.write(completeBuilder.toString().getBytes());
			writer.close();
		}
	}

}
