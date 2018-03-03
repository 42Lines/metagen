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

package net.ftlines.metagen.processor.strings.p;

import static net.ftlines.metagen.processor.MetaAsserts.assertMetaClassGenerated;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import net.ftlines.metagen.processor.MetaPackageTest;

public class PTest extends MetaPackageTest
{

	@Test
	public void constants() throws Exception
	{
		assertTrue(result.isClean());
		assertMetaClassGenerated(result, Bean.class);

		Class<?> meta = result.getMetaClass(Bean.class);
		Class<?> c = findInnerClass(meta, "P");
		assertNotNull(c);
		assertEquals(2, c.getDeclaredFields().length);
		assertEquals("a", c.getField("a").get(null));
		assertEquals("b", c.getField("b").get(null));

		meta = result.getMetaClass(Bean.Inner.class);
		c = findInnerClass(meta, "P");
		assertNotNull(c);
		assertEquals(1, c.getDeclaredFields().length);
		Field f = c.getDeclaredField("a");
		f.setAccessible(true);
		assertEquals("a", f.get(null));

	}

	@Test
	public void visibility() throws Exception
	{
		Class<?> meta = result.getMetaClass(Bean.class);
		Class<?> c = findInnerClass(meta, "P");
		assertNotNull(c);
		assertTrue((c.getModifiers() & Modifier.PUBLIC) > 0);
		for (String constant : new String[] { "a", "b" })
		{
			Field f = c.getDeclaredField(constant);
			assertTrue((f.getModifiers() & Modifier.PUBLIC) > 0);
			assertTrue((f.getModifiers() & Modifier.STATIC) > 0);
			assertTrue((f.getModifiers() & Modifier.FINAL) > 0);
		}

		meta = result.getMetaClass(Bean.Inner.class);
		c = findInnerClass(meta, "P");
		assertTrue((c.getModifiers() & Modifier.PROTECTED) > 0);
		assertNotNull(c);

		for (String constant : new String[] { "a" })
		{
			Field f = c.getDeclaredField(constant);
			assertTrue((f.getModifiers() & Modifier.PROTECTED) > 0);
			assertTrue((f.getModifiers() & Modifier.STATIC) > 0);
			assertTrue((f.getModifiers() & Modifier.FINAL) > 0);
		}
	}


	@Test
	public void hierarchy() throws Exception
	{
		Class<?> meta = result.getMetaClass(Bean.class);
		Class<?> c = findInnerClass(meta, "P");
		assertNotNull(c);
		assertEquals(Object.class, c.getSuperclass());

		meta = result.getMetaClass(Bean.Inner.class);
		Class<?> cc = findInnerClass(meta, "P");

		assertEquals(c, cc.getSuperclass());
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
