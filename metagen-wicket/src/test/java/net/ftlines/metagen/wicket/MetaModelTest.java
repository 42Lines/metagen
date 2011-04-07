package net.ftlines.metagen.wicket;

import static org.junit.Assert.*;

import org.apache.wicket.model.IModel;
import org.junit.Test;

public class MetaModelTest {
	@Test
	public void propertyAccess() {
		Address address = new Address();
		address.setStreet("street");
		address.setCity("city");

		Person person = new Person();
		person.setName("person");
		person.setAddress(address);

		IModel<String> street = MetaModel.of(person).get(PersonMeta.address)
				.get(AddressMeta.street);

		assertEquals(address.getStreet(), street.getObject());
	}
}
