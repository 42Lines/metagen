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

package net.ftlines.metagen.processor.unclean.reserved.success;

import static net.ftlines.metagen.processor.MetaAsserts.assertDiagnostic;
import static net.ftlines.metagen.processor.MetaAsserts.assertMetaPropertyGenerated;

import java.io.FileNotFoundException;

import javax.tools.Diagnostic;

import net.ftlines.metagen.processor.MetaPackageTest;

import org.junit.Test;

public class ReservedKeywordRenameSuccessTest extends MetaPackageTest
{

	@Test
	public void renaming() throws FileNotFoundException, SecurityException, IllegalArgumentException,
		ClassNotFoundException, NoSuchFieldException, IllegalAccessException
	{
		// final property should be renamed to final_ with a warning

		assertDiagnostic(result, Diagnostic.Kind.WARNING, "'final'", "'final_'", "renamed", SimpleBean.class.getName());

		assertMetaPropertyGenerated(result, SimpleBean.class, "final_");
	}

}
