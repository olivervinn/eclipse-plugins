package com.vinn.cdt.ui;

import java.util.HashMap;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

public class ConfContributionItem extends ContributionItem {

  private IResource mValue;

  public ConfContributionItem() {}

  public ConfContributionItem(String id) {
    super(id);
  }

  public ConfContributionItem(String id, IResource value) {
    super(id);
    mValue = value;
  }

  @Override
  public void fill(Menu menu, int index) {

    MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);

    if (mValue == null) {
      menuItem.setText("None");
      menuItem.setEnabled(false);
    } else {
      menuItem.setText(mValue.getProjectRelativePath().toOSString());
      menuItem.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          IHandlerService handlerService =
              (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
          IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
          ICommandService cmdService = (ICommandService) window.getService(ICommandService.class);
          HashMap<String, IResource> m = new HashMap<String, IResource>();
          m.put("com.vinn.build.commandParameter1", mValue);
          
          Command cmd = cmdService.getCommand("com.vinn.build.commands.applyconfig");
          ParameterizedCommand pcmd = ParameterizedCommand.generateCommand(cmd, m);
          
          try {
            handlerService.executeCommand(pcmd, null);
            
          } catch (ExecutionException | NotDefinedException | NotEnabledException
              | NotHandledException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        };
      });
    }
  }
}
