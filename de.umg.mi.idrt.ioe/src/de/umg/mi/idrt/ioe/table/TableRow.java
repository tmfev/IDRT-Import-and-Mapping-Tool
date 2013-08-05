package de.umg.mi.idrt.ioe.table;

import java.util.List;

public class TableRow {

	private String title;
	private List<String> rowContent;
	private String datatype;
	private String metainfo;
	public TableRow(String title, List<String> rowContent, String datatype, String metainfo) {
		this.setTitle(title);
		this.setRowContent(rowContent);
		this.setDatatype(datatype);
		this.setMetainfo(metainfo);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getRowContent() {
		return rowContent;
	}

	public void setRowContent(List<String> rowContent) {
		this.rowContent = rowContent;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getMetainfo() {
		return metainfo;
	}

	public void setMetainfo(String metainfo) {
		this.metainfo = metainfo;
	}



}
