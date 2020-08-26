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

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import net.ftlines.metagen.processor.util.Optional;

import org.junit.Assert;

public class ReflectionAssert
{
	public static void assertHasDeclaredField(Class<?> clazz, String name, Type type, int... modifiers)
	{

		if (findField(clazz, name, type, modifiers).isNull())
		{
			Assert.fail("Could not find field: " + name + " in class: " + clazz.getName());
		}

	}

	private static Optional<Field> findField(Class<?> clazz, String name, Type type, int... modifiers)
	{

		for (Field field : clazz.getDeclaredFields())
		{
			if (field.getName().equals(name) && field.getType().equals(type))
			{
				for (int modifier : modifiers)
				{
					if ((field.getModifiers() & modifier) == 0)
					{
						continue;
					}
				}
				return Optional.of(field);
			}
		}
		return Optional.of(null);
	}
}
