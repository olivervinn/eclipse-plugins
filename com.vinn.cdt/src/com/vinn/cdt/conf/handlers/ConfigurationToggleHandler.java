package com.vinn.cdt.conf.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.UIElement;

import com.vinn.cdt.Activator;
import com.vinn.cdt.conf.providers.ConfigurationActiveState;

public class ConfigurationToggleHandler extends AbstractHandler implements IElementUpdater {

  public static final String ID = "com.vinn.cdt.conf.toggle"; //$NON-NLS-1$

  public static ImageDescriptor fEnableIcon;
  public static ImageDescriptor fDisableIcon;

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
    IHandlerService handlerService =
        (IHandlerService) window.getWorkbench().getService(IHandlerService.class);

    try {
      if (!ConfigurationActiveState.getInstance().getIsActive()) {
        handlerService.executeCommand(ConfigurationSelectionHandler.ID, null);
      } else {
        handlerService.executeCommand(ConfigurationClearHandler.ID, null);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void updateElement(UIElement element, Map parameters) {

    if (ConfigurationActiveState.getInstance().getIsActive()) {
      if (fEnableIcon == null) fEnableIcon = Activator.getImageDescriptor("icons/active.gif");
      element.setIcon(fEnableIcon);
      element.setTooltip("Clear Configuration");
    } else {
      if (fDisableIcon == null) fDisableIcon = Activator.getImageDescriptor("icons/dactive.gif");
      element.setIcon(fDisableIcon);
      element.setTooltip("Select Configuration");
    }
  }
}
