/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.handlers;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import com.vinn.cdt.Activator;
import com.vinn.cdt.Utils;
import com.vinn.cdt.preferences.PreferenceConstants;

public class ProjectAssociationHandler extends AbstractHandler {

  public static final String ID = "com.vinn.cdt.linkprojects"; //$NON-NLS-1$

  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {

    Display.getDefault().asyncExec(new Runnable() {
      @Override
      public void run() {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        ICProject[] cProjects = Utils.getCProjects();
        IPreferenceStore p = Activator.getDefault().getPreferenceStore();
        boolean isUnlink = event.getCommand().getId().endsWith("unlink"); //$NON-NLS-1$
        boolean automaticLinking = p.getBoolean(PreferenceConstants.P_AUTO_LINK_PROJECTS);
        boolean linkWithoutConfirm = p.getBoolean(PreferenceConstants.P_HIDE_LINK_CONFIRMATION);
        if (!automaticLinking && !linkWithoutConfirm) {
          MessageDialogWithToggle userConfirmation = MessageDialogWithToggle.openOkCancelConfirm(window.getShell(),
              "Confirm Project Association", String.format(
                  "Found %d CDT projects. Do you want to continue to %s these projects?", cProjects.length,
                  (isUnlink ? "unlink" : "link")), "Don't prompt me again", false, null, null);

          linkWithoutConfirm = userConfirmation.getReturnCode() == MessageDialogWithToggle.OK;
          p.setValue(PreferenceConstants.P_HIDE_LINK_CONFIRMATION, userConfirmation.getToggleState());
        }
        if (automaticLinking || linkWithoutConfirm) {
          linkUnlinkProjects(isUnlink, cProjects);
        }
      }
    });
    return null;
  }

  private void linkUnlinkProjects(boolean unlink, ICProject[] cProjectsA) {
    String t = ""; //$NON-NLS-1$
    IProject[] cProjectsB = new IProject[cProjectsA.length - 1];
    for (int i = 0; i < cProjectsA.length; i++) {
      IProject masterCProj = cProjectsA[i].getProject();
            // addProvider(masterCProj);
      if (!unlink) {
        int indx = 0;
        for (int j = 0; j < cProjectsA.length; j++) {
          if (i == j) {
            continue;
        } else {
            cProjectsB[indx++] = cProjectsA[j].getProject();
        }
        }
      } else {
        cProjectsB = new IProject[0];
      }
      try {
        IProjectDescription description = masterCProj.getDescription();
        description.setDynamicReferences(cProjectsB);
        masterCProj.setDescription(description, null);
      } catch (CoreException e) {
        e.printStackTrace();
      }
      t += masterCProj.getName() + " "; //$NON-NLS-1$
    }
    // Now we freshen the Indexer so it can resolve across projects.
    for (ICProject element : cProjectsA) {
      CCorePlugin.getIndexManager().reindex(element);
    }
    StatusManager.getManager().handle(
        new Status(IStatus.INFO, Activator.PLUGIN_ID, String.format("Associated projects (%s)", t)));
  }
}
