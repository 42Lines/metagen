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

import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.model.QualifiedName;

public class Constants {
	public static final String PROPERTY = "net.ftlines.metagen.annot.Property";
	public static final String BEAN = "net.ftlines.metagen.annot.Bean";
	public static final String IGNORE = "net.ftlines.metagen.annot.Ignore";

	public static final String ENTITY = "javax.persistence.Entity";
	public static final String MAPPED_SUPERCLASS = "javax.persistence.MappedSuperclass";

	public static final String SINGULAR = "net.ftlines.metagen.SingularProperty";
	private static final String MARKER = "Meta";

	public static QualifiedName getMetaClassName(TypeElement element) {

		String cn = element.getSimpleName() + MARKER;
		while (element.getNestingKind() != NestingKind.TOP_LEVEL) {
			element = (TypeElement) element.getEnclosingElement();
			cn = element.getSimpleName() + MARKER + "." + cn;
		}
		cn = new QualifiedName(element.getQualifiedName().toString())
				.getNamespace() + "." + cn;

		return new QualifiedName(cn);
	}

	public static QualifiedName getMetaClassName(Class<?> source) {
		String cn = source.getSimpleName() + Constants.MARKER;

		while (source.getDeclaringClass() != null) {
			source = source.getDeclaringClass();
			cn = (source.getSimpleName() + Constants.MARKER) + "$" + cn;
		}

		cn = source.getPackage().getName() + "." + cn;

		return new QualifiedName(cn);
	}

}
