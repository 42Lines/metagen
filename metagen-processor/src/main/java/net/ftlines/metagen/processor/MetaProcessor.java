/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.ftlines.metagen.processor;

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

import net.ftlines.metagen.annot.Meta;
import net.ftlines.metagen.processor.resolver.PropertyResolvers;
import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.tree.visitor.CodeGeneratingVisitor;
import net.ftlines.metagen.processor.tree.visitor.PropertyResolvingVisitor;
import net.ftlines.metagen.processor.tree.visitor.SuperclassResolvingVisitor;
import net.ftlines.metagen.processor.tree.visitor.TrimmingVisitor;
import net.ftlines.metagen.processor.tree.visitor.ValidatingVisitor;
import net.ftlines.metagen.processor.util.Logger;

public class MetaProcessor implements Processor
{
	private ProcessingEnvironment environment;
	private PropertyResolvers resolvers;

	private Logger logger = new Logger(getClass());

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round)
	{

		environment.getMessager().printMessage(Kind.NOTE, "MetaGen");

		logger.log("");
		logger.log("STARTED ROUND");
		logger.log("");

		BeanSpace beans = new BeanSpace();
		try
		{
			// TODO this should prob delegate to beanspace.add(element);
			// TODO beanspace should check if element is supported and error
			// otherwise
			for (TypeElement annotation : annotations)
			{
				logger.log("Processing annotation: %s", annotation.getQualifiedName());

				for (Element annotated : round.getElementsAnnotatedWith(annotation))
				{
					TypeElement element = null;
					switch (annotated.getKind())
					{
						case CLASS :
						case ENUM :
							element = (TypeElement)annotated;

							logger.log("Processing annotated class/enum: %s", element.getQualifiedName());

							if (accept(element))
							{
								beans.add(element);
							}
							break;
						case FIELD :
						case METHOD :
							element = (TypeElement)annotated.getEnclosingElement();

							logger.log("Processing class/enum: %s derived from annotated element: %s",
								element.getQualifiedName(), annotated.getSimpleName());

							if (accept(element))
							{
								beans.add(element);
							}
							break;
						default :
							logger.log("Ignored element: %s of kind: %s", annotated.getSimpleName(),
								annotated.getKind());
					}
				}
			}

			beans.accept(new PropertyResolvingVisitor(resolvers));
			beans.accept(new TrimmingVisitor());
			// beans.accept(new PrintVisitor());
			beans.accept(new ValidatingVisitor(environment));
			beans.accept(new SuperclassResolvingVisitor());
			beans.accept(new TrimmingVisitor());
			beans.accept(new CodeGeneratingVisitor(environment));


			logger.log("");
			logger.log("ROUND COMPLETED [V3]");
			logger.log("");

			// return false so we do not claim annotaitons like @Entity
			return false;
		}
		catch (RuntimeException e)
		{
			logger.log("Error: %s", e.getMessage());
			environment.getMessager().printMessage(Kind.ERROR, e.getMessage());
			throw e;
		}
	}

	protected boolean accept(TypeElement element)
	{
		return true;
	}

	@Override
	public Set<String> getSupportedOptions()
	{
		return Collections.emptySet();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes()
	{
		HashSet<String> annotations = new HashSet<String>(resolvers.getSupportedAnnotationTypes());
		annotations.add(Meta.class.getName());
		return annotations;
	}

	@Override
	public SourceVersion getSupportedSourceVersion()
	{
		return SourceVersion.RELEASE_6;
	}

	@Override
	public void init(ProcessingEnvironment processingEnv)
	{
		environment = processingEnv;
		resolvers = new PropertyResolvers();
	}

	@Override
	public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
		ExecutableElement member, String userText)
	{
		return Collections.emptyList();
	}
}
