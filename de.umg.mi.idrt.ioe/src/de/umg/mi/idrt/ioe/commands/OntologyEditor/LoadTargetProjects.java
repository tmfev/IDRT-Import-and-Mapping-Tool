package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;


public class LoadTargetProjects extends AbstractHandler {

	
	public LoadTargetProjects(){
		super();
	}



	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//String tmpDataFile = "C:/I2B2ImportProject/tmp_targettreesave.csv";

		Console.info("Command: LoadTargetProjects");
		
		TOSConnector tos = new TOSConnector();
		
	

		try {

			tos.setContextVariable("Job", "loadTargetProjects");
			//tos.setContextVariable("SQLTable", "I2B2");
	
			tos.runJob();

		} catch (Exception e) {
			String message = "Error while using a TOS-plugin for job \"loadTargetProjects\": "
					+ e.getMessage();
			Console.error(message);
			Application.getStatusView().addErrorMessage(message);


		}
		
		
		
		return null;
	}
}
