package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;


public class etlStaginToTargetI2B2 extends AbstractHandler {

	
	public etlStaginToTargetI2B2(){
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		new TOSConnector();
		TOSConnector.uploadProject();
		
		return null;
	}
}
