package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
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

import au.com.bytecode.opencsv.CSVWriter;
import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.ExportDB.ExportConfig;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.importidrt.ServerView;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DBImportWizard extends Wizard {

	public static DBWizardPageThree getThree() {
		return three;
	}

	/**
	 * Kills the active Import.
	 */
	@SuppressWarnings("deprecation")
	public static void killThread() {

		if (started) {
			b.stop();
			// progressBar.close();

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					//					closeBar("DB Import Failed!", 1);
					Log.addLog(1, "DB Import Failed!");
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
			// progressBar.close();

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					//					closeBar(error, fileName, 1);
					Log.addLog(1, "DB Import Failed: " + error);
					MessageDialog.openError(Display.getDefault()
							.getActiveShell(), "Import Failed!",
							"Import failed. Please see the log.");
				}
			});
		}
		StatusListener.notifyListener();
		started = false;
	}

	/**
	 * Updates the progress bar.
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
	private HashMap<String, String> contextMap;
	protected WizardPageOne one;

	public static DBWizardPageTwo two;
	public static DBWizardPageThree three;
	public boolean done = false;
	public static boolean started = false;

	private static String FOLDERCSV;

	public static void setThree(DBWizardPageThree three) {
		DBImportWizard.three = three;
	}

	public DBImportWizard() {
		super();
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		one = new WizardPageOne();
		two = new DBWizardPageTwo();
		addPage(one);
		addPage(two);
	}

	@Override
	public boolean canFinish() {
		return (three != null);
	}

	private void createDBExportConfigCSV(List<ExportConfig> exportConfigs,
			String fileName) {
		try {
			File file = new File(fileName);
System.out.println("filename: " + file.getAbsolutePath());
			CSVWriter writer = new CSVWriter(new FileWriter(file), ';');
			String[] nextLine = new String[10];

			// Header
			int k = 0;
			nextLine[k++] = "Server Name";
			nextLine[k++] = "Server IP";
			nextLine[k++] = "Server Port";
			nextLine[k++] = "Server SID";
			nextLine[k++] = "Server Username";
			nextLine[k++] = "Server Password";
			nextLine[k++] = "Server Schema";
			nextLine[k++] = "Server Table";
			nextLine[k++] = "Server DatabaseType";
			nextLine[k++] = "mssqlSchema";
			writer.writeNext(nextLine);
			System.out.println("TABLEMAP: " + ServerList.getTableMap());
			for (int i = 0; i < exportConfigs.size(); i++) {
				System.out.println(exportConfigs.get(i).getServer().getDatabaseType());
				k = 0;
				nextLine[k++] = exportConfigs.get(i).getServer().getName();
				nextLine[k++] = exportConfigs.get(i).getServer().getIp();
				nextLine[k++] = exportConfigs.get(i).getServer().getPort();
				nextLine[k++] = exportConfigs.get(i).getServer().getSID();
				nextLine[k++] = exportConfigs.get(i).getServer().getUser();
				nextLine[k++] = exportConfigs.get(i).getServer().getPassword();
				nextLine[k++] = exportConfigs.get(i).getSchema();
				nextLine[k++] = exportConfigs.get(i).getTable();
				nextLine[k++] = exportConfigs.get(i).getServer().getDatabaseType();
				if(exportConfigs.get(i).getServer().getDatabaseType().equalsIgnoreCase("mssql"))
					nextLine[k++] = ServerList.getTableMap().get(exportConfigs.get(i).getTable()).getDatabaseSchema();
				else
					nextLine[k++]="";
				writer.writeNext(nextLine);
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispose() {
		System.out.println("disposing");
		super.dispose();
	}

	@Override
	public boolean performFinish() {
		try {

			final boolean cleanUp = DBWizardPageThree.getCleanUp();
			final boolean terms = DBWizardPageThree.getTerms();
			// FOLDERMAIN = DBWizardPageThree.getFolderMainText();
			FOLDERCSV = DBWizardPageThree.getCSVPath();
			System.out.println("finish?");
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/Default.properties"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
			// delete old files
			File folder = new File(FOLDERCSV);
			File[] files = folder.listFiles();
			for (File file : files) {
				file.delete();
			}

			Path inputPath = new Path("/misc/input/"); //$NON-NLS-1$
			URL inputURL = FileLocator.find(bundle, inputPath,
					Collections.EMPTY_MAP);
			URL inputURL2 = FileLocator.toFileURL(inputURL);
			File inputFolder = new File(inputURL2.getPath());
			File[] listOfInputFiles = inputFolder.listFiles();

			for (File listOfInputFile : listOfInputFiles) {
				if (listOfInputFile.getName().endsWith(".csv")) {
					System.out.println("deleting input file: "
							+ listOfInputFile.getName());
					listOfInputFile.delete();
				}
			}

			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			HashMap<Server, HashMap<String, List<String>>> checkedTables = DBWizardPageTwo
					.getCheckedTables();
			System.out.println(checkedTables);
			Iterator<Server> tableServerIterator = checkedTables.keySet()
					.iterator();
			List<ExportConfig> exportConfigs = new LinkedList<ExportConfig>();
			/**
			 * Import all checked tables read from disc
			 */
			while (tableServerIterator.hasNext()) {
				Server currentServer = tableServerIterator.next();
				String currentServerUniqueID = currentServer.getUniqueID()
						.replaceAll(" ", "_");
				System.out.println("currentserver: " + currentServerUniqueID);
				HashMap<String, java.util.List<String>> schemaHashMap = checkedTables
						.get(currentServer);
				Iterator<String> schemaHashMapIterator = schemaHashMap.keySet()
						.iterator();
				while (schemaHashMapIterator.hasNext()) {
					String currentSchema = schemaHashMapIterator.next();
					System.out.println("currentSchema: " + currentSchema);
					java.util.List<String> currentTables = schemaHashMap
							.get(currentSchema);
					Iterator<String> tablesTableIterator = currentTables
							.iterator();
					while (tablesTableIterator.hasNext()) {
						String table = tablesTableIterator.next();
						System.out.println("currenttable: " + table);
						currentServer.setTable(table);
						currentServer.setSchema(currentSchema);
						// output = new
						// File(FOLDERCSV+currentServerUniqueID+"_"+currentSchema+"_"+table+".csv");
						System.out.println("after output");
						// EXPORT DB --> TOS
						// ExportDB.exportDB(currentServer, output);
						System.out.println("table " + table + " created.");

						bundle = Activator.getDefault().getBundle();
						Path imgImportPath = new Path("/cfg/tables"); //$NON-NLS-1$
						url = FileLocator.find(bundle, imgImportPath,
								Collections.EMPTY_MAP);
						fileUrl = FileLocator.toFileURL(url);
						File serverFile = new File(fileUrl.getPath());
						ObjectInputStream is = new ObjectInputStream(
								new FileInputStream(serverFile));
						@SuppressWarnings("unchecked")
						HashMap<String, HashMap<String, List<List<String>>>> tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
						.readObject();
						is.close();
						List<List<String>> itemList = tableMap.get(
								currentServer.getUniqueID()).get(table);

						File tmpConfigFile = new File(FOLDERCSV
								+ currentServerUniqueID + "_" + currentSchema
								+ "_" + table + ".cfg.csv");
						writeTableConfigFile(itemList, tmpConfigFile);
						ExportConfig currentExportConfig = new ExportConfig(
								currentServer, currentSchema, table);
						exportConfigs.add(currentExportConfig);

					}
				}
			}
			contextMap = new HashMap<String, String>();

			contextMap.put("DBHost", WizardPageOne.getIpText());
