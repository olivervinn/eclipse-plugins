/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.build.handlers;

import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.vinn.build.Utils;


public class RefreshProjectsHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {   

    ICProject[] cProjects = Utils.getCProjects();

    for (int i = 0; i < cProjects.length; i++) {
      try {
        cProjects[i].getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }
  return null;
  }
}
