/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.utils.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.services.IServiceLocator;

import com.vinn.utils.Activator;
import com.vinn.utils.preferences.VinnMenuPreferenceConstants;

public class MenuViewActivityState extends AbstractSourceProvider {

  public final static String BASIC_STATE_ID = "com.vinn.utils.menu.basic.active";
  public final static String CDTBUILD_STATE_ID = "com.vinn.utils.menu.cdtbuild.active";
  public final static String OTHER_STATE_ID = "com.vinn.utils.menu.other.active";

  public final static String HIDE = "HIDE";
  public final static String SHOW = "SHOW";

  private boolean basic = true;
  private boolean cdtbuild = true;
  private boolean other = true;

  @Override
  public void initialize(final IServiceLocator locator) {
    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    basic = p.getBoolean(VinnMenuPreferenceConstants.P_STRING_HIDE_BASICS);
    cdtbuild = p.getBoolean(VinnMenuPreferenceConstants.P_STRING_HIDE_CDT_BUILD);
    other = p.getBoolean(VinnMenuPreferenceConstants.P_STRING_HIDE_OTHERS);
  }
  
  @Override
  public void dispose() {}

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] {BASIC_STATE_ID, CDTBUILD_STATE_ID};
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Map getCurrentState() {
    Map<String, String> map = new HashMap<String, String>(3);
    map.put(BASIC_STATE_ID, (basic ? HIDE : SHOW));
    map.put(CDTBUILD_STATE_ID, (cdtbuild ? HIDE : SHOW));
    map.put(OTHER_STATE_ID, (other ? HIDE : SHOW));
    return map;
  }
}
