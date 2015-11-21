package com.vinn.cdt.decorators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.cdtvariables.ICdtVariableManager;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import com.vinn.cdt.Activator;
import com.vinn.cdt.conf.preferences.PreferenceConstants;

public class CMBuildDecorator implements ILightweightLabelDecorator {

    private static final ImageDescriptor OKAY = Activator.getImageDescriptor("icons/module_okay.gif");
    // private static final ImageDescriptor WARN =
    // Activator.getImageDescriptor("icons/module_warning.gif");
    private static final ImageDescriptor ISSUE = Activator.getImageDescriptor("icons/module_issue.gif");
    final static String PARENT_FILE = "./component.mk";
    final static String PARENT_CHILD_FILE = "./module.mk";
    final static String[] ALLOWED_SUB_FOLDERS = { "src", "internal", "bin", "lib", "cfg" };

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

    @Override
    public void decorate(Object element, IDecoration decoration) {
        try {
            if (element instanceof IProject) {
                IProject f = (IProject) element;
                if (f.getName().contains("build")) {
                    String i = Activator.getDefault().getPreferenceStore()
                            .getString(PreferenceConstants.P_STRING_BUILD_META_FILE);
                    ICdtVariableManager varManager = CCorePlugin.getDefault().getCdtVariableManager();
                    String resolvedValue = varManager.resolveValue(i, "", null, null);
                    File file = new File(resolvedValue);
                    if (file.exists()) {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            decoration.addSuffix(" < " + line);
                            br.close();
                            return;
                        }
                        br.close();
                    }
                }
            } else if (element instanceof IContainer) {
                IContainer f = (IContainer) element;
                if (f.findMember(PARENT_CHILD_FILE) != null) {
                    // decoration.addSuffix(" [" + PARENT_CHILD_MARK + "]");
                    String folderName = f.getName().toLowerCase();
                    if (!folderName.contains("third.party") && !folderName.contains("thirdparty")
                            && !folderName.contains("include")) {
                        IResource[] childMemberResources = f.members();
                        for (IResource m_ : childMemberResources) {
                            if (m_ instanceof IFolder) {
                                String fname = ((IFolder) m_).getName();
                                if (!Arrays.asList(ALLOWED_SUB_FOLDERS).contains(fname)) {
                                    decoration.addOverlay(ISSUE, IDecoration.TOP_RIGHT);
                                    return;
                                }
                            }
                        }

                    }
                } else if (f.findMember(PARENT_FILE) != null) {
                    // decoration.addSuffix(" < " + PARENT_MARK);
                }
            } else if (element instanceof IResource) {
                IResource f = (IResource) element;
                String i = Activator.getDefault().getPreferenceStore()
                        .getString(PreferenceConstants.P_STRING_BUILD_META_FILE);
                ICdtVariableManager varManager = CCorePlugin.getDefault().getCdtVariableManager();
                String resolvedValue = varManager.resolveValue(i, "", null, null);
                File file = new File(resolvedValue);
                if (file.exists()) {
                    String m = f.getFullPath().toPortableString();
                    Scanner s = new Scanner(file);
                    try {
                        while (s.findWithinHorizon(m, 0) != null) {
                            decoration.addOverlay(OKAY, IDecoration.BOTTOM_LEFT);
                            return;
                        }
                    } finally {
                        s.close();
                    }
                    String s1 = f.getRawLocation().toOSString();
                    if (s1.equals(resolvedValue)) {
                        decoration.addOverlay(OKAY, IDecoration.BOTTOM_LEFT);
                        return;
                    }
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
