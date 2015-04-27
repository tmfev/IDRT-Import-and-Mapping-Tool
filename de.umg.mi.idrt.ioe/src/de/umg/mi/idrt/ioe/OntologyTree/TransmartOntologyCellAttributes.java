package de.umg.mi.idrt.ioe.OntologyTree;

import java.util.Date;
import java.util.HashMap;

public class TransmartOntologyCellAttributes {
	
	private HashMap<String, String> ontologyTable = new HashMap<String, String>();

	public HashMap<String, String> getOntologyTable() {
		return ontologyTable;
	}

	int C_HLEVEL;
	int C_TOTALNUM;
	String C_METADATAXML;
	String C_COMMENT;
	Date UPDATE_DATE;
	Date DOWNLOAD_DATE;
	Date IMPORT_DATE;
	
	public TransmartOntologyCellAttributes (){
		
	}
	
	
	/**
	 * @return the c_BASECODE
	 */
	public String getC_BASECODE() {
		return ontologyTable.get("c_basecode");
//		return C_BASECODE;
	}

	/**
	 * @return the c_COLUMNDATATYPE
	 */
	public String getC_COLUMNDATATYPE() {
		return ontologyTable.get("C_COLUMNDATATYPE".toLowerCase());
	}

	/**
	 * @return the c_COLUMNNAME
	 */
	public String getC_COLUMNNAME() {
//		return C_COLUMNNAME;
		return ontologyTable.get("C_COLUMNNAME".toLowerCase());
//		return map.set("c_columnname");
	}

	/**
	 * @return the c_COMMENT
	 */
	public String getC_COMMENT() {
		return ontologyTable.get("C_COMMENT".toLowerCase());
//		return C_COMMENT;
	}

	/**
	 * @return the c_DIMCODE
	 */
	public String getC_DIMCODE() {
		return ontologyTable.get("C_DIMCODE".toLowerCase());
	}

	/**
	 * @return the c_FACTTABLECOLUMN
	 */
	public String getC_FACTTABLECOLUMN() {
		return ontologyTable.get("C_FACTTABLECOLUMN".toLowerCase());
	}

	/**
	 * @return the c_FULLNAME
	 */
	public String getC_FULLNAME() {
		return ontologyTable.get("C_FULLNAME".toLowerCase());
	}

	/**
	 * @return the c_HLEVEL
	 */
	public int getC_HLEVEL() {
		return C_HLEVEL;
	}

	/**
	 * @return the c_METADATAXML
	 */
	public String getC_METADATAXML() {
		return C_METADATAXML;
	}

	/**
	 * @return the c_NAME
	 */
	public String getC_NAME() {
		return ontologyTable.get("C_NAME".toLowerCase());
	}

	/**
	 * @return the c_OPERATOR
	 */
	public String getC_OPERATOR() {
		return ontologyTable.get("C_COMMENT".toLowerCase());
	}

	/**
	 * @return the c_PATH
	 */
	public String getC_PATH() {
		return ontologyTable.get("C_PATH".toLowerCase());
	}

	/**
	 * @return the c_SYMBOL
	 */
	public String getC_SYMBOL() {
		return ontologyTable.get("C_SYMBOL".toLowerCase());
	}

	/**
	 * @return the c_SYNONYM_CD
	 */
	public String getC_SYNONYM_CD() {
		return ontologyTable.get("C_SYNONYM_CD".toLowerCase());
	}

	/**
	 * @return the c_TABLENAME
	 */
	public String getC_TABLENAME() {
		return ontologyTable.get("C_TABLENAME".toLowerCase());
	}

	/**
	 * @return the c_TOOLTIP
	 */
	public String getC_TOOLTIP() {
		return ontologyTable.get("C_TOOLTIP".toLowerCase());
	}

	/**
	 * @return the c_TOTALNUM
	 */
	public int getC_TOTALNUM() {
		return C_TOTALNUM;
	}

	/**
	 * @return the c_VISUALATTRIBUTES
	 */
	public String getC_VISUALATTRIBUTES() {
		return ontologyTable.get("C_VISUALATTRIBUTES".toLowerCase());
	}

