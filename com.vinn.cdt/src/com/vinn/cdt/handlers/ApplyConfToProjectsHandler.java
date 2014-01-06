package com.vinn.cdt.handlers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.settings.model.CMacroEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICFolderDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSetting;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.WriteAccessException;
import org.eclipse.cdt.internal.core.indexer.StdoutLogService;
import org.eclipse.cdt.internal.core.settings.model.CConfigurationDescription;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFilterMatcherDescriptor;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.vinn.cdt.build.Activator;
import com.vinn.cdt.build.Utils;
import com.vinn.cdt.preferences.VinnBuildPreferenceConstants;

public class ApplyConfToProjectsHandler extends AbstractHandler {

	public static final String CONFIGURATION_ID = "com.vinn.build.dynamiccconfig";
	public static final String CONFIGURATION_NAME = "[Vinn Dynamic Configuration]";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		@SuppressWarnings("rawtypes")
		Map params = event.getParameters();
		Object r = params.get("com.vinn.build.commandParameter1");

		if (r == null) {
			setEnvironmentWithText(null, null); // Clear
			removeResourceFilters(); // Clear
		} else {

			// Configuration root (we will filter out others not matching
			IResource searchRoot = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(r.toString());
			if (!searchRoot.isAccessible())
				return null;

			IPreferenceStore prf = Activator.getDefault().getPreferenceStore();

			final String cDefineFileSelector = prf
					.getString(
							VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_FILE_SELECTOR)
					.trim();
			final String cDefineExtractor = prf
					.getString(
							VinnBuildPreferenceConstants.P_STRING_CONF_DEFINE_EXTRACTOR)
					.trim();

			IResource foundResource;
			
			foundResource = findResourceFile(searchRoot, cDefineFileSelector);
			if (foundResource != null)
				setEnvrionmentWithCDefineFile((IFile) foundResource,
						cDefineExtractor);

			final String folderFilterFileSelector = prf
					.getString(
							VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_FILE_SELECTOR)
					.trim();
			final String folderFilterExtractor = prf
					.getString(
							VinnBuildPreferenceConstants.P_STRING_CONF_FOLDER_EXTRACTOR)
					.trim();

			foundResource = findResourceFile(searchRoot,
					folderFilterFileSelector);
			if (foundResource != null)
				setEnvironmentResourceFilterWithFile((IFile) foundResource,
						folderFilterExtractor);

		}

