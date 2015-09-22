package de.umg.mi.idrt.ioe.view;

import java.awt.MouseInfo;

import javax.swing.tree.MutableTreeNode;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;

import de.umg.mi.idrt.imt.transmart.TransmartConfigStyledLabelProvider;
import de.umg.mi.idrt.imt.transmart.TransmartConfigTree;
import de.umg.mi.idrt.imt.transmart.TransmartConfigTreeContentProvider;
import de.umg.mi.idrt.imt.transmart.TransmartConfigTreeModelProvider;
import de.umg.mi.idrt.imt.transmart.TransmartTreeContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.StyledViewTableLabelProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TreeStagingContentProvider;

public class TransmartConfigEditorView extends ViewPart{
	
	
	
	public TransmartConfigEditorView() {
	}
	private static TreeViewer stagingTreeViewer;
	@Override
	public void createPartControl(Composite parent) {
		
		TransmartConfigTree tree = new TransmartConfigTree();
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(composite, SWT.NONE);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite transmartOntology = new Composite(sashForm, SWT.NONE);
		transmartOntology.setLayout(new FillLayout(SWT.HORIZONTAL));
		sashForm.setWeights(new int[] {1, 1});
		
		stagingTreeViewer = new TreeViewer(transmartOntology, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		final Listener stagingTreeLabelListener = new Listener () {
			public void handleEvent (Event event) {
				Label label = (Label)event.widget;
				Shell shell = label.getShell ();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event ();
					e.item = (TreeItem) label.getData ("_TABLEITEM");
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					stagingTreeViewer.getTree().setSelection (new TreeItem [] {(TreeItem) e.item});
					stagingTreeViewer.getTree().notifyListeners (SWT.Selection, e);
					shell.dispose ();
					stagingTreeViewer.getTree().setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose ();
					break;
				}
			}
		};

		Listener stagingTreeListener = new Listener () {
			Shell tip = null;
			Label label = null;
			public void handleEvent (Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null) break;
					tip.dispose ();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					Point coords = new Point(event.x, event.y);
					TreeItem item = stagingTreeViewer.getTree().getItem(coords);
					MutableTreeNode nodse = null;
					String tooltip ="";
					if (item!=null) {
						if (item.getData() instanceof OntologyTreeNode) {
							nodse = (OntologyTreeNode) item.getData();
							if (((OntologyTreeNode) nodse).getOntologyCellAttributes().getC_TOOLTIP() != null)
								tooltip=((OntologyTreeNode) nodse).getOntologyCellAttributes().getC_TOOLTIP();
						}

						if (!tooltip.isEmpty()) {
							int columns = stagingTreeViewer.getTree().getColumnCount();

							for (int i = 0; i < columns || i == 0; i++) {
								if (item.getBounds(i).contains(coords)) {
									if (tip != null  && !tip.isDisposed ())
										tip.dispose ();
									tip = new Shell (stagingTreeViewer.getTree().getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
									tip.setBackground (stagingTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_BACKGROUND));
									FillLayout layout = new FillLayout ();
									layout.marginWidth = 2;
									tip.setLayout (layout);
									label = new Label (tip, SWT.NONE);
									label.setForeground (stagingTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_FOREGROUND));
									label.setBackground (stagingTreeViewer.getTree().getDisplay().getSystemColor (SWT.COLOR_INFO_BACKGROUND));
									label.setData ("_TABLEITEM", item);
									label.setText(tooltip);
									label.addListener (SWT.MouseExit, stagingTreeLabelListener);
									label.addListener (SWT.MouseDown, stagingTreeLabelListener);
									Point size = tip.computeSize (SWT.DEFAULT, SWT.DEFAULT);
									tip.setBounds (MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y-size.y, size.x, size.y);
									tip.setVisible (true);
									break;
								}
							}
						}
					}
				}
				}
			}
		};
		stagingTreeViewer.getTree().addListener (SWT.Dispose, stagingTreeListener);
		stagingTreeViewer.getTree().addListener (SWT.KeyDown, stagingTreeListener);
		stagingTreeViewer.getTree().addListener (SWT.MouseMove, stagingTreeListener);
		stagingTreeViewer.getTree().addListener (SWT.MouseHover, stagingTreeListener);
		stagingTreeViewer.setContentProvider(new TransmartConfigTreeContentProvider());		
		stagingTreeViewer.setLabelProvider(new TransmartConfigStyledLabelProvider());
		stagingTreeViewer.setInput(new TransmartConfigTreeModelProvider().getModel());
		stagingTreeViewer.expandToLevel(2);
	}

	@Override
	public void setFocus() {
		
	}
}
