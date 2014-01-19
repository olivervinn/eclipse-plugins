/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.vinn.utils.Activator;
import com.vinn.utils.preferences.ui.LabelFieldEditor;
import com.vinn.utils.preferences.ui.SpacerFieldEditor;

public class VinnMenuPreferencePage extends FieldEditorPreferencePage
    implements
      IWorkbenchPreferencePage {

  public VinnMenuPreferencePage() {
    super(FLAT);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Select the following options to hide items from the context menu");
  }

  @Override
  protected void createFieldEditors() {
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new BooleanFieldEditor(VinnMenuPreferenceConstants.P_STRING_HIDE_BASICS,
        "Hide basic actions (cut, copy, paste..)", getFieldEditorParent()));
    addField(new BooleanFieldEditor(VinnMenuPreferenceConstants.P_STRING_HIDE_CDT_BUILD,
        "Hide CDT build actions (build selected...)", getFieldEditorParent()));
    addField(new BooleanFieldEditor(VinnMenuPreferenceConstants.P_STRING_HIDE_OTHERS,
      "Hide other items from editor menu (Easy shell, Pydev...)", getFieldEditorParent()));
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new LabelFieldEditor("* Changes require restart to take affect", getFieldEditorParent()));
  }

  @Override
  public void init(IWorkbench workbench) {}
}