//			defaultProps.setProperty("DBHost", WizardPageOne.getIpText());
			contextMap.put("DBPassword", WizardPageOne.getDBUserPasswordText());
			contextMap.put("DBUsername", WizardPageOne.getDBUserText());
			contextMap.put("DBInstance", WizardPageOne.getDBSIDText());
			contextMap.put("DBPort", WizardPageOne.getPortText());
			contextMap.put("DBSchema", WizardPageOne.getDBSchemaText());


			/**
			 * page 2
			 */
			//
			if (DBWizardPageThree.getTerms()) {

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
			} else {
			}

			if (cleanUp) {
				defaultProps.setProperty("cleanUp", "true");
				contextMap.put("cleanUp", "true");
			} else {
				defaultProps.setProperty("cleanUp", "false");
				contextMap.put("cleanUp", "false");
			}

			if (DBWizardPageThree.getSaveContext()) {
				System.out.println("saving context");
				defaultProps.store(new FileWriter(properties), "");
			}

			Path tmpPath = new Path("/misc/tmp/"); //$NON-NLS-1$
			URL tmpURL = FileLocator.find(bundle, tmpPath,
					Collections.EMPTY_MAP);
			URL tmpURL2 = FileLocator.toFileURL(tmpURL);

			// String fileName ="C:/Desktop/export.csv";
			String fileName = tmpURL2.getPath() + "exportConfig.csv";
			System.out.println("ExportConfig FileName: " + fileName);
			createDBExportConfigCSV(exportConfigs, fileName);
