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

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

import net.ftlines.metagen.processor.util.Optional;

public class ElementResolver extends AbstractTypeVisitor<Optional<TypeElement>, Void>
{

	@Override
	public Optional<TypeElement> visitDeclared(DeclaredType t, Void p)
	{
		return Optional.of((TypeElement)t.asElement());
	}

	@Override
	public Optional<TypeElement> visit(TypeMirror t, Void p)
	{
		return Optional.ofNull();
	}

	@Override
	public Optional<TypeElement> visitTypeVariable(TypeVariable t, Void p)
	{
		TypeVariable tv = t;
		TypeMirror lb = tv.getLowerBound();
		TypeKind lbk = lb.getKind();
		if (TypeKind.NONE.equals(lbk) == false && TypeKind.NULL.equals(lbk) == false)
		{
			return lb.accept(this, null);
		}
		else
		{
			return tv.getUpperBound().accept(this, null);
		}

	}

	@Override
	public Optional<TypeElement> visitWildcard(WildcardType t, Void p)
	{
		if (t.getSuperBound() != null)
		{
			return t.getSuperBound().accept(this, null);
		}
		else
		{
			return t.getExtendsBound().accept(this, null);
		}
	}
}
