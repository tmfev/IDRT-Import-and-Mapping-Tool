package de.umg.mi.idrt.ioe.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.umg.mi.idrt.ioe.Console;

public class SystemExit extends AbstractHandler {

	//TODO maybe delete? is it used?
	
	public SystemExit() {
		super();
		Console.info("SuperExit!");

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerUtil.getActiveWorkbenchWindow(event).close();
		Console.info("Workbench closed");
		System.exit(0);
		return null;
	}

}
