/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.handlers;

import java.util.ArrayList;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.cdt.build.Activator;
import com.vinn.cdt.build.Utils;
import com.vinn.cdt.preferences.VinnBuildPreferenceConstants;

public class CProjectAssociationHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
    ICProject[] cProjects = Utils.getCProjects();
    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    boolean isUnlink = event.getCommand().getId().endsWith("unlink");
    boolean continueWithLink = p.getBoolean(VinnBuildPreferenceConstants.P_HIDE_LINK_CONFIRMATION);

    if (!continueWithLink) {
      String pNames = "";
      for (ICProject icProject : cProjects) {
        pNames += "\n    " + icProject.getProject().getName();
      }
      MessageDialogWithToggle userConfirmation =
          MessageDialogWithToggle.openOkCancelConfirm(window.getShell(),
              "Confirm Project Association", 
              String.format("Found %d projects. Continue to dynamically %s?\n%s", 
                cProjects.length, (isUnlink ? "unlink" : "link"), pNames),
              "Don't prompt me again", false, null, null);

      continueWithLink = userConfirmation.getReturnCode() == MessageDialogWithToggle.OK;
      p.setValue(VinnBuildPreferenceConstants.P_HIDE_LINK_CONFIRMATION, userConfirmation.getToggleState());
    }

    if (continueWithLink) {
      linkUnlinkProjects(isUnlink, cProjects);
    }

    return null;
  }

  private void linkUnlinkProjects(boolean unlink, ICProject[] cProjectsA) {

    IProject[] cProjectsB = new IProject[cProjectsA.length - 1];
    for (int i = 0; i < cProjectsA.length; i++) {
      IProject masterCProj = cProjectsA[i].getProject();

      if (!unlink) {
        int indx = 0;
        for (int j = 0; j < cProjectsA.length; j++) {
          if (i == j)
            continue;
          else
            cProjectsB[indx++] = cProjectsA[j].getProject();
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
    }

    // Now we freshen the Indexer so it can resolve across projects.
    for (int i = 0; i < cProjectsA.length; i++) {
      CCorePlugin.getIndexManager().reindex(cProjectsA[i]);
    }
  }

}
