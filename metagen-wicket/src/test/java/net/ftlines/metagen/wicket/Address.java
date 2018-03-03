package net.ftlines.metagen.wicket;

import java.util.Optional;

import net.ftlines.metagen.annot.Bean;

@Bean
public class Address
{
	private String street = "testStreet";
	private String zip = "testZip";

	public Optional<String> getStreet()
	{
		return Optional.of(street);
	}

	public void setStreet(Optional<String> street)
	{
		this.street = street.orElse(null);
	}

	public Optional<String> getZip()
	{
		return Optional.of(zip);
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

}
