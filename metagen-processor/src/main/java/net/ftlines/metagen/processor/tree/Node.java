package net.ftlines.metagen.processor.tree;

public interface Node {
	void accept(Visitor visitor);
}
