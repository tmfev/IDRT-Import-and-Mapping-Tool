package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import tos.idrtcommand_transformationtotarget_0_1.IDRTCommand_TransformationToTarget;

import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.misc.FileHandler;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         connector for using the TOS-generated java files inside the ieo code
 * 
 */

public class TOSConnector {

	private static Thread workerThread;
	public static String DEFAULT_CONTEXTNAME = "Default";
	private static int exit;
	private static String contextName;
	private static HashMap<String, String> contextVariables = new HashMap<String, String>();

	public static boolean checkOntology() {
		//		setContextVariable("Job", "check_ontology_empty");
		//		setContextVariable("Var1", "1");
		//
		//		boolean hasOntology = false;
		//
		//		try {
		//
		//			tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();
		//
		//			tos.runJobInTOS((getARGV()));
		//
		//			if (tos.getErrorCode() == 0) {
		//				hasOntology = true;
		//			}
		//
		//		} catch (Exception e) {
		//
		//			Console.error("Error while using a TOS-plugin with function writeTargetOntology(): "
		//					+ e.getMessage());
		//
		//		}

		//		return hasOntology;
		return true;

	}

	public static void deleteTargetOntologyFull() {

		setContextVariable("Job", "delete_target_ontology");

	}
	public static String[] getARGV() {
		List<String> parameters = new ArrayList<String>();
		if (contextName != null) {
			parameters.add("--context=" + contextName);
		}
		for (String key : contextVariables.keySet()) {
			parameters.add("--context_param");
			parameters.add(key + "=" + contextVariables.get(key));
		}

		return (String[]) parameters.toArray(new String[0]);
	}

