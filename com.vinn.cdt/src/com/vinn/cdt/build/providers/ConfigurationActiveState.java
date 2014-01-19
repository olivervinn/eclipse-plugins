/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.build.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.osgi.service.prefs.BackingStoreException;

import com.vinn.cdt.Activator;

public class ConfigurationActiveState extends AbstractSourceProvider {

  private static Boolean fCachedState;
  public final static String STATE = "com.vinn.cdt.build.active"; //$NON-NLS-1$

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] {STATE};
  }

  @Override
  public Map<String, String> getCurrentState() {
    Map<String, String> currentState = new HashMap<String, String>(1);
    boolean state = getIsActive();
    currentState.put(STATE, state ? "TRUE":"FALSE"); //$NON-NLS-1$
    return currentState;
  }

  @Override
  public void dispose() {}

  public void deactivate() {
    fireSourceChanged(ISources.WORKBENCH, STATE, "FALSE"); //$NON-NLS-1$
  }

  public void activate() {
    fireSourceChanged(ISources.WORKBENCH, STATE, "TRUE"); //$NON-NLS-1$
  }
  
  public static void setIsActive(boolean state) {
    IEclipsePreferences p = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
    p.putBoolean("configurationActive", state);
    try {
      p.flush();
    } catch (BackingStoreException e) {
      e.printStackTrace();
    }
    fCachedState = state;
  }

  public static boolean getIsActive() {
    if (fCachedState == null) {
      IEclipsePreferences p = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
      fCachedState = p.getBoolean("configurationActive", false);
    }
    return fCachedState.booleanValue();
  }
}
