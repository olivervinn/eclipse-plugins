/*
 * Copyright Oliver Vinn 2013
 */

package com.vinn.build.handlers;

import org.eclipse.cdt.internal.core.CCoreInternals;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

@SuppressWarnings("restriction")
public class CancelIndexerHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {   
    CCoreInternals.getPDOMManager().shutdown();
    CCoreInternals.getPDOMManager().startup();
    return null;
  }
}
