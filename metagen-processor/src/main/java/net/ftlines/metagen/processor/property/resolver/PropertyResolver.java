package net.ftlines.metagen.processor.property.resolver;

import java.util.Collection;
import java.util.Set;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.property.Property;


public interface PropertyResolver {
	Set<String> getSupportedAnnotationTypes();
	Collection<Property> findProperties(TypeElement type);
}
