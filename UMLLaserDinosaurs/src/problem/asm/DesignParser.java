package problem.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DesignParser {

	private static HashMap<String, DiagramMaker> diagramMap;

	public static void parse(String[] args, String fileName, String diagramKey) throws IOException {
		setUpHashMap();
		DiagramMaker maker = new NoSuchDiagramMaker();
		if(diagramMap.containsKey(diagramKey)){
			maker= diagramMap.get(diagramKey);
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

}