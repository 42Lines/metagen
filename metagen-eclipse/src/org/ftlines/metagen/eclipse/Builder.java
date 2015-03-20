package org.ftlines.metagen.eclipse;

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class Builder extends IncrementalProjectBuilder {
	public static final String BUILDER_ID = "metagen-eclipse.metagenBuilder";
	static final String MARKER_TYPE = "metagen-eclipse.metagenProblem";

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {

		getProject().deleteMarkers(MARKER_TYPE, true, 0);

		IClasspathEntry metagenClasspathEntry = findMetagenClassPathEntry();
		if (metagenClasspathEntry == null) {
			addMarker(getProject(), "Cannot find metagen build path entry", IMarker.SEVERITY_ERROR);
			return null;
		}

		IJavaProject javaProject = JavaCore.create(getProject());
		if (javaProject == null) {
			addMarker(getProject(), "Metagen can only run on Java projects", IMarker.SEVERITY_ERROR);
			return null;
		}

		Build build = new Build(javaProject, monitor, metagenClasspathEntry);

		IResourceDelta delta = null;
		if (kind != FULL_BUILD) {
			delta = getDelta(getProject());
		}

		if (delta != null) {
			// incremental build

			delta.accept(new IncrementalBuildVisitor(build));
		} else {
			// full build

			build.deleteAllMetaFiles();
			getProject().accept(new FullBuildVisitor(build));
		}

		return null;
	}

	private void addMarker(IResource file, String message, int severity) throws CoreException {
		IMarker marker = file.createMarker(MARKER_TYPE);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.SEVERITY, severity);
	}

	private IClasspathEntry findMetagenClassPathEntry() throws JavaModelException {
		IJavaProject project = JavaCore.create(getProject());

		IClasspathEntry metagen = null;
		IClasspathEntry annotations = null;

		for (IClasspathEntry classpath : project.getRawClasspath()) {
			if (classpath.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				if ("metagen".equals(classpath.getPath().lastSegment())) {
					metagen = classpath;
				} else if ("annotations".equals(classpath.getPath().lastSegment())) {
					annotations = classpath;
				}
			}
		}

		if (metagen != null) {
			return metagen;
		} else if (annotations != null) {
			return annotations;
		}

		return null;
	}

	class IncrementalBuildVisitor implements IResourceDeltaVisitor {
		private final Build build;

		private IncrementalBuildVisitor(Build build) {
			this.build = build;
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			try {
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					build.generateMetaFile(resource);
					break;
				case IResourceDelta.REMOVED:
					build.deleteMetaFile(resource);
					break;
				case IResourceDelta.CHANGED:
					build.deleteMarkers(resource);
					build.generateMetaFile(resource);
					break;
				}
			} catch (Exception e) {
				addMarker(resource, "Metagen unhandled error: " + e.getClass() + "/" + e.getMessage(), IMarker.SEVERITY_ERROR);
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}

	class FullBuildVisitor implements IResourceVisitor {
		private final Build build;

		private FullBuildVisitor(Build build) {
			this.build = build;
		}

		@Override
		public boolean visit(IResource resource) throws CoreException {
			try {
				build.generateMetaFile(resource);
			} catch (Exception e) {
				addMarker(resource, "Metagen unhandled error: " + e.getClass() + "/" + e.getMessage(), IMarker.SEVERITY_ERROR);
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}

}
