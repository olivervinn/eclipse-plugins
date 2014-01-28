package com.vinn.cdt.decorators;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.vinn.cdt.Activator;

public class Label implements ILightweightLabelDecorator, IColorProvider {

  private static final ImageDescriptor OKAY = Activator
      .getImageDescriptor("icons/module_okay.gif");
  private static final ImageDescriptor WARN = Activator
      .getImageDescriptor("icons/module_warning.gif");
  private static final ImageDescriptor ISSUE = Activator
      .getImageDescriptor("icons/module_issue.gif");

  @Override
  public void addListener(ILabelProviderListener listener) {}

  @Override
  public void dispose() {}

  @Override
  public boolean isLabelProperty(Object element, String property) {
    return false;
  }

  @Override
  public void removeListener(ILabelProviderListener listener) {}

  @Override
  public void decorate(Object element, IDecoration decoration) {
    String labTxt = "";
    IContainer f = (IContainer) element;
    if (f.findMember("./module.mk") != null) {
      labTxt += "M ";
      IResource c = f.findMember("./src");
      if (c != null) {
        try {
          IResource[] m = ((IContainer) c).members();
          int i = 2;
          for (IResource m_ : m) {
            if (m_ instanceof IFile && m_.getFileExtension().equals("c")) i--;
            if (i <= 0) break;
          }
          if (i > 0) {
            labTxt += "SPARSE, ";
            decoration.addOverlay(WARN,IDecoration.TOP_RIGHT);  
          }
        } catch (CoreException e) {
          e.printStackTrace();
        }
      }

      
      try {
        
        String folderName = f.getName();
        if (!folderName.equals("third.party")) {
                    
          IResource[] m = f.members();
          int fi = 6;
          int di = 0;
          for (IResource m_ : m) {
            if (m_ instanceof IFile && m_.getFileExtension().equals("h")) fi--;
            if (m_ instanceof IFolder &&
                (
                  !m_.getName().equals("src") && 
                  !m_.getName().equals("internal") && 
                  !m_.getName().equals("bin") &&
                  !m_.getName().equals("lib")
                )) di--;
          }
          if (di < 0) { 
            labTxt += "ILLEGAL-FOLDERS, ";
            decoration.addOverlay(ISSUE,IDecoration.TOP_RIGHT);          
          }
  
          if (fi < 0) { 
            labTxt += "VERBOSE-API, ";
            decoration.addOverlay(WARN,IDecoration.TOP_RIGHT);        
          }
        }
          
      } catch (CoreException e) {
        e.printStackTrace();
      }
      
      labTxt = "[" + labTxt.replaceFirst(",\\s$", "").trim() + "] ";
      decoration.addPrefix(labTxt);        
    } else if (f.findMember("./component.mk") != null) {
      labTxt += "C ";
      decoration.addPrefix("[" + labTxt.trim() + "] ");
    }
  }

  @Override
  public Color getForeground(Object element) {
    return Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE);
  }

  @Override
  public Color getBackground(Object element) {
    return Display.getDefault().getSystemColor(SWT.TRANSPARENT);
  }
}
