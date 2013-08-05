package de.umg.mi.idrt.ioe.OntologyTree;

public class TargetNodeAttributes {

	String sourcePath = "";
	String name = "";
	String nodeType = "";
	String startDateSource = "";
	String endDateSource = "";
	String visualattribute = "";
	
	boolean changed = false;
	/**
	 * @return the sourcePath
	 */
	public String getSourcePath() {
		return sourcePath;
	}
	/**
	 * @param sourcePath the sourcePath to set
	 */
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the nodeType
	 */
	public String getNodeType() {
		return nodeType;
	}
	/**
	 * @param nodeType the nodeType to set
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	/**
	 * @return the startDateSource
	 */
	public String getStartDateSource() {
		return startDateSource;
	}
	/**
	 * @param startDateSource the startDateSource to set
	 */
	public void setStartDateSourcePath(String startDateSource) {
		this.startDateSource = startDateSource;
	}
	/**
	 * @return the endDateSource
	 */
	public String getEndDateSource() {
		return endDateSource;
	}
	/**
	 * @param endDateSource the endDateSource to set
	 */
	public void setEndDateSourcePath(String endDateSource) {
		this.endDateSource = endDateSource;
	}
	/**
	 * @return the visualattribute
	 */
	public String getVisualattribute() {
		return visualattribute;
	}
	/**
	 * @param visualattribute the visualattribute to set
	 */
	public void setVisualattributes(String visualattribute) {
		this.visualattribute = visualattribute;
	}
	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}
	/**
	 * @param changed the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	public String getChanged(){
		return changed ? "0" : "1";
	}

}
