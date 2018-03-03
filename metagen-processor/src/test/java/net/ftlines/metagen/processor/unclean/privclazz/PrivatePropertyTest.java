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

package net.ftlines.metagen.processor.unclean.privclazz;

import static net.ftlines.metagen.processor.MetaAsserts.assertDiagnostic;

import javax.tools.Diagnostic;

import org.junit.Test;

import net.ftlines.metagen.processor.MetaPackageTest;

public class PrivatePropertyTest extends MetaPackageTest
{

	@Test
	public void test()
	{
		assertDiagnostic(result, Diagnostic.Kind.ERROR, "'p'", SimpleBean.class.getName() + ".One", "private",
			"invisible");
	}
}
