package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProjects;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.StatusView;

/**
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 *         
 *			
 */

public class IncrementTargetVersion extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		TargetProjects targetProjects = OntologyEditorView.getTargetProjects();

		// save the old target project version
		// if ( OntologyEditorView.isNotYetSaved() ) {
		// Application.executeCommand(new
		// ActionCommand(Resource.ID.Command.IEO.SAVETARGETPROJECT));
		// }

		// save ontology as the old target version
		// Application.executeCommand(new
		// ActionCommand(Resource.ID.Command.IEO.SAVETARGET));

		targetProjects.setSelectedTarget(targetProjects
				.incrementVersion(targetProjects.getSelectedTarget()));

		// save the new target project version
		Application.executeCommand(new ActionCommand(
				Resource.ID.Command.IOE.SAVETARGETPROJECT));

		// save ontology as the new target version
		Application.executeCommand(Resource.ID.Command.IOE.SAVETARGET);

		StatusView
				.addMessage("A new version of this i2b2 staging project has been created.");

		OntologyEditorView.refreshTargetVersionGUI();
		return null;
	}
}