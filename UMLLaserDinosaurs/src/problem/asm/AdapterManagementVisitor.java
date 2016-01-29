package problem.asm;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class AdapterManagementVisitor extends ClassVisitor {

	public AdapterManagementVisitor(int arg0, ClassVisitor visitor) {
		super(arg0, visitor);
		AdapterClassVisitor newVisitor = new AdapterClassVisitor(Opcodes.ASM5);
		try {
			ClassReader reader = new ClassReader(DesignParser.getCurrentClass());
			reader.accept(newVisitor, ClassReader.EXPAND_FRAMES);
			newVisitor.assignAdaption();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
