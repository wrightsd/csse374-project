package problem_asm;

import java.io.IOException;
import java.util.ArrayList;

public interface DiagramMaker {
	
	public StringBuilder generateDiagramText(String[] args) throws IOException;

	public String getCurrentClass();

	public ArrayList<String> getArguments();

}
