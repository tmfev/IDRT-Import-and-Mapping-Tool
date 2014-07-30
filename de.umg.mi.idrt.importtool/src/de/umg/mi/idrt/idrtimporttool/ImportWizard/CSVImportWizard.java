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
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
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
public class CSVImportWizard extends Wizard {

	private static int SEX = -1;
	private static int FIRSTNAME = -1;
	private static int NAME = -1;
	private static int REPDATE = -1;
	private static int BDAY = -1;

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
	
	//	protected WizardPage1 one;
	protected CSVWizardPage2 two;
	public static CSVWizardPage3 three;

	public static boolean started = false;

	public static void setThree(CSVWizardPage3 three) {
		CSVImportWizard.three = three;
	}


	public CSVImportWizard() {
		super();
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
//		one = new WizardPage1();
		three = new CSVWizardPage3();
		two = new CSVWizardPage2();
//		addPage(one);
		addPage(two);
		addPage(three);
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
		return (three != null && three.isPageComplete());
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
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean performCancel() {
		if (CSVWizardPage3.lastTable != null) {
			CSVWizardPage3.saveTable();
		}
		return super.performCancel();
	}

	@Override
	public boolean performFinish() {

		Server selectedServer = ServerView.getSelectedServer();
		final String ipText = selectedServer.getIp();
		final String passwordText = selectedServer.getPassword();
		final String dbUserText = selectedServer.getUser();
		final String dbSID = selectedServer.getSID();
		final String dbPort = selectedServer.getPort();
		final String dbSchema = selectedServer.getSchema();
		final String dbType = selectedServer.getDatabaseType();
		
		
//		final String ipText = WizardPage1.getIpText();
//		final String passwordText = WizardPage1.getDBUserPasswordText();
//		final String dbUserText = WizardPage1.getDBUserText();
//		final String dbSID = WizardPage1.getDBSIDText();
//		final String dbPort = WizardPage1.getPortText();
//		final String dbSchema = WizardPage1.getDBSchemaText();
		final boolean truncate = CSVWizardPage2.getTruncate();
		final boolean truncateQueries = CSVWizardPage2.getTruncateQueries();
		final boolean cleanUp = CSVWizardPage2.getCleanUp();
		final String pattern = CSVWizardPage2.getPattern();
	
		final String quoteChar = CSVWizardPage2.getQuoteCharText();
		final boolean usePIDGenerator = CSVWizardPage2.getUsePid();

		final boolean IDinCSVFile = CSVWizardPage2.getBtnRADIOCsvfile();
		final boolean IDinExternalIDFile = CSVWizardPage2.getBtnRADIOIdfile();

		final String folderCSV = CSVWizardPage2.getFolderCSVText();
		final char delim = CSVWizardPage3.getDEFAULTDELIM();

		final String externalIDFilePath = CSVWizardPage2
				.getExternalIDFilePath();
		final boolean terms = CSVWizardPage2.getTerms();
		final boolean save = CSVWizardPage2.getSaveContext();

		// saving the last table before import
		CSVWizardPage3.saveTable();

		final List<String> configs = new LinkedList<String>();

		HashMap<String, String> configMap = CSVWizardPage3
				.getFileConfigMap();

		Iterator<String> configIt = configMap.keySet().iterator();

		while (configIt.hasNext()) {
			String currentConfig = configIt.next();
			configs.add(configMap.get(currentConfig));
		}

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
					contextMap.put("MDPDName", defaultProps.getProperty("MDPDName"));

					/**
					 * page 2
					 */
					File misc = FileHandler.getBundleFile("/misc/");
					String miscPathReplaced = misc.getAbsolutePath()
							.replaceAll("\\\\", "/") + "/";

					if (terms) {
						/**
						 * ST-Import
						 */
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

					if (truncate) {
						contextMap.put("truncateProject", "true");
					} else {
						contextMap.put("truncateProject", "false");
					}
					contextMap.put("PIDURL", defaultProps.getProperty("PIDURL"));

					contextMap.put("folderMain", miscPathReplaced);

					contextMap.put("datePattern", pattern);
					defaultProps.setProperty("datePattern", pattern);
					
					contextMap.put("quoteChar", quoteChar);
					defaultProps.setProperty("quoteChar", quoteChar);
					// WITH PIDGEN!
					if (usePIDGenerator && IDinCSVFile) {
						// Int. Import

						for (int i = 0; i < configs.size(); i++) {
							for (File listOfInputFile : listOfInputFiles) {
								if (listOfInputFile.getName().endsWith(".csv")) {
									boolean delete = listOfInputFile.delete();
								}
							}

							if (i > 0) {
								contextMap.put("truncateProject", "false");
							}
							String currentConfig = configs.get(i);
							CSVReader readerConfig = new CSVReader(
									new FileReader(new File(folderCSV
											+ currentConfig)), delim);
							readerConfig.readNext();
							readerConfig.readNext();
							readerConfig.readNext();
							readerConfig.readNext();
							String[] line4 = readerConfig.readNext();
							readerConfig.close();
							for (int j = 0; j < line4.length; j++) {
								if (line4[j].equals("FirstName")) {
									FIRSTNAME = j;
								}
								if (line4[j].equals("LastName")) {
									NAME = j;
								}
								if (line4[j].equals("Reporting Date")) {
									REPDATE = j;
								}
								if (line4[j].equals("Birthday")) {
									BDAY = j;
								}
								if (line4[j].equals("Sex")) {
									SEX = j;
								}
							}
							if ((FIRSTNAME < 0) || (NAME < 0) || (REPDATE < 0)
									|| (BDAY < 0) || (SEX < 0)) {
							} else {
								String inputFolderReplaced = inputFolder
										.getAbsolutePath().replaceAll("\\\\",
												"/")
												+ "/";
								String outputFolderReplaced = outputFolder
										.getAbsolutePath().replaceAll("\\\\",
												"/")
												+ "/";
								String tmpElement = currentConfig;
								String filename = tmpElement.substring(0,
										tmpElement.indexOf("."));
								filename = filename + ".csv";
								String outputFilename = outputFolderReplaced
										+ "idFile_" + filename;

								contextMap.put("pidgen", "true");
								contextMap.put("idFile", outputFilename);

								contextMap
								.put("folderCSV", inputFolderReplaced);
								defaultProps.setProperty("folderCSV",
										inputFolderReplaced);

								File inputFile = new File(folderCSV + filename);

								if (inputFile.exists()) {
									CSVReader reader2 = new CSVReader(
											new FileReader(inputFile), delim);
									CSVWriter writer = new CSVWriter(
											new FileWriter(new File(
													outputFilename)), ';');
									String[] nextLine = reader2.readNext();
									String[] output = new String[6];
									int k = 0;

									while ((nextLine = reader2.readNext()) != null) {
										output[0] = "" + k;
										output[1] = nextLine[FIRSTNAME - 1];
										output[2] = nextLine[NAME - 1];
										output[3] = nextLine[SEX - 1];
										output[4] = nextLine[BDAY - 1];
										output[5] = nextLine[REPDATE - 1];
										k++;
										writer.writeNext(output);
									}

									reader2.close();
									writer.close();

									copyFile(new File(folderCSV + filename),
											new File(inputFolderReplaced + "/"
													+ filename));
									copyFile(
											new File(folderCSV + currentConfig),
											new File(inputFolderReplaced + "/"
													+ currentConfig));
								}
							}

							if (save) {
								defaultProps
								.setProperty("folderCSV", folderCSV);
								defaultProps.store(new FileWriter(properties),
										"");
							}

							// b = new Thread(new Runnable() {
							// @Override
							// public void run() {
							started = true;

							IDRTImport.setCompleteContext(contextMap);
							exitCode = IDRTImport.runCSVImport(terms);
							StatusListener.notifyListener();
							started = false;

							if (exitCode == 0) {
								continue;
							} else {
								break;
							}
						}
						if (exitCode == 0) {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									long end = System.currentTimeMillis();
									//
									long time = end - start;
									//									closeBar("CSV Import Finished!", 0);
									Log.addLog(0, "CSV Import Finished!");
									Log.addLog(0, "Duration: " + (time / 1000)
											+ " s");
									MessageDialog.openInformation(Application.getShell(),
											"Import Finished",
											"Import finished.");
								}
							});
						}
					}

					// WITHOUT PIDGEN
					else {
						contextMap.put("folderCSV", folderCSV);
						defaultProps.setProperty("folderCSV", folderCSV);

						if (usePIDGenerator && IDinExternalIDFile) {
							contextMap.put("pidgen", "true");
							defaultProps.setProperty("pidgen", "true");
							contextMap.put("idFile", externalIDFilePath);
							defaultProps.setProperty("idFile",
									externalIDFilePath);
						}
						else {
							contextMap.put("pidgen", "false");
							defaultProps.setProperty("pidgen", "false");
						}

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
								exitCode = IDRTImport.runCSVImport(terms);
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
															"CSV Import Finished!");

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
												}
											});
								}
							}
						});

						if (!started) {
							b.start();
						}
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