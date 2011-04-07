package net.ftlines.metagen.wicket;

import net.ftlines.metagen.annot.Property;

public class Person {
	private String name;
	private Address address;

	@Property
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Property
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
