package com.vinn.utils.menu.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.vinn.utils.Activator;
import com.vinn.utils.preferences.SpacerFieldEditor;
import com.vinn.utils.preferences.LabelFieldEditor;

public class VinnMenuPreferencePage extends FieldEditorPreferencePage
    implements
      IWorkbenchPreferencePage {

  public VinnMenuPreferencePage() {
    super(FLAT);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Editor Context Menu Item");
  }

  @Override
  protected void createFieldEditors() {
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new BooleanFieldEditor(VinnMenuPreferenceConstants.P_STRING_HIDE_BASICS,
        "&Hide Basic Actions (cut, copy, paste..)", getFieldEditorParent()));
    addField(new BooleanFieldEditor(VinnMenuPreferenceConstants.P_STRING_HIDE_CDT_BUILD,
        "&Hide CDT Build Actions (build selected...)", getFieldEditorParent()));
    addField(new BooleanFieldEditor(VinnMenuPreferenceConstants.P_STRING_HIDE_OTHERS,
      "&Hide Others (Easy shell, Pydev...)", getFieldEditorParent()));
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new LabelFieldEditor("* Changes require restart to take affect", getFieldEditorParent()));
  }

  @Override
  public void init(IWorkbench workbench) {}
}
