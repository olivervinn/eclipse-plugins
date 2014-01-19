/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.vinn.utils.Activator;
import com.vinn.utils.preferences.ui.LinkFieldEditor;
import com.vinn.utils.preferences.ui.SpacerFieldEditor;

public class VinnFontUtilsPreferencePage extends FieldEditorPreferencePage
    implements
      IWorkbenchPreferencePage {

  public VinnFontUtilsPreferencePage() {
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Specify font size keyboard shortcut change amount and default font size");
  }

  @Override
  protected void createFieldEditors() {
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new IntegerFieldEditor(VinnFontUtilsPreferenceConstants.P_STRING_FONT_STEP,
        "&Font Step Amount", getFieldEditorParent()));
    addField(new IntegerFieldEditor(VinnFontUtilsPreferenceConstants.P_STRING_FONT_RESETSIZE,
        "&Font Reset Size", getFieldEditorParent()));
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new LinkFieldEditor("See ", "keys", "org.eclipse.ui.preferences.keysPreferencePage",
        " for setting keyboard shortcuts", getFieldEditorParent()));
  }


  @Override
  public void init(IWorkbench workbench) {}
}
