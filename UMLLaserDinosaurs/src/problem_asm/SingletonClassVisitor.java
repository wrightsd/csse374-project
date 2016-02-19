package problem_asm;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class SingletonClassVisitor extends ClassVisitor {
	
	private static HashMap<String, HashMap> fieldMaps = new HashMap<String, HashMap>();
	private static HashMap<String,Boolean> requireGetInstanceMap = new HashMap<String,Boolean>();
	private boolean fieldSingletonCriteria;
	private boolean methodSingletonCriteria;
	private static HashMap fieldValues = new HashMap();

	public SingletonClassVisitor(int arg0, ClassVisitor arg1) {
		super(arg0, arg1);
		setupFieldMaps();
		HashMap<String, String> fieldSetters = UMLMaker.getFieldSelection("singleton");
		for(String s: fieldSetters.keySet()){
			System.out.println(s);
			if(fieldMaps.containsKey(s)){
				HashMap valueMap = fieldMaps.get(s);
				System.out.println(Arrays.toString(fieldMaps.keySet().toArray()));
				if(valueMap.containsKey(fieldSetters.get(s))){
					try {
						fieldValues.put(s, valueMap.get(fieldSetters.get(s)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		this.fieldSingletonCriteria = false;
		this.methodSingletonCriteria = false;
		System.out.println(fieldValues.get("requireGetInstance"));
	}

	private static void setupFieldMaps() {
		fieldMaps.put("requireGetInstance", requireGetInstanceMap);
		requireGetInstanceMap.put("true",true);
		requireGetInstanceMap.put("false",false);
		fieldValues.put("requireGetInstance", true);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);

		if ((access & Opcodes.ACC_STATIC) >= 1) {
			if (Type.getType(desc).getClassName().equals(DesignParser.getCurrentClass())) {
				this.fieldSingletonCriteria = true;
				if(!(Boolean)(fieldValues.get("requireGetInstance"))){
					UMLMaker.addPattern(DesignParser.getCurrentClass(),"singleton", "singleton", DesignParser.getCurrentClass());
				}
				else if (this.methodSingletonCriteria) {
					UMLMaker.addPattern(DesignParser.getCurrentClass(),"singleton", "singleton", DesignParser.getCurrentClass());
				}
			}
		}

		return toDecorate;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);

		if ((access & Opcodes.ACC_STATIC) >= 1) {
			if (Type.getArgumentTypes(desc).length == 0) {
				if (Type.getReturnType(desc).getClassName().equals(DesignParser.getCurrentClass())) {
					this.methodSingletonCriteria = true;
					if (this.fieldSingletonCriteria) {
						UMLMaker.addPattern(DesignParser.getCurrentClass(),"singleton", "singleton", DesignParser.getCurrentClass());
					}
				}
			}
		}

		return toDecorate;
	}

}
