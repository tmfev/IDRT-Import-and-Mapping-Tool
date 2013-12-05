package de.umg.mi.idrt.ioe.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.umg.mi.idrt.idrtimporttool.importidrt.ResourceManager;
import de.umg.mi.idrt.ioe.commands.OntologyEditor.CombineNodesCommand;
import de.umg.mi.idrt.ioe.misc.Regex;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import swing2swt.layout.BorderLayout;
import org.eclipse.swt.layout.GridLayout;

/**
 * @author Benjamin Baum <benjamin(dot)baum(at)med(dot)uni-goettingen(dot)de>
 *         Department of Medical Informatics Goettingen
 *         www.mi.med.uni-goettingen.de
 */
public class RegexWizardPage1 extends WizardPage {
	private TableColumn column_1;
	private TableColumn column_2;
	private TableViewer viewer;
	private Map<Object, Button> buttons;
	private List<Regex> regexs;

	public RegexWizardPage1() {
		super("Edit Regular Expressions");
		setTitle("Edit Regular Expressions");
		setDescription("Edit Regular Expressions");
	}

	@Override
	public void createControl(Composite parent) {
		try {
			CombineNodesCommand.clear();
			Regex.loadRegex();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new BorderLayout(0, 0));
		Composite composite = new Composite(comp, SWT.NONE);
		composite.setLayoutData(BorderLayout.SOUTH);
		composite.setLayout(new GridLayout(1, false));
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setBounds(0, 0, 75, 25);
		button_1.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/add.gif"));
		button_1.setToolTipText("Add Regular Expression");
		button_1.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CombineNodesCommand.addRegEx(new Regex("new","new","c_basecode"));
				setInput();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		Composite composite_1 = new Composite(comp, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		viewer = new TableViewer(composite_1,SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setColumnProperties(new String[] {});
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.setContentProvider(new ArrayContentProvider());

		Table table = viewer.getTable();
		//		column_2 = new TableColumn(viewer.getTable(), SWT.NONE);

		//		column_2.setWidth(50);
		TableViewerColumn saveCol = new TableViewerColumn(viewer, SWT.NONE);

		final TextCellEditor textCellEditor = new TextCellEditor(viewer.getTable(),SWT.NONE);
		TableViewerColumn nameCol = new TableViewerColumn(viewer, SWT.NONE);
		nameCol.setEditingSupport(new EditingSupport(viewer) {

			protected boolean canEdit(Object element) {
				if (!((Regex)element).getName().isEmpty() && !((Regex)element).getRegex().isEmpty())
					return true;
				else
					return false;
			}

			protected CellEditor getCellEditor(Object element) {
				return textCellEditor;
			}

			protected Object getValue(Object element) {
				return ((Regex) element).getName();
			}

			protected void setValue(Object element, Object value) {
				((Regex)element).setName((String)value);
				viewer.update(element, null);
			}
		});




		TableViewerColumn regexCol = new TableViewerColumn(viewer, SWT.NONE);
		regexCol.setEditingSupport(new EditingSupport(viewer) {

			protected boolean canEdit(Object element) {
				if (!((Regex)element).getName().isEmpty() && !((Regex)element).getRegex().isEmpty())
					return true;
				else
					return false;
			}

			protected CellEditor getCellEditor(Object element) {
				return textCellEditor;
			}

			protected Object getValue(Object element) {
				return ((Regex) element).getRegex();
			}

			protected void setValue(Object element, Object value) {
				((Regex)element).setRegex((String)value);
				viewer.update(element, null);
			}
		});
		
		TableViewerColumn regexTable = new TableViewerColumn(viewer, SWT.NONE);
		regexTable.setEditingSupport(new EditingSupport(viewer) {

			protected boolean canEdit(Object element) {
				if (!((Regex)element).getName().isEmpty() && !((Regex)element).getRegex().isEmpty())
					return true;
				else
					return false;
			}

			protected CellEditor getCellEditor(Object element) {
				return textCellEditor;
			}

			protected Object getValue(Object element) {
				return ((Regex) element).getTable();
			}

			protected void setValue(Object element, Object value) {
				((Regex)element).setTable((String)value);
				viewer.update(element, null);
			}
		});


		final TableViewerColumn testCol = new TableViewerColumn(viewer, SWT.NONE);
		testCol.setEditingSupport(new EditingSupport(viewer) {
			protected boolean canEdit(Object element) {
				if (!((Regex)element).getName().isEmpty() && !((Regex)element).getRegex().isEmpty())
					return true;
				else
					return false;
			}

			protected CellEditor getCellEditor(Object element) {
				return textCellEditor;
			}

			protected Object getValue(Object element) {
				return ((Regex) element).getTest();
			}

			protected void setValue(Object element, Object value) {
				((Regex)element).setTest((String)value);
				viewer.update(element, null);
				viewer.refresh();
			}
		});

		TableViewerColumn checkCol = new TableViewerColumn(viewer, SWT.NONE);
		//		comp.setLayout(new FillLayout(SWT.HORIZONTAL));

		buttons = new HashMap<Object, Button>();


		column_2 = saveCol.getColumn();
		column_2.setResizable(false);
		column_2.setWidth(50);
		saveCol.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public void update(final ViewerCell cell) {

				TableItem item = (TableItem) cell.getItem();
				final Button button;
				if (buttons.containsKey(cell.getElement())) {
					button = buttons.get(cell.getElement());
				}
				else {
					button = new Button(viewer.getTable(),SWT.PUSH);
					button.setImage(ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/remove.gif"));
					button.setToolTipText("Remove Regular Expression");
					button.setData(((Regex)cell.getElement()));

					button.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							Regex regex = (Regex) button.getData();
							CombineNodesCommand.removeRegex(regex);
							button.dispose();
							setInput();
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
						}
					});
					buttons.put(cell.getElement(), button);
				}
				TableEditor editor = new TableEditor(item.getParent());
				editor.grabHorizontal = true;
				editor.grabVertical = true;
				editor.setEditor(button , item, cell.getColumnIndex());
				//				button.pack();
				//				editor.layout();
			}

		});
		TableColumn column = nameCol.getColumn();
		column.setMoveable(true);
		column.setText("Name");
		column.setWidth(100);
		nameCol.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {
				Regex p = (Regex)element;
				return p.getName();
			}

		});
		column_1 = regexCol.getColumn();
		column_1.setMoveable(true);
		column_1.setText("Regex");
		column_1.setWidth(300);
		regexCol.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {
				Regex p = (Regex)element;
				return p.getRegex();
			}

		});
		
		

		TableColumn regexTableCol = regexTable.getColumn();
		regexTableCol.setMoveable(true);
		regexTableCol.setText("DB Table");
		regexTableCol.setWidth(100);
		regexTable.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {
				return ((Regex) element).getTable();
			}

		});
		
		TableColumn test = testCol.getColumn();
		test.setMoveable(true);
		test.setText("Test String");
		test.setWidth(100);
		testCol.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {
				return ((Regex) element).getTest();
			}

		});
		TableColumn check = checkCol.getColumn();
		check.setMoveable(true);
		check.setText("Check");
		check.setWidth(100);
		check.setAlignment(SWT.CENTER);
		checkCol.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public Image getImage(Object element) {
				if (!((Regex)element).getRegex().isEmpty()) {
					if (((Regex)element).check())
						return ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/itemstatus-checkmark16.png");
					else if (!((Regex)element).getTest().isEmpty())
						return ResourceManager.getPluginImage("de.umg.mi.idrt.ioe", "images/remove-grouping.png");
					else
						return null;
				}
				else {
					return null;
				}
			}
			@Override
			public String getText(Object element) {
				return "";
			}
		});
		//		column_2.pack();
		setInput();
		setControl(parent);
		setPageComplete(true);
	}

	private void setInput() {
		for (Button b : buttons.values()) {
			b.dispose();
		}
		buttons.clear();
		regexs = new ArrayList<Regex>();
		for (Regex regex : CombineNodesCommand.getRegex()) {
			regexs.add(regex);
		}

		//		regexs.add(new Regex("", ""));
		viewer.setInput(regexs);
		viewer.refresh();
	}
}
