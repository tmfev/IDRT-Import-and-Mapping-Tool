package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

//import edu.goettingen.i2b2.importtool.view.EditorTargetInfoNode;

public class NameEditingSupport extends EditingSupport {

	private final TableViewer viewer;

	public NameEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable());
	}

	@Override
	  protected Object getValue(Object element) {
		  
		  
	    //return ((OTNode) element).getName();
		  return ((EditorTargetInfoNode) element).getValue();
	  }

	@Override
	protected void setValue(Object element, Object value) {
		//((OTNode) element).setName(String.valueOf(value));
		((EditorTargetInfoNode) element).setValue(String.valueOf(value));
		viewer.update(element, null);
	}
}
