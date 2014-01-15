package com.vinn.rtc.startup;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.filesystem.rcp.core.internal.FileSystemResourcesPlugin;
import com.ibm.team.filesystem.rcp.core.internal.changes.model.IComponentSyncModel;
import com.ibm.team.filesystem.rcp.core.internal.changes.model.OfflineManager;

public class PeriodicRefreshPendingChanges implements IStartup {

  private Job job = null;
  final int INTERVAL = 5 * 60 * 1000;

  @Override
  public void earlyStartup() {
    job = new Job("Jazz Remote Change Checker (5 min)") {
      protected IStatus run(IProgressMonitor monitor) {
        try {
          OfflineManager.getInstance().requestUpdate();
          IComponentSyncModel model = FileSystemResourcesPlugin.getComponentSyncModel();
          model.refresh(true, monitor);
        } catch (TeamRepositoryException e) {
          e.printStackTrace();
        }
        schedule(INTERVAL);
        return Status.OK_STATUS;
      }
    };
    job.schedule(INTERVAL);
  }
}
