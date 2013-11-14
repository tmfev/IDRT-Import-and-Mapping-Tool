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
		
		Target targetOld;
		
		String version = event
				.getParameter(Resource.ID.Command.IOE.DELETETARGET_ATTRIBUTE_TARGETID);

		if ( version != null && !version.isEmpty() ){
			
			targetOld = OntologyEditorView.getTargetProjects().getTargetByVersion(Integer.valueOf( version ));
			if (targetOld == null){
				Console.error("Coudn't find the target for which a deletion was desired.");
				return null;
			} else {
				//OntologyEditorView.getTargetProjects().setSelectedTarget(targetOld);
			}
		}else
			targetOld = OntologyEditorView.getTargetProjects().getSelectedTarget();

		
		Console.info("Deleting Target Ontology with the TargetID="
				+ targetOld.getTargetID() + " and Version=" + targetOld.getVersion()
				+ " of the TargetProject with ID="
				+ OntologyEditorView.getTargetProjects()
						.getSelectedTargetProject().getTargetProjectID() + " ");
		
		// check if there are more than one version available 
		if (OntologyEditorView.getTargetProjects().getSelectedTargetProject().getTargetsList().size() <= 1){
			String message = "There is only one target version existing in this target instance. Deleting not aborted.";
			Console.error(message);
			StatusView.addErrorMessage(message);
		}
		try {
			TOSConnector tos = new TOSConnector();
			TOSConnector.setContextVariable("Job",
					Resource.ID.Command.TOS.DELETE_TARGET);
			tos.setContextVariable("TargetID", String.valueOf(targetOld.getTargetID()));
			
			tos.runJob();
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
		//Target oldTarget = OntologyEditorView.getTargetProjects().getTargetByID(Integer.valueOf( targetID ));
		OntologyEditorView.removeVersionFromCombo(String.valueOf( targetOld.getVersion() ));
		OntologyEditorView.getTargetProjects().getSelectedTargetProject().removeTarget(targetOld);
		
	
		return 0;
	}
}