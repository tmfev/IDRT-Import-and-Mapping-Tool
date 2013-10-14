package de.umg.mi.idrt.ioe.OntologyTree;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * @author Christian Bauer
 *         <christian(dot)bauer(at)med(dot)uni-goettingen(dot)de> Department of
 *         Medical Informatics Goettingen www.mi.med.uni-goettingen.de
 * 
 *         drag listener for getting data from the source tree to the target tree
 *         
 */

public class NodeMoveDragListener implements DragSourceListener {

	private final TreeViewer viewer;
private OntologyTreeNode firstElement;

	public NodeMoveDragListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		System.out.println("dragFinished");
		firstElement.removeFromParent();
//		viewer.expandAll();
		viewer.refresh();
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		System.out.println("dragSetData");
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		firstElement = (OntologyTreeNode) selection
				.getFirstElement();
		event.data = firstElement.getTreePath();
		event.doit=true;

	}

	@Override
	public void dragStart(DragSourceEvent event) {


		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		OntologyTreeNode firstElement = (OntologyTreeNode) selection
				.getFirstElement();
		System.out.println("DRAGGED: " + firstElement.getName());
		if (firstElement != null) {
			event.doit=true;
		}
		else {
			event.doit = false;
		}
	}

}