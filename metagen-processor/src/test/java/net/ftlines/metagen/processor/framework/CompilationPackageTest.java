package net.ftlines.metagen.processor.framework;

import java.io.File;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CompilationPackageTest<T extends CompilationResult> {
	private static final Logger logger = LoggerFactory
			.getLogger(CompilationPackageTest.class);

	private static final File SOURCE_BASE = new File("src/test/java");
	private static final File OUTPUT_BASE = new File("target/generated");

	protected Compilation compilation;
	protected T result;

	@Before
	public void setupCompilation() {
		compilation = new Compilation(SOURCE_BASE, OUTPUT_BASE);
		compilation.addDirectory(getClass());
		compilation.rebuildOutputFolder();
		configureCompilation(compilation);
		result = configureResult(compilation.compile());
	}

	protected void configureCompilation(Compilation compilation) {

	}

	protected T configureResult(CompilationResult result) {
		return (T) result;
	}

}
