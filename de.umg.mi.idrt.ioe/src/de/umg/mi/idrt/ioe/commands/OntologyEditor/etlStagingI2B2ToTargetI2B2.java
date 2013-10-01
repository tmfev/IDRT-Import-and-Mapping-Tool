package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import tos.tosidrtconnector_0_4.TOSIDRTConnector;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;


public class etlStagingI2B2ToTargetI2B2 extends AbstractHandler {

	
	public etlStagingI2B2ToTargetI2B2(){
		super();
	}

	private ExecutionEvent _event;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//String tmpDataFile = "C:/I2B2ImportProject/tmp_targettreesave.csv";

		Console.info("Saving Target Ontology.");
		
		TOSConnector tos = new TOSConnector();
		
		
		Console.info("TOSConnector: getOntology()");

		try {

			tos.setContextVariable("Job", Resource.ID.Command.IEO.ETLSTAGINGI2B2TOTARGETI2B2);
			//tos.setContextVariable("SQLTable", "I2B2");
	
			tos.runJob();

		} catch (Exception e) {
			String message = "Error while using a TOS-plugin with function getOntology(): "
					+ e.getMessage();
			Console.error(message);
			Application.getStatusView().addErrorMessage(message);
			Console.info("TOS-Error2: ");

		}
		
		
		
		return null;
	}
}
