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

package net.ftlines.metagen.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import net.ftlines.metagen.Property;
import net.ftlines.metagen.processor.framework.CompilationResult;
import net.ftlines.metagen.processor.framework.ForwardingCompilationResult;

public class MetaCompilationResult extends ForwardingCompilationResult {

	public MetaCompilationResult(CompilationResult result) {
		super(result);
	}

	public File getMetaSource(Class<?> source) throws FileNotFoundException {
		// TODO broken for nested classes
		return getFile(source, Constants.MARKER + ".java");
	}

	public Class<?> getMetaClass(Class<?> source) throws FileNotFoundException,
			ClassNotFoundException {

		String cn = source.getSimpleName() + Constants.MARKER;

		while (source.getDeclaringClass() != null) {
			source = source.getDeclaringClass();
			cn = (source.getSimpleName() + Constants.MARKER) + "$" + cn;
		}

		cn = source.getPackage().getName() + "." + cn;

		return getCompilationClassLoader().loadClass(cn);
	}

	public Property getMetaProperty(Class<?> source, String propertyName)
			throws FileNotFoundException, ClassNotFoundException,
			SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Class<?> meta = getMetaClass(source);
		Field field = meta.getField(propertyName);
		field.setAccessible(true);
		return (Property) field.get(null);
	}

}
