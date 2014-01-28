/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.conf.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.cdt.preferences.BuildConfPreferencePage;

public class OpenBuildPreferencesPageHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Shell shell = HandlerUtil.getActiveShellChecked(event);
    PreferenceDialog p = PreferencesUtil.createPreferenceDialogOn(shell, 
      BuildConfPreferencePage.ID, null, null, PreferencesUtil.OPTION_FILTER_LOCKED);
    
    p.open();
    return null;
  }

}