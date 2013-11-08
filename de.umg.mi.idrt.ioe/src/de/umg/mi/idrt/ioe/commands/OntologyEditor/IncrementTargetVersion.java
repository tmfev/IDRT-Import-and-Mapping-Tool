package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeTargetRootNode;
import de.umg.mi.idrt.ioe.OntologyTree.Target;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProjects;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class IncrementTargetVersion extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		TargetProjects targetProjects = OntologyEditorView.getTargetProjects();
				
		// save the old target project version
		/*
		if ( OntologyEditorView.isNotYetSaved() ) {
			ActionCommand command0  = new ActionCommand(Resource.ID.Command.IEO.SAVETARGETPROJECT);
			Application.executeCommand(command0);
		}
		*/
		
		// save ontology as the old target version
		ActionCommand command1  = new ActionCommand(Resource.ID.Command.IEO.SAVETARGET);
		Application.executeCommand(command1);
				
		Target newTarget = targetProjects.incrementVersion(targetProjects.getSelectedTarget());
		
		targetProjects.setSelectedTarget(newTarget);
		
		
		// save the new target project version
		ActionCommand command2  = new ActionCommand(Resource.ID.Command.IEO.SAVETARGETPROJECT);
		Application.executeCommand(command2);
		
		
		
			
		
		// save ontology as the new target version
		ActionCommand command3  = new ActionCommand(Resource.ID.Command.IEO.SAVETARGET);
		Application.executeCommand(command3);
		
		Application.getStatusView().addMessage(
				"A new version of this i2b2 staging project has been created.");
		
		OntologyEditorView.refreshVersionCombo();
		return null;
	}
}