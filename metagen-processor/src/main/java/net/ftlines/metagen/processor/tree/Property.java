package net.ftlines.metagen.processor.tree;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import net.ftlines.metagen.processor.model.Visibility;

public class Property implements Node {
	private final String name;
	private Visibility visibility;
	private Element field;
	private Element getter;
	private Element setter;

	public Property(String name) {
		this.name = name;
	}

	public Element getField() {
		return field;
	}

	public void setField(Element field) {
		this.field = field;
	}

	public Element getGetter() {
		return getter;
	}

	public void setGetter(Element getter) {
		this.getter = getter;
	}

	public Element getSetter() {
		return setter;
	}

	public void setSetter(Element setter) {
		this.setter = setter;
	}

	public String getName() {
		return name;
	}

	public TypeElement getContainer() {
		return (TypeElement) getAccessor().getEnclosingElement();
	}

	public TypeMirror getType() {
		return getAccessor().asType();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.enterProperty(this);
		visitor.exitProperty(this);
	}

	public Element getAccessor() {
		return (getter != null) ? getter : field;
	}

	public Visibility getVisibility() {
		if (visibility != null) {
			return visibility;
		}
		return Visibility.of(getAccessor());
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	
	
}
