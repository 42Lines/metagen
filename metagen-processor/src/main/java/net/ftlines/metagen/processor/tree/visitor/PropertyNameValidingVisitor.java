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
