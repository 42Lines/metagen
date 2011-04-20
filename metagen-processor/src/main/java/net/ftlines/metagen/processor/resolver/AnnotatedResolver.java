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

package net.ftlines.metagen.processor.resolver;

import java.util.Collections;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.model.ElementExt;
import net.ftlines.metagen.processor.model.ModelExt;
import net.ftlines.metagen.processor.model.Visibility;
import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.Property;

public class AnnotatedResolver implements PropertyResolver {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton(Constants.PROPERTY);
	}

	@Override
	public void resolveProperties(AbstractBean bean) {
		TypeElement type = bean.getElement();
		for (Element enclosed : type.getEnclosedElements()) {
			ElementExt ext = ModelExt.of(enclosed);
			// TODO error if annotated and is not a property
			if (ext.isProperty() && ext.hasAnnotation(Constants.PROPERTY)) {
				String name = ext.getPropertyName();
				Property property = bean.getProperties().get(name);
				if (property == null) {
					property = new Property(name);
					bean.getProperties().put(name, property);
				}
				if (ext.isGetter()) {
					property.setGetter(enclosed);
				} else if (ext.isSetter()) {
					property.setSetter(enclosed);
				} else {
					property.setField(enclosed);
				}

				// force visibility
				property.setVisibility(Visibility.of(enclosed));
			}
		}
	}
}
