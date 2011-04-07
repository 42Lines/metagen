package net.ftlines.metagen.processor.property.resolver;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.model.ElementExt;
import net.ftlines.metagen.processor.model.ModelExt;
import net.ftlines.metagen.processor.property.PropertiesCollector;
import net.ftlines.metagen.processor.property.Property;



public class AnnotatedResolver implements PropertyResolver {
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton(Constants.PROPERTY);
	}

	@Override
	public Collection<Property> findProperties(TypeElement type) {
		PropertiesCollector collector=new PropertiesCollector();
		for (Element enclosed:type.getEnclosedElements()) {
			ElementExt ext=ModelExt.of(enclosed);
			if (ext.isProperty()&&ext.hasAnnotation(Constants.PROPERTY)) {
				collector.add(new Property(type, ext.getVisibility(), ext
						.getPropertyType(), ext.getPropertyName()));
			}
		}
		return collector.getProperties();
	}

}
