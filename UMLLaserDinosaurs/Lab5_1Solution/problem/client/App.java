package problem.client;

import java.util.ArrayList;
import java.util.Enumeration;

import problem.lib.LinearTransformer;

public class App {
	public static void main(String[] args) throws Exception {
		ArrayList<String> vect = new ArrayList<String>();
		for(int i = 1; i <= 3; ++i) {
			vect.add("Tick Tick " + i);
		}

		Enumeration<String> etion = new IteratorToEnumerationAdapter<String>(vect.iterator());
		LinearTransformer<String> lT = new LinearTransformer<String>(etion);
		lT.transform(System.out);
	}
}
