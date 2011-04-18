package net.ftlines.metagen.processor.tree.visitor;

import java.util.Stack;

import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.BeanSpace;

public class TrimmingVisitor extends BeanVisitorAdapter {

	private BeanSpace space;
	private Stack<AbstractBean> beans = new Stack<AbstractBean>();

	@Override
	public void enterBeanSpace(BeanSpace space) {
		this.space = space;
	}

	@Override
	protected void enterBean(AbstractBean bean) {
		beans.push(bean);
	}

	@Override
	protected void exitBean(AbstractBean bean) {
		beans.pop();

		if (bean.getProperties().isEmpty() == false) {
			return;
		}
		if (bean.getNestedBeans().isEmpty() == false) {
			return;
		}

		// remove this bean because its empty

		if (beans.isEmpty()) {
			// top level bean
			space.remove(bean.getElement());
		} else {
			// nested bean
			beans.peek().getNestedBeans().remove(bean.getElement());
		}
	}

}
