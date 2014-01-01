package com.vinn.editors.resource;

import org.eclipse.ui.editors.text.TextEditor;

public class ResourceEditor extends TextEditor {

	private ColorManager colorManager;

	public ResourceEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
