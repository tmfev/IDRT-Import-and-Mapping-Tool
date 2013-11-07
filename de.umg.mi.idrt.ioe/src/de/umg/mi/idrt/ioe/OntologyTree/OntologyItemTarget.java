package de.umg.mi.idrt.ioe.OntologyTree;
/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class OntologyItemTarget {
	private int treeLevel;
	private String treePath;
	private String stagingPath;
	private String stagingDimension;
	private String name;
	private String startdateStagingPath;
	private String enddateStagingPath;
	private String visualattributes;
	
	public OntologyItemTarget(int treeLevel, 
			String treePath,
			String stagingPath,
			String stagingDimension,
			String name,
			String startdateStagingPath,
			String enddateStagingPath,
			String visualattributes) {
		this.treeLevel=treeLevel;
		this.treePath=treePath;
		this.stagingPath=stagingPath;
		this.stagingDimension=stagingDimension;
		this.name=name;
		this.startdateStagingPath=startdateStagingPath;
		this.enddateStagingPath=enddateStagingPath;
		this.visualattributes=visualattributes;
	}

	/**
	 * @return the treeLevel
	 */
	public int getTreeLevel() {
		return treeLevel;
	}

	/**
	 * @param treeLevel the treeLevel to set
	 */
	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}

	/**
	 * @return the treePath
	 */
	public String getTreePath() {
		return treePath;
	}

	/**
	 * @param treePath the treePath to set
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * @return the stagingPath
	 */
	public String getStagingPath() {
		return stagingPath;
	}

	/**
	 * @param stagingPath the stagingPath to set
	 */
	public void setStagingPath(String stagingPath) {
		this.stagingPath = stagingPath;
	}

	/**
	 * @return the stagingDimension
	 */
	public String getStagingDimension() {
		return stagingDimension;
	}

	/**
	 * @param stagingDimension the stagingDimension to set
	 */
	public void setStagingDimension(String stagingDimension) {
		this.stagingDimension = stagingDimension;
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
	 * @return the startdateStagingPath
	 */
	public String getStartdateStagingPath() {
		return startdateStagingPath;
	}

	/**
	 * @param startdateStagingPath the startdateStagingPath to set
	 */
	public void setStartdateStagingPath(String startdateStagingPath) {
		this.startdateStagingPath = startdateStagingPath;
	}

	/**
	 * @return the enddateStagingPath
	 */
	public String getEnddateStagingPath() {
		return enddateStagingPath;
	}

	/**
	 * @param enddateStagingPath the enddateStagingPath to set
	 */
	public void setEnddateStagingPath(String enddateStagingPath) {
		this.enddateStagingPath = enddateStagingPath;
	}

	/**
	 * @return the visualattributes
	 */
	public String getVisualattributes() {
		return visualattributes;
	}

	/**
	 * @param visualattributes the visualattributes to set
	 */
	public void setVisualattributes(String visualattributes) {
		this.visualattributes = visualattributes;
	}
}
