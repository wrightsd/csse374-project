package problem.asm;

public class TestClass extends TestClassSuper implements TestInterface {

	private int testField1 = 0;
	public String testField2 = "";
	protected char[] testField3 = {};

	public TestClass(int i) {
	}

	protected void testMethod1() {
		testMethod2(0);
	}

	private String testMethod2(int i) {
		return "";
	}

}
