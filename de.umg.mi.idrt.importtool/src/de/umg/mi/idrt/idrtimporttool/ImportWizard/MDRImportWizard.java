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
import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * Wizard for the CSV-Import.
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class MDRImportWizard extends Wizard {

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
					Log.addLog(1, "MDR Import Failed!");
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
					Log.addLog(1, "MDR Import Failed: " + error);
					MessageDialog.openError(Application.getShell(), "Import Failed!",
							"Import failed. Please see the log.");
				}
			});
		}
		started = false;
		StatusListener.notifyListener();
	}

	private HashMap<String, String> contextMap;

	//	protected WizardPage1 one;
	protected MDRWizardPage2 two;

	public static boolean started = false;

	public MDRImportWizard() {
		super();
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		//		one = new WizardPage1();
		two = new MDRWizardPage2();
		//		three = new CSVWizardPage3();

		//		addPage(one);
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
		return MDRWizardPage2.canFinish();
		//		return true;
	}

	//	public static CSVWizardPageThree getThree() {
	//		return three;
	//	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean performFinish() {

		Server selectedServer = ServerView.getSelectedServer();
		System.out.println("MDR: " + selectedServer.toString());
		final String ipText = selectedServer.getIp();
		final String passwordText = selectedServer.getPassword();
		final String dbUserText = selectedServer.getUser();
		final String dbSID = selectedServer.getSID();
		final String dbPort = selectedServer.getPort();
		final String dbSchema = selectedServer.getSchema();
		final String dbType = selectedServer.getDatabaseType();
		final String whType = selectedServer.getWhType().toLowerCase();

		//		final String ipText = WizardPage1.getIpText();
		//		final String passwordText = WizardPage1.getDBUserPasswordText();
		//		final String dbUserText = WizardPage1.getDBUserText();
		//		final String dbSID = WizardPage1.getDBSIDText();
		//		final String dbPort = WizardPage1.getPortText();
		//		final String dbSchema = WizardPage1.getDBSchemaText();

		final int mdrDesignation = MDRWizardPage2.getMDRDesignation();
		final String mdrInstance = MDRWizardPage2.getMDRInstance();
		final String mdrBaseURL = MDRWizardPage2.getMDRBaseURL();

		// saving the last table before import

		importThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					StatusListener.setStatus(1f, "Importing from MDR", "Instance: "+mdrInstance);
					File properties = FileHandler.getBundleFile("/cfg/Default.properties");
					defaultProps = new Properties();
					defaultProps.load(new FileReader(properties));

					contextMap = new HashMap<String, String>();

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

					contextMap.put("MDPDName", defaultProps.getProperty("MDPDName"));

					/**
					 * page 2
					 */
					File misc = FileHandler.getBundleFile("/misc/");
					String miscPathReplaced = misc.getAbsolutePath()
							.replaceAll("\\\\", "/") + "/";

					contextMap.put("folderMain", miscPathReplaced);

					contextMap.put("MDRStartDesignation", ""+mdrDesignation);
					defaultProps.setProperty("MDRStartDesignation", ""+mdrDesignation);

					contextMap.put("MDRBaseURL", ""+mdrBaseURL);
					defaultProps.setProperty("MDRBaseURL", ""+mdrBaseURL);
					
					contextMap.put("MDRInstance", ""+mdrInstance);
					defaultProps.setProperty("MDRInstance", ""+mdrInstance);
					b = new Thread(new Runnable() {

						@Override
						public void run() {
							StatusListener.setStatus(1f, "Importing from MDR", "Instance: "+mdrInstance);
							started = true;
							final long start = System.currentTimeMillis();
							IDRTImport.setCompleteContext(contextMap);
							exitCode = IDRTImport.runMDRImport();
							StatusListener.setStatus(100f, "Importing done:", "Instance: "+mdrInstance);
							System.out.println("exitCode: " + exitCode);
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
														"MDR Import Finished!");

												if (StatusListener.getImportErrorCounter()>0) {
													Log.addLog(0,
															StatusListener.getImportErrorCounter() + " Errors occured " +
															"during import!");
												}
												Log.addLog(0, "Duration: "
														+ (time / 1000)
														+ " s");
												if (StatusListener.getImportErrorCounter()>0) {
													MessageDialog
													.openInformation(
															Application.getShell(),
															"Import Finished",
															"Import finished.\n"+StatusListener.getImportErrorCounter() 
															+ " Errors occured!");
													StatusListener.setImportErrorCounter(0);
												}
												else {
													MessageDialog
													.openInformation(
															Application.getShell(),
															"Import Finished",
															"Import finished.");
												}
												StatusListener.setStatus(0, "", "");  
												StatusListener.setSubStatus(0, "");
												StatusListener.notifyListener();
												started = false;
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
										Log.addLog(0, "MDR Import Failed!");
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