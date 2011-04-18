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

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.model.ElementExt;
import net.ftlines.metagen.processor.model.ModelExt;
import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.PropertyNode;

public class JpaEntityResolver implements PropertyResolver {
	private static final Set<String> annots = new HashSet<String>();
	{
		annots.add(Constants.ENTITY);
		annots.add(Constants.MAPPED_SUPERCLASS);
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return annots;
	}

	@Override
	public void resolveProperties(AbstractBean bean) {
		TypeElement type = bean.getElement();
		if (!ModelExt.hasAnyAnnotation(type, annots)) {
			return;
		}

		for (Element enclosed : type.getEnclosedElements()) {
			ElementExt ext = ModelExt.of(enclosed);
			if (ext.isProperty()) {
				String name = ext.getPropertyName();
				PropertyNode property = bean.getProperties().get(name);
				if (property == null) {
					property = new PropertyNode(name);
					bean.getProperties().put(name, property);
				}
				if (ext.isGetter()) {
					property.setGetter(enclosed);
				} else if (ext.isSetter()) {
					property.setSetter(enclosed);
				} else {
					property.setField(enclosed);
				}
			}
		}
	}

}
