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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;


public class TypeResolver extends AbstractTypeVisitor<String, Void>
{
	private Set<DeclaredType> visited = new HashSet<DeclaredType>();

	@Override
	public String visitPrimitive(PrimitiveType t, Void p)
	{
		switch (t.getKind())
		{
			case BOOLEAN :
				return Boolean.class.getName();
			case BYTE :
				return Byte.class.getName();
			case CHAR :
				return Character.class.getName();
			case DOUBLE :
				return Double.class.getName();
			case FLOAT :
				return Float.class.getName();
			case INT :
				return Integer.class.getName();
			case LONG :
				return Long.class.getName();
			case SHORT :
				return Short.class.getName();
			default :
				throw new IllegalStateException();
		}
	}

	@Override
	public String visitArray(ArrayType t, Void p)
	{
		return t.getComponentType().accept(this, p) + "[]";
	}

	@Override
	public String visitExecutable(ExecutableType t, Void p) {
		return t.getReturnType().accept(this, null);
	}
	
	
	@Override
	public String visitDeclared(DeclaredType t, Void p)
	{
		String fqn = ((TypeElement)t.asElement()).getQualifiedName().toString();
		
		if (visited.contains(t)) {
			return fqn;
		}
		
		visited.add(t);
		
		List<? extends TypeMirror> args = t.getTypeArguments();
		if (args.size() > 0)
		{
			fqn = fqn + "<";
			for (int i = 0, l = args.size(); i < l; i++)
			{
				if (i > 0)
				{
					fqn += ",";
				}
				TypeMirror argType = args.get(i);
				String argTypeString = argType.accept(this, null);
				fqn += argTypeString;
			}
			fqn = fqn + ">";
		}
		return fqn;
	}

	public String visitTypeVariable(TypeVariable t, Void p)
	{
		TypeVariable tv = (TypeVariable)t;
		TypeMirror lb = tv.getLowerBound();
		TypeKind lbk = lb.getKind();
		if (TypeKind.NONE.equals(lbk) == false && TypeKind.NULL.equals(lbk) == false)
		{
			return " ? super "+lb.accept(this, null);
		}
		else
		{
			return " ? extends "+tv.getUpperBound().accept(this, null);
		}
	}

	@Override
	public String visitWildcard(WildcardType t, Void p)
	{
		if (t.getSuperBound() != null)
		{
			return " ? super "+t.getSuperBound().accept(this, null);
		}
		else
		{
			return " ? extends "+t.getExtendsBound().accept(this, null);
		}
	}
}
