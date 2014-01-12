package com.vinn.utils.font.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.utils.Activator;

public class VinnFontUtilsPreferenceInitializer extends AbstractPreferenceInitializer {

  public VinnFontUtilsPreferenceInitializer() {}

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setDefault(VinnFontUtilsPreferenceConstants.P_STRING_FONT_STEP, 1);
    store.setDefault(VinnFontUtilsPreferenceConstants.P_STRING_FONT_RESETSIZE, 11);
  }
}
