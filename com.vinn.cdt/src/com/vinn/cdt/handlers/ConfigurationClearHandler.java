package com.vinn.cdt.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

import com.vinn.cdt.ConfigurationActiveState;
import com.vinn.cdt.build.ConfigurationManager;

public class ConfigurationClearHandler extends AbstractHandler  {

  public ConfigurationClearHandler() {
  }
  
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    // perform login here ...
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
    ISourceProviderService service = (ISourceProviderService) window.getService(ISourceProviderService.class);
    ConfigurationActiveState sessionSourceProvider = (ConfigurationActiveState) service.getSourceProvider(ConfigurationActiveState.STATE);
    // update the source provider
    ConfigurationManager.getInstance().remove();
    sessionSourceProvider.deactivate();
    return null;
  }
}
