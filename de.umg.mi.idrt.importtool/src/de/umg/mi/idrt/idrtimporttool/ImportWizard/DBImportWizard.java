package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import au.com.bytecode.opencsv.CSVWriter;
import de.goettingen.i2b2.importtool.idrt.StatusListener.StatusListener;
import de.umg.mi.idrt.idrtimporttool.ExportDB.ExportConfig;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.importidrt.IDRTImport;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.importtool.misc.FileHandler;
import de.umg.mi.idrt.importtool.views.ServerView;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DBImportWizard extends Wizard {
	private static int HEADERCOLUMN = 0;
	private static int NAMECOLUMN = 1;
	private static int TOOLTIPCOLUMN = 2;
	private static int DATATYPECOLUMN = 3;
	private static int METADATACOLUMN = 4;
	private static int PIDGENCOLUMN = 5;
	private Properties defaultProps;
	private static Thread b;
	public static DBWizardPage3 getThree() {
		return three;
	}

	@SuppressWarnings("deprecation")
	public static void killThreadRemote(final String error,
			final String fileName) {

		if (started) {
			b.stop();

			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					Log.addLog(1, "DB Import Failed: " + error);
					MessageDialog.openError(Application.getShell(), "Import Failed!",
							"Import failed. Please see the log.");
				}
			});
		}
		StatusListener.notifyListener();
		started = false;
	}
	private HashMap<String, String> contextMap;
