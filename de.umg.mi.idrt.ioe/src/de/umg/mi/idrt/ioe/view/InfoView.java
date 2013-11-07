package de.umg.mi.idrt.ioe.view;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.Debug;
import de.umg.mi.idrt.ioe.Resource;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.TargetNodeAttributes;

public class InfoView extends ViewPart {

	private Resource _resource = null;
	private String _text = "";
	private Composite parentPane;

	OntologyTreeNode _node = null;
	private Composite targetNodeComposite;
	private Table _infoTable;
	private TableColumn infoTableDBColumn;
	private TableColumn infoTableValue;
	private TableItem tableItem;
	private TableCursor tableCursor;
	private TableViewer viewer;
	private Composite composite;
	public InfoView() {
	}

	private TableItem addColumItem(String text) {
		TableItem item = new TableItem(_infoTable, SWT.NONE);
		item.setText(new String[] { text, "" });
		return item;
	}

	public TableItem addValueItem(TableItem[] items, int row, String value) {
		if (items[row] == null) {
			Debug.e("Could not add an item to a table in EditorSourceInfoView, because there was no row #"
					+ row + ".");
			return null;
		}
		TableItem item = items[row];
		item.setText(1, value != null && !value.equals("null") ? value : "");
		return item;
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
		
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(composite, SWT.NONE);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		
		targetNodeComposite = new Composite(sashForm, SWT.NONE);
		sashForm.setWeights(new int[] {1, 1});
		createTable();
	}
	
	private void createTable() {

		if (_infoTable != null)
			return;

		int[] bounds = { 100, 100 };
		targetNodeComposite.setLayout(new GridLayout(1, false));

		_infoTable = new Table(targetNodeComposite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.VIRTUAL);
		_infoTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_infoTable.setHeaderVisible(true);
		_infoTable.setLinesVisible(true);

		final TableEditor editor = new TableEditor(_infoTable);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		_infoTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent event) {

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

					
					Console.info("row = " + row + " / col = " + col);
					
					// Source Path
					if (row == 0) {
						String path = item.getText(1);
						OntologyTreeNode node = OntologyEditorView.getOntologyStagingTree().getNodeLists().getNodeByPath(path);
						System.out.println("NODE SELECTED: " + node.getName());
						node.setHighlighted(true);
//						OntologyEditorView.getStagingTreeViewer().getLabelProvider().
						System.out.println(node.getName());
//						ISelection selection = new 
//						OntologyEditorView.getStagingTreeViewer().setSelection(selection);
						System.out.println("Editor: Source Path");
						final Text text = new Text(_infoTable, SWT.NONE);

						text.setForeground(item.getForeground());
						text.setText(item.getText(1));
						// item.getText(0));
						//text.setForeground(item.getForeground());
						text.selectAll();
						text.setFocus();
						editor.minimumWidth = text.getBounds().width;
						editor.setEditor(text, item, col);

						text.addModifyListener(new ModifyListener() {
							@Override
							public void modifyText(ModifyEvent event) {
								Console.info("ModifyListener: modifyText");
								item.setText(col, text.getText());
							}
						});
						text.addFocusListener(new FocusListener() {
							@Override
							public void focusGained(FocusEvent e) {
								Console.info("FocusListener: focusGained");
							}

							@Override
							public void focusLost(FocusEvent e) {
								Console.info("FocusListener: focusLost");
								item.setText(col, text.getText());
								text.dispose();
							}
						});

					}

					// The "nice name" of the column.
					if (row == 1) {
						final Text text = new Text(_infoTable, SWT.NONE);
						text.setForeground(item.getForeground());
						text.setText(item.getText(col));
						text.setForeground(item.getForeground());
						text.selectAll();
						text.setFocus();
						editor.minimumWidth = text.getBounds().width;
						editor.setEditor(text, item, col);

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
					} else if (column == 112) {
						final CCombo combo = new CCombo(_infoTable,
								SWT.READ_ONLY);

						// i2b2 Metadata of the column (e.g. patient id)
					} else if (column == 113) {
					}

				}
			}
		});

		infoTableDBColumn = new TableColumn(_infoTable, SWT.NONE);
		infoTableDBColumn.setWidth(170);
		infoTableDBColumn.setText("column");

		infoTableValue = new TableColumn(_infoTable, SWT.NONE);
		infoTableValue.setWidth(600);
		infoTableValue.setText("value");
		// infoTableValue.

		addColumItem(Resource.I2B2.NODE.TARGET.STAGING_PATH);
		addColumItem(Resource.I2B2.NODE.TARGET.NAME);
		addColumItem(Resource.I2B2.NODE.TARGET.STAGING_DIMENSION);
		addColumItem(Resource.I2B2.NODE.TARGET.STARTDATE_STAGING_PATH);
		addColumItem(Resource.I2B2.NODE.TARGET.ENDDATE_STAGING_PATH);
		addColumItem(Resource.I2B2.NODE.TARGET.VISUALATTRIBUTES);

		targetNodeComposite.layout();
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

	public Resource getResource() {
		return this._resource;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public void refresh() {
		System.out.println("executeRefresh for text:\"" + this._node.getName()
				+ "\"");

		if (parentPane == null) {
			Debug.e("no pane avaible @OntologyNodeEditorView");
			return;
		}

		createTable();

		TargetNodeAttributes attributes = _node.getTargetNodeAttributes();

		TableItem[] items = _infoTable.getItems();

		int row = 0;

		addValueItem(items, row++, String.valueOf(attributes.getSourcePath()));
		addValueItem(items, row++, String.valueOf(_node.getName()));
//		addValueItem(items, row++,
//				String.valueOf(attributes.isChanged() == true ? "true" : false));
		addValueItem(items, row++,
				String.valueOf(attributes.getStartDateSource()));
		addValueItem(items, row++,
				String.valueOf(attributes.getEndDateSource()));
		addValueItem(items, row++,
				String.valueOf(attributes.getVisualattribute()));
		composite.layout();
		targetNodeComposite.layout();

	}

	public void setComposite(Composite pane) {
		Debug.f("setComposite", this);

		refresh();
	}

	@Override
	public void setFocus() {
	}

//	public void refresh() {
//		Debug.f("refresh", this);
//
//		// Editor von Benjamin aus CSVWizard klauen
//
//		Display display = Display.getCurrent();
//
//		if (display == null) {
//			// Bad, no display for this thread => we are not in (a) UI thread
//			// display.syncExec(new Runnable() {void run() { gc = new
//			// GC(display);}});
//			Debug.e("no display");
//
//			if (PlatformUI.getWorkbench().getDisplay() != null) {
//				Debug.d("display by PlatformUI");
//				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
//					public void run() {
//						executeRefresh();
//					}
//				});
//
//			} else if (Application.getDisplay() != null) {
//				Debug.d("display by Acitvator");
//				Application.getDisplay().syncExec(new Runnable() {
//					public void run() {
//						executeRefresh();
//					}
//				});
//			} else {
//				Debug.e("no Display (final)");
//			}
//		} else {
//			new Runnable() {
//				public void run() {
//					executeRefresh();
//				}
//			};
//			executeRefresh();
//		}
//	}

	public void setNode(OntologyTreeNode node) {// , List<String> answersList,
												// MyOntologyTreeItemLists
												// itemLists){
		// Debug.f("setNode",this);
		// Console.info("setting node");
		System.out.println("setting node (" + node.getName() + ")");
		_node = node;
		refresh();
	}

	public void setResource(Resource resource) {
		this._resource = resource;
	}
	
}
