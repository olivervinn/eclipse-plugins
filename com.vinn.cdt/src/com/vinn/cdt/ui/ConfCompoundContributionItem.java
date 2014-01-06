package com.vinn.cdt.ui;

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

import com.vinn.cdt.build.Activator;
import com.vinn.cdt.build.Utils;
import com.vinn.cdt.preferences.VinnBuildPreferenceConstants;

public class ConfCompoundContributionItem extends CompoundContributionItem
    implements
      IWorkbenchContribution {

  public ConfCompoundContributionItem() {}

  public ConfCompoundContributionItem(final String id) {
    super(id);
  }

  @Override
  protected IContributionItem[] getContributionItems() {

	  
    ArrayList<IResource> allConfFiles = new ArrayList<IResource>();
	Utils.GetConfs(allConfFiles);	

    IContributionItem[] items = new IContributionItem[allConfFiles.size()];
    for (int i = 0; i < allConfFiles.size(); i++) {
      IResource iResource = allConfFiles.get(i);
      items[i] = new ConfContributionItem(null, iResource);
    }
    return items.length > 0
        ? items
        : new IContributionItem[] {new ConfContributionItem(null, null)};
  }

  @Override
  public void initialize(final IServiceLocator serviceLocator) {}

  @Override
  public boolean isDirty() {
    return true;
  }
}
