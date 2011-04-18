package net.ftlines.metagen.processor.tree.visitor;

import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.tree.NestedBean;
import net.ftlines.metagen.processor.tree.PropertyNode;
import net.ftlines.metagen.processor.tree.TopLevelBean;
import net.ftlines.metagen.processor.tree.Visitor;

public abstract class VisitorAdapter implements Visitor{

	@Override
	public void enterBeanSpace(BeanSpace space) {
	}

	@Override
	public void exitBeanSpace(BeanSpace space) {
	}

	@Override
	public void enterTopLevelBean(TopLevelBean node) {
	}

	@Override
	public void exitTopLevel(TopLevelBean node) {
	}

	@Override
	public void enterNestedBean(NestedBean node) {
	}

	@Override
	public void exitNestedBean(NestedBean node) {
	}

	@Override
	public void enterProperty(PropertyNode node) {
	}

	@Override
	public void exitProperty(PropertyNode node) {
	}

}
