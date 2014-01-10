package com.vinn.cdt.build;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.cdt.preferences.VinnBuildPreferenceConstants;

public class Utils {
  
  
  public static void rebuildCIndex() {
    ICProject[] cProjects = Utils.getCProjects();
    for (int i = 0; i < cProjects.length; i++) {
      CCorePlugin.getIndexManager().reindex(cProjects[i]);
    }
  }

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
  

  public static void FindResourceInTree(List<IResource> matchingResources, IPath path,
	      IWorkspaceRoot myWorkspaceRoot, Pattern pattern, int depth) {

	    if (depth == 0) {
	      return;
	    }

	    IContainer container = null;

	    try {
	      container = myWorkspaceRoot.getContainerForLocation(path);
	      if (container == null)
	        return;

	      IResource[] iResources = container.members();
	      for (IResource iR : iResources) {
	        String pathAsString = iR.getProjectRelativePath().toString();
	        Matcher m = pattern.matcher(pathAsString);
	        if (m.find()) {
	          matchingResources.add(iR);
	        }
	        if (iR.getType() == IResource.FOLDER) {
	          IPath tempPath = iR.getLocation();
	          FindResourceInTree(matchingResources, tempPath, myWorkspaceRoot, pattern, (depth - 1));
	        }
	      }
	    } catch (CoreException e) {
	      e.printStackTrace();
	    }
	    finally {
	    }
	  }
  
  public static String readTextFile(IFile file) {
	// Open file and parse out the defines e.g. -DOPTION=value
	String text = "";
	BufferedReader br;
	try {
		br = new BufferedReader(new InputStreamReader(file.getContents()));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			sb.append('\n');
			line = br.readLine();
		}
		sb.append('\n'); // Always end with a new line so expressions are
							// not fooled
		br.close();
		text = sb.toString();

	} catch (Exception e) {
	  text = "";
	}
	
	return text;
  }
  
	
}
