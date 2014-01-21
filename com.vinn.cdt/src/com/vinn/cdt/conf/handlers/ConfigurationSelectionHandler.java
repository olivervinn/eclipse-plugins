/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.conf.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.cdt.Utils;
import com.vinn.cdt.conf.ConfigurationManager;
import com.vinn.cdt.conf.ConfigurationManager.ConfigurationEntity;
import com.vinn.cdt.conf.providers.ConfigurationActiveState;
import com.vinn.cdt.conf.ui.ConfigurationSelectionDialog;

public class ConfigurationSelectionHandler extends AbstractHandler {

  public static final String ID = "com.vinn.cdt.conf.select"; //$NON-NLS-1$

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    final ConfigurationSelectionDialog dialog = new ConfigurationSelectionDialog(shell);
    dialog.open();

    if (Dialog.OK == dialog.getReturnCode()) {

      final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

      final ICommandService commandService =
          (ICommandService) window.getService(ICommandService.class);
      if (commandService != null) {
        
      }
      
      IRunnableWithProgress operation = new IRunnableWithProgress() {
        public void run(IProgressMonitor monitor) {
          Object[] profiles = dialog.getResult();
          ConfigurationManager.getInstance().apply((ConfigurationEntity) profiles[0]);
          ConfigurationActiveState.getInstance().setIsActive(true);
          commandService.refreshElements(ConfigurationToggleHandler.ID, null);
          Utils.rebuildCIndex();
        }
      };
      try {
        PlatformUI.getWorkbench().getProgressService().run(true, true, operation);
      } catch (InvocationTargetException e) {

      } catch (InterruptedException e) {}
    }

    return null;
  }
}
