package net.ftlines.metagen.processor;

import java.io.File;
import java.io.FileNotFoundException;

import net.ftlines.metagen.processor.Constants;
import net.ftlines.metagen.processor.framework.CompilationResult;
import net.ftlines.metagen.processor.framework.ForwardingCompilationResult;


public class MetaCompilationResult extends ForwardingCompilationResult {

	public MetaCompilationResult(CompilationResult result) {
		super(result);
	}

	public File getMetaSource(Class<?> source) throws FileNotFoundException {
		return getFile(source, Constants.MARKER + ".java");
	}

}
