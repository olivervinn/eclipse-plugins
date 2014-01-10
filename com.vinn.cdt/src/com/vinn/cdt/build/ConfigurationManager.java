package com.vinn.cdt.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.settings.model.CMacroEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICFolderDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSetting;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.statushandlers.StatusManager;

import com.vinn.cdt.preferences.VinnBuildPreferenceConstants;

public class ConfigurationManager {

  private static volatile ConfigurationManager instance = null;

  public static final String CONFIGURATION_ID = "com.vinn.build.dynamiccconfig";
  public static final String CONFIGURATION_NAME = "[Vinn Dynamic Configuration]";

  private IPreferenceStore prf;
  private List<ConfigurationEntity> fConfigurationEntities;
  private ConfigurationEntity fActiveConfigurationEntity;

  public class ConfigurationEntity {
    private IResource fRoot;
    private IResource fDefinesFile;
    private IResource fFilterFile;

    public IResource getConfRoot() {
      return fRoot;
    }

    public IResource getDefinesFile() {
      return fDefinesFile;
    }

    public IResource getFilterFile() {
      return fFilterFile;
    }

    public ConfigurationEntity(IResource configuration, IResource defines, IResource filter) {
      fRoot = configuration;
      fDefinesFile = defines;
      fFilterFile = filter;
    }
  }

  public ConfigurationManager() {
    prf = Activator.getDefault().getPreferenceStore();
  }

  public static ConfigurationManager getInstance() {
    if (instance == null) {
      synchronized (ConfigurationManager.class) {
        if (instance == null) {
          instance = new ConfigurationManager();
        }
      }
    }
    return instance;
  }

  public void remove() {
    fActiveConfigurationEntity = null;
    setEnvironmentWithText(null, null);
    removeResourceFiltersFromAllProjects();
    StatusManager.getManager().handle(
        new Status(IStatus.INFO, Activator.PLUGIN_ID, "Removed configuration"));

  }

  public Object apply(ConfigurationEntity entity) {

    if (entity == null) {
      remove();
    } else {
      if (fConfigurationEntities != null)
        if (fConfigurationEntities.contains(entity)) {
          fActiveConfigurationEntity = entity;
        } else {
          fConfigurationEntities.add(entity);
          fActiveConfigurationEntity = entity;
        }
      else {
        StatusManager.getManager().handle(
            new Status(IStatus.INFO, Activator.PLUGIN_ID, "Bad configuration state"));
        return null;
      }

      removeResourceFiltersFromAllProjects();

      if (!fActiveConfigurationEntity.getConfRoot().isAccessible()) {
        StatusManager.getManager().handle(
            new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not access "
                + fActiveConfigurationEntity.getConfRoot().getName()));
        return null;
      }

      final String cDefineExtractor =
          prf.getString(VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR).trim();

      IResource foundResource = fActiveConfigurationEntity.getDefinesFile();
      if (foundResource != null) {
        setEnvrionmentWithCDefineFile((IFile) foundResource, cDefineExtractor);
      } else {
        StatusManager.getManager()
            .handle(
                new Status(IStatus.ERROR, Activator.PLUGIN_ID, String.format(
                    "Unable to find a defines file in (%s)",
                    fActiveConfigurationEntity.getConfRoot())));
      }

      final String folderFilterExtractor =
          prf.getString(VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR).trim();

      foundResource = fActiveConfigurationEntity.getFilterFile();
      if (foundResource == null) {
        StatusManager.getManager().handle(
            new Status(IStatus.ERROR, Activator.PLUGIN_ID, String.format(
                "Unable to find a filter file in (%s)", fActiveConfigurationEntity.getConfRoot())));
      }
      // Carry on as will just filter the configurations
      setEnvironmentResourceFilterWithFile((IFile) foundResource, folderFilterExtractor);
    }

