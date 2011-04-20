package net.ftlines.metagen.processor.tree.visitor;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.model.ElementResolver;
import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.util.Optional;

public class SuperclassResolvingVisitor extends BeanVisitorAdapter {

	private BeanSpace space;

	@Override
	public void enterBeanSpace(BeanSpace space) {
		this.space = space;
	}

	@Override
	protected void enterBean(final AbstractBean bean) {
		Optional<TypeElement> element = Optional.of(bean.getElement());

		do {
			element = element.get().getSuperclass()
					.accept(new ElementResolver(), null);
			if (element.isSet()) {
				Optional<AbstractBean> superclass = space.get(element.get());
				if (superclass.isSet()) {
					bean.setSuperclass(superclass.get());
					return;
				}
			}
		} while (element.isSet());
	}

	@Override
	protected void exitBean(AbstractBean bean) {
	}

}
