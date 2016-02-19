package problem_asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DesignParser {

	private static HashMap<String, DiagramMaker> diagramMap;
	private static ArrayList<String> patternKeys;
	private static DiagramMaker maker;

	public static void parse(String[] args, String fileName, String diagramKey, ArrayList<String> inputPatterns) throws IOException {
		setUpHashMap();
		maker = new NoSuchDiagramMaker();
		if(diagramMap.containsKey(diagramKey)){
			maker = diagramMap.get(diagramKey);
		}
		if(inputPatterns != null){
			patternKeys = inputPatterns;
		}
		StringBuilder completeBuilder = maker.generateDiagramText(args);
		FileOutputStream writer = new FileOutputStream(fileName);
		writer.write(completeBuilder.toString().getBytes());
		writer.close();
	}

	private static void setUpHashMap() {
		if (diagramMap == null) {
			diagramMap = new HashMap<String, DiagramMaker>();
			diagramMap.put("uml", new UMLMaker());
			diagramMap.put("seq", new SequenceMaker());
		}
		
	}

	public static void addDiagramType(String diagramName, DiagramMaker diagramMaker) {
		setUpHashMap();
		diagramMap.put(diagramName, diagramMaker);
	}

	public static String getCurrentClass() {
		return maker.getCurrentClass();
	}
	
	public static ArrayList<String> getArguments() {
		return maker.getArguments();
	}
	
	public static ArrayList<String> getPatterns() {
		return patternKeys;
	}

}