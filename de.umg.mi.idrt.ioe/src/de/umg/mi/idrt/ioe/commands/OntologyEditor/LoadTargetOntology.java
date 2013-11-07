package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeTargetRootNode;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.OntologyTree.Target;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProject;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProjects;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;


public class LoadTargetOntology extends AbstractHandler {

	
	public LoadTargetOntology(){
		super();
	}



	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Console.info("Command: LoadTargetOntology");
		
		TOSConnector tos = new TOSConnector();
	
		TargetProjects targetProjects = OntologyEditorView.getTargetProjects();

		if (targetProjects.getSelectedTarget() == null || targetProjects.getSelectedTargetProject() == null ){
			Console.error("Can not load target ontology, because no target is selected.");
			return null;
		}
		
		Target target = targetProjects.getSelectedTarget();
		
		try {
			tos.setContextVariable("Job", "LoadTargetOntology");
			tos.setContextVariable("TargetID", String.valueOf(target.getTargetID()));
			//tos.setContextVariable("SQLTable", "I2B2");
			tos.runJob();
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Error while using a TOS-plugin for job \"LoadTargetOntology\": "
					+ e.getMessage();
			Console.error(message);
			Application.getStatusView().addErrorMessage(message);


		}
		
		
		
		return null;
	}
}
