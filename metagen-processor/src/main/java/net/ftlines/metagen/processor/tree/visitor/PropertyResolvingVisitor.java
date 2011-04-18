package net.ftlines.metagen.processor.tree.visitor;

import net.ftlines.metagen.processor.resolver.PropertyResolvers;
import net.ftlines.metagen.processor.tree.AbstractBean;

public class PropertyResolvingVisitor extends BeanVisitorAdapter {
	private final PropertyResolvers resolvers;

	public PropertyResolvingVisitor(PropertyResolvers resolvers) {
		this.resolvers = resolvers;
	}

	@Override
	protected void enterBean(AbstractBean bean) {
		resolvers.resolveProperties(bean);
	}

	@Override
	protected void exitBean(AbstractBean bean) {
	}

}
