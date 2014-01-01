package com.vinn.build.ui;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
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
import com.vinn.build.preferences.PreferenceConstants;

public class ConfCompoundContributionItem extends CompoundContributionItem
    implements
      IWorkbenchContribution {

  private long mLastTimeStamp = 0;

  public ConfCompoundContributionItem() {}

  public ConfCompoundContributionItem(final String id) {
    super(id);
  }

  @Override
  protected IContributionItem[] getContributionItems() {
    mLastTimeStamp = System.currentTimeMillis();

    // Find Environment *.confs the parameter is the IResource
    // this is used later to extract the presentation name as well
    // as then use the IResource to parse the connect if selected

    ArrayList<IResource> allConfFiles = new ArrayList<IResource>();
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    IProject productProject = workspaceRoot.getProject("product");
    IPath path = productProject.getLocation().append("build");

    boolean pathExists = workspaceRoot.getContainerForLocation(path).exists();

    if (!productProject.isAccessible() || !pathExists) {
      return new IContributionItem[] {new ConfContributionItem(null, null)};
    }

    recursiveFindCFiles(allConfFiles, path, workspaceRoot);

    IContributionItem[] items = new IContributionItem[allConfFiles.size()];
    for (int i = 0; i < allConfFiles.size(); i++) {
      IResource iResource = allConfFiles.get(i);
      items[i] = new ConfContributionItem(null, iResource);
    }
    return items.length > 0
        ? items
        : new IContributionItem[] {new ConfContributionItem(null, null)};
  }

  private static void recursiveFindCFiles(ArrayList<IResource> allCFiles, IPath path,
      IWorkspaceRoot myWorkspaceRoot) {

    IContainer container = myWorkspaceRoot.getContainerForLocation(path);
    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    final String extension = p.getString(PreferenceConstants.P_STRING_CONF_EXTENSION).trim();

    try {
      IResource[] iResources;
      iResources = container.members();
      for (IResource iR : iResources) {
        if (extension.equalsIgnoreCase(iR.getFileExtension())) allCFiles.add(iR);
        if (iR.getType() == IResource.FOLDER) {
          IPath tempPath = iR.getLocation();
          recursiveFindCFiles(allCFiles, tempPath, myWorkspaceRoot);
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(final IServiceLocator serviceLocator) {}

  @Override
  public boolean isDirty() {
    return mLastTimeStamp + 5000 < System.currentTimeMillis();
  }
}
