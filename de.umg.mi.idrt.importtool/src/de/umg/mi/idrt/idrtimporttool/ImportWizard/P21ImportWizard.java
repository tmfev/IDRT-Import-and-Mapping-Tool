package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class P21ImportWizard extends Wizard {

	private Properties defaultProps;
	private static Thread b;
	private HashMap<String, String> contextMap;
//	protected WizardPage1 one;
	protected P21WizardPage2 two;
	public boolean done = false;
	public static boolean started = false;
	private static String CSVPath;
	private static String version;
	private static String p21inputFolderPath;


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

	@SuppressWarnings("deprecation")
	public static void killThread() {

		if (started) {
			b.stop();

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					Log.addLog(1, "§21 Import Failed!");
					MessageDialog
					.openError(Application.getShell(),
							"Import Failed!",
							"Import failed. (User aborted!)\nYou might want to redo your last action!");
				}
			});
		}
		started = false;
		StatusListener.notifyListener();
	}

	@SuppressWarnings("deprecation")
	public static void killThreadRemote(final String error,
			final String fileName) {

		if (started) {
			b.stop();
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					Log.addLog(1, "§21 Import Failed: " + error);
					MessageDialog.openError(Application.getShell(), "Import Failed!",
							"Import failed. Please see the log.");
				}
			});
		}
		started = false;
		StatusListener.notifyListener();
	}

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

	public P21ImportWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
//		one = new WizardPage1();
		two = new P21WizardPage2();
