package com.vinn.ui.view.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.utils.Activator;

public class ViewHopperHandler extends AbstractHandler {

  final String BACKUP_LIST_ID = "backupviewlist";
  final String SHOW_LIST_ID = "setviewlist";
  final String LIST_DELIMITER = ";";
  final String HANDLE_PARAM_ID = "com.vinn.utils.ui.views.hopper.mode";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
    String mode = event.getParameter(HANDLE_PARAM_ID);

    if (mode.equalsIgnoreCase("set")) {
      String delimList = getOpenViews(page);
      saveViews(page, delimList,SHOW_LIST_ID);
    } else if (mode.equalsIgnoreCase("restore")) {
      String l = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(SHOW_LIST_ID, null);
      if (l != null) {
        String[] fRestoreViewIds = l.split(LIST_DELIMITER);
        restoreViews(page, fRestoreViewIds);
      }
    } else {
      String l = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(BACKUP_LIST_ID, null);
      if (l != null) {
        String[] fRestoreViewIds = l.split(LIST_DELIMITER);
        restoreViews(page, fRestoreViewIds);
      }
    }
    return null;
  }

  private void restoreViews(IWorkbenchPage page, String[] viewIdsToShow) {
    IViewReference[] visibleViewRefs = page.getViewReferences();
    boolean changeOccurred = false;
    String openPriorToChange = getOpenViews(page);

    for (int visibleViewRefIndex = 0; visibleViewRefIndex < visibleViewRefs.length; visibleViewRefIndex++) {
      boolean found = false;
      for (int viewIdIndex = 0; viewIdIndex < viewIdsToShow.length; viewIdIndex++) {
        String visibleId = visibleViewRefs[visibleViewRefIndex].getId();
        if (visibleId.equals(viewIdsToShow[viewIdIndex])) {
          viewIdsToShow[viewIdIndex] = null;
          found = true;
          break;
        }
      }
      if (!found) {
        page.hideView(visibleViewRefs[visibleViewRefIndex]);
        changeOccurred = true;
      }
    }

    // For those remaining make them visible
    for (String viewId : viewIdsToShow) {
      if (viewId != null && !viewId.equalsIgnoreCase("")) {
        try {
          page.showView(viewId);
          changeOccurred = true;
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      }
    }

    if (changeOccurred)  {
      saveViews(page, openPriorToChange, BACKUP_LIST_ID);
    }
  }

  private void saveViews(IWorkbenchPage page, String delimList, String instanceId) {
    InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).put(instanceId, delimList);
  }

  private String getOpenViews(IWorkbenchPage page) {
    IViewReference[] a = page.getViewReferences();
    String view_formatter_list = "";
    for (IViewReference b : a) {
      view_formatter_list += b.getId() + LIST_DELIMITER;
    }
    return view_formatter_list;
  }
}
