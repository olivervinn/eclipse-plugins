/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.handlers.view;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.FontData;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.vinn.utils.Activator;
import com.vinn.utils.preferences.VinnFontUtilsPreferenceConstants;

public class FontSizeChangeHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    
    if (event.getCommand().getId().endsWith(".increase")) {
      changeFontSize(p.getInt(VinnFontUtilsPreferenceConstants.P_STRING_FONT_STEP), true);
    } else if (event.getCommand().getId().endsWith(".decrease")) {
      changeFontSize(-p.getInt(VinnFontUtilsPreferenceConstants.P_STRING_FONT_STEP), true);
    } else {
      changeFontSize(p.getInt(VinnFontUtilsPreferenceConstants.P_STRING_FONT_RESETSIZE), false);
    }
    return null;
  }

  private static void changeFontSize(int deltaOrSize, boolean isDelta) {

    final String q = "org.eclipse.ui.workbench";
    final String[] keys =
        {"org.eclipse.jdt.ui.editors.textfont", "org.eclipse.cdt.ui.editors.textfont",
            "org.eclipse.ui.workbench.texteditor.blockSelectionModeFont",
            "org.eclipse.jface.textfont"};

    IPreferencesService prefService = Platform.getPreferencesService();
    Preferences pref = prefService.getRootNode().node("/instance/" + q);

    for (String key : keys) {

      String prefItemValue = prefService.getString(q, key, null, null);

      // Skip if not valid on this host for some reason
      if (prefItemValue == null) continue;

      FontData fontData[] = PreferenceConverter.basicGetFontData(prefItemValue);
      if (fontData == null || fontData.length < 1) continue;

      // Update font size by delta
      FontData fontCopy = fontData[0];
      int s = isDelta ? fontCopy.getHeight() + deltaOrSize : deltaOrSize;
      fontCopy.setHeight(Math.max(s, 4));

      // Update pref value
      pref.put(key, fontCopy.toString());
    }

    // Persist changes to all prefs
    try {
      pref.flush();
    } catch (BackingStoreException e) {}
  }
}
