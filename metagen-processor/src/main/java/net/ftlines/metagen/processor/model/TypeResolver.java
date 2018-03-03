/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.ftlines.metagen.processor.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

public class TypeResolver extends AbstractTypeVisitor<TypeResolver.ResolvedType, Void>
{
	public static class ResolvedType
	{
		private boolean optional;
		private String type;

		private ResolvedType(String type)
		{
			this.type = type;
		}

		private ResolvedType(boolean optional, String type)
		{
			this.optional = optional;
			this.type = type;
		}


		public boolean isOptional()
		{
			return optional;
		}

		public String getType()
		{
			return type;
		}
	}

	private final Set<String> visited;

	private final boolean unwrapOptional;

	public TypeResolver()
	{
		this(true, null);
	}

	public TypeResolver(boolean unwrapOptional, TypeResolver parent)
	{
		this.unwrapOptional = unwrapOptional;
		this.visited = parent == null ? new HashSet<>() : parent.visited;
	}

	@Override
	public ResolvedType visitPrimitive(PrimitiveType t, Void p)
	{
		switch (t.getKind())
		{
			case BOOLEAN :
				return new ResolvedType(Boolean.class.getName());
			case BYTE :
				return new ResolvedType(Byte.class.getName());
			case CHAR :
				return new ResolvedType(Character.class.getName());
			case DOUBLE :
				return new ResolvedType(Double.class.getName());
			case FLOAT :
				return new ResolvedType(Float.class.getName());
			case INT :
				return new ResolvedType(Integer.class.getName());
			case LONG :
				return new ResolvedType(Long.class.getName());
			case SHORT :
				return new ResolvedType(Short.class.getName());
			default :
				throw new IllegalStateException();
		}
	}

	@Override
	public ResolvedType visitArray(ArrayType t, Void p)
	{
		return new ResolvedType(t.getComponentType().accept(this, p).getType() + "[]");
	}

	@Override
	public ResolvedType visitExecutable(ExecutableType t, Void p)
	{
		return t.getReturnType().accept(this, null);
	}

	@Override
	public ResolvedType visitDeclared(DeclaredType t, Void p)
	{
		String fqn = ((TypeElement)t.asElement()).getQualifiedName().toString();
		System.out.println(fqn);

		if (visited.contains(fqn))
		{
			System.out.println("short-circuiting");
			return new ResolvedType(fqn);
		}

		visited.add(fqn);

		List<? extends TypeMirror> args = t.getTypeArguments();

		boolean optional = "java.util.Optional".equals(fqn);

		if (optional && unwrapOptional)
		{
			if (args.isEmpty())
			{
				return new ResolvedType(true, fqn);
			}
			else
			{
				return new ResolvedType(true, args.get(0).accept(new TypeResolver(false, this), null).getType());
			}
		}
		else
		{
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
					String argTypeString = argType.accept(new TypeResolver(false, this), null).type;
					fqn += argTypeString;
				}
				fqn = fqn + ">";
			}
			return new ResolvedType(fqn);
		}
	}

	@Override
	public ResolvedType visitTypeVariable(TypeVariable t, Void p)
	{
		TypeVariable tv = t;
		TypeMirror lb = tv.getLowerBound();
		TypeKind lbk = lb.getKind();
		if (TypeKind.NONE.equals(lbk) == false && TypeKind.NULL.equals(lbk) == false)
		{
			return new ResolvedType(" ? super " + lb.accept(new TypeResolver(false, this), null).getType());
		}
		else
		{
			return new ResolvedType(
				" ? extends " + tv.getUpperBound().accept(new TypeResolver(false, this), null).getType());
		}
	}

	@Override
	public ResolvedType visitWildcard(WildcardType t, Void p)
	{
		if (t.getSuperBound() != null)
		{
			return new ResolvedType(
				" ? super " + t.getSuperBound().accept(new TypeResolver(false, this), null).getType());
		}
		else if (t.getExtendsBound() != null)
		{
			return new ResolvedType(
				" ? extends " + t.getExtendsBound().accept(new TypeResolver(false, this), null).getType());
		}
		else
		{
			return new ResolvedType(" ? ");
		}
	}

	@Override
	public ResolvedType visitError(ErrorType t, Void p)
	{
		return new ResolvedType("/* FIXME MetaGen could not process this type: " + t.toString() +
			". Substituting with Object */ " + Object.class.getName());
	}

	@Override
	public ResolvedType visitNoType(NoType t, Void aVoid)
	{
		return new ResolvedType(false, "void");
	}
}
