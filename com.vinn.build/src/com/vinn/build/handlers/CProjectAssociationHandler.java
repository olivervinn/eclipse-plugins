/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.build.handlers;

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

import com.vinn.build.Activator;
import com.vinn.build.preferences.PreferenceConstants;

public class CProjectAssociationHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
    ICProject[] cProjects = getCProjects();
    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    boolean isUnlink = event.getCommand().getId().endsWith("unlink");
    boolean continueWithLink = p.getBoolean(PreferenceConstants.P_HIDE_LINK_CONFIRMATION);

    if (!continueWithLink) {
      MessageDialogWithToggle userConfirmation =
          MessageDialogWithToggle.openOkCancelConfirm(window.getShell(),
              "Confirm Project Association", "Found " + cProjects.length
                  + " projects to dynamically " + (isUnlink ? "unlink" : "link") + ". Continue?",
              "Don't prompt me again", false, null, null);

      continueWithLink = userConfirmation.getReturnCode() == MessageDialogWithToggle.OK;
      p.setValue(PreferenceConstants.P_HIDE_LINK_CONFIRMATION, userConfirmation.getToggleState());
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

  private ICProject[] getCProjects() {

    try {
      ICProject[] projects = CoreModel.getDefault().getCModel().getCProjects();
      ArrayList<ICProject> cProjects = new ArrayList<ICProject>(projects.length);

      for (ICProject iProject : projects) {
        if (iProject.getProject().isOpen()
            && !isProjectNameExcluded(iProject.getProject().getName())) {
          cProjects.add(iProject);
        }
      }

      ICProject[] cProjectsA = new ICProject[cProjects.size()];
      cProjectsA = cProjects.toArray(cProjectsA);
      return cProjectsA;

    } catch (CModelException e1) {
      e1.printStackTrace();
      return null;
    }
  }

  private boolean isProjectNameExcluded(String name) {
    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    final String[] excludedEndings =
        p.getString(PreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS).split(";");
    for (String excludedEnding : excludedEndings) {
      if (name.endsWith(excludedEnding)) return true;
    }

    return false;
  }

}
