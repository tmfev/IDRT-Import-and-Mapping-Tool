package de.umg.mi.idrt.ioe.tos;

import java.util.Date;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.OntologyTree.MyOntologyTrees;
import de.umg.mi.idrt.ioe.OntologyTree.NodeType;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyItem;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyItemTarget;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeTargetRootNode;
import de.umg.mi.idrt.ioe.OntologyTree.Target;
import de.umg.mi.idrt.ioe.OntologyTree.TargetInstance;
import de.umg.mi.idrt.ioe.OntologyTree.TargetInstances;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.ProgressView;
import de.umg.mi.idrt.ioe.view.StatusView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         interface that enables a talend open studio job to interact with the
 *         ioe a dummy copy of this has to be imported into the tos job
 */

public class TOSHandler {

	static OntologyTree ontologyStagingTree = null;

	private static int counter=0;
	public static final String TableIEOTargetOntology = "ioe_target_ontology";
	public static final String TableIEOTarget = "ioe_target";
	public static final String TableIEOTargetProject = "ioe_target_project";

	public TOSHandler() {

		boolean nullError = false;

		// try to get the current ot from the view
		if (OntologyEditorView.getMyOntologyTree() == null) {
			nullError = true;
		} else if (OntologyEditorView.getOntologyStagingTree()
				== null) {
			nullError = true;
		}

		if (nullError == true) {
			Console.error("Could not find a working OntologyTree to add data.");
			return;
		}

		//MyOntologyTree _myOT = OntologyEditorView.getMyOntologyTree();
		ontologyStagingTree = OntologyEditorView.getOntologyStagingTree();
		/*
		 * _otCreator = OntologyEditorView.getI2b2ImportTool() .getOTCreator();
		 */
	}

	public static void status(String status) {
		Console.info("TOS: " + status);
	}

	public static void statusSuccess(String status) {
		StatusView
		.addMessage(
				new SystemMessage(status, SystemMessage.MessageType.SUCCESS));

	}

	public static void statusError(String status) {
		Console.error("TOS error:" + status);
		StatusView
		.addMessage(
				new SystemMessage(status, SystemMessage.MessageType.ERROR));
	}

	public static void addi2b2OntologyItemToTree(final int C_HLEVEL,
			final String C_FULLNAME,final String C_NAME,final String C_SYNONYM_CD,final
			String C_VISUALATTRIBUTES,final int C_TOTALNUM,final String C_BASECODE,final
			String C_METADATAXML,final String C_FACTTABLECOLUMN,final String C_TABLENAME,
			final String C_COLUMNNAME, final String C_COLUMNDATATYPE, final String C_OPERATOR,
			final String C_DIMCODE, final String C_COMMENT, final String C_TOOLTIP,
			final String M_APPLIED_PATH, final Date UPDATE_DATE, final Date DOWNLOAD_DATE,
			final Date IMPORT_DATE, final String SOURCESYSTEM_CD, final String VALUETYPE_CD,
			final String M_EXCLUSION_CD, final String C_PATH, final String C_SYMBOL, 
			final String SEC_OBJ) {
		setCounter(getCounter() + 1);
		if (getCounter()%1000==0)
			ProgressView.setProgress(0, "Importing...", getCounter()+"/??? items");
		OntologyItem item = new OntologyItem(C_HLEVEL, C_FULLNAME, C_NAME, C_SYNONYM_CD, 
				C_VISUALATTRIBUTES, C_TOTALNUM, C_BASECODE, C_METADATAXML, C_FACTTABLECOLUMN, 
				C_TABLENAME, C_COLUMNNAME, C_COLUMNDATATYPE, C_OPERATOR, C_DIMCODE, C_COMMENT, 
				C_TOOLTIP, M_APPLIED_PATH, UPDATE_DATE, DOWNLOAD_DATE, IMPORT_DATE, SOURCESYSTEM_CD, 
				VALUETYPE_CD, M_EXCLUSION_CD, C_PATH, C_SYMBOL,SEC_OBJ);

		if (ontologyStagingTree == null)
			ontologyStagingTree = OntologyEditorView.getOntologyStagingTree();
		if (C_HLEVEL == 0) {
			ontologyStagingTree.addNodeByPath(item.getC_FULLNAME(),item.getC_NAME(),Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE,item,NodeType.I2B2ROOT);
		}
		else {
			if (item.getM_APPLIED_PATH() == null){
				item.setM_APPLIED_PATH("@");
			}
			if (item.getM_APPLIED_PATH().equals("@"))
				ontologyStagingTree.addNodeByPath(item.getC_FULLNAME(), item.getC_NAME(),Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE,item,null);
			else {
				//				ontologyStagingTree.addNodeByPath(item.getC_FULLNAME(), item.getC_NAME(),Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE,item,null);
				ontologyStagingTree.addModifierNodeByPath(item, Resource.I2B2.NODE.TYPE.ONTOLOGY_SOURCE, null);
			}
		}
	}

