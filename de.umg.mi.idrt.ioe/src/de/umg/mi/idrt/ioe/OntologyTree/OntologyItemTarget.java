package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.Date;

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
	private String basecode;
	private String metadataxml;
	private String columndatatype;
	private String c_operator;
	private String c_comment;
	private String tooltip;
	private Date updateDate;
	private Date downloadDate;
	private Date importDate;
	private String sourceSystemCD;
	private String valueTypeCD;
	private String m_applied_path;
	private String staging_m_applied_path;


	public OntologyItemTarget(int treeLevel, 
			String treePath,
			String stagingPath,
			String stagingDimension,
			String name,
			String startdateStagingPath,
			String enddateStagingPath,
			String visualattributes, 
			//NEW
			String basecode, String metadataxml, String columndatatype, 
			String c_operator, String c_comment, String tooltip, Date updateDate, Date downloadDate, 
			Date importDate, String sourceSystemCD, String valueTypeCD, String m_applied_path, String staging_m_applied_path) {

		this.treeLevel=treeLevel;
		this.treePath=treePath;
		this.stagingPath=stagingPath;
		this.stagingDimension=stagingDimension;
		this.name=name;
		this.startdateStagingPath=startdateStagingPath;
		this.enddateStagingPath=enddateStagingPath;
		this.visualattributes=visualattributes;

		//NEW
		this.basecode = basecode;
		this.metadataxml=metadataxml;
		this.columndatatype=columndatatype;
		this.c_operator=c_operator;
		this.c_comment=c_comment;
		this.tooltip=tooltip;
		this.updateDate=updateDate;
		this.downloadDate=downloadDate;
		this.importDate=importDate;
		this.sourceSystemCD=sourceSystemCD;
		this.valueTypeCD=valueTypeCD;
		this.m_applied_path=m_applied_path;
		this.setStaging_m_applied_path(staging_m_applied_path);
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

	/**
	 * @return the basecode
	 */
	public String getBasecode() {
		return basecode;
	}

	/**
	 * @param basecode the basecode to set
	 */
	public void setBasecode(String basecode) {
		this.basecode = basecode;
	}

	/**
	 * @return the metadataxml
	 */
	public String getMetadataxml() {
		return metadataxml;
	}

	/**
	 * @param metadataxml the metadataxml to set
	 */
	public void setMetadataxml(String metadataxml) {
		this.metadataxml = metadataxml;
	}

	/**
	 * @return the columndatatype
	 */
	public String getColumndatatype() {
		return columndatatype;
	}

	/**
	 * @param columndatatype the columndatatype to set
	 */
	public void setColumndatatype(String columndatatype) {
		this.columndatatype = columndatatype;
	}

	/**
	 * @return the c_operator
	 */
	public String getC_operator() {
		return c_operator;
	}

	/**
	 * @param c_operator the c_operator to set
	 */
	public void setC_operator(String c_operator) {
		this.c_operator = c_operator;
	}

	/**
	 * @return the c_comment
	 */
	public String getC_comment() {
		return c_comment;
	}

	/**
	 * @param c_comment the c_comment to set
	 */
	public void setC_comment(String c_comment) {
		this.c_comment = c_comment;
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param tooltip the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the downloadDate
	 */
	public Date getDownloadDate() {
		return downloadDate;
	}

	/**
	 * @param downloadDate the downloadDate to set
	 */
	public void setDownloadDate(Date downloadDate) {
		this.downloadDate = downloadDate;
	}

	/**
	 * @return the importDate
	 */
	public Date getImportDate() {
		return importDate;
	}

	/**
	 * @param importDate the importDate to set
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	/**
	 * @return the sourceSystemCD
	 */
	public String getSourceSystemCD() {
		return sourceSystemCD;
	}

	/**
	 * @param sourceSystemCD the sourceSystemCD to set
	 */
	public void setSourceSystemCD(String sourceSystemCD) {
		this.sourceSystemCD = sourceSystemCD;
	}

	/**
	 * @return the valueTypeCD
	 */
	public String getValueTypeCD() {
		return valueTypeCD;
	}

	/**
	 * @param valueTypeCD the valueTypeCD to set
	 */
	public void setValueTypeCD(String valueTypeCD) {
		this.valueTypeCD = valueTypeCD;
	}

	/**
	 * @return the m_applied_path
	 */
	public String getM_applied_path() {
		return m_applied_path;
	}

	/**
	 * @param m_applied_path the m_applied_path to set
	 */
	public void setM_applied_path(String m_applied_path) {
		this.m_applied_path = m_applied_path;
	}

	/**
	 * @return
	 */
	public String getUpdateDateAsString() {
		return updateDate!=null?updateDate.toString():"";
	}

	/**
	 * @return
	 */
	public String getImportDateAsString() {
		return importDate!=null?importDate.toString():"";
	}

	/**
	 * @return
	 */
	public String getDownloadDateAsString() {
		return downloadDate!=null?downloadDate.toString():"";
	}

	public String getStaging_m_applied_path() {
		return staging_m_applied_path;
	}

	public void setStaging_m_applied_path(String staging_m_applied_path) {
		this.staging_m_applied_path = staging_m_applied_path;
	}

}
