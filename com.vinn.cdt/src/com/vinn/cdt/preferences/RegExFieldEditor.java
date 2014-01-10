package com.vinn.cdt.preferences;

import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.text.FindReplaceDocumentAdapterContentProposalProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

/**
 * A field editor for adding separator to a preference page.
 */
public class RegExFieldEditor extends StringFieldEditor {
  ContentAssistCommandAdapter fContentAssistFindField;
  ComboContentAdapter contentAdapter;

  public RegExFieldEditor(String pStringConfSelectorRegex, String string,
      Composite fieldEditorParent) {
    super(pStringConfSelectorRegex, string, fieldEditorParent);
  }

  // Fills the field editor's controls into the given parent.
  protected void doFillIntoGrid(Composite parent, int numColumns) {
    super.doFillIntoGrid(parent, numColumns);

    // Create the find content assist field
    contentAdapter = new ComboContentAdapter();
    FindReplaceDocumentAdapterContentProposalProvider findProposer =
        new FindReplaceDocumentAdapterContentProposalProvider(true);
    Text tControl = getTextControl();
    // fFindField = new Combo(panel, SWT.DROP_DOWN | SWT.BORDER);
    fContentAssistFindField =
        new ContentAssistCommandAdapter(tControl, contentAdapter, findProposer,
            ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, new char[0], true);
    // setGridData(fFindField, SWT.FILL, true, SWT.CENTER, false);
    addDecorationMargin(tControl);
  }

  private void addDecorationMargin(Control control) {
    Object layoutData = control.getLayoutData();
    if (!(layoutData instanceof GridData)) return;
    GridData gd = (GridData) layoutData;
    FieldDecoration dec =
        FieldDecorationRegistry.getDefault().getFieldDecoration(
            FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
    gd.horizontalIndent = dec.getImage().getBounds().width;
  }
}
