package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;

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

	private Properties defaultProps;
	private static Thread b;
	private static Thread importThread;
	private HashMap<String, String> contextMap;
	protected WizardPage1 one;
	protected CSVWizardPage2 two;
	public static CSVWizardPage3 three;
	public static boolean started = false;

	public CSVImportWizard() {
		super();
		setNeedsProgressMonitor(false);

	}

	@Override
	public void addPages() {
		one = new WizardPage1();
		two = new CSVWizardPage2();
		addPage(one);
		addPage(two);
	}

	//
	@Override
	public boolean canFinish() {
		return (three != null);
	}

	@Override
	public void dispose() {
		super.dispose();
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

		final String ipText = WizardPage1.getIpText();
		final String passwordText = WizardPage1.getDBUserPasswordText();
		final String dbUserText = WizardPage1.getDBUserText();
		final String dbSID = WizardPage1.getDBSIDText();
		final String dbPort = WizardPage1.getPortText();
		final String dbSchema = WizardPage1.getDBSchemaText();
		final boolean truncate = CSVWizardPage2.getTruncate();
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

					Bundle bundle = Activator.getDefault().getBundle();
					Path path = new Path("/cfg/Default.properties"); //$NON-NLS-1$
					URL url = FileLocator.find(bundle, path,
							Collections.EMPTY_MAP);
					URL fileUrl = null;

					Path inputPath = new Path("/misc/input/"); //$NON-NLS-1$
					URL inputURL = FileLocator.find(bundle, inputPath,
							Collections.EMPTY_MAP);
					URL inputURL2 = FileLocator.toFileURL(inputURL);
					File inputFolder = new File(inputURL2.getPath());

					Path outputPath = new Path("/misc/output/"); //$NON-NLS-1$
					URL outputURL = FileLocator.find(bundle, outputPath,
							Collections.EMPTY_MAP);
					URL outputURL2 = FileLocator.toFileURL(outputURL);
					File outputFolder = new File(outputURL2.getPath());

					File[] listOfInputFiles = inputFolder.listFiles();

					for (File listOfInputFile : listOfInputFiles) {
						if (listOfInputFile.getName().endsWith(".csv")) {
							boolean delete = listOfInputFile.delete();
						}
					}

					fileUrl = FileLocator.toFileURL(url);
					File properties = new File(fileUrl.getPath());
					defaultProps = new Properties();
					defaultProps.load(new FileReader(properties));

					contextMap = new HashMap<String, String>();

					/**
					 * page 1
					 */
					contextMap.put("DBHost", ipText);
					contextMap.put("DBPassword", passwordText);
					contextMap.put("DBUsername", dbUserText);
					contextMap.put("DBInstance", dbSID);
					contextMap.put("DBPort", dbPort);
					contextMap.put("DBSchema", dbSchema);

					/**
					 * page 2
					 */
					Path miscPath = new Path("/misc/"); //$NON-NLS-1$
					URL miscUrl = FileLocator.find(bundle, miscPath,
							Collections.EMPTY_MAP);
					URL miscFileUrl = FileLocator.toFileURL(miscUrl);
					File misc = new File(miscFileUrl.getPath());
					String miscPathReplaced = misc.getAbsolutePath()
							.replaceAll("\\\\", "/") + "/";

					if (terms) {
						/**
						 * ST-Import
						 */
						Path cfgPath = new Path("/cfg/"); //$NON-NLS-1$
						URL cfgUrl = FileLocator.find(bundle, cfgPath,
								Collections.EMPTY_MAP);
						URL cfgFileUrl = FileLocator.toFileURL(cfgUrl);
						File rootDir = new File(cfgFileUrl.getPath()
								+ "/Standardterminologien/".replaceAll("\\\\", "/"));
						contextMap.put("icd10Dir", rootDir.getAbsolutePath()+ "/ICD-10-GM/" + "/");
						contextMap.put("tnmDir",rootDir.getAbsolutePath() + "/TNM/" + "/");
						contextMap.put("rootDir",rootDir.getAbsolutePath()+ "/");
						contextMap.put("loincDir", rootDir.getAbsolutePath() + "/LOINC/" + "/");
						contextMap.put("opsDir",rootDir.getAbsolutePath() + "/OPS/" + "/");
						contextMap.put("p21Dir",rootDir.getAbsolutePath() + "/P21/" + "/");
						contextMap.put("drgDir",rootDir.getAbsolutePath() + "/DRG/" + "/");
						contextMap.put("icdoDir",rootDir.getAbsolutePath() + "/ICD-O-3/" + "/");
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
									MessageDialog.openInformation(Display
											.getDefault().getActiveShell(),
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
																Display.getDefault()
																.getActiveShell(),
																"Import Finished",
																"Import finished.\n"+StatusListener.getImportErrorCounter() 
																+ " Errors occured!");
														StatusListener.setImportErrorCounter(0);
													}
													else {
														MessageDialog
														.openInformation(
																Display.getDefault()
																.getActiveShell(),
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

	//	private static void closeBar(String msg, int status) {
	//		closeBar(msg, "", status);
	//	}

	//	private static void closeBar(String msg, final String fileName, int status) {
	//		ServerView.closeBar(msg, fileName, status);
	//	}

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

//	public static CSVWizardPageThree getThree() {
//		return three;
//	}

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
					.openError(Display.getDefault().getActiveShell(),
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
					MessageDialog.openError(Display.getDefault()
							.getActiveShell(), "Import Failed!",
							"Import failed. Please see the log.");
				}
			});
		}
		started = false;
		StatusListener.notifyListener();
	}

	public static void setThree(CSVWizardPage3 three) {
		CSVImportWizard.three = three;
	}

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
}