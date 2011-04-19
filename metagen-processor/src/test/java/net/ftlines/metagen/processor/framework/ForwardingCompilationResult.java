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

	public File getOutputFile(String path)
			throws FileNotFoundException {
		return result.getOutputFile(path);
	}

	@Override
	public ClassLoader getCompilationClassLoader() {
		return result.getCompilationClassLoader();
	}

}
