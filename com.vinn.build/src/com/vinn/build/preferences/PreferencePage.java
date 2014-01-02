package com.vinn.build.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.vinn.build.Activator;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace
 * that allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that
 * belongs to the main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  public PreferencePage() {
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Settings related to the enivronment (cross c project)");
  }

  /**
   * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to
   * manipulate various types of preferences. Each field editor knows how to save and restore
   * itself.
   */
  public void createFieldEditors() {
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new BooleanFieldEditor(PreferenceConstants.P_HIDE_LINK_CONFIRMATION,
        "&Hide link confirmation dialog", getFieldEditorParent()));
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new AddRemoveListFieldEditor(PreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS,
        "List of project name endings to ignore", getFieldEditorParent()));
    
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new StringFieldEditor(PreferenceConstants.P_STRING_CONF_SELECTOR_REGEX,
        "Regex to find a configuration e.g. <project>/path/\\.conf (from workspace)", getFieldEditorParent()));
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    
    addField(new StringFieldEditor(PreferenceConstants.P_STRING_CONF_DEFINE_FILENAME,
      "Selector for file in configuration &Configuration file extension (without dot)", getFieldEditorParent()));
    addField(new StringFieldEditor(PreferenceConstants.P_STRING_CONF_DEFINE_REGEX,
      "&Configuration file extension (without dot)", getFieldEditorParent()));
    
    addField(new StringFieldEditor(PreferenceConstants.P_STRING_CONF_ENBFOLDER_FILENAME,
      "&RegEx to findConfiguration file extension (without dot)", getFieldEditorParent()));
    addField(new StringFieldEditor(PreferenceConstants.P_STRING_CONF_ENBFOLDER_REGEX,
      "&Configuration file extension (without dot)", getFieldEditorParent()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench) {}

}
