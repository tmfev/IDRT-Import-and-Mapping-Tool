package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import au.com.bytecode.opencsv.CSVReader;
import swing2swt.layout.BorderLayout;
import de.umg.mi.idrt.idrtimporttool.importidrt.Application;
import de.umg.mi.idrt.idrtimporttool.messages.Messages;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerTable;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerImportDBLabelProvider;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerImportDBModel;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerSourceContentProvider;
import de.umg.mi.idrt.idrtimporttool.table.TableRow;
import de.umg.mi.idrt.importtool.misc.FileHandler;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DBWizardPage2 extends WizardPage {

	private static int HEADERCOLUMN = 0;
	private static int NAMECOLUMN = 1;
	private static int TOOLTIPCOLUMN = 2;
	private static int DATATYPECOLUMN = 3;
	private static int METADATACOLUMN = 4;
	private static int PIDGENCOLUMN = 5;
	
	private static List<TableRow> tableRows;// = new LinkedList<TableRow>();
	private static List<TableItem> tableItems;
	private static HashMap<String, HashMap<String, List<List<String>>>> tableMap;
	private static Properties defaultProps;
	private static File properties;
	private static HashMap<Server, HashMap<String, List<String>>> checkedTables;
	private static Server server;
	private static Menu importServerMenu;
	private static String tableName;
	
	private static char DEFAULTDELIM = ';';

	private static char QUOTECHAR='\"';
	/**
	 * 
	 * @return all checked tables
	 */
	public static HashMap<Server, HashMap<String, List<String>>> getCheckedTables() {
		return checkedTables;
	}
	private Composite container;
	private SashForm sashForm;
	private Composite compositeServer;
	private Composite compositeTables;
	private static CheckboxTreeViewer serverViewer;
	private static Table table;
	private TableColumn tblclmnItem;
	private TableColumn tblclmnDatatype;
	private TableColumn tblclmnMetadata;
	private TableColumn tblclmnName;
	private TableColumn tblclmnTooltip;
	private Composite composite;
	private static Control old;
	private static TableItem item;

	private static CCombo combo;

	public static TreeViewer getImportDBViewer() {
		return serverViewer;
	}

	/**
	 * 
	 * @return the current table.
	 */
	public static Table getTable() {
		return table;
	}

	/**
	 * @return the current table name
	 */
	public static String getTableName() {
		return tableName;
	}

	/**
	 * @return the tableRows
	 */
	public static List<TableRow> getTableRows() {
		return tableRows;
	}

	/**
	 * commands for target server
	 */
	public static void refresh() {
		serverViewer.setContentProvider(new ServerSourceContentProvider());
		serverViewer.refresh();
	}

	// private String[] getMetaComboPIDGen(String[] optionsMeta, TableItem[]
	// items, String currentOption) {
	// boolean firstName = false;
	// boolean name = false;
	// boolean reportDate = false;
	// boolean birthday = false;
	// boolean sex = false;
	// for (int i = 0; i<items.length;i++){
	// TableItem item = items[i];
	// if (item.getText(4).equals("FirstName")){
	// firstName = true;
	// }
	// if (item.getText(4).equals("Name")){
	// name=true;
	// }
	// if (item.getText(4).equals("Reporting Date")){
	// reportDate=true;
	// }
	// if (item.getText(4).equals("Birthday")){
	// birthday=true;
	// }
	// if (item.getText(4).equals("Sex")){
	// sex=true;
	// }
	// }
	// List<String> metaOptionList = new LinkedList<String>();
	//
	// if (currentOption.isEmpty()){
	// metaOptionList.add("");
	// }
	// else {
	// metaOptionList.add("");
	// metaOptionList.add(currentOption);
	// }
	// // metaOptionList.add("ignore");
	//
	// if (!firstName)
	// metaOptionList.add("FirstName");
	// if (!name)
	// metaOptionList.add("Name");
	// if (!reportDate)
	// metaOptionList.add("Reporting Date");
	// if (!birthday)
	// metaOptionList.add("Birthday");
	// if (!sex)
	// metaOptionList.add("Sex");
	//
	// String [] metaString = new String[metaOptionList.size()];
	// for (int i = 0;i<metaOptionList.size();i++){
	// metaString[i]=metaOptionList.get(i);
	// }
	//
	// return metaString;
	// }
	public static Table fillTable(Table table) {
		try {

			
			File schemaFile = FileHandler.getBundleFile("/cfg/schema.csv");

			char inputDelim = DEFAULTDELIM;
			CSVReader reader = new CSVReader(
					new FileReader(schemaFile),
					inputDelim);
			String[] testLine = reader.readNext();
			reader.close();

			HashMap<Integer,String> patientIDMap = new HashMap<Integer, String>();
			HashMap<Integer,String> objectIDMap = new HashMap<Integer, String>();
			HashMap<Integer,String> encounterIDMap = new HashMap<Integer, String>();
			HashMap<Integer,String> downloadDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> importDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> startDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> updateDateMap = new HashMap<Integer, String>();
			HashMap<Integer,String> endDateMap = new HashMap<Integer, String>();


			if (testLine != null) {

				if (testLine.length == 1) {
					System.err
					.println("Wrong delimiter?"); 
					if (inputDelim == DEFAULTDELIM) {
						inputDelim = '\t';
						System.err
						.println("Delimiter set to: \\t"); 
					} else {
						inputDelim = DEFAULTDELIM;
						System.err
						.println("Delimiter set to: " 
								+ DEFAULTDELIM);
					}
				}

				reader = new CSVReader(new FileReader(
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
				//				System.out.println(objectIDMap);
				//				System.out.println(encounterIDMap);
				//				System.out.println(downloadDateMap);
				//				System.out.println(importDateMap);
				//				System.out.println(startDateMap);
				//				System.out.println(updateDateMap);
			}
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
			TableItem[] items = table.getItems();
			for (int i = 0; i < items.length; i++) {

				int pidmapSize = patientIDMap.size()-1;
				int objmapSize = objectIDMap.size()-1;
				int encmapSize = encounterIDMap.size()-1;
				int dwnmapSize = downloadDateMap.size()-1;
				int impmapSize = importDateMap.size()-1;
				int srtmapSize = startDateMap.size()-1;
				int updmapSize = updateDateMap.size()-1;
				int endmapSize = endDateMap.size()-1;

				for (int i2 = pidmapSize; i2 >=0; i2--) {
					if (items[i].getText(0).toLowerCase().contains(patientIDMap.get(i2).toLowerCase())) {
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
					if (items[i].getText(0).toLowerCase().contains(objectIDMap.get(i2).toLowerCase())) {
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
					if (items[i].getText(0).toLowerCase().contains(encounterIDMap.get(i2).toLowerCase())) {
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
					if (items[i].getText(0).toLowerCase().contains(downloadDateMap.get(i2).toLowerCase())) {
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
					if (items[i].getText(0).toLowerCase().contains(importDateMap.get(i2).toLowerCase())) {
						if (impLocation>=0) {
							if (i2 < impWeight) {
								table.getItems()[impLocation].setText(METADATACOLUMN, "");
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
					if (items[i].getText(0).toLowerCase().contains(startDateMap.get(i2).toLowerCase())) {
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
					if (items[i].getText(0).toLowerCase().contains(updateDateMap.get(i2).toLowerCase())) {
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
					if (items[i].getText(0).toLowerCase().contains(endDateMap.get(i2).toLowerCase())) {
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

			return table;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}
	@SuppressWarnings("unchecked")
	private static void clearSingleTable() {
		try {
			tableName = serverViewer.getTree().getSelection()[0].getText();
			File tableFile = FileHandler.getBundleFile("/cfg/tables");

			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					tableFile));
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			is.close();

			tableMap.remove(tableName);

			ObjectOutputStream os;
			os = new ObjectOutputStream(new FileOutputStream(tableFile));
			os.writeObject(tableMap);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public DBWizardPage2() {
		super("Source DB Settings");
		setTitle("Source DB Settings");
		// setDescription("Source DB Settings");
	}

	@Override
	public boolean canFlipToNextPage() {
		return tablesComplete() && !checkedTables.isEmpty();
	}

	@Override
	public void createControl(final Composite parent) {
		try {
			tableName = null;
			checkedTables = new HashMap<Server, HashMap<String, List<String>>>();
			tableRows = new LinkedList<TableRow>();
			tableItems = new LinkedList<TableItem>();
			properties =FileHandler.getBundleFile("/cfg/Default.properties");
			defaultProps = new Properties();
			defaultProps.load(new FileReader(properties));
			container = new Composite(parent, SWT.NULL);

			layoutSourceServerContextMenu();
			setControl(container);
			container.setLayout(new FillLayout(SWT.VERTICAL));
			sashForm = new SashForm(container, SWT.SMOOTH);

			compositeServer = new Composite(sashForm, SWT.NONE);
			compositeServer.setLayout(new FillLayout(SWT.HORIZONTAL));

			compositeTables = new Composite(sashForm, SWT.NONE);
			compositeTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
					true, true, 1, 1));
			compositeTables.setLayout(new BorderLayout(0, 0));
			table = new Table(compositeTables, SWT.BORDER | SWT.FULL_SELECTION
					| SWT.VIRTUAL);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			final TableEditor editor = new TableEditor(table);
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			MouseAdapter listener = new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent event) {
					old = editor.getEditor();
					if (old != null) {
						old.dispose();
					}
					Point pt = new Point(event.x, event.y);
					item = table.getItem(pt);
					if (item != null) {
						int column = -1;
						for (int i = 0, n = table.getColumnCount(); i < n; i++) {
							Rectangle rect = item.getBounds(i);
							if (rect.contains(pt)) {
								column = i;
								break;
							}
						}
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
						}
						else if (column == TOOLTIPCOLUMN){
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
						}

						else if (column == DATATYPECOLUMN) {
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
						} else if (column == METADATACOLUMN) {
							String[] optionMetas = ConfigMetaData.getMetaCombo(
									table.getItems(),
									item.getText(column));
							combo = new CCombo(table, SWT.READ_ONLY);
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
									DBWizardPage2.this
									.saveCurrentTableToDisc();
									DBWizardPage2.this.checkTable();

								}
							});
						}
					}
				}

			};

			table.addMouseListener(listener);

			tblclmnItem = new TableColumn(table, SWT.NONE);
			tblclmnItem.setWidth(100);
			tblclmnItem.setText("Item");

			tblclmnName = new TableColumn(table, SWT.NONE);
			tblclmnName.setWidth(100);
			tblclmnName.setText("Name");
			
			tblclmnTooltip = new TableColumn(table, SWT.NONE);
			tblclmnTooltip.setWidth(100);
			tblclmnTooltip.setText("Tooltip");

			tblclmnDatatype = new TableColumn(table, SWT.NONE);
			tblclmnDatatype.setWidth(100);
			tblclmnDatatype.setText("Datatype");

			tblclmnMetadata = new TableColumn(table, SWT.NONE);
			tblclmnMetadata.setWidth(100);
			tblclmnMetadata.setText("Metadata");

			composite = new Composite(compositeTables, SWT.NONE);
			composite.setLayoutData(BorderLayout.SOUTH);
			composite.setLayout(new GridLayout(3, false));

			Button btnGuessSchema = new Button(composite, SWT.PUSH);
			btnGuessSchema.setText("Guess Schema");
			btnGuessSchema.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true,
					true, 1, 1));
			btnGuessSchema.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}

				@Override
				public void widgetSelected(SelectionEvent e) {

					boolean cont = MessageDialog.openConfirm(Application.getShell(),
							"Guess Schema?",
							"This will overwrite your current Config!\nContinue?");

					if (cont) {
						DBWizardPage2.this.clearMetaDataFromTable();

						
						
						fillTable(table);
						
					
					}
				}
			});

			Button clearSingleTable = new Button(composite, SWT.PUSH);
			clearSingleTable.setText("Clear Single Table");
			clearSingleTable.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {

					boolean cont = MessageDialog.openConfirm(Application.getShell(),
							"Clear Table?",
							"This will clear the current Table!\nContinue?");

					if (cont) {
						clearSingleTable();
						DBWizardPage2.this.loadCurrentTableFromDB();
						table.redraw();
						table.update();
					}
				}
			});

			Button clearAllTables = new Button(composite, SWT.PUSH);
			clearAllTables.setText("Clear All Tables");
			clearAllTables.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						boolean result = MessageDialog.openConfirm(
								Application.getShell(), "Clear All Tables?",
								"Do you really want to clear all tables?");

						if (result) {
							File tableFile = FileHandler.getBundleFile("/cfg/tables");
							tableMap = new HashMap<String, HashMap<String, List<List<String>>>>();
							ObjectOutputStream os = new ObjectOutputStream(
									new FileOutputStream(tableFile));
							// os.writeObject(itemList);
							os.writeObject(tableMap);
							os.flush();
							os.close();
							DBWizardPage2.this.loadCurrentTableFromDB();
							table.redraw();
							table.update();
						}

					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			serverViewer = new CheckboxTreeViewer(compositeServer, SWT.CHECK);
			serverViewer
			.setContentProvider(new ServerSourceContentProvider());
			serverViewer.setLabelProvider(new ServerImportDBLabelProvider());
			serverViewer.setInput(new ServerImportDBModel());
			serverViewer.setSorter(new ViewerSorter());
			serverViewer.addTreeListener(new ITreeViewerListener() {

				@Override
				public void treeCollapsed(TreeExpansionEvent event) {
				}

				@Override
				public void treeExpanded(TreeExpansionEvent event) {
				}
			});
			importServerMenu = new Menu(serverViewer.getTree());
			serverViewer.getTree().setMenu(importServerMenu);

			serverViewer.addCheckStateListener(new ICheckStateListener() {

				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {

					if (event.getChecked()) {
						serverViewer.setSubtreeChecked(event.getElement(), true);
					}
					if (!event.getChecked()) {
						serverViewer.setSubtreeChecked(event.getElement(),
								false);
					}
					checkedTables.clear();
					DBWizardPage2.this.getWizard().getContainer()
					.updateButtons();
					Object[] checkedElements = serverViewer
							.getCheckedElements();
					for (Object checkedElement : checkedElements) {
						if (checkedElement.getClass().equals(ServerTable.class)) {
							ServerTable currentTable = (ServerTable) checkedElement;
							server = currentTable.getServer();
							if (checkedTables.get(currentTable.getServer()) != null) {

								if (checkedTables.get(currentTable.getServer())
										.get(currentTable.getDatabaseUser()) != null) {
									checkedTables.get(currentTable.getServer())
									.get(currentTable.getDatabaseUser())
									.add(currentTable.getName());
								} else {
									List<String> tmpList = new LinkedList<String>();
									tmpList.add(currentTable.getName());
									checkedTables.get(currentTable.getServer())
									.put(currentTable.getDatabaseUser(),
											tmpList);
								}
							} else {
								HashMap<String, List<String>> tmpHash = new HashMap<String, List<String>>();
								List<String> tmpList = new LinkedList<String>();
								tmpList.add(currentTable.getName());
								tmpHash.put(currentTable.getDatabaseUser(), tmpList);
								checkedTables.put(currentTable.getServer(),
										tmpHash);
							}
						}
					}

				}

			});
			serverViewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					TreeViewer viewer = (TreeViewer) event.getViewer();
					IStructuredSelection thisSelection = (IStructuredSelection) event
							.getSelection();
					Object selectedNode = thisSelection.getFirstElement();
					viewer.setExpandedState(selectedNode,
							!viewer.getExpandedState(selectedNode));
				}
			});

			serverViewer.getTree().addSelectionListener(
					new SelectionListener() {

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
						}

						@Override
						public void widgetSelected(SelectionEvent e) {

							if (combo != null) {
								combo.dispose();
							}
							Object[] checkedElements = serverViewer
									.getCheckedElements();
							if (checkedElements.length > 0) {

								// tableName =
								// ((ServerTable)checkedElements[0]).getName();
							}
							// if (btnAutoLoadNSave.getSelection()){
							// if (viewer.getTree().getSelection()[0].getData()
							// instanceof ServerTable && server !=null){
							if ((server != null) && (tableName != null)) {
								DBWizardPage2.this.saveCurrentTableToDisc();
							}
							// }
							table.removeAll();
							table.clearAll();

							if (serverViewer.getTree().getSelectionCount() > 0) {
								if ((serverViewer.getTree().getSelection()[0]
										.getData() instanceof ServerTable)) {
									tableName = serverViewer.getTree()
											.getSelection()[0].getText();
									if (serverViewer.getTree().getSelection()[0]
											.getData() instanceof ServerTable) {
										server = ServerList
												.getSourceServers()
												.get(serverViewer.getTree()
														.getSelection()[0]
																.getParentItem()
																.getParentItem()
																.getText());
										loadCurrentTableFromDB();
									}
									// if (btnAutoLoadNSave.getSelection()){
									if (serverViewer.getTree().getSelection()[0]
											.getData() instanceof ServerTable) {
										DBWizardPage2.this
										.loadCurrentTableFromDisc();
										// }
										// checkTable();
									}
								} else {
									tableName = null;
								}
								DBWizardPage2.this.checkTable();
							}
						}
					});

			MenuItem addImportDBServerMenuItem = new MenuItem(importServerMenu,
					SWT.PUSH);
			addImportDBServerMenuItem.setText("Add Server");
			addImportDBServerMenuItem
			.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					DBWizardPage2.this.addSourceDBServer();
				}
			});

			final MenuItem editImportDBServerMenuItem = new MenuItem(
					importServerMenu, SWT.PUSH);
			editImportDBServerMenuItem.setText("Edit Server");
			editImportDBServerMenuItem
			.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					DBWizardPage2.this.editSourceServer();
				}
			});

			final MenuItem deleteImportDBServerMenuItem = new MenuItem(
					importServerMenu, SWT.PUSH);
			deleteImportDBServerMenuItem.setText("Delete Server");
			deleteImportDBServerMenuItem
			.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					DBWizardPage2.this.deleteSourceServer();
					if (serverViewer.getTree().getChildren() == null) {
					}
				}
			});
			new MenuItem(importServerMenu, SWT.SEPARATOR);

			MenuItem exportImportDBServerMenuItem = new MenuItem(
					importServerMenu, SWT.PUSH);
			exportImportDBServerMenuItem.setText("Export Server");
			exportImportDBServerMenuItem
			.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					DBWizardPage2.this.exportSourceServer();
				}
			});
			MenuItem importImportDBServerMenuItem = new MenuItem(
					importServerMenu, SWT.PUSH);
			importImportDBServerMenuItem.setText("Import Server");
			importImportDBServerMenuItem
			.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					DBWizardPage2.this.importSourceServer();
				}
			});
			new MenuItem(importServerMenu, SWT.SEPARATOR);
			MenuItem refreshImportDBServerMenuItem = new MenuItem(
					importServerMenu, SWT.PUSH);
			refreshImportDBServerMenuItem.setText("Refresh");
			sashForm.setWeights(new int[] {233, 513});
			refreshImportDBServerMenuItem
			.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					refresh();
				}
			});

			importServerMenu.addListener(SWT.Show, new Listener() {

				@Override
				public void handleEvent(Event event) {
					if (serverViewer.getTree().getSelectionCount() > 0) {
						if (serverViewer.getTree().getSelection()[0]
								.getParentItem() == null) {
							editImportDBServerMenuItem.setEnabled(true);
							deleteImportDBServerMenuItem.setEnabled(true);
						} else {
							deleteImportDBServerMenuItem.setEnabled(false);
							editImportDBServerMenuItem.setEnabled(false);
						}
					}
				}
			});

			setPageComplete(false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public IWizardPage getNextPage() {
		saveCurrentTableToDisc();
		DBImportWizard.setThree(new DBWizardPage3());
		return DBImportWizard.three;
	}

	private void addSourceDBServer() {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		try {
			serverViewer = (CheckboxTreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.addSourceServer",
					null);
			serverViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.addSourceServer.command not found");
		}
	}

	/**
	 * Checks the active, shown table, if patientID is assigned.
	 */
	private void checkTable() {
		boolean pid = false;
		getWizard().getContainer().updateButtons();
		TableItem[] items = table.getItems();
		for (TableItem item2 : items) {
			if (item2.getText(METADATACOLUMN).toLowerCase().equals("patientid")) {
				pid = true;
				break;
			}
		}
		if (!pid
				&& serverViewer.getTree().getSelection()[0].getData()
				.getClass().equals(ServerTable.class)) {
			serverViewer.getTree().getSelection()[0].setImage(PlatformUI
					.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_ETOOL_DELETE));
		} else if (pid
				&& serverViewer.getTree().getSelection()[0].getData()
				.getClass().equals(ServerTable.class)) {
			serverViewer.getTree().getSelection()[0].setImage(PlatformUI
					.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE));
		}

	}

	private void clearMetaDataFromTable() {
		clearSingleTable();
		loadCurrentTableFromDB();
	}

	/**
	 * commands for importDBServer
	 */
	private void deleteSourceServer() {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		try {
			serverViewer = (CheckboxTreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.deleteSourceServer",
					null);
			serverViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.deleteSourceServer.command not found");
		}
	}

	private void editSourceServer() {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		try {
			serverViewer = (CheckboxTreeViewer) handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.editSourceServer",
					null);
			serverViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.editSourceServer.command not found");
		}
	}

	private void exportSourceServer() {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.exportSourceServer",
					null);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.exportSourceServer.command not found");
		}
	}

	private void importSourceServer() {
		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		try {
			handlerService.executeCommand(
					"de.goettingen.i2b2.importtool.idrt.importSourceServer",
					null);
			serverViewer.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("de.goettingen.i2b2.importtool.idrt.importSourceServer.command not found");
		}
	}

	private void layoutSourceServerContextMenu() {
	}

	private void loadCurrentTableFromDB() {

		table.removeAll();
		tableName = serverViewer.getTree().getSelection()[0].getText();
		server = ServerList.getSourceServers().get(
				serverViewer.getTree().getSelection()[0].getParentItem()
				.getParentItem().getText());
		final ResultSet rset = ServerList.getPreview(tableName, serverViewer
				.getTree().getSelection()[0].getParentItem().getText(), server);
		try {
			int columnCount = rset.getMetaData().getColumnCount();
			List<String> rowContent = new LinkedList<String>();
			while (rset.next()) {
				for (int i = 1; i <= columnCount; i++) {
					rowContent.add(rset.getString(i));
				}
			}

			for (int i = 1; i <= columnCount; i++) {
				String dataType = null;
				if (rset.getMetaData().getColumnClassName(i)
						.equalsIgnoreCase("java.lang.String")) {
					dataType = "String";
				} else if (rset.getMetaData().getColumnClassName(i)
						.equalsIgnoreCase("java.sql.Timestamp")) {
					dataType = "Date";
				} else if (rset.getMetaData().getColumnClassName(i)
						.equalsIgnoreCase("java.math.BigDecimal")) {
					dataType = "Float";
				}else if (rset.getMetaData().getColumnClassName(i)
						.equalsIgnoreCase("java.lang.Integer")) {
					dataType = "Integer";
				}else {
					dataType = rset.getMetaData().getColumnClassName(i);
				}
				TableRow tableRow = new TableRow(rset.getMetaData()
						.getColumnName(i), rowContent, dataType, "");

				final TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, rset.getMetaData().getColumnName(i));
				item.setText(NAMECOLUMN, "");
				item.setText(TOOLTIPCOLUMN, "");
				item.setText(DATATYPECOLUMN, dataType);
				item.setText(METADATACOLUMN, "");
				item.setData(rowContent);
				tableItems.add(item);
				tableRows.add(tableRow);
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Loads the current table from disc.
	 */
	@SuppressWarnings("unchecked")
	private void loadCurrentTableFromDisc() {
		try {
			File serverFile = FileHandler.getBundleFile("/cfg/tables");

			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					serverFile));
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			is.close();

			if (tableMap.get(server.getUniqueID()) != null) {

				List<List<String>> itemList = tableMap
						.get(server.getUniqueID()).get(tableName);
				if (itemList != null) {
					TableItem[] oldTableItems = table.getItems();
					for (int i = 0; i < itemList.size(); i++) {
						List<String> currentList = itemList.get(i);
						for (int j = 1; j < 5; j++) {
							oldTableItems[i].setText(j, currentList.get(j));
						}
					}
				}
			}

		}catch (EOFException ee) {
			System.err.println("EOF");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Saves the current table to disc.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	private void saveCurrentTableToDisc() {
		try {
			File serverFile = FileHandler.getBundleFile("/cfg/tables");
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(serverFile));

			// List<List<String>> itemList = (List<List<String>>)
			// is.readObject();
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			if (tableMap == null) {
				tableMap = new HashMap<String, HashMap<String, List<List<String>>>>();
			}

			TableItem[] tableItems = table.getItems();
			List<List<String>> itemList = new LinkedList<List<String>>();
			for (TableItem currentItem : tableItems) {
				List<String> currentItemList = new LinkedList<String>();
				for (int j = 0; j < 5; j++) {
					currentItemList.add(currentItem.getText(j));
				}
				itemList.add(currentItemList);
			}

			if (server == null) {
			}
			if (tableMap.get(server.getUniqueID()) == null) {
				HashMap<String, List<List<String>>> tmpHash = new HashMap<String, List<List<String>>>();
				tmpHash.put(tableName, itemList);
				tableMap.put(server.getUniqueID(), tmpHash);
			} else {
				tableMap.get(server.getUniqueID()).put(tableName, itemList);
			}
			ObjectOutputStream os;
			os = new ObjectOutputStream(new FileOutputStream(serverFile));
			os.writeObject(tableMap);
			os.flush();
			os.close();

		} catch (EOFException eee) {
			File serverFile = FileHandler.getBundleFile("/cfg/tables");
			tableMap = new HashMap<String, HashMap<String, List<List<String>>>>();
			TableItem[] tableItems = table.getItems();
			List<List<String>> itemList = new LinkedList<List<String>>();
			for (TableItem currentItem : tableItems) {
				List<String> currentItemList = new LinkedList<String>();
				for (int j = 0; j < 5; j++) {
					currentItemList.add(currentItem.getText(j));
				}
				itemList.add(currentItemList);
			}

			if (tableMap.get(server.getUniqueID()) == null) {
				HashMap<String, List<List<String>>> tmpHash = new HashMap<String, List<List<String>>>();
				tmpHash.put(tableName, itemList);
				tableMap.put(server.getUniqueID(), tmpHash);
			} else {
				tableMap.get(server.getUniqueID()).put(tableName, itemList);
			}
			ObjectOutputStream os;
			try {
				os = new ObjectOutputStream(new FileOutputStream(serverFile));
				os.writeObject(tableMap);
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private boolean tablesComplete() {
		boolean complete = false;
		boolean tableComplete = false;

		try {
			File serverFile = FileHandler.getBundleFile("/cfg/tables");
			System.out.println(serverFile.getAbsolutePath());
			if (!serverFile.exists()) {
				serverFile = new File(serverFile.getAbsolutePath());
				serverFile.createNewFile();				
			}

			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					serverFile));
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			is.close();

			HashMap<Server, HashMap<String, java.util.List<String>>> tables = DBWizardPage2
					.getCheckedTables();
			if (!tables.isEmpty()) {
				Iterator<Server> tableServerIterator = tables.keySet()
						.iterator();

				// server
				while (tableServerIterator.hasNext()) {
					Server currentServer = tableServerIterator.next();
					HashMap<String, java.util.List<String>> schemaHashMap = tables
							.get(currentServer);
					Iterator<String> schemaHashMapIterator = schemaHashMap
							.keySet().iterator();

					// schema
					while (schemaHashMapIterator.hasNext()) {
						String currentSchema = schemaHashMapIterator.next();
						java.util.List<String> currentTables = schemaHashMap
								.get(currentSchema);
						Iterator<String> tablesTableIterator = currentTables
								.iterator();

						// tables
						while (tablesTableIterator.hasNext()) {
							String currentTable = tablesTableIterator.next();
							if (tableMap.get(currentServer.getUniqueID()) != null) {
								List<List<String>> itemList = tableMap.get(
										currentServer.getUniqueID()).get(
												currentTable);
								if (itemList != null) {
									table.getItems();

									// single items
									for (int i = 0; i < itemList.size(); i++) {
										List<String> currentList = itemList
												.get(i);
										if (currentList.get(3).toLowerCase()
												.contains("patientid")) {
											complete = true;
											continue;
										}

									}
								}
							}
							// tabelle hatte keine pid
							if (complete == false) {
								setErrorMessage("Error in table: "
										+ currentTable + ". PatientID missing?");
								tableComplete = false;
								break;
							} else {
								complete = false;
								tableComplete = true;
							}
						}
						if (tableComplete == false) {
							break;
						}
					}
					if (tableComplete == false) {
						break;
					}
				}
			} else {
				setErrorMessage(null);
			}

		}		catch (EOFException ee) {
			System.err.println("Tables empty!");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		if (tableComplete) {
			setErrorMessage(null);
		}
		return tableComplete;
	}
}