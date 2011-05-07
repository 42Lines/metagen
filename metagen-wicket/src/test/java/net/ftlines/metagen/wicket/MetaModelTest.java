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

package net.ftlines.metagen.wicket;

import static org.junit.Assert.*;

import org.apache.wicket.model.IModel;
import org.junit.Test;

public class MetaModelTest
{
	@Test
	public void propertyAccess()
	{
		Address address = new Address();
		address.setStreet("street");
		address.setCity("city");

		Person person = new Person();
		person.setName("person");
		person.setAddress(address);

		IModel<String> street = MetaModel.of(person).get(PersonMeta.address).get(AddressMeta.street);

		assertEquals(address.getStreet(), street.getObject());
	}
}
