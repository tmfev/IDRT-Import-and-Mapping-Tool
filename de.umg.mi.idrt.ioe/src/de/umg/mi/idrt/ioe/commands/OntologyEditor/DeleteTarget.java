package de.umg.mi.idrt.ioe.commands.OntologyEditor;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.OntologyTree.Target;
import de.umg.mi.idrt.ioe.OntologyTree.TargetProject;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 */
public class DeleteTarget extends AbstractHandler {
	int targetID;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String targetID = event
				.getParameter(Resource.ID.Command.IEO.DELETETARGET_ATTRIBUTE_TARGETID);
		Console.info("Deleting Target Ontology with the TargetID="
				+ targetID
				+ " of the TargetProject with ID="
				+ OntologyEditorView.getTargetProjects()
						.getSelectedTargetProject().getTargetProjectID() + " ");
		// ActionCommand command = new
		// ActionCommand(Resource.ID.Command.IEO.DELETETARGET);
		// command.addParameter(Resource.ID.Command.IEO.DELETETARGET_ATTRIBUTE_TARGETID,
		// "");
		// Application.executeCommand(command);
		
		// check if there are more than one version available 
		if (OntologyEditorView.getTargetProjects().getSelectedTargetProject().getTargetsList().size() <= 1){
			String message = "There is only one target version existing in this target instance. Deleting not aborted.";
			Console.error(message);
			Application.getStatusView().addErrorMessage(message);
		}
		try {
			TOSConnector tos = new TOSConnector();
			TOSConnector.setContextVariable("Job",
					Resource.ID.Command.TOS.DELETE_TARGET);
			tos.setContextVariable("TargetID", targetID);
		} catch (Exception e) {
			Console.error("Could not delete the Target Ontology with the TargetID="
					+ targetID
					+ " of the TargetProject with ID="
					+ OntologyEditorView.getTargetProjects()
							.getSelectedTargetProject().getTargetProjectID()
					+ ". " + e.getMessage());
			return 1;
		}
		
		// deleting the target from the target projects object and the gui drop down menu
		Target oldTarget = OntologyEditorView.getTargetProjects().getTargetByID(Integer.valueOf( targetID ));
		OntologyEditorView.removeFromVersionCombo(String.valueOf( oldTarget.getVersion() ));
		OntologyEditorView.getTargetProjects().getSelectedTargetProject().removeTarget(oldTarget);
		
	
		return 0;
	}
}