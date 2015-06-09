package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * Wizard for the CSV-Import.
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class BiobankImportWizard extends Wizard {

	private static int exitCode;

	/**
	 * Updates the status of the progress bar.
	 */
	public static void updateStatus() {
		if (started) {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					ServerView.setProgress((int) StatusListener.getPercentage());
					ServerView.setProgressTop(StatusListener.getFile());
					ServerView.setProgressBottom(""
							+ StatusListener.getStatus());
				}
			});
		}
	}
	private Properties defaultProps;
	private static Thread b;
	private static Thread importThread;
	/**
	 * Copies a file.
	 * @param inputFile Input file.
	 * @param outputFile Output file.
	 */
	public static void copyFile(File inputFile, File outputFile) {
		try {
			FileReader in = new FileReader(inputFile);
			FileWriter out = new FileWriter(outputFile);
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Brutally kills the the import thread.
	 */
	@SuppressWarnings("deprecation")
	public static void killThread() {
		if (started) {
			importThread.stop();
			b.stop();
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					//					closeBar("CSV Import Failed!", 1);
					Log.addLog(1, "CSV Import Failed!");
					MessageDialog
					.openError(Application.getShell(),
							"Import Failed!",
							"Import failed. (User aborted!)\nYou might want to redo your last action!");
				}
			});
		}
		StatusListener.notifyListener();
		started = false;
	}
	@SuppressWarnings("deprecation")
	public static void killThreadRemote(final String error,
			final String fileName) {

		if (started) {
			importThread.stop();
			// b.stop();

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					//					closeBar(error, fileName, 1);
					Log.addLog(1, "CSV Import Failed: " + error);
					MessageDialog.openError(Application.getShell(), "Import Failed!",
							"Import failed. Please see the log.");
				}
			});
		}
		started = false;
		StatusListener.notifyListener();
	}

	private HashMap<String, String> contextMap;

	protected BiobankWizardPage1 one;
	protected BiobankWizardPage2 two;
	public static boolean started = false;



	public BiobankImportWizard() {
		super();
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		one = new BiobankWizardPage1();
				two = new BiobankWizardPage2();
		//		three = new CSVWizardPage3();

		addPage(one);
				addPage(two);
		//		addPage(three);
	}

	//	private static void closeBar(String msg, int status) {
	//		closeBar(msg, "", status);
	//	}

	//	private static void closeBar(String msg, final String fileName, int status) {
	//		ServerView.closeBar(msg, fileName, status);
	//	}

	//
	@Override
	public boolean canFinish() {
		return (two.isPageComplete());
	}

	//	public static CSVWizardPageThree getThree() {
	//		return three;
	//	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getPageCount()
	 */
	@Override
	public int getPageCount() {
		return 1;
	}

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}

	@Override
	public boolean performFinish() {

		final Server selectedServer = ServerView.getSelectedServer();
		final String ipText = selectedServer.getIp();
		final String passwordText = selectedServer.getPassword();
		final String dbUserText = selectedServer.getUser();
		final String dbSID = selectedServer.getSID();
		final String dbPort = selectedServer.getPort();
		final String dbSchema = selectedServer.getSchema();
		final String dbType = selectedServer.getDatabaseType();
		final String whType = selectedServer.getWhType().toLowerCase();

		
		final Server bioServer = ServerList.getSourceServers().get(BiobankWizardPage1.getCurrentServerString());
		final String ipTextBio = BiobankWizardPage1.getIpText();
		final String passwordTextBio = BiobankWizardPage1.getDBUserPasswordText();
		final String dbUserTextBio = BiobankWizardPage1.getDBUserText();
		final String dbSIDBio = BiobankWizardPage1.getDBSIDText();
		final String dbPortBio = BiobankWizardPage1.getPortText();
//		final String dbSchemaBio = BiobankWizardPage1.getDBSchemaText();
		final String dbWH_Type = BiobankWizardPage1.getDBType();


		final boolean truncate = BiobankWizardPage2.getTruncate();
		final boolean truncateQueries = BiobankWizardPage2.getTruncateQueries();
		final boolean cleanUp = BiobankWizardPage2.getCleanUp();

		final boolean stopIndex = BiobankWizardPage2.getStopIndex();
		final boolean dropIndex = BiobankWizardPage2.getDropIndex();

		final boolean save = BiobankWizardPage2.getSaveContext();

		final String language = BiobankWizardPage2.getLanguage();
		final String rasprojectno = BiobankWizardPage2.getRasprojectno();
		final String rasclientid = BiobankWizardPage2.getRasclientid();


		// saving the last table before import


		importThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final long start = System.currentTimeMillis();

					File inputFolder = FileHandler.getBundleFile("/misc/input/");
					File outputFolder = FileHandler.getBundleFile("/misc/output/");

					File[] listOfInputFiles = inputFolder.listFiles();

					for (File listOfInputFile : listOfInputFiles) {
						if (listOfInputFile.getName().endsWith(".csv")) {
							boolean delete = listOfInputFile.delete();
						}
					}

					File properties = FileHandler.getBundleFile("/cfg/Default.properties");
					defaultProps = new Properties();
					defaultProps.load(new FileReader(properties));

					contextMap = new HashMap<String, String>();
					File misc = FileHandler.getBundleFile("/misc/");
					String miscPathReplaced = misc.getAbsolutePath()
							.replaceAll("\\\\", "/") + "/";
					/**
					 * STARLIMS
					 * 
					 */

					contextMap.put("ont_path", miscPathReplaced+"output/ont.csv");
					contextMap.put("data_path", miscPathReplaced+"output/data.csv");
					contextMap.put("language", language);
					contextMap.put("rasclientid", rasclientid);
					contextMap.put("rasprojectno", rasprojectno);
					File sprec = FileHandler.getBundleFile("/misc/SPREC.xlsx");
					contextMap.put("sprec_path", sprec.getAbsolutePath().replaceAll("\\\\", "/"));
					
//					String PRJ_IDRT_STARLIMS_JdbcUrl = "";
//					String PRJ_IDRT_STARLIMS_ClassName ="";
//					if (dbWH_Type.equalsIgnoreCase("postgres")){
//						PRJ_IDRT_STARLIMS_JdbcUrl = "jdbc:postgresql://"+ipTextBio+":"+dbPortBio+"/"+dbSIDBio;
//						PRJ_IDRT_STARLIMS_ClassName = "org.postgresql.Driver";
//					}
//					else if (dbWH_Type.equalsIgnoreCase("oracle")){
//						PRJ_IDRT_STARLIMS_JdbcUrl = "jdbc:oracle:thin:@"+ipTextBio +":" + dbPortBio + ":" + dbSIDBio;
//						PRJ_IDRT_STARLIMS_ClassName = "oracle.jdbc.driver.OracleDriver";
//					}

					contextMap.put("PRJ_IDRT_STARLIMS_JdbcUrl", bioServer.getJDBCURL());
					contextMap.put("PRJ_IDRT_STARLIMS_ClassName", bioServer.getJDBCDriver());
					
					
					/**
					 * page 1
					 */
					contextMap.put("DB_StagingI2B2_Host", ipText);
					contextMap.put("DB_StagingI2B2_Password", passwordText);
					contextMap.put("DB_StagingI2B2_Username", dbUserText);
					contextMap.put("DB_StagingI2B2_Instance", dbSID);
					contextMap.put("DB_StagingI2B2_Port", dbPort);
					contextMap.put("DB_StagingI2B2_Schema", dbSchema);
					contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
					contextMap.put("DB_StagingI2B2_WHType", whType);
					
					contextMap.put("DB_StagingI2B2_jdbcurl", selectedServer.getJDBCURL());
					

					contextMap.put("MDPDName", defaultProps.getProperty("MDPDName"));

					/**
					 * page 2
					 */
					if (dropIndex){
						defaultProps.setProperty("IndexStop", "false");
						contextMap.put("IndexStop", "false");

						defaultProps.setProperty("IndexDrop", "true");
						contextMap.put("IndexDrop", "true");
					}
					else if (stopIndex){
						defaultProps.setProperty("IndexStop", "true");
						contextMap.put("IndexStop", "true");
						defaultProps.setProperty("IndexDrop", "false");
						contextMap.put("IndexDrop", "false");
					}
					else {
						defaultProps.setProperty("IndexStop", "false");
						contextMap.put("IndexStop", "false");
						defaultProps.setProperty("IndexDrop", "false");
						contextMap.put("IndexDrop", "false");
					}

					if (truncate) {
						contextMap.put("truncateProject", "true");
					} else {
						contextMap.put("truncateProject", "false");
					}
					contextMap.put("PIDURL", defaultProps.getProperty("PIDURL"));

					contextMap.put("folderMain", miscPathReplaced);


					if (cleanUp) {
						defaultProps.setProperty("cleanUp", "true");
						contextMap.put("cleanUp", "true");
					} else {
						defaultProps.setProperty("cleanUp", "false");
						contextMap.put("cleanUp", "false");
					}


					if (save) {
						defaultProps.store(new FileWriter(properties), "");
					}

					if (truncate) {
						contextMap.put("truncateProject", "true");
					} else {
						contextMap.put("truncateProject", "false");
					}

					if (truncateQueries){
						contextMap.put("truncateQueries", "true");
					}
					else {
						contextMap.put("truncateQueries", "false");
					}

					b = new Thread(new Runnable() {

						@Override
						public void run() {
							started = true;
							final long start = System.currentTimeMillis();
							IDRTImport.setCompleteContext(contextMap);
							exitCode = IDRTImport.runStarLIMSImport();
							StatusListener.setStatus(0, "", "");  
							StatusListener.setSubStatus(0, "");
							StatusListener.notifyListener();
							started = false;
							if (exitCode == 0) {
								Display.getDefault().syncExec(
										new Runnable() {
											@Override
											public void run() {
												long end = System
														.currentTimeMillis();
												//
												long time = end - start;
												Log.addLog(0,
														"StarLIMS Import Finished!");

												Log.addLog(0, "Duration: "
														+ (time / 1000)
														+ " s");
												MessageDialog
												.openInformation(
														Application.getShell(),
														"Import Finished",
														"Import finished.");
											}
										});
							}
							else {
								Display.getDefault().syncExec(new Runnable() {
									@Override
									public void run() {
										long end = System.currentTimeMillis();
										//
										long time = end - start;
										//									closeBar("CSV Import Finished!", 0);
										Log.addLog(0, "StarLIMS Import Failed!");
										Log.addLog(0, "Duration: " + (time / 1000)
												+ " s");
										MessageDialog.openError(Application.getShell(),
												"Import Failed",
												"Import Failed!");
									}
								});
							}
						}
					});

					if (!started) {
						b.start();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		importThread.start();
		return true;
	}
}