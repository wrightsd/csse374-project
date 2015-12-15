package problem.asm;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

public class FirstASM {
	
	public static String myField = "Hello World!";
	public static void main(String[] args) throws IOException{
		ClassReader reader = new ClassReader("java.lang.String");
		
		ClassVisitor visitor = new TraceClassVisitor(
				new PrintWriter(System.out));
		
		reader.accept(visitor, ClassReader.EXPAND_FRAMES);
	}

}
