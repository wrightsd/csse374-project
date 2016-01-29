package problem.asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class UMLMaker implements DiagramMaker {

	private String currentClass;
	private ArrayList<String> classSet;
	private String[] myArgs;
	private ArrayList<String> usesList;
	private ArrayList<String> associatesList;
	private HashMap<String, String> borderColorMap = new HashMap<String, String>();
	private HashMap<String, String> fillColorMap = new HashMap<String, String>();
	private static HashMap<String, HashMap<String, StringBuilder>> classInfo = new HashMap<String, HashMap<String, StringBuilder>>();

	private static ArrayList<String> nonIncludedClasses = new ArrayList<String>();

	private static HashMap<String, ArrayList<String>> patternLists = new HashMap<String, ArrayList<String>>();

	@Override
	public StringBuilder generateDiagramText(String[] args) throws IOException {
		myArgs = args;
		this.setupBorderColorMap();
		this.setupFillColorMap();

		StringBuilder completeBuilder = new StringBuilder();
		completeBuilder.append("digraph text{\n");
		completeBuilder.append("rankdir=BT;\n");

		associatesList = new ArrayList<String>();
		usesList = new ArrayList<String>();
		StringBuilder associationBuilder = new StringBuilder();

		for (String className : args) {
			currentClass = className;
			ArbitraryNodeNames.getInstance().addNewNode(currentClass);
			StringBuilder methodBuilder = new StringBuilder();
			StringBuilder fieldBuilder = new StringBuilder();
			StringBuilder arrowBuilder = new StringBuilder();
			StringBuilder interfaceBuilder = new StringBuilder();
			// ASM's ClassReader does the heavy lifting of parsing the compiled
			// Java class
			ClassReader reader = new ClassReader(className);
			// make class declaration visitor to get superclass and interfaces
			ClassVisitor declVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, arrowBuilder);

			ClassVisitor singletonVisitor = new SingletonClassVisitor(Opcodes.ASM5, declVisitor);

			ClassVisitor interfaceVisitor = new InterfaceAbstractCheckVisitor(Opcodes.ASM5, singletonVisitor,
					interfaceBuilder);
			// DECORATE declaration visitor with field visitor
			ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, interfaceVisitor, fieldBuilder);
			// DECORATE field visitor with method visitor
			ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, methodBuilder);
			// TODO: add more DECORATORS here in later milestones to accomplish
			// specific tasks

			ClassVisitor associationVisitor = new ClassAssociationVisitor(Opcodes.ASM5, methodVisitor, usesList,
					associatesList);

			ClassVisitor decoratorVisitor = new DecoratorClassVisitor(Opcodes.ASM5, associationVisitor);

			// Tell the Reader to use our (heavily decorated) ClassVisitor to
			// visit the class
			reader.accept(decoratorVisitor, ClassReader.EXPAND_FRAMES);

			HashMap<String, StringBuilder> builderList = new HashMap<String, StringBuilder>();
			builderList.put("field", fieldBuilder);
			builderList.put("method", methodBuilder);
			builderList.put("interface", interfaceBuilder);
			builderList.put("arrows", arrowBuilder);

			classInfo.put(className, builderList);
		}

		for (String className : args) {

			StringBuilder methodBuilder = classInfo.get(className).get("method");
			StringBuilder fieldBuilder = classInfo.get(className).get("field");
			StringBuilder arrowBuilder = classInfo.get(className).get("arrows");
			StringBuilder interfaceBuilder = classInfo.get(className).get("interface");

			completeBuilder.append(ArbitraryNodeNames.getInstance().getNodeName(className) + " [\nshape=\"record\",\n");
			ArrayList<String> patternBuilder = patternLists.get(className);
			if (patternBuilder != null) {
				for (String pattern : patternBuilder) {

					if (borderColorMap.containsKey(pattern)) {
						completeBuilder.append("color =" + borderColorMap.get(pattern));
						completeBuilder.append(",\n");
					}
					if (fillColorMap.containsKey(pattern)) {
						completeBuilder.append("style = filled,\n");
						completeBuilder.append("fillcolor =" + fillColorMap.get(pattern));
						completeBuilder.append(",\n");
					}
				}
			}
			completeBuilder.append("label = \"{");
			if (interfaceBuilder.toString().length() > 0) {
				completeBuilder.append(interfaceBuilder.toString());
			}
			completeBuilder.append(className);
			if (patternBuilder != null) {
				for (String pattern : patternBuilder) {
					completeBuilder.append("\\n\\<\\<" + pattern + "\\>\\>");
				}
			}

			completeBuilder.append("|");
			if (fieldBuilder.toString().length() > 0) {
				completeBuilder.append(fieldBuilder.toString());
				completeBuilder.append("|");
			}
			if (methodBuilder.toString().length() > 0) {
				// Remove default constructors from UML
				String methods = methodBuilder.toString().replaceAll("[+]init[(][)] : void\\\\l", "");
				methods = methods.replaceAll("init[(]", className + "(");
				completeBuilder.append(methods);
			}

			completeBuilder.append("}\"\n];\n");
			completeBuilder.append(arrowBuilder.toString());
		}

		for (String s : nonIncludedClasses) {
			StringBuilder nonIncludedBuilder = new StringBuilder();
			nonIncludedBuilder.append(ArbitraryNodeNames.getInstance().getNodeName(s) + "[\n");
			ArrayList<String> patternBuilder = patternLists.get(s);
			if (patternBuilder != null) {
				for (String pattern : patternBuilder) {

					if (borderColorMap.containsKey(pattern)) {
						nonIncludedBuilder.append("color =" + borderColorMap.get(pattern));
						nonIncludedBuilder.append(",\n");
					}
					if (fillColorMap.containsKey(pattern)) {
						nonIncludedBuilder.append("style = filled,\n");
						nonIncludedBuilder.append("fillcolor =" + fillColorMap.get(pattern));
						nonIncludedBuilder.append(",\n");
					}
				}
			}

			nonIncludedBuilder.append("label = \"" + s);
			ArrayList<String> patterns = patternLists.get(s);
			if (patterns != null) {
				for (String pattern : patternLists.get(s)) {
					nonIncludedBuilder.append("\\n\\<\\<" + pattern + "\\>\\>");
				}
			}
			nonIncludedBuilder.append("\"];");

			completeBuilder.append(nonIncludedBuilder.toString() + "\n");
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
			if (myArgs != null) {
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

	private void setupBorderColorMap() {
		this.addColorKey(this.borderColorMap, "Singleton", "blue");
	}

	private void setupFillColorMap() {
		this.addColorKey(this.fillColorMap, "adapter", "firebrick2");
		this.addColorKey(this.fillColorMap, "adaptee", "firebrick2");
		this.addColorKey(this.fillColorMap, "target", "firebrick2");
		this.addColorKey(this.fillColorMap, "decorator", "chartreuse");
		this.addColorKey(this.fillColorMap, "component", "chartreuse");
	}

	private void addColorKey(HashMap<String, String> colorMap, String patternString, String colorString) {
		colorMap.put(patternString, colorString);
	}

	public static void addNonIncludedClass(String className) {
		if (!nonIncludedClasses.contains(className)) {
			nonIncludedClasses.add(className);
		}
	}

	public static void addPattern(String classString, String patternString) {
		if (patternLists.containsKey(classString)) {
			ArrayList<String> currentPatterns = patternLists.get(classString);
			if (!currentPatterns.contains(patternString)) {
				patternLists.get(classString).add(patternString);
			}
		} else {
			ArrayList<String> newList = new ArrayList<String>();
			newList.add(patternString);
			patternLists.put(classString, newList);
		}
	}

	public static boolean isBlacklisted(String s) {
		for (String blacklisted : BlacklistHolder.getInstance()) {
			if (s.contains(blacklisted)) {
				return true;
			}
		}
		return false;
	}

}
