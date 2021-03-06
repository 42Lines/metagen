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

package net.ftlines.metagen.processor.tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.model.ElementResolver;
import net.ftlines.metagen.processor.util.Logger;
import net.ftlines.metagen.processor.util.Optional;

public class BeanSpace implements Node
{
	private final Map<Name, TopLevelBean> beans = new HashMap<Name, TopLevelBean>();

	private final Set<String> ignoredPackages = new HashSet<String>();

	private Logger logger = new Logger(getClass());

	public BeanSpace()
	{
		ignoredPackages.add("java.");
		ignoredPackages.add("javax.");
	}

	public void add(TypeElement element)
	{
		recursiveGetOrAdd(element, true);
	}

	public Optional<AbstractBean> get(TypeElement element)
	{
		return Optional.of(recursiveGetOrAdd(element, false));
	}

	private AbstractBean recursiveGetOrAdd(TypeElement element, boolean add)
	{
		Name name = element.getQualifiedName();
		switch (element.getNestingKind())
		{
			case TOP_LEVEL :
				TopLevelBean node = beans.get(name);
				if (node == null && add && valid(element))
				{
					node = new TopLevelBean(element);
					beans.put(name, node);
					logger.log("Added top level bean: %s", name);
					addSuperClass(element);
				}
				return node;
			case MEMBER :
				AbstractBean parent = recursiveGetOrAdd((TypeElement)element.getEnclosingElement(), add);
				if (parent == null)
				{
					logger.log("Can't recursiveGetOrAdd of %s in %s with add="+add,
						 ((TypeElement)element.getEnclosingElement()).getQualifiedName(),name);
					return null;
 				}
				NestedBean nested = parent.getNestedBeans().get(element);
				if (nested == null && add && valid(element))
				{
					nested = new NestedBean(element);
					parent.getNestedBeans().put(element, nested);

					logger.log("Added: %s to: %s", name, parent.getElement().getQualifiedName());

					addSuperClass(element);
				}
				return nested;
			default :
				throw new IllegalStateException("Tried to create bean node for element: " + element +
					" with unsupported nesting kind: " + element.getNestingKind());
		}
	}

	private void addSuperClass(TypeElement element)
	{
		if (element.getSuperclass() != null)
		{
			Optional<TypeElement> superclass = element.getSuperclass().accept(new ElementResolver(), null);
			if (superclass.isSet())
			{
				recursiveGetOrAdd(superclass.get(), true);
			}
		}
	}

	@Override
	public void accept(Visitor visitor)
	{
		visitor.enterBeanSpace(this);
		for (TopLevelBean node : AbstractBean.copyValues(beans))
		{
			node.accept(visitor);
		}
		visitor.exitBeanSpace(this);
	}

	public void remove(TypeElement element)
	{
		beans.remove(element.getQualifiedName());
	}

	private boolean valid(TypeElement element)
	{
		final String fqn = element.getQualifiedName().toString();
		for (String ignoredPackage : ignoredPackages)
		{
			if (fqn.startsWith(ignoredPackage))
			{
				return false;
			}
		}
		return true;
	}

}
