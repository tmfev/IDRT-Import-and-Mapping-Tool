package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import routines.TalendDate;

import au.com.bytecode.opencsv.CSVWriter;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeTargetRootNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeSubNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProjects;
import de.umg.mi.idrt.ioe.misc.FileHandler;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.StatusView;
/**
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 */
public class SaveTarget extends AbstractHandler {

	CSVWriter writer = null;
	String targetID = "0";


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ActionCommand command0  = new ActionCommand(Resource.ID.Command.IOE.SAVETARGETPROJECT);
		Application.executeCommand(command0);

		Console.info("Saving Target Ontology.");

		String stringPath = FileHandler.getTempFilePath(Resource.Files.TEMP_TOS_CONNECTOR_FILE);
//		System.out.println("stringPath: " + stringPath);
		TargetProjects targetProjects = OntologyEditorView.getTargetProjects();

		if ( targetProjects != null && targetProjects.getSelectedTarget() != null ){
//			System.out.println(" *  targetProjects.getSelectedTarget():" + (  targetProjects.getSelectedTarget() == null ? "isNull" : "isNotNull" ));
			// System.out.println(" *  targetProjects.getSelectedTarget().getTargetID():" + (  targetProjects.getSelectedTarget().getTargetID()  ));

			targetID = String.valueOf( targetProjects.getSelectedTarget().getTargetID() );
		} else {
			Console.error("Coud not read a TargetID, so saving will be done with TargetID=0.");
		}

		// init

		try {
			OntologyTree ontologyTreeTarget = OntologyEditorView.getOntologyTargetTree();

			String[] fields = new String[21];

			fields[0] = Resource.I2B2.NODE.TARGET.TARGET_ID;
			fields[1] = Resource.I2B2.NODE.TARGET.TREE_LEVEL;
			fields[2] = Resource.I2B2.NODE.TARGET.TREE_PATH;
			fields[3] = Resource.I2B2.NODE.TARGET.STAGING_PATH;
			fields[4] = Resource.I2B2.NODE.TARGET.STAGING_DIMENSION;
			fields[5] = Resource.I2B2.NODE.TARGET.C_NAME;
			fields[6] = Resource.I2B2.NODE.TARGET.STARTDATE_STAGING_PATH;
			fields[7] = Resource.I2B2.NODE.TARGET.ENDDATE_STAGING_PATH;
			fields[8] = Resource.I2B2.NODE.TARGET.VISUALATTRIBUTES;

			//new table columns
			fields[9]  = Resource.I2B2.NODE.TARGET.C_BASECODE;
			fields[10] = Resource.I2B2.NODE.TARGET.C_METADATAXML;
			fields[11] = Resource.I2B2.NODE.TARGET.C_COLUMNDATATYPE;
			fields[12] = Resource.I2B2.NODE.TARGET.C_OPERATOR;
			fields[13] = Resource.I2B2.NODE.TARGET.C_COMMENT;
			fields[14] = Resource.I2B2.NODE.TARGET.C_TOOLTIP;
			fields[15] = Resource.I2B2.NODE.TARGET.UPDATE_DATE;
			fields[16] = Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE;
			fields[17] = Resource.I2B2.NODE.TARGET.IMPORT_DATE;
			fields[18] = Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD;
			fields[19] = Resource.I2B2.NODE.TARGET.VALUETYPE_CD;
			fields[20] = Resource.I2B2.NODE.TARGET.M_APPLIED_PATH;


			// TODO save target tmp file

			writer = new CSVWriter(new OutputStreamWriter(
					new FileOutputStream(stringPath), "UTF-8"),
					';');
			writer.writeNext(fields);
			writeNode(ontologyTreeTarget.getI2B2RootNode());
			writer.close();


		} catch (IOException e) {
			Console.error(e.toString());
		} catch (java.lang.NullPointerException e) {
			Console.error(
					"Coudn't save the Target-Tree, because nothing has been loaded so far.",
					e);
			StatusView.addMessage(
					new SystemMessage(
							"Coudn't save the Target-Tree, because nothing has been loaded so far. (I could also be wrong about that and just the saving failed to load the already active target ontology.)",
							SystemMessage.MessageType.ERROR));
			return null;
		}

