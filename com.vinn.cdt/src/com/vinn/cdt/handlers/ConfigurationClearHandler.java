package com.vinn.cdt.handlers;

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
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.statushandlers.StatusManager;

import com.vinn.cdt.build.Activator;
import com.vinn.cdt.build.ConfigurationManager;
import com.vinn.cdt.build.Utils;
import com.vinn.cdt.preferences.VinnBuildPreferenceConstants;

public class ConfigurationClearHandler extends AbstractHandler {

	public static final String CONFIGURATION_ID = "com.vinn.build.dynamiccconfig";
	public static final String CONFIGURATION_NAME = "[Vinn Dynamic Configuration]";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		@SuppressWarnings("rawtypes")
		Map params = event.getParameters();
		Object r = params.get("com.vinn.build.commandParameter1");
		
		ConfigurationManager.getInstance().remove();
		
		return null;
	}
}
