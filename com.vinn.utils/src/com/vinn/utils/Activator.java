/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "com.vinn.utils"; //$NON-NLS-1$

  // The shared instance
  private static Activator plugin;

  /**
   * The constructor
   */
  public Activator() {}

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
    
    // Doesn't seem possible to track toggle state. Can get the command but not the state?
    /*
    IWorkbench workbench = PlatformUI.getWorkbench();
    ICommandService service =(ICommandService) workbench.getService(ICommandService.class);
    Command command = service.getCommand("org.eclipse.ui.edit.text.toggleShowWhitespaceCharacters");
    command.addCommandListener(new ICommandListener() {
      
      @SuppressWarnings("restriction")
      @Override
      public void commandChanged(CommandEvent commandEvent) {
      
        IWorkbenchWindow window = Workbench.getInstance().getActiveWorkbenchWindow();
        if(window instanceof WorkbenchWindow) {
            MenuManager menuManager = ((WorkbenchWindow)window).getMenuManager();
            Menu menu = menuManager.getMenu();
            IContributionItem item = menuManager.findUsingPath("com.vinn.utils.tools.menu/com.vinn.utils.editor.showwhitespace");
            if (item instanceof ContributionItem) {
              System.out.println(item.isEnabled());
            }
        }
      }
    });
    */
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
   */
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

}
