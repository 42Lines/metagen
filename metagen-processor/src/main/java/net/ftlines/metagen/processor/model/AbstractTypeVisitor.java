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
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;

public class AbstractTypeVisitor<R, P> implements TypeVisitor<R, P>
{

	@Override
	public R visit(TypeMirror t, P p)
	{
		throw new UnsupportedOperationException(t.toString());
	}

	@Override
	public R visit(TypeMirror t)
	{
		return visit(t, null);
	}

	@Override
	public R visitPrimitive(PrimitiveType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitNull(NullType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitArray(ArrayType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitDeclared(DeclaredType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitError(ErrorType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitTypeVariable(TypeVariable t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitWildcard(WildcardType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitExecutable(ExecutableType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitNoType(NoType t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitUnknown(TypeMirror t, P p)
	{
		return visit(t, p);
	}

	@Override
	public R visitUnion(UnionType t, P p)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
