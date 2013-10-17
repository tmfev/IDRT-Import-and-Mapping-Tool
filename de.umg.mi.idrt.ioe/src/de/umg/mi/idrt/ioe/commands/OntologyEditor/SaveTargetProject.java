package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.SystemMessage;
import de.umg.mi.idrt.ioe.OntologyTree.FileHandling;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import au.com.bytecode.opencsv.CSVWriter;

public class SaveTargetProject extends AbstractHandler {

	CSVWriter _writer = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Console.info("Saving TargetProject ...");
		
		String tempFilePath = FileHandling.getTempFilePath(Resource.Files.TEMP_TOS_CONNECTOR_FILE);

		// init
		OntologyTree ontologyTreeTarget = null;

		try {
			ontologyTreeTarget = OntologyEditorView.getI2b2ImportTool()
					.getMyOntologyTrees().getOntologyTreeTarget();
		} catch (java.lang.NullPointerException e) {
			Console.error(
					"Coudn't save the Target Project, because nothing has been loaded so far.",
					e);
			Application
					.getStatusView()
					.addMessage(
							new SystemMessage(
									"Coudn't save the Target Project, because nothing has been loaded so far. (I could also be wrong about that and just the saving failed to load the already active target ontology.)",
									SystemMessage.MessageType.ERROR));
			return null;
		}

		String[] fields = new String[9];

		fields[0] = Resource.I2B2.NODE.TARGET.TARGET_ID;
		fields[1] = Resource.I2B2.NODE.TARGET.TREE_LEVEL;
		fields[2] = Resource.I2B2.NODE.TARGET.TREE_PATH;
		fields[3] = Resource.I2B2.NODE.TARGET.SOURCE_PATH;
		fields[4] = Resource.I2B2.NODE.TARGET.NAME;
		fields[5] = Resource.I2B2.NODE.TARGET.CHANGED;
		fields[6] = Resource.I2B2.NODE.TARGET.STARTDATE_SOURCE_PATH;
		fields[7] = Resource.I2B2.NODE.TARGET.ENDDATE_SOURCE_PATH;
		fields[8] = Resource.I2B2.NODE.TARGET.VISUALATTRIBUTE;

		try {
			// TODO save target tmp file

			CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                    new FileOutputStream(tempFilePath), "UTF-8"),
                    ';', '\'');

			_writer.writeNext(fields);
			writeNode((OntologyTreeNode) ((OntologyTreeNode) ontologyTreeTarget
					.getTreeRoot().getFirstChild()));
			_writer.close();
			
			
		} catch (IOException e) {
			Console.error(e.toString());
		}

		TOSConnector tos = new TOSConnector();

		TOSConnector.setContextVariable("Job", "save_target_project");
		tos.setContextVariable("Var1", "1"); // target project id
		tos.setContextVariable("Var2", "1"); // target id
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

}
