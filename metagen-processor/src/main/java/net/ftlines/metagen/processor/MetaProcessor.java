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
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import net.ftlines.metagen.processor.model.ModelExt;
import net.ftlines.metagen.processor.property.resolver.PropertyResolvers;
import net.ftlines.metagen.processor.util.Optional;

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
		try {

			new ClassWriter(type, resolvers, environment).write();
		} catch (IOException e) {
			environment.getMessager().printMessage(
					Kind.ERROR,
					"Could not write source for: " + type.getQualifiedName()
							+ ": " + e.getMessage());
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
