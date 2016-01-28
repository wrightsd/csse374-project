package problem.asm;

import java.util.ArrayList;
import java.util.List;

public class TestForLoop {
	
	public void testMethod(){
		List<String> mylist = new ArrayList<String>();
		mylist.add("1");
		mylist.add("2");
		for(String s: mylist){
		    System.out.println("This is really an iterator! "+s);
		}
	}

}
