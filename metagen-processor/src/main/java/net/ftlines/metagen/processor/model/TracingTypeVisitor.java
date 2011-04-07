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

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;

public class TracingTypeVisitor<R, P> implements TypeVisitor<R, P>
{
	private final TypeVisitor<R, P> next;

	public TracingTypeVisitor()
	{
		this(null);
	}

	public TracingTypeVisitor(TypeVisitor<R, P> next)
	{
		this.next = next;
	}

	@Override
	public R visit(TypeMirror t, P p)
	{
		log("visit(Type Mirror=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visit(t, p);
		return null;
	}

	private void log(String string, Object... params)
	{
		System.out.println(String.format(string, params));

	}

	@Override
	public R visit(TypeMirror t)
	{
		log("visit(Type Mirror=[%s])", t.toString());
		if (next != null)
			return next.visit(t);
		return null;
	}

	@Override
	public R visitPrimitive(PrimitiveType t, P p)
	{
		log("visitPrimitive(PrimitiveType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitPrimitive(t, p);
		return null;
	}

	@Override
	public R visitNull(NullType t, P p)
	{
		log("visitMull(NullType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitNull(t, p);
		return null;
	}

	@Override
	public R visitArray(ArrayType t, P p)
	{
		log("visitArray(ArrayType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitArray(t, p);
		return null;
	}

	@Override
	public R visitDeclared(DeclaredType t, P p)
	{
		log("visitDeclared(DeclaredType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitDeclared(t, p);
		return null;
	}

	@Override
	public R visitError(ErrorType t, P p)
	{
		log("visitError(ErrorType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitError(t, p);
		return null;
	}

	@Override
	public R visitTypeVariable(TypeVariable t, P p)
	{
		log("visitTypeVariable(TypeVariable=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitTypeVariable(t, p);
		return null;
	}

	@Override
	public R visitWildcard(WildcardType t, P p)
	{
		log("visitWildcard(WildcardType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitWildcard(t, p);
		return null;
	}

	@Override
	public R visitExecutable(ExecutableType t, P p)
	{
		log("visitExecutable(ExecutableType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitExecutable(t, p);
		return null;
	}

	@Override
	public R visitNoType(NoType t, P p)
	{
		log("visitNoType(NoType=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitNoType(t, p);
		return null;
	}

	@Override
	public R visitUnknown(TypeMirror t, P p)
	{
		log("visitUnknown(TypeMirror=[%s], P p=[%s])", t.toString(), p);
		if (next != null)
			return next.visitUnknown(t, p);
		return null;
	}

}
