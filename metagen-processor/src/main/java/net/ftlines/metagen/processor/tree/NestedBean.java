package net.ftlines.metagen.processor.tree;

import javax.lang.model.element.TypeElement;

public class NestedBean extends AbstractBean {

	public NestedBean(TypeElement element) {
		super(element);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.enterNestedBean(this);
		visitProperties(visitor);
		visitNestedBeans(visitor);
		visitor.exitNestedBean(this);
	}

}
