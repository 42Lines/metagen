package net.ftlines.metagen.wicket;

import net.ftlines.metagen.annot.Property;

public class Address {

	private String street;
	private String city;

	@Property
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Property
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
