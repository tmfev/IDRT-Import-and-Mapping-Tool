package de.umg.mi.idrt.imt.transmart;

import static org.junit.Assert.*;

import java.awt.MouseInfo;

import javax.swing.tree.MutableTreeNode;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;

import de.umg.mi.idrt.idrtimporttool.server.Settings.I2b2ProjectTransferType;
import de.umg.mi.idrt.imt.transmart.TransmartConfigStyledLabelProvider;
import de.umg.mi.idrt.imt.transmart.TransmartConfigTree;
import de.umg.mi.idrt.imt.transmart.TransmartConfigTreeContentProvider;
import de.umg.mi.idrt.imt.transmart.TransmartConfigTreeModelProvider;
import de.umg.mi.idrt.imt.transmart.TransmartTreeContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDragListener;
import de.umg.mi.idrt.ioe.OntologyTree.NodeDropListener;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeContentProvider;
import de.umg.mi.idrt.ioe.OntologyTree.OntologyTreeNode;
import de.umg.mi.idrt.ioe.OntologyTree.StyledViewTableLabelProvider;
import de.umg.mi.idrt.ioe.OntologyTree.TreeStagingContentProvider;
import de.umg.mi.idrt.ioe.view.EditorTargetInfoView;

import org.junit.Before;
import org.junit.Test;
import org.eclipse.swt.widgets.Tree;

public class TransmartConfigTreeTest {
	private static TreeViewer stagingTreeViewer;
	private TransmartConfigTree tree;
	@Before
	public void setUp() throws Exception {
		tree = new TransmartConfigTree();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Test
	public void testTransmartConfigTree() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		
	
//		TransmartConfigTreeItem item = new TransmartConfigTreeItem("DELETED", 1);
//		assertTrue(TransmartConfigTree.getRoot().getChildren().size()==5);
//		assertTrue(TransmartConfigTree.getRoot().addNode(item));
//		assertTrue(TransmartConfigTree.getRoot().getChildren().size()==6);
//		assertTrue(tree.removeNode(item));
//		assertTrue(TransmartConfigTree.getRoot().getChildren().size()==5);
		
		SashForm sashForm = new SashForm(shell, SWT.NONE);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Tree tree_1 = new Tree(composite_1, SWT.BORDER);
		
		TreeItem trtmNewTreeitem = new TreeItem(tree_1, SWT.NONE);
		trtmNewTreeitem.setText("DIVI.csv");
		
		TreeItem trtmNewTreeitem_3 = new TreeItem(trtmNewTreeitem, SWT.NONE);
		trtmNewTreeitem_3.setText("KHSAufnahme");
		
		TreeItem trtmNewTreeitem_4 = new TreeItem(trtmNewTreeitem, SWT.NONE);
		trtmNewTreeitem_4.setText("Station");
		trtmNewTreeitem.setExpanded(true);
		
		Composite transmartOntology = new Composite(sashForm, SWT.NONE);
		sashForm.setWeights(new int[] {1, 1});
		transmartOntology.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		stagingTreeViewer = new TreeViewer(transmartOntology, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);

		stagingTreeViewer.setContentProvider(new TransmartConfigTreeContentProvider());		
		stagingTreeViewer.setLabelProvider(new TransmartConfigStyledLabelProvider());
		stagingTreeViewer.setInput(new TransmartConfigTreeModelProvider().getModel());
		
		
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() , TransmartTransferType.getInstance()};
		TransmartNodeDropListener nodeDropListener = new TransmartNodeDropListener(stagingTreeViewer);
		TransmartNodeDragListener nodeDragListener = new TransmartNodeDragListener(stagingTreeViewer);
		stagingTreeViewer.addDragSupport(operations, transferTypes, nodeDragListener);
		stagingTreeViewer.addDropSupport(operations, transferTypes, nodeDropListener);
		stagingTreeViewer.getTree().addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selected " + e);
				if (e.item.getData() instanceof TransmartConfigTreeItem) {
					TransmartConfigTreeItem node = (TransmartConfigTreeItem) e.item.getData();
						System.out.println(node.getName());
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
//		stagingTreeViewer.getTree().addSelecti)
		shell.layout();
//		stagingTreeViewer.expandToLevel(2);
		// run the event loop as long as the window is open
				while (!shell.isDisposed()) {
				    // read the next OS event queue and transfer it to a SWT event 
				  if (!display.readAndDispatch())
				   {
				  // if there are currently no other OS event to process
				  // sleep until the next OS event is available 
				    display.sleep();
				   }
				}
				display.dispose(); 		
	}


}
