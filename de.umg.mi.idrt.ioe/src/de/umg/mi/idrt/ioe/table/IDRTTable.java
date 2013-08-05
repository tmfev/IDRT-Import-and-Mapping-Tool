package de.umg.mi.idrt.ioe.table;

import java.io.Serializable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class IDRTTable implements Serializable{

	private static Table table;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IDRTTable(Composite parent, int style) {
		table = new Table(parent, style);
	}

}