    return null;
  }

  public IResource findDefineFile(IResource searchRoot) {
    return findResourceFile(searchRoot,
        VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR);
  }

  public IResource findFilterFile(IResource searchRoot) {
    return findResourceFile(searchRoot,
        VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR);
  }

  private IResource findResourceFile(IResource searchRoot, String patternId) {

    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    final String patternTxt = prf.getString(patternId).trim();
    Pattern pattern = Pattern.compile(patternTxt);

    ArrayList<IResource> found = new ArrayList<IResource>();
    Utils.FindResourceInTree(found, searchRoot.getLocation(), workspaceRoot, pattern, 3);

    if (found.size() == 1) {
      return found.get(0);
    } else {
      return null;
    }
  }

  private void removeResourceFiltersFromAllProjects() {
    ICProject[] projects = Utils.getCProjects();
    for (ICProject icProject : projects) {
      try {
        IContainer rootContainer = icProject.getProject();
        for (IResourceFilterDescription i : rootContainer.getFilters()) {
          i.delete(IResource.FORCE, null);
        }
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }
  }

  private void setEnvironmentResourceFilterWithFile(IFile iResource, String pattern) {

    Vector<String> projectNames = new Vector<String>();
    Vector<String> paths = new Vector<String>();

    try {
      String text = Utils.readTextFile(iResource);
      if (text != null && pattern != null) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        while (m.find()) {
          projectNames.add(m.group(1));
          paths.add(m.group(2));
          System.out.println(m.group());
        }
      }
    } catch (Exception e) {
      // If is occurs assuming most situations its because none exists rather
      // than a parse error. In this case we still want to continue to filter the
      // other configs
      projectNames.clear();
      paths.clear();
    }

    // Filters should have been removed by time we are here and active project set

    ICProject[] projects = Utils.getCProjects();
    IContainer activeConfigProjContainer = getActiceConfiguration().getConfRoot().getProject();

    for (ConfigurationEntity entity : fConfigurationEntities) {
      if (!entity.getConfRoot().equals(getActiceConfiguration().getConfRoot())) {
        FileInfoMatcherDescription matcherDescription =
            new FileInfoMatcherDescription("org.eclipse.ui.ide.multiFilter", String.format(
                "1.0-projectRelativePath-matches-false-true-%s", entity.getConfRoot()
                    .getProjectRelativePath().toString()));

        try {
          activeConfigProjContainer.createFilter(IResourceFilterDescription.EXCLUDE_ALL
              | IResourceFilterDescription.FOLDERS | IResourceFilterDescription.INHERITABLE,
              matcherDescription, IResource.BACKGROUND_REFRESH, null);
        } catch (CoreException e) {
          e.printStackTrace();
        }
      }
    }

    StatusManager.getManager().handle(
        new Status(IStatus.INFO, Activator.PLUGIN_ID, String.format("Applied filter on configs")));

    if (projectNames.size() > 0) {
      for (ICProject icProject : projects) {
        activeConfigProjContainer = icProject.getProject();
        try {
          // Add new
          for (int i = 0; i < projectNames.size(); i++) {
            if (projectNames.get(i).equals(activeConfigProjContainer.getName())) {
              FileInfoMatcherDescription matcherDescription =
                  new FileInfoMatcherDescription("org.eclipse.ui.ide.multiFilter", String.format(
                      "1.0-location-matches-false-true-.*%s$", paths.get(i)));

              activeConfigProjContainer.createFilter(IResourceFilterDescription.EXCLUDE_ALL
                  | IResourceFilterDescription.FOLDERS | IResourceFilterDescription.INHERITABLE,
                  matcherDescription, IResource.BACKGROUND_REFRESH, null);
            }
          }
        } catch (CoreException e) {
          e.printStackTrace();
        }
      }
      StatusManager.getManager().handle(
          new Status(IStatus.INFO, Activator.PLUGIN_ID, String.format(
              "Applied project resource filters (%d)", projectNames.size())));
    }
  }

  public ConfigurationEntity getActiceConfiguration() {
    return fActiveConfigurationEntity;
  }

  public List<ConfigurationEntity> getConfigurationResources() {

    // Find Environment *.confs the parameter is the IResource
    // this is used later to extract the presentation name as well
    // as then use the IResource to parse the connect if selected

    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

    // Find project and base location to search for configurations

    IPreferenceStore p = Activator.getDefault().getPreferenceStore();
    final String projectName =
        p.getString(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_NAME).trim();
    final String projectPath =
        p.getString(VinnBuildPreferenceConstants.P_STRING_CONF_PROJECT_PATH).trim();

    IProject confHomeProject = workspaceRoot.getProject(projectName);
    IPath path = confHomeProject.getLocation().append(projectPath);

    boolean pathExists = workspaceRoot.getContainerForLocation(path).exists();
    if (!confHomeProject.isAccessible() || !pathExists) {
      return null;
    }

    // Configuration identification

    final String confSelector =
        p.getString(VinnBuildPreferenceConstants.P_STRING_CONF_SELECTOR).trim();
    final Pattern confSelectorPattern =
        java.util.regex.Pattern.compile(confSelector, Pattern.CASE_INSENSITIVE);


    IResourceFilterDescription[] tempFilters = null;

    try {
      tempFilters = confHomeProject.getFilters();
      for (IResourceFilterDescription i : tempFilters) {
        i.delete(IResource.FORCE, null);
      }
    } catch (CoreException e) {}

    List<IResource> foundConfigRoots = new ArrayList<IResource>();
    Utils.FindResourceInTree(foundConfigRoots, path, workspaceRoot, confSelectorPattern, 3);

    fConfigurationEntities = new ArrayList<ConfigurationEntity>();
    for (IResource r : foundConfigRoots) {
      fConfigurationEntities.add(new ConfigurationEntity(r, findDefineFile(r), findFilterFile(r)));
    }

    if (tempFilters != null) {
      for (IResourceFilterDescription i : tempFilters) {
        try {
          confHomeProject.createFilter(i.getType(), i.getFileInfoMatcherDescription(),
              IResource.BACKGROUND_REFRESH, null);
        } catch (CoreException e) {
          e.printStackTrace();
        }
      }
    }

    fActiveConfigurationEntity = null;
    return fConfigurationEntities;
  }

  private void setEnvrionmentWithCDefineFile(IFile file, String cDefineExtractor) {
    // Open file and parse out the defines e.g. -DOPTION=value

    String defineText = Utils.readTextFile(file);
    if (!defineText.equals(""))
      StatusManager.getManager().handle(
          new Status(IStatus.INFO, Activator.PLUGIN_ID, String.format("Using define file (%s)",
              file.getName())));

    setEnvironmentWithText(defineText, cDefineExtractor);
  }

  private void setEnvironmentWithText(String text, String cDefineExtractor) {

    Map<String, String> defineMap = new HashMap<String, String>();

    if (text != null && cDefineExtractor != null) {
      Pattern p = Pattern.compile(cDefineExtractor);
      Matcher m = p.matcher(text);
      while (m.find()) {
        String s = m.group();
        defineMap.put(m.group(1), m.group(2));
        System.out.println(s);
      }
    }

    // If we have some apply them to all eligible projects
    ICProject[] projects = Utils.getCProjects();
    for (ICProject icProject : projects) {
      ICLanguageSettingEntry[] newMacroSettings = new CMacroEntry[defineMap.size()];

      int i = 0;
      Iterator<Entry<String, String>> it = defineMap.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, String> pairs = it.next();
        newMacroSettings[i++] =
            new CMacroEntry(pairs.getKey().toString(), pairs.getValue().toString(),
                ICSettingEntry.VALUE_WORKSPACE_PATH);
      }

      setProjectMacros(icProject, newMacroSettings);
    }

    StatusManager.getManager().handle(
        new Status(IStatus.INFO, Activator.PLUGIN_ID, String.format("Set macros (%s)", defineMap)));
  }

  void setProjectMacros(ICProject project, ICLanguageSettingEntry[] macros) {
    ICProjectDescription pDescription =
        CoreModel.getDefault().getProjectDescription(project.getProject());

    ICConfigurationDescription activeCfg = pDescription.getConfigurationById(CONFIGURATION_ID);
    if (activeCfg == null)
      try {
        activeCfg =
            pDescription.createConfiguration(CONFIGURATION_ID, CONFIGURATION_NAME,
                pDescription.getDefaultSettingConfiguration());
      } catch (CoreException e1) {
        e1.printStackTrace();
      }

    activeCfg.setActive();

    ICFolderDescription folderDescription = activeCfg.getRootFolderDescription();
    ICLanguageSetting[] languageSettings = folderDescription.getLanguageSettings();

    for (int i = 0; i < languageSettings.length; i++) {
      String langId = languageSettings[i].getLanguageId();
      if (langId != null && langId.contains("org.eclipse.cdt.core.gcc")) //$NON-NLS-1$
      {
        languageSettings[i].setSettingEntries(ICSettingEntry.MACRO | ICSettingEntry.LOCAL, macros);

        try {
          CoreModel.getDefault().setProjectDescription(project.getProject(), pDescription);

        } catch (CoreException e) {
          e.printStackTrace();
        }

        break;
      }
    }
  }

}