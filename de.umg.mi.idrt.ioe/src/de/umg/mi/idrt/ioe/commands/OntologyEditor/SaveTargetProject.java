package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import au.com.bytecode.opencsv.CSVWriter;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.FileHandling;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeTargetRootNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.OntologyTree.Target;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProject;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         
  */

public class SaveTargetProject extends AbstractHandler {

	CSVWriter _writer = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Console.info("Saving TargetProject ...");
		
		String tempFilePath = FileHandling.getTempFilePath(Resource.Files.TEMP_TOS_CONNECTOR_FILE);

		// init
		OntologyTree ontologyTreeTarget = null;

		try {
			// TODO save target tmp file

			_writer = new CSVWriter(new OutputStreamWriter(
                    new FileOutputStream(tempFilePath), "UTF-8"),
                    ';');
			
			String[] fields = new String[6];

			fields[0] = "TARGETPROJECT_ID";
			fields[1] = "TARGET_ID";
			fields[2] = "NAME";
			fields[3] = "DESCRIPTION";
			fields[4] = "VERSION";
			fields[5] = "TARGET_DB_SCHEMA";
			_writer.writeNext(fields);
			
			
			
			
			OntologyTreeTargetRootNode targetTreeRoot = ((OntologyTreeTargetRootNode)OntologyEditorView.getI2b2ImportTool().getMyOntologyTrees().getOntologyTreeTarget().getTreeRoot());
			
			TargetProject targetProject = targetTreeRoot.getTargetProjects().getSelectedTargetProject();
			Target target = null;
			
			if (targetProject == null || targetTreeRoot.getTargetProjects().getSelectedTarget() == null ){
				Console.error("No TargetInstance and/or TargetVersion available to save.");
				return null;
			} else {
				target = targetTreeRoot.getTargetProjects().getSelectedTarget();
			}
			
			System.out.println("selected Target to save:");
			System.out.println(" - " + String.valueOf(targetProject.getTargetProjectID()));
			System.out.println(" - " + String.valueOf(target.getTargetID()));
			System.out.println(" - " + targetProject.getName());
			System.out.println(" - " + target.getTargetDBSchema());
			
			fields = new String[6];
			
			fields[0] = String.valueOf(targetProject.getTargetProjectID());
			fields[1] = String.valueOf(target.getTargetID());
			fields[2] = targetProject.getName();
			fields[3] = targetProject.getDescription();
			fields[4] = String.valueOf(target.getVersion());
			fields[5] = target.getTargetDBSchema();

			_writer.writeNext(fields);
			
			
			_writer.close();
			
			
		} catch (IOException e) {
			Console.error(e.toString());
		}

		TOSConnector tos = new TOSConnector();

		TOSConnector.setContextVariable("Job", "SaveTargetProjects");
		//tos.setContextVariable("Var1", "1"); // target project id
		//tos.setContextVariable("Var2", "1"); // target id
		tos.setContextVariable("DataFile", tempFilePath);

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

	/*
	private void writeNode(OntologyTreeNode node) {

		String[] fields = new String[9];
		fields[0] = "1";
		fields[1] = String.valueOf(node.getTreePathLevel());
		fields[2] = node.getTreePath();
		fields[3] = node.getTargetNodeAttributes().getSourcePath();
		fields[4] = node.getTargetNodeAttributes().getName();
		fields[5] = node.getTargetNodeAttributes().getChanged();
		fields[6] = node.getTargetNodeAttributes().getStartDateSource();
		fields[7] = node.getTargetNodeAttributes().getEndDateSource();
		fields[8] = node.getTargetNodeAttributes().getVisualattribute();

		_writer.writeNext(fields);

		for (int x = 0; x < node.getChildCount(); x++) {
			writeNode((OntologyTreeNode) node.getChildAt(x));
		}

	}
	*/

}
