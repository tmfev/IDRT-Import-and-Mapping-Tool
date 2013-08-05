package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
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
import org.osgi.framework.Bundle;

import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.server.Settings.Server;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerList;
import de.umg.mi.idrt.idrtimporttool.server.Settings.ServerTable;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerImportDBLabelProvider;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerImportDBModel;
import de.umg.mi.idrt.idrtimporttool.server.serverWizard.ServerSourceContentProvider;
import de.umg.mi.idrt.idrtimporttool.table.TableRow;
import swing2swt.layout.BorderLayout;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class DBWizardPageTwo extends WizardPage {

	private static List<TableRow> tableRows;// = new LinkedList<TableRow>();
	private static List<TableItem> tableItems;
	private static HashMap<String, HashMap<String, List<List<String>>>> tableMap;
	private static Properties defaultProps;
	private static File properties;
	private static HashMap<Server, HashMap<String, List<String>>> checkedTables;
	private static Server server;
	private static Menu importServerMenu;
	private static String tableName;
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
	private Composite composite;
	private static Control old;
	private static TableItem item;
	private static CCombo combo;

	private static void clearSingleTable() {
		try {
			tableName = serverViewer.getTree().getSelection()[0].getText();
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/tables"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			if (url != null) {

				fileUrl = FileLocator.toFileURL(url);

			}
			File serverFile = null;
			if (fileUrl != null) {
				serverFile = new File(fileUrl.getPath());
			}

			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					serverFile));
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			is.close();

			tableMap.remove(tableName);
			System.out.println("deleted " + tableName + " from "
					+ server.getUniqueID());

			ObjectOutputStream os;
			os = new ObjectOutputStream(new FileOutputStream(serverFile));
			os.writeObject(tableMap);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return all checked tables
	 */
	public static HashMap<Server, HashMap<String, List<String>>> getCheckedTables() {
		return checkedTables;
	}

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

	/**
	 * commands for target server
	 */
	public static void refresh() {
		System.out.println("refreshing");
		serverViewer.setContentProvider(new ServerSourceContentProvider());
		serverViewer.refresh();
	}

	public DBWizardPageTwo() {
		super("Source DB Settings");
		setTitle("Source DB Settings");
		// setDescription("Source DB Settings");
	}

	@Override
	public boolean canFlipToNextPage() {
		return tablesComplete() && !checkedTables.isEmpty();
	}

	/**
	 * Checks the active, shown table, if patientID is assigned.
	 */
	private void checkTable() {
		System.out.println("CHECKING TABLE");
		boolean pid = false;
		getWizard().getContainer().updateButtons();
		TableItem[] items = table.getItems();
		for (TableItem item2 : items) {
			if (item2.getText(3).toLowerCase().equals("patientid")) {
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

		// TableItem[] items = table.getItems();
		//
		// for (int i = 0; i < items.length; i++) {
		// System.out.println("1: " + items[i].getText(1));
		// }
		clearSingleTable();
		loadCurrentTableFromDB();
	}

	@Override
	public void createControl(final Composite parent) {
		try {
			tableName = null;
			checkedTables = new HashMap<Server, HashMap<String, List<String>>>();
			tableRows = new LinkedList<TableRow>();
			tableItems = new LinkedList<TableItem>();
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/Default.properties"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			fileUrl = FileLocator.toFileURL(url);
			properties = new File(fileUrl.getPath());
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
						System.out.println("old editor !=null");
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
						if (column == 1) {
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

						else if (column == 2) {
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
						} else if (column == 3) {
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
									DBWizardPageTwo.this
											.saveCurrentTableToDisc();
									DBWizardPageTwo.this.checkTable();

								}
							});
						}
						// PID-Generator Column
						// else if (column == 4) {
						// final CCombo combo = new CCombo(table,
						// SWT.READ_ONLY);
						// String[] optionMetas =
						// getMetaComboPIDGen(optionsPIDGen,table.getItems(),item.getText(column));
						// for (int i = 0, n = optionMetas.length; i < n; i++) {
						// combo.add(optionMetas[i]);
						// }
						// combo.select(combo.indexOf(item.getText(column)));
						// editor.minimumWidth = combo.computeSize(SWT.DEFAULT,
						// SWT.DEFAULT).x;
						// combo.setFocus();
						// editor.setEditor(combo, item, column);
						//
						// final int col = column;
						// combo.addSelectionListener(new SelectionAdapter() {
						// public void widgetSelected(SelectionEvent event) {
						// item.setText(col, combo.getText());
						// combo.dispose();
						// }
						// });
						// }
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

			tblclmnDatatype = new TableColumn(table, SWT.NONE);
			tblclmnDatatype.setWidth(100);
			tblclmnDatatype.setText("Datatype");

			tblclmnMetadata = new TableColumn(table, SWT.NONE);
			tblclmnMetadata.setWidth(100);
			tblclmnMetadata.setText("Metadata");

			// tblclmnPIDGen = new TableColumn(table, SWT.NONE);
			// tblclmnPIDGen.setWidth(100);
			// tblclmnPIDGen.setText("PID-Generator");

			composite = new Composite(compositeTables, SWT.NONE);
			composite.setLayoutData(BorderLayout.SOUTH);
			composite.setLayout(new GridLayout(3, false));
			// btnSaveTable = new Button(composite, SWT.NONE);
			// btnSaveTable.addSelectionListener(new SelectionAdapter() {
			// @Override
			// public void widgetSelected(SelectionEvent e) {
			// saveCurrentTable();
			// }
			// });
			//
			// btnSaveTable.setText("Save Config Table");
			// btnLoadTable = new Button(composite, SWT.NONE);
			// btnLoadTable.setText("Load Config Table");
			// btnLoadTable.addSelectionListener(new SelectionListener() {
			//
			// @Override
			// public void widgetSelected(SelectionEvent e) {
			// loadCurrentTableFromDisc();
			// }
			//
			// @Override
			// public void widgetDefaultSelected(SelectionEvent e) {
			// }
			// });
			// AUTOLOADANDSAVE
			// btnAutoLoadNSave = new Button(composite, SWT.CHECK);
			// btnAutoLoadNSave.setText("Auto Load and Save");
			// btnAutoLoadNSave.setSelection(Boolean.parseBoolean(defaultProps.getProperty(("autoLoadAndSave"))));
			// btnAutoLoadNSave.addSelectionListener(new SelectionListener() {
			//
			// @Override
			// public void widgetSelected(SelectionEvent e) {
			// if(btnAutoLoadNSave.getSelection()){
			// defaultProps.setProperty(("autoLoadAndSave"),"true");
			// }
			// else {
			// defaultProps.setProperty(("autoLoadAndSave"),"false");
			// }
			// try {
			// defaultProps.store(new FileWriter(new
			// File(properties.getAbsolutePath())), "");
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			// }
			//
			// @Override
			// public void widgetDefaultSelected(SelectionEvent e) {
			// }
			// });

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

					boolean cont = MessageDialog.openConfirm(parent.getShell(),
							"Guess Schema?",
							"This will overwrite your current Config!\nContinue?");

					if (cont) {
						DBWizardPageTwo.this.clearMetaDataFromTable();

						System.out.println("Guessing Schema");
						TableItem[] items = table.getItems();

						for (int i = 0; i < items.length; i++) {
							System.out.println("0: " + items[i].getText(0));
							System.out.println("1: " + items[i].getText(1));
							System.out.println();

							if (items[i].getText(0).toLowerCase()
									.contains("patient")
									|| items[i].getText(0).toLowerCase()
											.contains("pid")) {
								table.getItems()[i].setText(3, "PatientID");
								continue;
							} else if (items[i].getText(0).toLowerCase()
									.contains("encounter")
									|| items[i].getText(0).toLowerCase()
											.contains("enc")) {
								table.getItems()[i].setText(3, "EncounterID");
								continue;
							} else if (items[i].getText(0).toLowerCase()
									.contains("update")) {
								table.getItems()[i].setText(3, "UpdateDate");
								continue;
							} else if (items[i].getText(0).toLowerCase()
									.contains("import")) {
								table.getItems()[i].setText(3, "ImportDate");
								continue;
							} else if (items[i].getText(0).toLowerCase()
									.contains("download")) {
								table.getItems()[i].setText(3, "DownloadDate");
								continue;
							} else if (items[i].getText(0).toLowerCase()
									.contains("source")) {
								table.getItems()[i].setText(3, "SourceSystem");
								continue;
							}
						}
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

					boolean cont = MessageDialog.openConfirm(parent.getShell(),
							"Clear Table?",
							"This will clear the current Table!\nContinue?");

					if (cont) {
						clearSingleTable();
						DBWizardPageTwo.this.loadCurrentTableFromDB();
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
					System.out.println("deleting saved tables");
					try {
						boolean result = MessageDialog.openConfirm(
								parent.getShell(), "Clear All Tables?",
								"Do you really want to clear all tables?");

						if (result) {
							Bundle bundle = Activator.getDefault().getBundle();
							Path imgImportPath = new Path("/cfg/tables"); //$NON-NLS-1$
							URL url = FileLocator.find(bundle, imgImportPath,
									Collections.EMPTY_MAP);
							URL fileUrl = FileLocator.toFileURL(url);
							File serverFile = new File(fileUrl.getPath());
							tableMap = new HashMap<String, HashMap<String, List<List<String>>>>();
							System.out.println(serverFile.getAbsolutePath());
							ObjectOutputStream os = new ObjectOutputStream(
									new FileOutputStream(serverFile));
							// os.writeObject(itemList);
							os.writeObject(tableMap);
							os.flush();
							os.close();
							DBWizardPageTwo.this.loadCurrentTableFromDB();
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

			sashForm.setWeights(new int[] { 1, 2 });
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
					DBWizardPageTwo.this.getWizard().getContainer()
							.updateButtons();
					Object[] checkedElements = serverViewer
							.getCheckedElements();
					for (Object checkedElement : checkedElements) {
						if (checkedElement.getClass().equals(ServerTable.class)) {
							ServerTable currentTable = (ServerTable) checkedElement;
							System.out.println("currenttable server: "
									+ currentTable.getServer());
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
								System.out.println("saved: " + tableName);
								DBWizardPageTwo.this.saveCurrentTableToDisc();
							}
							// }
							table.removeAll();
							table.clearAll();

							if (serverViewer.getTree().getSelectionCount() > 0) {
								if ((serverViewer.getTree().getSelection()[0]
										.getData() instanceof ServerTable)) {
									System.out.println("class: "
											+ serverViewer.getTree()
													.getSelection()[0]
													.getData().getClass());
									tableName = serverViewer.getTree()
											.getSelection()[0].getText();
									System.out.println("NEW TABLE NAME: "
											+ tableName);
									if (serverViewer.getTree().getSelection()[0]
											.getData() instanceof ServerTable) {
										server = ServerList
												.getSourceServers()
												.get(serverViewer.getTree()
														.getSelection()[0]
														.getParentItem()
														.getParentItem()
														.getText());
										DBWizardPageTwo.this
												.loadCurrentTableFromDB();
									}
									// if (btnAutoLoadNSave.getSelection()){
									if (serverViewer.getTree().getSelection()[0]
											.getData() instanceof ServerTable) {
										DBWizardPageTwo.this
												.loadCurrentTableFromDisc();
										// }
										// checkTable();
									}
								} else {
									tableName = null;
								}
								DBWizardPageTwo.this.checkTable();
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
							System.out.println("add server clicked");
							DBWizardPageTwo.this.addSourceDBServer();
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
							System.out.println("edit server clicked");
							DBWizardPageTwo.this.editSourceServer();
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
							System.out.println("delete server clicked");
							DBWizardPageTwo.this.deleteSourceServer();
							if (serverViewer.getTree().getChildren() == null) {
								System.out.println("empty tree");
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
							DBWizardPageTwo.this.exportSourceServer();
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
							DBWizardPageTwo.this.importSourceServer();
						}
					});
			new MenuItem(importServerMenu, SWT.SEPARATOR);
			MenuItem refreshImportDBServerMenuItem = new MenuItem(
					importServerMenu, SWT.PUSH);
			refreshImportDBServerMenuItem.setText("Refresh");
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

	@Override
	public IWizardPage getNextPage() {
		saveCurrentTableToDisc();
		DBImportWizard.setThree(new DBWizardPageThree());
		return DBImportWizard.three;
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
			System.out.println("count: " + columnCount);
			System.out.println("size: " + rset.getFetchSize());
			List<String> rowContent = new LinkedList<String>();
			while (rset.next()) {
				for (int i = 1; i <= columnCount; i++) {
					rowContent.add(rset.getString(i));
				}
			}

			for (int i = 1; i <= columnCount; i++) {
				String dataType = null;
				System.out.println("DATATYPE: " + rset.getMetaData().getColumnClassName(i));
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
				item.setText(1, "");
				item.setText(2, dataType);
				item.setText(3, "");
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
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/tables"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			if (url != null) {
				fileUrl = FileLocator.toFileURL(url);
			}
			File serverFile = null;
			if (fileUrl != null) {
				serverFile = new File(fileUrl.getPath());
			}

			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					serverFile));
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			is.close();
			System.out.println("loading " + tableName + " "
					+ server.getUniqueID());

			if (tableMap.get(server.getUniqueID()) == null) {
				System.out.println("get server null");
			} else {
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
	 */
	private void saveCurrentTableToDisc() {
		try {
			System.out.println("@saveCurrentTable() -- saving: " + tableName);
			Bundle bundle = Activator.getDefault().getBundle();
			Path imgImportPath = new Path("/cfg/tables"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, imgImportPath,
					Collections.EMPTY_MAP);
			URL fileUrl = FileLocator.toFileURL(url);
			File serverFile = new File(fileUrl.getPath());
			ObjectInputStream is;
			// tableMap = new HashMap<String, List<List<String>>>();
			is = new ObjectInputStream(new FileInputStream(serverFile));
			// List<List<String>> itemList = (List<List<String>>)
			// is.readObject();
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			if (tableMap == null) {
				tableMap = new HashMap<String, HashMap<String, List<List<String>>>>();
			}
			is.close();
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
				System.out.println("SERVER NULL???!?!?");
			}
			if (tableMap.get(server.getUniqueID()) == null) {
				System.out.println("save getserver null");
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
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private boolean tablesComplete() {
		boolean complete = false;
		boolean tableComplete = false;

		try {
			Bundle bundle = Activator.getDefault().getBundle();
			Path propPath = new Path("/cfg/tables"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, propPath, Collections.EMPTY_MAP);
			URL fileUrl = null;
			if (url != null) {
				fileUrl = FileLocator.toFileURL(url);
			}
			File serverFile = null;
			if (fileUrl != null) {
				serverFile = new File(fileUrl.getPath());
			}

			ObjectInputStream is = new ObjectInputStream(new FileInputStream(
					serverFile));
			tableMap = (HashMap<String, HashMap<String, List<List<String>>>>) is
					.readObject();
			is.close();
			// System.out.println("loading " + tableName + " " +
			// server.getUniqueID());

			HashMap<Server, HashMap<String, java.util.List<String>>> tables = DBWizardPageTwo
					.getCheckedTables();
			System.out.println("CHECKED TABLEs@tablesComplete: " + tables);
			if (!tables.isEmpty()) {
				Iterator<Server> tableServerIterator = tables.keySet()
						.iterator();

				// server
				while (tableServerIterator.hasNext()) {
					Server currentServer = tableServerIterator.next();
					System.out.println("currentServer: " + currentServer);
					HashMap<String, java.util.List<String>> schemaHashMap = tables
							.get(currentServer);
					Iterator<String> schemaHashMapIterator = schemaHashMap
							.keySet().iterator();

					// schema
					while (schemaHashMapIterator.hasNext()) {
						String currentSchema = schemaHashMapIterator.next();
						System.out.println("currentSchema: " + currentSchema);
						java.util.List<String> currentTables = schemaHashMap
								.get(currentSchema);
						Iterator<String> tablesTableIterator = currentTables
								.iterator();

						// tables
						while (tablesTableIterator.hasNext()) {
							String currentTable = tablesTableIterator.next();
							System.out.println("currentTable: " + currentTable);
							// list.add(currentServer.getUniqueID() + ": " +
							// currentSchema + " - " +
							// tablesTableIterator.next());
							if (tableMap.get(currentServer.getUniqueID()) == null) {
								System.out.println("get server null");
							} else {
								List<List<String>> itemList = tableMap.get(
										currentServer.getUniqueID()).get(
										currentTable);
								if (itemList != null) {
									table.getItems();

									// single items
									for (int i = 0; i < itemList.size(); i++) {
										List<String> currentList = itemList
												.get(i);
										// oldTableItems[i].setText(j,
										// currentList.get(j));
										// check if patient id is set
										if (currentList.get(3).toLowerCase()
												.contains("patientid")) {
											System.out.println("PID FOUND: "
													+ currentList.get(3));
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
				System.out.println("TABLES EMPTY!");
				setErrorMessage(null);
			}

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