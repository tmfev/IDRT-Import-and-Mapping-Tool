package de.umg.mi.idrt.ioe.OntologyTree;

	import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;


	public class TextEditingSupport extends EditingSupport {

	  private final TableViewer viewer;

	  public TextEditingSupport(TableViewer viewer) {
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
	    return ((OntologyTreeNode) element).getName();
	  }

	  @Override
	  protected void setValue(Object element, Object value) {
	    ((OntologyTreeNode) element).setName(String.valueOf(value));
	    viewer.update(element, null);
	  }
	} 