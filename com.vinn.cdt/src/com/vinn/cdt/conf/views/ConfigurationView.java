package com.vinn.cdt.conf.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.core.runtime.IAdaptable;

import com.vinn.cdt.conf.ConfigurationManager;
import com.vinn.cdt.conf.ConfigurationManager.ConfigurationEntity;
import com.vinn.cdt.conf.providers.ConfigurationActiveState;


/**
 * This sample class demonstrates how to plug-in a new workbench view. The view shows data obtained
 * from the model. The sample creates a dummy model on the fly, but a real implementation would
 * connect to the model available either in this or another plug-in (e.g. the workspace). The view
 * is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be presented in the view. Each
 * view can present the same model objects using different labels and icons, if needed.
 * Alternatively, a single label provider can be shared between views in order to ensure that
 * objects of the same type are presented in the same way everywhere.
 * <p>
 */

public class ConfigurationView extends ViewPart implements IPropertyListener {

  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID = "com.vinn.cdt.views.ConfigurationView";

  private TreeViewer viewer;
  private DrillDownAdapter drillDownAdapter;
  private Action action1;
  private Action action2;
  private Action doubleClickAction;

  /*
   * The content provider class is responsible for providing objects to the view. It can wrap
   * existing objects in adapters or simply return objects as-is. These objects may be sensitive to
   * the current input of the view, or ignore it and always show the same content (like Task List,
   * for example).
   */

  class TreeObject implements IAdaptable {
    private String name;
    private TreeParent parent;

    public TreeObject(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setParent(TreeParent parent) {
      this.parent = parent;
    }

    public TreeParent getParent() {
      return parent;
    }

    public String toString() {
      return getName();
    }

    public Object getAdapter(Class key) {
      return null;
    }
  }

  class TreeParent extends TreeObject {
    private ArrayList children;

    public TreeParent(String name) {
      super(name);
      children = new ArrayList();
    }

    public void addChild(TreeObject child) {
      children.add(child);
      child.setParent(this);
    }

    public void removeChild(TreeObject child) {
      children.remove(child);
      child.setParent(null);
    }

    public TreeObject[] getChildren() {
      return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
    }

    public boolean hasChildren() {
      return children.size() > 0;
    }
  }

  public class Macro {
    private String name = "";
    private String value = "";

    public Macro(String name, String value) {
      this.name = name;
      this.value = value;

    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  public class Category {
    private String name;
    private int sort;
    private List<Macro> todos = new ArrayList<Macro>();

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getSort() {
      return sort;
    }

    public void setSort(int sort) {
      this.sort = sort;
    }

    public List<Macro> getTodos() {
      return todos;
    }
  }

  class Model {

    Map<String,String> value;

    @SuppressWarnings("unchecked")
    public Model(Object newInput) {
      if (newInput == null)
        this.value = null;
      else
        this.value = (Map<String,String>)newInput;
    }

    public List<Category> getCategories() {
      List<Category> categories = new ArrayList<Category>();
      Category macroCat = new Category();
      macroCat.setName("Macro");

      Category foldercat = new Category();
      foldercat.setName("Folders");

      categories.add(macroCat);
      categories.add(foldercat);

      if (this.value != null) {
        for (Entry<String,String> m : this.value.entrySet()) {
          Macro todo = new Macro(m.getKey(), m.getValue());
          macroCat.getTodos().add(todo);
        }
      }
      
      return categories;
    }
  }

  class ViewContentProvider implements ITreeContentProvider {

    private Model model;

    @Override
    public void dispose() {}

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      this.model = new Model(newInput);
    }

    @Override
    public Object[] getElements(Object inputElement) {
      return model.getCategories().toArray();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
      if (parentElement instanceof Category) {
        Category category = (Category) parentElement;
        return category.getTodos().toArray();
      }
      return null;
    }

    @Override
    public Object getParent(Object element) {
      return null;
    }

    @Override
    public boolean hasChildren(Object element) {
      if (element instanceof Category) {
        return true;
      }
      return false;
    }

  }
  class ViewLabelProvider extends LabelProvider {

