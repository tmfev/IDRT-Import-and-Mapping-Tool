package de.umg.mi.idrt.ioe.table;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;


public class MetadataEditingSupport extends EditingSupport {


private final TableViewer viewer;

  public MetadataEditingSupport(TableViewer viewer) {
    super(viewer);
    this.viewer = viewer;
  }

  @Override
  protected CellEditor getCellEditor(Object element) {
    return new TextCellEditor(viewer.getTable());
  }

  @Override
  protected boolean canEdit(Object element) {
    return true;
  }

  @Override
  protected Object getValue(Object element) {
	  return "TestReturn";
	  //return ((TableRow) element).getMetainfo();
  }

  @Override
  protected void setValue(Object element, Object value) {
    //((TableRow) element).setMetainfo(String.valueOf(value));
    //viewer.update(element, null);
	  viewer.update("test", null);
  }
} 