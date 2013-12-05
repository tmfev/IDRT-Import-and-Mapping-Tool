package de.umg.mi.idrt.ioe.view;

import java.util.ArrayList;

import javax.swing.tree.MutableTreeNode;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

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

	private static Resource _resource = null;
	private static String _text = "";
	private static Composite parentPane;

	private static MutableTreeNode _node;
	private static Composite _editorComposite;
	private static Composite _parent;
	private static Table _infoTable;
	private static TableColumn infoTableDBColumn;
	private static TableColumn infoTableValue;
	private static TableItem tableItem;
	private static TableCursor tableCursor;
	private static TableViewer viewer;

	private static TableItem addColumItem(String text) {
		TableItem item = new TableItem(_infoTable, SWT.NONE);
		item.setText(new String[] { text, "" });
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

		if (_infoTable != null)
			return;

		int[] bounds = { 100, 100 };

		_infoTable = new Table(_parent, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.VIRTUAL);
		_infoTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_infoTable.setHeaderVisible(true);
		_infoTable.setLinesVisible(true);

		// _infoTable.getnCount().get

		// editor as mouse listener

		final TableEditor editor = new TableEditor(_infoTable);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		_infoTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent event) {
				if (_node instanceof OntologyTreeNode) {

					final OntologyTreeNode treeNode = (OntologyTreeNode) _node;
					Console.info("Mouse pressed in the target info view ... (mouseDown)");

					Control old = editor.getEditor();
					if (old != null) {
						old.dispose();
					}
					Point pt = new Point(event.x, event.y);

					Console.info(" ... at position: " + event.x + "/" + event.y
							+ "!");

					final TableItem item = _infoTable.getItem(pt);

					Console.info((item != null ? " ... the item " + item.getText()
							+ " was found at this position"
							: " ... no item was found at this position"));

					if (item != null) {
						int column = -1;
						int row = -1;

						for (int i = 0, n = _infoTable.getColumnCount(); i < n; i++) {
							Rectangle rect = item.getBounds(i);
							System.out.println("position1: " + rect.height + "/"
									+ rect.width + "!");

							if (rect.contains(pt)) {
								column = i;
								break;
							}
						}

						// detect the row where the mouse was clicked
						for (int i = 0, n = _infoTable.getItemCount(); i < n; i++) {
							Rectangle rect = item.getBounds(i);
							if (rect.contains(pt)) {
								row = i;
								break;
							}
						}

						System.out.println("position2: " + column + "/" + row + "("
								+ _infoTable.getSelectionIndex() + ")!");

						row = _infoTable.getSelectionIndex();
						final int col = 1;
						String path = item.getText(1);
						final OntologyTreeNode node = OntologyEditorView.getOntologyStagingTree().getNodeLists().getNodeByPath(path);

						Console.info("row = " + row + " / col = " + col);

						// The "nice name" of the column.
						if (row == 1) {
							final Text text = new Text(_infoTable, SWT.NONE);
							text.setForeground(item.getForeground());
							//						text.setText(item.getText(col));
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
							// Datatype of the column
						} else if (column == 112) {
							final CCombo combo = new CCombo(_infoTable,
									SWT.READ_ONLY);

							/*
							 * for (String element : ConfigMetaData.optionsData) {
							 * combo.add(element); }
							 * combo.setText(item.getText(column));
							 * combo.select(combo.indexOf(item.getText(column)));
							 * editor.minimumWidth = combo.computeSize( SWT.DEFAULT,
							 * SWT.DEFAULT).x; combo.setFocus();
							 * editor.setEditor(combo, item, column);
							 */
							/*
							 * final int col = column;
							 * combo.addSelectionListener(new SelectionAdapter() {
							 * 
							 * @Override public void widgetSelected(SelectionEvent
							 * event) { item.setText(col, combo.getText());
							 * combo.dispose(); } });
							 */
							// i2b2 Metadata of the column (e.g. patient id)
						} else if (column == 113) {
							/*
							 * String[] optionMetas = ConfigMetaData.getMetaCombo(
							 * table.getItems(), item.getText(column)); final CCombo
							 * combo = new CCombo(table, SWT.READ_ONLY); for (String
							 * optionMeta : optionMetas) { combo.add(optionMeta); }
							 * combo.select(combo.indexOf(item.getText(column)));
							 * editor.minimumWidth = combo.computeSize( SWT.DEFAULT,
							 * SWT.DEFAULT).x; combo.setFocus();
							 * editor.setEditor(combo, item, column); final int col
							 * = column; combo.addSelectionListener(new
							 * SelectionAdapter() {
							 * 
							 * @Override public void widgetSelected(SelectionEvent
							 * event) { item.setText(col, combo.getText());
							 * combo.dispose(); // checkTable(); } });
							 */
							// PID-Generator specific Metadata (e.g. bday, name,
							// lastname...)
							/*
							 * } else if ((column == 4) &&
							 * CSVWizardPageTwo.getBtnRADIOCsvfile() &&
							 * CSVWizardPageTwo.getUsePid()) { final CCombo combo =
							 * new CCombo(table, SWT.READ_ONLY); String[]
							 * optionMetas = ConfigMetaData .getMetaComboPIDGen(
							 * table.getItems(), item.getText(column)); for (String
							 * optionMeta : optionMetas) { combo.add(optionMeta); }
							 * combo.select(combo.indexOf(item.getText(column)));
							 * editor.minimumWidth = combo.computeSize( SWT.DEFAULT,
							 * SWT.DEFAULT).x; combo.setFocus();
							 * editor.setEditor(combo, item, column);
							 * 
							 * final int col = column;
							 * combo.addSelectionListener(new SelectionAdapter() {
							 * 
							 * @Override public void widgetSelected(SelectionEvent
							 * event) { item.setText(col, combo.getText()); if
							 * (!combo.getText().isEmpty()) { item.setText(col - 1,
							 * "ignore"); } else { item.setText(col - 1, ""); }
							 * combo.dispose(); } });
							 */
						}

					}
				}
			}
		});

		infoTableDBColumn = new TableColumn(_infoTable, SWT.NONE);
		infoTableDBColumn.setWidth(170);
		infoTableDBColumn.setText("Attribute");

		infoTableValue = new TableColumn(_infoTable, SWT.NONE);
		infoTableValue.setWidth(600);
		infoTableValue.setText("Value");
		// infoTableValue.

		addColumItem(Resource.I2B2.NODE.TARGET.STAGING_PATH);
		addColumItem(Resource.I2B2.NODE.TARGET.NAME);
		addColumItem(Resource.I2B2.NODE.TARGET.STAGING_DIMENSION);
		addColumItem(Resource.I2B2.NODE.TARGET.STARTDATE_STAGING_PATH);
		addColumItem(Resource.I2B2.NODE.TARGET.ENDDATE_STAGING_PATH);
		addColumItem(Resource.I2B2.NODE.TARGET.VISUALATTRIBUTES);
		
		addColumItem(Resource.I2B2.NODE.TARGET.BASECODE);
		addColumItem(Resource.I2B2.NODE.TARGET.METADATAXML);
		addColumItem(Resource.I2B2.NODE.TARGET.COLUMNDATATYPE);
		addColumItem(Resource.I2B2.NODE.TARGET.OPERATOR);
		addColumItem(Resource.I2B2.NODE.TARGET.COMMENT);
		addColumItem(Resource.I2B2.NODE.TARGET.TOOLTIP);
		addColumItem(Resource.I2B2.NODE.TARGET.UPDATE_DATE);
		addColumItem(Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE);
		addColumItem(Resource.I2B2.NODE.TARGET.IMPORT_DATE);
		addColumItem(Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD);
		addColumItem(Resource.I2B2.NODE.TARGET.VALUETYPE_CD);
		addColumItem(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH);
		
		
		addColumItem("TREE_LEVEL");
		addColumItem("TREE_PATH");
		// TableViewerColumn col =
		// createTableViewerColumn(Resource.I2B2.NODE.TARGET.SOURCE_PATH,
		// bounds[0], 0);



		//tableCursor = new TableCursor(_infoTable, SWT.NONE);

	}

	public static void executeRefresh() {
		//		System.out.println("executeRefresh for text:\"" + _node.getName()
		//				+ "\"");

		if (parentPane == null) {
			return;
		}

		if (_node instanceof OntologyTreeNode) {
			OntologyTreeNode node = (OntologyTreeNode)_node;



			createTable();

			TargetNodeAttributes attributes = node.getTargetNodeAttributes();

			if (!_infoTable.isDisposed()) {
				TableItem[] items = _infoTable.getItems();

				int row = 0;
				ArrayList<String> stagingPathList = new ArrayList<String>();
				for (OntologyTreeSubNode subNode : attributes.getSubNodeList()) {
					stagingPathList.add(subNode.getStagingPath());
				}

				addValueItem(items, row++, stagingPathList.toString());

				addValueItem(items, row++, String.valueOf(node.getName()));
				addValueItem(items, row++,
						String.valueOf(attributes.getDimension()));
				addValueItem(items, row++,
						String.valueOf(attributes.getStartDateSource()));
				addValueItem(items, row++,
						String.valueOf(attributes.getEndDateSource()));
				addValueItem(items, row++,
						String.valueOf(attributes.getVisualattribute()));
				
				addValueItem(items, row++, attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.BASECODE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.METADATAXML));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.COLUMNDATATYPE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.OPERATOR));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.COMMENT));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.TOOLTIP));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.UPDATE_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.IMPORT_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.VALUETYPE_CD));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH));

				addValueItem(items, row++,""+node.getTreePathLevel());
				addValueItem(items, row++,node.getTreePath());
				
				_editorComposite.layout();

				_parent.layout();
			}
		}
		else if (_node instanceof OntologyTreeSubNode) {
			OntologyTreeSubNode node = (OntologyTreeSubNode)_node;



			createTable();

			TargetNodeAttributes attributes = node.getParent().getTargetNodeAttributes();

			if (!_infoTable.isDisposed()) {
				TableItem[] items = _infoTable.getItems();

				int row = 0;

				addValueItem(items, row++, node.getStagingPath());

				addValueItem(items, row++, String.valueOf(node.getStagingName()));
				addValueItem(items, row++,
						String.valueOf(attributes.getDimension()));
				addValueItem(items, row++,
						String.valueOf(node.getTargetSubNodeAttributes().getStartDateSource()));
				addValueItem(items, row++,
						String.valueOf(node.getTargetSubNodeAttributes().getEndDateSource()));
				addValueItem(items, row++,
						String.valueOf(attributes.getVisualattribute()));
				
				addValueItem(items, row++, attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.BASECODE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.METADATAXML));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.COLUMNDATATYPE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.OPERATOR));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.COMMENT));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.TOOLTIP));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.UPDATE_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.DOWNLOAD_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.IMPORT_DATE));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.SOURCESYSTEM_CD));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.VALUETYPE_CD));
				addValueItem(items, row++,attributes.getTargetNodeMap().get(Resource.I2B2.NODE.TARGET.M_APPLIED_PATH));
				
				addValueItem(items, row++,""+node.getParent().getTreePathLevel());
				addValueItem(items, row++,node.getParent().getTreePath());
				
				
				
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

	public static void setNode(MutableTreeNode node) {// , List<String> answersList,
		// MyOntologyTreeItemLists
		// itemLists){
		// Debug.f("setNode",this);
		// Console.info("setting node");
		//		System.out.println("setting node (" + node.getName() + ")");
		_node = node;
		refresh();
	}

	public EditorTargetInfoView() {

	}

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "First name", "Last name", "Gender", "Married" };
		int[] bounds = { 100, 100, 100, 100 };

		// First column is for the first name
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				// Person p = (Person) element;
				// return p.getFirstName();
				return "firstname";
			}
		});

		/*
		 * @Override public Image getImage(Object element) { if (((Person)
		 * element).isMarried()) { return CHECKED; } else { return UNCHECKED; }
		 * }
		 */
		// });

	}

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

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		// column.addSelectionListener(getSelectionAdapter(column, colNumber));
		return viewerColumn;
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		// viewer.setInput(ModelProvider.INSTANCE.getPersons());
		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	public Composite getComposite() {
		return _editorComposite;
	}

	public Resource getResource() {
		return this._resource;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	private boolean hasNode() {
		if (_node != null)
			return true;
		else
			return false;
	}

	public void setComposite(Composite pane) {

		refresh();
	}

	public void setComposite(String text) {

		this._text = text;
		refresh();
	}

	@Override
	public void setFocus() {

	}

	public void setResource(Resource resource) {
		this._resource = resource;
	}
}
