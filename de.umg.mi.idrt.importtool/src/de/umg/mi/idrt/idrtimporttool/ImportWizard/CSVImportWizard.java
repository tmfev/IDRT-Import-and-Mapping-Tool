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

import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

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
	protected WizardPageOne one;
	protected CSVWizardPageTwo two;
	public static CSVWizardPageThree three;
	public static boolean started = false;

	public CSVImportWizard() {
		super();
		setNeedsProgressMonitor(false);

	}

	@Override
	public void addPages() {
		one = new WizardPageOne();
		two = new CSVWizardPageTwo();
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
		if (CSVWizardPageThree.lastTable != null) {
			CSVWizardPageThree.saveTable();
			System.out.println("cancel, lasttable!=null");
		} else {
			System.out.println("cancel, lasttable == null");
		}
		return super.performCancel();
	}

	@Override
	public boolean performFinish() {

		final String ipText = WizardPageOne.getIpText();
		final String passwordText = WizardPageOne.getDBUserPasswordText();
		final String dbUserText = WizardPageOne.getDBUserText();
		final String dbSID = WizardPageOne.getDBSIDText();
		final String dbPort = WizardPageOne.getPortText();
		final String dbSchema = WizardPageOne.getDBSchemaText();
		final boolean truncate = CSVWizardPageTwo.getTruncate();
		final boolean cleanUp = CSVWizardPageTwo.getCleanUp();
		final String pattern = CSVWizardPageTwo.getPattern();
		final String quoteChar = CSVWizardPageTwo.getQuoteCharText();
		final boolean usePIDGenerator = CSVWizardPageTwo.getUsePid();

		final boolean IDinCSVFile = CSVWizardPageTwo.getBtnRADIOCsvfile();
		final boolean IDinExternalIDFile = CSVWizardPageTwo.getBtnRADIOIdfile();

		final String folderCSV = CSVWizardPageTwo.getFolderCSVText();
		final char delim = CSVWizardPageThree.getDEFAULTDELIM();

		final String externalIDFilePath = CSVWizardPageTwo
				.getExternalIDFilePath();
		final boolean terms = CSVWizardPageTwo.getTerms();
		final boolean save = CSVWizardPageTwo.getSaveContext();

		// saving the last table before import
		CSVWizardPageThree.saveTable();

		final List<String> configs = new LinkedList<String>();

		HashMap<String, String> configMap = CSVWizardPageThree
				.getFileConfigMap();

		Iterator<String> configIt = configMap.keySet().iterator();

		while (configIt.hasNext()) {
			String currentConfig = configIt.next();
			configs.add(configMap.get(currentConfig));
			System.out.println("added " + configMap.get(currentConfig));
		}

		importThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("CSV: finish");
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
							System.out.println("deleting input file: "
									+ listOfInputFile.getName());
							boolean delete = listOfInputFile.delete();
							System.out.println("success? " + delete);
						}
					}

					fileUrl = FileLocator.toFileURL(url);
					File properties = new File(fileUrl.getPath());
					defaultProps = new Properties();
					defaultProps.load(new FileReader(properties));

					contextMap = new HashMap<String, String>();

					/*
					 * page 1
					 */
					contextMap.put("DBHost", ipText);
//					defaultProps.setProperty("DBHost", ipText);

					contextMap.put("DBPassword", passwordText);
					// defaultProps.setProperty("pw",
					// WizardPageOne.getDBUserPasswordText());

					contextMap.put("DBUsername", dbUserText);
//					defaultProps.setProperty("DBUsername", dbUserText);

					contextMap.put("DBInstance", dbSID);
//					defaultProps.setProperty("DBInstance", dbSID);

					contextMap.put("DBPort", dbPort);
//					defaultProps.setProperty("DBPort", dbPort);

					contextMap.put("DBSchema", dbSchema);
