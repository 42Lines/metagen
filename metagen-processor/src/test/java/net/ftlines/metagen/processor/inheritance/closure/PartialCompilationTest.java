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

package net.ftlines.metagen.processor.inheritance.closure;

import static net.ftlines.metagen.processor.MetaAsserts.*;
import static org.junit.Assert.*;

import javax.lang.model.element.TypeElement;

import net.ftlines.metagen.processor.MetaPackageTest;
import net.ftlines.metagen.processor.MetaProcessor;
import net.ftlines.metagen.processor.framework.Compilation;

import org.junit.Test;

/**
 * This test simulates a partial compilation inside IDE. Since we are processing only {@link C} but
 * C extends B and A CMeta should have a proper superclass, in this case AMeta
 * 
 * @author igor
 * 
 */
public class PartialCompilationTest extends MetaPackageTest
{

	/**
	 * This processor simulates how a change to class {@link C} is handled inside an IDE by hiding
	 * {@link A} and {@link B} from round processing.
	 * 
	 * @author igor
	 * 
	 */
	public static class PartialProcessor extends MetaProcessor
	{
		@Override
		protected boolean accept(TypeElement element)
		{
			String name = element.getQualifiedName().toString();
			if (name.equals(A.class.getName()) || name.equals(B.class.getName()))
			{
				return false;
			}
			return true;
		}
	}


	@Override
	protected void configureCompilation(Compilation compilation)
	{
		super.configureCompilation(compilation);
		compilation.setAnnotationProcessor(PartialProcessor.class);
	}

	@Test
	public void test() throws Exception
	{
		assertTrue(result.isClean());
		assertMetaClassGenerated(result, C.class);
		assertMetaClassNotGenerated(result, B.class);

		// make sure that even though A and B were not discovered by processor's round environments
		// they are still properly processed

		assertMetaClassGenerated(result, A.class);


		assertEquals(result.getMetaClass(A.class).getSuperclass(), Object.class);
		assertMetaClassInhertance(result, C.class, A.class);
	}

}
