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

package net.ftlines.metagen.processor.resolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.model.ModelExt;
import net.ftlines.metagen.processor.tree.AbstractBean;

public class PropertyResolvers implements Iterable<PropertyResolver>, PropertyResolver
{

	private final List<PropertyResolver> resolvers;

	public PropertyResolvers()
	{
		resolvers = new ArrayList<PropertyResolver>();
		resolvers.add(new BeanResolver());
		resolvers.add(new JpaEntityResolver());
		resolvers.add(new AnnotatedResolver());
	}

	@Override
	public Iterator<PropertyResolver> iterator()
	{
		return resolvers.iterator();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes()
	{
		Set<String> types = new HashSet<String>();
		for (PropertyResolver processor : new PropertyResolvers())
		{
			types.addAll(processor.getSupportedAnnotationTypes());
		}
		return types;
	}

	@Override
	public void resolveProperties(AbstractBean bean)
	{
		if (ModelExt.hasAnnotation(bean.getElement(), Constants.IGNORE))
		{
			return;
		}

		for (PropertyResolver resolver : resolvers)
		{
			resolver.resolveProperties(bean);
		}
	}
}
