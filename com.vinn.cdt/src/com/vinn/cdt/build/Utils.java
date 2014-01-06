package com.vinn.cdt.build;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.cdt.preferences.VinnBuildPreferenceConstants;
import com.vinn.cdt.ui.ConfContributionItem;

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
  
  public static void recursiveFind(ArrayList<IResource> allMatchingResources, IPath path,
	      IWorkspaceRoot myWorkspaceRoot, Pattern pattern, int depth, boolean includeFiltered) {

	    if (depth == 0 || allMatchingResources == null) {
	      return;
	    }
	    
	    IResourceFilterDescription[] tempFilters = null;
	    IContainer rootContainer = null;
	    IContainer container = null;
	    
	    try {      
	      container = myWorkspaceRoot.getContainerForLocation(path);
	      
	      if (includeFiltered) {
	        // Supports filter only where we put it (on the root / project)
	        rootContainer = container;
	        while (rootContainer.getType() != IResource.PROJECT) {
	          rootContainer = rootContainer.getParent();
	        }
	        tempFilters = rootContainer.getFilters();
	        for (IResourceFilterDescription i : tempFilters) {
	          i.delete(IResource.FORCE, null);          
	        }
	      }
	      
	      IResource[] iResources = container.members();
	      for (IResource iR : iResources) {
	        String pathAsString = iR.getProjectRelativePath().toString();
	        Matcher m = pattern.matcher(pathAsString);
	        if (m.find()) {
	          allMatchingResources.add(iR);
	        }
	        if (iR.getType() == IResource.FOLDER) {
	          IPath tempPath = iR.getLocation();
	          recursiveFind(allMatchingResources, tempPath, myWorkspaceRoot, pattern, (depth - 1), includeFiltered);
	        }
	      }
	    } catch (CoreException e) {
	      e.printStackTrace();
	    }
	    finally {
	      if (tempFilters != null && rootContainer != null) {
	          for (IResourceFilterDescription i : tempFilters) {
	            try {
	              rootContainer.createFilter(i.getType(), i.getFileInfoMatcherDescription(),
	                IResource.BACKGROUND_REFRESH, null);
	            } catch (CoreException e) {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	            }
	          }
	      }
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

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (CoreException e) {
		e.printStackTrace();
	}
	
	return text;
  }
  
	public static void GetConfs(ArrayList<IResource> allConfFiles) {

		// Find Environment *.confs the parameter is the IResource
		// this is used later to extract the presentation name as well
		// as then use the IResource to parse the connect if selected

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		// Find project and base location to search for configurations

		IPreferenceStore p = Activator.getDefault().getPreferenceStore();
		final String projectName = p.getString(
				VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_NAME).trim();
		final String projectPath = p.getString(
				VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_PATH).trim();

		IProject productProject = workspaceRoot.getProject(projectName);
		IPath path = productProject.getLocation().append(projectPath);

		boolean pathExists = workspaceRoot.getContainerForLocation(path)
				.exists();
		if (!productProject.isAccessible() || !pathExists) {
			return;
		}

		// Configuration identification

		final String confSelector = p.getString(
				VinnBuildPreferenceConstants.P_STRING_CONF_SELECTOR).trim();
		final Pattern confSelectorPattern = java.util.regex.Pattern.compile(
				confSelector, Pattern.CASE_INSENSITIVE);

		Utils.recursiveFind(allConfFiles, path, workspaceRoot,
				confSelectorPattern, 3, true);
		return;
	}
}
