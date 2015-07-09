package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         
 */

public class LoadEverything extends AbstractHandler {
	public LoadEverything() {
		super();
	}
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Console.info("Command: LoadTargetProjects");

		Application.executeCommand(new ActionCommand(
				Resource.ID.Command.IOE.LOADTARGETPROJECTS));

		Application.executeCommand(new ActionCommand(
				Resource.ID.Command.IOE.LOADSTAGINGONTOLOGY));

		Application.executeCommand(new ActionCommand(
				Resource.ID.Command.IOE.LOADTARGETONTOLOGY));
		return null;
	}
}
