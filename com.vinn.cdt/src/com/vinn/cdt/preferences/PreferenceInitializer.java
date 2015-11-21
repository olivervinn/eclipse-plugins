/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.vinn.cdt.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_AUTO_LINK_PROJECTS, false);
        store.setDefault(PreferenceConstants.P_HIDE_LINK_CONFIRMATION, false);
        store.setDefault(PreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS, ".*\\.doc;.*\\.test;"); //$NON-NLS-1$
    }

}
