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

package net.ftlines.metagen.processor.model;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;


public class ElementExt implements Element
{
	protected final Element e;

	protected ElementExt(Element e)
	{
		this.e = e;
	}

	@Override
	public TypeMirror asType()
	{
		return e.asType();
	}

	@Override
	public ElementKind getKind()
	{
		return e.getKind();
	}

	@Override
	public List<? extends AnnotationMirror> getAnnotationMirrors()
	{
		return e.getAnnotationMirrors();
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType)
	{
		return e.getAnnotation(annotationType);
	}

	@Override
	public Set<Modifier> getModifiers()
	{
		return e.getModifiers();
	}

	@Override
	public Name getSimpleName()
	{
		return e.getSimpleName();
	}

	@Override
	public Element getEnclosingElement()
	{
		return e.getEnclosingElement();
	}

	@Override
	public List<? extends Element> getEnclosedElements()
	{
		return e.getEnclosedElements();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementExt))
		{
			return false;
		}
		ElementExt other = (ElementExt)obj;
		if (e == null)
		{
			if (other.e != null)
				return false;
		}
		else if (!e.equals(other.e))
			return false;
		return true;
	}

	@Override
	public <R, P> R accept(ElementVisitor<R, P> v, P p)
	{
		return e.accept(v, p);
	}

	public boolean isStatic()
	{
		return e.getModifiers().contains(Modifier.STATIC);
	}

	public boolean isField()
	{
		return e.getKind().equals(ElementKind.FIELD);
	}

	public boolean isMethod()
	{
		return e.getKind().equals(ElementKind.METHOD);
	}

	public boolean isGetter()
	{
		if (!isMethod())
		{
			return false;
		}

		ExecutableElement ex = (ExecutableElement)e;

		if (ex.getParameters().size() > 0)
		{
			return false;
		}

		if (ex.getReturnType().getKind().equals(TypeKind.VOID))
		{
			return false;
		}

		String name = getName();
		boolean get = name.startsWith("get") && name.length() > 3 && Character.isUpperCase(name.charAt(3));
		boolean is = name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2));

		if (!get && !is)
		{
			return false;
		}

		return true;
	}

	public boolean isSetter()
	{
		if (!isMethod())
		{
			return false;
		}

		ExecutableElement ex = (ExecutableElement)e;

		if (ex.getParameters().size() != 1)
		{
			return false;
		}

		if (!ex.getReturnType().getKind().equals(TypeKind.VOID))
		{
			return false;
		}

		String name = getName();
		boolean get = name.startsWith("set") && name.length() > 3 && Character.isUpperCase(name.charAt(3));

		if (!get)
		{
			return false;
		}

		return true;
	}

	public boolean isProperty()
	{
		return !isStatic() && (isField() || isGetter());
	}

	public String getPropertyName()
	{
		String name = getName();
		if (isField())
		{
			return name;
		}
		else if (isGetter() || isSetter())
		{
			int cut = name.startsWith("is") ? 2 : 3;
			String prefix = name.substring(cut, cut + 1).toLowerCase();
			String suffix = name.substring(cut + 1);
			return prefix + suffix;
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	public TypeMirror getPropertyType()
	{
		if (isField())
		{
			return e.asType();
		}
		else if (isGetter() || isSetter())
		{
			return ((ExecutableElement)e).getReturnType();
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	public String getName()
	{
		return e.getSimpleName().toString();
	}

	public String resolveType()
	{
		switch (e.getKind())
		{
			case FIELD :
				return e.asType().accept(new TypeResolver(), null);
			case METHOD :
				return ((ExecutableElement)e).getReturnType().accept(new TypeResolver(), null);
			default :
				throw new IllegalArgumentException("Can only resolve types of fields or methods. Unsupported elment: " +
					e);
		}
	}

	public boolean hasAnnotation(String annotationType)
	{
		for (AnnotationMirror mirror : getAnnotationMirrors())
		{
			String typeName = ((TypeElement)mirror.getAnnotationType().asElement()).getQualifiedName().toString();
			if (typeName.equals(annotationType))
			{
				return true;
			}
		}
		return false;
	}

	public Element toElement()
	{
		return e;
	}

	public Visibility getVisibility()
	{
		if (e.getModifiers().contains(Modifier.PUBLIC))
		{
			return Visibility.PUBLIC;
		}
		else if (e.getModifiers().contains(Modifier.PROTECTED))
		{
			return Visibility.PROTECTED;
		}
		else if (e.getModifiers().contains(Modifier.PRIVATE))
		{
			return Visibility.PRIVATE;
		}
		else
		{
			return Visibility.DEFAULT;
		}
	}

}
