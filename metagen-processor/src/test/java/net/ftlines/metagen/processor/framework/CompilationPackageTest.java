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

package net.ftlines.metagen.processor.framework;

import java.io.File;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CompilationPackageTest<T extends CompilationResult>
{
	private static final Logger logger = LoggerFactory.getLogger(CompilationPackageTest.class);

	private static final File SOURCE_BASE = new File("src/test/java");
	private static final File OUTPUT_BASE = new File("target/generated");

	protected Compilation compilation;
	protected T result;

	@Before
	public void setupCompilation()
	{
		compilation = new Compilation(SOURCE_BASE, OUTPUT_BASE);
		compilation.addDirectory(getClass());
		compilation.rebuildOutputFolder();
		configureCompilation(compilation);
		result = configureResult(compilation.compile());
	}

	protected void configureCompilation(Compilation compilation)
	{

	}

	protected T configureResult(CompilationResult result)
	{
		return (T)result;
	}

}
