package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.File;
import java.io.InputStream;

import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.OntologyItem;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.tos.TOSHandler;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;


/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         sdf
 * 
 */

public class OntologyTreeCreatorTOS extends OntologyTreeCreator {

	String _filename = "";

	File _ontologyFile = null;


	public OntologyTreeCreatorTOS ( MyOntologyTrees myOT, String filename ){
		super(myOT);
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
					
//					Server server = ServerList.getTargetServers().get(ServerView.getSelectedServer());
//					server.getOntology(ServerView.getCurrentSchema());
//					int counter = 0;
//					long time = System.currentTimeMillis();
//					for (OntologyItem ont : Server.getOntology()) {
//						counter++;
//						if (counter%1000==0) {
//							System.out.println(System.currentTimeMillis()-time + "ms " + counter + " nodes added!");
//						time = System.currentTimeMillis();	
//						}
////						System.out.println("ADDING: " +ont.getC_FULLNAME());
//						TOSHandler.addi2b2OntologyItemToTree(ont);
//					}
//					OntologyEditorView.getStagingTreeViewer().refresh();
					
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
