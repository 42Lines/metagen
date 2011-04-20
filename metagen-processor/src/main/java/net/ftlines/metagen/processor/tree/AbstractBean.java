package net.ftlines.metagen.processor.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.model.QualifiedName;
import net.ftlines.metagen.processor.model.Visibility;
import net.ftlines.metagen.processor.util.Optional;

public abstract class AbstractBean implements Node {
	private final TypeElement element;

	private final Map<TypeElement, NestedBean> nestedBeans = new HashMap<TypeElement, NestedBean>();

	private final Map<String, Property> properties = new HashMap<String, Property>();

	private AbstractBean superclass;

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

	public Visibility getVisibility() {
		return Visibility.of(element);
	}

	public static final <T> Collection<T> copyValues(Map<?, T> map) {
		return new ArrayList<T>(map.values());
	}

	public void remove(Property property) {
		Property removed = properties.remove(property.getName());
		if (removed == null) {
			throw new IllegalStateException(
					"Attempted to remove property that was not part of the bean. Property name: "
							+ property.getName());
		}
	}

	public Optional<AbstractBean> getSuperclass() {
		return Optional.of(superclass);
	}

	public void setSuperclass(AbstractBean superclass) {
		this.superclass = superclass;
	}

}
