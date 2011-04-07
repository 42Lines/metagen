package net.ftlines.metagen.processor.basic.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import net.ftlines.metagen.processor.MetaPackageTest;

import org.junit.Test;


public class BasicTest extends MetaPackageTest {

	@Test
	public void test() throws FileNotFoundException {
		assertTrue(result.isClean());
		assertTrue(result.getMetaSource(Bean.class).exists());
	}
}
