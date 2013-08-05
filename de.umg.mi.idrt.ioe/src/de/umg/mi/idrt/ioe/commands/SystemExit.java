package de.umg.mi.idrt.ioe.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.umg.mi.idrt.ioe.Debug;



public class SystemExit extends AbstractHandler {

	public SystemExit(){
		super();
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerUtil.getActiveWorkbenchWindow(event).close();
		Debug.d("Workbench closed");
		System.exit(0);
		return null;
	}


}
