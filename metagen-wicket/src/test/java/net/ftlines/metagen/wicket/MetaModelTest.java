package net.ftlines.metagen.wicket;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTestCase;
import org.junit.Test;

public class MetaModelTest extends WicketTestCase
{

	@Test
	public void testDirectUnwrap()
	{
		Person person = new Person();
		MetaModel meta = MetaModel.of(Model.of(person));
		assertThat(meta.get(PersonMeta.name).getObject(), is("test"));
		assertThat(meta.get(PersonMeta.name).getObject(), instanceOf(String.class));
	}

	@Test
	public void testTransitiveUnwrap()
	{
		Person person = new Person();
		MetaModel meta = MetaModel.of(Model.of(person));
		assertThat(meta.get(PersonMeta.address).get(AddressMeta.street).getObject(), is("testStreet"));
		assertThat(meta.get(PersonMeta.address).get(AddressMeta.street).getObject(), instanceOf(String.class));
	}

	@Test
	public void testOptionalSetter()
	{
		Person person = new Person();
		MetaModel meta = MetaModel.of(Model.of(person));
		MetaModel address = meta.get(PersonMeta.address);
		MetaModel street = address.get(AddressMeta.street);
		street.setObject("street1");

		assertThat(street.getObject(), is("street1"));
		assertThat(street.getObject(), instanceOf(String.class));
	}


}
