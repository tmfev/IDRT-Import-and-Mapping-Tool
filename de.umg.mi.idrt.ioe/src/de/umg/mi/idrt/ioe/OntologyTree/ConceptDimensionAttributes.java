package de.umg.mi.idrt.ioe.OntologyTree;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         storage class adopting the i2b2 concept dimension
 * 
 */

public class ConceptDimensionAttributes {

	String CONCEPT_PATH;
	String CONCEPT_CD;
	String NAME_CHAR;
	String CONCEPT_BLOB;
	String C_VISUALATTRIBUTES;
	String UPDATE_DATE;
	String DOWNLOAD_DATE;
	String IMPORT_DATE;
	String SOURCESYSTEM_CD;
	String UPLOAD_ID;

	public ConceptDimensionAttributes() {

	}

	/**
	 * @return the c_VISUALATTRIBUTES
	 */
	public String getC_VISUALATTRIBUTES() {
		return C_VISUALATTRIBUTES;
	}

	/**
	 * @return the cONCEPT_BLOB
	 */
	public String getCONCEPT_BLOB() {
		return CONCEPT_BLOB;
	}

	/**
	 * @return the CONCEPT_CD
	 */
	public String getCONCEPT_CD() {
		return CONCEPT_CD;
	}

	/**
	 * @return the cONCEPT_PATH
	 */
	public String getCONCEPT_PATH() {
		return CONCEPT_PATH;
	}

	/**
	 * @return the dOWNLOAD_DATE
	 */
	public String getDOWNLOAD_DATE() {
		return DOWNLOAD_DATE;
	}

	/**
	 * @return the iMPORT_DATE
	 */
	public String getIMPORT_DATE() {
		return IMPORT_DATE;
	}

	/**
	 * @return the nAME_CHAR
	 */
	public String getNAME_CHAR() {
		return NAME_CHAR;
	}

	/**
	 * @return the sOURCESYSTEM_CD
	 */
	public String getSOURCESYSTEM_CD() {
		return SOURCESYSTEM_CD;
	}

	/**
	 * @return the uPDATE_DATE
	 */
	public String getUPDATE_DATE() {
		return UPDATE_DATE;
	}

	/**
	 * @return the uPLOAD_ID
	 */
	public String getUPLOAD_ID() {
		return UPLOAD_ID;
	}

	/**
	 * @param c_VISUALATTRIBUTES
	 *            the c_VISUALATTRIBUTES to set
	 */
	public void setC_VISUALATTRIBUTES(String c_VISUALATTRIBUTES) {
		C_VISUALATTRIBUTES = c_VISUALATTRIBUTES;
	}

	/**
	 * @param cONCEPT_BLOB
	 *            the cONCEPT_BLOB to set
	 */
	public void setCONCEPT_BLOB(String cONCEPT_BLOB) {
		CONCEPT_BLOB = cONCEPT_BLOB;
	}

	/**
	 * @param CONCEPT_CD
	 *            the CONCEPT_CD to set
	 */
	public void setCONCEPT_CD(String CONCEPT_CD) {
		CONCEPT_CD = CONCEPT_CD;
	}

	/**
	 * @param CONCEPT_PATH
	 *            the cONCEPT_PATH to set
	 */
	public void setCONCEPT_PATH(String cONCEPT_PATH) {
		CONCEPT_PATH = cONCEPT_PATH;
	}

	/**
	 * @param dOWNLOAD_DATE
	 *            the dOWNLOAD_DATE to set
	 */
	public void setDOWNLOAD_DATE(String dOWNLOAD_DATE) {
		DOWNLOAD_DATE = dOWNLOAD_DATE;
	}

	/**
	 * @param iMPORT_DATE
	 *            the iMPORT_DATE to set
	 */
	public void setIMPORT_DATE(String iMPORT_DATE) {
		IMPORT_DATE = iMPORT_DATE;
	}

	/**
	 * @param nAME_CHAR
	 *            the nAME_CHAR to set
	 */
	public void setNAME_CHAR(String nAME_CHAR) {
		NAME_CHAR = nAME_CHAR;
	}

	/**
	 * @param sOURCESYSTEM_CD
	 *            the sOURCESYSTEM_CD to set
	 */
	public void setSOURCESYSTEM_CD(String sOURCESYSTEM_CD) {
		SOURCESYSTEM_CD = sOURCESYSTEM_CD;
	}

	/**
	 * @param uPDATE_DATE
	 *            the uPDATE_DATE to set
	 */
	public void setUPDATE_DATE(String uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}

	/**
	 * @param uPLOAD_ID
	 *            the uPLOAD_ID to set
	 */
	public void setUPLOAD_ID(String uPLOAD_ID) {
		UPLOAD_ID = uPLOAD_ID;
	}

}
