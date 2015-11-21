/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.conf.providers;

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
  private static ConfigurationActiveState instance;
  public final static String STATE = "com.vinn.cdt.conf.active"; //$NON-NLS-1$
  
  public static ConfigurationActiveState getInstance() {
    if (instance == null) {
      synchronized (ConfigurationActiveState.class) {
        if (instance == null) {
          instance = new ConfigurationActiveState();
        }
      }
    }
    return instance;
  }

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
  
  public void setIsActive(boolean state) {
    IEclipsePreferences p = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
    p.putBoolean("configurationActive", state);
    try {
      p.flush();
    } catch (BackingStoreException e) {
      e.printStackTrace();
    }
    fCachedState = state;
    if (fCachedState) 
      getInstance().fireSourceChanged(ISources.WORKBENCH, STATE, "TRUE"); //$NON-NLS-1$
    else 
      getInstance().fireSourceChanged(ISources.WORKBENCH, STATE, "FALSE"); //$NON-NLS-1$
  }

  public boolean getIsActive() {
    if (fCachedState == null) {
      IEclipsePreferences p = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
      fCachedState = p.getBoolean("configurationActive", false);
    }
    return fCachedState.booleanValue();
  }
}
