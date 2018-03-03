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

package net.ftlines.metagen.processor.tree.visitor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

import net.ftlines.metagen.processor.model.Visibility;
import net.ftlines.metagen.processor.tree.AbstractBean;
import net.ftlines.metagen.processor.tree.Property;

public class ValidatingVisitor extends BeanVisitorAdapter
{
	private final ProcessingEnvironment env;

	private Stack<AbstractBean> stack = new Stack<AbstractBean>();

	public ValidatingVisitor(ProcessingEnvironment env)
	{
		this.env = env;
	}

	@Override
	protected void enterBean(AbstractBean bean)
	{
		stack.push(bean);
	}

	@Override
	protected void exitBean(AbstractBean bean)
	{
		stack.pop();
	}

	@Override
	public void enterProperty(Property node)
	{
		AbstractBean bean = stack.peek();
		boolean isPrivate = Visibility.PRIVATE.equals(bean.getVisibility());
		if (isPrivate)
		{
			env.getMessager().printMessage(Kind.ERROR,
				String.format("Property '%s' in class '%s' is invisible because the class is private.", node.getName(),
					bean.getName().getQualified()));
			bean.remove(node);
			return;
		}

		if (Visibility.PRIVATE.equals(node.getVisibility()))
		{
			env.getMessager().printMessage(Kind.ERROR,
				String.format("Property '%s' in class '%s' is invisible because it is private.", node.getName(),
					bean.getName().getQualified()));
			bean.remove(node);
			return;
		}

		final String name = node.getName();
		final String handle = node.getHandle();
		if (RESERVED.contains(handle))
		{
			String altHandle = handle + "_";
			boolean altHandleTaken = false;
			for (Property property : bean.getProperties().values())
			{
				if (altHandle.equals(property.getHandle()))
				{
					altHandleTaken = true;
					break;
				}
			}
			if (altHandleTaken)
			{
				env.getMessager().printMessage(Kind.ERROR, String.format(
					"Property '%s' in class '%s' has the same name as a reserved word in Java. Alternate name '%s' has also been taken.",
					name, bean.getName().getQualified(), altHandle));
				bean.remove(node);
			}
			else
			{
				node.setHandle(altHandle);
				env.getMessager().printMessage(Kind.WARNING,
					String.format(
						"Property '%s' in class '%s' has the same name as a reserved word in Java, renamed to '%s'",
						name, bean.getName().getQualified(), altHandle));
			}
			return;
		}
	}

	private static final Set<String> RESERVED = new HashSet<String>(Arrays.asList("abstract", "assert", "boolean",
		"break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else",
		"enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof",
		"int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short",
		"static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
		"volatile", "while", "false", "null", "true"));
}
