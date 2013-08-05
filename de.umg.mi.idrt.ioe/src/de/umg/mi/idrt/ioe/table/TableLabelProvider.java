package de.umg.mi.idrt.ioe.table;
import org.eclipse.jface.viewers.ColumnLabelProvider;


public class TableLabelProvider extends ColumnLabelProvider {


	
@Override
public String getText(Object element) {
	TableRow p = (TableRow) element;
	return p.getDatatype();
}
	
} 
