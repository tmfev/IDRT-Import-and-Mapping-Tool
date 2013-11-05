package de.umg.mi.idrt.ioe.OntologyTree;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * Department of Medical Informatics Goettingen
 * www.mi.med.uni-goettingen.de
 */
public class TargetSubNodeAttributes {

	private String sourcePath;
	private String startDateSource;
	private String endDateSource;
	private OntologyTreeSubNode parent;


	public TargetSubNodeAttributes(OntologyTreeSubNode parent) {
		setParent(parent);
	}

	/**
	 * @return the endDateSource
	 */
	public String getEndDateSource() {
		return endDateSource;
	}

	public OntologyTreeSubNode getParent() {
		return parent;
	}

	/**
	 * @return the sourcePath
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * @return the startDateSource
	 */
	public String getStartDateSource() {
		return startDateSource;
	}

	/**
	 * @param endDateSource
	 *            the endDateSource to set
	 */
	public void setEndDateSourcePath(String endDateSource) {
		this.endDateSource = endDateSource;
	}



	public void setParent(OntologyTreeSubNode parent) {
		this.parent = parent;
	}



	/**
	 * @param startDateSource
	 *            the startDateSource to set
	 */
	public void setStartDateSourcePath(String startDateSource) {
		this.startDateSource = startDateSource;
	}

}
