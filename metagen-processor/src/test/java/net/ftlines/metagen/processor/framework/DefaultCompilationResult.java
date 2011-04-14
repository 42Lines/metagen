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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;

public class DefaultCompilationResult implements CompilationResult {
	private final File outputDir;
	private final DiagnosticCollector collector;
	private final ClassLoader cloader;

	public DefaultCompilationResult(File outputDir,
			DiagnosticCollector collector) {
		this.outputDir = outputDir;
		this.collector = collector;

		try {
			URL url = outputDir.toURL();
			cloader = new URLClassLoader(new URL[] { url });
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<Diagnostic<?>> getDiagnostics() {
		return Collections.unmodifiableList(collector.getDiagnostics());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see binder.framework.CompilationResult#isClean()
	 */
	@Override
	public boolean isClean() {
		for (Diagnostic<?> diagnostic : getDiagnostics()) {
			switch (diagnostic.getKind()) {
			case ERROR:
			case MANDATORY_WARNING:
			case WARNING:
				return false;
			}
		}
		return true;
	}

	private File getFile(File base, Class<?> clazz, String suffix) {
		String name = clazz.getName();
		name = name.replace('.', '/');
		return new File(base, name + suffix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see binder.framework.CompilationResult#getFile(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public File getFile(Class<?> clazz, String suffix)
			throws FileNotFoundException {
		File file = getFile(outputDir, clazz, suffix);
		if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		return file;
	}

	public void dumpFile(Class<?> clazz, String suffix) throws IOException {
		File file = getFile(clazz, suffix);
		FileInputStream in = new FileInputStream(file);
		byte[] buff = new byte[1024];
		int c = 0;
		while ((c = in.read(buff)) > 0) {
			System.out.write(buff, 0, c);
		}
		in.close();
	}

	@Override
	public ClassLoader getCompilationClassLoader() {
		return cloader;
	}

}
