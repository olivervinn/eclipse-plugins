/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.conf.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.cdt.conf.ConfigurationManager;
import com.vinn.cdt.conf.providers.ConfigurationActiveState;
import com.vinn.cdt.handlers.IndexerCancelHandler;

public class ConfigurationClearHandler extends AbstractHandler {

  public static final String ID = "com.vinn.cdt.conf.clear"; //$NON-NLS-1$

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);

    // Cancel the indexer in case its in-progress.
    new IndexerCancelHandler().execute(event);
    // Remove config
    ConfigurationManager.getInstance().remove();
    // Update state
    ConfigurationActiveState.getInstance().setIsActive(false);

    ICommandService commandService = (ICommandService) window.getService(ICommandService.class);
    if (commandService != null) {
      commandService.refreshElements(ConfigurationToggleHandler.ID, null);
    }

    return null;
  }
}