	/**
	 * @return the dOWNLOAD_DATE
	 */
	public Date getDOWNLOAD_DATE() {
		return DOWNLOAD_DATE;
	}
	
	public String getDOWNLOAD_DATEAsString() {
		return DOWNLOAD_DATE!=null?DOWNLOAD_DATE.toString():"";
	}

	/**
	 * @return the iMPORT_DATE
	 */
	public Date getIMPORT_DATE() {
		return IMPORT_DATE;
	}
	public String getIMPORT_DATEAsString() {
		return IMPORT_DATE!=null?IMPORT_DATE.toString():"";
	}

	/**
	 * @return the m_APPLIED_PATH
	 */
	public String getM_APPLIED_PATH() {
		return ontologyTable.get("M_APPLIED_PATH".toLowerCase());
	}

	/**
	 * @return the m_EXCLUSION_CD
	 */
	public String getM_EXCLUSION_CD() {
		return ontologyTable.get("M_EXCLUSION_CD".toLowerCase());
	}

	/**
	 * @return the sOURCESYSTEM_CD
	 */
	public String getSOURCESYSTEM_CD() {
		return ontologyTable.get("SOURCESYSTEM_CD".toLowerCase());
	}

	/**
	 * @return the uPDATE_DATE
	 */
	public Date getUPDATE_DATE() {
		return UPDATE_DATE;
	}
	public String getUPDATE_DATEAsString() {
		return UPDATE_DATE!=null?UPDATE_DATE.toString():"";
	}

	/**
	 * @return the vALUETYPE_CD
	 */
	public String getVALUETYPE_CD() {
		return ontologyTable.get("VALUETYPE_CD".toLowerCase());
	}
	

	public String getSEC_OBJ() {
		return ontologyTable.get("SEC_OBJ");
	}
	
	public String setSEC_OBJ(String sec_obj) {
		return ontologyTable.put("SEC_OBJ",sec_obj);
	}

	/**
	 * @param c_BASECODE the c_BASECODE to set
	 */
	public void setC_BASECODE(String c_BASECODE) {
		ontologyTable.put("c_basecode", c_BASECODE);
	}

	/**
	 * @param c_COLUMNDATATYPE the c_COLUMNDATATYPE to set
	 */
	public void setC_COLUMNDATATYPE(String c_COLUMNDATATYPE) {
		ontologyTable.put("c_COLUMNDATATYPE".toLowerCase(), c_COLUMNDATATYPE);
//		C_COLUMNDATATYPE = c_COLUMNDATATYPE;
	}

	/**
	 * @param c_COLUMNNAME the c_COLUMNNAME to set
	 */
	public void setC_COLUMNNAME(String c_COLUMNNAME) {
		ontologyTable.put("c_COLUMNNAME".toLowerCase(), c_COLUMNNAME);
	}

	/**
	 * @param c_COMMENT the c_COMMENT to set
	 */
	public void setC_COMMENT(Object c_COMMENT) {
		ontologyTable.put("c_COMMENT".toLowerCase(), (String)c_COMMENT);
	}

	/**
	 * @param c_DIMCODE the c_DIMCODE to set
	 */
	public void setC_DIMCODE(String c_DIMCODE) {
		ontologyTable.put("c_DIMCODE".toLowerCase(), c_DIMCODE);
	}

	/**
	 * @param c_FACTTABLECOLUMN the c_FACTTABLECOLUMN to set
	 */
	public void setC_FACTTABLECOLUMN(String c_FACTTABLECOLUMN) {
		ontologyTable.put("c_FACTTABLECOLUMN".toLowerCase(), c_FACTTABLECOLUMN);
	}

	/**
	 * @param c_FULLNAME the c_FULLNAME to set
	 */
	public void setC_FULLNAME(String c_FULLNAME) {
		ontologyTable.put("c_FULLNAME".toLowerCase(), c_FULLNAME);
	}

	/**
	 * @param c_HLEVEL the c_HLEVEL to set
	 */
	public void setC_HLEVEL(int c_HLEVEL) {
		C_HLEVEL = c_HLEVEL;
	}

	/**
	 * @param c_METADATAXML the c_METADATAXML to set
	 */
	public void setC_METADATAXML(String c_METADATAXML) {
		C_METADATAXML = c_METADATAXML;
	}

