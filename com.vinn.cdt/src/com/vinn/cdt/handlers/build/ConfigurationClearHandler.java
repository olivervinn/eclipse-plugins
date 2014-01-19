/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.handlers.build;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.cdt.build.providers.ConfigurationActiveState;
import com.vinn.cdt.build.ConfigurationManager;

public class ConfigurationClearHandler extends AbstractHandler {

  public static final String ID = "com.vinn.cdt.build.conf.clear"; //$NON-NLS-1$

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

    ConfigurationManager.getInstance().remove();
    ConfigurationActiveState.setIsActive(false);
    
    ICommandService commandService = (ICommandService) window.getService(ICommandService.class);
    if (commandService != null) {
        commandService.refreshElements(ConfigurationToggleHandler.ID, null);
    }
    
    return null;
  }
}
