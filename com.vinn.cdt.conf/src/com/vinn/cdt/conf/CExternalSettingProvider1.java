package com.vinn.cdt.conf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.settings.model.CExternalSetting;
import org.eclipse.cdt.core.settings.model.CMacroEntry;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICMacroEntry;
import org.eclipse.cdt.core.settings.model.ICSettingEntry;
import org.eclipse.cdt.core.settings.model.ICSourceEntry;
import org.eclipse.cdt.core.settings.model.extension.CExternalSettingProvider;
import org.eclipse.core.resources.IProject;

public class CExternalSettingProvider1 extends CExternalSettingProvider {

	public static final String ID = "com.vinn.cdt.provider.meta"; //$NON-NLS-1$
	
	public CExternalSettingProvider1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public CExternalSetting[] getSettings(IProject project, ICConfigurationDescription cfg) {
		//supply a simple macro entry
        ArrayList<ICSettingEntry> pathEntries = new ArrayList<ICSettingEntry>();
        ICMacroEntry macro = new CMacroEntry("MY_DEFINE", "\"my_value\"", 0);
        pathEntries.add(macro);

        ICSettingEntry[] settings = (ICSettingEntry[]) pathEntries.toArray(new ICSettingEntry[pathEntries.size()]);
        return new CExternalSetting[]{new CExternalSetting(null, null, null, settings)};
	}
}