//					defaultProps.setProperty("DBSchema", dbSchema);
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

						/*
						 * ST-Import
						 */
						Path cfgPath = new Path("/cfg/"); //$NON-NLS-1$
						URL cfgUrl = FileLocator.find(bundle, cfgPath,
								Collections.EMPTY_MAP);
						URL cfgFileUrl = FileLocator.toFileURL(cfgUrl);

						File rootDir = new File(cfgFileUrl.getPath()
								+ "/Standardterminologien/".replaceAll("\\\\",
										"/"));

						System.out.println("**********************");
						System.out.println("STFOLDER: "
								+ rootDir.getAbsolutePath().replaceAll("\\\\",
										"/"));
						System.out.println("**********************");

						File icd10Dir = new File(rootDir.getAbsolutePath()
								+ "/ICD-10-GM/");
						contextMap.put("icd10Dir", icd10Dir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("icd10Dir", icd10Dir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");

						File tnmDir = new File(rootDir.getAbsolutePath()
								+ "/TNM/");
						contextMap.put("tnmDir", tnmDir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("tnmDir", tnmDir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");

						contextMap.put("rootDir", rootDir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("rootDir", rootDir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");

						File loincDir = new File(rootDir.getAbsolutePath()
								+ "/LOINC/");
						contextMap.put("loincDir", loincDir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("loincDir", loincDir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");

						File opsDir = new File(rootDir.getAbsolutePath()
								+ "/OPS/");
						contextMap.put("opsDir", opsDir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("opsDir", opsDir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");

						File p21Dir = new File(rootDir.getAbsolutePath()
								+ "/P21/");
						contextMap.put("p21Dir", p21Dir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("p21Dir", p21Dir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");

						File drgDir = new File(rootDir.getAbsolutePath()
								+ "/DRG/");
						contextMap.put("drgDir", drgDir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("drgDir", drgDir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");

						File icdoDir = new File(rootDir.getAbsolutePath()
								+ "/ICD-O-3/");
						contextMap.put("icdoDir", icdoDir.getAbsolutePath()
								.replaceAll("\\\\", "/") + "/");
//						defaultProps.setProperty("icdoDir", icdoDir
//								.getAbsolutePath().replaceAll("\\\\", "/")
//								+ "/");
					} 

					if (truncate) {
						System.out.println("truncating");
						contextMap.put("truncateProject", "true");
//						defaultProps.setProperty("truncateProject", "true");
					} else {
						contextMap.put("truncateProject", "false");
//						defaultProps.setProperty("truncateProject", "false");
					}
					contextMap.put("PIDURL", defaultProps.getProperty("PIDURL"));
					contextMap.put("folderMain", miscPathReplaced);
//					defaultProps.setProperty("folderMainCSV", miscPathReplaced);

					contextMap.put("datePattern", pattern);
					defaultProps.setProperty("datePattern", pattern);

					contextMap.put("quoteChar", quoteChar);
					defaultProps.setProperty("quoteChar", quoteChar);
					// WITH PIDGEN!
					if (usePIDGenerator && IDinCSVFile) {
						System.out.println("WITH PIDGEN");
						// Int. Import

						System.out.println("CONFIGS: " + configs);
						System.out.println("SIZE: " + configs.size());
						for (int i = 0; i < configs.size(); i++) {
							for (File listOfInputFile : listOfInputFiles) {
								if (listOfInputFile.getName().endsWith(".csv")) {
									System.out.println("deleting input file: "
											+ listOfInputFile.getName());
									boolean delete = listOfInputFile.delete();
									System.out.println("success? " + delete);
								}
							}

							if (i > 0) {
								contextMap.put("truncateProject", "false");
//								defaultProps.setProperty("truncateProject",
//										"false");
							}
							String currentConfig = configs.get(i);
							System.out.println("config: " + currentConfig
									+ " -- RUN: " + i + " von "
									+ configs.size());
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
									System.out
									.println("FIRSTNAME " + FIRSTNAME);
								}
								if (line4[j].equals("LastName")) {
									NAME = j;
									System.out.println("NAME: " + NAME);
								}
								if (line4[j].equals("Reporting Date")) {
									REPDATE = j;
									System.out.println("REPDATE: " + REPDATE);
								}
								if (line4[j].equals("Birthday")) {
									BDAY = j;
									System.out.println("BDAY: " + BDAY);
								}
								if (line4[j].equals("Sex")) {
									SEX = j;
									System.out.println("SEX: " + SEX);
								}
							}
							if ((FIRSTNAME < 0) || (NAME < 0) || (REPDATE < 0)
									|| (BDAY < 0) || (SEX < 0)) {
								System.out.println("NONONONO");
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
								defaultProps.setProperty("pidgen", "true");
								contextMap.put("idFile", outputFilename);
								defaultProps.setProperty("idFile",
										outputFilename);

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
							System.out.println("STARTING CSVIMPORT");
							exitCode = IDRTImport.runCSVImport(terms);
							System.out.println("CSVIMPORT FINISHED!");
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
							System.out.println("GetPid==true");
							contextMap.put("pidgen", "true");
							defaultProps.setProperty("pidgen", "true");
							contextMap.put("idFile", externalIDFilePath);
							defaultProps.setProperty("idFile",
									externalIDFilePath);
						}
						else {
							System.out.println("GetPid==false");
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
							System.out.println("saving context");
							defaultProps.store(new FileWriter(properties), "");
						}

						if (truncate) {
							System.out.println("truncating");
							contextMap.put("truncateProject", "true");
//							defaultProps.setProperty("truncateProject", "true");
						} else {
							contextMap.put("truncateProject", "false");
//							defaultProps
//							.setProperty("truncateProject", "false");
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
								// progressBar.close();
								if (exitCode == 0) {
									Display.getDefault().syncExec(
											new Runnable() {

												@Override
												public void run() {
													long end = System
															.currentTimeMillis();
													//
													long time = end - start;
//													closeBar(
//															"CSV Import Finished!",
//															0);
													Log.addLog(0,
															"CSV Import Finished!");
													Log.addLog(0, "Duration: "
															+ (time / 1000)
															+ " s");
													MessageDialog
													.openInformation(
															Display.getDefault()
															.getActiveShell(),
															"Import Finished",
															"Import finished.");
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

	public static CSVWizardPageThree getThree() {
		return three;
	}

	/**
	 * Brutally kills the the import thread.
	 */
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

	public static void setThree(CSVWizardPageThree three) {
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