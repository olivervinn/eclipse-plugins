package com.vinn.build.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;

import com.vinn.build.Activator;
import com.vinn.build.cdt.preferences.VinnBuildPreferenceConstants;

public class ConfCompoundContributionItem extends CompoundContributionItem
    implements
      IWorkbenchContribution {

  public ConfCompoundContributionItem() {}

  public ConfCompoundContributionItem(final String id) {
    super(id);
  }

  @Override
  protected IContributionItem[] getContributionItems() {

    // Find Environment *.confs the parameter is the IResource
    // this is used later to extract the presentation name as well
    // as then use the IResource to parse the connect if selected

    ArrayList<IResource> allConfFiles = new ArrayList<IResource>();
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

    // Find project and base location to search for configurations

    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    final String projectName =
        p.getString(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_NAME).trim();
    final String projectPath =
        p.getString(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_PATH).trim();

    IProject productProject = workspaceRoot.getProject(projectName);
    IPath path = productProject.getLocation().append(projectPath);

    boolean pathExists = workspaceRoot.getContainerForLocation(path).exists();
    if (!productProject.isAccessible() || !pathExists) {
      return new IContributionItem[] {new ConfContributionItem(null, null)};
    }

    // Configuration identification

    final String confSelector =
        p.getString(VinnBuildPreferenceConstants.P_STRING_CONF_SELECTOR).trim();
    final Pattern confSelectorPattern =
        java.util.regex.Pattern.compile(confSelector, Pattern.CASE_INSENSITIVE);

    recursiveFind(allConfFiles, path, workspaceRoot, confSelectorPattern, 3, true);

    IContributionItem[] items = new IContributionItem[allConfFiles.size()];
    for (int i = 0; i < allConfFiles.size(); i++) {
      IResource iResource = allConfFiles.get(i);
      items[i] = new ConfContributionItem(null, iResource);
    }
    return items.length > 0
        ? items
        : new IContributionItem[] {new ConfContributionItem(null, null)};
  }

  private static void recursiveFind(ArrayList<IResource> allMatchingResources, IPath path,
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

  @Override
  public void initialize(final IServiceLocator serviceLocator) {}

  @Override
  public boolean isDirty() {
    return true;
  }
}
