package net.ftlines.metagen.processor.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;

public abstract class AbstractBean implements Node {
	private final TypeElement element;

	private final Map<TypeElement, NestedBean> nestedBeans = new HashMap<TypeElement, NestedBean>();

	private final Map<String, PropertyNode> properties = new HashMap<String, PropertyNode>();

	public AbstractBean(TypeElement element) {
		this.element = element;
	}

	public TypeElement getElement() {
		return element;
	}

	public Map<TypeElement, NestedBean> getNestedBeans() {
		return nestedBeans;
	}

	public Map<String, PropertyNode> getProperties() {
		return properties;
	}

	protected void visitProperties(Visitor visitor) {
		for (PropertyNode property : copyValues(properties)) {
			property.accept(visitor);
		}
	}

	protected void visitNestedBeans(Visitor visitor) {
		for (NestedBean bean : copyValues(nestedBeans)) {
			bean.accept(visitor);
		}
	}

	public static final <T> Collection<T> copyValues(Map<?, T> map) {
		return new ArrayList<T>(map.values());
	}

}
