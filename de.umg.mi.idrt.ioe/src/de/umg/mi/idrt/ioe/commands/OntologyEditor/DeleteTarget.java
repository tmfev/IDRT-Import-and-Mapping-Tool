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
import de.umg.mi.idrt.ioe.OntologyTree.TargetInstance;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;
import de.umg.mi.idrt.ioe.view.StatusView;
/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 */
public class DeleteTarget extends AbstractHandler {
	int targetID;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Target targetToDelete;
		String message;
		
		String version = event
				.getParameter(Resource.ID.Command.IOE.DELETETARGET_ATTRIBUTE_TARGETID);

		if ( version != null && !version.isEmpty() ){
			
			targetToDelete = OntologyEditorView.getTargetInstance().getTargetByVersion(Integer.valueOf( version ));
			if (targetToDelete == null){
				message = "Coudn't find the target for which a deletion was desired.";
				Console.error(message);
				StatusView.addErrorMessage(message);
				return 1;
			}
		}else {
			targetToDelete = OntologyEditorView.getTargetInstance().getSelectedTarget();
		}
		
		Console.info("Deleting Target Ontology with the TargetID="
				+ targetToDelete.getTargetID() + " and Version=" + targetToDelete.getVersion()
				+ " of the TargetProject with ID="
				+ OntologyEditorView.getTargetInstance()
						.getSelectedTargetInstance().getTargetInstanceID() + " ");
		
		// check if there are more than one version available 
		if (OntologyEditorView.getTargetInstance().getSelectedTargetInstance().getTargetsList().size() <= 1){
			message = "There is only one target version existing in this target instance. Deleting not aborted.";
			Console.error(message);
			StatusView.addErrorMessage(message);
			return 1;
		}
		
		
		try {
			TOSConnector tos = new TOSConnector();
			TOSConnector.setContextVariable("Job",
					Resource.ID.Command.TOS.DELETE_TARGET);
			tos.setContextVariable("TargetID", String.valueOf(targetToDelete.getTargetID()));
			
			tos.runJob();
		} catch (Exception e) {
			message = ("Could not delete the Target Ontology with the TargetID="
					+ targetID
					+ " of the TargetProject with ID="
					+ OntologyEditorView.getTargetInstance()
							.getSelectedTargetInstance().getTargetInstanceID()
					+ ". " + e.getMessage());
			Console.error(message);
			StatusView.addErrorMessage(message);
			return 1;
		}
		
		// deleting the target from the target projects object and the gui drop down menu
		//Target oldTarget = OntologyEditorView.getTargetProjects().getTargetByID(Integer.valueOf( targetID ));
		
		OntologyEditorView.getTargetInstance().getSelectedTargetInstance().removeTarget(targetToDelete);
		
		// GUI stuff
		//OntologyEditorView.removeVersionFromCombo(String.valueOf( targetToDelete.getVersion() ));
		/*
		if ( OntologyEditorView.getTargetProjects().getSelectedTarget() == targetToDelete  ){
			OntologyEditorView.getTargetProjects().setSelectedTarget(OntologyEditorView.getTargetProjects().getSelectedTargetProject().getHighestTarget());
		}
		*/
		
		StatusView.addSuccsessMessage("Version " + targetToDelete.getVersion() + " was deleted.");
	
		return 0;
	}
}