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
import java.util.Optional;

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
	private boolean fieldOptional = false, getterOptional = false, setterOptional = false;

	public Property(String name, Class<?> container, String fieldName, boolean fieldOptional, String getterName,
		boolean getterOptional, String setterName, boolean setterOptional)
	{
		this.name = name;
		this.fieldOptional = fieldOptional;
		this.getterOptional = getterOptional;
		this.setterOptional = setterOptional;

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
				Class<?> type = (field != null) ? field.getType() : getter.getReturnType();
				try
				{
					setter = container.getDeclaredMethod(setterName, type);
				}
				catch (NoSuchMethodException e)
				{
					Class<?> alternateType = getAutoboxAlternative(type);
					setter = container.getDeclaredMethod(setterName, alternateType);
				}
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

	public boolean isFieldOptional()
	{
		return fieldOptional;
	}

	public boolean isGetterOptional()
	{
		return getterOptional;
	}

	public boolean isSetterOptional()
	{
		return setterOptional;
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
				if (getterOptional)
				{
					Optional<R> optional = (Optional<R>)getter.invoke(instance);
					return optional != null ? optional.orElse(null) : null;
				}
				else
				{
					return (R)getter.invoke(instance);
				}
			}
			else
			{
				if (fieldOptional)
				{
					Optional<R> optional = (Optional<R>)field.get(instance);
					return optional != null ? optional.orElse(null) : null;
				}
				else
				{
					return (R)field.get(instance);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while getting value of property: " + name + " of object: " + instance,
				e);
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
				if (setterOptional)
				{
					setter.invoke(instance, new Object[] { Optional.ofNullable(value) });
				}
				else
				{
					setter.invoke(instance, new Object[] { value });
				}
			}
			else
			{
				if (fieldOptional)
				{
					field.set(instance, Optional.ofNullable(value));
				}
				else
				{
					field.set(instance, value);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Exception while setting value of property: " + name + " of object: " +
				instance + " to value: " + value, e);
		}
	}


	/*
	 * public Class<?> getType() { if (getter != null) { return getter.getReturnType(); } else if
	 * (setter != null) { return setter.getParameterTypes()[0]; } else { return field.getType(); } }
	 * 
	 * public Class<?> getDeclaringClass() { if (getter != null) { return getter.getDeclaringClass(); }
	 * else if (setter != null) { return setter.getDeclaringClass(); } else { return
	 * field.getDeclaringClass(); } }
	 */

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

	private static Class<?> getAutoboxAlternative(Class<?> type)
	{
		if (Boolean.class.equals(type))
		{
			return Boolean.TYPE;
		}
		else if (Boolean.TYPE.equals(type))
		{
			return Boolean.class;
		}
		else if (Integer.class.equals(type))
		{
			return Integer.TYPE;
		}
		else if (Integer.TYPE.equals(type))
		{
			return Integer.class;
		}
		else if (Long.class.equals(type))
		{
			return Long.TYPE;
		}
		else if (Long.TYPE.equals(type))
		{
			return Long.class;
		}
		else if (Float.class.equals(type))
		{
			return Float.TYPE;
		}
		else if (Float.TYPE.equals(type))
		{
			return Float.class;
		}
		else if (Double.class.equals(type))
		{
			return Double.TYPE;
		}
		else if (Double.TYPE.equals(type))
		{
			return Double.class;
		}
		else if (Short.class.equals(type))
		{
			return Short.TYPE;
		}
		else if (Short.TYPE.equals(type))
		{
			return Short.class;
		}
		else if (Byte.class.equals(type))
		{
			return Byte.TYPE;
		}
		else if (Byte.TYPE.equals(type))
		{
			return Byte.class;
		}
		else if (Character.class.equals(type))
		{
			return Character.TYPE;
		}
		else if (Character.TYPE.equals(type))
		{
			return Character.class;
		}
		else
		{
			return null;
		}
	}

	private static String accessorName(String prefix, String field)
	{
		return prefix + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	static abstract class SerializedProperty implements Serializable
	{
		String n, cn, fn, gn, sn;
		boolean fo, go, so;

		public SerializedProperty(Property<?, ?> p)
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
			fo = p.isFieldOptional();
			go = p.isGetterOptional();
			so = p.isSetterOptional();
		}

	}

}
