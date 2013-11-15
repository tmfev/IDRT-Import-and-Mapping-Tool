package de.umg.mi.idrt.ioe.view;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.umg.mi.idrt.ioe.Activator;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.SystemMessage;

public class StatusView extends ViewPart {
	Composite _parent = null;
	private static boolean  pressed = false;
	private static Label _label;
	private static StringBuffer _output = new StringBuffer();;
	private static ScrolledComposite _sc;
	private static Composite _pane;
	private static Table _table;
	private static TableColumn _tableColumn;
	private static Color _colorInfo;
	private static Color _colorSuccess;
	private static Color _colorError;
	private static TableColumn _tableColumn2;
	private static SystemMessage _message;

	public StatusView() {
		super();
		Activator.getDefault().getResource().setStatusView(this);

	}

	public static void addErrorMessage(String message) {
		addMessage(new SystemMessage(message, SystemMessage.MessageType.ERROR,
				SystemMessage.MessageLocation.MAIN));
	}

	public static void addMessage(String message) {
		addMessage(new SystemMessage(message, SystemMessage.MessageType.INFO,
				SystemMessage.MessageLocation.MAIN));
	}

	/*
	 * public TestViewComposite(final Composite parent, int style) {
	 * super(parent, style);
	 * 
	 * GridLayout gridLayout = new GridLayout(1, true);
	 * this.setLayout(gridLayout); //Test-Inhalt for(int i = 1; i < 15; i++) new
	 * Button(this, SWT.PUSH).setText("Blubber"); }
	 */

	public static void addMessage(String message, SystemMessage.MessageType messageType) {
		addMessage(new SystemMessage(message, messageType,
				SystemMessage.MessageLocation.MAIN));
	}

	public void addMessage(String message,
			SystemMessage.MessageType messageType,
			SystemMessage.MessageLocation messageLocation) {
		addMessage(new SystemMessage(message, messageType, messageLocation));
	}

	public static void addMessage(SystemMessage message) {

		Console.message(message.getMessageText());

		// use field for easy access in subfunction
		_message = message;

		// run asynchron to avoid GUI conflicts

		Application.getDisplay().syncExec(new Runnable() {

			public void run() {

				if (_table == null) {
					return;
				}

				TableItem tableItem = new TableItem(_table, SWT.NONE, 0);
				tableItem.setText(new String[] {
						new SimpleDateFormat("HH:mm:ss").format(new Date(System
								.currentTimeMillis())),
						_message.getMessageText() });

				if (_message.getMessageType().equals(
						SystemMessage.MessageType.INFO)) {
					tableItem.setBackground(0, _colorInfo);
				} else if (_message.getMessageType().equals(
						SystemMessage.MessageType.SUCCESS)) {
					tableItem.setBackground(0, _colorSuccess);
				} else {
					tableItem.setBackground(0, _colorError);
				}

				tableItem.setBackground(1, _colorInfo);

				refresh();
			}
		});
	}

	public static void addSuccsessMessage(String message) {
		addMessage(new SystemMessage(message,
				SystemMessage.MessageType.SUCCESS,
				SystemMessage.MessageLocation.MAIN));
	}

	@Override
	public void createPartControl(Composite parent) {
		// parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		_parent = parent;

		_table = new Table(parent, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100));
		_table.setLayout(tlayout);

		_tableColumn = new TableColumn(_table, SWT.NONE);
		_tableColumn.setResizable(false);
		_tableColumn.setText("time");
		_tableColumn.setResizable(true);

		_tableColumn2 = new TableColumn(_table, SWT.NONE);
		_tableColumn2.setResizable(false);
		_tableColumn2.setText("message");

		/*
		 * TableColumnLayout tableColumnLayout = new TableColumnLayout();
		 * tableColumnLayout.setColumnData(_tableColumn, new
		 * ColumnWeightData(300));
		 */

		_colorInfo = new Color(Display.getCurrent(), 255, 255, 255);
		_colorSuccess = new Color(Display.getCurrent(), 204, 251, 196);
		_colorError = new Color(Display.getCurrent(), 255, 228, 225);

		/*
		 * TableItem tableItem = new TableItem(_table, SWT.NONE);
		 * tableItem.setText("New TableItem");
		 * tableItem.setBackground(_colorError);
		 */

		// this.addMessage("program started");
		refresh();
	}

	public static void refresh() {

		_tableColumn.pack();
		_tableColumn2.pack();
		// _table.update();
	}

	public void update() {

		Color col = new Color(Display.getCurrent(), 0, 255, 0);

		_sc.setBackground(col);


		_label.redraw();
		_pane.redraw();
		_pane.layout();
		_sc.setMinSize(_pane.getSize());

		if (_sc != null) {
			_sc.layout();
			_sc.redraw();
			// _sc1.setMinSize(_sc1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

		// _sc.setSize(size);
		_parent.layout();
		_parent.redraw();

	}

	@Override
	public void setFocus() {
		
	}

	/*
	 * private void refreshExecution(){ //Debug.f("refreshExecution",this);
	 * 
	 * 
	 * 
	 * _parent.setLayout(new FillLayout());
	 * 
	 * // Create the ScrolledComposite to scroll horizontally and vertically if
	 * (_sc == null) _sc = new ScrolledComposite(_parent, SWT.V_SCROLL);
	 * 
	 * // Create a child composite to hold the controls if (_pane == null){
	 * _pane = new Composite(_sc, SWT.NONE); _pane.setLayout(new FillLayout());
	 * }
	 * 
	 * if (_label == null){ _label = new Label(_pane, SWT.FILL);
	 * _label.setLayoutData(new FillLayout()); //Color col = new
	 * Color(Display.getCurrent(), 200, 200, 200); //_label.setBackground(col);
	 * _label.setForeground(new Color(Display.getCurrent(), 0, 0, 0)); }
	 * _label.setText(_output.toString());
	 * 
	 * 
	 * 
	 * 
	 * // Expand both horizontally and vertically _sc.setExpandHorizontal(true);
	 * _sc.setExpandVertical(true); _sc.setAlwaysShowScrollBars(true);
	 * 
	 * 
	 * _pane.setSize(_label.getSize()); _pane.layout(); _pane.redraw();
	 * _pane.update();
	 * 
	 * _sc.setContent(_pane); _sc.setMinSize(_label.getSize()); _sc.layout();
	 * _sc.redraw(); _sc.update();
	 * 
	 * _parent.layout(); _parent.redraw(); _parent.update();
	 * 
	 * }
	 */
}
