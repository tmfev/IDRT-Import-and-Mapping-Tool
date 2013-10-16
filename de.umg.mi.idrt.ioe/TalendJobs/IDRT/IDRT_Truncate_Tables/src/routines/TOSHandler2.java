package routines;

import java.util.Date;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         interface for providing ieo functions to a TOS job
 * 
 */

public class TOSHandler2 {

	public void status(String status) {
		// implement in main program
		System.out.println("TOS: " + status);
	}

	public void statusSuccess(String status) {
		// implement in main program
		System.out.println("TOS Success: " + status);
	}

	public void statusError(String status) {
		// implement in main program
		System.out.println("TOS Error: " + status);
	}

	public void addi2b2OntologyItemToTree(int C_HLEVEL, String C_FULLNAME, String C_NAME,
			String C_SYNONYM_CD, String C_VISUALATTRIBUTES, int C_TOTALNUM,
			String C_BASECODE, String C_METADATAXML, String C_FACTTABLECOLUMN,
			String C_TABLENAME, String C_COLUMNNAME, String C_COLUMNDATATYPE,
			String C_OPERATOR, String C_DIMCODE, String C_COMMENT,
			String C_TOOLTIP, String M_APPLIED_PATH, Date UPDATE_DATE,
			Date DOWNLOAD_DATE, Date IMPORT_DATE, String SOURCESYSTEM_CD,
			String VALUETYPE_CD, String M_EXCLUSION_CD, String C_PATH,
			String C_SYMBOL) {
		// implement in main program
	}

	public void addi2b2ConceptDimensionItemToTree(boolean isConceptDimension,
			String dimensionPath, String dimensionCD, String nameChar,
			String dimensionBlob, Date updateDate, Date downloadDate,
			Date importDate, String sourcesysteCD, String uploadID) {
		// implement in main program
	}

	public void getTargetProjects() {
		// implement in main program
	}

	public void getTargetProjectsVersions(int targetProjectID) {
		// implement in main program
	}

	public void addTargetProject(String projectName) {
		// implement in main program
	}

	public void addTargetProjectVersion(int targetProjectID) {
		// implement in main program
	}

	public void getTargetOntology(int targetID) {
		// implement in main program
	}

	public void saveTargetOntology(int targetID, int target_h_level,
			String target_c_fullname, String source_node_path,
			String startDateSourcePath, String endDateSourcePath) {
		// implement in main program
	}

	public void readTargetOntology(int treeLevel, String treePath,
			String sourcePath, String name, int changed,
			String startdateSourcePath, String enddateSourcePath,
			String visualattributes) {
		// implement in main program
	}

	public void writeTargetOntology(int targetID, String treeNodePath,
			int treeLevel, String sourceNodePath, int changed,
			String startdateSourcePath, String enddateSourcePath) {
		// implement in main program
	}

}
