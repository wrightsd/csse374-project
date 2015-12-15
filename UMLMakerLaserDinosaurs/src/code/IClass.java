package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


public class IClass {

	public static void main(String args[]) {
		new IClass().toString();
	}

	public IClass() {
		// hi
	}

	public String toString() {
		try {
			FileOutputStream writer = new FileOutputStream("output/output.txt");
			StringBuilder builder = new StringBuilder();
			builder.append("digraph text{\n");
			builder.append("rankdir=BT;\n");
			Class classType = this.getClass();
			String className = classType.getName();
			builder.append(className + " [\nshape=\"record\",\n");
			builder.append("label = \"[" + className + "|+ ");
			System.out.println(Modifier.toString(classType.getModifiers()));
			Constructor<?>[] constructors = classType.getConstructors();
			for (int i = 0; i < constructors.length; i++) {
				System.out.println(constructors[i].toString());
			}
			Method[] methods = classType.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				System.out.println(methods[i].toString());
			}
			classType.getFields();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
