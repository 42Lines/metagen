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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import net.ftlines.metagen.annot.Meta;
import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.tree.Property;
import net.ftlines.metagen.processor.util.Logger;

public class TrimmingVisitor extends BeanVisitorAdapter
{
	private final Logger logger = new Logger(getClass());

	private BeanSpace space;
	private Stack<AbstractBean> beans = new Stack<AbstractBean>();

	@Override
	public void enterBeanSpace(BeanSpace space)
	{
		this.space = space;
	}

	@Override
	protected void enterBean(AbstractBean bean)
	{
		beans.push(bean);
	}

	@Override
	protected void exitBean(AbstractBean bean)
	{
		beans.pop();

		// remove setter only properties
		for (String propertyName : new ArrayList<String>(bean.getProperties().keySet()))
		{
			Property property = bean.getProperties().get(propertyName);
			if (property.getSetter() != null && property.getGetter() == null && property.getField() == null)
			{
				bean.remove(property);
			}
		}

		// remove setters for which there is no field type and for which the return type of the getter doesn't match
		// the type of the setter
		for (String propertyName : new ArrayList<String>(bean.getProperties().keySet()))
		{
			Property property = bean.getProperties().get(propertyName);
			if (property.getField() == null && property.getGetter() != null && property.getSetter() != null)
			{
				List<? extends VariableElement> parameters = ((ExecutableElement)property.getSetter()).getParameters();
				if (parameters.size() != 1 || !((ExecutableElement)property.getGetter()).getReturnType().toString().equals(parameters.get(0).asType().toString()))
				{
					property.setSetter(null);
				}
			}
		}



		if (bean.getProperties().isEmpty() == false)
		{
			return;
		}
		if (bean.getNestedBeans().isEmpty() == false)
		{
			return;
		}

		// metadata was specifically requested for this bean
		if (bean.getElement().getAnnotation(Meta.class) != null)
		{
			return;
		}

		// remove this bean because its empty

		if (beans.isEmpty())
		{
			// top level bean
			space.remove(bean.getElement());

			logger.log("Trimmed top level bean: %s", bean.getElement().getQualifiedName());
		}
		else
		{
			// nested bean
			beans.peek().getNestedBeans().remove(bean.getElement());

			logger.log("Trimmed nested bean: %s from: %s", bean.getElement().getQualifiedName(), beans.peek()
				.getNestedBeans());
		}
	}

}
