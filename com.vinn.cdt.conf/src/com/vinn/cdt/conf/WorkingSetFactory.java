package com.vinn.cdt.conf;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

public class WorkingSetFactory {

    IWorkingSet create(String name, String[] paths) {
        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot wsRoot = ws.getRoot();
        IWorkingSetManager manager = wb.getWorkingSetManager();
        IAdaptable[] elements = new IAdaptable[paths.length];

        for (int i = 0; i < paths.length; i++) {
            IFile p = wsRoot.getFileForLocation(Path.fromOSString(paths[i]));
            if (p != null) {
                elements[i] = p;
            }
        }

        IWorkingSet result = manager.createWorkingSet(name, elements);
        return result;
    }

}
