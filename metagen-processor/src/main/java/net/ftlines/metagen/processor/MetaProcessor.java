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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import net.ftlines.metagen.processor.model.ModelExt;
import net.ftlines.metagen.processor.model.QualifiedName;
import net.ftlines.metagen.processor.property.Property;
import net.ftlines.metagen.processor.property.resolver.PropertyResolvers;
import net.ftlines.metagen.processor.util.Optional;
import net.ftlines.metagen.processor.util.SourceWriter;


@SupportedAnnotationTypes({ Constants.PROPERTY, Constants.ENTITY,
		Constants.MAPPED_SUPERCLASS })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MetaProcessor implements Processor {
	private ProcessingEnvironment environment;
	private PropertyResolvers resolvers;

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment round) {

		Set<TypeElement> types = findTypes(annotations, round);
		for (TypeElement type : types) {
			processType(type);
		}
		return true;
	}

	private void processType(TypeElement type) {
		QualifiedName qn = new QualifiedName(type.getQualifiedName().toString()+Constants.MARKER);
		try {

			JavaFileObject file = environment.getFiler().createSourceFile(
					qn.getQualified(), type);
			SourceWriter writer = new SourceWriter(file.openOutputStream());

			writer.line("package %s;", qn.getNamespace());
			writer.line("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })");
			writer.line("public class %s", qn.getLocal());
			writer.startBlock();

			for (Property property : resolvers.findProperties(type)) {
				property.generateSource(writer);
			}

			writer.endBlock().close();
		} catch (IOException e) {
			environment.getMessager().printMessage(
					Kind.ERROR,
					"Could not write source for: " + qn.getQualified() + ": "
							+ e.getMessage());
		}

	}

	private Set<TypeElement> findTypes(Set<? extends TypeElement> annotations,
			RoundEnvironment round) {

		Set<TypeElement> types = new HashSet<TypeElement>();

		for (TypeElement annotation : annotations) {
			Set<? extends Element> annotated = round
					.getElementsAnnotatedWith(annotation);

			for (Element element : annotated) {

				Optional<TypeElement> top = ModelExt.findTopLevelType(element);
				if (top.isSet())
					types.add(top.get());
			}
		}

		return types;
	}

	@Override
	public Set<String> getSupportedOptions() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return resolvers.getSupportedAnnotationTypes();
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_6;
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		environment = processingEnv;
		resolvers = new PropertyResolvers();
	}

	@Override
	public Iterable<? extends Completion> getCompletions(Element element,
			AnnotationMirror annotation, ExecutableElement member,
			String userText) {
		return Collections.emptyList();
	}
}
