package net.ftlines.metagen.processor.tree;

import javax.lang.model.element.Element;

public class Property implements Node {
	private final String name;
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

	@Override
	public void accept(Visitor visitor) {
		visitor.enterProperty(this);
		visitor.exitProperty(this);
	}
}
