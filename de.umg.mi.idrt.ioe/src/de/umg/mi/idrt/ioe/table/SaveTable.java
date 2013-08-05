package de.umg.mi.idrt.ioe.table;

import java.io.Serializable;
import java.util.List;

public class SaveTable implements Serializable{
	private static final long serialVersionUID = 1L;
	private static List<List<String>> tableItems;
	
	
	public SaveTable(List<List<String>> itemList) {
		tableItems = itemList;
	}


	public List<List<String>> getTableItems() {
		return tableItems;
	}


	public static void setTableItems(List<List<String>> tableItems) {
		SaveTable.tableItems = tableItems;
	}
	
}
