package net.ftlines.metagen.processor.framework;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Compilation
{
	private static final Logger logger = LoggerFactory.getLogger(Compilation.class);

	private final File sourceBase;
	private final File outputBase;
	private FileFilter sourceFilter = new FileFilter()
	{

		@Override
		public boolean accept(File pathname)
		{
			if (!pathname.isFile())
			{
				return false;
			}

			if (!pathname.getName().endsWith(".java"))
			{
				return false;
			}

			if (pathname.getName().endsWith("Test.java"))
			{
				return false;
			}

			if (pathname.getName().endsWith("TestProcessor.java"))
			{
				return false;
			}

			
			return true;
		}
	};

	private Set<File> sources = new HashSet<File>();
	private List<String> options = new ArrayList<String>();

	public Compilation(String sourceBase, String outputBase)
	{
		this(new File(sourceBase), new File(outputBase));
	}

	public Compilation(File sourceBase, File outputBase)
	{
		this.sourceBase = sourceBase;
		this.outputBase = outputBase;
	}

	public Compilation addFile(Class<?>... classes)
	{
		for (int i = 0; i < classes.length; i++)
		{
			sources.add(getFile(sourceBase, classes[i], ".java"));
		}
		return this;
	}

	public Compilation addDirectory(Class<?>... classes)
	{
		for (int i = 0; i < classes.length; i++)
		{
			File source = getFile(sourceBase, classes[i], ".java");
			File pkg = source.getParentFile();

			sources.addAll(Arrays.asList(pkg.listFiles(sourceFilter)));
		}
		return this;
	}

	private File getFile(File base, Class<?> clazz, String suffix)
	{
		String name = clazz.getName();
		name = name.replace('.', '/');
		return new File(base, name + suffix);
	}


	public Compilation rebuildOutputFolder()
	{
		boolean deleted = delete(outputBase);
		if (!deleted)
		{
			throw new RuntimeException("Could not delete base output directory: " + outputBase.getAbsolutePath());
		}

		boolean created = outputBase.mkdirs();
		if (!created)
		{
			throw new RuntimeException("Could not create base output directory: " + outputBase.getAbsolutePath());
		}
		return this;
	}


	public void setAnnotationProcessor(Class<?> processor)
	{
		options.add("-processor");
		options.add(processor.getName());
	}

	public CompilationResult compile()
	{
		List<String> compilerOptions = new ArrayList<String>(options);
		compilerOptions.add("-d");
		compilerOptions.add(outputBase.getAbsolutePath());

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager filer = compiler.getStandardFileManager(diagnosticsCollector, null, null);
		Iterable<? extends JavaFileObject> units = filer.getJavaFileObjectsFromFiles(sources);

		JavaCompiler.CompilationTask task = compiler.getTask(null, filer, diagnosticsCollector, compilerOptions, null,
			units);
		task.call();

		for (Diagnostic<?> diagnostic : diagnosticsCollector.getDiagnostics())
		{
			logger.debug(diagnostic.getMessage(null));
		}

		try
		{
			filer.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Cannot close filer", e);
		}

		return new DefaultCompilationResult(outputBase, diagnosticsCollector);
	}


	private static boolean delete(File file)
	{
		if (!file.exists())
		{
			return true;
		}
		if (file.isDirectory())
		{
			for (File child : file.listFiles())
			{
				delete(child);
			}
		}
		return file.delete();
	}

}
