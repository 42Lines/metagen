package net.ftlines.metagen.processor.framework;

import java.io.File;
import java.io.FileNotFoundException;

public interface CompilationResult {

	boolean isClean();

	File getFile(Class<?> clazz, String suffix) throws FileNotFoundException;

}