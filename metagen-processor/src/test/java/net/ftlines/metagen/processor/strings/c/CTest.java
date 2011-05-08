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

package net.ftlines.metagen.processor.strings.c;

import static net.ftlines.metagen.processor.MetaAsserts.assertMetaClassGenerated;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.ftlines.metagen.processor.MetaPackageTest;

import org.junit.Test;

public class CTest extends MetaPackageTest
{

	@Test
	public void constants() throws Exception
	{
		assertTrue(result.isClean());
		assertMetaClassGenerated(result, Bean.class);

		Class<?> meta = result.getMetaClass(Bean.class);
		Class<?> c = findInnerClass(meta, "C");
		assertNotNull(c);
		String name = (String)c.getField("name").get(null);
		assertEquals(Bean.class.getName(), name);

		String simpleName = (String)c.getField("simpleName").get(null);
		assertEquals(Bean.class.getSimpleName(), simpleName);
	}

	@Test
	public void visibility() throws Exception
	{
		Class<?> meta = result.getMetaClass(Bean.class);
		Class<?> c = findInnerClass(meta, "C");
		assertNotNull(c);
		assertTrue((c.getModifiers() & Modifier.PUBLIC) > 0);
		for (String constant : new String[] { "name", "simpleName" })
		{
			Field f = c.getDeclaredField(constant);
			assertTrue((f.getModifiers() & Modifier.PUBLIC) > 0);
			assertTrue((f.getModifiers() & Modifier.STATIC) > 0);
			assertTrue((f.getModifiers() & Modifier.FINAL) > 0);
		}

		meta = result.getMetaClass(Bean.Inner.class);
		c = findInnerClass(meta, "C");
		assertTrue((c.getModifiers() & Modifier.PROTECTED) > 0);
		assertNotNull(c);

		for (String constant : new String[] { "name", "simpleName" })
		{
			Field f = c.getDeclaredField(constant);
			assertTrue((f.getModifiers() & Modifier.PROTECTED) > 0);
			assertTrue((f.getModifiers() & Modifier.STATIC) > 0);
			assertTrue((f.getModifiers() & Modifier.FINAL) > 0);
		}
	}


	private static Class<?> findInnerClass(Class<?> outer, String innerName)
	{
		Class<?> c = null;
		for (Class<?> inner : outer.getDeclaredClasses())
		{
			if (innerName.equals(inner.getSimpleName()))
			{
				c = inner;
				break;
			}
		}
		return c;
	}

}
