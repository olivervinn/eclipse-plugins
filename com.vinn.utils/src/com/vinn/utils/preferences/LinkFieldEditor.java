package com.vinn.utils.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * A field editor for displaying labels not associated with other widgets.
 */
public class LinkFieldEditor extends FieldEditor {

  private Label label;

  // All labels can use the same preference name since they don't
  // store any preference.
  public LinkFieldEditor(String beginValue, String link, String linkId, String endValue, Composite parent) {
    super("link", beginValue + link + endValue, parent);
  }

  // Adjusts the field editor to be displayed correctly
  // for the given number of columns.
  protected void adjustForNumColumns(int numColumns) {
    ((GridData) label.getLayoutData()).horizontalSpan = numColumns;
  }

  // Fills the field editor's controls into the given parent.
  protected void doFillIntoGrid(final Composite parent, int numColumns) {

    label = getLabelControl(parent);
       
    GridData gridData = new GridData();
    gridData.horizontalSpan = numColumns;
    gridData.horizontalAlignment = GridData.FILL;
    gridData.grabExcessHorizontalSpace = false;
    gridData.verticalAlignment = GridData.CENTER;
    gridData.grabExcessVerticalSpace = false;
    label.setLayoutData(gridData);
  }

  // Returns the number of controls in the field editor.
  public int getNumberOfControls() {
    return 1;
  }

  // Labels do not persist any preferences, so these methods are empty.
  protected void doLoad() {}

  protected void doLoadDefault() {}

  protected void doStore() {}
}
