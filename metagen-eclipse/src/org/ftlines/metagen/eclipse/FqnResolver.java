package org.ftlines.metagen.eclipse;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

public class FqnResolver {
	private final ICompilationUnit cu;

	public FqnResolver(ICompilationUnit cu) {
		this.cu = cu;
	}

	public boolean is(IJavaElement element, String fqn) throws JavaModelException {
		return is(element.getElementName(), fqn);
	}

	public boolean isAny(IJavaElement element, String... fqns) throws JavaModelException {
		for (String fqn : fqns) {
			if (is(element, fqn)) {
				return true;
			}
		}
		return false;
	}

	private boolean is(String name, String fqn) throws JavaModelException {
		if (fqn.equals(name)) {
			// we matched a fully qualified name, done
			return true;
		} else if (fqn.endsWith("." + name)) {
			// we matched an unqualified name, check if there is an import
			for (IImportDeclaration declaration : cu.getImports()) {
				if (declaration.getElementName().equals(fqn)) {
					// we matched a fully qualified import, done
					return true;
				} else if (declaration.isOnDemand()) {
					// this is a wild card import: import foo.bar.*;

					// strip simple name from fully qualified, leaving just the package
					String packageName = fqn.substring(0, fqn.lastIndexOf("."));

					// strip .* from the end of ondemand import
					String importPackageName = declaration.getElementName().substring(0, declaration.getElementName().length() - 2);

					if (packageName.equals(importPackageName)) {
						return true;
					}
				}
			}

		}
		return false;
	}
}
