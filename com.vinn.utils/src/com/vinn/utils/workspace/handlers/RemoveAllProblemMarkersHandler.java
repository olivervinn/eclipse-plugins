/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.workspace.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;


public class RemoveAllProblemMarkersHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Job job = new Job("Removing markers") {
      @Override
      protected IStatus run(IProgressMonitor monitor) {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject project : projects) {
          try {
            IMarker[] markers =
                project.getWorkspace().getRoot().findMarkers(null, true, IResource.DEPTH_INFINITE);

            for (IMarker iMarker : markers) {
              iMarker.delete();
            }
          } catch (CoreException e) {
          }
        }
        return Status.OK_STATUS;
      }
    };
    job.setPriority(Job.LONG);
    job.setUser(true);
    job.schedule();
    return null;
  }
}
