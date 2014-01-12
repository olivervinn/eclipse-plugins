package com.vinn.cdt.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.cdt.build.Activator;

/**
 * Class used to initialize default preference values.
 */
public class VinnBuildPreferenceInitializer extends AbstractPreferenceInitializer {

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences
   * ()
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setDefault(VinnBuildPreferenceConstants.P_HIDE_LINK_CONFIRMATION, false);
    store.setDefault(VinnBuildPreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS, ".doc;.test;");
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_NAME, "build");
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_PATH, "out/");
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_DEPTH, 3);
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_SELECTOR, "^\\w+/\\w+/\\w+$");
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR,
        ".*_opt_cc.txt$");
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR,
        "-D(\\w+)[=]{0,1}[\\\"]{0,1}(.*?)[\\\"]{0,1}\\s+");
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR,
        ".*_bfolders.txt$");
    store.setDefault(VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR,
        "-../(\\w+)/([^\\s\\n]+)");
  }

}
