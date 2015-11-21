/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.workspace.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class RefreshProjectsHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

    Job job = new Job("Refreshing all projects") {
      @Override
      protected IStatus run(IProgressMonitor monitor) {

        for (int i = 0; i < projects.length; i++) {
          try {
            projects[i].refreshLocal(IResource.DEPTH_INFINITE, null);
          } catch (CoreException e) {
            e.printStackTrace();
            return Status.CANCEL_STATUS;
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
