package com.vinn.build.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.build.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences
   * ()
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setDefault(PreferenceConstants.P_HIDE_LINK_CONFIRMATION, false);
    store.setDefault(PreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS, "build;.doc;.test;");
    store.setDefault(PreferenceConstants.P_STRING_CONF_SELECTOR_REGEX, "/project/build/conf");
    store.setDefault(PreferenceConstants.P_STRING_CONF_DEFINE_REGEX, "conf");
  }

}
