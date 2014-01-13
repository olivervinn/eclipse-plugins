package com.vinn.cdt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

import com.vinn.cdt.build.ConfigurationManager;

public class ConfigurationActiveState extends AbstractSourceProvider {

  public final static String STATE = "com.vinn.cdt.active";

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] {STATE};
  }

  @Override
  public Map<String, String> getCurrentState() {
    Map<String, String> currentState = new HashMap<String, String>(1);
    boolean state = ConfigurationManager.getInstance().getIsActive();
    currentState.put(STATE, state ? "TRUE":"FALSE");
    System.out.println("State reported as " + state);
    return currentState;
  }

  @Override
  public void dispose() {}

  public void deactivate() {
    fireSourceChanged(ISources.WORKBENCH, STATE, "FALSE");
  }

  public void activate() {
    fireSourceChanged(ISources.WORKBENCH, STATE, "TRUE");
  }
}
