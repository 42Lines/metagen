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

package net.ftlines.metagen.processor;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.Stack;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import net.ftlines.metagen.processor.model.QualifiedName;
import net.ftlines.metagen.processor.model.Visibility;
import net.ftlines.metagen.processor.property.Property;
import net.ftlines.metagen.processor.property.resolver.PropertyResolvers;
import net.ftlines.metagen.processor.util.SourceWriter;

/**
 * naive and crude way to write out meta classes before i have the time to
 * implement an ast for beans
 */
class ClassWriter {
	private final TypeElement root;
	private final ProcessingEnvironment env;
	private final PropertyResolvers resolvers;

	private Stack<TypeElement> types;
	private int written;
	private int processing;
	JavaFileObject file;
	SourceWriter writer;

	public ClassWriter(TypeElement root, PropertyResolvers resolvers,
			ProcessingEnvironment env) {
		this.root = root;
		this.resolvers = resolvers;
		this.env = env;
	}

	public void write() throws IOException {
		types = new Stack<TypeElement>();
		written = 0;
		processing = 0;
		file = null;
		writer = null;

		types.push(root);

		processType(root);

		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}

	private void processType(TypeElement type) throws IOException {

		int originalWritten = written;
		boolean wrote = false;

		Collection<Property> properties = resolvers.findProperties(type);

		if (!properties.isEmpty()) {
			originalWritten = written;
			wrote = true;
			writeClassLeadins();
		}

		for (Property property : properties) {
			property.generateSource(writer);
		}

		for (Element enclosed : type.getEnclosedElements()) {
			if (!ElementKind.CLASS.equals(enclosed.getKind())) {
				continue;
			}

			TypeElement nested = (TypeElement) enclosed;

			if (!NestingKind.MEMBER.equals(nested.getNestingKind())) {
				continue;
			}

			types.push(nested);
			processing++;
			processType(nested);
			types.pop();
			processing--;
			
		}

		if (wrote) {
			for (int i = 0; i < (written - originalWritten); i++) {
				writer.endClass();
			}
			written = originalWritten;
		}

		
	}

	private void writeClassLeadins() throws IOException {
		for (int i = written; i <= processing; i++) {
			TypeElement type = types.get(i);
			if (i == 0) {
				QualifiedName qn = new QualifiedName(type.getQualifiedName()
						.toString() + Constants.MARKER);

				file = env.getFiler().createSourceFile(qn.getQualified(), type);
				writer = new SourceWriter(file.openOutputStream());

				writer.header(qn.getNamespace());
				writer.startClass(Visibility.PUBLIC, qn.getLocal());
				written++;
			} else {
				writer.startNestedClass(Visibility.PUBLIC, type.getSimpleName()
						.toString()+Constants.MARKER);
				written++;
			}
		}
	}
}
