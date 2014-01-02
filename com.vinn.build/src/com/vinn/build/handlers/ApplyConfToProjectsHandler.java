package com.vinn.build.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class ApplyConfToProjectsHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    MessageDialog.openInformation(window.getShell(), "Vinn Build", "Hello, Eclipse world");
    
   // Use Eclipse resource filters to hide from view AND from the indexer
    final String[] enabled = {"../build.test/component"};
    
    IProject p = null;
    try {
      p.createFilter( 
        IResourceFilterDescription.EXCLUDE_ALL | IResourceFilterDescription.FOLDERS|  
        IResourceFilterDescription.INHERITABLE,
            new FileInfoMatcherDescription("org.eclipse.core.resources.regexFilterMatcher",
                "xml"), IResource.BACKGROUND_REFRESH, null);
      
      IResourceFilterDescription[] a = p.getFilters();
      
      a[0].delete(IResource.BACKGROUND_REFRESH, null);
     
    } catch (CoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return null;
  }


}
