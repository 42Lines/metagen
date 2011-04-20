/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ftlines.metagen.property.visibility;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.ftlines.metagen.processor.MetaPackageTest;

import org.junit.Test;

public class VisibilityTest extends MetaPackageTest {

	@Test
	public void test() throws FileNotFoundException, SecurityException,
			IllegalArgumentException, ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		assertTrue(result.isClean());

		Field field = result.getMetaField(TestBean.class, "publicProperty");
		assertTrue((field.getModifiers() & Modifier.PUBLIC) > 0);
		
		field = result.getMetaField(TestBean.class, "protectedProperty");
		assertTrue((field.getModifiers() & Modifier.PROTECTED) > 0);
		
		field = result.getMetaField(TestBean.class, "defaultProperty");
		assertTrue((field.getModifiers() & Modifier.PUBLIC) == 0);
		assertTrue((field.getModifiers() & Modifier.PROTECTED) == 0);
		assertTrue((field.getModifiers() & Modifier.PRIVATE) == 0);
	}
	
	@Test
	public void testOverride() throws FileNotFoundException, SecurityException,
			IllegalArgumentException, ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		assertTrue(result.isClean());

		// p1 gets most relaxed visibility - public getter
		Field field = result.getMetaField(OverrideBean.class, "p1");
		assertTrue((field.getModifiers() & Modifier.PUBLIC) > 0);
		
		// p2 gets visibility of annotated member - protected field
		field = result.getMetaField(OverrideBean.class, "p2");
		assertTrue((field.getModifiers() & Modifier.PROTECTED) > 0);
	}
}
