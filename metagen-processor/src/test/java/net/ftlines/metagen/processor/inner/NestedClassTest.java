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

package net.ftlines.metagen.processor.inner;

import static org.junit.Assert.*;
import net.ftlines.metagen.Property;
import net.ftlines.metagen.processor.MetaPackageTest;

import org.junit.Test;

public class NestedClassTest extends MetaPackageTest
{

	@Test
	public void test() throws Exception
	{
		assertTrue(result.isClean());

		Property property1 = result.getMetaProperty(Bean.class, "property1");
		assertNotNull(property1);
		Property inner1 = result.getMetaProperty(Bean.Inner.class, "inner1");
		assertNotNull(inner1);
	}

	@Test
	public void beansWithGaps() throws Exception
	{
		assertNotNull(result.getMetaProperty(BeanWithGaps.BeanWithNoProps.AnotherBeanWithNoProps.BeanWithProp.class,
			"int2"));
	}

	@Test
	public void beansStartingWithGaps() throws Exception
	{
		assertNotNull(result.getMetaProperty(
			BeanStartingWithGap.BeanWithNoProps.AnotherBeanWithNoProps.BeanWithProp.class, "int2"));
	}

	@Test
	public void beansWithMultipleInnerClasses() throws Exception
	{
		assertNotNull(result.getMetaProperty(BeanWithMulti.One.class, "p"));
		assertNotNull(result.getMetaProperty(BeanWithMulti.Two.class, "p"));
		assertNotNull(result.getMetaProperty(BeanWithMulti.Three.Four.class, "p"));
	}
}
