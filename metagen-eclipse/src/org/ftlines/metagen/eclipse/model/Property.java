package org.ftlines.metagen.eclipse.model;

import java.util.HashMap;
import java.util.Map;

public class Property {

	private static final Map<String, String> PRIMITIVE_TO_NONPRIMITIVE;
	private static final Map<String, String> UNSAFE_NAMES_TO_SAFE;

	static {
		PRIMITIVE_TO_NONPRIMITIVE = new HashMap<String, String>();
		PRIMITIVE_TO_NONPRIMITIVE.put("int", "java.lang.Integer");
		PRIMITIVE_TO_NONPRIMITIVE.put("long", "java.lang.Long");
		PRIMITIVE_TO_NONPRIMITIVE.put("float", "java.lang.Float");
		PRIMITIVE_TO_NONPRIMITIVE.put("double", "java.lang.Double");
		PRIMITIVE_TO_NONPRIMITIVE.put("short", "java.lang.Short");
		PRIMITIVE_TO_NONPRIMITIVE.put("void", "java.lang.Void");
		PRIMITIVE_TO_NONPRIMITIVE.put("byte", "java.lang.Byte");
		PRIMITIVE_TO_NONPRIMITIVE.put("boolean", "java.lang.Boolean");
		PRIMITIVE_TO_NONPRIMITIVE.put("char", "java.lang.Character");

		UNSAFE_NAMES_TO_SAFE = new HashMap<String, String>();
		UNSAFE_NAMES_TO_SAFE.put("public", "public_");
		UNSAFE_NAMES_TO_SAFE.put("protected", "protected_");
		UNSAFE_NAMES_TO_SAFE.put("package", "package_");
		UNSAFE_NAMES_TO_SAFE.put("final", "final_");
		
	}

	private String type;
	private String fieldName, getterName, setterName;
	private final String name;

	private boolean deprecated;
	private Visibility visibility;

	public Property(Visibility visibility, String type, String name) {
		this.visibility = visibility;
		this.type = type;
		this.name = name;
	}

	public String getBoxedType() {
		String boxed = PRIMITIVE_TO_NONPRIMITIVE.get(type);
		return boxed != null ? boxed : type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getSafeName() {
		if (UNSAFE_NAMES_TO_SAFE.containsKey(name)) {
			return UNSAFE_NAMES_TO_SAFE.get(name);
		}
		return name;
	}

	public String getGetterName() {
		return getterName;
	}

	public String getSetterName() {
		return setterName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setGetterName(String getterName) {
		this.getterName = getterName;
	}

	public void setSetterName(String setterName) {
		this.setterName = setterName;
	}

	public String getName() {
		return name;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public String getType() {
		return type;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public void relaxVisibility(Visibility other) {
		this.visibility = this.visibility.relax(other);
	}

}
