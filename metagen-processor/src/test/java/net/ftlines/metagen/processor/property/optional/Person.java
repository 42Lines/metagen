package net.ftlines.metagen.processor.property.optional;


import java.util.Optional;

import net.ftlines.metagen.annot.Bean;

@Bean
public class Person<T extends Address>
{
	public Optional<String> name;
	public Optional<Address> address;
	public Optional<? extends Address> extendsAddress;
	public Optional<T> typedAddress;

	public Optional<String> getEmail()
	{
		return Optional.of("email");
	}

	public void setCounty(Optional<String> county)
	{
	}
}
