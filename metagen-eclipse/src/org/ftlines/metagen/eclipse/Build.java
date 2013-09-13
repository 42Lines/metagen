package org.ftlines.metagen.eclipse;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.ftlines.metagen.eclipse.model.BeanUnit;
import org.ftlines.metagen.eclipse.model.ErrorCollector;

public class Build implements ErrorCollector {

	private final IJavaProject javaProject;
	private final IProject project;
	private final IProgressMonitor monitor;
	private final IClasspathEntry root;
	private final IPath rootPath;

	public Build(IJavaProject javaProject, IProgressMonitor monitor, IClasspathEntry root) {
		this.javaProject = javaProject;
		this.project = javaProject.getProject();
		this.monitor = monitor;
		this.root = root;
		this.rootPath = root.getPath().removeFirstSegments(1);
	}

	private IFile getFile(IResource resource) {
		return project.getFile(resource.getProjectRelativePath());
	}

	private ICompilationUnit getCompilationUnit(IResource resource) {
		String extension = resource.getFileExtension();
		String name = resource.getName();
		if ("java".equals(extension) && !"package-info".equals(name)) {
			IFile file = getFile(resource);
			return JavaCore.createCompilationUnitFrom(file);
		}
		return null;
	}

	private ASTNode parse(ICompilationUnit cu) throws JavaModelException {
		if (!cu.isConsistent()) {
			return cu.reconcile(AST.JLS4, true, null, monitor);
		} else {
			ASTParser parser = ASTParser.newParser(AST.JLS4);

			// TODO: it may be possible to convert SimpleNames and QualifiedNames to
			// fully qualified names, thereby avoiding full binding resolution.

			parser.setResolveBindings(true);
			parser.setSource(cu);

			monitor.subTask("Metagen: parsing AST of " + cu.getElementName());

			return parser.createAST(monitor);
		}

	}

	private IFile getMetaFile(ICompilationUnit cu) {
		IType type = cu.findPrimaryType();
		if (type == null) {
			// compilation unit without a type - like package-info.java
			return null;
		}

		String name = type.getFullyQualifiedName().replace('.', '/') + "Meta.java";
		IPath path = rootPath.append(name);
		IFile file = project.getFile(path);
		return file;
	}

	private IFile getMetaFile(IResource resource) {
		ICompilationUnit cu = getCompilationUnit(resource);
		if (cu == null) {
			return null;
		}
		return getMetaFile(cu);
	}

	public void deleteMetaFile(IResource resource) throws CoreException {
		IFile meta = getMetaFile(resource);
		if (meta != null && meta.exists()) {
			monitor.subTask("Metagen: deleting meta file of: " + resource.getName());
			meta.delete(true, monitor);
		}
	}

	public void generateMetaFile(IResource resource) throws CoreException {
		ICompilationUnit cu = getCompilationUnit(resource);
		if (cu == null) {
			return;
		}
		if (cu.getTypes().length == 0) {
			// compilation unit without a primary type, like package-info.java
			return;
		}

		monitor.subTask("Metagen: generating meta for: " + cu.getElementName());

		ASTNode ast = parse(cu);

		BeanUnit beanUnit = new BeanUnit(cu, ast);

		beanUnit.discover(this);

		IFile metaFile = getMetaFile(cu);

		if (beanUnit.isEmpty()) {
			// no beans, cleanup in case there was an existing meta file

			if (metaFile.exists()) {
				metaFile.delete(true, monitor);
				cleanupEmptyPackages(metaFile);
			}
		} else {

			// we have something to generate, make sure the parent package exists

			ensurePackageExists(cu, metaFile);

			String source = beanUnit.generate(cu, metaFile);

			ByteArrayInputStream in = new ByteArrayInputStream(source.getBytes());

			if (metaFile.exists()) {
				metaFile.setContents(in, IResource.FORCE, monitor);
			} else {
				metaFile.create(in, true, monitor);
			}

			metaFile.setDerived(true, null);
		}
	}

	private void cleanupEmptyPackages(IFile metaFile) throws CoreException {
		IContainer container = metaFile.getParent();
		while (container.getType() == IResource.FOLDER && container.members().length == 0) {
			container.delete(true, monitor);
			container = container.getParent();
		}
	}

	private void ensurePackageExists(ICompilationUnit cu, IFile metaFile) throws CoreException {

		String[] segments = cu.getParent().getElementName().split("\\.");
		IFolder cursor = project.getFolder(rootPath);
		for (String segment : segments) {
			cursor = cursor.getFolder(segment);
			if (!cursor.exists()) {
				cursor.create(true, true, monitor);
				cursor.setDerived(true, monitor);
			}
		}
	}

	public void deleteAllMetaFiles() throws CoreException {
		IFolder resource = project.getFolder(rootPath);
		for (IResource member : resource.members()) {
			delete(member);
		}
	}

	private void delete(IResource resource) throws CoreException {
		if (resource.getType() == IResource.FOLDER) {
			IFolder folder = (IFolder) resource;
			for (IResource child : folder.members()) {
				delete(child);
			}
		}
		resource.delete(true, monitor);
	}

	private void addMarker(IResource file, String message, int lineNumber, int severity) throws CoreException {
		IMarker marker = file.createMarker(Builder.MARKER_TYPE);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.SEVERITY, severity);
		if (lineNumber == -1) {
			lineNumber = 1;
		}
		marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
	}

	public void deleteMarkers(IResource file) {
		try {
			file.deleteMarkers(Builder.MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	public void deleteAllMarkers() throws CoreException {
		project.deleteMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	@Override
	public void error(IResource resource, int line, String error) {
		try {
			addMarker(resource, error, line, IMarker.SEVERITY_ERROR);
		} catch (CoreException e) {
		}
	}
}
