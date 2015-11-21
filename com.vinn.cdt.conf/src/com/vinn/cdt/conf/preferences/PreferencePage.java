/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.conf.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.vinn.cdt.conf.Activator;
import com.vinn.utils.preferences.ui.SpacerFieldEditor;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String ID = "com.vinn.cdt.conf.preferences.PreferencePage"; //$NON-NLS-1$

    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Settings for CDT project workspace configuration");
    }

    @Override
    public void createFieldEditors() {

        addField(new WorkspaceFileFieldEditor(PreferenceConstants.P_STRING_BUILD_META_FILE_REGEX,
                "Build Meta File Selector", getFieldEditorParent()));
        addField(new WorkspaceFileFieldEditor(PreferenceConstants.P_STRING_BUILD_META_FILE, "Build Meta File",
                getFieldEditorParent()));

        addField(new SpacerFieldEditor(getFieldEditorParent()));

    }

    @Override
    public boolean performOk() {
        return super.performOk();
    }

    @Override
    public void init(IWorkbench workbench) {
    }

}
