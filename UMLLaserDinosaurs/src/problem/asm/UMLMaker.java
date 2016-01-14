package problem.asm;

import java.io.IOException;
import java.util.ArrayList;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class UMLMaker implements DiagramMaker {
	
	public static String currentClass;
	private static ArrayList<String> classSet;
	private static String[] myArgs;
	private static ArrayList<String> usesList;
	private static ArrayList<String> associatesList;

	@Override
	public StringBuilder generateDiagramText(String[] args) throws IOException {
		myArgs = args;

		StringBuilder completeBuilder = new StringBuilder();
		completeBuilder.append("digraph text{\n");
		completeBuilder.append("rankdir=BT;\n");

		associatesList = new ArrayList<String>();
		usesList = new ArrayList<String>();
		StringBuilder associationBuilder = new StringBuilder();

		for (String className : args) {
			currentClass = className;
			StringBuilder methodBuilder = new StringBuilder();
			StringBuilder fieldBuilder = new StringBuilder();
			StringBuilder arrowBuilder = new StringBuilder();
			StringBuilder interfaceBuilder = new StringBuilder();
			// ASM's ClassReader does the heavy lifting of parsing the compiled
			// Java class
			ClassReader reader = new ClassReader(className);
			// make class declaration visitor to get superclass and interfaces
			ClassVisitor declVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, arrowBuilder);
			String[] classNameToWriteArray = className.split("[.]");
			String classNameToWrite = classNameToWriteArray[classNameToWriteArray.length - 1];
			completeBuilder.append(classNameToWrite + " [\nshape=\"record\",\n");
			completeBuilder.append("label = \"{");

			ClassVisitor interfaceVisitor = new InterfaceAbstractCheckVisitor(Opcodes.ASM5, declVisitor,
					interfaceBuilder);
			// DECORATE declaration visitor with field visitor
			ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, interfaceVisitor, fieldBuilder);
			// DECORATE field visitor with method visitor
			ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, methodBuilder);
			// TODO: add more DECORATORS here in later milestones to accomplish
			// specific tasks

			ClassVisitor associationVisitor = new ClassAssociationVisitor(Opcodes.ASM5, methodVisitor, usesList,
					associatesList);

			// Tell the Reader to use our (heavily decorated) ClassVisitor to
			// visit the class
			reader.accept(associationVisitor, ClassReader.EXPAND_FRAMES);
			if (interfaceBuilder.toString().length() > 0) {
				completeBuilder.append(interfaceBuilder.toString());
			}
			completeBuilder.append(classNameToWrite + "|");
			if (fieldBuilder.toString().length() > 0) {
				completeBuilder.append(fieldBuilder.toString());
				completeBuilder.append("|");
			}
			if (methodBuilder.toString().length() > 0) {
				// Remove default constructors from UML
				String methods = methodBuilder.toString().replaceAll("[+]init[(][)] : void\\\\l", "");
				methods = methods.replaceAll("init[(]", classNameToWrite + "(");
				completeBuilder.append(methods);
			}
			completeBuilder.append("}\"\n];\n");
			completeBuilder.append(arrowBuilder.toString());
		}

		drawUsesArrows(usesList, associatesList, associationBuilder);
		completeBuilder.append(associationBuilder.toString());
		completeBuilder.append("}");
		
		return completeBuilder;
	}
	
	private static void drawUsesArrows(ArrayList<String> usesList, ArrayList<String> associatesList,
			StringBuilder builder) {
		for (String s : usesList) {
			builder.append(s);
			builder.append("[arrowhead=\"ovee\", style=\"dashed\"];");
			builder.append("\n");
		}
		for (String s : associatesList) {
			builder.append(s);
			builder.append("[arrowhead=\"ovee\", style=\"solid\"];");
			builder.append("\n");
		}
	}

	@Override
	public ArrayList<String> getArguments() {
		if (classSet == null) {
			classSet = new ArrayList<String>();
			if(myArgs != null){
				for (String s : myArgs) {
					classSet.add(s);
				}
			}
		}
		return classSet;
	}

	@Override
	public String getCurrentClass() {
		return currentClass;
	}

}
