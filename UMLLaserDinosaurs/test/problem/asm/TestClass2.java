package problem.asm;

import java.util.ArrayList;

public class TestClass2 {
	private ArrayList<String> testing;
	private TestClass t;
	
	public TestClass2(String s){
		functionsOne("hi", t);
		makeNewOne();
		testing = new ArrayList<String>();
	}
	
	public void functionsOne(String given, TestClassSuper ts){
		return;
	}
	
	public void makeNewOne(){
		AnotherTestClass thingToTest = new AnotherTestClass(0, new TestClass(0));
		thingToTest.doTest(new TestClass(1));
	}

}
