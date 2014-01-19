/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.handlers.workspace;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;


public class RefreshProjectsHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

    for (int i = 0; i < projects.length; i++) {
      try {
        projects[i].refreshLocal(IResource.DEPTH_INFINITE, null);
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
