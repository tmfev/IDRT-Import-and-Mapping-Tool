package de.umg.mi.idrt.idrtimporttool.table;

import java.util.List;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class TableRow {

	private String title;
	private List<String> rowContent;
	private String datatype;
	private String metainfo;

	public TableRow(String title, List<String> rowContent, String datatype,
			String metainfo) {
		setTitle(title);
		setRowContent(rowContent);
		setDatatype(datatype);
		setMetainfo(metainfo);
	}

	public String getDatatype() {
		return datatype;
	}

	public String getMetainfo() {
		return metainfo;
	}

	public List<String> getRowContent() {
		return rowContent;
	}

	public String getTitle() {
		return title;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public void setMetainfo(String metainfo) {
		this.metainfo = metainfo;
	}

	public void setRowContent(List<String> rowContent) {
		this.rowContent = rowContent;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
