/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.vinn.cdt.Activator;
import com.vinn.utils.preferences.ui.AddRemoveListFieldEditor;
import com.vinn.utils.preferences.ui.SpacerFieldEditor;

public class BuildConfPreferencePage extends FieldEditorPreferencePage
    implements
      IWorkbenchPreferencePage {

  List<StringFieldEditor> stringRegexEditors;

  public BuildConfPreferencePage() {
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Settings for the cross c project environment configuration");
    stringRegexEditors = new ArrayList<StringFieldEditor>();
  }

  public void createFieldEditors() {
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new BooleanFieldEditor(BuildConfPreferenceConstants.P_HIDE_LINK_CONFIRMATION,
        "&Hide link confirmation dialog", getFieldEditorParent()));
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new AddRemoveListFieldEditor(
        BuildConfPreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS,
        "List of project name endings to ignore", getFieldEditorParent()));

    addField(new SpacerFieldEditor(getFieldEditorParent()));

    StringFieldEditor sBuild = new StringFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_PROJECT_NAME,
      "Config project name", getFieldEditorParent());
    sBuild.setEmptyStringAllowed(false);
    addField(sBuild);

    StringFieldEditor sSearch = new StringFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_PROJECT_PATH,
      "Base path to search", getFieldEditorParent());
    sSearch.setEmptyStringAllowed(false);
    addField(sSearch);

    IntegerFieldEditor intField = new IntegerFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_PROJECT_DEPTH,
      "Search depth", getFieldEditorParent());
    intField.setEmptyStringAllowed(false);
    addField(intField);

    stringRegexEditors.add(new StringFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_SELECTOR,
      "Config file selector", getFieldEditorParent()));

    stringRegexEditors.add(new StringFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR,
      "C Define file selector", getFieldEditorParent()));

    stringRegexEditors.add(new StringFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR,
      "C Define extractor", getFieldEditorParent()));

    stringRegexEditors.add(new StringFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR,
      "Folder include file selector", getFieldEditorParent()));

    stringRegexEditors.add(new StringFieldEditor(BuildConfPreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR,
      "Folder include extractor", getFieldEditorParent()));

    for(StringFieldEditor s : stringRegexEditors) {
      s.setEmptyStringAllowed(false);
      addField(s);
    }

    addField(new SpacerFieldEditor(getFieldEditorParent()));

  }

  @Override
  public boolean performOk() {
    for(StringFieldEditor s : stringRegexEditors) {
      String value = s.getStringValue();
      try {
      Pattern.compile(value);
      } catch (PatternSyntaxException e) {
        s.setErrorMessage(String.format("%s - Invalid Regular Expression",s.getLabelText()));
        s.showErrorMessage();
        return false;
      }
    }
    return super.performOk();
  }

  public void init(IWorkbench workbench) {}

}
