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

package net.ftlines.metagen.processor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;

public class TypeElementExt extends ElementExt implements TypeElement
{
	protected TypeElementExt(TypeElement e)
	{
		super(e);
	}

	public NestingKind getNestingKind()
	{
		return ((TypeElement)e).getNestingKind();
	}

	public Name getQualifiedName()
	{
		return ((TypeElement)e).getQualifiedName();
	}

	public TypeMirror getSuperclass()
	{
		return ((TypeElement)e).getSuperclass();
	}

	public List<? extends TypeMirror> getInterfaces()
	{
		return ((TypeElement)e).getInterfaces();
	}

	public List<? extends TypeParameterElement> getTypeParameters()
	{
		return ((TypeElement)e).getTypeParameters();
	}

	public List<? extends ElementExt> getEnclosedElementsEx()
	{
		List<ElementExt> elements = new ArrayList<ElementExt>();
		for (Element enclosed : getEnclosedElements())
		{
			elements.add(new ElementExt(enclosed));
		}
		return Collections.unmodifiableList(elements);
	}

	public boolean isTopLevel()
	{
		return e.getEnclosingElement().getKind().equals(ElementKind.PACKAGE);
	}
	
	
}
