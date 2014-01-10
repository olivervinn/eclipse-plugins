/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.vinn.cdt.build.Utils;

public class RebuildIndexerHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Job job = new Job("Rebuild Index For All Projects") {
      @Override
      protected IStatus run(IProgressMonitor monitor) {
        Utils.rebuildCIndex();
        monitor.done();
        return Status.OK_STATUS;
      }
    };
    job.setUser(true);
    job.schedule();
    return null;
  }
}
