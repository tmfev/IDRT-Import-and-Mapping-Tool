package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.I2B2ImportTool;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeCreatorTOS;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeModel;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.view.EditorSourceInfoView;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class ReadSourceAndCreateViews extends AbstractHandler {

	public ReadSourceAndCreateViews() {
		super();
	}

	private ExecutionEvent _event;

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Debug.d("Loading the i2b2-Ontology-Editor.");
		Console.info("Loading the i2b2-Ontology-Editor.");
		// init
//		EditorSourceView editorSourceView = null;
//		EditorTargetView editorTargetView = null;

		// creating new main tool
		I2B2ImportTool i2b2ImportTool = new I2B2ImportTool(null);

		Debug.dd("I2B2ImportTool isNull1?:"
				+ (i2b2ImportTool == null ? "true" : "false"));

	
		// check if the ontology table has more than 1 item
		if (TOSConnector.checkOntology() == true) {
			Console.info("Ontology found in the i2b2 source table.");
		} else {
			Console.error("Console: No ontology found in the i2b2 source table. Abort loading the editor.");
			Application
					.getStatusView()
					.addErrorMessage(
							"No ontology found in the i2b2 source table. Abort loading the editor.");
			return null;
		}

		_event = event;

		// create the source i2b2-Ontology-Editor view
		try {
			OntologyEditorView.setI2B2ImportTool(i2b2ImportTool);

			// create secondary views
			EditorSourceInfoView editorSourceInfoView = (EditorSourceInfoView) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView(Resource.ID.View.EDITOR_SOURCE_INFO_VIEW);
			Activator.getDefault().getResource()
					.setEditorSourceInfoView(editorSourceInfoView);

			new OntologyTreeCreatorTOS(
					i2b2ImportTool.getMyOntologyTrees(), "");

			/*
			creator.createMeta();
			creator.createOntology();
			creator.createPatientData();
			*/

//			editorSourceView.setComposite();
			OntologyEditorView.setSourceContent();
			OntologyTreeModel newTreeModel = new OntologyTreeModel(
					i2b2ImportTool.getMyOntologyTrees().getTreeRoot());

			i2b2ImportTool.getMyOntologyTrees().getOntologyTreeSource()
					.setModel(newTreeModel);
			i2b2ImportTool.getMyOntologyTrees().getOntologyTreeSource()
					.updateUI();

			OntologyTree OT = i2b2ImportTool.getMyOntologyTrees()
					.getOntologyTreeSource();

			if (OT != null) {
				

			}

			OntologyTreeModel newTreeModel2 = new OntologyTreeModel(
					i2b2ImportTool.getMyOntologyTrees().getTargetTreeRoot());

			i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget()
					.setModel(newTreeModel2);
			i2b2ImportTool.getMyOntologyTrees().getOntologyTreeTarget()
					.updateUI();

			OntologyTree OTTarget = i2b2ImportTool.getMyOntologyTrees()
					.getOntologyTreeTarget();


//TODO Ontology Editor
			OntologyEditorView.setTargetContent(OTTarget);
			EditorTargetInfoView editorTargetInfoView = (EditorTargetInfoView) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView(Resource.ID.View.EDITOR_TARGET_INFO_VIEW);
			Activator.getDefault().getResource()
					.setEditorTargetInfoView(editorTargetInfoView);

		} catch (PartInitException e) {
			Console.error(e.toString());
			e.printStackTrace();
		}

		return null;
	}
}