	public static tos.tosidrtconnector_0_4.TOSIDRTConnector getConnection() {
		Console.info("TOSConnector: gettingen Connection");

		tos.tosidrtconnector_0_4.TOSIDRTConnector tos = null;

		try {
			tos = new tos.tosidrtconnector_0_4.TOSIDRTConnector();

			contextName = "Default";

			// trying to get the server-data from the currently selected server
			// in the servereditor

			// VM-Server
			tos = new tos.tosidrtconnector_0_4.TOSIDRTConnector();

			// if there is a i2b2-project selected in the server view, use it as
			// the
			// editors

			File properties = null;
			Properties defaultProps = null;

			try {
				properties = FileHandler.getBundleFile("/cfg/Default.properties");
				defaultProps = new Properties();
				defaultProps.load(new FileReader(properties));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			Server currentServer = OntologyEditorView.getStagingServer();

			Console.info(currentServer.getSchema());
			//			if (serverUniqueName != null) {
			//				currentServer = ServerList.getTargetServers().get(
			//						serverUniqueName);
			//			}

			Console.info("Current server: " + currentServer.toString() + " " + currentServer.getSchema());

			if (currentServer != null) {
				Console.info("Using selected server \""
						+ currentServer.getName() + "(\""
						+ currentServer.getSchema() + "\")\" for db query.");

				//				currentServer.setSchema(schema);
				//				OntologyEditorView.setCurrentServer(currentServer);

				Console.info("currentSchema:" + currentServer.getSchema());
				Console.info("sid: " + currentServer.getSID());
				System.out
				.println("OracleUsername: " + currentServer.getUser());

				setContextVariable("OracleHost", currentServer.getIp());
				setContextVariable("OraclePort", currentServer.getPort());
				setContextVariable("OracleSid", currentServer.getSID());
				setContextVariable("OracleUsername", currentServer.getUser());
				setContextVariable("OraclePassword",
						currentServer.getPassword());
				setContextVariable("OracleDB", currentServer.getSID());
				setContextVariable("SQLTable", currentServer.getTable());
				setContextVariable("OracleSchema", currentServer.getSchema());

				setContextVariable("DB_StagingI2B2_Username",
						currentServer.getUser());
				setContextVariable("DB_StagingI2B2_Instance",
						currentServer.getSID());
				setContextVariable("DB_StagingI2B2_Password",
						currentServer.getPassword());
				setContextVariable("DB_StagingI2B2_Schema",
						OntologyEditorView.getStagingSchemaName());
				setContextVariable("DB_StagingI2B2_jdbcurl", "jdbc:oracle:thin:@" + currentServer.getIp() + ":" + currentServer.getPort() + ":" + currentServer.getSID());
				setContextVariable("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");
				setContextVariable("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");

				//
				Server targetServer = ServerList.getTargetServers().get(ServerList.getUserServer().get(OntologyEditorView.getTargetSchemaName()));

				if (targetServer!=null) {
					System.out.println("TargetServerName" + targetServer.getName()); 
					//				targetServer.setSchema(OntologyEditorView.getTargetSchemaName());


					setContextVariable("DB_TargetI2B2_Username",
							targetServer.getUser());
					setContextVariable("DB_TargetI2B2_Password",
							targetServer.getPassword());
					setContextVariable("DB_TargetI2B2_Schema",
							OntologyEditorView.getTargetSchemaName());
					setContextVariable("DB_TargetI2B2_Instance",
							targetServer.getSID());


					setContextVariable("DB_TargetI2B2_jdbcurl", "jdbc:oracle:thin:@" + targetServer.getIp() + ":" + targetServer.getPort() + ":" + targetServer.getSID());
				}
				setContextVariable("DB_TargetI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");

				setContextVariable("TableIEOTargetOntology", "IOE_TARGET_ONTOLOGY");
				setContextVariable("TableIEOTarget", "IOE_TARGET");
				setContextVariable("TableIEOTargetProject", "IOE_TARGET_PROJECT");


				//

				/*
				 * Application.getStatusView().addMessage( "i2b2 project \"" +
				 * currentServer.getSchema() + "\"selected via ServerView.");
				 */

			}

		} catch (Exception e) {
			e.printStackTrace();
			Console.error("Error while using a TOS-plugin: " + e.getMessage());
		}

		return tos;

	}

	public static int loadSourceToTarget(String targetID,
			final String tmpDataFile) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setContextVariable("Job", "load_source_to_target");
				setContextVariable("Var1", "1");
				setContextVariable("DataFile", tmpDataFile);

				try {
					tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();
					exit = tos.runJobInTOS((getARGV()));
				} catch (Exception e) {
					e.printStackTrace();
					Console.error("Error while using a TOS-plugin with function loadSourceToTarget(): "
							+ e.getMessage());
				}
			}
		}).run();

		return exit;
	}

	public static void readTargetOntology(String targetID) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				setContextVariable("Job", "read_target_ontology");
				setContextVariable("Var1", "1");
				try {
					tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();
					tos.runJobInTOS((getARGV()));
				} catch (Exception e) {
					Console.error("Error while using a TOS-plugin with function readTargetOntology(): "
							+ e.getMessage());
				}
			}
		}).run();

	}

	/**
	 * Sets all context-variables.
	 * 
	 * @param contexts
	 *            The contexts to set.
	 */
	public static void setCompleteContext(HashMap<String, String> contexts) {
		Iterator<String> contextsIt = contexts.keySet().iterator();
		while (contextsIt.hasNext()) {
			String nextKey = contextsIt.next();
			setContextVariable(nextKey, contexts.get(nextKey));
		}
	}

	public static void setContextVariable(String key, String value) {
		contextVariables.put(key, value);
	}

	public static int uploadProject() {

		Display display = Application.getDisplay();
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setContextVariable("Job", "etlStagingI2B2ToTargetI2B2");
				Server currentServer = OntologyEditorView.getStagingServer();
				setContextVariable("DB_StagingI2B2_Username",
						currentServer.getUser());
				setContextVariable("DB_StagingI2B2_Instance",
						currentServer.getSID());
				setContextVariable("DB_StagingI2B2_Password",
						currentServer.getPassword());
				setContextVariable("DB_StagingI2B2_Schema",
						OntologyEditorView.getStagingSchemaName());
				setContextVariable("DB_StagingI2B2_jdbcurl", "jdbc:oracle:thin:@" + currentServer.getIp() + ":" + currentServer.getPort() + ":" + currentServer.getSID());
				setContextVariable("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");
				setContextVariable("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");

				//
				Server targetServer = ServerList.getTargetServers().get(ServerList.getUserServer().get(OntologyEditorView.getTargetSchemaName()));

				if (targetServer!=null) {
					System.out.println("TargetServerName" + targetServer.getName()); 
					//				targetServer.setSchema(OntologyEditorView.getTargetSchemaName());


					setContextVariable("DB_TargetI2B2_Username",
							targetServer.getUser());
					setContextVariable("DB_TargetI2B2_Password",
							targetServer.getPassword());
					setContextVariable("DB_TargetI2B2_Schema",
							OntologyEditorView.getTargetSchemaName());
					setContextVariable("DB_TargetI2B2_Instance",
							targetServer.getSID());


					setContextVariable("DB_TargetI2B2_jdbcurl", "jdbc:oracle:thin:@" + targetServer.getIp() + ":" + targetServer.getPort() + ":" + targetServer.getSID());
				}
				setContextVariable("DB_TargetI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");
				

				Console.info(currentServer.getSchema());
				//			if (serverUniqueName != null) {
				//				currentServer = ServerList.getTargetServers().get(
				//						serverUniqueName);
				//			}

				Console.info("Current server: " + currentServer.toString());

				Console.info("Using selected server \""
						+ currentServer.getName() + "(\""
						+ currentServer.getSchema() + "\")\" for db query.");

				//				currentServer.setSchema(schema);
				//				OntologyEditorView.setCurrentServer(currentServer);

				Console.info("currentSchema:" + currentServer.getSchema());
				Console.info("sid: " + currentServer.getSID());
				System.out
				.println("OracleUsername: " + currentServer.getUser());

				//

				//setContextVariable("OracleHost", currentServer.getIp());
				//setContextVariable("OraclePort", currentServer.getPort());
				//setContextVariable("OracleSid", currentServer.getSID());
				setContextVariable("DB_StagingI2B2_Username",
						currentServer.getUser());
				setContextVariable("DB_StagingI2B2_Password",
						currentServer.getPassword());
				System.out.println("OntologyEditorView.getStagingSchemaName()" +OntologyEditorView.getStagingSchemaName());


				setContextVariable("DB_StagingI2B2_Schema",
						OntologyEditorView.getStagingSchemaName());
				setContextVariable("DB_StagingI2B2_jdbcurl", "jdbc:oracle:thin:@" + currentServer.getIp() + ":" + currentServer.getPort() + ":" + currentServer.getSID());
				setContextVariable("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");
				setContextVariable("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");


				//				try {
//				tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();
			
				IDRTCommand_TransformationToTarget trans = new IDRTCommand_TransformationToTarget();
				exit = trans.runJobInTOS((getARGV()));

				if (exit==0) {
					MessageDialog.openInformation(Application.getShell(), "Success!", "Upload Done!");
				}
				else {
					MessageDialog.openError(Application.getShell(), "Failure!", "Upload failed!");
				}
				//				} catch (Exception e) {
				//					Console.error("Error while using a TOS-plugin with for job \"etlStagingI2B2ToTargetI2B2\": "
				//							+ e.getMessage());
				//				}

			}
		});

		workerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});
		workerThread.run();
		return exit;

	}
	@Deprecated
	public static int writeTargetOntology(String targetID, String tmpDataFile) {

		setContextVariable("Job", "write_target_ontology");
		setContextVariable("Var1", "1");
		setContextVariable("DataFile", tmpDataFile);

		try {
			tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();
			tos.runJobInTOS((getARGV()));
		} catch (Exception e) {
			Console.error("Error while using a TOS-plugin with function writeTargetOntology(): "
					+ e.getMessage());
			return 1;
		}
		return 0;

	}

	public TOSConnector() {

		Console.info("TOSConnector: establising a TOS connection");
	}



	public void runJob() {

		getConnection().runJobInTOS((getARGV()));

	}

}
