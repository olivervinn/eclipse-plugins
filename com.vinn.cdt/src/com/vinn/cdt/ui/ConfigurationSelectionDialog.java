package com.vinn.cdt.ui;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import com.vinn.cdt.build.Activator;
import com.vinn.cdt.build.ConfigurationManager;
import com.vinn.cdt.build.ConfigurationManager.ConfigurationEntity;

public class ConfigurationSelectionDialog extends FilteredItemsSelectionDialog {

  public ConfigurationSelectionDialog(Shell shell) {
    super(shell, false);
    setTitle("Select Configuration");
    setInitialPattern("**");
    setListLabelProvider(new ILabelProvider() {

      @Override
      public void removeListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub

      }

      @Override
      public boolean isLabelProperty(Object element, String property) {
        return true;
      }

      @Override
      public void dispose() {
        // TODO Auto-generated method stub

      }

      @Override
      public void addListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub

      }

      @Override
      public String getText(Object element) {
        if (element != null)
          return ((ConfigurationEntity) element).getConfRoot().getProjectRelativePath().toString();
        return "";
      }

      @Override
      public Image getImage(Object element) {
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD);
        // return null;
      }
    });

    setDetailsLabelProvider(new ILabelProvider() {

      @Override
      public void removeListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub

      }

      @Override
      public boolean isLabelProperty(Object element, String property) {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public void dispose() {
        // TODO Auto-generated method stub

      }

      @Override
      public void addListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub

      }

      @Override
      public String getText(Object element) {
        if (element == null) return null;
        ConfigurationEntity entity = (ConfigurationEntity) element;
        IResource r1 = entity.getDefinesFile();
        IResource r2 = entity.getFilterFile();
        return String.format("Define (%s) - Filter (%s)",
            (r1 == null ? "Not found" : r1.getName()), (r2 == null ? "Not found" : r2.getName()));
      }

      @Override
      public Image getImage(Object element) {
        // TODO Auto-generated method stub
        return null;
      }
    });
  }

  @Override
  protected Control createExtendedContentArea(Composite parent) {
    return null;
  }

  private static final String SETTINGS = ConfigurationSelectionDialog.class.getCanonicalName();

  @Override
  protected IDialogSettings getDialogSettings() {
    IDialogSettings settings = Activator.getDefault().getDialogSettings().getSection(SETTINGS);
    if (settings == null) {
      settings = Activator.getDefault().getDialogSettings().addNewSection(SETTINGS);
    }
    return settings;
  }

  @Override
  protected IStatus validateItem(Object item) {
    return new Status(IStatus.OK, Activator.PLUGIN_ID, ""); //$NON-NLS-1$
  }

  @Override
  protected ItemsFilter createFilter() {
    return new ConfigurationFilter() {};
  }

  @Override
  protected Comparator<ConfigurationEntity> getItemsComparator() {
    Comparator<ConfigurationEntity> comp = new Comparator<ConfigurationEntity>() {

      public int compare(ConfigurationEntity o1, ConfigurationEntity o2) {
        return o1.getConfRoot().toString().compareTo(o2.getConfRoot().toString());
      }
    };
    return comp;
  }

  @Override
  protected void fillContentProvider(AbstractContentProvider contentProvider,
      ItemsFilter itemsFilter, IProgressMonitor progressMonitor) throws CoreException {
    if (progressMonitor != null) {
      progressMonitor.beginTask("Finding configurations", IProgressMonitor.UNKNOWN);
    }

    List<ConfigurationEntity> allConfFiles =
        ConfigurationManager.getInstance().getConfigurationResources();

    for (int i = 0; i < allConfFiles.size(); i++) {
      ConfigurationEntity iResource = allConfFiles.get(i);
      contentProvider.add(iResource, itemsFilter);
    }
    if (progressMonitor != null) {
      progressMonitor.done();
    }
  }

  @Override
  public String getElementName(Object item) {
    if (!(item instanceof ConfigurationEntity)) {
      return null;
    }
    String r = ((ConfigurationEntity) item).getConfRoot().getProjectRelativePath().toString();
    return r;
  }

  /**
   * Filter for extension points.
   */
  private class ConfigurationFilter extends ItemsFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConsistentItem(Object item) {
      if (item instanceof ConfigurationEntity) {
        return true;
      }
      return false;
    }

    /**
     * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter#matchItem(java.lang.Object)
     * 
     * @param item
     * @return
     */

    @Override
    public boolean matchItem(Object item) {
      if ((item instanceof ConfigurationEntity)) {
        return super.matches(((ConfigurationEntity) item).getConfRoot().getProjectRelativePath()
            .toString());
      }
      return false;
    }
  }
}
