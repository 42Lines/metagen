package net.ftlines.metagen.processor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import javax.tools.Diagnostic;

import net.ftlines.metagen.processor.framework.CompilationResult;

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

	public static void assertMetaClassInhertance(MetaCompilationResult result,
			Class<?> clazz, Class<?> superClazz) throws FileNotFoundException,
			ClassNotFoundException {

		Class<?> metaClass = result.getMetaClass(clazz);
		Class<?> metaSuperClass = result.getMetaClass(superClazz);

		Class<?> cursor = metaClass.getSuperclass();
		while (cursor != null) {
			if (cursor.equals(metaSuperClass)) {
				return;
			}
			cursor=cursor.getSuperclass();
		}
		fail("Meta class: " + metaClass.getName() + " does not extend: "
				+ metaSuperClass.getName());
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

	public static void assertDiagnostic(CompilationResult result,
			Diagnostic.Kind kind, String... keywords) {
		boolean found = false;
		for (Diagnostic diagnostic : result.getDiagnostics()) {
			if (matches(diagnostic, kind, keywords)) {
				found = true;
				break;
			}
		}
		if (!found) {
			fail("Expected dignostic not found");
		}
	}

	private static boolean matches(Diagnostic diag, Diagnostic.Kind kind,
			String... keywords) {
		if (!diag.getKind().equals(kind)) {
			return false;
		}
		String m = diag.getMessage(null).toLowerCase();
		for (String keyword : keywords) {
			if (!m.contains(keyword.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

}
