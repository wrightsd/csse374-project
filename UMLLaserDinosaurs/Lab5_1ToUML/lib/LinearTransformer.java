package problem.lib;

import java.io.OutputStream;
import java.util.Enumeration;

public class LinearTransformer<T> {
	private Enumeration<T> enumTion;
	
	public LinearTransformer(Enumeration<T> e) {
		this.enumTion = e;
	}
	
	public void transform(OutputStream s) throws Exception {
		s.write("Transformation in progress ...\n".getBytes());
		int i = 0;
		while(enumTion.hasMoreElements()) {
			T elem = enumTion.nextElement();
			
			s.write((++i + "").getBytes());
			s.write(" : ".getBytes());
			s.write(elem.toString().getBytes());
			s.write('\n');
		}
		s.write("Transformation complete!\n".getBytes());
		s.flush();
	}
}
