package de.umg.mi.idrt.idrtimporttool.importidrt;

import i2b2_postgres2.csv_master_0_1.CSV_MASTER;
import i2b2_postgres2.dbimport_master_0_1.DBIMPORT_MASTER;
import i2b2_postgres2.dropioetables_0_1.DropIOETables;
import i2b2_postgres2.idrt_stdterm_0_1.IDRT_STDTERM;
import i2b2_postgres2.idrt_transformation_0_5.IDRT_TRANSFORMATION;
import i2b2_postgres2.idrt_truncate_tables_0_1.IDRT_Truncate_Tables;
import i2b2_postgres2.mdr_idrt_anbindung_0_1.MDR_IDRT_Anbindung;
import i2b2_postgres2.odm_master_0_1.ODM_MASTER;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.ExportDB.ExportDB;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class IDRTImport {

	public static int status;
	public static String error;
	private static Connection connect = null;
	public static String DEFAULT_CONTEXTNAME = "Default";
	private static String contextName = "Default";
	private static HashMap<String, String> contextVariables = new HashMap<String, String>();
	private static int exitCode;
	private static Thread workerThread;
	public static void clearContextVariable() {
		contextVariables.clear();
	}

	/**
	 * Sets all context variables
	 * @return String[] of all CV
	 * @See contextVariables
	 */
	public static String[] getARGV() {
		List<String> parameters = new ArrayList<String>();
		if (contextName != null) {
			parameters.add("--context=" + contextName);
		}
		for (String key : contextVariables.keySet()) {
			parameters.add("--context_param");
			parameters.add(key + "=" + contextVariables.get(key));
			System.out.println("--context_param"+ key+ "="+contextVariables.get(key));
		}
		return parameters.toArray(new String[0]);
	}

	public static int getStatus() {
		return status;
	}
	@SuppressWarnings("deprecation")
	public static void killThread() {

		if (ServerView.stdImportStarted) {
			workerThread.stop();
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					Log.addLog(1, "Import Standardterminologies Failed!");
					StatusListener.setStatus(0, "", "");
					StatusListener.setSubStatus(0, "");
					MessageDialog
					.openError(Application.getShell(),
							"Import Standardterminologies Failed!",
							"Import Standardterminologies failed. (User aborted!)\nYou might want to redo your last action!");
				}
			});
		}
		ServerView.stdImportStarted = false;
	}

	
	public static int runMDRImport(){
		MDR_IDRT_Anbindung mdr = new MDR_IDRT_Anbindung();
		exitCode = mdr.runJobInTOS(getARGV());
			return exitCode;
	}
	
	/**
	 * TOS-Job which imports several CSV-Files in a Folder.
	 * <p><strong>Note:</strong> CSV-File delimiter: tabulator, semicolon</p>
	 * @param importTerms Import the standard terminologies?
	 * @return Exitcode from TOS (0=success)
	 * 
	 */
	public static int runCSVImport(boolean importTerms) {
		StatusListener.startLogging();
		ServerView.btnStopSetEnabled(true);
		CSV_MASTER CSVImport = new CSV_MASTER();
		exitCode = CSVImport.runJobInTOS(getARGV());
		System.out.println("exitCode " + exitCode);
		clearInputFolder();
		if (exitCode == 0 && importTerms) {
			StatusListener.setStatus(99f, "Importing and Mapping Terminologies", "");
			//			IDRT_STDTERM stdTerm = new IDRT_STDTERM();
			//			exitCode = stdTerm.runJobInTOS(getARGV());
			if (exitCode == 0) {
				HashMap<String, String> contextMap = new HashMap<String, String>();
				File t_mapping = FileHandler.getBundleFile("/cfg/t_mapping.csv");
				contextMap.put("t_mapping_path", t_mapping.getAbsolutePath().replaceAll("\\\\", "/"));
				setCompleteContext(contextMap);
				IDRT_TRANSFORMATION transform = new IDRT_TRANSFORMATION();
				exitCode = transform.runJobInTOS(getARGV());

			}
//			StatusListener.clearStatus();
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		} else {
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		}
	}
	/**
	 * TOS-Job which imports data from another sql database.
	 * @param importTerms Import the standard terminologies?
	 * @return Exitcode from TOS (0=success)
	 */
	public static int runDBImport(boolean importTerms) {
		StatusListener.startLogging();
		ServerView.btnStopSetEnabled(true);
		DBIMPORT_MASTER db = new DBIMPORT_MASTER();
		exitCode = db.runJobInTOS(getARGV());
		clearInputFolder();
		if (exitCode == 0 && importTerms) {
			StatusListener.setStatus(99f, "Importing and Mapping Terminologies", "");
			//			IDRT_STDTERM stdTerm = new IDRT_STDTERM();
			//			exitCode = stdTerm.runJobInTOS(getARGV());
			if (exitCode == 0) {
				HashMap<String, String> contextMap = new HashMap<String, String>();
				File t_mapping = FileHandler.getBundleFile("/cfg/t_mapping.csv");
				contextMap.put("t_mapping_path", t_mapping.getAbsolutePath().replaceAll("\\\\", "/"));
				setCompleteContext(contextMap);
				IDRT_TRANSFORMATION transform = new IDRT_TRANSFORMATION();
				exitCode = transform.runJobInTOS(getARGV());
			}
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		} else {
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		}
	}

	/**
	 * 
	 * @deprecated
	 * @return Exitcode from TOS (0=success)
	 */
	@Deprecated
	public static int runDBImport(Server server, File output,
			boolean importTerms) {
		setContextVariable("currentFile", output.getAbsolutePath());
		setContextVariable("fileName", server.getTable());
		ExportDB.exportDB(server, output);
		CSV_MASTER CSVImport = new CSV_MASTER();
		exitCode = CSVImport.runJobInTOS(getARGV());
		clearContextVariable();
		output.delete();
		clearInputFolder();
		if (exitCode == 0 && importTerms) {
			StatusListener.setStatus(99f, "Importing and Mapping Terminologies", "");
			//			IDRT_STDTERM stdTerm = new IDRT_STDTERM();
			//			exitCode = stdTerm.runJobInTOS(getARGV());
			if (exitCode == 0) {
				HashMap<String, String> contextMap = new HashMap<String, String>();
				File t_mapping = FileHandler.getBundleFile("/cfg/t_mapping.csv");
				contextMap.put("t_mapping_path", t_mapping.getAbsolutePath().replaceAll("\\\\", "/"));
				setCompleteContext(contextMap);
				IDRT_TRANSFORMATION transform = new IDRT_TRANSFORMATION();
				exitCode = transform.runJobInTOS(getARGV());
			}
			return exitCode;
		} else {
			return exitCode;
		}
	}
	
	
	

	/**
	 * Imports the standard terminologies.
	 * @param server the target server.
	 * @param project the i2b2 project within the server.
	 */
	public static void runImportST(Server server, String project) {
		StatusListener.startLogging();
		ServerView.stdImportStarted = true;
		HashMap<String, String> contextMap = new HashMap<String, String>();
		final String dbType = server.getDatabaseType();
		contextMap.put("DB_StagingI2B2_Host", server.getIp());
		contextMap.put("DB_StagingI2B2_Password", server.getPassword());
		contextMap.put("DB_StagingI2B2_Username", server.getUser());
		contextMap.put("DB_StagingI2B2_Instance", server.getSID());
		contextMap.put("DB_StagingI2B2_Port", server.getPort());
		contextMap.put("DB_StagingI2B2_Schema", project);
		contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
		/**
		 * ST-Import
		 */
		setSTContext(contextMap);
		setCompleteContext(contextMap);

		StatusListener.setStatus(1f, "Importing Terminologies", "");
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				ServerView.setProgress((int) StatusListener.getPercentage());
				ServerView.setProgressTop(StatusListener.getFile());
				ServerView.setProgressBottom(""
						+ StatusListener.getStatus());
			}
		});

		workerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerView.btnStopSetEnabled(true);
				//				IDRT_STDTERM stImport = new IDRT_STDTERM();
				//				exitCode = 	stImport.runJobInTOS(getARGV());
				if (exitCode==0) {
					IDRT_TRANSFORMATION transform = new IDRT_TRANSFORMATION();
					exitCode = transform.runJobInTOS(getARGV());
					ServerView.btnStopSetEnabled(false);
					StatusListener.stopLogging();

				}
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if (exitCode == 0) {
							StatusListener.setStatus(100f, "Import and Mapping done","");
							StatusListener.setSubStatus(0.0f, "");
							MessageDialog.openInformation(Application.getShell(),"Import and Mapping Complete!", "Import and Mapping Complete!");
							StatusListener.setStatus(0.0f, "","");
							StatusListener.setSubStatus(0.0f, "");
						}
						else {
							StatusListener.setStatus(100f, "Import and Mapping failed","");
							StatusListener.setSubStatus(0.0f, "");
							MessageDialog.openError(Application.getShell(), "Import failed!", "Import failed!");
							StatusListener.setStatus(0.0f, "","");
							StatusListener.setSubStatus(0.0f, "");
						}
					}
				});
			}
		});
		workerThread.start();
	}

	public static void runImportST_NoMap(Server server, String project) {
		StatusListener.startLogging();
		ServerView.stdImportStarted = true;
		HashMap<String, String> contextMap = new HashMap<String, String>();
		final String dbType = server.getDatabaseType();
		contextMap.put("DBHost", server.getIp());
		contextMap.put("DBPassword", server.getPassword());
		contextMap.put("DBUsername", server.getUser());
		contextMap.put("DBInstance", server.getSID());
		contextMap.put("DBPort", server.getPort());
		contextMap.put("DBSchema", project);
		contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
//		contextMap.put("DB_StagingI2B2_Host", server.getIp());
//		contextMap.put("DB_StagingI2B2_Password", server.getPassword());
//		contextMap.put("DB_StagingI2B2_Username", server.getUser());
//		contextMap.put("DB_StagingI2B2_Instance", server.getSID());
//		contextMap.put("DB_StagingI2B2_Port", server.getPort());
//		contextMap.put("DB_StagingI2B2_Schema", project);
//		contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
		/**
		 * ST-Import
		 */
		setSTContext(contextMap);
		setCompleteContext(contextMap);

		StatusListener.setStatus(1f, "Importing Terminologies", "");
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				ServerView.setProgress((int) StatusListener.getPercentage());
				ServerView.setProgressTop(StatusListener.getFile());
				ServerView.setProgressBottom(""
						+ StatusListener.getStatus());
			}
		});

		workerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerView.btnStopSetEnabled(true);
				System.out.println("yes");
				IDRT_STDTERM std = new IDRT_STDTERM();
				//				IDRT_STDTERM std = new IDRT_STDTERM();
				System.out.println("2");
				exitCode = 	std.runJobInTOS(getARGV());
				System.out.println("3");
				StatusListener.stopLogging();

				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if (exitCode == 0) {
							StatusListener.setStatus(100f, "Import and Mapping done","");
							StatusListener.setSubStatus(0.0f, "");
							MessageDialog.openInformation(Application.getShell(),"Import and Mapping Complete!", "Import and Mapping Complete!");
							StatusListener.setStatus(0.0f, "","");
							StatusListener.setSubStatus(0.0f, "");
						}
						else {
							StatusListener.setStatus(100f, "Import and Mapping failed","");
							StatusListener.setSubStatus(0.0f, "");
							MessageDialog.openError(Application.getShell(), "Import failed!", "Import failed!");
							StatusListener.setStatus(0.0f, "","");
							StatusListener.setSubStatus(0.0f, "");
						}
					}
				});
			}
		});
		workerThread.start();
	}

	/**
	 * TOS-Job which imports data from ODM files in a folder.
	 * @param importTerms Import the standard terminologies?
	 * @return Exitcode from TOS (0=success)
	 */
	public static int runODMImport(boolean importTerms) {
		StatusListener.startLogging();
		ServerView.btnStopSetEnabled(true);
		ODM_MASTER ODMImport = new ODM_MASTER();
		exitCode = ODMImport.runJobInTOS(getARGV());
		clearInputFolder();
		if (exitCode == 0 && importTerms) {
			StatusListener.setStatus(99f, "Importing and Mapping Terminologies", "");
			//			IDRT_STDTERM stdTerm = new IDRT_STDTERM();
			//			exitCode = stdTerm.runJobInTOS(getARGV());
			if (exitCode == 0) {
				HashMap<String, String> contextMap = new HashMap<String, String>();
				File t_mapping = FileHandler.getBundleFile("/cfg/t_mapping.csv");
				contextMap.put("t_mapping_path", t_mapping.getAbsolutePath().replaceAll("\\\\", "/"));
				setCompleteContext(contextMap);
				IDRT_TRANSFORMATION transform = new IDRT_TRANSFORMATION();
				exitCode = transform.runJobInTOS(getARGV());
			}
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		} else {
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		}
	}

	/**
	 * TOS-Job which imports the §21 dataset.
	 * @param oldVersion old or new version of §21
	 * @param importTerms Import the standard terminologies?
	 * @return Exitcode from TOS (0=success)
	 */
	public static int runP21Import(String version, boolean importTerms) {
		StatusListener.startLogging();

		ServerView.btnStopSetEnabled(true);
		CSV_MASTER CSVImport = new CSV_MASTER();
		exitCode = CSVImport.runJobInTOS(getARGV());
		clearInputFolder();
		if (exitCode == 0 && importTerms) {
			StatusListener.setStatus(99f, "Importing and Mapping Terminologies", "");
			//			IDRT_STDTERM stdTerm = new IDRT_STDTERM();
			//			exitCode = stdTerm.runJobInTOS(getARGV());
			if (exitCode == 0) {
				HashMap<String, String> contextMap = new HashMap<String, String>();
				File t_mapping = FileHandler.getBundleFile("/cfg/t_mapping.csv");
				contextMap.put("t_mapping_path", t_mapping.getAbsolutePath().replaceAll("\\\\", "/"));
				setCompleteContext(contextMap);
				IDRT_TRANSFORMATION transform = new IDRT_TRANSFORMATION();
				exitCode = transform.runJobInTOS(getARGV());
			}
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		} else {
			ServerView.btnStopSetEnabled(false);
			StatusListener.stopLogging();
			return exitCode;
		}
	}

	/**
	 * @param server
	 * @param text
	 */
	public static void runRemoveLocks(Server server, String project) {
		StatusListener.startLogging();
		HashMap<String, String> contextMap = new HashMap<String, String>();
		final String dbType = server.getDatabaseType();
		contextMap.put("DB_StagingI2B2_Host", server.getIp());
		contextMap.put("DB_StagingI2B2_Password", server.getPassword());
		contextMap.put("DB_StagingI2B2_Username", server.getUser());
		contextMap.put("DB_StagingI2B2_Instance", server.getSID());
		contextMap.put("DB_StagingI2B2_Port", server.getPort());
		contextMap.put("DB_StagingI2B2_Schema", project);
		contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
		setCompleteContext(contextMap);

		Thread workerThread = new Thread(new Runnable() {
			@Override
			public void run() {
//				SERVER_FreeLocks freeLocks = new SERVER_FreeLocks();
				ServerView.btnStopSetEnabled(true);
				exitCode = -1;	//freeLocks.runJobInTOS(getARGV());
				ServerView.btnStopSetEnabled(false);

				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						StatusListener.stopLogging();
						if (exitCode == 0) {

							StatusListener.setStatus(100f, "Locks removed.","");
							MessageDialog.openInformation(Application.getShell(),"Locks removed!", "Locks removed!");
						}
						else {
							MessageDialog.openError(Application.getShell(), "Removing Locks failed!", "Removing Locks failed!\nAre you sys admin?");
						}
					}
				});

			}
		});
		workerThread.start();

	}
	

	public static void dropIOETables(Server server, String project) {
		// TODO Auto-generated method stub
		StatusListener.startLogging();
		HashMap<String, String> contextMap = new HashMap<String, String>();
		final String dbType = server.getDatabaseType();
		contextMap.put("DB_StagingI2B2_Host", server.getIp());
		contextMap.put("DB_StagingI2B2_Password", server.getPassword());
		contextMap.put("DB_StagingI2B2_Username", server.getUser());
		contextMap.put("DB_StagingI2B2_Instance", server.getSID());
		contextMap.put("DB_StagingI2B2_Port", server.getPort());
		contextMap.put("DB_StagingI2B2_Schema", project);
		contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
		setCompleteContext(contextMap);


		Thread workerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				DropIOETables dropTables = new DropIOETables();
				ServerView.btnStopSetEnabled(true);
				exitCode = 	dropTables.runJobInTOS(getARGV());
				ServerView.btnStopSetEnabled(false);

				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						StatusListener.stopLogging();
						if (exitCode == 0) {

							StatusListener.setStatus(100f, "Tables Dropped.","");
							MessageDialog.openInformation(Application.getShell(),"Tables Dropped!", "Tables Dropped!");
						}
						else {
							MessageDialog.openError(Application.getShell(), "Dropping Tables failed!", "Dropping Tables failed!");
						}
					}
				});

			}
		});
		workerThread.start();
	}

	/**
	 * TOS-Job which truncates the selected project.
	 * 
	 * @param server The target server.
	 * @param project The i2b2 project within the server.
	 */
	public static void runTruncateProject(final Server server, final String project) {
		System.out.println("trunc: " + project);
		Thread workerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				ServerView.btnStopSetEnabled(true);
				IDRT_Truncate_Tables truncJob = new IDRT_Truncate_Tables();
				HashMap<String, String> contextMap = new HashMap<String, String>();
				final String dbType = server.getDatabaseType();
				contextMap.put("DB_StagingI2B2_Host", server.getIp());
				contextMap.put("DB_StagingI2B2_Password", server.getPassword());
				contextMap.put("DB_StagingI2B2_Username", server.getUser());
				contextMap.put("DB_StagingI2B2_Instance", server.getSID());
				contextMap.put("DB_StagingI2B2_Port", server.getPort());
				contextMap.put("DB_StagingI2B2_Schema", project);
				contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
				contextMap.put("truncateQueries", "true");
				setCompleteContext(contextMap);
				StatusListener.setStatus(0f, "Truncating Project", "");
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						ServerView.setProgress((int) StatusListener.getPercentage());
						ServerView.setProgressTop(StatusListener.getFile());
						ServerView.setProgressBottom(""
								+ StatusListener.getStatus());
					}
				});
				exitCode = truncJob.runJobInTOS(getARGV());
				StatusListener.setStatus(100f, "Truncating Project done", "");
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						ServerView.btnStopSetEnabled(false);
						ServerView.setProgress((int) StatusListener.getPercentage());
						ServerView.setProgressTop(StatusListener.getFile());
						ServerView.setProgressBottom(""
								+ StatusListener.getStatus());
						if (exitCode == 0) {
							MessageDialog.openInformation(Application.getShell(),"Truncate Complete!", "Truncate Complete!");
						}
						else {
							MessageDialog.openError(Application.getShell(), "Truncate failed!", "Truncate failed!");
						}
					}
				});
			}
		});
		workerThread.start();
	}

	/**
	 * Sets all context-variables.
	 * @param contexts The contexts to set.
	 */
	public static void setCompleteContext(HashMap<String, String> contexts) {
		Iterator<String> contextsIt = contexts.keySet().iterator();
		while (contextsIt.hasNext()) {
			String nextKey = contextsIt.next();
			setContextVariable(nextKey, contexts.get(nextKey));
		}
	}

	public static void setContextName(String name) {
		contextName = name;
	}

	public static void setContextVariable(String key, String value) {
		contextVariables.put(key, value);
	}

	public static void setStatus(String statusMsg, int percentage) {
		status = percentage;
	}

	/**
	 * Tests the connection to a servers database.
	 * @param server The database to be tested.
	 * @return Success=true Error=Failure
	 */
	public static boolean testDB(Server server) {
		try {
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
			connect.close();
		} catch (SQLException e) {
			error = Server.getError();
			System.err.println(Server.getError());
			//			e.printStackTrace();
			return false;
		}
		catch(Exception e2) {
			error = Server.getError();
			//			e2.printStackTrace();
			System.err.println(Server.getError());
			return false;
		}
		return true;
	}

	/**
	 * Tests the connection to a servers database.
	 * @param ip IP of the server.
	 * @param port Port of the server.
	 * @param username Username of the servers database.
	 * @param password Password of the user of the database.
	 * @param sid SID of the servers database.
	 * @return Success=true Error=Failure
	 */
	public static boolean testDB(String ip, String port, String username,
			String password, String sid, String databaseType, boolean useWinAuth, boolean savePassword) {
		try {
			Server server = new Server("test", ip, port, username, password, sid, databaseType,useWinAuth,savePassword);
			DriverManager.setLoginTimeout(2);
			connect = server.getConnection();
			connect.close();
		} catch (SQLException e) {
			error = Server.getError();
			e.printStackTrace();
			return false;
		}
		catch (Exception e2) {
			error = Server.getError();
			e2.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Deletes all unneeded input files from previous imports.
	 */
	private static void clearInputFolder() {

		File inputFolder = FileHandler.getBundleFile("/misc/input/");
		File[] listOfInputFiles = inputFolder.listFiles();

		for (File listOfInputFile : listOfInputFiles) {
			if (listOfInputFile.getName().endsWith(".csv")) {
				listOfInputFile.delete();
			}
		}
	}

	/**
	 * @param contextMap 
	 * 
	 */
	private static void setSTContext(HashMap<String, String> contextMap) {
		// TODO Auto-generated method stub
		File rootDir = FileHandler.getBundleFile("/cfg/Standardterminologien/");
		contextMap.put("icd10Dir", rootDir.getAbsolutePath().replaceAll("\\\\", "/")+ "/ICD-10-GM/" + "/");
		contextMap.put("tnmDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/TNM/" + "/");
		contextMap.put("rootDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/")+ "/");
		contextMap.put("loincDir", rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/LOINC/" + "/");
		contextMap.put("opsDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/OPS/" + "/");
		contextMap.put("p21Dir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/P21/" + "/");
		contextMap.put("drgDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/DRG/" + "/");
		contextMap.put("icdoDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/ICD-O-3/" + "/"); 

	}

}
