package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import swing2swt.layout.BorderLayout;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.commands.CSVImportCommand;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.importtool.misc.FileHandler;

import org.eclipse.wb.swt.ResourceManager;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class CSVWizardPage3 extends WizardPage {

	
	private static int HEADERCOLUMN = 0;
	private static int NAMECOLUMN = 1;
	private static int TOOLTIPCOLUMN = 2;
	private static int DATATYPECOLUMN = 3;
	private static int METADATACOLUMN = 4;
	private static int PIDGENCOLUMN = 5;
	char[] DELIMITERS = {';','\t',','};

	public Table fillTable(Table table, String[] nextLine, String file) {
		try {



			File schemaFile = FileHandler.getBundleFile("/cfg/schema.csv");

			char inputDelim = getDelimiter(schemaFile.getAbsoluteFile().getAbsolutePath());

			HashMap<Integer,String> patientIDMap = new HashMap<Integer, String>();
			HashMap<Integer,String> objectIDMap = new HashMap<Integer, String>();
			HashMap<Integer,String> encounterIDMap = new HashMap<Integer, String>();
			HashMap<Integer,String> downloadDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> importDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> startDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> updateDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> endDateMap = new HashMap<Integer, String>();


				

			CSVReader reader = new CSVReader(new FileReader(
						schemaFile), inputDelim, QUOTECHAR);

				String [] schemaContent = reader.readNext();



				int counter = 0;
				while ((schemaContent = reader.readNext()) != null) {
					if (schemaContent[0] != null && schemaContent[0].length()>0)
						patientIDMap.put(counter, schemaContent[0]);
					if (schemaContent[1] != null && schemaContent[1].length()>0)
						objectIDMap.put(counter, schemaContent[1]);
					if (schemaContent[2] != null && schemaContent[2].length()>0)
						encounterIDMap.put(counter, schemaContent[2]);
					if (schemaContent[3] != null && schemaContent[3].length()>0)
						downloadDateMap.put(counter, schemaContent[3]);
					if (schemaContent[4] != null && schemaContent[4].length()>0)
						importDateMap.put(counter, schemaContent[4]);
					if (schemaContent[5] != null && schemaContent[5].length()>0)
						startDateMap.put(counter, schemaContent[5]);
					if (schemaContent[6] != null && schemaContent[6].length()>0)
						updateDateMap.put(counter, schemaContent[6]);
					if (schemaContent[7] != null && schemaContent[7].length()>0)
						endDateMap.put(counter, schemaContent[7]);
					counter++;
				}
				reader.close();
				//				System.out.println(objectIDMap);
				//				System.out.println(encounterIDMap);
				//				System.out.println(downloadDateMap);
				//				System.out.println(importDateMap);
				//				System.out.println(startDateMap);
				//				System.out.println(updateDateMap);
			int pidLocation = -1;
			int pidWeight = 99999;
			int objLocation = -1;
			int objWeight = 99999;
			int encLocation = -1;
			int encWeight = 99999;
			int dwnLocation = -1;
			int dwnWeight = 99999;
			int impLocation = -1;
			int impWeight = 99999;
			int srtLocation = -1;
			int strWeight = 99999;
			int updLocation = -1;
			int updWeight = 99999;
			int endLocation = -1;
			int endWeight = 99999;

			inputDelim = getDelimiter(file);
			CSVReader fileReader = new CSVReader(
					new FileReader(file),
					inputDelim,QUOTECHAR);
			for (int i = 0; i < nextLine.length; i++) {


				int pidmapSize = patientIDMap.size()-1;
				int objmapSize = objectIDMap.size()-1;
				int encmapSize = encounterIDMap.size()-1;
				int dwnmapSize = downloadDateMap.size()-1;
				int impmapSize = importDateMap.size()-1;
				int srtmapSize = startDateMap.size()-1;
				int updmapSize = updateDateMap.size()-1;
				int endmapSize = endDateMap.size()-1;

				for (int i2 = pidmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(patientIDMap.get(i2).toLowerCase())) {
						if (pidLocation>=0) {
							if (i2 < pidWeight) {
								table.getItems()[pidLocation].setText(METADATACOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_PatientID); 
								pidWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_PatientID); 
							pidLocation = i;
							pidWeight = i2;
						}
					}
				}
				for (int i2 = objmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(objectIDMap.get(i2).toLowerCase())) {
						if (objLocation>=0) {
							if (i2 < objWeight) {
								table.getItems()[objLocation].setText(METADATACOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_ObjectID); 
								objWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_ObjectID); 
							objLocation = i;
							objWeight = i2;
						}
					}
				}

				for (int i2 = encmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(encounterIDMap.get(i2).toLowerCase())) {
						if (encLocation>=0) {
							if (i2 < encWeight) {
								table.getItems()[encLocation].setText(METADATACOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_EncounterID); 
								encWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_EncounterID); 
							encLocation = i;
							encWeight = i2;
						}
					}
				}

				for (int i2 = dwnmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(downloadDateMap.get(i2).toLowerCase())) {
						if (dwnLocation>=0) {
							if (i2 < dwnWeight) {
								table.getItems()[dwnLocation].setText(METADATACOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_DownloadDate); 
								dwnWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_DownloadDate); 
							dwnLocation = i;
							dwnWeight = i2;
						}
					}
				}

				for (int i2 = impmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(importDateMap.get(i2).toLowerCase())) {
						if (impLocation>=0) {
							if (i2 < impWeight) {
								table.getItems()[impLocation].setText(DATATYPECOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_ImportDate); 
								impWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_ImportDate); 
							impLocation = i;
							impWeight = i2;
						}
					}
				}

				for (int i2 = srtmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(startDateMap.get(i2).toLowerCase())) {
						if (srtLocation>=0) {
							if (i2 < strWeight) {
								table.getItems()[srtLocation].setText(METADATACOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_StartDate); 
								strWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_StartDate); 
							srtLocation = i;
							strWeight = i2;
						}
					}
				}

				for (int i2 = updmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(updateDateMap.get(i2).toLowerCase())) {
						if (updLocation>=0) {
							if (i2 < updWeight) {
								table.getItems()[updLocation].setText(METADATACOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_UpdateDate); 
								updWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_UpdateDate); 
							updLocation = i;
							updWeight = i2;
						}
					}
				}

				for (int i2 = endmapSize; i2 >=0; i2--) {
					if (nextLine[i].toLowerCase().contains(endDateMap.get(i2).toLowerCase())) {
						if (endLocation>=0) {
							if (i2 < endWeight) {
								table.getItems()[endLocation].setText(METADATACOLUMN, "");
								table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_EndDate); 
								endWeight = i2;
							}
						}
						else {
							table.getItems()[i].setText(METADATACOLUMN, Messages.ConfigMetaData_EndDate); 
							endLocation = i;
							endWeight = i2;
						}
					}
				}

			}
			String[] nextFileLine = fileReader.readNext();
			HashSet<String> pidSet = new HashSet<String>();

			while ((nextFileLine = fileReader.readNext()) != null) {
				System.out.println(nextFileLine[0].toString());
				if (pidLocation>=0 && !pidSet.add(nextFileLine[pidLocation])){
					System.out.println("PID DOUBLE:");
					System.out.println(nextFileLine[pidLocation]);

					boolean cont = MessageDialog.openQuestion(Application.getShell(), "Duplicate Found", "A duplicate of the PatientID: " 
							+ nextFileLine[pidLocation] + " has been found. Treat " + file +" as Modifier?");
					if (cont)
						table.getItems()[pidLocation].setText(METADATACOLUMN, Messages.ConfigMetaData_ObjectID); 
					break;
				}
			}
			fileReader.close();
			return table;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}
	private Composite container;
	private static String csvFolder;

	//	private static String mainPath = "";
	private static Vector<String> list;
	/**
	 * Table for the configuration of each column of the import-data.
	 */
	private static Table table;
	private TableColumn tblclmnItem;
	private TableColumn tblclmnDatatype;
	private TableColumn tblclmnMetadata;
	private TableColumn tblclmnName;
	private TableColumn tblclmnToolTip;
	private TableColumn tblclmnPIDGen;
	private Composite compositeTables;

	private boolean allConfigs;

	private static String oldHeadlineNumber;

	private static List<String> configList;
	/**
	 * Default Properties for the TOS Jobs
	 */
	private static Properties defaultProps;
	private static char DEFAULTDELIM = ';';

	private static char QUOTECHAR='\"';


	private Composite buttonComposite;

	/**
	 * Last active Table
	 */
	public static String lastTable;

	/**
	 * The TableViewer for all Servers
	 */
	private static TableViewer serverListViewer;


	/**
	 * Mapping between Config Files and corresponding import file.
	 */
	private static HashMap<String, String> fileConfigMap;

	/**
	 * @return List of all .cfg.csv Files
	 */
	public static List<String> getConfigList() {
		return configList;
	}


	public static char getDEFAULTDELIM() {
		return DEFAULTDELIM;
	}

	/**
	 * @return The HashMap with the mapping of the config files and data files.
	 */
	public static HashMap<String, String> getFileConfigMap() {
		return fileConfigMap;
	}

	/**
	 * @return The TableViewer for the ServerList
	 */
	public static TableViewer getListViewer() {
		return serverListViewer;
	}


	/**
	 * Button for "guess schema"
	 */
	private Button btnGuessSchema;
	/**
	 * Button for "clear Table"
	 */
	private Button clearTables;
	private Button button;
	private Label startLineLabel;
	private static Text headLineText;

	/**
	 * Saves the Table to disc.
	 */
	public static void saveTable() {
		if ((fileConfigMap.get(lastTable) != null) && !table.isDisposed()) {

			File tmpTableFile = new File(csvFolder
					+ fileConfigMap.get(lastTable));
			try {
				CSVWriter rotatedOutput = new CSVWriter(new FileWriter(
						tmpTableFile), DEFAULTDELIM);
				TableItem[] tableItems = table.getItems();
				String[] nextLine = new String[tableItems.length + 1];

				nextLine[0] = "Spaltenname (Pflicht)"; 
				if (tableItems != null) {
					nextLine[0] = "Spaltenname (Pflicht)"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(HEADERCOLUMN);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "Datentyp (Pflicht)"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(DATATYPECOLUMN);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "Name (kann leer sein)"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(NAMECOLUMN);
					}
					rotatedOutput.writeNext(nextLine);

					//TODO Tooltip
					nextLine[0] = "Tooltip (kann leer sein)"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(TOOLTIPCOLUMN);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "Metainformationen"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(METADATACOLUMN);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "PID-Generator"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(PIDGENCOLUMN);
					}
					rotatedOutput.writeNext(nextLine);
					nextLine[0] = "Headline"; 
					nextLine[1] = headLineText.getText();
					rotatedOutput.writeNext(nextLine);
				}
				rotatedOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	private Button refreshBtn;

	private HashSet<String> incompleteConfigs;

	/**
	 * Default Constructor
	 */
	public CSVWizardPage3() {
		super(Messages.CSVWizardPageThree_CSVImportSettings);
		setTitle(Messages.CSVWizardPageThree_CSVImportSettings); 
		setWizard(CSVImportCommand.getWizard());
		setDescription(Messages.CSVWizardPageThree_CSVImportSettings); 
	}


	private char getDelimiter(String string) {

		CSVReader reader;
		char dEFAULTDELIM = DELIMITERS[0];
		try {

			for (int i = 0; i < DELIMITERS.length; i++){
				dEFAULTDELIM = DELIMITERS[i];
				reader = new CSVReader(
						new FileReader(string),	dEFAULTDELIM, QUOTECHAR, getHeadLine());

				String[] testLine = reader.readNext();
				reader.close();
				if (testLine.length > 1) {
					break;
				}
			}

			System.out.println("DELIMITER IS " +dEFAULTDELIM);
			return dEFAULTDELIM;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private static int getHeadLine() {
		try {
			return Integer.parseInt(headLineText.getText());
		}catch (Exception e) {
			System.err.println("Error @ Config:Headline");
			return 0;
		}
	}


	public boolean checkTables() {
		boolean filesOK = true;
		//		boolean anythingwrong = false;
		try {

			for(TableItem item : serverListViewer
					.getTable().getItems()) {
				String table = item.getText();
				if (fileConfigMap.get(table) != null) {
					File configFile = new File(csvFolder
							+ fileConfigMap
							.get(table));
					char inputDelim = DEFAULTDELIM;
					CSVReader reader = new CSVReader(
							new FileReader(configFile),
							inputDelim);
					String[] testLine = reader.readNext();
					reader.close();
					if (testLine != null) {

						if (testLine.length == 1) {
							System.err
							.println("Wrong delimiter?"); 
							System.err.println(inputDelim + "==" + DEFAULTDELIM);
							if (inputDelim == DEFAULTDELIM) {
								inputDelim = '\t';
								System.err
								.println("Delimiter set to: \\t"); 
							} else {
								inputDelim = DEFAULTDELIM;
								System.err
								.println("DefDelimiter set to: " 
										+ DEFAULTDELIM);
							}
						}
						//										DEFAULTDELIM = inputDelim;

						reader = new CSVReader(new FileReader(
								configFile), inputDelim, QUOTECHAR);
						String[] line1 = reader.readNext();
						reader.readNext();
						reader.readNext();
						reader.readNext();
						String[] line4 = reader.readNext();
						reader.close();
						boolean fine = false;
						for (int i = 1; i < line1.length; i++) {
							//							System.out.println(line4[i]);
							if (line4[i].equalsIgnoreCase("patientid") ||line4[i].equalsIgnoreCase("encounterid") || line4[i].equalsIgnoreCase("objectid") )
							{
								fine=true;
								break;
							}
						}
						if (fine) {
							incompleteConfigs.remove(table);
						}
						else {
							filesOK=false;
							setPageComplete(false);
							setErrorMessage("No PatientID or ObjectID found in " + table + "!");
							incompleteConfigs.add(table);
						}
					}
				}
				else {
					incompleteConfigs.add(table);
					setErrorMessage("No PatientID or EncounterID found in " + table + "!");
					setPageComplete(false);
				}
				if (filesOK) {
					setPageComplete(true);
					setErrorMessage(null);
				}
			}
			serverListViewer.refresh(true);
			return filesOK;
		}catch (Exception e) {
			e.printStackTrace();
			serverListViewer.refresh(true);
			return filesOK;
		}
	}
	@Override
	public void createControl(final Composite parent) {
		try {
			lastTable = null;
			incompleteConfigs = new HashSet<String>();
			fileConfigMap = new HashMap<String, String>();

			/*
			 * Filling the Mapping between the config and data files 
			 */
			File imgConfigFile = FileHandler.getBundleFile("/images/itemstatus-checkmark16.png");
			File imgNoConfigFile = FileHandler.getBundleFile("/images/remove-grouping.png");
			final Image imgHasConfig = new Image(parent.getDisplay(),
					imgConfigFile.getAbsolutePath());
			final Image imgNoConfig = new Image(parent.getDisplay(),
					imgNoConfigFile.getAbsolutePath());
			csvFolder = CSVWizardPage2.getFolderCSVText();
			File folder = new File(csvFolder);
			if (folder!=null) {
				File[] listOfFiles = folder.listFiles();
				configList = new LinkedList<String>();
				list = new Vector<String>();
				if (listOfFiles!=null) {
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].getName().toLowerCase().endsWith(".cfg.csv")) { 
							configList.add(listOfFiles[i].getName());
						}
						if (!listOfFiles[i].getName().toLowerCase().endsWith(".cfg.csv") 
								&& listOfFiles[i].isFile()) {
							list.add(listOfFiles[i].getName());
						}
					}

					for (int i = 0; i < list.size(); i++) {
						String tmpElement = list.get(i);
						if (tmpElement.contains(".")) { 
							String filename = tmpElement.substring(0,
									tmpElement.lastIndexOf(".")); 
							String extension = tmpElement.substring(tmpElement
									.lastIndexOf(".")); 
							if (configList.contains(filename + ".cfg" + extension)) { 
								fileConfigMap.put(list.get(i), filename + ".cfg" 
										+ extension);
							}
						}
					}
				}
			}
			File properties = FileHandler.getBundleFile("/cfg/Default.properties");
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));

			container = new Composite(parent, SWT.NULL);

			container.setLayout(new BorderLayout(0, 0));

			SashForm sashForm = new SashForm(container, SWT.NONE);
			sashForm.setLayoutData(BorderLayout.CENTER);

			Composite compositeList = new Composite(sashForm, SWT.NONE);
			compositeTables = new Composite(sashForm, SWT.NONE);
			compositeTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, true, 1, 1));
			compositeTables.setLayout(new BorderLayout(0, 0));

			/*
			 * Filling the table
			 */
			table = new Table(compositeTables, SWT.BORDER | SWT.FULL_SELECTION
					| SWT.VIRTUAL);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			final TableEditor editor = new TableEditor(table);
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;

			table.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent event) {
					Control old = editor.getEditor();
					if (old != null) {
						old.dispose();
					}
					Point pt = new Point(event.x, event.y);
					final TableItem item = table.getItem(pt);
					if (item != null) {
						int column = -1;
						for (int i = 0, n = table.getColumnCount(); i < n; i++) {
							Rectangle rect = item.getBounds(i);
							if (rect.contains(pt)) {
								column = i;
								break;
							}
						}
						// The "nice name" of the column.
						if (column == NAMECOLUMN) {
							final Text text = new Text(table, SWT.NONE);
							text.setForeground(item.getForeground());
							text.setText(item.getText(column));
							text.setForeground(item.getForeground());
							text.selectAll();
							text.setFocus();
							editor.minimumWidth = text.getBounds().width;
							editor.setEditor(text, item, column);
							final int col = column;
							text.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(ModifyEvent event) {
									item.setText(col, text.getText());
								}
							});
							text.addFocusListener(new FocusListener() {
								@Override
								public void focusGained(FocusEvent e) {
								}

								@Override
								public void focusLost(FocusEvent e) {
									item.setText(col, text.getText());
									text.dispose();
								}
							});
							//TODO
							// Tooltip
						}else if (column == TOOLTIPCOLUMN) {
							final Text text = new Text(table, SWT.NONE);
							text.setForeground(item.getForeground());
							text.setText(item.getText(column));
							text.setForeground(item.getForeground());
							text.selectAll();
							text.setFocus();
							editor.minimumWidth = text.getBounds().width;
							editor.setEditor(text, item, column);
							final int col = column;
							text.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(ModifyEvent event) {
									item.setText(col, text.getText());
								}
							});
							text.addFocusListener(new FocusListener() {
								@Override
								public void focusGained(FocusEvent e) {
								}

								@Override
								public void focusLost(FocusEvent e) {
									item.setText(col, text.getText());
									text.dispose();
								}
							});
							// Datatype of the column
						} else if (column == DATATYPECOLUMN) {
							final CCombo combo = new CCombo(table,
									SWT.READ_ONLY);
							for (String element : ConfigMetaData.optionsData) {
								combo.add(element);
							}
							combo.setText(item.getText(column));
							combo.select(combo.indexOf(item.getText(column)));
							editor.minimumWidth = combo.computeSize(
									SWT.DEFAULT, SWT.DEFAULT).x;
							combo.setFocus();
							editor.setEditor(combo, item, column);

							final int col = column;
							combo.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent event) {
									item.setText(col, combo.getText());
									combo.dispose();
								}
							});
							// 	i2b2 Metadata of the column (e.g. patient id)
						} else if (column == METADATACOLUMN) {
							String[] optionMetas = ConfigMetaData.getMetaCombo(
									table.getItems(),
									item.getText(column));
							final CCombo combo = new CCombo(table,
									SWT.READ_ONLY);
							for (String optionMeta : optionMetas) {
								combo.add(optionMeta);
							}
							combo.select(combo.indexOf(item.getText(column)));
							editor.minimumWidth = combo.computeSize(
									SWT.DEFAULT, SWT.DEFAULT).x;
							combo.setFocus();
							editor.setEditor(combo, item, column);
							final int col = column;
							combo.addSelectionListener(new SelectionAdapter() {

								@Override
								public void widgetSelected(SelectionEvent event) {
									item.setText(col, combo.getText());
									combo.dispose();
									saveTable();
									checkTables();
								}
							});
							// PID-Generator specific Metadata (e.g. bday, name, lastname...)
						} else if ((column == 5)
								&& CSVWizardPage2.getBtnRADIOCsvfile()
								&& CSVWizardPage2.getUsePid()) {
							final CCombo combo = new CCombo(table,
									SWT.READ_ONLY);
							String[] optionMetas = ConfigMetaData
									.getMetaComboPIDGen(
											table.getItems(),
											item.getText(column));
							for (String optionMeta : optionMetas) {
								combo.add(optionMeta);
							}
							combo.select(combo.indexOf(item.getText(column)));
							editor.minimumWidth = combo.computeSize(
									SWT.DEFAULT, SWT.DEFAULT).x;
							combo.setFocus();
							editor.setEditor(combo, item, column);

							final int col = column;
							combo.addSelectionListener(new SelectionAdapter() {

								@Override
								public void widgetSelected(SelectionEvent event) {
									item.setText(col, combo.getText());
									if (!combo.getText().isEmpty()) {
										item.setText(col - 1, "ignore"); 
									} else {
										item.setText(col - 1, ""); 
									}
									combo.dispose();
								}
							});
						}
					}
					checkTables();
				}
			});

			compositeList.setLayout(new GridLayout(2, false));

			serverListViewer = new TableViewer(compositeList);
			serverListViewer.getTable().setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			new Label(compositeList, SWT.NONE);

			button = new Button(compositeList, SWT.NONE);
			button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			button.setText("Guess All Schemata");
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {

					boolean cont = MessageDialog.openConfirm(Application.getShell(),
							"Overwrite all Configs?",
							"Do you really want to overwirte all Configs?");
					if (cont) {
						for (int i = 0; i<serverListViewer.getTable().getItemCount();i++) {
							allConfigs = true;
							serverListViewer.getTable().select(i);
							serverListViewer.getTable().notifyListeners(SWT.Selection, new Event());
							btnGuessSchema.notifyListeners(SWT.Selection, new Event());

							serverListViewer.getTable().deselect(i);
							checkTables();
							allConfigs = false;
						}
					}
				}
			});
			new Label(compositeList, SWT.NONE);

			serverListViewer
			.setContentProvider(new IStructuredContentProvider() {

				@Override
				public void dispose() {
				}

				@Override
				public Object[] getElements(Object inputElement) {
					@SuppressWarnings("rawtypes")
					Vector v = (Vector) inputElement;
					return v.toArray();
				}

				@Override
				public void inputChanged(Viewer viewer,
						Object oldInput, Object newInput) {
				}
			});

			serverListViewer.setLabelProvider(new LabelProvider() {
				@Override
				public Image getImage(Object element) {
					String tmpElement = (String) element;
					if (tmpElement.contains(".")) { 
						if (!incompleteConfigs.contains(tmpElement)) { 
							return imgHasConfig;
						} else {
							return imgNoConfig;
						}
					} else {
						return imgNoConfig;
					}
				}
				@Override
				public String getText(Object element) {
					return element.toString();
				}
			});

			serverListViewer.setInput(list);

			tblclmnItem = new TableColumn(table, SWT.NONE);
			tblclmnItem.setWidth(100);
			tblclmnItem.setText("Item"); 
			tblclmnName = new TableColumn(table, SWT.NONE);
			tblclmnName.setWidth(100);
			tblclmnName.setText("Name"); 

			tblclmnToolTip = new TableColumn(table, SWT.NONE);
			tblclmnToolTip.setWidth(100);
			tblclmnToolTip.setText("Tooltip"); 

			tblclmnDatatype = new TableColumn(table, SWT.NONE);
			tblclmnDatatype.setWidth(100);
			tblclmnDatatype.setText("Datatype"); 

			tblclmnMetadata = new TableColumn(table, SWT.NONE);
			tblclmnMetadata.setWidth(100);
			tblclmnMetadata.setText("Metadata"); 

			// Display only if PID-Generator was selected
			if (CSVWizardPage2.getBtnRADIOCsvfile()
					&& CSVWizardPage2.getUsePid()) {
				tblclmnPIDGen = new TableColumn(table, SWT.NONE);
				tblclmnPIDGen.setWidth(100);
				tblclmnPIDGen.setText("PID-Generator"); 
			}

			buttonComposite = new Composite(compositeTables, SWT.NONE);
			buttonComposite.setLayoutData(BorderLayout.SOUTH);
			buttonComposite.setLayout(new GridLayout(5, false));
			serverListViewer.getTable().addSelectionListener(
					new SelectionListener() {
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
						}

						@Override
						public void widgetSelected(SelectionEvent e) {

							try {
								checkTables();
								/*
								 * Fills the table on click by reading or creating a config file
								 */
								if ((lastTable != null)
										&& (fileConfigMap.get(lastTable) != null)) {
									saveTable();
								}

								if (lastTable == null) {
									lastTable = serverListViewer.getTable()
											.getSelection()[0].getText();
								}
								//Config present
								if (fileConfigMap.get(serverListViewer
										.getTable().getSelection()[0].getText()) != null) {
									File configFile = new File(csvFolder
											+ fileConfigMap
											.get(serverListViewer
													.getTable()
													.getSelection()[0]
															.getText()));
									CSVReader reader;

										char inputDelim = getDelimiter(configFile.getAbsoluteFile().getAbsolutePath());
										//										DEFAULTDELIM = inputDelim;
										reader = new CSVReader(new FileReader(
												configFile), inputDelim, QUOTECHAR);
										String[] headLine1 = reader.readNext();
										String[] datatype2 = reader.readNext();
										String[] nicename3 = reader.readNext();
										String[] tooltip4 = reader.readNext();
										String[] metainformation5 = reader.readNext();
										String[] pidgen6 = reader.readNext();
										String[] headline6 = reader.readNext();
										//TODO read header line from config, pidgen?
										//										System.out.println("READ: " + line6[0]);
										if (headline6!=null) {
											headLineText.setText(headline6[1]);
											oldHeadlineNumber = headline6[1];
										}
										else {
											headLineText.setText("0");
											oldHeadlineNumber = "0";
										}
										headLineText.update();
										headLineText.redraw();
										reader.close();
										lastTable = serverListViewer.getTable()
												.getSelection()[0].getText();
										table.removeAll();
										table.clearAll();
										for (int i = 1; i < headLine1.length; i++) {

											final TableItem item = new TableItem(
													table, SWT.NONE);
											item.setText(HEADERCOLUMN, headLine1[i]);
											item.setText(NAMECOLUMN, nicename3[i]);
											item.setText(TOOLTIPCOLUMN, tooltip4[i]);
											item.setText(DATATYPECOLUMN, datatype2[i]);
											item.setText(METADATACOLUMN, metainformation5[i]);
											if (metainformation5!= null)
												item.setText(METADATACOLUMN, metainformation5[i]);
										}
									//new config
								} 
								else {

									String tmp = serverListViewer.getTable()
											.getSelection()[0].getText();
									tmp = tmp.substring(0, tmp.lastIndexOf("."));
									tmp = tmp + ".cfg.csv";
									String newConfig = tmp;

									//									System.out.println(newConfig);
									configList.add(newConfig);
									//									System.out.println("added " + serverListViewer.getTable()
									//											.getSelection()[0].getText());
									lastTable = serverListViewer.getTable()
											.getSelection()[0].getText();
									table.removeAll();
									table.clearAll();
									char inputDelim = getDelimiter(csvFolder
											+ serverListViewer
											.getTable()
											.getSelection()[0]
													.getText());
									//									DEFAULTDELIM = inputDelim;
									String tmpElement = serverListViewer
											.getTable().getSelection()[0]
													.getText();
									String filename = tmpElement.substring(0,
											tmpElement.lastIndexOf(".")); 
									String extension = tmpElement
											.substring(tmpElement
													.lastIndexOf(".")); 
									fileConfigMap.put(serverListViewer
											.getTable().getSelection()[0]
													.getText(), filename + ".cfg" 
															+ extension);

									headLineText.setText(""+getHeadLine());
									CSVReader reader = new CSVReader(new FileReader(
											csvFolder
											+ serverListViewer
											.getTable()
											.getSelection()[0]
													.getText()),
													inputDelim,'\"',getHeadLine());
									String[] line1 = reader.readNext();
									reader.close();
									for (String element : line1) {
										final TableItem item = new TableItem(
												table, SWT.NONE);
										item.setText(HEADERCOLUMN, element);
										item.setText(NAMECOLUMN, ""); 
										item.setText(TOOLTIPCOLUMN, ""); 
										item.setText(DATATYPECOLUMN, "");
										item.setText(METADATACOLUMN, ""); 
									}

									File tmpTableFile = new File(csvFolder
											+ fileConfigMap
											.get(serverListViewer
													.getTable()
													.getSelection()[0]
															.getText()));
									try {
										CSVWriter rotatedOutput = new CSVWriter(
												new FileWriter(tmpTableFile),
												inputDelim);

										TableItem[] tableItems = table
												.getItems();
										String[] nextLine = new String[tableItems.length + 1];

										nextLine[0] = "Spaltenname (Pflicht)"; 
										if (tableItems != null) {
											nextLine[0] = "Spaltenname (Pflicht)"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(HEADERCOLUMN);
											}
											rotatedOutput.writeNext(nextLine);

											nextLine[0] = "Datentyp (Pflicht)"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(DATATYPECOLUMN);
											}
											rotatedOutput.writeNext(nextLine);

											nextLine[0] = "Name (kann leer sein)"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(NAMECOLUMN);
											}
											rotatedOutput.writeNext(nextLine);

											nextLine[0] = "Tooltip (kann leer sein)"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(TOOLTIPCOLUMN);
											}
											rotatedOutput.writeNext(nextLine);

											nextLine[0] = "Metainformationen"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(METADATACOLUMN);
											}
											rotatedOutput.writeNext(nextLine);
										}
										rotatedOutput.close();
									} catch (IOException e2) {
										e2.printStackTrace();
									}
								}
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							serverListViewer.refresh(true,true);
						}

					});

			startLineLabel = new Label(buttonComposite, SWT.NONE);
			startLineLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			startLineLabel.setText("Headline:");

			headLineText = new Text(buttonComposite, SWT.BORDER);
			GridData gd_startLineText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_startLineText.widthHint = 25;
			headLineText.setLayoutData(gd_startLineText);
			headLineText.setText("");
			headLineText.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					try {
						Integer.parseInt(headLineText.getText());
						oldHeadlineNumber = headLineText.getText();

						//TODO reRead CSV
					}catch (NumberFormatException e2) {
						if (headLineText.getText().isEmpty()) {
							oldHeadlineNumber = headLineText.getText();
						}
						else {
							headLineText.setText(oldHeadlineNumber);
						}
					}
				}});

			refreshBtn = new Button(buttonComposite, SWT.NONE);
			refreshBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					//					serverListViewer.getTable().notifyListeners(SWT.Selection, new Event());
					clearTables.notifyListeners(SWT.Selection, new Event());
				}
			});
			refreshBtn.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.importtool", "images/refresh.gif"));
			clearTables = new Button(buttonComposite, SWT.PUSH);
			clearTables.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			clearTables.setText(Messages.CSVWizardPageThree_ClearTable);
			clearTables.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean cont = MessageDialog.openConfirm(Application.getShell(),
							Messages.CSVWizardPageThree_ClearTableConfirm,
							Messages.CSVWizardPageThree_ClearTableConfirmText);
					if (cont) {
						clearTable();
						checkTables();
					}
				}
			});
			btnGuessSchema = new Button(buttonComposite, SWT.NONE);
			btnGuessSchema.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			btnGuessSchema.setText(Messages.CSVWizardPageThree_GuessSchema);
			sashForm.setWeights(new int[] {195, 476});
			btnGuessSchema.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {

					boolean cont = allConfigs;

					if (!cont) {
						cont = MessageDialog.openConfirm(Application.getShell(),
								Messages.CSVWizardPageThree_GuessSchemaConfirm,
								Messages.CSVWizardPageThree_GuessSchemaConfirmText);
					}
					if (cont) {
						clearMetaDataFromTable();

						try {
							char inputDelim = getDelimiter(csvFolder
									+ serverListViewer.getTable()
									.getSelection()[0]
											.getText());

							CSVReader reader = new CSVReader(new FileReader(csvFolder
									+ serverListViewer.getTable()
									.getSelection()[0].getText()),
									inputDelim,QUOTECHAR,getHeadLine());
							//							DEFAULTDELIM = inputDelim;

							String[] nextLine = reader.readNext();

							int k = 0;
							HashMap<Integer, HashSet<String>> content = new HashMap<Integer, HashSet<String>>();
							int guessRow = -1;
							try {
								guessRow = Integer.parseInt(defaultProps
										.getProperty("guessRows")); 
							} catch (Exception ee) {
								System.err
								.println("Guess Row is not an Integer."); 
								guessRow = -1;
							}
							if (guessRow > 0) {
								while (((nextLine = reader.readNext()) != null)
										&& (k < guessRow)) {
									k++;

									for (int i = 0; i < nextLine.length; i++) {
										if (content.get(i) != null) {
											content.get(i).add(nextLine[i]);
										} else {
											HashSet<String> tmp = new HashSet<String>();
											tmp.add(nextLine[i]);
											content.put(i, tmp);
										}
									}
								}
							} else {
								while ((nextLine = reader.readNext()) != null) {
									k++;

									for (int i = 0; i < nextLine.length; i++) {
										if (content.get(i) != null) {
											content.get(i).add(nextLine[i]);
										} else {
											HashSet<String> tmp = new HashSet<String>();
											tmp.add(nextLine[i]);
											content.put(i, tmp);
										}
									}
								}
							}
							reader.close();
							// teste nach Datentypen
							for (int i = 0; i < content.keySet().size(); i++) {
								HashSet<String> tmp = content.get(i);
								int countInt = 0;
								int countFloat = 0;
								int countDate = 0;
								int countString = 0;
								String regex = "((([0-2]\\d)|(31|30))|(\\d\\d\\d\\d))(-|\\.)([0-1]\\d)(-|\\.)(([1-2](\\d)*)|((([0-2]\\d)|(31|30))))"; // valid Date 
								Iterator<String> contentIt = tmp.iterator();
								while (contentIt.hasNext()) {
									boolean minusString = false;
									String next = contentIt.next();
									if (next.matches(regex)) {
										countDate++;
									} else {
										if (next.length()>0)
											countString++;
									}

									// try integer
									try {
										Integer.parseInt(next);
										countInt++;
										minusString = true;
									} catch (Exception e3) {
									}

									// try float
									try {
										Float.parseFloat(next.replaceAll("," ,"."));
										countFloat++;
										minusString = true;
									} catch (Exception e4) {
									}

									if (minusString) {
										countString--;
									}
								}
								System.out.println(i + " " +table.getItems()[i].getText(0) + ": f" +countFloat + " s" + countString + " i" + countInt);
								if ((countFloat == 0) && (countInt == 0)
										&& (countString > 0)) {
									table.getItems()[i].setText(DATATYPECOLUMN, "String"); 
								} else if ((countFloat > countInt)
										&& (countString <= 0)) {

									table.getItems()[i].setText(DATATYPECOLUMN, "Float"); 
								} else if ((countInt > countFloat)
										&& (countString <= 0) && countString ==0) {
									table.getItems()[i].setText(DATATYPECOLUMN, "Integer"); 
								} else if ((countFloat == countInt)
										&& (countFloat != 0) && countString ==0) {
									table.getItems()[i].setText(DATATYPECOLUMN, "Integer"); 
								} else if ((countDate > 0)
										&& (countString <= 0)) {
									table.getItems()[i].setText(DATATYPECOLUMN, "Date"); 
								} else {
									table.getItems()[i].setText(DATATYPECOLUMN, "String"); 
								}
							}
							reader = new CSVReader(new FileReader(csvFolder
									+ serverListViewer.getTable()
									.getSelection()[0].getText()),
									inputDelim,QUOTECHAR,getHeadLine());
							//							DEFAULTDELIM = inputDelim;

							nextLine = reader.readNext();
							reader.close();

							fillTable(table, nextLine,
									csvFolder
									+ serverListViewer.getTable()
									.getSelection()[0]
											.getText());
							checkTables();
							saveTable();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						checkTables();
					}
				}
			});
			setControl(container);

			setPageComplete(checkTables());

		} catch (FileNotFoundException e1) {			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	@Override
	public IWizardPage getPreviousPage() {
		if (lastTable != null) {
			saveTable();
		}
		return super.getPreviousPage();
	}

	/**
	 * Clear all metadata from the current table.
	 */
	private void clearMetaDataFromTable() {
		try {
			Log.addLog(0, "Deleted " 
					+ serverListViewer.getTable().getSelection()[0].getText());
			fileConfigMap.remove(serverListViewer.getTable().getSelection()[0]
					.getText());

			String tmpElement = serverListViewer.getTable().getSelection()[0]
					.getText();
			String filename = tmpElement.substring(0,
					tmpElement.lastIndexOf(".")); 
			String extension = tmpElement
					.substring(tmpElement.lastIndexOf(".")); 
			fileConfigMap.put(
					serverListViewer.getTable().getSelection()[0].getText(),
					filename + ".cfg" + extension); 

			// Read File to generate new Config
			char inputDelim = getDelimiter(csvFolder
					+ serverListViewer.getTable().getSelection()[0].getText());

			CSVReader reader = new CSVReader(new FileReader(csvFolder
					+ serverListViewer.getTable().getSelection()[0].getText()),
					inputDelim,QUOTECHAR,getHeadLine());

			//			DEFAULTDELIM = inputDelim;
			TableItem[] items = table.getItems();
			List<String> names = new LinkedList<String>();
			for (TableItem item : items) {
				names.add(item.getText(1));
			}
			table.removeAll();
			String[] line1 = reader.readNext();
			for (int i = 0; i < line1.length; i++) {
				final TableItem item = new TableItem(table, SWT.NONE);
				item.setText(HEADERCOLUMN, line1[i]);
				item.setText(NAMECOLUMN, names.get(i));
				item.setText(TOOLTIPCOLUMN, ""); 
				item.setText(DATATYPECOLUMN, ""); 
				item.setText(METADATACOLUMN, ""); 
			}
			reader.close();
			checkTables();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

	}

	/**
	 * Clears the current table.
	 */
	private void clearTable() {
		try {
			table.removeAll();
			Log.addLog(0, "Deleted " 
					+ serverListViewer.getTable().getSelection()[0].getText());

			// Read File to generate new Config
			File newConf = new File(csvFolder
					+ serverListViewer.getTable().getSelection()[0].getText());
			//TODO
			char inputDelim = getDelimiter(newConf.getAbsoluteFile().getAbsolutePath());

			String[] nextLine;
			CSVReader reader = new CSVReader(new FileReader(newConf), inputDelim,QUOTECHAR,getHeadLine());
			//			DEFAULTDELIM = inputDelim;
			String[] line1 = reader.readNext();
			for (String element : line1) {
				final TableItem item = new TableItem(table, SWT.NONE);
				item.setText(HEADERCOLUMN, element);
				item.setText(NAMECOLUMN, ""); 
				item.setText(TOOLTIPCOLUMN, ""); 
				item.setText(DATATYPECOLUMN, "");
				item.setText(METADATACOLUMN, ""); 
			}
			//			headLineText.setText("0");

			checkTables();
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
}
