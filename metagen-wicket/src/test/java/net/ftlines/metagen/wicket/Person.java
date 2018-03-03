package net.ftlines.metagen.wicket;

import java.io.Serializable;
import java.util.Optional;

import net.ftlines.metagen.annot.Bean;

@Bean
public class Person implements Serializable
{

	private Address address = new Address();

	public Optional<String> getName()
	{
		return Optional.of("test");
	}

	public Optional<Address> getAddress()
	{
		return Optional.of(address);
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}
}
