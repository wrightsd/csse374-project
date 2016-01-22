package problem.asm;

public class LazySingletonSample {
	private static LazySingletonSample s;
	
	public static LazySingletonSample getInstance() {
		if (s == null) {
			s = new LazySingletonSample();
		}
		return s;
	}
}
