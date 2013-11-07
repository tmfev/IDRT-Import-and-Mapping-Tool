package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.File;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;


/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 */

public class OntologyTreeCreatorTOS {

	String _filename = "";

	File _ontologyFile = null;


	public OntologyTreeCreatorTOS ( MyOntologyTrees myOT, String filename ){
		_filename = filename;
		System.out.println("OTCreatorTOS");
		
		// loading the ieo target project into the target ontology tree
		ActionCommand command = new ActionCommand(
				Resource.ID.Command.IEO.LOADTARGETPROJECTS);
		Application.executeCommand(command);
		

		// loading the staging i2b2 ontology
		try {
			// create a new db connector via TOS
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("STARTING tos.getOntology();");
					// TODO Auto-generated method stub
					TOSConnector tos = new TOSConnector();
					tos.getOntology();
					
					System.out.println("FINISHED tos.getOntology();");
				}
			}).run();
			
			// loading the target ontology from the selected target
			try {

				ActionCommand command2 = new ActionCommand(
						Resource.ID.Command.IEO.LOADTARGETONTOLOGY);
				Application.executeCommand(command2);

			} catch ( Exception e ) {
				Console.error("Could not load the target ontology.", e);
			}

		} catch ( Exception e ) {
			Console.error("", e);
		}

	}



}
