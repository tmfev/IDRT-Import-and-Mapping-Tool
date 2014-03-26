package de.umg.mi.idrt.ioe.view;

import java.util.ArrayList;
import javax.swing.tree.MutableTreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeSubNode;
import de.umg.mi.idrt.ioe.OntologyTree.TargetNodeAttributes;

/**
 * @author Christian Bauer <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> 
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 * 			Department of Medical Informatics Goettingen 
 * 			www.mi.med.uni-goettingen.de
 *         main class managing and giving access to the source and target trees
 */

public class EditorTargetInfoView extends ViewPart {

	private static Composite parentPane;
	private static MutableTreeNode node;
	private static Composite _editorComposite;
	private static Composite _parent;
	private static Table infoTable;
	private static TableColumn infoTableDBColumn;
	private static TableColumn infoTableValue;
	private static TableColumn infoTableBtn;

	private static TableItem addColumItem(String text, boolean grayed, final int row) {
		final TableItem item = new TableItem(infoTable, SWT.NONE);
		if (grayed) {
			item.setForeground(Application.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		}

		item.setText(new String[] { text, "" });

		if (!grayed) {
			Button button = new Button(infoTable, SWT.PUSH);
			button.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/remove.gif"));
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (node instanceof OntologyTreeNode) {
						final OntologyTreeNode treeNode = (OntologyTreeNode) node;
						if (treeNode!=null) {
							TargetNodeAttributes attributes = treeNode.getTargetNodeAttributes();

							item.setText(1, "");
							switch (row) {
							case 1:
								treeNode.getTargetNodeAttributes().setName("");
								treeNode.setName("");
								break;
							case 3:
								treeNode.getTargetNodeAttributes().setStartDateSourcePath("");
								break;
							case 4:
								treeNode.getTargetNodeAttributes().setEndDateSourcePath("");
								break;
							case 5:
								//VA
								break;
							case 7:
								attributes.getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_METADATAXML,"");
								break;
							case 10:
								attributes.getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_COMMENT,"");
								break;
							case 11:
								attributes.getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_TOOLTIP,"");
								break;
							case 16:
								attributes.getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.VALUETYPE_CD,"");
								break;
							default:
								break;
							}
							OntologyEditorView.getTargetTreeViewer().update(treeNode, null);
						}
					}
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			button.pack();
			infoTableBtn.setWidth(button.getBounds().width);
			//			buttons.add(button);
			TableEditor editor = new TableEditor(infoTable);
			editor.minimumWidth = button.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(button, item, 2);
		}
		return item;
	}

	public static TableItem addValueItem(TableItem[] items, int row, String value) {
		if (items[row] == null) {
			Console.error("Could not add an item to a table in EditorSourceInfoView, because there was no row #"
					+ row + ".");
			return null;
		}

		TableItem item = items[row];
		item.setText(1, value != null && !value.equals("null") ? value : "");

		return item;
	}

	private static void createTable() {

		if (infoTable != null)
			infoTable.dispose();

		infoTable = new Table(_parent, SWT.BORDER | SWT.MULTI);
		infoTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		infoTable.setHeaderVisible(true);
		infoTable.setLinesVisible(true);
//		infoTable.setTopIndex(4);


		final TableEditor editor = new TableEditor(infoTable);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		infoTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent event) {
				if (node instanceof OntologyTreeNode) {

					final OntologyTreeNode treeNode = (OntologyTreeNode) node;

					Control old = editor.getEditor();
					if (old != null) {
						old.dispose();
					}
					Point pt = new Point(event.x, event.y);

					final TableItem item = infoTable.getItem(pt);


					if (item != null) {
						int row = infoTable.getSelectionIndex();
						String column = infoTable.getItem(row).getText(0);
						final int col = 1;

						if (column.equalsIgnoreCase("c_name")) { //row==1;
							final Text text = new Text(infoTable, SWT.NONE);
							text.setForeground(item.getForeground());
							text.setText(treeNode.getName());
							text.setForeground(item.getForeground());
							text.selectAll();
							text.setFocus();
							editor.minimumWidth = text.getBounds().width;
							editor.setEditor(text, item, col);

							text.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(ModifyEvent event) {
									item.setText(col, text.getText());
									treeNode.getTargetNodeAttributes().setName(text.getText());
									treeNode.setName(text.getText());
									OntologyEditorView.getTargetTreeViewer().update(treeNode, null);
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
						//						else if (row == 5) { //VA DROPBOX
						//
						//							final CCombo combo = new CCombo(infoTable,
						//									SWT.READ_ONLY);
						//							if (treeNode.getTargetNodeAttributes().getVisualattribute().toLowerCase().contains("la")||
						//									treeNode.getTargetNodeAttributes().getVisualattribute().toLowerCase().contains("fa")) {
						//								combo.add("LA ");
						//								combo.add("FA ");
						//							}
						//							else if (treeNode.getTargetNodeAttributes().getVisualattribute().toLowerCase().contains("ra")||
						//									treeNode.getTargetNodeAttributes().getVisualattribute().toLowerCase().contains("da")) {
						//								combo.add("RA ");
						//								combo.add("DA ");
						//							}
						//							combo.setText(treeNode.getTargetNodeAttributes().getVisualattribute());
						//							combo.select(combo.indexOf(item.getText(col)));
						//							editor.minimumWidth = combo.computeSize(
						//									SWT.DEFAULT, SWT.DEFAULT).x;
						//							combo.setFocus();
						//							editor.setEditor(combo, item, col);
						//							combo.addSelectionListener(new SelectionAdapter() {
						//								@Override
						//								public void widgetSelected(SelectionEvent event) {
						//									item.setText(col, combo.getText());
						//									System.out.println("SETTING " + combo.getText());
						//									treeNode.getTargetNodeAttributes().setVisualattributes(combo.getText());
						//									OntologyEditorView.getTargetTreeViewer().update(treeNode, null);
						//									combo.dispose();
						//								}
						//							});
						//						} 	
						else if (column.equalsIgnoreCase("c_metadataxml")) {//(row == 7) { //METADATAXML
							final Text text = new Text(infoTable, SWT.NONE);
							text.setForeground(item.getForeground());
							if (treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_METADATAXML)!=null)
							if (!treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_METADATAXML).equals("null"))
								text.setText(treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_METADATAXML));
							else
								text.setText("");
							text.setForeground(item.getForeground());
							text.selectAll();
							text.setFocus();
							editor.minimumWidth = text.getBounds().width;
							editor.setEditor(text, item, col);

							text.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(ModifyEvent event) {
									item.setText(col, text.getText());
									treeNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_METADATAXML, text.getText());
									//									treeNode.setName(text.getText());
									OntologyEditorView.getTargetTreeViewer().update(treeNode, null);
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
						}	else if (column.equalsIgnoreCase("c_comment")) {//(row == 10) { //COMMENT
							final Text text = new Text(infoTable, SWT.NONE);
							text.setForeground(item.getForeground());
							if (!treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COMMENT).equals("null"))
								text.setText(treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COMMENT));
							else
								text.setText("");
							text.setForeground(item.getForeground());
							text.selectAll();
							text.setFocus();
							editor.minimumWidth = text.getBounds().width;
							editor.setEditor(text, item, col);

							text.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(ModifyEvent event) {
									item.setText(col, text.getText());
									treeNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_COMMENT, text.getText());
									OntologyEditorView.getTargetTreeViewer().update(treeNode, null);
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
						} 	else if (column.equalsIgnoreCase("c_tooltip")) {//(row == 11) { //TOOLTIP
							final Text text = new Text(infoTable, SWT.NONE);
							text.setForeground(item.getForeground());
							if (!treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_TOOLTIP).equals("null"))
								text.setText(treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_TOOLTIP));
							else
								text.setText("");
							text.setForeground(item.getForeground());
							text.selectAll();
							text.setFocus();
							editor.minimumWidth = text.getBounds().width;
							editor.setEditor(text, item, col);

							text.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(ModifyEvent event) {
									item.setText(col, text.getText());
									treeNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.C_TOOLTIP, text.getText());
									OntologyEditorView.getTargetTreeViewer().update(treeNode, null);
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
						else if (column.equalsIgnoreCase("valuetype_cd")) {//(row == 16) { //VALUETYPE_CD
							final Text text = new Text(infoTable, SWT.NONE);
							text.setForeground(item.getForeground());
							if (!treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.VALUETYPE_CD).equals("null")) {
								text.setText(treeNode.getTargetNodeAttributes().getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.VALUETYPE_CD));
							}
							else
								text.setText("");
							text.setForeground(item.getForeground());
							text.selectAll();
							text.setFocus();
							editor.minimumWidth = text.getBounds().width;
							editor.setEditor(text, item, col);

							text.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(ModifyEvent event) {
									item.setText(col, text.getText());
									treeNode.getTargetNodeAttributes().getTargetNodeMap().put(Resource.I2B2.NODE.TARGET.VALUETYPE_CD, text.getText());
									OntologyEditorView.getTargetTreeViewer().update(treeNode, null);
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
					}
				}
			}
		});

		infoTableDBColumn = new TableColumn(infoTable, SWT.NONE);
		infoTableDBColumn.setWidth(170);
		infoTableDBColumn.setText("Attribute");

		infoTableValue = new TableColumn(infoTable, SWT.NONE);
		infoTableValue.setWidth(600);
		infoTableValue.setText("Value");

		infoTableBtn = new TableColumn(infoTable, SWT.NONE);
		infoTableBtn.setResizable(false);
		
		
		// infoTableValue.
		int row = 0;
		
		boolean grayed = node instanceof OntologyTreeNode;
		addColumItem(Resource.I2B2.NODE.TARGET.C_NAME,!grayed,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.C_TOOLTIP,!grayed,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.C_COMMENT,!grayed,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.C_METADATAXML,!grayed,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.VALUETYPE_CD,!grayed,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.STARTDATE_STAGING_PATH,!grayed,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.ENDDATE_STAGING_PATH,!grayed,row++);
		
		addColumItem(Resource.I2B2.NODE.TARGET.UPDATE_DATE,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.IMPORT_DATE,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.VISUALATTRIBUTES,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.STAGING_PATH,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.STAGING_DIMENSION,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.C_BASECODE,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.C_COLUMNDATATYPE,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.C_OPERATOR,true,row++);
		addColumItem(Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD,true,row++);
		addColumItem("TREE_PATHLEVEL",true,row++);
		addColumItem("TREE_PATH",true,row++);
	}

	public static void executeRefresh() {

		if (parentPane == null) {
			return;
		}

		if (node instanceof OntologyTreeNode) {
			OntologyTreeNode treeNode = (OntologyTreeNode)node;

			createTable();
			TargetNodeAttributes attributes = treeNode.getTargetNodeAttributes();

			if (!infoTable.isDisposed()) {
				TableItem[] items = infoTable.getItems();

				int row = 0;
				ArrayList<String> stagingPathList = new ArrayList<String>();
				for (OntologyTreeSubNode subNode : attributes.getSubNodeList()) {
					stagingPathList.add(subNode.getStagingPath());
				}
				addValueItem(items, row++, String.valueOf(treeNode.getName()));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_TOOLTIP));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COMMENT));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_METADATAXML));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.VALUETYPE_CD));
				addValueItem(items, row++,attributes.getStartDateSource());
				addValueItem(items, row++,attributes.getEndDateSource());
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.UPDATE_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.IMPORT_DATE));
				addValueItem(items, row++,attributes.getVisualattribute());
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH));
				addValueItem(items, row++,stagingPathList.toString());
				addValueItem(items, row++,attributes.getDimension());
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_BASECODE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COLUMNDATATYPE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_OPERATOR));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD));

				addValueItem(items, row++,""+treeNode.getTreePathLevel());
				addValueItem(items, row++,treeNode.getTreePath());

				_editorComposite.update();
				_editorComposite.layout();
				_parent.layout();
			}
		}
		else if (node instanceof OntologyTreeSubNode) {
			OntologyTreeSubNode treeNode = (OntologyTreeSubNode)node;

			createTable();

			TargetNodeAttributes attributes = treeNode.getParent().getTargetNodeAttributes();

			if (!infoTable.isDisposed()) {
				TableItem[] items = infoTable.getItems();

				int row = 0;

				addValueItem(items, row++, treeNode.getStagingPath());

				addValueItem(items, row++, String.valueOf(treeNode.getStagingName()));
				addValueItem(items, row++,
						String.valueOf(attributes.getDimension()));
				addValueItem(items, row++,
						String.valueOf(treeNode.getTargetSubNodeAttributes().getStartDateSource()));
				addValueItem(items, row++,
						String.valueOf(treeNode.getTargetSubNodeAttributes().getEndDateSource()));
				addValueItem(items, row++,
						String.valueOf(attributes.getVisualattribute()));

				addValueItem(items, row++, attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_BASECODE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_METADATAXML));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COLUMNDATATYPE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_OPERATOR));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_COMMENT));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.C_TOOLTIP));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.UPDATE_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.IMPORT_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.VALUETYPE_CD));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH));

				addValueItem(items, row++,""+treeNode.getParent().getTreePathLevel());
				addValueItem(items, row++,treeNode.getParent().getTreePath());


				_editorComposite.update();
				_editorComposite.layout();
				_parent.layout();
			}
		}
	}

	public static void refresh() {

		// Editor von Benjamin aus CSVWizard klauen

		Display display = Display.getCurrent();

		if (display == null) {
			// Bad, no display for this thread => we are not in (a) UI thread
			// display.syncExec(new Runnable() {void run() { gc = new
			// GC(display);}});

			if (PlatformUI.getWorkbench().getDisplay() != null) {
				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
					public void run() {
						executeRefresh();
					}
				});

			} else if (Application.getDisplay() != null) {
				Application.getDisplay().syncExec(new Runnable() {
					public void run() {
						executeRefresh();
					}
				});
			} 
		} else {
			new Runnable() {
				public void run() {
					executeRefresh();
				}
			};
			executeRefresh();
		}
	}

	public static void setNode(MutableTreeNode newNode) {// , List<String> answersList,
		// MyOntologyTreeItemLists
		// itemLists){
		// Console.info("setting node");
		//		System.out.println("setting node (" + node.getName() + ")");
		node = newNode;
		refresh();
	}

	//	private void createColumns(final Composite parent, final TableViewer viewer) {
	//		String[] titles = { "First name", "Last name", "Gender", "Married" };
	//		int[] bounds = { 100, 100, 100, 100 };
	//
	//		// First column is for the first name
	//		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
	//		col.setLabelProvider(new ColumnLabelProvider() {
	//			@Override
	//			public String getText(Object element) {
	//				// Person p = (Person) element;
	//				// return p.getFirstName();
	//				return "firstname";
	//			}
	//		});
	//
	//		/*
	//		 * @Override public Image getImage(Object element) { if (((Person)
	//		 * element).isMarried()) { return CHECKED; } else { return UNCHECKED; }
	//		 * }
	//		 */
	//		// });
	//
	//	}

	@Override
	public void createPartControl(Composite parent) {
		_parent = parent;
		parent.setLayout(new GridLayout(1, false));

		// createInfoGroup();




		_editorComposite = new Composite(parent, SWT.NONE);
		_editorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		_editorComposite.setLayout(new FillLayout(SWT.VERTICAL));

		createTable();

		// item.setImage (image);

		parentPane = parent.getParent();
	}
	//
	//	private TableViewerColumn createTableViewerColumn(String title, int bound,
	//			final int colNumber) {
	//		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
	//				SWT.NONE);
	//		final TableColumn column = viewerColumn.getColumn();
	//		column.setText(title);
	//		column.setWidth(bound);
	//		column.setResizable(true);
	//		column.setMoveable(true);
	//		// column.addSelectionListener(getSelectionAdapter(column, colNumber));
	//		return viewerColumn;
	//	}

	//	private void createViewer(Composite parent) {
	//		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
	//				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
	////		createColumns(parent, viewer);
	//		final Table table = viewer.getTable();
	//		table.setHeaderVisible(true);
	//		table.setLinesVisible(true);
	//
	//		viewer.setContentProvider(new ArrayContentProvider());
	//		// Get the content for the viewer, setInput will call getElements in the
	//		// contentProvider
	//		// viewer.setInput(ModelProvider.INSTANCE.getPersons());
	//		// Make the selection available to other views
	//		getSite().setSelectionProvider(viewer);
	//
	//		// Layout the viewer
	//		GridData gridData = new GridData();
	//		gridData.verticalAlignment = GridData.FILL;
	//		gridData.horizontalSpan = 2;
	//		gridData.grabExcessHorizontalSpace = true;
	//		gridData.grabExcessVerticalSpace = true;
	//		gridData.horizontalAlignment = GridData.FILL;
	//		viewer.getControl().setLayoutData(gridData);
	//	}

	@Override
	public void setFocus() {

	}

}