//	protected WizardPage1 one;
	public static DBWizardPage2 two;
	public static DBWizardPage3 three;

	public boolean done = false;

	public static boolean started = false;

	private static String FOLDERCSV;
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
					Log.addLog(1, "DB Import Failed!");
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

	public static void setThree(DBWizardPage3 three) {
		DBImportWizard.three = three;
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

	public DBImportWizard() {
		super();
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
//		one = new WizardPage1();
		two = new DBWizardPage2();
		three = new DBWizardPage3();
//		addPage(one);
		addPage(two);
		addPage(three);
	}

	@Override
	public boolean canFinish() {
		return (three != null);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean performFinish() {
		try {

			final boolean cleanUp = DBWizardPage3.getCleanUp();
			final boolean truncateQueries = DBWizardPage3.getTruncateQueries();
			
			final boolean stopIndex = DBWizardPage3.getStopIndex();
			final boolean dropIndex = DBWizardPage3.getDropIndex();
			// FOLDERMAIN = DBWizardPageThree.getFolderMainText();
			FOLDERCSV = DBWizardPage3.getCSVPath();
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			// delete old files
			File folder = new File(FOLDERCSV);
			File[] files = folder.listFiles();
			for (File file : files) {
				file.delete();
			}

			File inputFolder = FileHandler.getBundleFile("/misc/input/");
			File[] listOfInputFiles = inputFolder.listFiles();

			for (File listOfInputFile : listOfInputFiles) {
				if (listOfInputFile.getName().endsWith(".csv")) {
					listOfInputFile.delete();
				}
			}

			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			HashMap<Server, HashMap<String, List<String>>> checkedTables = DBWizardPage2
					.getCheckedTables();
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
				HashMap<String, java.util.List<String>> schemaHashMap = checkedTables
						.get(currentServer);
				Iterator<String> schemaHashMapIterator = schemaHashMap.keySet()
						.iterator();
				while (schemaHashMapIterator.hasNext()) {
					String currentSchema = schemaHashMapIterator.next();
					java.util.List<String> currentTables = schemaHashMap
							.get(currentSchema);
					Iterator<String> tablesTableIterator = currentTables
							.iterator();
					while (tablesTableIterator.hasNext()) {
						String table = tablesTableIterator.next();
						currentServer.setTable(table);
						currentServer.setSchema(currentSchema);

						File tableFile = FileHandler.getBundleFile("/cfg/tables");
						if (!tableFile.exists()) {
							tableFile = new File(tableFile.getAbsolutePath());
							tableFile.createNewFile();							
						}
						ObjectInputStream is = new ObjectInputStream(
								new FileInputStream(tableFile));
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
			
			Server selectedServer = ServerView.getSelectedServer();
			final String ipText = selectedServer.getIp();
			final String passwordText = selectedServer.getPassword();
			final String dbUserText = selectedServer.getUser();
			final String dbSID = selectedServer.getSID();
			final String dbPort = selectedServer.getPort();
			final String dbSchema = selectedServer.getSchema();
			final String dbType = selectedServer.getDatabaseType();
			final String whType = selectedServer.getWhType().toLowerCase();
			
			contextMap.put("DB_StagingI2B2_Host", ipText);
			contextMap.put("DB_StagingI2B2_Password", passwordText);
			contextMap.put("DB_StagingI2B2_Username", dbUserText);
			contextMap.put("DB_StagingI2B2_Instance", dbSID);
			contextMap.put("DB_StagingI2B2_Port", dbPort);
			contextMap.put("DB_StagingI2B2_Schema", dbSchema);
			contextMap.put("MDPDName", defaultProps.getProperty("MDPDName"));
			contextMap.put("DB_StagingI2B2_DatabaseType", dbType);
			contextMap.put("DB_StagingI2B2_WHType", whType);
//			contextMap.put("DBHost", WizardPage1.getIpText());
//			contextMap.put("DBPassword", WizardPage1.getDBUserPasswordText());
//			contextMap.put("DBUsername", WizardPage1.getDBUserText());
//			contextMap.put("DBInstance", WizardPage1.getDBSIDText());
//			contextMap.put("DBPort", WizardPage1.getPortText());
//			contextMap.put("DBSchema", WizardPage1.getDBSchemaText());


			/**
			 * page 2
			 */
			if (cleanUp) {
				defaultProps.setProperty("cleanUp", "true");
				contextMap.put("cleanUp", "true");
			} else {
				defaultProps.setProperty("cleanUp", "false");
				contextMap.put("cleanUp", "false");
			}
			
			
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
			
			if (DBWizardPage3.getSaveContext()) {
				defaultProps.store(new FileWriter(properties), "");
			}

			File config = FileHandler.getBundleFile("/misc/tmp/exportConfig.csv");
			createDBExportConfigCSV(exportConfigs, config.getAbsolutePath().replaceAll("\\\\",
					"/"));
			contextMap.put("exportDBConfig", config.getAbsolutePath().replaceAll("\\\\",
					"/"));

			contextMap.put("folderCSV", FOLDERCSV);

			File misc = FileHandler.getBundleFile("/misc/");
			String miscPathReplaced = misc.getAbsolutePath().replaceAll("\\\\",
					"/")
					+ "/";
			contextMap.put("folderMain", miscPathReplaced);
			if (DBWizardPage3.getTruncate()) {
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

			contextMap.put("datePattern", "yyyy-MM-dd");
			contextMap.put("pidgen", "false");

			done = false;
			b = new Thread(new Runnable() {

				@Override
				public void run() {
					started = true;
					final long start = System.currentTimeMillis();
					IDRTImport.setCompleteContext(contextMap);
					int exitCode = IDRTImport.runDBImport();
					done = true;
					StatusListener.setStatus(0, "", "");  
					StatusListener.setSubStatus(0.0f, "");
					StatusListener.notifyListener();
					started = false;

					if (exitCode == 0) {
						Display.getDefault().syncExec(new Runnable() {

							@Override
							public void run() {
								Log.addLog(0, "DB Import Finished!");
								long end = System.currentTimeMillis();
								long time = end - start;
								Log.addLog(0, "Duration: " + (time / 1000)
										+ " s");
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void createDBExportConfigCSV(List<ExportConfig> exportConfigs,
			String fileName) {
		try {
			File file = new File(fileName);
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
			for (int i = 0; i < exportConfigs.size(); i++) {
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
					nextLine[i + 1] = itemList.get(i).get(HEADERCOLUMN);
				}
				rotatedOutput.writeNext(nextLine);

				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(DATATYPECOLUMN);
				}
				rotatedOutput.writeNext(nextLine);
				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(NAMECOLUMN);
				}
				rotatedOutput.writeNext(nextLine);
				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(TOOLTIPCOLUMN);
				}
				rotatedOutput.writeNext(nextLine);
				for (int i = 0; i < itemList.size(); i++) {
					nextLine[i + 1] = itemList.get(i).get(METADATACOLUMN);
				}
				rotatedOutput.writeNext(nextLine);
			}
			rotatedOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}