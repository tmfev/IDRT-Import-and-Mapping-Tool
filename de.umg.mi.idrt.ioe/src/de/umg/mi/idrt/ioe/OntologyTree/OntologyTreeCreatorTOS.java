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

	private InputStream _ontologyStream;
	private InputStream _metaStream;
	private InputStream _patientStream;
	private InputStream _patientDataStream;

	public OntologyTreeCreatorTOS ( MyOntologyTree myOT, String filename ){
		super(myOT);
		_filename = filename;
		System.out.println("OTCreatorTOS");
		
		
		// loading the ieo target project into the target ontology tree
		ActionCommand command = new ActionCommand(
				Resource.ID.Command.IEO.LOADTARGETPROJECTS);
		Application.executeCommand(command);
		

		try {
			// create a new db connector via TOS
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("STARTING tos.getOntology();");
					// TODO Auto-generated method stub
//					TOSConnector tos = new TOSConnector();
//					tos.getOntology();
					
					Server server = ServerList.getTargetServers().get(ServerView.getSelectedServer());
					server.getOntology(ServerView.getCurrentSchema());
					int counter = 0;
					long time = System.currentTimeMillis();
					for (OntologyItem ont : Server.getOntology()) {
						counter++;
						if (counter%1000==0) {
							System.out.println(System.currentTimeMillis()-time + "ms " + counter + " nodes added!");
						time = System.currentTimeMillis();	
						}
//						System.out.println("ADDING: " +ont.getC_FULLNAME());
						TOSHandler.addi2b2OntologyItemToTree(ont);
					}
//					OntologyEditorView.getStagingTreeViewer().refresh();
					
					System.out.println("FINISHED tos.getOntology();");
				}
			}).run();
			

			try {



			} catch ( Exception e ) {
				Console.error("", e);
			}

		} catch ( Exception e ) {
			Console.error("", e);
		}

	}

	/**
	 * This method create the ontology.
	 * 
	 * @version 1.0
	 */
	public void createOntology(){
		System.out.println("createOntology()");
		
		


	}

	public void createMeta(){
		System.out.println("createMeta()");

		/*

		   try {

	        	List<String> list = null;

	        	String strFile = "C:\\I2B2ImportProject\\Eclipse\\output\\save_meta.csv";

	        	//create BufferedReader to read csv file
	        	//BufferedReader br = new BufferedReader( new FileReader(strFile));
	        	BufferedReader br = new BufferedReader( new InputStreamReader( _metaStream ) );
	        	int lineNumber = 0;


                String strLine = "";
				//read comma separated file line by line
                while( (strLine  = br.readLine()) != null ) {

                		list = new ArrayList<String>();
                        lineNumber++;

                        String[] split = strLine.split(";", -1);

                        // gather all the columns in list for easy access  
                        for (String item:split) { 
                            list.add(item);
                         }

                        // use functions to populate the tree depending on 
                        if (lineNumber > 1) {
                        	//this.getMyOT().getOT().getTreeRoot().setOriginalFileName(list.get(0));
                        }
                }

                br.close();

        } catch(Exception e) {
                System.out.println("Exception while reading csv file: " + e);                  
        }

		 */

	}

	public void createPatientData(){
		System.out.println("createPatientData");

		/*

			try {

	        	List<String> list = null;

	        	//String strFile = "C:\\I2B2ImportProject\\Eclipse\\output\\save_patient_data.csv";

	        	//create BufferedReader to read csv file
	        	//BufferedReader br = new BufferedReader( new FileReader(strFile));
	        	BufferedReader br = new BufferedReader( new InputStreamReader( _patientDataStream ) );
	        	String strLine = "";
	        	int lineNumber = 0;

	        	String patientID = "";
	        	OntologyTreePatient patientNode = null;
	        	 XMLGregorianCalendar xmlDate = null;

	             //read comma separated file line by line
	             while( ( strLine = br.readLine()) != null ) {

	             		list = new ArrayList<String>();
	                     lineNumber++;
	                     System.out.println("  create patient #" + lineNumber);
	                     String[] split = strLine.split(";", -1);

	                     // gather all the columns in list for easy access  
	                     for (String item:split) { 
	                    	 System.out.println("i:"+ item);
	                         list.add(item);
	                      }

	                     // use functions to populate the tree depending on 
	                     if (lineNumber > 1) {
	                    	 System.out.println("x1");
	                    	 patientID = list.get(0);
	                    	 patientNode = this.addPatient(patientID, patientID, "meta");
	                    	 patientNode.setI2B2ID(Long.valueOf(list.get(1)));

	                    	 System.out.println("x2");
	                    	 if (!list.get(12).isEmpty())
	                    		 xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(list.get(12));
	                    	 else
	                    		 xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
	                    	 System.out.println("x2b");
	                    	 addPatientAnswer(
	                    			 list.get(3),
	                    			 patientNode,
	                    			 list.get(7),
	                    			 list.get(7),
	                    			 xmlDate,
	                    			 list.get(12),
	                    			 true,
	                    			 "",
	                    			 ""); //TODO

	                    	System.out.println("x3");

	                     }
             }

             br.close();

     } catch(Exception e) {
             System.out.println("Exception while reading csv file: " + e);                  
     }

		 */

	}


}
