package com.vinn.cdt.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.vinn.cdt.Activator;
import com.vinn.utils.preferences.AddRemoveListFieldEditor;
import com.vinn.utils.preferences.SpacerFieldEditor;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace
 * that allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that
 * belongs to the main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */

public class VinnBuildPreferencePage extends FieldEditorPreferencePage
    implements
      IWorkbenchPreferencePage {
  
  List<StringFieldEditor> stringRegexEditors;

  public VinnBuildPreferencePage() {
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    setDescription("Settings for the cross c project environment configuration");
    stringRegexEditors = new ArrayList<StringFieldEditor>();
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
    addField(new AddRemoveListFieldEditor(
        VinnBuildPreferenceConstants.P_EXCLUDED_PROJECT_NAME_ENDINGS,
        "List of project name endings to ignore", getFieldEditorParent()));

    addField(new SpacerFieldEditor(getFieldEditorParent()));

    // build
    StringFieldEditor sBuild = new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_NAME,
      "Config project name", getFieldEditorParent());
    sBuild.setEmptyStringAllowed(false);
    addField(sBuild);

    // out/
    StringFieldEditor sSearch = new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_PATH,
      "Base path to search", getFieldEditorParent());
    sSearch.setEmptyStringAllowed(false);
    addField(sSearch);

    // 2
    IntegerFieldEditor intField = new IntegerFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_DEPTH,
      "Search depth", getFieldEditorParent());
    intField.setEmptyStringAllowed(false);
    addField(intField);
    

    // \w+/\w+/ (e.g. product/part/ for a folder selector)
    // \w+/\w+/config.txt (e.g. product/part/ for a folder-file selector)

    stringRegexEditors.add(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_SELECTOR,
      "Config file selector", getFieldEditorParent()));

    // From path where the config was found
    // e.g. product/part/cc_opts.txt

    stringRegexEditors.add(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR,
      "C Define file selector", getFieldEditorParent()));

    stringRegexEditors.add(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR,
      "C Define extractor", getFieldEditorParent()));

    stringRegexEditors.add(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR,
      "Folder include file selector", getFieldEditorParent()));

    stringRegexEditors.add(new StringFieldEditor(VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR,
      "Folder include extractor", getFieldEditorParent()));

    for(StringFieldEditor s : stringRegexEditors) {
      s.setEmptyStringAllowed(false);
      addField(s);
    }

    addField(new SpacerFieldEditor(getFieldEditorParent()));

  }
    
  @Override
  public boolean performOk() {
    // Check that regex compile
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

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench) {}

}
