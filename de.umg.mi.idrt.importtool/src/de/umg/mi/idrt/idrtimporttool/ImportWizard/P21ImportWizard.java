package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
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

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class P21ImportWizard extends Wizard {

	private Properties defaultProps;
	private static Thread b;
	private HashMap<String, String> contextMap;
	protected WizardPageOne one;
	protected P21WizardPageTwo two;
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
					.openError(Display.getDefault().getActiveShell(),
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
					MessageDialog.openError(Display.getDefault()
							.getActiveShell(), "Import Failed!",
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
		one = new WizardPageOne();
		two = new P21WizardPageTwo();
		addPage(one);
		addPage(two);
	}

	@Override
	public boolean canFinish() {
		return super.canFinish();
	}

	@Override
	public boolean performFinish() {
		try {
			final boolean cleanUp = P21WizardPageTwo.getCleanUp();
			final boolean terms = P21WizardPageTwo.getTerms();
			final Bundle bundle = Activator.getDefault().getBundle();
			final String pattern = P21WizardPageTwo.getPattern(); 
			Path inputPath = new Path("/misc/input/"); //$NON-NLS-1$
			URL inputURL = FileLocator.find(bundle, inputPath,
					Collections.EMPTY_MAP);
			URL inputURL2 = FileLocator.toFileURL(inputURL);
			File inputFolder = new File(inputURL2.getPath());
			File[] listOfInputFiles = inputFolder.listFiles();

			for (File listOfInputFile : listOfInputFiles) {
				if (listOfInputFile.getName().endsWith(".csv")) {
					listOfInputFile.delete();
				}
			}

			Path path = new Path("/cfg/Default.properties"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = null;

			fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			contextMap = new HashMap<String, String>();
			/**
			 * page 1
			 */
			contextMap.put("DBHost", WizardPageOne.getIpText());
			contextMap.put("DBPassword", WizardPageOne.getDBUserPasswordText());
			contextMap.put("DBUsername", WizardPageOne.getDBUserText());
			contextMap.put("DBInstance", WizardPageOne.getDBSIDText());
			contextMap.put("DBPort", WizardPageOne.getPortText());
			contextMap.put("DBSchema", WizardPageOne.getDBSchemaText());

			/**
			 * page 2
			 */

			Path tmpPath = new Path("/misc/input/"); 
			URL tmpurl = FileLocator.find(bundle, tmpPath,
					Collections.EMPTY_MAP);
			URL tmpurl2 = FileLocator.toFileURL(tmpurl);
			File tmpFolder = new File(tmpurl2.getPath());
			CSVPath = tmpFolder.getAbsolutePath();
			CSVPath = CSVPath.replaceAll("\\\\", "/") + "/";

			Path miscPath = new Path("/misc/"); 
			URL miscUrl = FileLocator.find(bundle, miscPath,
					Collections.EMPTY_MAP);
			URL miscFileUrl = null;

			miscFileUrl = FileLocator.toFileURL(miscUrl);
			File misc = new File(miscFileUrl.getPath());
			String miscPathReplaced = misc.getAbsolutePath().replaceAll("\\\\",
					"/")
					+ "/";
			version = P21WizardPageTwo.getP21VersionCombo();
			p21inputFolderPath	= P21WizardPageTwo.getFolderCSVText();

			if (P21WizardPageTwo.getTerms()) {
				/**
				 * ST-Import
				 */
				Path cfgPath = new Path("/cfg/"); 
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
			if (cleanUp) {
				defaultProps.setProperty("cleanUp", "true");
				contextMap.put("cleanUp", "true");
			} else {
				defaultProps.setProperty("cleanUp", "false");
				contextMap.put("cleanUp", "false");
			}
			contextMap.put("p21_input", P21WizardPageTwo.getFolderCSVText());
			defaultProps.setProperty("p21_input", P21WizardPageTwo.getFolderCSVText());
			
			contextMap.put("datePattern", pattern);
			defaultProps.setProperty("datePattern", pattern);
			
			contextMap.put("p21_output", CSVPath);

			if (P21WizardPageTwo.getSaveContext()) {
				defaultProps.store(new FileWriter(properties), "");
			}
			if (P21WizardPageTwo.getTruncate()) {
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
					try {
						StatusListener.setStatus(1, "Preparing §21 Import", "");  
						Path p21Path = new Path("/cfg/p21/" + version+"/");
						URL p21url = FileLocator.find(bundle, p21Path,
								Collections.EMPTY_MAP);
						URL p21url2;

						p21url2 = FileLocator.toFileURL(p21url);

						File p21Folder = new File(p21url2.getPath());
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
					} catch (IOException e) {
						e.printStackTrace();
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
								MessageDialog.openInformation(Display
										.getDefault().getActiveShell(),
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