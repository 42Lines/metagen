package net.ftlines.metagen.processor.tree;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;

public class BeanSpace implements Node {
	private final Map<TypeElement, TopLevelBean> beans = new HashMap<TypeElement, TopLevelBean>();

	public void add(TypeElement element) {
		recursiveAdd(element);
	}

	private AbstractBean recursiveAdd(TypeElement element) {
		switch (element.getNestingKind()) {
		case TOP_LEVEL:
			TopLevelBean node = beans.get(element);
			if (node == null) {
				node = new TopLevelBean(element);
				beans.put(element, node);
			}
			return node;
		case MEMBER:
			AbstractBean parent = recursiveAdd((TypeElement) element
					.getEnclosingElement());
			NestedBean nested = parent.getNestedBeans().get(element);
			if (nested == null) {
				nested = new NestedBean(element);
				parent.getNestedBeans().put(element, nested);
			}
			return nested;
		default:
			throw new IllegalStateException(
					"Tried to create bean node for element: " + element
							+ " with unsupported nesting kind: "
							+ element.getNestingKind());
		}
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.enterBeanSpace(this);
		for (TopLevelBean node : AbstractBean.copyValues(beans)) {
			node.accept(visitor);
		}
		visitor.exitBeanSpace(this);
	}

	public void remove(TypeElement element) {
		beans.remove(element);
	}

}