    // private static final Image FOLDER = getImage("folder.gif");
    // private static final Image FILE = getImage("file.gif");

    @Override
    public String getText(Object element) {
      if (element instanceof Category) {
        Category category = (Category) element;
        return category.getName();
      }
      return ((Macro) element).getName() + " = " + ((Macro) element).getValue();
    }

    public Image getImage(Object obj) {
      String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
      if (obj instanceof Category) imageKey = ISharedImages.IMG_OBJ_FOLDER;
      return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }

    // @Override
    // public Image getImage(Object element) {
    // if (element instanceof Category) {
    // return FOLDER;
    // }
    // return FILE;
    // }
    //
    // // Helper Method to load the images
    // private static Image getImage(String file) {
    // Bundle bundle = FrameworkUtil.getBundle(TodoLabelProvider.class);
    // URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
    // ImageDescriptor image = ImageDescriptor.createFromURL(url);
    // return image.createImage();
    //
    // }

  }
  class NameSorter extends ViewerSorter {}

  /**
   * The constructor.
   */
  public ConfigurationView() {}

  /**
   * This is a callback that will allow us to create the viewer and initialize it.
   */
  public void createPartControl(Composite parent) {
    viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    drillDownAdapter = new DrillDownAdapter(viewer);
    viewer.setContentProvider(new ViewContentProvider());
    viewer.setLabelProvider(new ViewLabelProvider());
    viewer.setSorter(new NameSorter());
    ConfigurationEntity o = ConfigurationManager.getInstance().getActiveConfiguration();
    viewer.setInput(o == null?null:o.macroValues);
//    ConfigurationActiveState.getInstance().addSourceProviderListener(new ISourceProviderListener() {
//
//      @Override
//      public void sourceChanged(int sourcePriority, String sourceName, Object sourceValue) {
//        System.out.println(sourceValue);
//        ConfigurationEntity o = ConfigurationManager.getInstance().getActiveConfiguration();
//        viewer.getContentProvider().inputChanged(null, null, o == null?null:o.macroValues);
//        viewer.refresh();
//      }
//
//      @Override
//      public void sourceChanged(int sourcePriority, Map sourceValuesByName) {
//        // TODO Auto-generated method stub
//
//      }
//    });
    makeActions();
    hookContextMenu();
    hookDoubleClickAction();
    contributeToActionBars();
  }

  private void hookContextMenu() {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(IMenuManager manager) {
        ConfigurationView.this.fillContextMenu(manager);
      }
    });
    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  private void contributeToActionBars() {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

  private void fillLocalPullDown(IMenuManager manager) {
    manager.add(action1);
    manager.add(new Separator());
    manager.add(action2);
  }

  private void fillContextMenu(IMenuManager manager) {
    manager.add(action1);
    manager.add(action2);
    manager.add(new Separator());
    drillDownAdapter.addNavigationActions(manager);
    // Other plug-ins can contribute there actions here
    manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
  }

  private void fillLocalToolBar(IToolBarManager manager) {
    manager.add(action1);
    manager.add(action2);
    manager.add(new Separator());
    drillDownAdapter.addNavigationActions(manager);
  }

  private void makeActions() {
    action1 = new Action() {
      public void run() {
        showMessage("Action 1 executed");
      }
    };
    action1.setText("Action 1");
    action1.setToolTipText("Action 1 tooltip");
    action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
        .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

    action2 = new Action() {
      public void run() {
        showMessage("Action 2 executed");
      }
    };
    action2.setText("Action 2");
    action2.setToolTipText("Action 2 tooltip");
    action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
        .getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
    doubleClickAction = new Action() {
      public void run() {
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        showMessage("Double-click detected on " + obj.toString());
      }
    };
  }

  private void hookDoubleClickAction() {
    viewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent event) {
        doubleClickAction.run();
      }
    });
  }

  private void showMessage(String message) {
    MessageDialog.openInformation(viewer.getControl().getShell(), "Configuration View", message);
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  @Override
  public void propertyChanged(Object source, int propId) {
    viewer.refresh();
  }
}
