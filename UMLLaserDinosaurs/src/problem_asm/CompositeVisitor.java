package problem_asm;

import java.util.ArrayList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class CompositeVisitor extends ClassVisitor {

	private static ArrayList<ArrayList<String>> compositeComponents;

	public CompositeVisitor(int arg0, ClassVisitor arg1) {
		super(arg0, arg1);
		compositeComponents = new ArrayList<ArrayList<String>>();
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		Type[] argTypes = Type.getArgumentTypes(desc);
		ArrayList<String> classNames = new ArrayList<String>();
		for (int i = 0; i < argTypes.length; i++) {
			classNames.add(argTypes[i].getClassName());
		}
		if (classNames.contains(DesignParser.getCurrentClass()) && (access & Opcodes.ACC_PUBLIC) > 0) {
			UMLMaker.addPattern(DesignParser.getCurrentClass(), "composite component", "composite",
					DesignParser.getCurrentClass());
			ArrayList<String> newList = new ArrayList<String>();
			newList.add(DesignParser.getCurrentClass());
			this.compositeComponents.add(newList);
		}
		return toDecorate;
	}

	public static void finishPatternFinder() {
		boolean loop = true;
		while (loop) {
			ArrayList<Integer> tempCompositesSize = new ArrayList<Integer>();
			for (int i = 0; i < compositeComponents.size(); i++) {
				tempCompositesSize.add(compositeComponents.get(i).size());
			}
			loop = false;
			for (String s : UMLMaker.getClassExtensions().keySet()) {
				String mainComposite = "";
				ArrayList<String> extendedClasses = UMLMaker.getClassExtensions().get(s);
				for (String e : extendedClasses) {
					ArrayList<String[]> patternList = UMLMaker.getPatternLists().get(e);
					if (patternList != null) {
						for (int j = 0; j < patternList.size(); j++) {
							if (patternList.get(j)[0].equals("composite component")) {
								ArrayList<ArrayList<String>> listOfLists = UMLMaker.getClassMethodMap().get(s);
								ArrayList<ArrayList<String>> parentList = UMLMaker.getClassMethodMap().get(e);
								boolean leaf = true;
								for (int i = 0; i < listOfLists.size(); i++) {
									String params = listOfLists.get(i).get(1);
									if (params.contains(e)) {
										for (ArrayList<String> list : parentList) {
											if (list.get(0).equals(listOfLists.get(i).get(0))
													&& list.get(1).equals(listOfLists.get(i).get(1))
													&& list.get(2).equals(listOfLists.get(i).get(2))) {
												UMLMaker.addPattern(s, "composite", "composite", s);
												mainComposite = s;
												leaf = false;
											}
										}
									}
								}
								if (leaf) {
									UMLMaker.addPattern(s, "leaf", "composite", mainComposite);
								}
								for (ArrayList<String> list : compositeComponents) {
									if (list.contains(e)) {
										if (!list.contains(s)) {
											list.add(s);
										}
									}
								}
							} else if (patternList.contains("composite")) {
								UMLMaker.addPattern(s, "composite", "composite", mainComposite);
								for (ArrayList<String> list : compositeComponents) {
									if (list.contains(e)) {
										if (!list.contains(s)) {
											list.add(s);
										}
									}
								}
							} else if (patternList.contains("leaf")) {
								UMLMaker.addPattern(s, "leaf", "composite", mainComposite);
								for (ArrayList<String> list : compositeComponents) {
									if (list.contains(e)) {
										if (!list.contains(s)) {
											list.add(s);
										}
									}
								}
							}
						}
					}
				}
			}

			for (int i = 0; i < tempCompositesSize.size(); i++) {
				if (tempCompositesSize.get(i) != compositeComponents.get(i).size()) {
					loop = true;
					break;
				}
			}

			for (String s : UMLMaker.getInterfaceExtensions().keySet()) {
				String mainComposite = "";
				ArrayList<String> extendedClasses = UMLMaker.getInterfaceExtensions().get(s);
				for (String e : extendedClasses) {
					// ArrayList<String[]> patternList = patternLists.get(e);
					if (UMLMaker.getPatternLists() != null && UMLMaker.getPatternLists().get(e) != null) {
						for (int j = 0; j < UMLMaker.getPatternLists().get(e).size(); j++) {
							if (UMLMaker.getPatternLists().get(e).get(j)[0].equals("composite component")) {
								ArrayList<ArrayList<String>> listOfLists = UMLMaker.getClassMethodMap().get(s);
								ArrayList<ArrayList<String>> parentList = UMLMaker.getClassMethodMap().get(e);
								boolean leaf = true;
								for (int i = 0; i < listOfLists.size(); i++) {
									String params = listOfLists.get(i).get(1);
									if (params.contains(e)) {
										for (ArrayList<String> list : parentList) {
											if (list.get(0).equals(listOfLists.get(i).get(0))
													&& list.get(1).equals(listOfLists.get(i).get(1))
													&& list.get(2).equals(listOfLists.get(i).get(2))) {
												UMLMaker.addPattern(s, "composite", "composite", s);
												mainComposite = s;
												leaf = false;
											}
										}
									}
								}
								if (leaf) {
									UMLMaker.addPattern(s, "leaf", "composite", mainComposite);
								}
								for (ArrayList<String> list : compositeComponents) {
									if (list.contains(e)) {
										if (!list.contains(s)) {
											list.add(s);
										}
									}
								}
							} else if (UMLMaker.getPatternLists().get(e).get(j)[0].contains("composite")) {
								UMLMaker.addPattern(s, "composite", "composite", mainComposite);
								for (ArrayList<String> list : compositeComponents) {
									if (list.contains(e)) {
										if (!list.contains(s)) {
											list.add(s);
										}
									}
								}
							} else if (UMLMaker.getPatternLists().get(e).get(j)[0].contains("leaf")) {
								UMLMaker.addPattern(s, "leaf", "composite", mainComposite);
								for (ArrayList<String> list : compositeComponents) {
									if (list.contains(e)) {
										if (!list.contains(s)) {
											list.add(s);
										}
									}
								}
							}
						}
					}
				}
			}
			for (int i = 0; i < tempCompositesSize.size(); i++) {
				if (tempCompositesSize.get(i) != compositeComponents.get(i).size()) {
					loop = true;
					break;
				}
			}
		}
	}
}
