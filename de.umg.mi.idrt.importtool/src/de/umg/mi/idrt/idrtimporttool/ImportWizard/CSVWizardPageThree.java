package de.umg.mi.idrt.idrtimporttool.ImportWizard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
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
import org.osgi.framework.Bundle;

import swing2swt.layout.BorderLayout;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.umg.mi.idrt.idrtimporttool.Log.Log;
import de.umg.mi.idrt.idrtimporttool.commands.CSVImportCommand;
import de.umg.mi.idrt.idrtimporttool.importidrt.Activator;
import de.umg.mi.idrt.idrtimporttool.messages.Messages;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class CSVWizardPageThree extends WizardPage {

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
	private TableColumn tblclmnPIDGen;
	private Composite compositeTables;
	private boolean allConfigs;

	private static List<String> configList;

	/**
	 * Default Properties for the TOS Jobs
	 */
	private static Properties defaultProps;
	private static char DEFAULTDELIM = ';';


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

	/**
	 * Default Constructor
	 */
	public CSVWizardPageThree() {
		super(Messages.CSVWizardPageThree_CSVImportSettings);
		setTitle(Messages.CSVWizardPageThree_CSVImportSettings); 
		setWizard(CSVImportCommand.getWizard());
		setDescription(Messages.CSVWizardPageThree_CSVImportSettings); 
	}

	@Override
	public void createControl(final Composite parent) {
		try {
			lastTable = null;
			fileConfigMap = new HashMap<String, String>();

			/*
			 * Filling the Mapping between the config and data files 
			 */
			Bundle bundle = Activator.getDefault().getBundle();
			Path imgConfigPath = new Path("/images/itemstatus-checkmark16.png"); 
			URL imageConfigURL = FileLocator.find(bundle, imgConfigPath,
					Collections.EMPTY_MAP);
			URL imageConfigURL2 = FileLocator.toFileURL(imageConfigURL);
			File imgConfigFile = new File(imageConfigURL2.getPath());

			Path imgNoConfigPath = new Path("/images/remove-grouping.png"); 
			URL imageNoConfigURL = FileLocator.find(bundle, imgNoConfigPath,
					Collections.EMPTY_MAP);
			URL imageNoConfigURL2 = FileLocator.toFileURL(imageNoConfigURL);
			File imgNoConfigFile = new File(imageNoConfigURL2.getPath());
			final Image imgHasConfig = new Image(parent.getDisplay(),
					imgConfigFile.getAbsolutePath());
			final Image imgNoConfig = new Image(parent.getDisplay(),
					imgNoConfigFile.getAbsolutePath());
			csvFolder = CSVWizardPageTwo.getFolderCSVText();

			File folder = new File(csvFolder);
			File[] listOfFiles = folder.listFiles();
			configList = new LinkedList<String>();
			list = new Vector<String>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].getName().endsWith(".cfg.csv")) { 
					configList.add(listOfFiles[i].getName());
				}
				if (!listOfFiles[i].getName().endsWith(".cfg.csv") 
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
						System.out.println("put: " + list.get(i) + " "  
								+ filename + ".cfg" + extension); 
					}
				}
			}

			Path path = new Path("/cfg/Default.properties"); 
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = null;

			fileUrl = FileLocator.toFileURL(url);
			File properties = new File(fileUrl.getPath());
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

							// Datatype of the column
						} else if (column == 2) {
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
						} else if (column == 3) {
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
									// checkTable();
								}
							});
							// PID-Generator specific Metadata (e.g. bday, name, lastname...)
						} else if ((column == 4)
								&& CSVWizardPageTwo.getBtnRADIOCsvfile()
								&& CSVWizardPageTwo.getUsePid()) {
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
				public void widgetSelected(SelectionEvent e) {

					boolean cont = MessageDialog.openConfirm(parent.getShell(),
							"Overwrite all Configs?",
							"Do you really want to overwirte all Configs?");
					if (cont) {
						for (int i = 0; i<serverListViewer.getTable().getItemCount();i++) {
							allConfigs = true;
							serverListViewer.getTable().select(i);
							System.out.println("SELECTED: " + serverListViewer.getTable().getItem(i).getText());
							serverListViewer.getTable().notifyListeners(SWT.Selection, new Event());
							btnGuessSchema.notifyListeners(SWT.Selection, new Event());

							serverListViewer.getTable().deselect(i);
							allConfigs = false;
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
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
						String filename = tmpElement.substring(0,
								tmpElement.lastIndexOf(".")); 
						String extension = tmpElement.substring(tmpElement
								.lastIndexOf(".")); 
						if (configList.contains(filename + ".cfg" + extension)) { 
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

			tblclmnDatatype = new TableColumn(table, SWT.NONE);
			tblclmnDatatype.setWidth(100);
			tblclmnDatatype.setText("Datatype"); 

			tblclmnMetadata = new TableColumn(table, SWT.NONE);
			tblclmnMetadata.setWidth(100);
			tblclmnMetadata.setText("Metadata"); 

			// Display only if PID-Generator was selected
			if (CSVWizardPageTwo.getBtnRADIOCsvfile()
					&& CSVWizardPageTwo.getUsePid()) {
				tblclmnPIDGen = new TableColumn(table, SWT.NONE);
				tblclmnPIDGen.setWidth(100);
				tblclmnPIDGen.setText("PID-Generator"); 
			}

			buttonComposite = new Composite(compositeTables, SWT.NONE);
			buttonComposite.setLayoutData(BorderLayout.SOUTH);
			buttonComposite.setLayout(new GridLayout(2, false));
			serverListViewer.getTable().addSelectionListener(
					new SelectionListener() {
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
							System.out.println("widgetDefaultSelected");
						}

						@Override
						public void widgetSelected(SelectionEvent e) {

							try {

								/*
								 * Fills the table on click by reading or creating a config file
								 */
								if ((lastTable != null)
										&& (fileConfigMap.get(lastTable) != null)) {
									System.out.println("saved lasttable: " 
											+ lastTable);
									saveTable();
								}

								if (lastTable == null) {
									lastTable = serverListViewer.getTable()
											.getSelection()[0].getText();
								}
								if (fileConfigMap.get(serverListViewer
										.getTable().getSelection()[0].getText()) != null) {
									File configFile = new File(csvFolder
											+ fileConfigMap
											.get(serverListViewer
													.getTable()
													.getSelection()[0]
															.getText()));
									char inputDelim = DEFAULTDELIM;
									CSVReader reader = new CSVReader(
											new FileReader(configFile),
											DEFAULTDELIM);
									String[] testLine = reader.readNext();
									reader.close();
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
										DEFAULTDELIM = inputDelim;

										reader = new CSVReader(new FileReader(
												configFile), inputDelim);
										String[] line1 = reader.readNext();
										String[] line2 = reader.readNext();
										String[] line3 = reader.readNext();
										String[] line4 = reader.readNext();
										String[] line5 = reader.readNext();
										reader.close();
										lastTable = serverListViewer.getTable()
												.getSelection()[0].getText();
										table.removeAll();
										table.clearAll();
										for (int i = 1; i < line1.length; i++) {

											final TableItem item = new TableItem(
													table, SWT.NONE);
											item.setText(0, line1[i]);
											item.setText(1, line3[i]);
											item.setText(2, line2[i]);
											item.setText(3, line4[i]);
											if (line5!= null)
												item.setText(4, line5[i]);
										}
									}
								} else {
									lastTable = serverListViewer.getTable()
											.getSelection()[0].getText();
									table.removeAll();
									table.clearAll();
									char inputDelim = DEFAULTDELIM;
									CSVReader reader = new CSVReader(
											new FileReader(csvFolder
													+ serverListViewer
													.getTable()
													.getSelection()[0]
															.getText()),
															DEFAULTDELIM);
									String[] testLine = reader.readNext();
									reader.close();

									if (testLine.length == 1) {
										System.err.println("Wrong delimiter?"); 
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
									DEFAULTDELIM = inputDelim;
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
									System.out.println("reading: " 
											+ csvFolder
											+ serverListViewer.getTable()
											.getSelection()[0]
													.getText());

									reader = new CSVReader(new FileReader(
											csvFolder
											+ serverListViewer
											.getTable()
											.getSelection()[0]
													.getText()),
													inputDelim);
									String[] line1 = reader.readNext();
									reader.close();
									for (String element : line1) {
										final TableItem item = new TableItem(
												table, SWT.NONE);
										item.setText(0, element);
										item.setText(1, ""); 
										item.setText(2, ""); 
										item.setText(3, ""); 
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
												DEFAULTDELIM);

										TableItem[] tableItems = table
												.getItems();
										String[] nextLine = new String[tableItems.length + 1];

										nextLine[0] = "Spaltenname (Pflicht)"; 
										if (tableItems != null) {
											nextLine[0] = "Spaltenname (Pflicht)"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(0);
											}
											rotatedOutput.writeNext(nextLine);

											nextLine[0] = "Datentyp (Pflicht)"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(2);
											}
											rotatedOutput.writeNext(nextLine);

											nextLine[0] = "Name (kann leer sein)"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(1);
											}
											rotatedOutput.writeNext(nextLine);

											nextLine[0] = "Metainformationen"; 
											for (int i = 0; i < tableItems.length; i++) {
												nextLine[i + 1] = tableItems[i]
														.getText(3);
											}
											rotatedOutput.writeNext(nextLine);
										}
										rotatedOutput.close();
										System.out.println("writing cfg done"); 
									} catch (IOException e2) {
										e2.printStackTrace();
									}
								}
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e2) {
								e2.printStackTrace();
							}

						}
					});


			clearTables = new Button(buttonComposite, SWT.PUSH);
			clearTables.setText(Messages.CSVWizardPageThree_ClearTable);
			clearTables.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean cont = MessageDialog.openConfirm(parent.getShell(),
							Messages.CSVWizardPageThree_ClearTableConfirm,
							Messages.CSVWizardPageThree_ClearTableConfirmText);
					if (cont) {
						clearTable();
					}
				}
			});
			btnGuessSchema = new Button(buttonComposite, SWT.NONE);
			btnGuessSchema.setText(Messages.CSVWizardPageThree_GuessSchema);
			btnGuessSchema.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {

					boolean cont = allConfigs;

					if (!cont) {
						cont = MessageDialog.openConfirm(parent.getShell(),
								Messages.CSVWizardPageThree_GuessSchemaConfirm,
								Messages.CSVWizardPageThree_GuessSchemaConfirmText);
					}
					if (cont) {
						clearMetaDataFromTable();

						System.out.println("fileConfigMap: " 
								+ fileConfigMap.toString());
						System.out.println("Guessing Schema"); 
						System.out.println("selected file: " 
								+ csvFolder
								+ serverListViewer.getTable().getSelection()[0]
										.getText());
						try {
							char inputDelim = ';';
							CSVReader reader = new CSVReader(new FileReader(
									csvFolder
									+ serverListViewer.getTable()
									.getSelection()[0]
											.getText()), inputDelim);

							String[] nextLine = reader.readNext();
							if (nextLine.length == 1) {
								System.err.println("Wrong delimiter?"); 
								if (inputDelim == DEFAULTDELIM) {
									inputDelim = '\t';
									System.err.println("Delimiter set to: \\t"); 
								} else {
									inputDelim = DEFAULTDELIM;
									System.err.println("Delimiter set to: " 
											+ DEFAULTDELIM);
								}
							}
							reader.close();

							reader = new CSVReader(new FileReader(csvFolder
									+ serverListViewer.getTable()
									.getSelection()[0].getText()),
									inputDelim);
							DEFAULTDELIM = inputDelim;

							nextLine = reader.readNext();

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
							System.out.println("GUESSROW: " + guessRow); 
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
										Float.parseFloat(next);
										countFloat++;
										minusString = true;
									} catch (Exception e4) {
									}

									if (minusString) {
										countString--;
									}
								}

								if ((countFloat == 0) && (countInt == 0)
										&& (countString > 0)) {
									table.getItems()[i].setText(2, "String"); 
								} else if ((countFloat > countInt)
										&& (countString <= 0)) {
									table.getItems()[i].setText(2, "Float"); 
								} else if ((countInt > countFloat)
										&& (countString <= 0) && countString ==0) {
									table.getItems()[i].setText(2, "Integer"); 
								} else if ((countFloat == countInt)
										&& (countFloat != 0) && countString ==0) {
									table.getItems()[i].setText(2, "Integer"); 
								} else if ((countDate > 0)
										&& (countString <= 0)) {
									table.getItems()[i].setText(2, "Date"); 
								} else {
									table.getItems()[i].setText(2, "String"); 
								}
							}
							reader = new CSVReader(new FileReader(csvFolder
									+ serverListViewer.getTable()
									.getSelection()[0].getText()),
									inputDelim);
							DEFAULTDELIM = inputDelim;

							nextLine = reader.readNext();
							reader.close();
							boolean foundPid = false;
							boolean foundEnc = false;
							boolean foundUpdate = false;
							boolean foundImp = false;
							boolean foundDownl = false;
							boolean foundSource = false;
							boolean foundStart = false;
							boolean foundEnd = false;

							for (int i = 0; i < nextLine.length; i++) {
								if (!foundPid&& table.getItems()[i].getText(2).equals("Integer")&& (nextLine[i].toLowerCase().contains("patient")  
										|| nextLine[i].toLowerCase().contains(
												"pid") || nextLine[i].toLowerCase().contains( 
														"pat_id"))) { 
									table.getItems()[i].setText(3, "PatientID"); 

									foundPid = true;
									continue;
								} else if (!foundEnc
										&& table.getItems()[i].getText(2)
										.equals("Integer") 
										&& nextLine[i].toLowerCase().contains(
												"encounter") 
												|| nextLine[i].toLowerCase().contains(
														"enc")|| nextLine[i].toLowerCase().contains( 
																"kh-internes-kennzeichen") || nextLine[i].toLowerCase().contains(
																		"fallnummer")) { 
									table.getItems()[i].setText(3,
											"EncounterID"); 
									foundEnc = true;
									continue;
								} else if (!foundUpdate
										&& table.getItems()[i].getText(2)
										.equals("Date") 
										&& nextLine[i].toLowerCase().contains(
												"update")) { 
									table.getItems()[i]
											.setText(3, "UpdateDate"); 
									foundUpdate = true;
									continue;
								} else if (!foundImp
										&& table.getItems()[i].getText(2)
										.equals("Date") 
										&& nextLine[i].toLowerCase().contains(
												"import")) { 
									table.getItems()[i]
											.setText(3, "ImportDate"); 
									foundImp = true;
									continue;
								} else if (!foundDownl
										&& table.getItems()[i].getText(2)
										.equals("Date") 
										&& nextLine[i].toLowerCase().contains(
												"download")) { 
									table.getItems()[i].setText(3,
											"DownloadDate"); 
									foundDownl = true;
									continue;
								} else if (!foundSource
										&& nextLine[i].toLowerCase().contains(
												"source")) { 
									table.getItems()[i].setText(3,
											"SourceSystem"); 
									foundSource = true;
									continue;
								} else if (!foundStart
										&& table.getItems()[i].getText(2)
										.equals("Date") 
										&& nextLine[i].toLowerCase().contains(
												"start")) { 
									table.getItems()[i].setText(3, "StartDate"); 
									foundStart = true;
									continue;
								}
								else if (!foundEnd
										&& table.getItems()[i].getText(2)
										.equals("Date")
										&& nextLine[i].toLowerCase().contains(
												"enddatum")) {
									table.getItems()[i].setText(3, "EndDate");
									foundEnd = true;
									continue;
								}
							} 
							saveTable();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				}
			});

			sashForm.setWeights(new int[] { 192, 379 });
			setControl(container);
			setPageComplete(true);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Clear all metadata from the current table.
	 */
	private static void clearMetaDataFromTable() {
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
			System.out.println("reading: " + csvFolder 
					+ serverListViewer.getTable().getSelection()[0].getText());

			// Read File to generate new Config
			char inputDelim = ';';
			System.out.println("DEFAULTDELIM: .." + DEFAULTDELIM + "..");  
			CSVReader reader = new CSVReader(new FileReader(csvFolder
					+ serverListViewer.getTable().getSelection()[0].getText()),
					inputDelim);

			String[] nextLine = reader.readNext();
			if (nextLine.length == 1) {
				System.err.println("Wrong delimiter?"); 
				if (inputDelim == DEFAULTDELIM) {
					inputDelim = '\t';
					System.err.println("Delimiter set to: \\t"); 
				} else {
					inputDelim = DEFAULTDELIM;

					System.err.println("Delimiter set to: " + DEFAULTDELIM); 
				}
			}
			reader.close();
			reader = new CSVReader(new FileReader(csvFolder
					+ serverListViewer.getTable().getSelection()[0].getText()),
					inputDelim);

			DEFAULTDELIM = inputDelim;
			TableItem[] items = table.getItems();
			List<String> names = new LinkedList<String>();
			for (TableItem item : items) {
				System.out.println("item: " + item.getText(1));
				names.add(item.getText(1));
			}
			table.removeAll();
			String[] line1 = reader.readNext();
			for (int i = 0; i < line1.length; i++) {
				final TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, line1[i]);
				item.setText(1, names.get(i));
				item.setText(2, ""); 
				item.setText(3, ""); 
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	/**
	 * Clears the current table.
	 */
	private static void clearTable() {
		try {
			table.removeAll();
			Log.addLog(0, "Deleted " 
					+ serverListViewer.getTable().getSelection()[0].getText());

			// Read File to generate new Config
			File newConf = new File(csvFolder
					+ serverListViewer.getTable().getSelection()[0].getText());
			char inputDelim = ';';
			CSVReader reader = new CSVReader(new FileReader(newConf),
					inputDelim);

			String[] nextLine = reader.readNext();
			if (nextLine.length == 1) {
				System.err.println("Wrong delimiter?"); 
				if (inputDelim == DEFAULTDELIM) {
					inputDelim = '\t';
					System.err.println("Delimiter set to: \\t"); 
				} else {
					inputDelim = DEFAULTDELIM;
					System.err.println("Delimiter set to: " + DEFAULTDELIM); 
				}
			}
			reader.close();
			reader = new CSVReader(new FileReader(newConf), inputDelim);
			DEFAULTDELIM = inputDelim;
			String[] line1 = reader.readNext();
			for (String element : line1) {
				final TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, element);
				item.setText(1, ""); 
				item.setText(2, ""); 
				item.setText(3, ""); 
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		System.out.println(fileConfigMap.get(serverListViewer.getTable()
				.getSelection()[0].getText()));
	}

	/**
	 * Saves the Table to disc.
	 */
	public static void saveTable() {
		System.out.println("lastTable: " + lastTable); 
		System.out.println("fileconfigmap: " + fileConfigMap); 
		System.out.println("table.isDisposed() " + table.isDisposed()); 
		System.out.println("fileConfigMap.get(lastTable) " 
				+ fileConfigMap.get(lastTable));
		System.out.println();
		if ((fileConfigMap.get(lastTable) != null) && !table.isDisposed()) {
			System.out.println("writing cfg to: " + csvFolder 
					+ fileConfigMap.get(lastTable));

			File tmpTableFile = new File(csvFolder
					+ fileConfigMap.get(lastTable));
			try {
				CSVWriter rotatedOutput = new CSVWriter(new FileWriter(
						tmpTableFile), DEFAULTDELIM);
				System.out.println("save table: ::" + DEFAULTDELIM + "::");  
				TableItem[] tableItems = table.getItems();
				String[] nextLine = new String[tableItems.length + 1];

				nextLine[0] = "Spaltenname (Pflicht)"; 
				if (tableItems != null) {
					nextLine[0] = "Spaltenname (Pflicht)"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(0);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "Datentyp (Pflicht)"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(2);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "Name (kann leer sein)"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(1);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "Metainformationen"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(3);
					}
					rotatedOutput.writeNext(nextLine);

					nextLine[0] = "PID-Generator"; 
					for (int i = 0; i < tableItems.length; i++) {
						nextLine[i + 1] = tableItems[i].getText(4);
					}
					rotatedOutput.writeNext(nextLine);
				}
				rotatedOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	@Override
	public IWizardPage getPreviousPage() {
		if (lastTable != null) {
			saveTable();
		}
		return super.getPreviousPage();
	}

}
