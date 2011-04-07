package net.ftlines.metagen;

/**
 * 
 * @author igor
 * 
 * @param <C>
 *            type that contains the attribute
 */
public class Property<C> {
	private final String name;

	public Property(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
