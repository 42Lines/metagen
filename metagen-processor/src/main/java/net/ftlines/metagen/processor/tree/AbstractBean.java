package net.ftlines.metagen.processor.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.model.QualifiedName;

public abstract class AbstractBean implements Node {
	private final TypeElement element;

	private final Map<TypeElement, NestedBean> nestedBeans = new HashMap<TypeElement, NestedBean>();

	private final Map<String, Property> properties = new HashMap<String, Property>();

	public AbstractBean(TypeElement element) {
		this.element = element;
	}

	public TypeElement getElement() {
		return element;
	}

	public Map<TypeElement, NestedBean> getNestedBeans() {
		return nestedBeans;
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	protected void visitProperties(Visitor visitor) {
		for (Property property : copyValues(properties)) {
			property.accept(visitor);
		}
	}

	protected void visitNestedBeans(Visitor visitor) {
		for (NestedBean bean : copyValues(nestedBeans)) {
			bean.accept(visitor);
		}
	}
	
	public QualifiedName getName() {
		return new QualifiedName(element);
	}

	public static final <T> Collection<T> copyValues(Map<?, T> map) {
		return new ArrayList<T>(map.values());
	}

}
