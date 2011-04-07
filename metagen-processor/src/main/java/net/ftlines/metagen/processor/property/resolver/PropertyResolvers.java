package net.ftlines.metagen.processor.property.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.property.Property;


public class PropertyResolvers implements Iterable<PropertyResolver>,
		PropertyResolver {

	private final List<PropertyResolver> resolvers;

	public PropertyResolvers() {
		resolvers = new ArrayList<PropertyResolver>();
		resolvers.add(new EntityProcessor());
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
