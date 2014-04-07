package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeTargetRootNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.OntologyTree.Target;
import de.umg.mi.idrt.ioe.OntologyTree.TargetInstance;
import de.umg.mi.idrt.ioe.OntologyTree.TargetInstances;
import de.umg.mi.idrt.ioe.OntologyTree.TreeTargetContentProvider;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.StatusView;


public class LoadTargetOntology extends AbstractHandler {


	public LoadTargetOntology(){
		super();
	}



	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Target target;
		Console.info("Command: LoadTargetOntology");

		String version = event
				.getParameter(Resource.ID.Command.IOE.LOADTARGETONTOLOGY_ATTRIBUTE_VERSION);

		if ( version != null && !version.isEmpty() ){
			System.out.println("STRINGVERSION:" + version);
			target = OntologyEditorView.getTargetInstance().getTargetByVersion(Integer.valueOf( version ));
			if (target == null){
				Console.error("Coudn't find the target to load.");
				return null;
			} else {
				OntologyEditorView.getTargetInstance().setSelectedTarget(target);
			}
		}else
			target = OntologyEditorView.getTargetInstance().getSelectedTarget();

		TOSConnector tos = new TOSConnector();
		//Clears the TargetOntologyTree
		if (OntologyEditorView.getTargetTreeViewer()!=null) {
			OntologyEditorView.getOntologyTargetTree().getI2B2RootNode().removeFromParent();
			OntologyEditorView.getOntologyTargetTree().getI2B2RootNode().getChildren().clear();
			OntologyEditorView.getOntologyTargetTree()
			.getNodeLists().add(OntologyEditorView.getOntologyTargetTree().getI2B2RootNode());
//			for (OntologyTreeNode child : OntologyEditorView.getOntologyTargetTree().getI2B2RootNode().getChildren()) {
//				OntologyEditorView.getOntologyTargetTree().getI2B2RootNode().remove(child);
//			}

		}

		if ( target  == null ){
			Console.error("Can not load target ontology, because no target is selected or version found.");
			return null;
		}

		Console.info("Loading target ontology for TargetID=" + target.getTargetID() + " and Version=" + target.getVersion());


		try {
			tos.setContextVariable("Job", "LoadTargetOntology");
			tos.setContextVariable("TargetID", String.valueOf(target.getTargetID()));

			tos.runJob();
			if (OntologyEditorView.getTargetTreeViewer() != null)
				OntologyEditorView.getTargetTreeViewer().refresh();
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Error while using a TOS-plugin for job \"LoadTargetOntology\": "
					+ e.getMessage();
			Console.error(message);
			StatusView.addErrorMessage(message);


		}

		OntologyEditorView.refreshTargetVersionGUI();


		return null;
	}
}
