package com.vinn.cdt.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.vinn.cdt.build.ConfigurationManager;

public class ConfigurationClearHandler extends AbstractHandler {

  public static final String CONFIGURATION_ID = "com.vinn.build.dynamiccconfig";
  public static final String CONFIGURATION_NAME = "[Vinn Dynamic Configuration]";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    ConfigurationManager.getInstance().remove();

    return null;
  }
}
