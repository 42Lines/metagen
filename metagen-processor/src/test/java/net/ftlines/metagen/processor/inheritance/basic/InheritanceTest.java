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

package net.ftlines.metagen.processor.inheritance.basic;

import static net.ftlines.metagen.processor.MetaAsserts.assertMetaClassGenerated;
import static net.ftlines.metagen.processor.MetaAsserts.assertMetaClassInhertance;
import static net.ftlines.metagen.processor.MetaAsserts.assertMetaClassNotGenerated;
import static net.ftlines.metagen.processor.MetaAsserts.assertMetaPropertyGenerated;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.ftlines.metagen.processor.MetaPackageTest;

import org.junit.Test;

public class InheritanceTest extends MetaPackageTest
{

	@Test
	public void test() throws Exception
	{
		assertTrue(result.isClean());
		assertMetaClassGenerated(result, A.class);
		assertMetaClassNotGenerated(result, B.class);
		assertMetaClassGenerated(result, C.class);
		assertMetaClassGenerated(result, C.D.class);
		assertMetaClassGenerated(result, C.D.F.class);
		assertMetaClassGenerated(result, E.class);

		assertMetaPropertyGenerated(result, A.class, "a");
		assertMetaPropertyGenerated(result, C.class, "c");
		assertMetaPropertyGenerated(result, C.D.class, "d");
		assertMetaPropertyGenerated(result, C.D.F.class, "f");
		assertMetaPropertyGenerated(result, E.class, "e");

		assertEquals(result.getMetaClass(A.class).getSuperclass(), Object.class);
		assertMetaClassInhertance(result, C.class, A.class);
		assertMetaClassInhertance(result, C.D.class, C.class);
		assertMetaClassInhertance(result, C.D.F.class, C.D.class);
		assertMetaClassInhertance(result, E.class, C.class);
	}

}
