package com.vinn.ui.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;

public class VinnUtilsActivityState extends AbstractSourceProvider {

  public final static String BASIC_STATE_ID = "com.vinn.ui.utils.filterbasic.active";
  public final static String OTHER_STATE_ID = "com.vinn.ui.utils.filterother.active";

  public final static String ENABLED = "ENABLED";
  public final static String DISABLED = "DISABLED";

  private boolean basicEnabled = true;
  private boolean otherEnabled = true;


  @Override
  public void dispose() {}

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] {BASIC_STATE_ID, OTHER_STATE_ID};
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Map getCurrentState() {
    Map<String, String> map = new HashMap<String, String>(2);
    map.put(BASIC_STATE_ID, (basicEnabled ? ENABLED : DISABLED));
    map.put(OTHER_STATE_ID, (otherEnabled ? ENABLED : DISABLED));
    return map;
  }



  // This method can be used from other commands to change the state
  // Most likely you would use a setter to define directly the state and not use this toogle method
  // But hey, this works well for my example
  public void toogleEnabled() {
    /*
     * enabled = !enabled ; String value = enabled ? ENABLED : DISENABLED;
     * fireSourceChanged(ISources.WORKBENCH, MY_STATE, value);
     */
  }

}
