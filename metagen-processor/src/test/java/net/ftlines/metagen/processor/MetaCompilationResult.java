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

package net.ftlines.metagen.processor;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.ftlines.metagen.Property;
import net.ftlines.metagen.processor.framework.CompilationResult;
import net.ftlines.metagen.processor.framework.ForwardingCompilationResult;

public class MetaCompilationResult extends ForwardingCompilationResult
{

	public MetaCompilationResult(CompilationResult result)
	{
		super(result);
	}

	public File getMetaSource(Class<?> source) throws FileNotFoundException
	{
		// TODO broken for nested classes
		return getOutputFile(Constants.getMetaClassName(source).getQualified().replace(".", "/") + ".java");
	}

	public Class<?> getMetaClass(Class<?> source) throws FileNotFoundException, ClassNotFoundException
	{

		return getCompilationClassLoader().loadClass(Constants.getMetaClassName(source).getQualified());
	}

	public Property getMetaProperty(Class<?> source, String propertyName)
		throws FileNotFoundException, ClassNotFoundException, SecurityException, NoSuchFieldException,
		IllegalArgumentException, IllegalAccessException
	{
		Field field = getMetaField(source, propertyName);
		field.setAccessible(true);
		return (Property)field.get(null);
	}

	public Field getMetaField(Class<?> source, String propertyName)
		throws FileNotFoundException, ClassNotFoundException, SecurityException, NoSuchFieldException,
		IllegalArgumentException, IllegalAccessException
	{
		Class<?> meta = getMetaClass(source);
		return meta.getDeclaredField(propertyName);
	}

	public Method getMetaMethod(Class<?> source, String name, Class<?>... parameters) throws Exception
	{
		Class<?> meta = getMetaClass(source);
		return meta.getDeclaredMethod(name, parameters);
	}


	public Type getPropertyFirstParameterizedType(Class<?> beanType, String propertyName) throws Exception
	{
		Field field = getMetaField(beanType, propertyName);
		assertTrue(field != null);

		Type type = field.getGenericType();
		assertTrue(type instanceof ParameterizedType);

		ParameterizedType ptype = (ParameterizedType)type;

		assertTrue(ptype.getActualTypeArguments().length == 2);

		Type propertyType = ptype.getActualTypeArguments()[1];
		return propertyType;
	}


}
