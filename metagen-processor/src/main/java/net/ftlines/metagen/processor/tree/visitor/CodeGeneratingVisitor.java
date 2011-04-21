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

package net.ftlines.metagen.processor.tree.visitor;

import java.io.IOException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.model.QualifiedName;
import net.ftlines.metagen.processor.model.TypeResolver;
import net.ftlines.metagen.processor.model.Visibility;
import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.tree.NestedBean;
import net.ftlines.metagen.processor.tree.Property;
import net.ftlines.metagen.processor.tree.TopLevelBean;
import net.ftlines.metagen.processor.tree.Visitor;
import net.ftlines.metagen.processor.util.Optional;
import net.ftlines.metagen.processor.util.SourceWriter;

public class CodeGeneratingVisitor implements Visitor {

	private final ProcessingEnvironment env;

	private SourceWriter writer;

	public CodeGeneratingVisitor(ProcessingEnvironment env) {
		this.env = env;
	}

	@Override
	public void enterBeanSpace(BeanSpace space) {
	}

	@Override
	public void exitBeanSpace(BeanSpace space) {
	}

	@Override
	public void enterTopLevelBean(TopLevelBean node) {
		TypeElement element = node.getElement();
		try {
			QualifiedName name = Constants.getMetaClassName(node.getElement());

			JavaFileObject source = env.getFiler().createSourceFile(
					name.getQualified(), node.getElement());

			writer = new SourceWriter(source.openOutputStream());

			writer.header(node.getName().getNamespace());
			writer.line();

			Optional<QualifiedName> scn = Optional.of(node.getSuperclass()
					.isSet() ? Constants.getMetaClassName(node.getSuperclass()
					.get().getElement()) : null);

			writer.startClass(Visibility.PUBLIC, name.getLocal(), scn);

		} catch (IOException e) {
			// TODO handle this
			throw new RuntimeException(e);
		}
	}

	@Override
	public void exitTopLevel(TopLevelBean node) {
		try {

			writer.endClass();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO handle this
			throw new RuntimeException(e);
		}

	}

	@Override
	public void enterNestedBean(NestedBean node) {
		try {
			QualifiedName name = Constants.getMetaClassName(node.getElement());
			Optional<QualifiedName> scn = Optional.of(node.getSuperclass()
					.isSet() ? Constants.getMetaClassName(node.getSuperclass()
					.get().getElement()) : null);

			writer.startNestedClass(Visibility.PUBLIC, name.getLocal(), scn);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void exitNestedBean(NestedBean node) {
		try {
			writer.endClass();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void enterProperty(Property node) {
		Element element = node.getAccessor();

		String type = node.getType().accept(new TypeResolver(), null);
		Visibility visibility = node.getVisibility();
		QualifiedName containerName = new QualifiedName(node.getContainer());
		try {
			writer.line("%s static final %s<%s,%s> %s = new %s(\"%s\");",
					visibility.getKeyword(), Constants.SINGULAR,
					containerName.getQualified(), type, node.getName(),
					Constants.SINGULAR, node.getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void exitProperty(Property node) {

	}

}
