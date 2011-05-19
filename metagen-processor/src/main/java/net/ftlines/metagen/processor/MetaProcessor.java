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

import net.ftlines.metagen.annot.Meta;
import net.ftlines.metagen.processor.resolver.PropertyResolvers;
import net.ftlines.metagen.processor.tree.BeanSpace;
import net.ftlines.metagen.processor.tree.visitor.CodeGeneratingVisitor;
import net.ftlines.metagen.processor.tree.visitor.PropertyResolvingVisitor;
import net.ftlines.metagen.processor.tree.visitor.SuperclassResolvingVisitor;
import net.ftlines.metagen.processor.tree.visitor.TrimmingVisitor;
import net.ftlines.metagen.processor.tree.visitor.ValidatingVisitor;

public class MetaProcessor implements Processor
{
	private ProcessingEnvironment environment;
	private PropertyResolvers resolvers;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round)
	{

		BeanSpace beans = new BeanSpace();

		// TODO this should prob delegate to beanspace.add(element);
		// TODO beanspace should check if element is supported and error
		// otherwise
		for (TypeElement annotation : annotations)
		{
			for (Element annotated : round.getElementsAnnotatedWith(annotation))
			{
				TypeElement element = null;
				switch (annotated.getKind())
				{
					case CLASS :
					case ENUM :
						element = (TypeElement)annotated;
						if (accept(element))
						{
							beans.add(element);
						}
						break;
					case FIELD :
					case METHOD :
						element = (TypeElement)annotated.getEnclosingElement();
						if (accept(element))
						{
							beans.add(element);
						}
						break;
				}
			}
		}

		beans.accept(new PropertyResolvingVisitor(resolvers));
		beans.accept(new TrimmingVisitor());
		// beans.accept(new PrintVisitor());
		beans.accept(new ValidatingVisitor(environment));
		beans.accept(new TrimmingVisitor());
		beans.accept(new SuperclassResolvingVisitor());
		beans.accept(new CodeGeneratingVisitor(environment));

		// return false so we do not claim annotaitons like @Entity and
		// @MappedSuperClass
		return false;
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
