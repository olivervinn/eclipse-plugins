package com.vinn.ui.view.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.utils.Activator;


public class ViewHopperHandler extends AbstractHandler {

  String[] fPreviousViewIds;

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
    String mode = event.getParameter("com.vinn.utils.ui.views.hopper.mode");

    if (mode.equalsIgnoreCase("set")) {
      rememberViews(page, "setviewlist");
    } else if (mode.equalsIgnoreCase("restore")) {
      
      String l = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get("setviewlist", null);
      if (l != null) {
        String[] fRestoreViewIds = l.split(";");
        restoreViews(page, fRestoreViewIds);
      }
      
    } else {
      
      String l = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get("backupviewlist", null);
      if (l != null) {
        String[] fRestoreViewIds = l.split(";");
        restoreViews(page, fRestoreViewIds);
      }
      
    }
    return null;
  }

  private void restoreViews(IWorkbenchPage page, String[] viewIds) {
    
    rememberViews(page, "backupviewlist");

    IViewReference[] visibleViewRefs = page.getViewReferences();
    boolean found = false;

    // Visible
    for (int i = 0; i < visibleViewRefs.length; i++) {
      found = false;
      String id = visibleViewRefs[i].getId();
      
      // Should be?
      for (int j = 0; j < viewIds.length; j++) {
        if (id.equals(viewIds[j])) {
          found = true;
          break;
        }
      }
      
      if (found) {
        // Already visible then remove those to show
        viewIds[i] = null;
      } else {
        // Should be hidden
        page.hideView(visibleViewRefs[i]);
      }
    }


    // For those remaining make them visible now
    for (String viewId : viewIds) {
      if (viewId != null && !viewId.equalsIgnoreCase("")) {
        try {
          page.showView(viewId);
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void rememberViews(IWorkbenchPage page, String instanceId) {
    
    String view_formatter_list = "";
    
    IViewReference[] a =
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
    for (IViewReference b : a) {
      view_formatter_list += b.getId() + ";";
    }
    
    InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(instanceId, view_formatter_list);

  }
}
