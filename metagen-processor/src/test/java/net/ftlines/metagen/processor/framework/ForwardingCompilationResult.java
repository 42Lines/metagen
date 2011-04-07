package net.ftlines.metagen.processor.framework;

import java.io.File;
import java.io.FileNotFoundException;

public class ForwardingCompilationResult implements CompilationResult {
	protected final CompilationResult result;

	public ForwardingCompilationResult(CompilationResult result) {
		this.result = result;
	}

	public boolean isClean() {
		return result.isClean();
	}

	public File getFile(Class<?> clazz, String suffix)
			throws FileNotFoundException {
		return result.getFile(clazz, suffix);
	}

}
