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

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

public class AbstractElementVisitor<R, P> implements ElementVisitor<R, P>
{

	@Override
	public R visit(Element e, P p)
	{
		throw new UnsupportedOperationException(e.toString());
	}

	@Override
	public R visit(Element e)
	{
		return visit(e, null);
	}

	@Override
	public R visitPackage(PackageElement e, P p)
	{
		return visit(e, p);
	}

	@Override
	public R visitType(TypeElement e, P p)
	{
		return visit(e, p);
	}

	@Override
	public R visitVariable(VariableElement e, P p)
	{
		return visit(e, p);
	}

	@Override
	public R visitExecutable(ExecutableElement e, P p)
	{
		return visit(e, p);
	}

	@Override
	public R visitTypeParameter(TypeParameterElement e, P p)
	{
		return visit(e, p);
	}

	@Override
	public R visitUnknown(Element e, P p)
	{
		return visit(e, p);
	}
}
