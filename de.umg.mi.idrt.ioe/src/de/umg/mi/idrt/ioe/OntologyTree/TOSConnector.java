package de.umg.mi.idrt.ioe.OntologyTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
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

	public static String DEFAULT_CONTEXTNAME = "Default";
	private static int exit;
	private static String contextName;
	private static HashMap<String, String> contextVariables = new HashMap<String, String>();

	public TOSConnector() {

		Console.info("TOSConnector: establising a TOS connection");
	}

	public static tos.tosidrtconnector_0_4.TOSIDRTConnector getConnection() {
		Debug.d("TOSIDRTCOnnector: gettingen Connection");

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

			Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("/cfg/Default.properties"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = null;

			File properties = null;
			Properties defaultProps = null;

			try {
				fileUrl = FileLocator.toFileURL(url);
				properties = new File(fileUrl.getPath());
				defaultProps = new Properties();
				defaultProps.load(new FileReader(properties));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			//			String schema = ServerView.getCurrentSchema();
			// String schema = ServerView.getCurrentSchema();
			// defaultProps.getProperty("schema");

			//			String serverUniqueName = OntologyEditorView.getCurrentServer().;
			// String serverUniqueName = OntologyEditorView.getCurrentServer().;

			Server currentServer = OntologyEditorView.getStagingServer();
			Console.info(currentServer.getSchema());
			//			if (serverUniqueName != null) {
			//				currentServer = ServerList.getTargetServers().get(
			//						serverUniqueName);
			//			}

			Console.info("Current server: " + currentServer.toString());

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

		
				//

				//setContextVariable("OracleHost", currentServer.getIp());
				//setContextVariable("OraclePort", currentServer.getPort());
				//setContextVariable("OracleSid", currentServer.getSID());
				setContextVariable("DB_StagingI2B2_Username",
						currentServer.getUser());
				setContextVariable("DB_StagingI2B2_Password",
						currentServer.getPassword());
				setContextVariable("DB_StagingI2B2_Schema",
						currentServer.getSchema());
				
				setContextVariable("DB_StagingI2B2_jdbcurl", "jdbc:oracle:thin:@" + currentServer.getIp() + ":" + currentServer.getPort() + ":" + currentServer.getSID());
				setContextVariable("DB_StagingI2B2_sqlclassname", "oracle.jdbc.driver.OracleDriver");

				
				
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
			Console.error("Error while using a TOS-plugin: " + e.getMessage());
			Debug.e("TOS-Error: " + tos.getErrorCode());
		}

		return tos;

	}
	public void getOntology() {
		Console.info("TOSConnector: getOntology()");

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();

				try {

					setContextVariable("Job", "ontology");
					setContextVariable("SQLTable", "I2B2");
					/*
					 * setContextVariable("SQLCommand",
					 * "SELECT * FROM I2B2IDRT.I2B2 WHERE C_HLEVEL > 0 ORDER BY C_HLEVEL ASC"
					 * );
					 */

					tos.runJobInTOS((getARGV()));

				} catch (Exception e) {
					String message = "Error while using a TOS-plugin with function getOntology(): "
							+ e.getMessage();
					Console.error(message);
					Application.getStatusView().addErrorMessage(message);
					Console.info("TOS-Error2: " + tos.getErrorCode() + " / "
							+ " / " + tos.getException() + " / "
							+ tos.getStatus() + " / "
							+ tos.getExceptionStackTrace() + " / "
							+ tos.getContext().SQLCommand);

				}
			}
		}).run();

	}

	public void runJob() {

		getConnection().runJobInTOS((getARGV()));

	}

	public static void setContextVariable(String key, String value) {
		contextVariables.put(key, value);
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

	public static void deleteTargetOntologyFull() {

		setContextVariable("Job", "delete_target_ontology");

	}

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

	public static boolean checkOntology() {
		setContextVariable("Job", "check_ontology_empty");
		setContextVariable("Var1", "1");

		boolean hasOntology = false;

		try {

			tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();

			tos.runJobInTOS((getARGV()));

			if (tos.getErrorCode() == 0) {
				hasOntology = true;
			}

		} catch (Exception e) {

			Console.error("Error while using a TOS-plugin with function writeTargetOntology(): "
					+ e.getMessage());

		}

		return hasOntology;

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

	public static int uploadProject() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setContextVariable("Job", "etlStagingI2B2ToTargetI2B2");

				try {
					tos.tosidrtconnector_0_4.TOSIDRTConnector tos = getConnection();
					exit = tos.runJobInTOS((getARGV()));
				} catch (Exception e) {
					Console.error("Error while using a TOS-plugin with for job \"etlStagingI2B2ToTargetI2B2\": "
							+ e.getMessage());
				}
			}
		}).run();

		return exit;

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

}
