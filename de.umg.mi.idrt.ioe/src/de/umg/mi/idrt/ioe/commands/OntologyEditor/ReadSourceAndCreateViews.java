package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTree;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeCreatorTOS;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeModel;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.view.EditorSourceInfoView;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class ReadSourceAndCreateViews extends AbstractHandler {

	private ExecutionEvent _event;

	public ReadSourceAndCreateViews() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Console.info("Loading the i2b2-Ontology-Editor.");

		System.out.println(ServerView.getCurrentSchema());
		Server stagingServer = ServerList.getTargetServers().get(ServerList.getUserServer().get(ServerView.getCurrentSchema()));
		
		stagingServer.setSchema(ServerView.getCurrentSchema());
		OntologyEditorView.setStagingServer(stagingServer);
		OntologyEditorView.setStagingSchemaName(ServerView.getCurrentSchema());
		// check if the ontology table has more than 1 item
		if (TOSConnector.checkOntology() == true) {
			Console.info("Ontology found in the i2b2 source table.");
		} else {
			String errorMessage = "No ontology found in the i2b2 source table. Abort loading the editor.";
			Console.error(errorMessage);
			Application.getStatusView().addErrorMessage(errorMessage);
			return null;
		}

		_event = event;

		// create the source i2b2-Ontology-Editor view
		try {

			// create secondary views
			EditorSourceInfoView editorSourceInfoView = (EditorSourceInfoView) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView(Resource.ID.View.EDITOR_SOURCE_INFO_VIEW);
			Activator.getDefault().getResource()
					.setEditorSourceInfoView(editorSourceInfoView);

			new OntologyTreeCreatorTOS(OntologyEditorView.getMyOntologyTree(), "");

			/*
			 * creator.createMeta(); creator.createOntology();
			 * creator.createPatientData();
			 */

			// editorSourceView.setComposite();
			OntologyEditorView.setSourceContent();
			OntologyTreeModel newTreeModel = new OntologyTreeModel(
					OntologyEditorView.getOntologyStagingTree().getTreeRoot());

			OntologyEditorView.getOntologyStagingTree()
					.setModel(newTreeModel);
			OntologyEditorView.getOntologyStagingTree()
					.updateUI();

			OntologyTree ontologySourceTree = OntologyEditorView.getOntologyStagingTree();

			if (ontologySourceTree != null) {

			}

			OntologyTreeModel newTreeModel2 = new OntologyTreeModel(
					OntologyEditorView.getOntologyTargetTree().getTreeRoot());

			OntologyEditorView.getOntologyTargetTree()
					.setModel(newTreeModel2);
			OntologyEditorView.getOntologyTargetTree()
					.updateUI();

			OntologyTree ontologyTargetTree = OntologyEditorView.getOntologyTargetTree();

			// TODO Ontology Editor
			OntologyEditorView.setTargetContent(ontologyTargetTree);
			EditorTargetInfoView editorTargetInfoView = (EditorTargetInfoView) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView(Resource.ID.View.EDITOR_TARGET_INFO_VIEW);
			Activator.getDefault().getResource()
					.setEditorTargetInfoView(editorTargetInfoView);

			
			Application.getStatusView().addMessage(
					"The i2b2 staging project has been loaded.");
			
		} catch (PartInitException e) {
			Console.error(e.toString());
			e.printStackTrace();
		}

		return null;
	}
}