//			defaultProps.setProperty("exportDBConfig", fileName);
						contextMap.put("exportDBConfig", fileName);

//			defaultProps.setProperty("folderCSV", FOLDERCSV);
						contextMap.put("folderCSV", FOLDERCSV);

			Path miscPath = new Path("/misc/"); //$NON-NLS-1$
			URL miscUrl = FileLocator.find(bundle, miscPath,
					Collections.EMPTY_MAP);
			URL miscFileUrl = FileLocator.toFileURL(miscUrl);

			File misc = new File(miscFileUrl.getPath());
			String miscPathReplaced = misc.getAbsolutePath().replaceAll("\\\\",
					"/")
					+ "/";
			contextMap.put("folderMain", miscPathReplaced);
			//			defaultProps.setProperty("folderMainCSV", miscPathReplaced);

			if (DBWizardPageThree.getTruncate()) {
				System.out.println("truncating");
				contextMap.put("truncateProject", "true");
				//				defaultProps.setProperty("truncateProject", "true");
			} else {
				contextMap.put("truncateProject", "false");
				//				defaultProps.setProperty("truncateProject", "false");
			}

			contextMap.put("datePattern", "yyyy-MM-dd");
			//			defaultProps.setProperty("datePattern", "yyyy-MM-dd");

			contextMap.put("pidgen", "false");
			//			defaultProps.setProperty("pidgen", "false");

			done = false;
			System.out.println("creating b");
			b = new Thread(new Runnable() {

				@Override
				public void run() {
					started = true;
					final long start = System.currentTimeMillis();
					IDRTImport.setCompleteContext(contextMap);
					// int exitCode = IDRTImport.runCSVImport();
					int exitCode = IDRTImport.runDBImport(terms);
					done = true;
					StatusListener.setStatus(0, "", "");  
					StatusListener.setSubStatus(0.0f, "");
					StatusListener.notifyListener();
					started = false;

					if (exitCode == 0) {
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								//								closeBar("DB Import Finished!", 0);
								Log.addLog(0, "DB Import Finished!");
								long end = System.currentTimeMillis();
								long time = end - start;
								Log.addLog(0, "Duration: " + (time / 1000)
										+ " s");
								MessageDialog.openInformation(Display
										.getDefault().getActiveShell(),
										"Import Finished", "Import finished.");
							}
						});
					}
				}
			});

			if (!started) {
				// progressBar = new AddProgressBar();
				// progressBar.init();
				b.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Writes the config file for the DBImport
	 * 
	 * @param itemList
	 *            Lists of Items
	 * @param tmpTableFile
	 *            Output Temp File
	 */
	private void writeTableConfigFile(List<List<String>> itemList,
			File tmpTableFile) {
		try {
			CSVWriter rotatedOutput = new CSVWriter(
					new FileWriter(tmpTableFile), '\t');
			String[] nextLine = new String[itemList.size() + 1];
			nextLine[0] = "configTmp";
			if (itemList != null) {
				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(0);
				}
				rotatedOutput.writeNext(nextLine);

				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(2);
				}
				rotatedOutput.writeNext(nextLine);
				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(1);
				}
				rotatedOutput.writeNext(nextLine);
				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(3);
				}
				rotatedOutput.writeNext(nextLine);
			}
			rotatedOutput.close();
			System.out.println("writing cfg done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}