		/*
		 * editorTargetView = (EditorTargetView) PlatformUI.getWorkbench()
		 * .getActiveWorkbenchWindow().getActivePage()
		 * .showView(Resource.ID.View.EDITOR_TARGET_VIEW);
		 */

		TOSConnector tos = new TOSConnector();

		TOSConnector.setContextVariable("Job", Resource.ID.Command.TOS.WRITE_TARGET_ONTOLOGY);
		//tos.setContextVariable("Var1", "1"); // target ontology id
		tos.setContextVariable("TargetID", targetID);
		tos.setContextVariable("DataFile", stringPath);

		try {
			tos.runJob();
			OntologyEditorView.setNotYetSaved(false);
		} catch (Exception e) {
			Console.error("Error while using a TOS-plugin with function writeTargetOntology(): "
					+ e.getMessage());
			return 1;
		}

		return null;
	}

	private void writeNode(OntologyTreeNode node) {

		for (OntologyTreeSubNode subNode : node.getTargetNodeAttributes().getSubNodeList()) {
			String[] fields = new String[21];

			fields[0] = targetID;
			if (!node.isModifier()){
				fields[1] = String.valueOf(subNode.getParent().getTreePathLevel());
			}
			else {
				fields[1] = String.valueOf(subNode.getParent().getTreePathLevel());	
			}

			if (subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH).equals("@"))
				fields[2] = subNode.getParent().getTreePath();
			else {
				String appliedPath = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get
						(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH);
//				System.out.println("APPLIEDPATH:_ "+appliedPath);
				String treePath = subNode.getParent().getTreePath();
//				System.out.println("TREEPATH:"  + treePath);
				fields[2] = treePath.substring(treePath.indexOf(appliedPath)+appliedPath.length()-1);	
			}
			fields[3] = subNode.getStagingPath();
			fields[4] = subNode.getParent().getTargetNodeAttributes().getDimension().toString();
			fields[5] = subNode.getParent().getTargetNodeAttributes().getName();
			fields[6] = subNode.getParent().getTargetNodeAttributes().getStartDateSource();
			fields[7] = subNode.getParent().getTargetNodeAttributes().getEndDateSource();
			fields[8] = subNode.getParent().getTargetNodeAttributes().getVisualattribute();


			String updateDate = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.UPDATE_DATE);
			if (updateDate!= null && updateDate.length()>0)
				updateDate = TalendDate.formatDate("dd-MM-yyyy", TalendDate.parseDate("yyyy-MM-dd hh:mm:ss", updateDate));

			String downloadDate = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE);
			if (downloadDate!= null && downloadDate.length()>0)
				downloadDate = TalendDate.formatDate("dd-MM-yyyy", TalendDate.parseDate("yyyy-MM-dd hh:mm:ss", downloadDate));

			String importDate = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.IMPORT_DATE);
			if (importDate!= null && importDate.length()>0)
				importDate = TalendDate.formatDate("dd-MM-yyyy", TalendDate.parseDate("yyyy-MM-dd hh:mm:ss", importDate));

			fields[9]  = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_BASECODE);
			fields[10] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_METADATAXML);
			fields[11] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COLUMNDATATYPE);
			fields[12] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_OPERATOR);
			fields[13] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COMMENT);
			fields[14] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_TOOLTIP);
			fields[15] = updateDate;
			fields[16] = downloadDate;
			fields[17] = importDate;
			fields[18] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD);
			fields[19] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.VALUETYPE_CD);
			fields[20] = subNode.getParent().getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH);
			writer.writeNext(fields);
		}

		for (OntologyTreeNode child : node.getChildren())
			writeNode(child);
	}
}
