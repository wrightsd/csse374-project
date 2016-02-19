package problem_asm;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class UMLMaker implements DiagramMaker {

	private static String currentClass;
	private ArrayList<String> classSet;
	private static String[] myArgs;
	private ArrayList<String> usesList;
	private ArrayList<String> associatesList;
	private HashMap<String, String> borderColorMap = new HashMap<String, String>();
	private HashMap<String, String> fillColorMap = new HashMap<String, String>();
	private static HashMap<String, Class<? extends ClassVisitor>> patternMap = new HashMap<String, Class<? extends ClassVisitor>>();
	private static HashMap<String, HashMap<String, StringBuilder>> classInfo = new HashMap<String, HashMap<String, StringBuilder>>();

	private static ArrayList<String> nonIncludedClasses = new ArrayList<String>();

	private static HashMap<String, ArrayList<String[]>> patternLists = new HashMap<String, ArrayList<String[]>>();

	private static StringBuilder labelledArrows = new StringBuilder();

	private static HashMap<String, ArrayList<String>> classExtensions = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, ArrayList<String>> interfaceExtensions = new HashMap<String, ArrayList<String>>();

	private static HashMap<String, ArrayList<ArrayList<String>>> classMethodMap = new HashMap<String, ArrayList<ArrayList<String>>>();

	private static HashMap<String, ClassVisitor> patternVisitors = new HashMap<String, ClassVisitor>();
	private static HashMap<String, HashMap<String, String>> fieldIndicators = new HashMap<String, HashMap<String, String>>();

	@Override
	public StringBuilder generateDiagramText(String[] args) throws IOException {
		myArgs = args;
		this.setupBorderColorMap();
		this.setupFillColorMap();
		this.setupPatternMap();

		ArrayList<String> selectedPatterns = DesignParser.getPatterns();

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

			ClassVisitor recentVisitor = associationVisitor;
			for (String s : selectedPatterns) {
				if (this.patternMap.containsKey(s)) {
					try {
						Constructor c = patternMap.get(s).getConstructor(int.class, ClassVisitor.class);
						ClassVisitor cv = (ClassVisitor) c.newInstance(Opcodes.ASM5, recentVisitor);
						recentVisitor = cv;
						patternVisitors.put(s, cv);
					} catch (Exception e) {
					}
				} else {
					throw new UnsupportedOperationException("Pattern Not Included");
				}
			}

			reader.accept(recentVisitor, ClassReader.EXPAND_FRAMES);

			HashMap<String, StringBuilder> builderList = new HashMap<String, StringBuilder>();
			builderList.put("field", fieldBuilder);
			builderList.put("method", methodBuilder);
			builderList.put("interface", interfaceBuilder);
			builderList.put("arrows", arrowBuilder);

			classInfo.put(className, builderList);
		}

		for (String s : selectedPatterns) {
			if (this.patternMap.containsKey(s)) {
				try {

					try {
						Method m = this.patternMap.get(s).getMethod("finishPatternFinder", null);
						m.invoke(patternVisitors.get(s), null);
					} catch (Exception e) {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				throw new UnsupportedOperationException("Pattern Not Included");
			}
		}

		for (String className : args) {

			StringBuilder methodBuilder = classInfo.get(className).get("method");
			StringBuilder fieldBuilder = classInfo.get(className).get("field");
			StringBuilder arrowBuilder = classInfo.get(className).get("arrows");
			StringBuilder interfaceBuilder = classInfo.get(className).get("interface");

			completeBuilder.append(ArbitraryNodeNames.getInstance().getNodeName(className) + " [\nshape=\"record\",\n");
			ArrayList<String[]> patternBuilder = patternLists.get(className);
			if (patternBuilder != null) {
				for (String[] array : patternBuilder) {
					String pattern = array[0];
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
				ArrayList<String> listOfAlreadyShownUpPatterns = new ArrayList<String>();
				for (String[] array : patternBuilder) {
					String pattern = array[0];
					if (!listOfAlreadyShownUpPatterns.contains(pattern)) {
						completeBuilder.append("\\n\\<\\<" + pattern + "\\>\\>");
						listOfAlreadyShownUpPatterns.add(pattern);
					}
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

		// for (String s : nonIncludedClasses) {
		// StringBuilder nonIncludedBuilder = new StringBuilder();
		// nonIncludedBuilder.append(ArbitraryNodeNames.getInstance().getNodeName(s)
		// + "[\n");
		// ArrayList<String[]> patternBuilder = patternLists.get(s);
		// if (patternBuilder != null) {
		// for (String[] array : patternBuilder) {
		// String pattern = array[0];
		//
		// if (borderColorMap.containsKey(pattern)) {
		// nonIncludedBuilder.append("color =" + borderColorMap.get(pattern));
		// nonIncludedBuilder.append(",\n");
		// }
		// if (fillColorMap.containsKey(pattern)) {
		// nonIncludedBuilder.append("style = filled,\n");
		// nonIncludedBuilder.append("fillcolor =" + fillColorMap.get(pattern));
		// nonIncludedBuilder.append(",\n");
		// }
		// }
		// }
		//
		// nonIncludedBuilder.append("label = \"" + s);
		// ArrayList<String[]> patterns = patternLists.get(s);
		// if (patterns != null) {
		// for (String[] pattern : patternLists.get(s)) {
		// nonIncludedBuilder.append("\\n\\<\\<" + pattern[0] + "\\>\\>");
		// }
		// }
		// nonIncludedBuilder.append("\"];");
		//
		// completeBuilder.append(nonIncludedBuilder.toString() + "\n");
		// }

		drawUsesArrows(usesList, associatesList, associationBuilder);
		completeBuilder.append(labelledArrows.toString());
		completeBuilder.append(associationBuilder.toString());
		completeBuilder.append("}");

		// for (String s : patternLists.keySet()) {
		// System.out.println("Key: " + s);
		// for (String[] arr : patternLists.get(s)) {
		// System.out.println("\t + " + Arrays.toString(arr));
		// }
		// }

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

	private void setupPatternMap() {
		this.patternMap.put("singleton", SingletonClassVisitor.class);
		this.patternMap.put("adapter", AdapterManagementVisitor.class);
		this.patternMap.put("decorator", DecoratorClassVisitor.class);
		this.patternMap.put("composite", CompositeVisitor.class);
	}

	public static void addPatternToPatternMap(String pattern, Class<? extends ClassVisitor> cvClass) {
		patternMap.put(pattern, cvClass);
	}

	public void setupBorderColorMap() {
		this.addColorKey(this.borderColorMap, "singleton", "blue");
	}

	private void setupFillColorMap() {
		this.addColorKey(this.fillColorMap, "adapter", "firebrick2");
		this.addColorKey(this.fillColorMap, "adaptee", "firebrick2");
		this.addColorKey(this.fillColorMap, "target", "firebrick2");
		this.addColorKey(this.fillColorMap, "decorator", "chartreuse");
		this.addColorKey(this.fillColorMap, "component", "chartreuse");
		this.addColorKey(this.fillColorMap, "composite component", "yellow1");
		this.addColorKey(this.fillColorMap, "composite", "yellow1");
		this.addColorKey(this.fillColorMap, "leaf", "yellow1");
	}

	public void addColorKey(HashMap<String, String> colorMap, String patternString, String colorString) {
		colorMap.put(patternString, colorString);
	}

	// public static void addNonIncludedClass(String className) {
	// if (!nonIncludedClasses.contains(className)) {
	// nonIncludedClasses.add(className);
	// }
	// }

	public static void addPattern(String classString, String patternString, String patternType,
			String specificPatternName) {
		if (patternLists.containsKey(classString)) {
			ArrayList<String[]> currentPatterns = patternLists.get(classString);
			boolean alreadyThere = false;
			for (String[] pattern : currentPatterns) {
				if (pattern[0].equals(patternString) && pattern[2].equals(specificPatternName)) {
					alreadyThere = true;
				}
			}
			if (!alreadyThere) {
				String[] newArray = { patternString, patternType, specificPatternName };
				currentPatterns.add(newArray);
			}

		} else {
			ArrayList<String[]> newList = new ArrayList<String[]>();
			String[] newArray = { patternString, patternType, specificPatternName };
			newList.add(newArray);
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

	public static void addLabelledArrow(String pointee, String pointer, String labelText) {
		if (isArgument(pointee) && isArgument(pointer)) {
			String pointeeName = ArbitraryNodeNames.getInstance().getNodeName(pointee);
			// if (pointeeName == null) {
			// ArbitraryNodeNames.getInstance().addNewNode(pointee);
			// addNonIncludedClass(pointee);
			// pointeeName =
			// ArbitraryNodeNames.getInstance().getNodeName(pointee);
			// }
			String pointerName = ArbitraryNodeNames.getInstance().getNodeName(pointer);
			// if (pointerName == null) {
			// ArbitraryNodeNames.getInstance().addNewNode(pointer);
			// addNonIncludedClass(pointer);
			// pointerName =
			// ArbitraryNodeNames.getInstance().getNodeName(pointer);
			// }
			if (pointeeName != null && pointerName != null) {
				if (pointerName.equals("null")) {
					return;
				}
				if (!labelledArrows.toString().contains(pointeeName + "->" + pointerName
						+ "[arrowhead=\"normal\", style=\"solid\"" + ", label=\"" + labelText + "\"];\n")) {
					labelledArrows.append(pointeeName + "->" + pointerName);
					labelledArrows.append("[arrowhead=\"normal\", style=\"solid\"");
					labelledArrows.append(", label=\"" + labelText + "\"];\n");
				}
			}
		}
	}

	public static void addExtendsArrow(String pointee, String pointer) {
		if (isArgument(pointee) && isArgument(pointer)) {
			String pointeeName = ArbitraryNodeNames.getInstance().getNodeName(pointee);
			// if (pointeeName == null) {
			// ArbitraryNodeNames.getInstance().addNewNode(pointee);
			// addNonIncludedClass(pointee);
			// pointeeName =
			// ArbitraryNodeNames.getInstance().getNodeName(pointee);
			// }
			String pointerName = ArbitraryNodeNames.getInstance().getNodeName(pointer);
			// if (pointerName == null) {
			// ArbitraryNodeNames.getInstance().addNewNode(pointer);
			// addNonIncludedClass(pointer);
			// pointerName =
			// ArbitraryNodeNames.getInstance().getNodeName(pointer);
			// }
			if (pointeeName != null && pointerName != null) {
				if (pointerName.equals("null")) {
					return;
				}
				labelledArrows.append(pointeeName + "->" + pointerName);
				labelledArrows.append("[arrowhead=\"onormal\", style=\"solid\"]\n");
			}
		}
	}

	public static void addImplementsArrow(String pointee, String pointer) {
		if (isArgument(pointee) && isArgument(pointer)) {
			String pointeeName = ArbitraryNodeNames.getInstance().getNodeName(pointee);
			// if (pointeeName == null) {
			// ArbitraryNodeNames.getInstance().addNewNode(pointee);
			// addNonIncludedClass(pointee);
			// pointeeName =
			// ArbitraryNodeNames.getInstance().getNodeName(pointee);
			// }
			String pointerName = ArbitraryNodeNames.getInstance().getNodeName(pointer);
			// if (pointerName == null) {
			// ArbitraryNodeNames.getInstance().addNewNode(pointer);
			// addNonIncludedClass(pointer);
			// pointerName =
			// ArbitraryNodeNames.getInstance().getNodeName(pointer);
			// }
			if (pointeeName != null && pointerName != null) {
				if (pointerName.equals("null")) {
					return;
				}
				labelledArrows.append(pointeeName + "->" + pointerName);
				labelledArrows.append("[arrowhead=\"onormal\", style=\"dashed\"]\n");
			}
		}
	}

	public static void addClassExtensionMapping(String classString, String extensionClass) {
		if (classExtensions.containsKey(classString)) {
			if (classExtensions.get(classString).contains(extensionClass)) {
				return;
			}
			classExtensions.get(classString).add(extensionClass);
		} else {
			ArrayList<String> classExtensionList = new ArrayList<String>();
			classExtensionList.add(extensionClass);
			classExtensions.put(classString, classExtensionList);
		}
	}

	public static void addInterfaceExtensionMapping(String classString, String extensionClass) {
		if (interfaceExtensions.containsKey(classString)) {
			if (interfaceExtensions.get(classString).contains(extensionClass)) {
				return;
			}
			interfaceExtensions.get(classString).add(extensionClass);
		} else {
			ArrayList<String> classExtensionList = new ArrayList<String>();
			classExtensionList.add(extensionClass);
			interfaceExtensions.put(classString, classExtensionList);
		}
	}

	public static void addMethodsToList(String classKey, ArrayList<String> listToAdd) {
		if (classMethodMap.containsKey(classKey)) {
			classMethodMap.get(classKey).add(listToAdd);
		} else {
			ArrayList<ArrayList<String>> newestList = new ArrayList<ArrayList<String>>();
			newestList.add(listToAdd);
			classMethodMap.put(classKey, newestList);
		}
	}

	public static HashMap<String, ArrayList<String[]>> getPatternLists() {
		return patternLists;
	}

	public static HashMap<String, ArrayList<String>> getInterfaceExtensions() {
		return interfaceExtensions;
	}

	public static HashMap<String, ArrayList<String>> getClassExtensions() {
		return classExtensions;
	}

	public static HashMap<String, ArrayList<ArrayList<String>>> getClassMethodMap() {
		return classMethodMap;
	}

	public static boolean isArgument(String superName) {
		for (String s : myArgs) {
			if (s.equals(superName)) {
				return true;
			}
		}
		return false;
	}

	public static HashMap<String, String> getFieldSelection(String string) {
		return fieldIndicators.get("singleton");
	}

	public static void setFieldIndicator(String pattern, String field, String valueKey) {
		HashMap<String, String> fieldValues = new HashMap<String, String>();
		if (fieldIndicators.containsKey(pattern)) {
			fieldValues = fieldIndicators.get(pattern);
		}
		fieldValues.put(field, valueKey);
		fieldIndicators.put(pattern, fieldValues);
	}

}
