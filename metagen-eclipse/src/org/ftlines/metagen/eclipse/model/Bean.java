package org.ftlines.metagen.eclipse.model;

import java.util.Map;
import java.util.TreeMap;

public class Bean extends BeanContainer {
	private final String name;

	private boolean forced;

	private TreeMap<String, Property> properties;

	private final Visibility visibility;

	private String superclass;

	public Bean(Visibility visibility, String name) {
		this.name = name;
		this.visibility = visibility;
	}

	public boolean willGenerateMeta() {
		return getProperties() != null || isForced() || (getBeans() != null && getBeans().size() > 0);
	}

	public String getSuperclass() {
		return superclass;
	}

	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}

	public boolean isForced() {
		return forced;
	}

	public void setForced(boolean forced) {
		this.forced = forced;
	}

	public String getName() {
		return name;
	}

	public String getSimpleName() {
		int dot = name.lastIndexOf(".");
		if (dot < 0) {
			return name;
		} else {
			return name.substring(dot + 1);
		}
	}

	public Property getProperty(String name) {
		return properties == null ? null : properties.get(name);
	}

	public void addProperty(Property property) {
		if (properties == null) {
			properties = new TreeMap<String, Property>();
		}
		properties.put(property.getName(), property);
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	public Visibility getVisibility() {
		return visibility;
	}
}
