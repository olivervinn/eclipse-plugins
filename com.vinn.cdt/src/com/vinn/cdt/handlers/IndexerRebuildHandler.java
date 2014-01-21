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

import com.vinn.cdt.Utils;

public class IndexerRebuildHandler extends AbstractHandler {

  public final static String ID = "com.vinn.cdt.indexer.rebuild"; //$NON-NLS-1$

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
