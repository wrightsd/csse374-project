package problem.asm;

import java.io.FileOutputStream;
import java.io.IOException;

public class DesignParser {

	public static void parse(String[] args, String fileName) throws IOException {
		StringBuilder completeBuilder = UMLMaker.generateDiagramText(args);
		FileOutputStream writer = new FileOutputStream(fileName);
		writer.write(completeBuilder.toString().getBytes());
		writer.close();
	}

}