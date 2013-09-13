package org.ftlines.metagen.eclipse.model;

import org.eclipse.core.resources.IResource;

public interface ErrorCollector {
	void error(IResource resource, int line, String error);
}
