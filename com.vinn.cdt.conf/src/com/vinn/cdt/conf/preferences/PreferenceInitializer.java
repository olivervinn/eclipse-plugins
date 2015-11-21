/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.conf.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.cdt.conf.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();

        store.setDefault(PreferenceConstants.P_STRING_CONF_PROJECT_NAME, "build"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_STRING_CONF_PROJECT_PATH, "out/"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_STRING_CONF_PROJECT_DEPTH, 3);
        store.setDefault(PreferenceConstants.P_STRING_CONF_SELECTOR, "^\\w+/\\w+/\\w+$"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR, ".*_opt_cc.txt$"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR,
                "-D(\\w+)[=]{0,1}[\\\"]{0,1}(.*?)[\\\"]{0,1}\\s+"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR, ".*_bfolders.txt$"); //$NON-NLS-1$
        store.setDefault(PreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR, "-../(\\w+)/([^\\s\\n]+)"); //$NON-NLS-1$
    }

}
