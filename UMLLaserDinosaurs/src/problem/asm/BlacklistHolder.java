package problem.asm;

import java.util.ArrayList;

public class BlacklistHolder {
	
	private static ArrayList<String> blacklist;
	
	public static ArrayList<String> getInstance(){
		if(blacklist == null){
			blacklist = new ArrayList<String>();
			blacklist.add("java.lang.Object");
		}
		return blacklist;
	}

}
