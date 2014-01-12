package com.vinn.utils.font.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.vinn.utils.Activator;

public class VinnFontUtilsPreferencePage extends FieldEditorPreferencePage
    implements
      IWorkbenchPreferencePage {

  public VinnFontUtilsPreferencePage() {
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Font Size");
  }

  @Override
  protected void createFieldEditors() {
    addField(new IntegerFieldEditor(VinnFontUtilsPreferenceConstants.P_STRING_FONT_STEP,
        "&Font Step Amount", getFieldEditorParent()));
    addField(new IntegerFieldEditor(VinnFontUtilsPreferenceConstants.P_STRING_FONT_RESETSIZE,
        "&Font Reset Size", getFieldEditorParent()));
  }

  @Override
  public void init(IWorkbench workbench) {
  }
}
