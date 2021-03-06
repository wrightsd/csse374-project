package problem_asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class AdapterClassVisitor extends ClassVisitor {

	private ArrayList<String> checkMethods = new ArrayList<String>();
	private ArrayList<String> superMethods = new ArrayList<String>();
	private ArrayList<String> interfaceMethods = new ArrayList<String>();
	private ArrayList<String> attemptedMethods = new ArrayList<String>();
	private String nameOfSuper;
	private String[] interfaces;
	private String param = "";
	private ArrayList<String> fieldList = new ArrayList<String>();
	private ClassReader reader;
	private ArrayList<String> attemptedInterfaceMethods = new ArrayList<String>();

	public AdapterClassVisitor(int api) {
		super(api);
		// TODO Auto-generated constructor stub
	}

	public AdapterClassVisitor(int api, ClassVisitor visitor) {
		super(api, visitor);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		if (superName != null) {
			this.nameOfSuper = superName.replaceAll("/", ".");
			try {
				reader = new ClassReader(superName);
				ClassVisitor visitor = new MethodGetterVisitor(Opcodes.ASM5, superMethods);
				reader.accept(visitor, ClassReader.EXPAND_FRAMES);
			} catch (IOException e) {
			}
		}
		if (null != interfaces && interfaces.length == 1) {
			this.interfaces = interfaces;
			try {
				reader = new ClassReader(interfaces[0]);
				ClassVisitor visitor = new MethodGetterVisitor(Opcodes.ASM5, interfaceMethods);
				reader.accept(visitor, ClassReader.EXPAND_FRAMES);
			} catch (IOException e) {
			}
		}

		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		String type = Type.getType(desc).getClassName();
		fieldList.add(type);
		return toDecorate;
	};

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);
		if (null == nameOfSuper && null == this.interfaces) {
			return toDecorate;
		}
		if (name.equals("<init>")) {
			Type[] args = Type.getArgumentTypes(desc);
			if (args.length != 1) {
				return toDecorate;
			}
			if (fieldList.contains(args[0].getClassName())) {
				param = args[0].getClassName();
			} else {
				return toDecorate;
			}
		} else if ((access & Opcodes.ACC_PUBLIC) > 0) {
			if (superMethods.contains(name + desc)) {
				attemptedMethods.add(name + desc);
				MethodVisitor adapterVisitor = new AdapterMethodVisitor(Opcodes.ASM5, this, param, name + desc);
				return adapterVisitor;
			}
			if (interfaceMethods.contains(name + desc)) {
				attemptedInterfaceMethods.add(name + desc);
				MethodVisitor adapterVisitor = new AdapterMethodVisitor(Opcodes.ASM5, this, param, name + desc);
				return adapterVisitor;
			}
		}
		return toDecorate;
	}

	public void verify(String nameDesc) {
		checkMethods.add(nameDesc);
	}

	public void assignAdaption() {

		boolean interfaceTarget = false;
		if (attemptedInterfaceMethods.size() > 0) {
			interfaceTarget = true;
		}

		for (int i = 0; i < attemptedInterfaceMethods.size(); i++) {
			if (!checkMethods.contains(attemptedInterfaceMethods.get(i))) {
				interfaceTarget = false;
				break;
			}
		}
		if (interfaceTarget) {
			if (!interfaces[0].replaceAll("/", ".").equals(this.param.replaceAll("/", "."))) {
				UMLMaker.addPattern(interfaces[0].replaceAll("/", "."), "target", "adapter",
						DesignParser.getCurrentClass());
				UMLMaker.addPattern(DesignParser.getCurrentClass(), "adapter", "adapter",
						DesignParser.getCurrentClass());
				UMLMaker.addPattern(this.param.replaceAll("/", "."), "adaptee", "adapter",
						DesignParser.getCurrentClass());
				UMLMaker.addLabelledArrow(DesignParser.getCurrentClass(), interfaces[0].replaceAll("/", "."),
						"\\<\\<adapts\\>\\>");
			}
		} else {

			boolean superTarget = false;
			if (attemptedMethods.size() > 0) {
				superTarget = true;
			}
			for (int i = 0; i < attemptedMethods.size(); i++) {
				if (!checkMethods.contains(attemptedMethods.get(i))) {
					superTarget = false;
					break;
				}
			}
			if (superTarget) {
				if (!this.nameOfSuper.equals(this.param.replaceAll("/", "."))) {
					UMLMaker.addPattern(this.nameOfSuper, "target", "adapter", DesignParser.getCurrentClass());
					UMLMaker.addPattern(DesignParser.getCurrentClass(), "adapter", "adapter",
							DesignParser.getCurrentClass());
					UMLMaker.addPattern(this.param, "adaptee", "adapter", DesignParser.getCurrentClass());
					UMLMaker.addLabelledArrow(DesignParser.getCurrentClass(), this.nameOfSuper, "\\<\\<adapts\\>\\>");
				}
			}
		}
	}

}
