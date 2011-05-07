/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.ftlines.metagen.processor.tree.visitor;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.model.ElementResolver;
import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.util.Optional;

public class SuperclassResolvingVisitor extends BeanVisitorAdapter
{

	private BeanSpace space;

	@Override
	public void enterBeanSpace(BeanSpace space)
	{
		this.space = space;
	}

	@Override
	protected void enterBean(final AbstractBean bean)
	{
		Optional<TypeElement> element = Optional.of(bean.getElement());

		do
		{
			element = element.get().getSuperclass().accept(new ElementResolver(), null);
			if (element.isSet())
			{
				Optional<AbstractBean> superclass = space.get(element.get());
				if (superclass.isSet())
				{
					bean.setSuperclass(superclass.get());
					return;
				}
			}
		}
		while (element.isSet());
	}

	@Override
	protected void exitBean(AbstractBean bean)
	{
	}

}
