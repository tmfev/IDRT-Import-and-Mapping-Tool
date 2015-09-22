package de.umg.mi.idrt.imt.transmart;

import java.util.Iterator;

import javax.swing.tree.MutableTreeNode;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.events.TypedEvent;

import de.umg.mi.idrt.ioe.Console;
import de.umg.mi.idrt.ioe.view.OntologyEditorView;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         drag listener for getting data from the source tree to the target tree
 *         
 */

public class TransmartNodeDragListener implements DragSourceListener {

	private TreeViewer viewer;
	private ISelection selection;
	public TransmartNodeDragListener(TreeViewer viewer) {
		this.viewer = viewer;
	
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		Console.info("Finshed Drag");
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		Console.info("dragSetData2");
		event.doit=true;
		selection = viewer.getSelection();
		event.data=(IStructuredSelection) selection;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (selection.getFirstElement() instanceof TransmartConfigTreeItem) {
			TransmartConfigTreeItem firstElement = (TransmartConfigTreeItem) selection
					.getFirstElement();
				Console.info("Start Drag");
				event.doit = true;
			
		}
		
	}

}