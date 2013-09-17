package org.ftlines.metagen.eclipse;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ITypeBinding;

/**
 * Resolves a type binding into its most concrete type, replacing type variables with their bounds, etc.
 * 
 * @author igor
 * 
 */
public class TypeResolver {
	private final ITypeBinding rootType;
	private final Set<ITypeBinding> processed = new HashSet<ITypeBinding>();

	public TypeResolver(ITypeBinding type) {
		this.rootType = type;
	}

	public String resolve() {
		return internalResolve(rootType);
	}

	private String internalResolve(ITypeBinding type) {
		if (processed.contains(type)) {
			// we are already processing this type variable via recursion, fall back on erasure
			boolean upperBound = isUpperBoundTypeVariable(type);
			return (upperBound ? "? extends " : "") + type.getErasure().getQualifiedName();
		}

		processed.add(type);
		try {

			if (type.isWildcardType()) {

				// ? extends Foo or ? super Foo

				if (type.isUpperbound()) {
					return "? extends " + internalResolve(type.getBound());
				} else {
					return "? super " + internalResolve(type.getBound());
				}
			} else if (type.isCapture()) {

				// should not happen
				throw new RuntimeException("Capture types: " + type.getQualifiedName() + " are not yet supported");

			} else if (type.isTypeVariable()) {

				// T

				ITypeBinding[] bindings = type.getTypeBounds();
				if (bindings.length == 0) {
					return "?";
				} else {
					ITypeBinding firstBound = bindings[0];
					String bindingName = internalResolve(firstBound);

					if (type.getSuperclass() != null && firstBound == type.getSuperclass()) {
						return "? extends " + bindingName;

					}
					if (type.getSuperclass().getInterfaces().length > 0 && firstBound != type.getSuperclass()) {
						return "? extends " + bindingName;
					}
					return bindingName;
				}

			}

			String name = type.getErasure().getQualifiedName();

			if (type.isParameterizedType()) {

				// SomeClass<A,B>

				ITypeBinding[] params = type.getTypeArguments();

				name += "<";

				boolean first = true;
				for (ITypeBinding param : params) {
					if (!first) {
						name += ", ";
					}
					first = false;

					name += internalResolve(param);
				}

				name += ">";
			}

			return name;

		} finally {

			processed.remove(type);

		}
	}

	private boolean isUpperBoundTypeVariable(ITypeBinding type) {
		if (type.isTypeVariable()) {
			ITypeBinding firstBound = type.getTypeBounds()[0];
			if (type.getSuperclass() != null && firstBound == type.getSuperclass()) {
				return true;

			}
			if (type.getSuperclass().getInterfaces().length > 0 && firstBound != type.getSuperclass()) {
				return true;
			}
		}
		return false;
	}

	// private void dumpType(ITypeBinding type) {
	// String out = "[Type " + type.getQualifiedName();
	// if (type.isCapture()) {
	// out += " capture";
	// }
	// if (type.isGenericType()) {
	// out += " generic";
	// }
	// if (type.isParameterizedType()) {
	// out += " parameterized";
	// }
	// if (type.isRawType()) {
	// out += " raw";
	// }
	// if (type.isRecovered()) {
	// out += " recovered";
	// }
	// if (type.isTypeVariable()) {
	// out += " typevar";
	// }
	// if (type.isUpperbound()) {
	// out += " upperbound";
	// }
	// if (type.isWildcardType()) {
	// out += " wildcard";
	// }
	// if (type.isAnnotation()) {
	// out += " annotation";
	// }
	// if (type.isAnonymous()) {
	// out += " anonymous";
	// }
	// if (type.isClass()) {
	// out += " class";
	// }
	// if (type.isEnum()) {
	// out += " enum";
	// }
	// if (type.isFromSource()) {
	// out += " from-source";
	// }
	// if (type.isInterface()) {
	// out += " interface";
	// }
	// if (type.isLocal()) {
	// out += " local";
	// }
	// if (type.isMember()) {
	// out += " member";
	// }
	// if (type.isNested()) {
	// out += " nested";
	// }
	// if (type.isNullType()) {
	// out += " null";
	// }
	// if (type.isPrimitive()) {
	// out += " primitive";
	// }
	// if (type.isSynthetic()) {
	// out += " synthetic";
	// }
	// ITypeBinding[] temp;
	// temp = type.getTypeArguments();
	// out += " type-arguments:" + temp.length;
	// temp = type.getTypeBounds();
	// out += " bounds:" + temp.length;
	// temp = type.getTypeParameters();
	// out += " type-params:" + temp.length;
	// if (type.getBound() != null) {
	// out += " bound:" + type.getBound().getName();
	// }
	// if (type.getGenericTypeOfWildcardType() != null) {
	// out += " generic-type-of-wildcard:" + type.getGenericTypeOfWildcardType().getName();
	// }
	// if (type.getWildcard() != null) {
	// out += " wildcard:" + type.getWildcard().getName();
	// }
	// out += "]";
	// System.out.println(out);
	// System.out.println(type.getQualifiedName());
	// System.out.println("----------------------------------");
	// }

}
