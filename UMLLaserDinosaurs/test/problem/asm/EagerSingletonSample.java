package problem.asm;

public class EagerSingletonSample {
	private static EagerSingletonSample s = new EagerSingletonSample();
	
	public static EagerSingletonSample getInstance() {
		return s;
	}
}
