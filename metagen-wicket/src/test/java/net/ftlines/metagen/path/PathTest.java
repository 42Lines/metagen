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

package net.ftlines.metagen.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.ftlines.metagen.Path;

import org.junit.Test;

public class PathTest
{
	@Test
	public void expession()
	{
		assertEquals("address.city.name",
			Path.of(Person.class).get(PersonMeta.address).get(AddressMeta.city).get(CityMeta.name).expression());
	}

	@Test
	public void immutable()
	{
		Path<Person, Address> addr = Path.of(Person.class).get(PersonMeta.address);
		assertTrue((Object)addr != addr.get(AddressMeta.city));
	}

	@SuppressWarnings("unused")
	@Test
	public void generics()
	{
		Path<Person, Person> p = Path.of(Person.class);
		Path<Person, Address> a = p.get(PersonMeta.address);
		Path<Person, City> c = a.get(AddressMeta.city);
		Path<Person, String> n = c.get(CityMeta.name);
	}
}
