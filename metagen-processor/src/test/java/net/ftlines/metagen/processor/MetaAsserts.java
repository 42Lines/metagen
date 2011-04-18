package net.ftlines.metagen.processor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

public class MetaAsserts {
	public static void assertMetaClassNotGenerated(
			MetaCompilationResult result, Class<?> clazz) {
		try {
			result.getMetaClass(clazz);
			fail();
		} catch (ClassNotFoundException e) {
			// expected
		} catch (FileNotFoundException e) {
			// expected
		}
	}

	public static void assertMetaClassGenerated(MetaCompilationResult result,
			Class<?> clazz) {
		try {
			result.getMetaClass(clazz);
		} catch (ClassNotFoundException e) {
			fail();
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	public static void assertMetaPropertyGenerated(
			MetaCompilationResult result, Class<?> clazz, String name)
			throws FileNotFoundException, SecurityException,
			IllegalArgumentException, ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		assertNotNull(result.getMetaProperty(clazz, name));
	}

	public static void assertMetaPropertyNotGenerated(
			MetaCompilationResult result, Class<?> clazz, String name)
			throws FileNotFoundException, SecurityException,
			IllegalArgumentException, ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException {
		assertNull(result.getMetaProperty(clazz, name));
	}

}
