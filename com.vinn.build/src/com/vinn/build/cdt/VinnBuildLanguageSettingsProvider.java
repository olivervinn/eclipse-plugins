package com.vinn.build.cdt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.language.settings.providers.ILanguageSettingsProvider;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.core.resources.IResource;

public class VinnBuildLanguageSettingsProvider implements ILanguageSettingsProvider {

  public static final String ID = "com.vinn.build.cdt.settings.provider"; //$NON-NLS-1$
  private String id;
  private String name;

  /** Language scope, i.e. list of languages the entries will be provided for. */
  protected List<String> languageScope = null;

  /** Custom parameter. Intended for providers extending this class. */
  protected String customParameter = null;

  /** List of entries defined by this provider. */
  private List<ICLanguageSettingEntry> entries = null;

  /**
   * Default constructor.
   */
  public VinnBuildLanguageSettingsProvider() {
          this.id = this.getClass().getCanonicalName();
          this.name = this.getClass().getSimpleName();
  }

  /**
   * Constructor. Creates an "empty" provider.
   * 
   * @param id - id of the provider.
   * @param name - name of the provider to be presented to a user.
   */
  public VinnBuildLanguageSettingsProvider(String id, String name) {
          this.id = id;
          this.name = name;
  }

  /**
   * Constructor.
   * 
   * @param id - id of the provider.
   * @param name - name of the provider to be presented to a user.
   * @param languages - list of languages the {@code entries} provided for.
   *    {@code languages} can be {@code null}, in this case the {@code entries}
   *    are provided for any language.
   * @param entries - the list of language settings entries this provider provides.
   *    If {@code null} is passed, the provider creates an empty list.
   */
  public VinnBuildLanguageSettingsProvider(String id, String name, List<String> languages, List<ICLanguageSettingEntry> entries) {
          this.id = id;
          this.name = name;
          this.languageScope = languages!=null ? new ArrayList<String>(languages) : null;
          this.entries = cloneList(entries);
  }

  /**
   * Constructor.
   * 
   * @param id - id of the provider.
   * @param name - name of the provider to be presented to a user.
   * @param languages - list of languages the {@code entries} provided for.
   *    {@code languages} can be {@code null}, in this case the {@code entries}
   *    are provided for any language.
   * @param entries - the list of language settings entries this provider provides.
   *    If {@code null} is passed, the provider creates an empty list.
   * @param customParameter - a custom parameter as the means to customize
   *    providers extending this class.
   */
  public VinnBuildLanguageSettingsProvider(String id, String name, List<String> languages, List<ICLanguageSettingEntry> entries, String customParameter) {
          this.id = id;
          this.name = name;
          this.languageScope = languages!=null ? new ArrayList<String>(languages) : null;
          this.entries = cloneList(entries);
          this.customParameter = customParameter;
  }

  /**
   * A method to configure the provider. The initialization of provider from
   * the extension point is done in 2 steps. First, the class is created as
   * an executable extension using the default provider. Then this method is
   * used to configure the provider.
   * 
   * It is not allowed to reconfigure the provider.
   * 
   * @param id - id of the provider.
   * @param name - name of the provider to be presented to a user.
   * @param languages - list of languages the {@code entries} provided for.
   *    {@code languages} can be {@code null}, in this case the {@code entries}
   *    are provided for any language.
   * @param entries - the list of language settings entries this provider provides.
   *    If {@code null} is passed, the provider creates an empty list.
   * @param customParameter - a custom parameter as the means to customize
   *    providers extending this class from extension definition in {@code plugin.xml}.
   * 
   * @throws UnsupportedOperationException if an attempt to reconfigure provider is made.
   */
  public void configureProvider(String id, String name, List<String> languages, List<ICLanguageSettingEntry> entries, String customParameter) {
          if (this.entries!=null)
                  throw new UnsupportedOperationException("LanguageSettingsBaseProvider.CanBeConfiguredOnlyOnce"); //$NON-NLS-1$

          this.id = id;
          this.name = name;
          this.languageScope = languages!=null ? new ArrayList<String>(languages) : null;
          this.entries = cloneList(entries);
          this.customParameter = customParameter;
  }

  /**
   * Set extension ID.
   *
   * @param id of extension
   */
  public void setId(String id) {
          this.id = id;
  }

  /**
   * Set extension name.
   *
   * @param name of extension
   */
  public void setName(String name) {
          this.name = name;
  }

  /**
   * @return id of extension
   */
  public String getId() {
          return id;
  }

  /**
   * @return name of extension
   */
  public String getName() {
          return name;
  }

  /* (non-Javadoc)
   * @see org.eclipse.cdt.core.language.settings.providers.ILanguageSettingsProvider#getSettingEntries(org.eclipse.cdt.core.settings.model.ICConfigurationDescription, org.eclipse.core.resources.IResource, java.lang.String)
   */
  public List<ICLanguageSettingEntry> getSettingEntries(ICConfigurationDescription cfgDescription, IResource rc, String languageId) {
          if (languageScope==null) {
                  return cloneList(entries);
          }
          for (String lang : languageScope) {
                  if (lang.equals(languageId)) {
                          return cloneList(entries);
                  }
          }
          return null;
  }

  /**
   * @return the list of languages this provider provides for.
   */
  public List<String> getLanguageIds() {
          return languageScope!=null ? new ArrayList<String>(languageScope) : null;
  }

  /**
   * @return the custom parameter defined in the extension in {@code plugin.xml}.
   */
  public String getCustomParameter() {
          return customParameter;
  }

  /**
   * @param entries
   * @return copy of the list of the entries.
   */
  private List<ICLanguageSettingEntry> cloneList(List<ICLanguageSettingEntry> entries) {
          return entries!=null ? new ArrayList<ICLanguageSettingEntry>(entries) : null;
  }

  /**
   * Method toString() intended for debugging purpose only.
   */
  @SuppressWarnings("nls")
  @Override
  public String toString() {
          return "id="+id+", name="+name;
  }
}
