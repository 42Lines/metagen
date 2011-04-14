/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ftlines.metagen.processor.property.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.model.ModelExt;
import net.ftlines.metagen.processor.property.Property;

public class PropertyResolvers implements Iterable<PropertyResolver>,
		PropertyResolver {

	private final List<PropertyResolver> resolvers;

	public PropertyResolvers() {
		resolvers = new ArrayList<PropertyResolver>();
		resolvers.add(new BeanResolver());
		resolvers.add(new JpaEntityResolver());
		resolvers.add(new AnnotatedResolver());
	}

	@Override
	public Iterator<PropertyResolver> iterator() {
		return resolvers.iterator();
	}

	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new HashSet<String>();
		for (PropertyResolver processor : new PropertyResolvers()) {
			types.addAll(processor.getSupportedAnnotationTypes());
		}
		return types;
	}

	@Override
	public Collection<Property> findProperties(TypeElement type) {

		if (ModelExt.hasAnnotation(type, Constants.IGNORE)) {
			return Collections.emptySet();
		}

		Map<String, Property> properties = new HashMap<String, Property>();
		for (PropertyResolver resolver : resolvers) {
			Collection<Property> resolved = resolver.findProperties(type);
			for (Property property : resolved) {
				properties.put(property.getName(), property);
			}
		}
		return new HashSet<Property>(properties.values());
	}
}
