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

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

public class TracingElementVisitor<R, P> implements ElementVisitor<R, P>
{
	private final ElementVisitor<R, P> next;

	public TracingElementVisitor()
	{
		this(null);
	}

	public TracingElementVisitor(ElementVisitor<R, P> next)
	{
		this.next = next;
	}

	@Override
	public R visit(Element e, P p)
	{
		log("visit(Element e=[%s], P p=[%s])", e.getSimpleName(), p);
		if (next != null)
			return next.visit(e, p);
		return null;
	}

	private void log(String string, Object... params)
	{
		System.out.println(String.format(string, params));

	}

	@Override
	public R visit(Element e)
	{
		log("visit(Element e=[%s])", e.getSimpleName());
		if (next != null)
			return next.visit(e);
		return null;
	}

	@Override
	public R visitPackage(PackageElement e, P p)
	{
		log("visitPackage(PackageElement e=[%s], P p=[%s])", e.getSimpleName(), p);
		if (next != null)
			return next.visitPackage(e, p);
		return null;
	}

	@Override
	public R visitType(TypeElement e, P p)
	{
		log("visitType(TypeElement e=[%s], P p=[%s])", e.getSimpleName(), p);
		if (next != null)
			return next.visitType(e, p);
		return null;
	}

	@Override
	public R visitVariable(VariableElement e, P p)
	{
		log("visitVariable(VariableElement e=[%s], P p=[%s])", e.getSimpleName(), p);
		if (next != null)
			return next.visitVariable(e, p);
		return null;
	}

	@Override
	public R visitExecutable(ExecutableElement e, P p)
	{
		log("visitExecutable(ExecutableElement e=[%s], P p=[%s])", e.getSimpleName(), p);
		if (next != null)
			return next.visitExecutable(e, p);
		return null;
	}

	@Override
	public R visitTypeParameter(TypeParameterElement e, P p)
	{
		log("visitTypeParameter(TypeParameterElement e=[%s], P p=[%s])", e.getSimpleName(), p);
		if (next != null)
			return next.visitTypeParameter(e, p);
		return null;
	}

	@Override
	public R visitUnknown(Element e, P p)
	{
		log("visitUnknown(Element e=[%s], P p=[%s])", e.getSimpleName(), p);
		if (next != null)
			return next.visitUnknown(e, p);
		return null;
	}


}
