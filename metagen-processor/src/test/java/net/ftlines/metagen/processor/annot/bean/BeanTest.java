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

package net.ftlines.metagen.processor.annot.bean;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.ftlines.metagen.Property;
import net.ftlines.metagen.processor.MetaPackageTest;

import org.junit.Test;

public class BeanTest extends MetaPackageTest {

	@Test
	public void test() throws Exception {
		assertTrue(result.isClean());

		Property property1 = result.getMetaProperty(Bean.class, "property1");
		assertNotNull(property1);
		Property property2 = result.getMetaProperty(Bean.class, "property2");
		assertNotNull(property2);
	}
}