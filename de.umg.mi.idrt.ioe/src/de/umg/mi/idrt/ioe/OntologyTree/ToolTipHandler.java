/*\
|*| Copyright (C) 2009 Tresys Technology, LLC
|*| License: refer to COPYING file for license information.
|*| Author:     Norman Patrick
|*| 
|*| $Rev$
|*| $Date$
\*/
package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * @author sa-user
 *
 */
public class ToolTipHandler implements MouseMoveListener
{
	private IToolTipEditor parentEditor;
	
	private Shell parentShell;

	private Shell tipShell;

	private Label tipLabelTitle, tipLabelSeparator, tipLabelText;

	private Widget tipWidget; // widget this tooltip is hovering over

	private Point tipPosition; // the position being hovered over

	/**
	 * Creates a new tooltip handler
	 * 
	 * @param parent
	 *            the parent Shell
	 */
	public ToolTipHandler(IToolTipEditor editor)
	{
		this.parentEditor = editor;
		this.parentShell = editor.getShell ();
		final Display display = parentShell.getDisplay();

		tipShell = new Shell(parentShell, SWT.ON_TOP | SWT.TOOL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginWidth = 2;
		gridLayout.marginHeight = 2;
		tipShell.setLayout(gridLayout);

		tipShell.setBackground(display
			.getSystemColor(SWT.COLOR_INFO_BACKGROUND));

		tipLabelTitle = new Label(tipShell, SWT.NONE);
		tipLabelTitle.setForeground(display
			.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipLabelTitle.setBackground(display
			.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipLabelTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
			| GridData.VERTICAL_ALIGN_CENTER));
		
		FontData[] fontData = tipLabelTitle.getFont ().getFontData ();
		
		for (int i = 0; i < fontData.length; i++)
		{
			fontData[i].setStyle (fontData[i].getStyle () | SWT.BOLD);
		}
		
		Font titleFont = new Font(display, fontData);
		tipLabelTitle.setFont (titleFont);
		

		tipLabelSeparator = new Label(tipShell, SWT.SEPARATOR | SWT.HORIZONTAL);
		tipLabelSeparator.setForeground(display
			.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipLabelSeparator.setBackground(display
			.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipLabelSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
			| GridData.VERTICAL_ALIGN_CENTER));

		tipLabelText = new Label(tipShell, SWT.NONE);
		tipLabelText.setForeground(display
			.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipLabelText.setBackground(display
			.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipLabelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
			| GridData.VERTICAL_ALIGN_CENTER));
		
		this.activateHoverHelp (editor.getToolTipControl ());
	}

	/**
	 * Enables customized hover help for a specified control
	 * 
	 * @control the control on which to enable hoverhelp
	 */
	private void activateHoverHelp(final Control control) {
		
		control.addMouseMoveListener (this);
		
		/*
		 * Get out of the way if we attempt to activate the control
		 * underneath the tooltip
		 */
		control.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (tipShell.isVisible())
					tipShell.setVisible(false);
			}
		});

		/*
		 * Trap hover events to pop-up tooltip
		 */
		control.addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseExit(MouseEvent e) {
				if (tipShell.isVisible())
					tipShell.setVisible(false);
				tipWidget = null;
			}

			public void mouseHover(MouseEvent event) {
				Point pt = new Point(event.x, event.y);
				Widget widget = event.widget;
				//          if (widget instanceof ToolBar) {
				//            ToolBar w = (ToolBar) widget;
				//            widget = w.getItem(pt);
				//          }
				//          if (widget instanceof Table) {
				//            Table w = (Table) widget;
				//            widget = w.getItem(pt);
				//          }
				//          if (widget instanceof Tree) {
				//            Tree w = (Tree) widget;
				//            widget = w.getItem(pt);
				//          }
				if (widget == null) {
					tipShell.setVisible(false);
					tipWidget = null;
					return;
				}
//				if (widget == tipWidget)
//					return;
				tipWidget = widget;
				tipPosition = control.toDisplay(pt);
				String title = parentEditor.getToolTipTitle (pt);
				String text = parentEditor.getToolTipText(pt);
				tipLabelTitle.setText(title); // accepts null
				tipLabelText.setText(text != null ? text : "");
				setHoverLocation(tipShell, tipPosition);

				tipLabelSeparator.setVisible (tipLabelText.getText ().length () > 0);
				tipLabelText.setVisible (tipLabelText.getText ().length () > 0);

				tipShell.pack();

				if (tipLabelText.getText ().length () == 0)
				{
					Point size = tipShell.getSize ();
					size.y = tipLabelTitle.getSize ().y + 6;
					tipShell.setSize (size);
				}
				
				if (tipLabelTitle.getText ().length () > 0)
					tipShell.setVisible(true);
			}
		});

		/*
		 * Trap F1 Help to pop up a custom help box
		 */
		control.addHelpListener(new HelpListener() {
			public void helpRequested(HelpEvent event) {
				if (tipWidget == null)
					return;
				ToolTipHelpTextHandler handler = (ToolTipHelpTextHandler) tipWidget
				.getData("TIP_HELPTEXTHANDLER");
				if (handler == null)
					return;
				String text = handler.getHelpText(tipWidget);
				if (text == null)
					return;

				if (tipShell.isVisible()) {
					tipShell.setVisible(false);
					Shell helpShell = new Shell(parentShell, SWT.SHELL_TRIM);
					helpShell.setLayout(new FillLayout());
					Label label = new Label(helpShell, SWT.NONE);
					label.setText(text);
					helpShell.pack();
					setHoverLocation(helpShell, tipPosition);
					helpShell.open();
				}
			}
		});
	}

	/**
	 * Sets the location for a hovering shell
	 * 
	 * @param shell
	 *            the object that is to hover
	 * @param position
	 *            the position of a widget to hover over
	 * @return the top-left location for a hovering box
	 */
	private void setHoverLocation(Shell shell, Point position) {
		Rectangle displayBounds = shell.getDisplay().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shellBounds.x = Math.max(Math.min(position.x, displayBounds.width
			- shellBounds.width), 0);
		shellBounds.y = Math.max(Math.min(position.y + 16,
			displayBounds.height - shellBounds.height), 0);
		shell.setBounds(shellBounds);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove (MouseEvent event)
	{
		Point pt = new Point(event.x, event.y);
		pt = ((Control)event.widget).toDisplay (pt);
		
		if (tipShell.isVisible() && !pt.equals (this.tipPosition))
			tipShell.setVisible(false);
	}
}