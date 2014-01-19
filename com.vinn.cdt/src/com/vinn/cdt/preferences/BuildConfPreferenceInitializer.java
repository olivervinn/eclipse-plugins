/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.cdt.Activator;

public class BuildConfPreferenceInitializer extends AbstractPreferenceInitializer {

  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setDefault(BuildConfPreferenceConstants.P_HIDE_LINK_CONFIRMATION, false);
    store.setDefault(BuildConfPreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS, ".doc;.test;"); //$NON-NLS-1$
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_PROJECT_NAME, "build"); //$NON-NLS-1$
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_PROJECT_PATH, "out/"); //$NON-NLS-1$
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_PROJECT_DEPTH, 3);
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_SELECTOR, "^\\w+/\\w+/\\w+$"); //$NON-NLS-1$
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR,
        ".*_opt_cc.txt$"); //$NON-NLS-1$
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR,
        "-D(\\w+)[=]{0,1}[\\\"]{0,1}(.*?)[\\\"]{0,1}\\s+"); //$NON-NLS-1$
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR,
        ".*_bfolders.txt$"); //$NON-NLS-1$
    store.setDefault(BuildConfPreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR,
        "-../(\\w+)/([^\\s\\n]+)"); //$NON-NLS-1$
  }

}
