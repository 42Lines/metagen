package org.ftlines.metagen.eclipse.model;

import java.util.HashMap;
import java.util.Map;

public class Bean extends BeanContainer {
	private final String name;

	private boolean forced;

	private Map<String, Property> properties;

	private Visibility visibility;

	public Bean(Visibility visibility, String name) {
		this.name = name;
		this.visibility = visibility;
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
			properties = new HashMap<String, Property>();
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
