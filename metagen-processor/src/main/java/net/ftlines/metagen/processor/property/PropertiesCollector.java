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

package net.ftlines.metagen.processor.property;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.ftlines.metagen.processor.model.Visibility;


public class PropertiesCollector {
	private static final Set<String> RESERVED = new HashSet<String>(
			Arrays.asList("abstract", "assert", "boolean", "break", "byte",
					"case", "catch", "char", "class", "const", "continue",
					"default", "do", "double", "else", "enum", "extends",
					"final", "finally", "float", "for", "goto", "if",
					"implements", "import", "instanceof", "int", "interface",
					"long", "native", "new", "package", "private", "protected",
					"public", "return", "short", "static", "strictfp", "super",
					"switch", "synchronized", "this", "throw", "throws",
					"transient", "try", "void", "volatile", "while", "false",
					"null", "true"));

	private final Map<String, Property> bindings = new HashMap<String, Property>();

	public void add(Property binding) {
		
		if (RESERVED.contains(binding.getName())) {
			// TODO log warning
			return;
		}
		
		Property current = bindings.get(binding.getName());
		if (current != null) {
			// if (!current.getType().equals(binding.getType())) {
			// throw new RuntimeException(
			// "Found two bindings with incompatible types. "
			// + toString(current) + ". " + toString(binding)
			// + ".");
			// }

			current.setVisibility(Visibility.max(current.getVisibility(),
					binding.getVisibility()));
		} else {
			bindings.put(binding.getName(), binding);
		}
	}

	public Collection<Property> getProperties() {
		return bindings.values();
	}

	private String toString(Property property) {
		return property.getContainer().getQualifiedName() + "#"
				+ property.getName() + "(" + property.getType() + ")";
	}
}
