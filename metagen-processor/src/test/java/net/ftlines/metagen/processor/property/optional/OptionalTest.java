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

package net.ftlines.metagen.processor.property.optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.ftlines.metagen.processor.MetaPackageTest;


public class OptionalTest extends MetaPackageTest
{

	@Test
	public void testStringProperty() throws Exception
	{
		assertTrue(result.isClean());
		assertTrue(result.getMetaSource(Person.class).exists());
		assertEquals(String.class, result.getPropertyFirstParameterizedType(Person.class, "name"));
	}

	@Test
	public void testObjectProperty() throws Exception
	{
		assertTrue(result.isClean());
		assertTrue(result.getMetaSource(Person.class).exists());
		assertEquals(Address.class, result.getPropertyFirstParameterizedType(Person.class, "address"));
	}

	@Test
	public void testExtendsProperty() throws Exception
	{
		assertTrue(result.isClean());
		assertTrue(result.getMetaSource(Person.class).exists());
		assertEquals("? extends " + Address.class.getName(),
			result.getPropertyFirstParameterizedType(Person.class, "extendsAddress").toString());
	}

	@Test
	public void testTypedProperty() throws Exception
	{
		assertTrue(result.isClean());
		assertTrue(result.getMetaSource(Person.class).exists());
		assertEquals("? extends " + Address.class.getName(),
			result.getPropertyFirstParameterizedType(Person.class, "typedAddress").toString());
	}

	@Test
	public void testGetter() throws Exception
	{
		assertTrue(result.isClean());
		assertTrue(result.getMetaSource(Person.class).exists());
		assertEquals(String.class, result.getPropertyFirstParameterizedType(Person.class, "email"));
	}
}