		return null;
	}
	
	private void removeResourceFilters() {
		ICProject[] projects = Utils.getCProjects();
		for (ICProject icProject : projects) {
			IContainer rootContainer = icProject.getProject();
			IResourceFilterDescription[] tempFilters;
			try {
				// Remove any existing
				tempFilters = rootContainer.getFilters();
				for (IResourceFilterDescription i : tempFilters) {
					i.delete(IResource.FORCE, null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void setEnvironmentResourceFilterWithFile(IFile iResource,
			String pattern) {

		Vector<String> projectNames = new Vector<String>();
		Vector<String> paths = new Vector<String>();
		
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
		
		String configPName = iResource.getProject().getName();
		
		ICProject[] projects = Utils.getCProjects();
		for (ICProject icProject : projects) {
			IContainer rootContainer = icProject.getProject();
			IResourceFilterDescription[] tempFilters;
			try {
				// Remove any existing
				tempFilters = rootContainer.getFilters();
				for (IResourceFilterDescription i : tempFilters) {
					i.delete(IResource.FORCE, null);
				}
				
				// Filter other configs
				if (configPName.equals(rootContainer.getName())) {
					
					ArrayList<IResource> allConfFiles = new ArrayList<IResource>();
					Utils.GetConfs(allConfFiles);
					
					for (IResource iResource2 : allConfFiles) {
						if (!iResource2.getProjectRelativePath().toString().equals(iResource.getParent().getProjectRelativePath().toString())) {
							
							FileInfoMatcherDescription matcherDescription = new FileInfoMatcherDescription(
									"org.eclipse.ui.ide.multiFilter",
									String.format("1.0-projectRelativePath-matches-false-true-%s", 
											iResource2.getProjectRelativePath().toString()));
							
							System.out.println("Filter " + matcherDescription.toString());
							
							rootContainer
									.createFilter(
											IResourceFilterDescription.EXCLUDE_ALL
													| IResourceFilterDescription.FOLDERS
													| IResourceFilterDescription.INHERITABLE,
											matcherDescription,
											IResource.BACKGROUND_REFRESH, null);
						}
					}
				}

				// Add new
				for (int i = 0; i < projectNames.size(); i++) {
					if (projectNames.get(i).equals(rootContainer.getName())) {
						FileInfoMatcherDescription matcherDescription = new FileInfoMatcherDescription(
								"org.eclipse.ui.ide.multiFilter",
								String.format("1.0-location-matches-false-true-.*%s$", paths.get(i)));
						
						rootContainer
								.createFilter(
										IResourceFilterDescription.EXCLUDE_ALL
												| IResourceFilterDescription.FOLDERS
												| IResourceFilterDescription.INHERITABLE,
										matcherDescription,
										IResource.BACKGROUND_REFRESH, null);
					}					
				}

			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void setEnvrionmentWithCDefineFile(IFile file,
			String cDefineExtractor) {
		// Open file and parse out the defines e.g. -DOPTION=value

		String defineText = Utils.readTextFile(file);
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
			ICLanguageSettingEntry[] newMacroSettings = new CMacroEntry[defineMap
					.size()];

			int i = 0;
			Iterator<Entry<String, String>> it = defineMap.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, String> pairs = it.next();
				newMacroSettings[i++] = new CMacroEntry(pairs.getKey()
						.toString(), pairs.getValue().toString(),
						ICSettingEntry.VALUE_WORKSPACE_PATH);
			}

			setProjectMacros(icProject, newMacroSettings);
		}
	}

	void setProjectMacros(ICProject project, ICLanguageSettingEntry[] macros) {
		ICProjectDescription pDescription = CoreModel.getDefault()
				.getProjectDescription(project.getProject());

		ICConfigurationDescription activeCfg = pDescription
				.getConfigurationById(CONFIGURATION_ID);
		if (activeCfg == null)
			try {
				activeCfg = pDescription.createConfiguration(CONFIGURATION_ID,
						CONFIGURATION_NAME,
						pDescription.getDefaultSettingConfiguration());
			} catch (WriteAccessException | CoreException e1) {
				e1.printStackTrace();
			}

		activeCfg.setActive();

		ICFolderDescription folderDescription = activeCfg
				.getRootFolderDescription();
		ICLanguageSetting[] languageSettings = folderDescription
				.getLanguageSettings();

		for (int i = 0; i < languageSettings.length; i++) {
			String langId = languageSettings[i].getLanguageId();
			if (langId != null && langId.contains("org.eclipse.cdt.core.gcc")) //$NON-NLS-1$
			{
				languageSettings[i].setSettingEntries(ICSettingEntry.MACRO
						| ICSettingEntry.LOCAL, macros);

				try {
					CoreModel.getDefault().setProjectDescription(
							project.getProject(), pDescription);

				} catch (CoreException e) {
					e.printStackTrace();
				}

				break;
			}
		}
	}

	IResource findResourceFile(IResource searchRoot, String seletorRegex) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IContainer container = workspace.getRoot().getContainerForLocation(
				searchRoot.getLocation());
		try {

			Pattern cDefineFilePattern = Pattern.compile(seletorRegex);
			IResource[] iResources = container.members();

			for (IResource iResource : iResources) {
				if (iResource.getType() == IResource.FILE) {
					Matcher m = cDefineFilePattern.matcher(iResource.getName());
					if (m.find()) {
						return iResource;
					}
				}
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}

		return null;
	}
}
