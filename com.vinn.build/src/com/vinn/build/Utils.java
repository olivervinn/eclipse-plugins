package com.vinn.build;

import java.util.ArrayList;

import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.build.cdt.preferences.VinnBuildPreferenceConstants;

public class Utils {

  public static ICProject[] getCProjects() {

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
  
  public static boolean isProjectNameExcluded(String name) {
    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    final String[] excludedEndings =
        p.getString(VinnBuildPreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS).split(";");
    for (String excludedEnding : excludedEndings) {
      if (name.endsWith(excludedEnding)) return true;
    }

    return false;
  }
}