	public void addi2b2ConceptDimensionItemToTree(boolean isConceptDimension,
			String dimensionPath, String dimensionCD, String nameChar,
			String dimensionBlob, Date updateDate, Date downloadDate,
			Date importDate, String sourcesysteCD, String uploadID) {
	}

	public static void addTargetOntologyItemToTree(
			int treeLevel, 
			String treePath,
			String stagingPath,
			String stagingDimension,
			String name,
			String startdateStagingPath,
			String enddateStagingPath,
			String visualattributes,
			String basecode,
			String metadataxml,
			String columndatatype,
			String c_operator,
			String c_comment,
			String tooltip,
			Date updateDate, 
			Date downloadDate,
			Date importDate,
			String sourceSystemCD,
			String valueTypeCD,
			String m_applied_path){
		//TODO FIX META ALWAYS NULL
		
		if (ontologyStagingTree == null)
			ontologyStagingTree = OntologyEditorView.getOntologyStagingTree();
		OntologyItemTarget item = new OntologyItemTarget(treeLevel, treePath, stagingPath, 
				stagingDimension, name, startdateStagingPath, enddateStagingPath, visualattributes, basecode,
				metadataxml,columndatatype,	c_operator,	c_comment,tooltip,updateDate, downloadDate,	importDate,
				sourceSystemCD,	valueTypeCD,m_applied_path);
		//		System.out.println("!!!adding: " + treePath);
		if (treeLevel!=0) {
			if ( item.getM_applied_path() == null || "@".equals(item.getM_applied_path())) {
				OntologyEditorView.getOntologyTargetTree().addTargetNodeByPath(item.getTreePath(),item.getName(),Resource.I2B2.NODE.TYPE.ONTOLOGY_TARGET,item,null);

			}
			else {
				OntologyEditorView.getOntologyTargetTree().addTargetModifierNodeByPath(item, Resource.I2B2.NODE.TYPE.ONTOLOGY_TARGET, null);
			}

		}
	}

	public void writeTargetOntology(int targetID, String treeNodePath,
			int treeLevel, String sourceNodePath, int changed,
			String startdateSourcePath, String enddateSourcePath) {
	}

	public static void  addTargetProjectToTargetProjects(int id,
			String name, String description) {

		TargetInstance targetProject = new TargetInstance();
		targetProject.setTargetProjectID(id);
		targetProject.setName(name);
		targetProject.setDescription(description);

		OntologyEditorView.getTargetInstance().add(targetProject);
	}

	public static void addTargetVersionToTargeProject(int targetID,
			int targetProjectID, int version, Date created, Date lastModified,
			String targetDBSchema) {

		Target target = new Target();
		target.setTargetID(targetID);
		target.setTargetProjectID(targetProjectID);
		target.setVersion(version);
		target.setCreated(created);
		target.setLastModified(lastModified);
		target.setTargetDBSchema(targetDBSchema);

		/*
		OntologyTreeTargetRootNode targetRootNode = new OntologyTreeTargetRootNode(
				"");

		targetRootNode = ((OntologyTreeTargetRootNode) OntologyEditorView.getOntologyTargetTree().getRootNode());

		TargetProjects targetProjects = OntologyEditorView.getTargetInstance();
		 */
		OntologyEditorView.getTargetInstance().addTarget(target);

	}

	public static void addIDsToSelectedTarget(int targetProjectID, int targetID, int version){		
		TargetInstances targetProjects = OntologyEditorView.getTargetInstance();

		targetProjects.getSelectedTarget().setTargetProjectID(targetProjectID);
		targetProjects.getSelectedTarget().setTargetID(targetID);
		targetProjects.getSelectedTarget().setVersion(version);
		targetProjects.getSelectedTargetInstance().setTargetProjectID(targetProjectID);

	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		TOSHandler.counter = counter;
	}

}
