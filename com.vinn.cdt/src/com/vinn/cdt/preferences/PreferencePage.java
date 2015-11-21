/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.vinn.cdt.Activator;
import com.vinn.utils.preferences.ui.AddRemoveListFieldEditor;
import com.vinn.utils.preferences.ui.SpacerFieldEditor;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "com.vinn.cdt.preferences.PreferencePage"; //$NON-NLS-1$

	List<StringFieldEditor> stringRegexEditors;

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Settings for CDT project workspace configuration");
		stringRegexEditors = new ArrayList<StringFieldEditor>();
	}

	@Override
    public void createFieldEditors() {
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_AUTO_LINK_PROJECTS,
				"&Automatically link CDT projects", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_HIDE_LINK_CONFIRMATION,
				"&Hide manual link confirmation dialog", getFieldEditorParent()));
		addField(new AddRemoveListFieldEditor(PreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS,
				"Exclude projects with name(s)", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));

		for (StringFieldEditor s : stringRegexEditors) {
			s.setEmptyStringAllowed(false);
			addField(s);
		}

		addField(new SpacerFieldEditor(getFieldEditorParent()));

	}

	@Override
	public boolean performOk() {
		for (StringFieldEditor s : stringRegexEditors) {
			String value = s.getStringValue();
			try {
				Pattern.compile(value);
			} catch (PatternSyntaxException e) {
				s.setErrorMessage(String.format("%s - Invalid Regular Expression", s.getLabelText()));
				s.showErrorMessage();
				return false;
			}
		}
		return super.performOk();
	}

	@Override
    public void init(IWorkbench workbench) {
	}

}