//		addPage(one);
		addPage(two);
	}

	@Override
	public boolean canFinish() {
		return super.canFinish();
	}

	@Override
	public boolean performFinish() {
		try {
			final boolean cleanUp = P21WizardPage2.getCleanUp();
			final boolean terms = P21WizardPage2.getTerms();
			final String pattern = P21WizardPage2.getPattern(); 
			File inputFolder = FileHandler.getBundleFile("/misc/input/");
			File[] listOfInputFiles = inputFolder.listFiles();

			for (File listOfInputFile : listOfInputFiles) {
				if (listOfInputFile.getName().endsWith(".csv")) {
					listOfInputFile.delete();
				}
			}
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			contextMap = new HashMap<String, String>();
			/**
			 * page 1
			 */
			
			Server selectedServer = ServerView.getSelectedServer();
			final String ipText = selectedServer.getIp();
			final String passwordText = selectedServer.getPassword();
			final String dbUserText = selectedServer.getUser();
			final String dbSID = selectedServer.getSID();
			final String dbPort = selectedServer.getPort();
			final String dbSchema = selectedServer.getSchema();
			
			contextMap.put("DBHost", ipText);
			contextMap.put("DBPassword", passwordText);
			contextMap.put("DBUsername", dbUserText);
			contextMap.put("DBInstance", dbSID);
			contextMap.put("DBPort", dbPort);
			contextMap.put("DBSchema", dbSchema);
			contextMap.put("MDPDName", defaultProps.getProperty("MDPDName"));

//			contextMap.put("DBHost", WizardPage1.getIpText());
//			contextMap.put("DBPassword", WizardPage1.getDBUserPasswordText());
//			contextMap.put("DBUsername", WizardPage1.getDBUserText());
//			contextMap.put("DBInstance", WizardPage1.getDBSIDText());
//			contextMap.put("DBPort", WizardPage1.getPortText());
//			contextMap.put("DBSchema", WizardPage1.getDBSchemaText());

			/**
			 * page 2
			 */

			File tmpFolder = FileHandler.getBundleFile("/misc/input/");
			CSVPath = tmpFolder.getAbsolutePath();
			CSVPath = CSVPath.replaceAll("\\\\", "/") + "/";

			File misc = FileHandler.getBundleFile("/misc/");
			String miscPathReplaced = misc.getAbsolutePath().replaceAll("\\\\",
					"/")
					+ "/";
			version = P21WizardPage2.getP21VersionCombo();
			p21inputFolderPath	= P21WizardPage2.getFolderCSVText();

			if (P21WizardPage2.getTerms()) {
				/**
				 * ST-Import
				 */
				File rootDir =FileHandler.getBundleFile("/cfg/Standardterminologien/");
				contextMap.put("icd10Dir", rootDir.getAbsolutePath().replaceAll("\\\\", "/")+ "/ICD-10-GM/" + "/");
				contextMap.put("tnmDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/TNM/" + "/");
				contextMap.put("rootDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/")+ "/");
				contextMap.put("loincDir", rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/LOINC/" + "/");
				contextMap.put("opsDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/OPS/" + "/");
				contextMap.put("p21Dir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/P21/" + "/");
				contextMap.put("drgDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/DRG/" + "/");
				contextMap.put("icdoDir",rootDir.getAbsolutePath().replaceAll("\\\\", "/") + "/ICD-O-3/" + "/");
			}
			if (cleanUp) {
				defaultProps.setProperty("cleanUp", "true");
				contextMap.put("cleanUp", "true");
			} else {
				defaultProps.setProperty("cleanUp", "false");
				contextMap.put("cleanUp", "false");
			}
			contextMap.put("p21_input", P21WizardPage2.getFolderCSVText());
			defaultProps.setProperty("p21_input", P21WizardPage2.getFolderCSVText());
			
			contextMap.put("datePattern", pattern);
			defaultProps.setProperty("datePattern", pattern);
			
			contextMap.put("p21_output", CSVPath);

			if (P21WizardPage2.getSaveContext()) {
				defaultProps.store(new FileWriter(properties), "");
			}
			if (P21WizardPage2.getTruncate()) {
				contextMap.put("truncateProject", "true");
			} else {
				contextMap.put("truncateProject", "false");
			}

			contextMap.put("folderMain", miscPathReplaced);
			contextMap.put("folderCSV", CSVPath);

			done = false;
			b = new Thread(new Runnable() {

				@Override
				public void run() {
					started = true;
					final long start = System.currentTimeMillis();
					StatusListener.setStatus(1, "Preparing §21 Import", "");  
					File p21Folder = FileHandler.getBundleFile("/cfg/p21/" + version+"/");
					File[] listOfFiles = p21Folder.listFiles();

					for (File listOfFile : listOfFiles) {
						if (listOfFile.getName().endsWith(".cfg.csv")) {
							File output = new File(CSVPath + listOfFile.getName().toLowerCase());
							if (listOfFile.getName().toLowerCase().startsWith("fall")) {
								output = new File(CSVPath + "_"+listOfFile.getName().toLowerCase());
							}
							copyFile(listOfFile, output);
						}
					}

					File p21InputFolder = new File(p21inputFolderPath);
					File[] listOfP21InputFiles = p21InputFolder.listFiles();

					for (File listOfFile : listOfP21InputFiles) {
						if (listOfFile.getName().endsWith(".csv")) {
							File output = new File(CSVPath + listOfFile.getName().toLowerCase());

							if (listOfFile.getName().toLowerCase().startsWith("fall")) {
								output = new File(CSVPath + "_"+listOfFile.getName().toLowerCase());
							}
							copyFile(listOfFile, output);
						}
					}

					IDRTImport.setCompleteContext(contextMap);
				
					int exitCode = IDRTImport.runP21Import(version, terms);
					done = true;
					StatusListener.setStatus(0, "", "");  
					StatusListener.setSubStatus(0.0f, "");
					StatusListener.notifyListener();
					started = false;
					if (exitCode == 0) {
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								long end = System.currentTimeMillis();
								long time = end - start;
								Log.addLog(0, "§21 Import Finished!");
								Log.addLog(0, "Duration: " + (time / 1000)
										+ " s");
								StatusListener.setSubStatus(0.0f, "");
								MessageDialog.openInformation(Application.getShell(),
										"Import Finished", "Import finished.");
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

		return true;
	}
}