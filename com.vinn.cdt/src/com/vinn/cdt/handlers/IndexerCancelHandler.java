/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.cdt.handlers;

import org.eclipse.cdt.internal.core.CCoreInternals;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

@SuppressWarnings("restriction")
public class IndexerCancelHandler extends AbstractHandler {

  public static final String ID = "com.vinn.cdt.indexer.cancel"; //$NON-NLS-1$

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    CCoreInternals.getPDOMManager().shutdown();
    CCoreInternals.getPDOMManager().startup();
    return null;
  }
}
