package de.umg.mi.idrt.ioe.commands.OntologyEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.TOSConnector;
import de.umg.mi.idrt.ioe.view.StatusView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         
 */

public class LoadStagingOntology extends AbstractHandler {

	public LoadStagingOntology() {
		super();
	}

	@SuppressWarnings("static-access")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Console.info("Command: LoadStagingOntology");

				TOSConnector tos = new TOSConnector();

				tos.setContextVariable(Resource.ID.TOS.ContextVariable.JOB,
						Resource.ID.Command.TOS.READ_STAGING_ONTOLOGY);

					tos.runJob();

		return null;
	}
}
