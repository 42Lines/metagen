package net.ftlines.metagen.processor.inheritance;

import net.ftlines.metagen.annot.Property;

public class C<T extends Number> extends B<T> {
	@Property
	int c;

	@SuppressWarnings("rawtypes")
	public static class D extends C {
		@Property
		int d;

		public static class F extends D {
			@Property
			int f;
		}
	}
}
