/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.conf.ui;

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
import org.eclipse.ui.IMemento;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import com.vinn.cdt.Activator;
import com.vinn.cdt.conf.ConfigurationManager;
import com.vinn.cdt.conf.ConfigurationManager.ConfigurationEntity;

public class ConfigurationSelectionDialog extends FilteredItemsSelectionDialog {

  public static final String IMAGE_CONF_OBJ_PATH = "icons/conf_obj.gif"; //$NON-NLS-1$

  public ConfigurationSelectionDialog(Shell shell) {
    super(shell, false);
    setTitle("Select Configuration");
    setInitialPattern("**"); //$NON-NLS-1$
    setListLabelProvider(new ILabelProvider() {

      private Image fIcon;

      @Override
      public void removeListener(ILabelProviderListener listener) {}

      @Override
      public boolean isLabelProperty(Object element, String property) {
        return true;
      }

      @Override
      public void dispose() {}

      @Override
      public void addListener(ILabelProviderListener listener) {}

      @Override
      public String getText(Object element) {
        if (element != null)
          return ((ConfigurationEntity) element).getConfRoot().getProjectRelativePath().toString();
        return "";
      }

      @Override
      public Image getImage(Object element) {
        if (fIcon == null)
          fIcon = Activator.getImageDescriptor(IMAGE_CONF_OBJ_PATH).createImage();
          return fIcon;
      }
    });

    setDetailsLabelProvider(new ILabelProvider() {

      @Override
      public void removeListener(ILabelProviderListener listener) {}

      @Override
      public boolean isLabelProperty(Object element, String property) {
        return false;
      }

      @Override
      public void dispose() {}

      @Override
      public void addListener(ILabelProviderListener listener) {}

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
        return null;
      }
    });

    setSelectionHistory(new SelectionHistory() {

      @Override
      protected void storeItemToMemento(Object item, IMemento memento) {
        ConfigurationEntity ce = (ConfigurationEntity) item;
        String s = ce.getConfRoot().toString();
        memento.putString("resource", s); //$NON-NLS-1$
      }

      @Override
      protected Object restoreItemFromMemento(IMemento memento) {
        Object root = memento.getString("resource"); //$NON-NLS-1$
        ConfigurationEntity c = ConfigurationManager.getInstance().createEntity((String) root);
        return c;
      }
    });

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Control createExtendedContentArea(Composite parent) {
    return null;
  }

  private static final String SETTINGS = ConfigurationSelectionDialog.class.getCanonicalName();

  /**
   * {@inheritDoc}
   */
  @Override
  protected IDialogSettings getDialogSettings() {
    IDialogSettings settings = Activator.getDefault().getDialogSettings().getSection(SETTINGS);
    if (settings == null) {
      settings = Activator.getDefault().getDialogSettings().addNewSection(SETTINGS);
    }
    return settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IStatus validateItem(Object item) {
    return new Status(IStatus.OK, Activator.PLUGIN_ID, ""); //$NON-NLS-1$
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ItemsFilter createFilter() {
    return new ConfigurationFilter() {};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Comparator<ConfigurationEntity> getItemsComparator() {
    Comparator<ConfigurationEntity> comp = new Comparator<ConfigurationEntity>() {

      public int compare(ConfigurationEntity o1, ConfigurationEntity o2) {
        return o1.getConfRoot().toString().compareTo(o2.getConfRoot().toString());
      }
    };
    return comp;
  }

  /**
   * {@inheritDoc}
   */
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

  /**
  * {@inheritDoc}
  */
  @Override
  public String getElementName(Object item) {
    if (!(item instanceof ConfigurationEntity)) {
      return null;
    }
    String r = ((ConfigurationEntity) item).getConfRoot().getProjectRelativePath().toString();
    return r;
  }


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
     * {@inheritDoc}
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
