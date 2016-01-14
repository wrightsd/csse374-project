package problem.asm;

import java.io.IOException;

public class NoSuchDiagramMaker implements DiagramMaker {

	@Override
	public StringBuilder generateDiagramText(String[] args) throws IOException {
		return new StringBuilder();
	}

}
