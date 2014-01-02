package com.vinn.build.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

  public static final String P_HIDE_LINK_CONFIRMATION = "com.build.link.hidePromptOnLinkChange";

  public static final String P_EXCLUDED_PROJECT_NAME_ENDINGS =
      "com.build.link.excludedProjectsWithEndings";

  public static final String P_STRING_CONF_SELECTOR_REGEX = "com.build.conf.selector.regex";

  /**
   * RegEx to select a extract DEFINES from the files found 
   * with <code>P_STRING_CONF_FILENAME_REGEX</code>.
   */
  public static final String P_STRING_CONF_DEFINE_REGEX = "com.build.conf.define.regex";

  /**
   * Selector for a file in the <code>P_STRING_CONF_REGEX</code> location. Can use groups from that 
   * RegEx result.
   */
  public static final String P_STRING_CONF_DEFINE_FILENAME = "com.build.conf.define.filename";

  /**
   * Selector for a file containing enabled folder listings in the <code>P_STRING_CONF_REGEX</code>
   * location. Can use groups from that RegEx result.
   */
  public static final String P_STRING_CONF_ENBFOLDER_FILENAME = "com.build.conf.enbfolder";

  /**
   * RegEx to extract enabled folder listing from <code>P_STRING_CONF_ENBFOLDER_FILENAME</code>.
   */
  public static final String P_STRING_CONF_ENBFOLDER_REGEX = "com.build.conf.enbfolder.regex";

}
