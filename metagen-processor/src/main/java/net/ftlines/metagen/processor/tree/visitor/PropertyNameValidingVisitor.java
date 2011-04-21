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

package net.ftlines.metagen.processor.tree.visitor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PropertyNameValidingVisitor {
	private static final Set<String> RESERVED = new HashSet<String>(
			Arrays.asList("abstract", "assert", "boolean", "break", "byte",
					"case", "catch", "char", "class", "const", "continue",
					"default", "do", "double", "else", "enum", "extends",
					"final", "finally", "float", "for", "goto", "if",
					"implements", "import", "instanceof", "int", "interface",
					"long", "native", "new", "package", "private", "protected",
					"public", "return", "short", "static", "strictfp", "super",
					"switch", "synchronized", "this", "throw", "throws",
					"transient", "try", "void", "volatile", "while", "false",
					"null", "true"));
}
