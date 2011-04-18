package net.ftlines.metagen.processor.tree;

import javax.lang.model.element.TypeElement;

public class TopLevelBean extends AbstractBean {

	public TopLevelBean(TypeElement element) {
		super(element);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.enterTopLevelBean(this);
		visitProperties(visitor);
		visitNestedBeans(visitor);
		visitor.exitTopLevel(this);
	}

}
