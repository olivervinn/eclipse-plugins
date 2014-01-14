/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.handlers;

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
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

import com.vinn.cdt.ConfigurationActiveState;
import com.vinn.cdt.Utils;
import com.vinn.cdt.build.ConfigurationManager;
import com.vinn.cdt.build.ConfigurationManager.ConfigurationEntity;
import com.vinn.cdt.ui.ConfigurationSelectionDialog;

public class ConfigurationSelectionHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    final ConfigurationSelectionDialog dialog = new ConfigurationSelectionDialog(shell);
    dialog.open();

    if (Dialog.OK == dialog.getReturnCode()) {
      
   // perform login here ...
      IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
      ISourceProviderService service = (ISourceProviderService) window.getService(ISourceProviderService.class);
      final ConfigurationActiveState sessionSourceProvider = (ConfigurationActiveState) service.getSourceProvider(ConfigurationActiveState.STATE);
      sessionSourceProvider.activate();
      
      IRunnableWithProgress operation = new IRunnableWithProgress() {
        public void run(IProgressMonitor monitor) {
          Object[] profiles = dialog.getResult();
          ConfigurationManager.getInstance().apply((ConfigurationEntity) profiles[0]);
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
