package problem.asm;

import java.util.ArrayList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class CompositeVisitor extends ClassVisitor {
	
	private ArrayList<ArrayList<String>> compositeComponents;

	public CompositeVisitor(int arg0, ClassVisitor arg1, ArrayList<ArrayList<String>> compositeComponents) {
		super(arg0, arg1);
		this.compositeComponents = compositeComponents;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		Type[] argTypes = Type.getArgumentTypes(desc);
		ArrayList<String> classNames = new ArrayList<String>();
		for (int i = 0; i < argTypes.length; i++) {
			classNames.add(argTypes[i].getClassName());
		}
		if(classNames.contains(DesignParser.getCurrentClass())){
			UMLMaker.addPattern(DesignParser.getCurrentClass(), "composite component");
			ArrayList<String> newList = new ArrayList<String>();
			newList.add(DesignParser.getCurrentClass());
			this.compositeComponents.add(newList);
		}
		return toDecorate;
	}

}
