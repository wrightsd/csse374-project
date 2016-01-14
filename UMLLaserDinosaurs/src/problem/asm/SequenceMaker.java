package problem.asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

public class SequenceMaker implements DiagramMaker {

	private static HashMap<String, String> classTags;

	public StringBuilder generateDiagramText(String[] args) throws IOException {
		StringBuilder classesBuilder = new StringBuilder();
		StringBuilder methodsBuilder = new StringBuilder();
		ClassSequenceVisitor classSequenceVisitor = new ClassSequenceVisitor();
		return null;

	}

	@Override
	public String getCurrentClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getArguments() {
		return new ArrayList<String>();
	}

	public void addClassTag(String className, String classTag) {
		if (classTags == null) {
			classTags = new HashMap<String, String>();
		}
		classTags.put(className, classTag);
	}

	public String getClassTag(String className) {
		if (classTags == null || !classTags.containsKey(className)) {
			return "";
		}
		return classTags.get(className);
	}

	public ArrayList<StringBuilder> recursiveSequenceGenerator(String args[], StringBuilder classSequenceBuilder,
			StringBuilder methodSequenceBuilder) throws IOException {
		int depth = 5;
		int startIndex = 0;
		try {
			depth = Integer.parseInt(args[0]);
			startIndex++;
		} catch (Exception e) {}
		String className = args[startIndex++];
		ClassReader reader = new ClassReader(className);
		String methodName = args[startIndex++];
		int newSize = args.length - startIndex;
		String[] parameters = new String[newSize];
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = args[i + startIndex];
		}
		ClassSequenceVisitor visitor = new ClassSequenceVisitor(Opcodes.ASM5, depth, methodName, parameters);
		
		ArrayList<StringBuilder> listOfBuilders = new ArrayList<StringBuilder>();
		listOfBuilders.add(classSequenceBuilder);
		listOfBuilders.add(methodSequenceBuilder);
		return listOfBuilders;
	}

}
