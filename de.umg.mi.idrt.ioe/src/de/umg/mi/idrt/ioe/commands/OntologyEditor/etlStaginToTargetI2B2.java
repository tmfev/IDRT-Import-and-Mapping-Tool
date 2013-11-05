package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;


public class etlStaginToTargetI2B2 extends AbstractHandler {

	
	private ExecutionEvent event;

	public etlStaginToTargetI2B2(){
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		TOSConnector tos = new TOSConnector();
		tos.uploadProject();
//		try {
//			tos.setContextVariable("Job", Resource.ID.Command.IEO.ETLSTAGINGI2B2TOTARGETI2B2);
//			//tos.setContextVariable("SQLTable", "I2B2");
//	
//			tos.runJob();
//
//		} catch (Exception e) {
//			String message = "Error while using a TOS-plugin with function getOntology(): "
//					+ e.getMessage();
//			Console.error(message);
//			Application.getStatusView().addErrorMessage(message);
//			Console.info("TOS-Error2: ");
//
//		}
		
		
		
		return null;
	}
}
