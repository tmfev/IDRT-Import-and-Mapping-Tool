package de.umg.mi.idrt.ioe.OntologyTree;

import java.awt.Color;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import de.umg.mi.idrt.ioe.ActionCommand;
import de.umg.mi.idrt.ioe.Application;
import de.umg.mi.idrt.ioe.Resource;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class GUIDialogTest2 extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public GUIDialogTest2(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setMinimumSize(new Point(400, 300));
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(3, false));
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(0, 0, 76, 21);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(0, 0, 75, 25);
		btnNewButton.setText("ok");

		
		shell.pack();
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setText("cancel");
		shell.open();

	}
}
