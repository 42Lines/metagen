package net.ftlines.metagen.processor.framework;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import net.ftlines.metagen.processor.util.Optional;

import org.junit.Assert;

public class ReflectionAssert {
	public static void assertHasDeclaredField(Class<?> clazz, String name,
			Type type, int... modifiers) {

		if (findField(clazz, name, type, modifiers).isNull()) {
			Assert.fail("Could not find field: " + name + " in class: "
					+ clazz.getName());
		}

	}

	private static Optional<Field> findField(Class<?> clazz, String name,
			Type type, int... modifiers) {

		for (Field field : clazz.getDeclaredFields()) {
			if (field.getName().equals(name) && field.getType().equals(type)) {
				for (int modifier : modifiers) {
					if ((field.getModifiers() & modifier) == 0) {
						continue;
					}
				}
				return Optional.of(field);
			}
		}
		return Optional.of(null);
	}
}