	/**
	 * @param c_NAME the c_NAME to set
	 */
	public void setC_NAME(String c_NAME) {
		ontologyTable.put("c_NAME".toLowerCase(), c_NAME);
	}

	/**
	 * @param c_OPERATOR the c_OPERATOR to set
	 */
	public void setC_OPERATOR(String c_OPERATOR) {
		ontologyTable.put("c_OPERATOR".toLowerCase(), c_OPERATOR);
	}

	/**
	 * @param c_PATH the c_PATH to set
	 */
	public void setC_PATH(String c_PATH) {
		ontologyTable.put("c_PATH".toLowerCase(), c_PATH);
	}

	/**
	 * @param c_SYMBOL the c_SYMBOL to set
	 */
	public void setC_SYMBOL(String c_SYMBOL) {
		ontologyTable.put("c_SYMBOL".toLowerCase(), c_SYMBOL);
	}

	/**
	 * @param c_SYNONYM_CD the c_SYNONYM_CD to set
	 */
	public void setC_SYNONYM_CD(String c_SYNONYM_CD) {
		ontologyTable.put("c_SYNONYM_CD".toLowerCase(), c_SYNONYM_CD);
	}

	/**
	 * @param c_TABLENAME the c_TABLENAME to set
	 */
	public void setC_TABLENAME(String c_TABLENAME) {
		ontologyTable.put("c_TABLENAME".toLowerCase(), c_TABLENAME);
	}

	/**
	 * @param c_TOOLTIP the c_TOOLTIP to set
	 */
	public void setC_TOOLTIP(String c_TOOLTIP) {
		ontologyTable.put("c_TOOLTIP".toLowerCase(), c_TOOLTIP);
	}

	/**
	 * @param c_TOTALNUM the c_TOTALNUM to set
	 */
	public void setC_TOTALNUM(int c_TOTALNUM) {
		C_TOTALNUM = c_TOTALNUM;
	}

	/**
	 * @param c_VISUALATTRIBUTES the c_VISUALATTRIBUTES to set
	 */
	public void setC_VISUALATTRIBUTES(String c_VISUALATTRIBUTES) {
		ontologyTable.put("c_VISUALATTRIBUTES".toLowerCase(), c_VISUALATTRIBUTES);
	}

	/**
	 * @param dOWNLOAD_DATE the dOWNLOAD_DATE to set
	 */
	public void setDOWNLOAD_DATE(Date dOWNLOAD_DATE) {
		DOWNLOAD_DATE = dOWNLOAD_DATE;
	}

	/**
	 * @param iMPORT_DATE the iMPORT_DATE to set
	 */
	public void setIMPORT_DATE(Date iMPORT_DATE) {
		IMPORT_DATE = iMPORT_DATE;
	}

	/**
	 * @param m_APPLIED_PATH the m_APPLIED_PATH to set
	 */
	public void setM_APPLIED_PATH(String m_APPLIED_PATH) {
		ontologyTable.put("m_APPLIED_PATH".toLowerCase(), m_APPLIED_PATH);
	}

	/**
	 * @param m_EXCLUSION_CD the m_EXCLUSION_CD to set
	 */
	public void setM_EXCLUSION_CD(String m_EXCLUSION_CD) {
		ontologyTable.put("m_EXCLUSION_CD".toLowerCase(), m_EXCLUSION_CD);
	}

	/**
	 * @param sOURCESYSTEM_CD the sOURCESYSTEM_CD to set
	 */
	public void setSOURCESYSTEM_CD(String sOURCESYSTEM_CD) {
		ontologyTable.put("sOURCESYSTEM_CD".toLowerCase(), sOURCESYSTEM_CD);
	}

	/**
	 * @param uPDATE_DATE the uPDATE_DATE to set
	 */
	public void setUPDATE_DATE(Date uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}

	/**
	 * @param vALUETYPE_CD the vALUETYPE_CD to set
	 */
	public void setVALUETYPE_CD(String vALUETYPE_CD) {
		ontologyTable.put("vALUETYPE_CD".toLowerCase(), vALUETYPE_CD);
	}




	
}
