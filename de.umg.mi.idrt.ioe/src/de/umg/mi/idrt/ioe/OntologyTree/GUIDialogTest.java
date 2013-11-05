package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import de.umg.mi.idrt.ioe.view.OntologyEditorView;

public class GUIDialogTest extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public GUIDialogTest(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		
		
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		
		

		Shell hoverInsertShell = new Shell(shell, SWT.NONE);
		
		Composite hoverInsert = new Composite(hoverInsertShell, SWT.NONE);
		
				hoverInsert.setLayout(new org.eclipse.swt.layout.GridLayout(2, false));

		Composite actionMenu = new Composite(shell, SWT.NONE);

		actionMenu.setLayout(new org.eclipse.swt.layout.GridLayout(2, false));

		final Button btnInsert = new Button(actionMenu, SWT.NONE);
		btnInsert.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool", "images/edit-copy.png"));
		btnInsert.setBounds(0, 0, 75, 25);
		btnInsert.setText("insert nodes here");
		btnInsert.addSelectionListener(new SelectionAdapter() {
			// SelectionAdapter Methode
			public void widgetSelected(SelectionEvent e) {

				shell.close();
				OntologyEditorView.getTargetTreeViewer().expandAll();
				OntologyEditorView.getTargetTreeViewer().refresh();
			}
		});
		
		
		//btnInsert.addMouseMoveListener( new ToolTipHandler (null) );
		
		
		
		
		/*
		btnInsert.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				
				Shell tip = null;
				Label label = null;
				
				Random r = new Random(System.currentTimeMillis());
				Point p = shell.getSize();
				int newX = r.nextInt(p.y);
				int newY = r.nextInt(p.x);
				btnInsert.setBounds(newX - 55, newY - 25, 55, 25);

				
				
				
				//TableItem item = table.getItem (new Point (event.x, event.y));
				if (item != null) {
					if (tip != null  && !tip.isDisposed ()) tip.dispose ();
					tip = new Shell (shell, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
					//tip.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
					
					FillLayout layout = new FillLayout ();
					layout.marginWidth = 2;
					tip.setLayout (layout);
					label = new Label (tip, SWT.NONE);
					//label.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
					//label.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
					label.setData ("_TABLEITEM", item);
					label.setText (item.getText ());
					//label.addListener (SWT.MouseExit, labelListener);
					//label.addListener (SWT.MouseDown, labelListener);
					Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
					Rectangle rect = item.getBounds (0);
					//Point pt = table.toDisplay (rect.x, rect.y);
					//tip.setBounds (pt.x, pt.y, size.x, size.y);
					tip.setVisible (true);
				}
				
			}

		});

		*/

		Button btnCombine = new Button(actionMenu, SWT.NONE);
		btnCombine.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		btnCombine.setImage(ResourceManager.getPluginImage(
				"edu.goettingen.i2b2.importtool",
				"images/format-indent-more.png"));
		btnCombine.setBounds(0, 0, 75, 25);
		btnCombine.setText("combine nodes here");
		btnCombine.addSelectionListener(new SelectionAdapter() {
			// SelectionAdapter Methode
			public void widgetSelected(SelectionEvent e) {
				// nothing
			}
		});
		
		actionMenu.pack();
		
		text = new Text(actionMenu, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(actionMenu, SWT.NONE);

		
		shell.pack();
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		composite.setBounds(10, 0, 89, 97);
		shell.open();

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
}
