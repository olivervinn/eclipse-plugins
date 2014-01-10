/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.ui.utils.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;


public class RemoveAllProblemMarkersHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    for (IProject project : projects) {
      try {
        IMarker[] markers =
            project.getWorkspace().getRoot().findMarkers(null, true, IResource.DEPTH_INFINITE);

        for (IMarker iMarker : markers) {
          iMarker.delete();
        }
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }



    return null;
  }
}
