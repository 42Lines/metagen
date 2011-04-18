package net.ftlines.metagen.processor.tree.visitor;

import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.tree.NestedBean;
import net.ftlines.metagen.processor.tree.Property;
import net.ftlines.metagen.processor.tree.TopLevelBean;
import net.ftlines.metagen.processor.tree.Visitor;

public class PrintVisitor implements Visitor {

	private int indent = 0;

	private final void line(String s, Object... p) {
		for (int i = 0; i < indent; i++) {
			System.out.print(" ");
		}
		System.out.println(String.format(s, p));
	}

	@Override
	public void enterBeanSpace(BeanSpace space) {
		line("Entering bean space");
		line("-------------------");
		indent++;
	}

	@Override
	public void exitBeanSpace(BeanSpace space) {
		indent--;
		line("-------------------");
		line("Done");
	}

	@Override
	public void enterTopLevelBean(TopLevelBean node) {
		line("Top level node: %s", node.getElement().getQualifiedName());
		indent++;
	}

	@Override
	public void exitTopLevel(TopLevelBean node) {
		indent--;
	}

	@Override
	public void enterNestedBean(NestedBean node) {
		line("Nested node: %s", node.getElement().getSimpleName());
		indent++;
	}

	@Override
	public void exitNestedBean(NestedBean node) {
		indent--;
	}

	@Override
	public void enterProperty(Property node) {
		line("Property: %s", node.getName());
	}

	@Override
	public void exitProperty(Property node) {
	}

}
