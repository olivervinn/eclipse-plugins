/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.utils.Activator;

public class VinnMenuPreferenceInitializer extends AbstractPreferenceInitializer {

  public VinnMenuPreferenceInitializer() {}

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setDefault(VinnMenuPreferenceConstants.P_STRING_HIDE_BASICS, true);
    store.setDefault(VinnMenuPreferenceConstants.P_STRING_HIDE_CDT_BUILD, true);
    store.setDefault(VinnMenuPreferenceConstants.P_STRING_HIDE_OTHERS, true);
  }
}
