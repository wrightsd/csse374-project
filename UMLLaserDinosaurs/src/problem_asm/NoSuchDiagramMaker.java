package problem_asm;

import java.io.IOException;
import java.util.ArrayList;

public class NoSuchDiagramMaker implements DiagramMaker {

	@Override
	public StringBuilder generateDiagramText(String[] args) throws IOException {
		return new StringBuilder();
	}

	@Override
	public String getCurrentClass() {
		return "";
	}

	@Override
	public ArrayList<String> getArguments() {
		return new ArrayList<String>();
	}

}
