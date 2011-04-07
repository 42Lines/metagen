package net.ftlines.metagen.processor;

import net.ftlines.metagen.processor.framework.Compilation;
import net.ftlines.metagen.processor.framework.CompilationPackageTest;
import net.ftlines.metagen.processor.framework.CompilationResult;

public class MetaPackageTest extends
		CompilationPackageTest<MetaCompilationResult> {

	@Override
	protected void configureCompilation(Compilation compilation) {
		super.configureCompilation(compilation);
	}

	@Override
	protected MetaCompilationResult configureResult(CompilationResult result) {
		return new MetaCompilationResult(result);
	}

}
