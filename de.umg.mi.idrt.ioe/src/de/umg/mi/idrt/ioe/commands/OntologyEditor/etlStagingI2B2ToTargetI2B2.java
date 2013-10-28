package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;


public class etlStagingI2B2ToTargetI2B2 extends AbstractHandler {

	
	public etlStagingI2B2ToTargetI2B2(){
		super();
	}

	private ExecutionEvent event;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Console.info("ETL StagingI2B2 to TargetI2B2.");
		
		TOSConnector tos = new TOSConnector();
		

		try {

			tos.setContextVariable("Job", Resource.ID.Command.IEO.ETLSTAGINGI2B2TOTARGETI2B2);
			//tos.setContextVariable("SQLTable", "I2B2");
	
			tos.runJob();

		} catch (Exception e) {
			Console.error(e);
			String message = "Error while using a TOS-plugin with function ETL StagingI2B2 to TargetI2B2: "
					+ e.getMessage();
			Application.getStatusView().addErrorMessage(message);

		}
		
		
		
		return null;
	}
}
