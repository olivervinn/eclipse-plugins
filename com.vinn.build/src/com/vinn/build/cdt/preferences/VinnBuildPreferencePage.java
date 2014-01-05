package com.vinn.build.cdt.preferences;

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

public class VinnBuildPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  public VinnBuildPreferencePage() {
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
    addField(new BooleanFieldEditor(VinnBuildPreferenceConstants.P_HIDE_LINK_CONFIRMATION,
        "&Hide link confirmation dialog", getFieldEditorParent()));
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    addField(new AddRemoveListFieldEditor(VinnBuildPreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS,
        "List of project name endings to ignore", getFieldEditorParent()));
    
    
    addField(new SpacerFieldEditor(getFieldEditorParent()));
    
    // build
    
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_NAME,
      "Config project name", getFieldEditorParent()));
    
    // out/
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_PATH,
      "Base path to search", getFieldEditorParent()));
    
    // 2
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_DEPTH,
      "Search depth", getFieldEditorParent()));
        
    // \w+/\w+/ (e.g. product/part/ for a folder selector)
    // \w+/\w+/config.txt (e.g. product/part/ for a folder-file selector)
    
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_SELECTOR,
        "Config file selector", getFieldEditorParent()));
    
    // From path where the config was found
    // e.g. product/part/cc_opts.txt
    
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR,
      "C Define file selector", getFieldEditorParent()));
    
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR,
      "C Define extractor", getFieldEditorParent()));
    
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR,
      "Folder include file selector", getFieldEditorParent()));
    
    addField(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR,
      "Folder include extractor", getFieldEditorParent()));
    
    addField(new SpacerFieldEditor(getFieldEditorParent()));
   
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench) {}

}
