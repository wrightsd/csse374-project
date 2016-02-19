package problem_asm;

import java.util.HashMap;

public class ArbitraryNodeNames {

	private static ArbitraryNodeNames names;
	private HashMap<String, String> nameMap;
	private int nodeNumber;

	private ArbitraryNodeNames() {
		this.nameMap = new HashMap<String, String>();
		this.nodeNumber = 0;
	}

	public static ArbitraryNodeNames getInstance() {
		if (names == null) {
			names = new ArbitraryNodeNames();
		}
		return names;
	}

	public void addNewNode(String className) {
		if (!this.nameMap.containsKey(className)) {
			this.nameMap.put(className, "n" + Integer.toString(nodeNumber));
			nodeNumber++;
		}
	}

	public String getNodeName(String className) {
		return this.nameMap.get(className);
	}

}
