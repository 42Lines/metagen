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

package net.ftlines.metagen;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 
 * @author igor
 * 
 * @param <C>
 *            type that contains the attribute
 * @param <R>
 *            type of the attribute
 */
public abstract class Property<C, R> implements Serializable
{
	private final String name;
	private Field field = null;
	private Method getter = null, setter = null;

	public Property(String name, Class<?> container, String fieldName, String getterName, String setterName)
	{
		this.name = name;

		try
		{
			if (fieldName != null)
			{
				field = container.getDeclaredField(fieldName);
			}
			if (getterName != null)
			{
				getter = container.getDeclaredMethod(getterName);
			}
			if (setterName != null)
			{
				setter = container.getDeclaredMethod(setterName,
					(field != null) ? field.getType() : getter.getReturnType());
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while discovering field, getter, and setter of property: " + name +
				" of class: " + container.getClass().getName(), e);
		}

		if (field != null && !field.isAccessible())
		{
			field.setAccessible(true);
		}
		if (getter != null && !getter.isAccessible())
		{
			getter.setAccessible(true);
		}
		if (setter != null && !setter.isAccessible())
		{
			setter.setAccessible(true);
		}
	}

	public String getName()
	{
		return name;
	}

	public Field getField()
	{
		return field;
	}

	public Method getGetter()
	{
		return getter;
	}

	public Method getSetter()
	{
		return setter;
	}

	@SuppressWarnings("unchecked")
	public R get(C instance)
	{
		if (instance == null)
		{
			throw new IllegalArgumentException("Argument 'instance' cannot be null");
		}
		try
		{
			if (visibility(getter) >= visibility(field))
			{
				return (R)getter.invoke(instance);
			}
			else
			{
				return (R)field.get(instance);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(
				"Exception while getting value of property: " + name + " of object: " + instance, e);
		}
	}

	public void set(C instance, R value)
	{
		if (instance == null)
		{
			throw new IllegalArgumentException("Argument 'instance' cannot be null");
		}
		try
		{
			if (visibility(setter) >= visibility(field))
			{
				setter.invoke(instance, new Object[] { value });
			}
			else
			{
				field.set(instance, value);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while setting value of property: " + name + " of object: " +
				instance + " to value: " + value, e);
		}
	}

	public Class<?> getType()
	{
		if (getter != null)
		{
			return getter.getReturnType();
		}
		else if (setter != null)
		{
			return setter.getParameterTypes()[0];
		}
		else
		{
			return field.getType();
		}
	}

	public Class<?> getDeclaringClass()
	{
		if (getter != null)
		{
			return getter.getDeclaringClass();
		}
		else if (setter != null)
		{
			return setter.getDeclaringClass();
		}
		else
		{
			return field.getDeclaringClass();
		}
	}

	private static int visibility(Member member)
	{
		if (member == null)
		{
			return 0;
		}
		final int mods = member.getModifiers();
		if (Modifier.isPublic(mods))
		{
			return 4;
		}
		else if (Modifier.isProtected(mods))
		{
			return 3;
		}
		else if (Modifier.isPrivate(mods))
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}

	private static String accessorName(String prefix, String field)
	{
		return prefix + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	static class SerializedProperty implements Serializable
	{
		String n, cn, fn, gn, sn;

		public SerializedProperty(Property<?,?> p)
		{
			n = p.getName();
			if (p.getField() != null)
			{
				cn = p.getField().getDeclaringClass().getName();
				fn = p.getField().getName();
			}
			if (p.getGetter() != null)
			{
				cn = cn != null ? cn : p.getGetter().getDeclaringClass().getName();
				gn = p.getGetter().getName();
			}
			if (p.getSetter() != null)
			{
				cn = cn != null ? cn : p.getSetter().getDeclaringClass().getName();
				sn = p.getSetter().getName();
			}
		}

	}

}
