package net.ftlines.metagen.processor.tree;

public interface Visitor {
	void enterBeanSpace(BeanSpace space);

	void exitBeanSpace(BeanSpace space);

	void enterTopLevelBean(TopLevelBean node);

	void exitTopLevel(TopLevelBean node);

	void enterNestedBean(NestedBean node);

	void exitNestedBean(NestedBean node);

	void enterProperty(Property node);

	void exitProperty(Property node);
}
