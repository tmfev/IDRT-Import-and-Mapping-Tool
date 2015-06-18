package routines;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class IDRTItem {

	private String columnName;
	private String dataType;
	private String niceName;
	private String tooltip;

	public IDRTItem(String columnName, String dataType, String niceName) {
		setColumnName(columnName);
		setDataType(dataType);
		setNiceName(niceName);
	}

	public String getColumnName() {
		return columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public String getNiceName() {
		return niceName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setNiceName(String niceName) {
		this.niceName = niceName;
	}

	@Override
	public String toString() {
		return columnName + " " + niceName + " " + dataType;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

}