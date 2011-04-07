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
